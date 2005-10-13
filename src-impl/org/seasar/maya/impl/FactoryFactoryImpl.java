/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.maya.impl;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.maya.FactoryFactory;
import org.seasar.maya.UnifiedFactory;
import org.seasar.maya.impl.factory.UnifiedFactoryHandler;
import org.seasar.maya.impl.source.ClassLoaderSourceDescriptor;
import org.seasar.maya.impl.util.IOUtil;
import org.seasar.maya.impl.util.XMLUtil;
import org.seasar.maya.source.SourceDescriptor;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class FactoryFactoryImpl extends FactoryFactory
        implements CONST_IMPL{

    private static final long serialVersionUID = -1393736148065197812L;
    private static Log LOG = LogFactory.getLog(FactoryFactoryImpl.class);

    protected boolean checkInterface(Class clazz) {
        if(clazz != null && clazz.isInterface() &&
                UnifiedFactory.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }
    
    protected UnifiedFactory createFactory(Class interfaceClass,
            SourceDescriptor source, UnifiedFactory beforeFactory) {
        if(source == null) {
            throw new IllegalArgumentException();
        }
        String systemID = source.getSystemID();
        if(source.exists()) {
            UnifiedFactoryHandler handler = 
                new UnifiedFactoryHandler(interfaceClass, beforeFactory);
            InputStream stream = source.getInputStream();
            try {
                XMLUtil.parse(handler, stream, PUBLIC_FACTORY10,
                        systemID, true, true, false);
            } catch(Throwable t) {
                if(LOG.isErrorEnabled()) {
                    LOG.error("Factory parse error on " + systemID, t);
                }
                return beforeFactory;
            } finally {
                IOUtil.close(stream);
            }
            return handler.getUnifiedFactory();
        }
        return beforeFactory;
    }
    
    protected UnifiedFactory createFactory(
    		Class interfaceClass, Object context) {
    	if(checkInterface(interfaceClass) == false || context == null) {
    		throw new IllegalArgumentException();
    	}
        String systemID = interfaceClass.getName();
        ClassLoaderSourceDescriptor defaultSource =
            new ClassLoaderSourceDescriptor();
        defaultSource.setSystemID(systemID);
        defaultSource.setNeighborClass(UnifiedFactoryHandler.class);
        UnifiedFactory factory = createFactory(
                interfaceClass, defaultSource, null);
        if(factory != null) {
            factory.setUnderlyingContext(context);
        }
        return factory;
    }
    
}
