package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * set-property element.
 *
 * @author jacky
 */
public interface SetProperty extends CommonDomModelElement {

    @Attribute("property")
    public GenericAttributeValue<String> getProperty();

    @Attribute("value")
    public GenericAttributeValue<String> getValue();
}