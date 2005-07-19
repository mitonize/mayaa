package org.seasar.maya.standard.engine.processor.jstl.xml;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.seasar.maya.cycle.AttributeScope;
import org.seasar.maya.cycle.ServiceCycle;
import org.seasar.maya.engine.processor.ProcessorProperty;
import org.seasar.maya.engine.specification.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author maruo_syunsuke
 */
public class ValueProcessor extends org.seasar.maya.standard.engine.processor.jstl.core.OutProcessor {

    private static final long serialVersionUID = 6109454153927428927L;
    
    public void setSelect(final String select) {
        super.setValue(new ProcessorProperty() {
            public QName getQName() {
                return null;
            }
            public String getPrefix() {
                return null;
            }
            public String getLiteral() {
                return getDocumentVariantName(select) ;
            }
            public boolean isDynamic() {
                return true;
            }
            public Object getValue(ServiceCycle cycle) {
                return getNode(cycle,select).getNodeValue() ;
            }
            public void setValue(ServiceCycle cycle, Object value) {
            }
        });
    }
    
    private Document getDocument(ServiceCycle cycle, String docVarName) {
        int scopeSeparaterIndex = docVarName.indexOf(':');
        if(scopeSeparaterIndex >= 0) {
            String scopeName = docVarName.substring(0, scopeSeparaterIndex);
            String attributeName = docVarName.substring(scopeSeparaterIndex + 1);
            AttributeScope scope = cycle.getAttributeScope(scopeName);
            return (Document)scope.getAttribute(attributeName);
        }
        String attributeName = docVarName;
        return (Document)cycle.getAttribute(attributeName);
    }
    
    private String getDocumentVariantName(String select){
        int firstSeparateIndex  = select.indexOf('/');
        return select.substring(0, firstSeparateIndex);
    }
    
    private Node getNode(ServiceCycle cycle, String select) {
        int firstSeparateIndex = select.indexOf('/');
        String docVarName = getDocumentVariantName(select);
        Document document = getDocument(cycle, docVarName);
        String xpathString = docVarName.substring(firstSeparateIndex+1);
        Node node;
        try {
            node = XPathAPI.selectSingleNode(document,xpathString);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return node;
    }

}
