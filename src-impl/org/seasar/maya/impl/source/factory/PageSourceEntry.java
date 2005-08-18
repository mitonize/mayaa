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
package org.seasar.maya.impl.source.factory;

import org.seasar.maya.impl.CONST_IMPL;
import org.seasar.maya.impl.source.CompositeSourceDescriptor;
import org.seasar.maya.impl.source.JavaSourceDescriptor;
import org.seasar.maya.impl.source.MetaInfSourceDescriptor;
import org.seasar.maya.source.SourceDescriptor;
import org.seasar.maya.source.factory.DescriptorEntry;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class PageSourceEntry implements DescriptorEntry, CONST_IMPL {

    private WebInfSourceEntry _webInfEntry = new WebInfSourceEntry();
    private ContextSourceEntry _contextEntry = new ContextSourceEntry();

    public String getProtocol() {
        return PREFIX_PAGE;
    }

    public SourceDescriptor createSourceDescriptor(String systemID) {
		CompositeSourceDescriptor descriptor = 
		    new CompositeSourceDescriptor(PROTOCOL_PAGE, systemID);
		descriptor.add(_webInfEntry.createSourceDescriptor(systemID));
		descriptor.add(_contextEntry.createSourceDescriptor(systemID));
		descriptor.add(new JavaSourceDescriptor(systemID));
		descriptor.add(new MetaInfSourceDescriptor(systemID));
		return descriptor;
    }

    public void putParameter(String name, String value) {
        throw new UnsupportedOperationException();
    }
    
}
