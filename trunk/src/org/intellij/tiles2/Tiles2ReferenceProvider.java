package org.intellij.tiles2;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.filters.*;
import com.intellij.psi.filters.position.NamespaceFilter;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.URIReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlUtil;
import org.intellij.tiles2.filters.AttributeReferenceDefinitionFilter;
import org.intellij.tiles2.filters.AttributeReferenceTemplateFilter;
import org.intellij.tiles2.filters.Struts2ReferenceDefinitionFilter;
import org.intellij.tiles2.providers.*;
import org.jetbrains.annotations.NonNls;

/**
 * all reference provider
 */
public class Tiles2ReferenceProvider extends PsiReferenceContributor {
    private NamespaceFilter tiles2ConfigNamespaceFilter;
    private NamespaceFilter tiles2TaglibNamespaceFilter;
    private PsiReferenceRegistrar registrary;
    public final static ClassFilter TAG_CLASS_FILTER = new ClassFilter(XmlTag.class);


    public Tiles2ReferenceProvider() {
        tiles2ConfigNamespaceFilter = new NamespaceFilter(Tiles2Constants.TILES2_CONFIG_DTD);
        tiles2TaglibNamespaceFilter = new NamespaceFilter(Tiles2Constants.TILES2_TAGLIB_SCHEMA);
    }

    public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
        this.registrary = psiReferenceRegistrar;
        registerProvider();
    }

    private void registerProvider() {
        JavaClassReferenceProvider classReferenceProvider = new JavaClassReferenceProvider(this.registrary.getProject());
        URIReferenceProvider uriReferenceProvider = new URIReferenceProvider();
        DefinitionReferenceProvider definitionReferenceProvider = new DefinitionReferenceProvider();
        DefinitionPutAttributeReferenceProvider putAttributeReferenceProvider = new DefinitionPutAttributeReferenceProvider();
        DefinitionAttributeReferenceProvider definitionAttributeReferenceProvider = new DefinitionAttributeReferenceProvider();
        AttributeGapReferenceProvider attributeGapReferenceProvider = new AttributeGapReferenceProvider();
        //template related
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "definition", new String[]{"template"}, uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "definition", new String[]{"template"}, uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "insertTemplate", new String[]{"template"}, uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "put-attribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "putAttribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "addAttribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        //definition related
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "definition", new String[]{"extends"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "definition", new String[]{"extends"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "insertDefinition", new String[]{"name"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "put-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "putAttribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "addAttribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        //other
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "definition", new String[]{"preparer"}, classReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "putAttribute", new String[]{"name"}, attributeGapReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "importAttribute", new String[]{"name"}, definitionAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "definition", new String[]{"preparer"}, classReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "insertAttribute", new String[]{"name"}, putAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "getAsString", new String[]{"name"}, putAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "bean", new String[]{"classtype"}, putAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "item", new String[]{"classtype"}, putAttributeReferenceProvider);
        registrary.registerReferenceProvider(new Struts2ReferenceDefinitionFilter(), new Struts2DefinitionReferenceProvider());
    }


    /**
     * Register the given provider on the given XmlAttribute/Namespace/XmlTag(s) combination.
     *
     * @param provider        Provider to install.
     * @param attributeNames  Attribute names.
     * @param namespaceFilter Namespace for tag(s).
     * @param tagName         tag name
     */
    private void registerXmlAttributeValueReferenceProvider(final NamespaceFilter namespaceFilter, String tagName, final @NonNls String[] attributeNames, final PsiReferenceProvider provider) {
        XmlUtil.registerXmlAttributeValueReferenceProvider(registrary, attributeNames, andTagNames(namespaceFilter, TrueFilter.INSTANCE, tagName), provider);
    }

    /**
     * Register the given provider on the given XmlAttribute/Namespace/XmlTag(s) combination.
     *
     * @param filter          filter
     * @param provider        Provider to install.
     * @param attributeNames  Attribute names.
     * @param namespaceFilter Namespace for tag(s).
     * @param tagName         tag name
     */
    private void registerXmlAttributeValueReferenceProvider(final NamespaceFilter namespaceFilter, String tagName, final @NonNls String[] attributeNames, ElementFilter filter, final PsiReferenceProvider provider) {
        XmlUtil.registerXmlAttributeValueReferenceProvider(registrary, attributeNames, andTagNames(namespaceFilter, filter, tagName), provider);
    }

    public static ScopeFilter andTagNames(final ElementFilter namespace, ElementFilter filter, final String... tagNames) {
        return new ScopeFilter(new ParentElementFilter(new AndFilter(namespace, filter, TAG_CLASS_FILTER, new TextFilter(tagNames)), 2));
    }
}
