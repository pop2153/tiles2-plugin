package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;

import java.util.List;

/**
 * bean element.
 *
 * @author jacky
 */
public interface Bean extends CommonDomModelElement {

    @Attribute("classtype")
    public GenericAttributeValue<PsiClass> getClasstype();

    @SubTagList("set-property")
    public List<SetProperty> getSetPropertyList();
}