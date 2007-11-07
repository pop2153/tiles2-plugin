package org.intellij.tiles2.dom;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.javaee.model.xml.CommonDomModelElement;

/**
 * put-attribute element
 *
 * @author jacky
 */
public interface PutAttribute extends CommonDomModelElement {
    @Attribute("name")
    public GenericAttributeValue<String> getName();

    @Attribute("type")
    public GenericAttributeValue<String> getType();

    @Attribute("value")
    public GenericAttributeValue<String> getValue();
}
