package org.intellij.tiles2.dom.impl;

import com.intellij.javaee.model.xml.impl.BaseImpl;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import org.intellij.tiles2.dom.Definition;
import org.intellij.tiles2.dom.PutAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * definition implemented
 *
 * @author jacky
 */
public abstract class DefinitionImpl extends BaseImpl implements Definition {
    /**
     * get all attribute in definition, extended definition also included
     *
     * @return PutAttribute list
     */
    @SuppressWarnings({"ConstantConditions"})
    @NotNull public List<PutAttribute> getAllPutAttribute() {
        List<PutAttribute> attributeList = getPutAttributes();
        if (getExtends().getValue() != null) {
            attributeList.addAll(getExtends().getValue().getAllPutAttribute());
        }
        return attributeList;
    }

    /**
     * get psi file for definition
     *
     * @return template psi file
     */
    @SuppressWarnings({"ConstantConditions"})
    @Nullable public PsiFile getTemplatePsiFile() {
        //find the template file according to template name
        if (getTemplate().getValue() != null) {
            XmlAttributeValue attributeValue = getTemplate().getXmlAttribute().getValueElement();
            PsiReference psiReference = attributeValue.getReference();
            if (psiReference != null) {
                PsiElement element = psiReference.resolve();
                if (element != null && element instanceof PsiFile) {
                    return (PsiFile) element;
                }
            }
        }
        //find the template file according to extends
        if (getExtends().getValue() != null) {
            return getExtends().getValue().getTemplatePsiFile();
        }
        return null;
    }


}
