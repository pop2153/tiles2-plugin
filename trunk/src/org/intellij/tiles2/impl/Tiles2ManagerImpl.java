package org.intellij.tiles2.impl;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.*;
import org.intellij.tiles2.*;
import org.intellij.tiles2.dom.Definition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tiles2 Manager implementation
 *
 * @author jacky
 */
public class Tiles2ManagerImpl extends Tiles2Manager {

    /**
     * get all definitions in Tiles2 config files
     *
     * @param context PsiElement object
     * @return definition list
     */
    @NotNull public List<Definition> getAllDefinition(PsiElement context) {
        List<Definition> definitionList = new ArrayList<Definition>();
        Module module = ModuleUtil.findModuleForPsiElement(context);
        if (module != null) {
            List<Tiles2ConfigModel> models = Tiles2ProjectComponent.getInstance(context.getProject()).getTiles2ConfigModelFactory().getAllModels(module);
            for (Tiles2ConfigModel model : models) {
                definitionList.addAll(model.getMergedModel().getDefinitions());
            }
        }
        return definitionList;
    }

    /**
     * get all attribute gaps in psi File
     *
     * @param xmlFile xml type file, jsp included
     * @return attribute gap name list
     */
    @NotNull
    public List<XmlTag> getAttributeGapsInFile(XmlFile xmlFile) {
        final List<XmlTag> nameList = new ArrayList<XmlTag>();
        XmlDocument document = xmlFile.getDocument();
        if (document != null) {
            XmlTag rootTag = document.getRootTag();
            if (rootTag != null) {
                XmlTag[] tags = rootTag.getSubTags();
                for (XmlTag tag : tags) {
                    String tagName = tag.getName();
                    if (tagName.endsWith("insertAttribute") || tagName.endsWith("getAsString") || tagName.endsWith("importAttribute")) {
                        if (Tiles2Constants.TILES2_TAGLIB_SCHEMA.equals(tag.getNamespace())) {
                            nameList.add(tag);
                        }
                    }
                }
            }
        }
        return nameList;
    }

    /**
     * find the definition for psi file
     *
     * @param psiFile psi file
     * @return definition
     */
    @Nullable
    public Definition findDefinitionForPsiFile(PsiFile psiFile) {
        List<Definition> definitionList = getAllDefinition(psiFile);
        String fileName = psiFile.getName();
        for (Definition definition : definitionList) {
            String template = definition.getTemplate().getStringValue();
            if (template != null && template.endsWith(fileName)) {
                if (psiFile == definition.getTemplatePsiFile()) {
                    return definition;
                }
            }
        }
        return null;
    }
}
