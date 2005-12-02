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
package org.seasar.mayaa.impl.builder.injection;

import org.seasar.mayaa.builder.injection.InjectionChain;
import org.seasar.mayaa.builder.injection.InjectionResolver;
import org.seasar.mayaa.engine.specification.NodeAttribute;
import org.seasar.mayaa.engine.specification.QName;
import org.seasar.mayaa.engine.specification.SpecificationNode;
import org.seasar.mayaa.impl.CONST_IMPL;
import org.seasar.mayaa.impl.ParameterAwareImpl;
import org.seasar.mayaa.impl.engine.specification.SpecificationUtil;
import org.seasar.mayaa.impl.util.StringUtil;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class MetaValuesSetter extends ParameterAwareImpl
        implements InjectionResolver, CONST_IMPL {

    protected static final QName QH_CONTENT = 
        SpecificationUtil.createQName(URI_HTML, "content");
    protected static final QName QH_HTTP_EQUIV = 
        SpecificationUtil.createQName(URI_HTML, "http-equiv");    
    protected static final QName QH_META = 
        SpecificationUtil.createQName(URI_HTML, "meta");
    protected static final QName QX_CONTENT = 
        SpecificationUtil.createQName(URI_XHTML, "content");
    protected static final QName QX_HTTP_EQUIV = 
        SpecificationUtil.createQName(URI_XHTML, "http-equiv");    
    protected static final QName QX_META = 
        SpecificationUtil.createQName(URI_XHTML, "meta");
    
    protected void addMayaaAttribute(
            SpecificationNode original, QName qName, String value) {
        if(original == null || qName == null || StringUtil.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
        SpecificationNode mayaa = SpecificationUtil.getMayaaNode(original);
        if(mayaa == null) {
            throw new IllegalStateException();
        }
        mayaa.addAttribute(qName, value);
    }
    
    protected void setContentValue(SpecificationNode original,
            QName httpEquivName, QName contentName) {
        NodeAttribute equiv = original.getAttribute(httpEquivName);
        if(equiv != null) {
            String equivValue = equiv.getValue();
            NodeAttribute content = original.getAttribute(contentName);
            String contentValue = content.getValue();
            if(StringUtil.hasValue(contentValue)) {
                if("Content-Type".equalsIgnoreCase(equivValue)) {
                    addMayaaAttribute(original, QM_CONTENT_TYPE, contentValue);
                } else if("Pragma".equalsIgnoreCase(equivValue) ||
                        "Cache-Control".equalsIgnoreCase(equivValue)) {
                    if("no-cache".equalsIgnoreCase(contentValue)) {
                        addMayaaAttribute(original, QM_NO_CACHE, "true");
                    }
                }
            }
        }
    }
    
    public SpecificationNode getNode(
            SpecificationNode original, InjectionChain chain) {
        if(original == null || chain == null) {
            throw new IllegalArgumentException();
        }
        QName originalName = original.getQName();
        if(QH_META.equals(originalName)) {
            setContentValue(original, QH_HTTP_EQUIV, QH_CONTENT);
        } else if(QX_META.equals(originalName)) {
            setContentValue(original, QX_HTTP_EQUIV, QX_CONTENT);
        }
        return chain.getNode(original);
    }

}