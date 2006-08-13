/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.mayaa.impl;


/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class IllegalParameterValueException extends MayaaException {

    private static final long serialVersionUID = -943480597929966946L;

    private Class _parameterized;
    private String _parameterName;

    public IllegalParameterValueException(
            Class parameterized, String parameterName) {
        _parameterized = parameterized;
        _parameterName = parameterName;
    }

    public Class getParameterizedClass() {
        return _parameterized;
    }

    public String getParameterName() {
        return _parameterName;
    }

    protected String[] getMessageParams() {
        String className = "";
        if (_parameterized != null) {
            className = _parameterized.getName();
        }
        return new String[] { className, _parameterName };
    }

}