package org.intellij.tiles2.filters;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;

/**
 * filter attribute to reference template
 *
 * @author jacky
 */
public class AttributeReferenceTemplateFilter implements ElementFilter {

    public boolean isAcceptable(Object element, PsiElement context) {
        XmlTag xmlTag = PsiTreeUtil.getParentOfType(context, XmlTag.class);
        if (xmlTag != null) {
            String attributeValue = xmlTag.getAttributeValue("value");
            String attributeType = xmlTag.getAttributeValue("type");
            if ((StringUtil.isEmpty(attributeType) && (attributeValue + "").startsWith("/")) || "template".equals(attributeType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassAcceptable(Class hintClass) {
        return false;
    }
}
