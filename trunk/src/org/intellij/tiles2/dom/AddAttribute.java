package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * add-attribute element.
 *
 * @author jacky
 */
public interface AddAttribute extends CommonDomModelElement {

    @Attribute("type")
    public GenericAttributeValue<String> getType();

    @Attribute("value")
    public GenericAttributeValue<String> getValue();
}
