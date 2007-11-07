package org.intellij.tiles2.dom;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.util.xml.*;
import org.intellij.tiles2.dom.converters.DefinitionConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * definition element
 *
 * @author jacky
 */
public interface Definition extends CommonDomModelElement {

    @Attribute("name")
    public GenericAttributeValue<String> getName();

    @Attribute("template")
    public GenericAttributeValue<String> getTemplate();

    @Attribute("extends")
    @Convert(DefinitionConverter.class)
    public GenericAttributeValue<Definition> getExtends();

    @Attribute("preparer")
    public GenericAttributeValue<PsiClass> getPreparer();

    @SubTagList("put-attribute")
    public List<PutAttribute> getPutAttributes();

    @SubTagList("put-list-attribute")
    public List<PutListAttribute> getPutListAttributes();

    /**
     * get all attribute in definition, extended definition also included
     *
     * @return PutAttribute list
     */
    @NotNull
    public List<PutAttribute> getAllPutAttribute();

    /**
     * get psi file for definition
     *
     * @return template psi file
     */
    @Nullable
    public PsiFile getTemplatePsiFile();

}
