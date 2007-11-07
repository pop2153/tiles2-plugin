package org.intellij.tiles2.dom.converters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.*;
import org.intellij.tiles2.dom.Definition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * definition converter
 *
 * @author jacky
 */
public class DefinitionConverter extends Converter<Definition> {
    @Nullable public Definition fromString(@Nullable @NonNls String name, ConvertContext context) {
        XmlElement xmlElement = context.getReferenceXmlElement();
        if (xmlElement != null) {
            PsiReference psiReference = xmlElement.getReference();
            if (psiReference != null) {
                PsiElement resolvedElement = psiReference.resolve();
                if (resolvedElement != null && resolvedElement instanceof XmlTag) {
                    XmlTag xmlTag = (XmlTag) resolvedElement;
                    DomElement domElement = DomManager.getDomManager(xmlElement.getProject()).getDomElement(xmlTag);
                    if (domElement != null && domElement instanceof Definition) {
                        return (Definition) domElement;
                    }
                }
            }
        }
        return null;
    }

    public String toString(@Nullable Definition definition, ConvertContext context) {
        return definition != null ? definition.getName().getStringValue() : "";
    }
}
