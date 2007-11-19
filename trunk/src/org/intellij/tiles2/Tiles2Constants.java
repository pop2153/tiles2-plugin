package org.intellij.tiles2;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * constants in Tiles 2
 *
 * @author jacky
 */
public interface Tiles2Constants {
    public String TILES2_CONFIG_DTD = "http://tiles.apache.org/dtds/tiles-config_2_0.dtd";
    public String STRUTS_CONFIG_DTD = "http://struts.apache.org/dtds/struts-2.0.dtd";
    public String TILES2_TAGLIB_SCHEMA = "http://tiles.apache.org/tags-tiles";
    public Icon TILES2_LOGO = IconLoader.findIcon("/icons/tiles2_logo.png");
    public Icon TILES2_CONFIG_LOGO = IconLoader.findIcon("/icons/TilesConfig.png");
    public Icon TILES2_DEFINITION_LOGO = IconLoader.findIcon("/icons/definition.png");

    public String DEFINITIONS_CONFIG = "org.apache.tiles.impl.BasicTilesContainer.DEFINITIONS_CONFIG";
    public String TILES_SERVLET = "org.apache.tiles.web.startup.TilesServlet";
    public String TILES_LISTENER = "org.apache.tiles.web.startup.TilesListener";
    public String TILES_FILTER = "org.apache.tiles.web.startup.TilesFilter";
}
