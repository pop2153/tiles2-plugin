package org.intellij.tiles2.providers;

import com.intellij.codeInsight.lookup.LookupValueFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.intellij.tiles2.Tiles2Constants;
import org.intellij.tiles2.Tiles2Manager;
import org.intellij.tiles2.dom.Definition;
import org.intellij.tiles2.dom.TilesDefinitions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * definition reference provider
 *
 * @author jacky
 */
public class DefinitionReferenceProvider extends BaseReferenceProvider {
    @NotNull public PsiReference[] getReferencesByElement(final PsiElement psiElement) {
        return new PsiReference[]{new XmlAttributeValuePsiReference((XmlAttributeValue) psiElement) {
            @Nullable
            public PsiElement resolve() {
                String definitionName = getCanonicalText();
                List<Definition> definitionList = Tiles2Manager.getInstance().getAllDefinition(getElement());
                for (Definition definition : definitionList) {
                    if (definitionName.equals(definition.getName().getStringValue())) {
                        return definition.getXmlTag();
                    }
                }
                return null;
            }

            public Object[] getVariants() {
                List<Object> variants = new ArrayList<Object>();
                List<Definition> definitionList = Tiles2Manager.getInstance().getAllDefinition(getElement());
                //use definition in current xml file if config files absent
                if (definitionList.size() == 0) {
                    DomFileElement<TilesDefinitions> domFileElement = DomManager.getDomManager(psiElement.getProject()).getFileElement((XmlFile) getElement().getContainingFile(), TilesDefinitions.class);
                    if (domFileElement != null) {
                        definitionList = domFileElement.getRootElement().getDefinitions();
                    }
                }
                for (Definition definition : definitionList) {
                    String definitionName = definition.getName().getStringValue();
                    if (definitionName != null && definitionName.length() > 0)
                        variants.add(LookupValueFactory.createLookupValue(definitionName, Tiles2Constants.TILES2_DEFINITION_LOGO));
                }
                return variants.toArray();
            }

            public boolean isSoft() {
                return false;
            }
        }};
    }
}
