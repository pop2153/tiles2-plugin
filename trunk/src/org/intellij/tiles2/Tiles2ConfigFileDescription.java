package org.intellij.tiles2;

import com.intellij.util.xml.DomFileDescription;
import org.intellij.tiles2.dom.*;
import org.intellij.tiles2.dom.impl.*;

/**
 * file description for tiles 2 configuration file
 *
 * @author jacky
 */
public class Tiles2ConfigFileDescription extends DomFileDescription<TilesDefinitions> {
    public Tiles2ConfigFileDescription() {
        super(TilesDefinitions.class, "tiles-definitions");
    }

    protected void initializeFileDescription() {
        registerImplementation(Definition.class, DefinitionImpl.class);
        registerImplementation(PutAttribute.class, PutAttributeImpl.class);
        registerImplementation(PutListAttribute.class, PutListAttributeImpl.class);
        registerImplementation(AddAttribute.class, AddAttributeImpl.class);
    }
}
