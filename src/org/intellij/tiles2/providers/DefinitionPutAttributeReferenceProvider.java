package org.intellij.tiles2.providers;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.intellij.tiles2.Tiles2Manager;
import org.intellij.tiles2.dom.Definition;
import org.intellij.tiles2.dom.PutAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * reference provider for put attribute reference in definition
 *
 * @author jacky.
 */
public class DefinitionPutAttributeReferenceProvider extends BaseReferenceProvider {
    @Override @NotNull public PsiReference[] getReferencesByElement(final PsiElement psiElement) {
        return new PsiReference[]{new XmlAttributeValuePsiReference((XmlAttributeValue) psiElement) {
            @Override @Nullable
            public PsiElement resolve() {
                String attributeName = getCanonicalText();
                Definition definition = Tiles2Manager.getInstance().findDefinitionForPsiFile(psiElement.getContainingFile());
                if (definition != null) {
                    List<PutAttribute> attributeList = definition.getAllPutAttribute();
                    for (PutAttribute putAttribute : attributeList) {
                        if (attributeName.equals(putAttribute.getName().getStringValue())) {
                            return putAttribute.getXmlTag();
                        }
                    }
                }
                return null;
            }

            @Override public Object[] getVariants() {
                List<String> variants = new ArrayList<String>();
                boolean isGetAsString = false;
                XmlTag xmlTag = PsiTreeUtil.getParentOfType(psiElement, XmlTag.class);
                if (xmlTag != null && xmlTag.getName().endsWith("getAsString")) {
                    isGetAsString = true;
                }
                Definition definition = Tiles2Manager.getInstance().findDefinitionForPsiFile(psiElement.getContainingFile().getOriginalFile());
                if (definition != null) {
                    for (PutAttribute putAttribute : definition.getAllPutAttribute()) {
                        String attributeName = putAttribute.getName().getStringValue();
                        String attributeType = putAttribute.getType().getStringValue();
                        String attributeValue = putAttribute.getValue().getStringValue();
                        if (StringUtil.isNotEmpty(attributeName)) {
                            //only string
                            if (isGetAsString) {
                                if ((attributeType == null && !(attributeValue + "").startsWith("/")) || "string".equals(attributeType)) {
                                    variants.add(attributeName);
                                }
                            } else {//template or definition, excluede string type
                                if (!"string".equals(attributeType)) {
                                    variants.add(attributeName);
                                }
                            }
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
