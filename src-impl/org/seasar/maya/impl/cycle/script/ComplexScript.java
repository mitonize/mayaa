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
package org.seasar.maya.impl.cycle.script;

import org.seasar.maya.cycle.script.CompiledScript;
import org.seasar.maya.impl.util.ObjectUtil;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class ComplexScript implements CompiledScript {

 	private static final long serialVersionUID = -7356099026354564155L;

    private Class _expectedType = Object.class;
    private CompiledScript[] _compiled;
    
    public ComplexScript(CompiledScript[] compiled) {
        if(compiled == null) {
            throw new IllegalArgumentException();
        }
        _compiled = compiled;
        for(int i = 0; i < compiled.length; i++) {
            compiled[i].setExpectedType(String.class);
        }
    }
    
    public void setExpectedType(Class expectedType) {
        if(expectedType == null) {
            throw new IllegalArgumentException();
        }
        _expectedType = expectedType;
    }
    
    public Class getExpectedType() {
        return _expectedType;
    }
    
    public Object execute(Object[] args) {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < _compiled.length; i++) {
            buffer.append(_compiled[i].execute(null));
        }
        if(_expectedType == Void.class) {
            return null;
        }
        return ObjectUtil.convert(_expectedType, buffer.toString());
    }

    public void setMethodArgTypes(Class[] methodArgTypes) {
        // do nothing.
    }
    
    public Class[] getMethodArgTypes() {
        return null;
    }

    public boolean isLiteral() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public void assignValue(Object value) {
        throw new ReadOnlyScriptBlockException(toString());
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < _compiled.length; i++) {
            buffer.append(_compiled[i].toString());
        }
        return buffer.toString();
    }
    
}
