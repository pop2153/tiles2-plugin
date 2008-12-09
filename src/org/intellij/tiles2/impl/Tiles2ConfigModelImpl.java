package org.intellij.tiles2.impl;

import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.model.impl.DomModelImpl;
import org.intellij.tiles2.Tiles2ConfigModel;
import org.intellij.tiles2.dom.TilesDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Tiles2ConfigMode implemented
 *
 * @author jacky
 */
public class Tiles2ConfigModelImpl extends DomModelImpl<TilesDefinitions> implements Tiles2ConfigModel {

    public Tiles2ConfigModelImpl(@NotNull TilesDefinitions mergedModel, @NotNull Set<XmlFile> configFiles) {
        super(mergedModel, configFiles);
    }
}
