package org.intellij.tiles2.filters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;

/**
 * filter attribute to reference definition
 *
 * @author jacky
 */
public class AttributeReferenceDefinitionFilter implements ElementFilter {

    public boolean isAcceptable(Object element, PsiElement context) {
        XmlTag xmlTag = PsiTreeUtil.getParentOfType(context, XmlTag.class);
        if (xmlTag != null) {
            String attributeType = xmlTag.getAttributeValue("type");
            if ("definition".equals(attributeType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassAcceptable(Class hintClass) {
        return false;
    }
}