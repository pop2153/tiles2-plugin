package org.intellij.tiles2;

import com.intellij.ide.IconProvider;
import com.intellij.javaee.ExternalResourceManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.intellij.tiles2.dom.TilesDefinitions;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * application component for Tiles2, now dtd registration included
 *
 * @author jacky
 */
public class Tiles2ApplicationComponent implements ApplicationComponent, IconProvider {
    public Tiles2ApplicationComponent() {
    }

    public void initComponent() {
        //register dtd
        registerDTD(Tiles2Constants.TILES2_CONFIG_DTD);
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return Tiles2Bundle.message("tiles2.application.component.name");
    }

    /**
     * register DTD for Tiles 2
     *
     * @param dtdUrl dtd url
     */
    private void registerDTD(String dtdUrl) {
        if (dtdUrl.startsWith("http://")) {
            int pos = dtdUrl.lastIndexOf('/');
            @NonNls String file = "/org/intellij/tiles2/dtds" + dtdUrl.substring(pos);
            ExternalResourceManager.getInstance().addStdResource(dtdUrl, file, Tiles2ApplicationComponent.class);
        }
    }

    /**
     * create icon for Tiles 2 config file
     *
     * @param element psiFile object
     * @param flags   flags
     * @see com.intellij.openapi.util.Iconable
     */
    @Nullable public Icon getIcon(@NotNull PsiElement element, int flags) {
        if (element instanceof XmlFile) {
            XmlFile xmlFile = (XmlFile) element;
            DomFileElement<TilesDefinitions> domFileElement = DomManager.getDomManager(element.getProject()).getFileElement(xmlFile, TilesDefinitions.class);
            if (domFileElement != null) {
                return Tiles2Constants.TILES2_CONFIG_LOGO;
            }
        }
        return null;
    }
}

