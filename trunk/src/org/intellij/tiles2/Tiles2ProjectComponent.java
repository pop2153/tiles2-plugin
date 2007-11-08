package org.intellij.tiles2;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.filters.*;
import com.intellij.psi.filters.position.NamespaceFilter;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.URIReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomManager;
import org.intellij.tiles2.filters.AttributeReferenceDefinitionFilter;
import org.intellij.tiles2.filters.AttributeReferenceTemplateFilter;
import org.intellij.tiles2.providers.*;
import org.jetbrains.annotations.NotNull;

/**
 * project component for Tiles 2
 *
 * @author jacky
 */
public class Tiles2ProjectComponent implements ProjectComponent {
    private ReferenceProvidersRegistry registry;
    private NamespaceFilter tiles2ConfigNamespaceFilter;
    private NamespaceFilter tiles2TaglibNamespaceFilter;
    private Tiles2ConfigModelFactory modelFactory;

    public Tiles2ProjectComponent(Project project, DomManager domManager) {
        tiles2ConfigNamespaceFilter = new NamespaceFilter(Tiles2Constants.TILES2_CONFIG_DTD);
        tiles2TaglibNamespaceFilter = new NamespaceFilter(Tiles2Constants.TILES2_TAGLIB_SCHEMA);
        modelFactory = new Tiles2ConfigModelFactory(domManager);
        registry = ReferenceProvidersRegistry.getInstance(project);
    }

    public static Tiles2ProjectComponent getInstance(Project project) {
        return project.getComponent(Tiles2ProjectComponent.class);
    }

    public void initComponent() {
        JavaClassReferenceProvider classReferenceProvider = new JavaClassReferenceProvider();
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
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "put-attribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceTemplateFilter(), uriReferenceProvider);
        //definition related
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "definition", new String[]{"extends"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "definition", new String[]{"extends"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "insertDefinition ", new String[]{"name"}, definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "put-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "put-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "add-attribute", new String[]{"value"}, new AttributeReferenceDefinitionFilter(), definitionReferenceProvider);
        //other
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "definition", new String[]{"preparer"}, classReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2ConfigNamespaceFilter, "put-attribute", new String[]{"name"}, attributeGapReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "importAttribute", new String[]{"name"}, definitionAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "definition", new String[]{"preparer"}, classReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "insertAttribute", new String[]{"name"}, putAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "getAsString", new String[]{"name"}, putAttributeReferenceProvider);
        registerXmlAttributeValueReferenceProvider(tiles2TaglibNamespaceFilter, "bean", new String[]{"classtype"}, putAttributeReferenceProvider);
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return Tiles2Bundle.message("tiles2.project.component.name");
    }

    public void projectOpened() {

    }

    public void projectClosed() {

    }

    private void registerXmlAttributeValueReferenceProvider(NamespaceFilter namespaceFilter, String tagName, String attributeNames[], PsiReferenceProvider referenceProvider) {
        registry.registerXmlAttributeValueReferenceProvider(attributeNames, new ScopeFilter(new ParentElementFilter(new AndFilter(new ClassFilter(XmlTag.class), new AndFilter(new OrFilter(new TextFilter(tagName)), namespaceFilter)), 2)), referenceProvider);
    }

    private void registerXmlAttributeValueReferenceProvider(NamespaceFilter namespaceFilter, String tagName, String attributeNames[], ElementFilter extraFilter, PsiReferenceProvider referenceProvider) {
        registry.registerXmlAttributeValueReferenceProvider(attributeNames, new ScopeFilter(new ParentElementFilter(new AndFilter(new ClassFilter(XmlTag.class), new AndFilter(new AndFilter(new OrFilter(new TextFilter(tagName)), namespaceFilter), extraFilter)), 2)), referenceProvider);
    }

    /**
     * get Tiles config model factory
     *
     * @return Tiles 2 model factory
     */
    @NotNull
    public Tiles2ConfigModelFactory getTiles2ConfigModelFactory() {
        return modelFactory;
    }
}
