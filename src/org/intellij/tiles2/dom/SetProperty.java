package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.xml.*;
import org.intellij.tiles2.dom.converters.JavaMethodConverter;
import org.jetbrains.annotations.NotNull;

/**
 * set-property element.
 *
 * @author jacky
 */
public interface SetProperty extends CommonDomModelElement {

    @NotNull @Attribute("property")
    @Convert(value = JavaMethodConverter.class)
    public GenericAttributeValue<PsiMethod> getProperty();

    @Attribute("value")
    public GenericAttributeValue<String> getValue();
}