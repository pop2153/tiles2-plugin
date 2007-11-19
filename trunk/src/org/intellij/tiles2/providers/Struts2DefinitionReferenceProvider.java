package org.intellij.tiles2.providers;

import com.intellij.codeInsight.lookup.LookupValueFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
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
public class Struts2DefinitionReferenceProvider extends BaseReferenceProvider {
    @NotNull public PsiReference[] getReferencesByElement(final PsiElement psiElement) {
        final XmlTag tag = (XmlTag) psiElement;
        return new PsiReference[]{new PsiReference() {
            public PsiElement getElement() {
                return psiElement;
            }

            public TextRange getRangeInElement() {
                final int start = tag.getTextRange().getStartOffset();
                return tag.getValue().getTextRange().shiftRight(-start);
            }

            @Nullable public PsiElement resolve() {
                String definitionName = getCanonicalText();
                List<Definition> definitionList = Tiles2Manager.getInstance().getAllDefinition(getElement());
                for (Definition definition : definitionList) {
                    if (definitionName.equals(definition.getName().getStringValue())) {
                        return definition.getXmlTag();
                    }
                }
                return null;
            }

            public String getCanonicalText() {
                return tag.getValue().getText();
            }

            @Nullable public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
                return null;
            }

            @Nullable public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
                return null;
            }

            public boolean isReferenceTo(PsiElement element) {
                return resolve() == element;
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
                    String defintionName = definition.getName().getStringValue();
                    if (defintionName!=null && defintionName.length()>0)
                        variants.add(LookupValueFactory.createLookupValue(defintionName, Tiles2Constants.TILES2_DEFINITION_LOGO));
                }
                return variants.toArray();
            }

            public boolean isSoft() {
                return false;
            }
        }};
    }
}