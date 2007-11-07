package org.intellij.tiles2.providers;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import org.intellij.tiles2.Tiles2Manager;
import org.intellij.tiles2.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * reference provider for attribute reference in definition, list attribute added
 *
 * @author jacky.
 */
public class DefinitionAttributeReferenceProvider extends BaseReferenceProvider {
    @Override @NotNull public PsiReference[] getReferencesByElement(final PsiElement psiElement) {
        return new PsiReference[]{new XmlAttributeValuePsiReference((XmlAttributeValue) psiElement) {
            @Override @Nullable
            public PsiElement resolve() {
                String attributeName = getCanonicalText();
                Definition definition = Tiles2Manager.getInstance().findDefinitionForPsiFile(psiElement.getContainingFile());
                if (definition != null) {
                    //put-attribute
                    List<PutAttribute> attributeList = definition.getAllPutAttribute();
                    for (PutAttribute putAttribute : attributeList) {
                        if (attributeName.equals(putAttribute.getName().getStringValue())) {
                            return putAttribute.getXmlTag();
                        }
                    }
                    //put-list-attribute
                    for (PutListAttribute putListAttribute : definition.getPutListAttributes()) {
                        if (attributeName.equals(putListAttribute.getName().getStringValue())) {
                            return putListAttribute.getXmlTag();
                        }
                    }
                }
                return null;
            }

            @Override public Object[] getVariants() {
                List<String> variants = new ArrayList<String>();

                Definition definition = Tiles2Manager.getInstance().findDefinitionForPsiFile(psiElement.getContainingFile().getOriginalFile());
                if (definition != null) {
                    //put-attribute
                    for (PutAttribute putAttribute : definition.getAllPutAttribute()) {
                        String attributeName = putAttribute.getName().getStringValue();
                        if (StringUtil.isNotEmpty(attributeName)) {
                            variants.add(attributeName);
                        }
                    }
                    //put-list-attribute
                    for (PutListAttribute putListAttribute : definition.getPutListAttributes()) {
                        String attributeName = putListAttribute.getName().getStringValue();
                        if (StringUtil.isNotEmpty(attributeName)) {
                            variants.add(attributeName);
                        }
                    }
                }
                return variants.toArray();
            }

            @Override public boolean isSoft() {
                return true;
            }
        }};
    }
}