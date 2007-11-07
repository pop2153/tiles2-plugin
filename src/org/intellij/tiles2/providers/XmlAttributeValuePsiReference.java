package org.intellij.tiles2.providers;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * xml attribute value psi reference, an XmlAttributeValue object should be supplied
 *
 * @author jacky
 */
public class XmlAttributeValuePsiReference implements PsiReference {
    private XmlAttributeValue xmlAttributeValue;

    public XmlAttributeValuePsiReference(XmlAttributeValue xmlAttributeValue) {
        this.xmlAttributeValue = xmlAttributeValue;
    }

    public XmlAttributeValue getElement() {
        return this.xmlAttributeValue;
    }

    public TextRange getRangeInElement() {
        return new TextRange(1, xmlAttributeValue.getValue().length() + 1);
    }

    @Nullable
    public PsiElement resolve() {
        return null;
    }

    public String getCanonicalText() {
        return xmlAttributeValue.getValue();
    }

    @Nullable public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Nullable public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    public boolean isReferenceTo(PsiElement element) {
        return element == resolve();
    }

    public Object[] getVariants() {
        return new Object[0];
    }

    public boolean isSoft() {
        return true;
    }

}