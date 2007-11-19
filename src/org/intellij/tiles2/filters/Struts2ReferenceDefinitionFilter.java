package org.intellij.tiles2.filters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.xml.XmlTag;
import org.intellij.tiles2.Tiles2Constants;

/**
 * struts2 result type of Tiles reference filter
 *
 * @author jacky
 */
public class Struts2ReferenceDefinitionFilter implements ElementFilter {

    public boolean isAcceptable(Object element, PsiElement context) {
        if (context instanceof XmlTag) {
            XmlTag tag = (XmlTag) context;
            String resultType = tag.getAttributeValue("type");
            if ("tiles".equals(resultType) && tag.getName().equals("result") && tag.getNamespace().equals(Tiles2Constants.STRUTS_CONFIG_DTD)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassAcceptable(Class hintClass) {
        return false;
    }
}
