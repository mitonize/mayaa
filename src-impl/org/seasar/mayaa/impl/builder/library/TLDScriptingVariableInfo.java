/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.mayaa.impl.builder.library;

import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.seasar.mayaa.impl.util.collection.NullIterator;

/**
 * @author Koji Suga (Gluegent, Inc.)
 */
public class TLDScriptingVariableInfo {

    private TagExtraInfo _tei;
    private TagData _tagData;
    private boolean _hasNestedVariable;
    private boolean _hasDynamicName;
    private VariableInfo[] _variableInfos;

    public boolean hasNestedVariable() {
        return _hasNestedVariable;
    }

    public boolean isNestedVariable(String name) {
        if (_hasNestedVariable == false) {
            return false;
        }

        VariableInfo variableInfo = findVariableInfo(name);
        return variableInfo != null
                && variableInfo.getScope() == VariableInfo.NESTED;
    }

    public VariableInfo findVariableInfo(String name) {
        for (Iterator it = variableInfos(); it.hasNext(); ) {
            VariableInfo info = (VariableInfo)it.next();
            if (name.equals(info.getVarName())) {
                return info;
            }
        }
        return null;
    }
    public Iterator variableInfos() {
        VariableInfo[] variableInfos;
        if (_hasDynamicName) {
            variableInfos = _tei.getVariableInfo(_tagData);
        } else {
            variableInfos = _variableInfos;
        }

        if (variableInfos == null) {
            return NullIterator.getInstance();
        }
        return Arrays.asList(variableInfos).iterator();
    }

    public void setTagExtraInfo(TagExtraInfo tei) {
        _tei = tei;
    }

    public void setNestedVariable(boolean hasNestedVariable) {
        _hasNestedVariable = hasNestedVariable;
    }

    public void setDynamicName(boolean hasDynamicName) {
        _hasDynamicName = hasDynamicName;
    }

    public void setTagData(TagData tagData) {
        _tagData = tagData;
    }

    public void setVariableInfos(VariableInfo[] variableInfos) {
        _variableInfos = variableInfos;
    }

}
