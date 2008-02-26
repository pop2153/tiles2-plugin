package org.intellij.tiles2;

import com.intellij.psi.xml.XmlTag;
import com.intellij.util.NotNullFunction;
import com.intellij.util.xml.DomFileDescription;
import org.intellij.tiles2.dom.*;
import org.intellij.tiles2.dom.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

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
        registerImplementation(Bean.class, BeanImpl.class);
        registerImplementation(SetProperty.class, SetPropertyImpl.class);

        registerNamespacePolicy("Tile 2.0 configuration name space", new NotNullFunction<XmlTag, List<String>>() {
            @NotNull
            public List<String> fun(final XmlTag tag) {
                return Arrays.asList(Tiles2Constants.TILES2_CONFIG_DTD);
            }
        });
    }
}
