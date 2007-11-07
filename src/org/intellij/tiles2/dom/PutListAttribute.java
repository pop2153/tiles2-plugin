package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.util.xml.*;

import java.util.List;

/**
 * put-list-attribute element
 *
 * @author jacky
 */
public interface PutListAttribute extends CommonDomModelElement {

    @Attribute("name")
    public GenericAttributeValue<String> getName();

    @SubTagList("add-attribute")
    public List<AddAttribute> getAddAttributes();
}
