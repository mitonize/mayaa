/*
 * Copyright (c) 2004-2005 the Seasar Foundation and the Others.
 *
 * Licensed under the Seasar Software License, v1.1 (aka "the License"); you may
 * not use this file except in compliance with the License which accompanies
 * this distribution, and is available at
 *
 *     http://www.seasar.org/SEASAR-LICENSE.TXT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.seasar.maya.impl.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.seasar.maya.cycle.Application;
import org.seasar.maya.impl.util.FileUtil;
import org.seasar.maya.impl.util.StringUtil;
import org.seasar.maya.source.SourceDescriptor;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class MetaInfCache {

    private Map _entryMap;
    private Set _ignoreNames;

    protected Iterator readLinesFromSource(SourceDescriptor source) {
        if(source == null) {
            throw new IllegalArgumentException();
        }
        if(source.exists() == false) {
            throw new RuntimeException(new FileNotFoundException());
        }
        InputStream stream = source.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List list = new ArrayList();
        try {
            while(true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                if(line.startsWith("#")) {
                    continue;
                }
                line = line.trim();
                if(line.length() > 0) {
                    list.add(line);
                }
            }
            return list.iterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
    }
    
    private void setupIgnoreList() {
        _ignoreNames = new HashSet();
        SourceDescriptor source = new JavaSourceDescriptor(
                "noscan.lst", MetaInfCache.class);
        for(Iterator it = readLinesFromSource(source); it.hasNext(); ) {
            _ignoreNames.add(it.next());
        }
    }

    protected void scanJar(JarFile jarFile) throws IOException {
        for(Enumeration entries = jarFile.entries(); entries.hasMoreElements(); ) {
            JarEntry jarEntry = (JarEntry)entries.nextElement();
            String name = jarEntry.getName();
            if(name.startsWith("META-INF/")) {
                _entryMap.put(name, new Entry(jarFile, jarEntry));
            }
        }
    }

    protected boolean ignoreListContains(String name) {
        for (Iterator it = _ignoreNames.iterator(); it.hasNext(); ) {
            if (name.startsWith(it.next().toString())) {
                return true;
            }
        }
        return false;
    }

    protected void scanJars(File root) {
        _entryMap = new HashMap();
        for(Iterator it = FileUtil.iterateFiles(root, "jar"); it.hasNext(); ) {
        	File jar = (File)it.next();
            if(ignoreListContains(jar.getName())) {
                continue;
            }
            try {
            	scanJar(new JarFile(jar));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }    
    
    public void init(Application application) {
        setupIgnoreList();
        String realPath = application.getRealPath("/WEB-INF/lib");
        scanJars(new File(realPath));
    }
    
    public Entry getMetaInfEntry(String path) {
        if(StringUtil.isEmpty(path)) {
            throw new IllegalArgumentException();
        }
        String name = "META-INF" + StringUtil.preparePath(path);
        return (Entry)_entryMap.get(name);
    }
    
    protected Iterator iterateEntry() {
        return _entryMap.values().iterator();
    }
    
    public class Entry {
        private JarFile _jarFile;
        private JarEntry _jarEntry;

        private Entry(JarFile jarFile, JarEntry jarEntry) {
            if(jarFile == null || jarEntry == null) {
                throw new IllegalArgumentException();
            }
            _jarFile = jarFile;
            _jarEntry = jarEntry;
        }
        
        public String getJarFileName() {
            return _jarFile.getName();
        }

        public String getJarEntryName() {
            return _jarEntry.getName();
        }
        
        public InputStream getInputStream() {
            try {
                return _jarFile.getInputStream(_jarEntry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public long getTime() {
            return _jarEntry.getTime();
        }
        
        public String toString() {
        	return StringUtil.preparePath(getJarFileName()) + 
					StringUtil.preparePath(getJarEntryName());
        }
        
    }
    
}
