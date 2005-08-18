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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seasar.maya.impl.util.StringUtil;
import org.seasar.maya.source.SourceDescriptor;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class PageSourceDescriptor implements SourceDescriptor {

	private static final long serialVersionUID = -6124718310228340001L;

    private List _descriptors;
    private String _systemID;
    
    public PageSourceDescriptor(String systemID) {
        _descriptors = new ArrayList();
        _descriptors.add(new ApplicationSourceDescriptor(null, systemID));
        _descriptors.add(new ApplicationSourceDescriptor("/WEB-INF", systemID));
        _descriptors.add(new ClassLoaderSourceDescriptor(null, systemID, null));
        _descriptors.add(new ClassLoaderSourceDescriptor("/META-INF", systemID, null));
        _systemID = StringUtil.preparePath(systemID);
    }

    public String getSystemID() {
        return _systemID;
    }
    
    private SourceDescriptor findDescriptor() {
        for(int i = 0; i < _descriptors.size(); i++) {
            SourceDescriptor descriptor = (SourceDescriptor)_descriptors.get(i);
            if(descriptor.exists()) {
                return descriptor;
            }
        }
        return null;
    }
    
    public boolean exists() {
        return findDescriptor() != null;
    }

    public InputStream getInputStream() {
        SourceDescriptor descriptor = findDescriptor();
        if(descriptor != null) {
            return descriptor.getInputStream();
        }
        return null;
    }
    
    public Date getTimestamp() {
        SourceDescriptor descriptor = findDescriptor();
        if(descriptor != null) {
            return descriptor.getTimestamp();
        }
        return new Date(0);

    }
    
}