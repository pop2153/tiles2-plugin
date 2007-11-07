package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelRootElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * tiles-definitions root element
 *
 * @author jacky
 */
public interface TilesDefinitions extends CommonDomModelRootElement {

    @SubTagList("definition")
    public List<Definition> getDefinitions();
}
