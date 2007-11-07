package org.intellij.tiles2;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.intellij.tiles2.dom.Definition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * manager interface for Tiles2
 *
 * @author jacky
 */
public abstract class Tiles2Manager {
    public Tiles2Manager() {
    }

    public static Tiles2Manager getInstance() {
        return ServiceManager.getService(Tiles2Manager.class);
    }

    /**
     * get all definitions in Tiles2 config files
     *
     * @param context PsiElement object
     * @return definition list
     */
    @NotNull
    public abstract List<Definition> getAllDefinition(PsiElement context);

    /**
     * get all attribute gaps in psi File
     *
     * @param xmlFile xml type file, jsp included
     * @return attribute gap name list
     */
    @NotNull
    public abstract List<XmlTag> getAttributeGapsInFile(XmlFile xmlFile);

    /**
     * find the definition for psi file
     *
     * @param psiFile psi file
     * @return definition
     */
    @Nullable
    public abstract Definition findDefinitionForPsiFile(PsiFile psiFile);
}
