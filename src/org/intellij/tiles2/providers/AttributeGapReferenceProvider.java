package org.intellij.tiles2.providers;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.*;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.intellij.tiles2.Tiles2Manager;
import org.intellij.tiles2.dom.Definition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * reference attribute gap in jsp file
 *
 * @author jacky
 */
public class AttributeGapReferenceProvider extends BaseReferenceProvider {
    @Override @NotNull public PsiReference[] getReferencesByElement(final PsiElement psiElement) {
        return new PsiReference[]{new XmlAttributeValuePsiReference((XmlAttributeValue) psiElement) {
            @Override @Nullable
            public PsiElement resolve() {
                String name = getCanonicalText();
                PsiFile templateFile = getTemplateFile(getElement());
                if (templateFile != null) {
                    List<XmlTag> xmlTags = Tiles2Manager.getInstance().getAttributeGapsInFile((XmlFile) templateFile);
                    for (XmlTag xmlTag : xmlTags) {
                        String attributeName = xmlTag.getAttributeValue("name");
                        if (name.equals(attributeName)) {
                            return xmlTag;
                        }
                    }
                }
                return null;
            }

            @Override public boolean isSoft() {
                return true;
            }

            @Override public Object[] getVariants() {
                List<String> variants = new ArrayList<String>();
                PsiFile templateFile = getTemplateFile(getElement());
                if (templateFile != null) {
                    List<XmlTag> xmlTags = Tiles2Manager.getInstance().getAttributeGapsInFile((XmlFile) templateFile);
                    for (XmlTag xmlTag : xmlTags) {
                        String attributeName = xmlTag.getAttributeValue("name");
                        if (StringUtil.isNotEmpty(attributeName)) {
                            variants.add(attributeName);
                        }
                    }
                }
                return variants.toArray();
            }

            /**
             * get the template from put-attribute
             * @param xmlAttributeValue  put-attribute tag's attribute value
             * @return template file
             */
            @Nullable
            private PsiFile getTemplateFile(XmlAttributeValue xmlAttributeValue) {
                XmlTag putAttributeTag = PsiTreeUtil.getParentOfType(psiElement, XmlTag.class);
                if (putAttributeTag != null) {
                    XmlTag definitionTag = putAttributeTag.getParentTag();
                    if (definitionTag != null) {
                        DomElement domElement = DomManager.getDomManager(psiElement.getProject()).getDomElement(definitionTag);
                        if (domElement != null && domElement instanceof Definition) {
                            Definition definition = (Definition) domElement;
                            PsiFile templateFile = definition.getTemplatePsiFile();
                            if (templateFile != null && templateFile instanceof XmlFile) {
                                return templateFile;
                            }
                        }
                    }
                }
                return null;
            }
        }};
    }
}
