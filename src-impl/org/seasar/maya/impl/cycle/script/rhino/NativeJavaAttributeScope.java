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
package org.seasar.maya.impl.cycle.script.rhino;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.seasar.maya.cycle.AttributeScope;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class NativeJavaAttributeScope extends NativeJavaObject {

	private static final long serialVersionUID = 326309209323166932L;

	private AttributeScope _attrs;
    
    public NativeJavaAttributeScope(Scriptable scope, AttributeScope attrs) {
        super(scope, attrs, Map.class);
        if(attrs == null) {
            throw new IllegalArgumentException();
        }
        _attrs = attrs;
    }
    
    public boolean has(String name, Scriptable start) {
        if(_attrs.hasAttribute(name)) {
            return true;
        }
        return super.has(name, start);
    }    

    public Object get(String name, Scriptable start) {
        if(_attrs.hasAttribute(name)) {
            return _attrs.getAttribute(name);
        }
        return super.get(name, start);
    }

    public void put(String name, Scriptable start, Object value) {
        _attrs.setAttribute(name, value);
    }

    public void delete(String name) {
        if(_attrs.hasAttribute(name)) {
        	_attrs.removeAttribute(name);
        }
    }
    
	public Object[] getIds() {
        Set set = new HashSet();
        for(Iterator it = _attrs.iterateAttributeNames(); it.hasNext(); ) {
        	set.add(it.next());
        }
        Object[] ids = super.getIds();
        for(int i = 0; i < ids.length; i++) {
            Object name = ids[i];
            if(set.contains(name) == false) {
                continue;
            }
            set.add(name);
        }
        return set.toArray(new Object[set.size()]);
    }
    
    public String getClassName() {
        return _attrs.getScopeName() + "Scope";
    }

}