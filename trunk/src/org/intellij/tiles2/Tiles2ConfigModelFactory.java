package org.intellij.tiles2;

import com.intellij.javaee.model.xml.ParamValue;
import com.intellij.javaee.model.xml.web.Filter;
import com.intellij.javaee.model.xml.web.Servlet;
import com.intellij.javaee.model.xml.web.WebApp;
import com.intellij.javaee.web.WebRoot;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.model.DomModelFactory;
import org.intellij.tiles2.dom.TilesDefinitions;
import org.intellij.tiles2.impl.Tiles2ConfigModelImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Tiles config model factory
 *
 * @author jacky
 */
public class Tiles2ConfigModelFactory extends DomModelFactory<TilesDefinitions, Tiles2ConfigModel, PsiElement> {

    protected Tiles2ConfigModelFactory(DomManager domManager) {
        super(TilesDefinitions.class, domManager.createModelMerger(), domManager.getProject(), "tiles2");
    }

    /**
     * get the Tiles 2 config model
     *
     * @param context psi element
     * @return config model
     */
    @Nullable
    public Tiles2ConfigModel getModel(@NotNull PsiElement context) {
        final PsiFile psiFile = context.getContainingFile();
        if (psiFile instanceof XmlFile) {
            return getModelByConfigFile((XmlFile) psiFile);
        }
        return null;
    }

    protected List<Tiles2ConfigModel> computeAllModels(@NotNull Module module) {
        List<Tiles2ConfigModel> models = new ArrayList<Tiles2ConfigModel>();
        Set<XmlFile> configFiles = getTiles2ConfigFiles(module);
        if (configFiles.size() > 0) {
            Tiles2ConfigModel model = new Tiles2ConfigModelImpl(createMergedModel(configFiles), configFiles);
            models.add(model);
        }
        return models;
    }

    protected Tiles2ConfigModel createCombinedModel(Set<XmlFile> xmlFiles, DomFileElement<TilesDefinitions> mergedModel, Tiles2ConfigModel tiles2ConfigModel, Module module) {
        return new Tiles2ConfigModelImpl(mergedModel.getRootElement(), xmlFiles);
    }

    /**
     * get Tiles 2 config files
     *
     * @param module Module object
     * @return Tiles 2 config files
     */
    @NotNull
    public Set<XmlFile> getTiles2ConfigFiles(Module module) {
        Set<XmlFile> configFiles = new HashSet<XmlFile>();
        Collection<WebFacet> webFacets = WebFacet.getInstances(module);
        for (WebFacet webFacet : webFacets) {
            WebApp webApp = webFacet.getRoot();
            if (webApp != null) {
                Set<String> fileNames = getTilesConfigFiles(webApp);
                if (fileNames.size() > 0) {
                    for (String fileName : fileNames) {
                        fileName = fileName.trim();
                        VirtualFile configFile;
                        if (fileName.contains("WEB-INF")) //search from web root
                        {
                            configFile = getFileInWebRoot(webFacet.getWebRoots(false), fileName);
                        } else  //search in classpath
                        {
                            ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
                            configFile = getFileInDirectories(rootManager.getSourceRoots(), fileName);
                        }
                        if (configFile != null) {
                            PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(configFile);
                            if (psiFile != null && psiFile instanceof XmlFile) {
                                configFiles.add((XmlFile) psiFile);
                            }
                        }
                    }
                }
            }
        }
        return configFiles;
    }

    /**
     * get file in web roots
     *
     * @param roots        web roots
     * @param relativePath relative path
     * @return destination file
     */
    @Nullable
    private VirtualFile getFileInWebRoot(List<WebRoot> roots, String relativePath) {
        for (WebRoot root : roots) {
            VirtualFile rootDirectory = root.getFile();
            if (rootDirectory != null) {
                VirtualFile destinationFile = rootDirectory.findFileByRelativePath(relativePath.startsWith("/") ? relativePath.substring(1) : relativePath);
                if (destinationFile != null) return destinationFile;
            }
        }
        return null;
    }

    /**
     * get file in directories
     *
     * @param directories  directories
     * @param relativePath relative path
     * @return destination file
     */
    @Nullable
    private VirtualFile getFileInDirectories(VirtualFile[] directories, String relativePath) {
        if (directories == null) return null;
        for (VirtualFile directory : directories) {
            VirtualFile destinationFile = directory.findFileByRelativePath(relativePath.startsWith("/") ? relativePath.substring(1) : relativePath);
            if (destinationFile != null) return destinationFile;
        }
        return null;
    }

    /**
     * get the Tiles 2 config files in web app
     *
     * @param webApp webApp object
     * @return files list
     */
    @NotNull
    private Set<String> getTilesConfigFiles(WebApp webApp) {
        Set<String> configFiles = new HashSet<String>();
        //check Tiles servlet
        List<Servlet> servlets = webApp.getServlets();
        for (Servlet servlet : servlets) {
            //valid Tiles servlet  class name
            if (Tiles2Constants.TILES_SERVLET.equals(servlet.getServletClass().getStringValue())) {
                configFiles.addAll(getSuitableConfigFiles(servlet.getInitParams()));
            }
        }
        //check context parameter
        if (configFiles.size() == 0) {
            configFiles.addAll(getSuitableConfigFiles(webApp.getContextParams()));
        }
        //check Tiles filter
        if (configFiles.size() == 0) {
            List<Filter> filterList = webApp.getFilters();
            for (Filter filter : filterList) {
                //check the filter class name
                if (Tiles2Constants.TILES_FILTER.equals(filter.getFilterClass().getStringValue())) {
                    configFiles.addAll(getSuitableConfigFiles(filter.getInitParams()));
                }
            }
        }
        //default Tiles 2 config file
        if (configFiles.size() == 0) {
            configFiles.add("/WEB-INF/tiles.xml");
        }
        return configFiles;
    }

    /**
     * get the Tiles 2 config file from parameter
     *
     * @param params ParamValue list
     * @return files list
     */
    @NotNull
    private Set<String> getSuitableConfigFiles(List<ParamValue> params) {
        Set<String> configFiles = new HashSet<String>();
        for (ParamValue initParam : params) {
            String paramName = initParam.getParamName().getStringValue();
            //valid param name for Tiles 2
            if (Tiles2Constants.DEFINITIONS_CONFIG.equals(paramName) || "definitions-config".equals(paramName)) {
                String paramValue = initParam.getParamValue().getStringValue();
                if (paramValue != null) {
                    configFiles.addAll(Arrays.asList(paramValue.split(",")));
                }
            }
        }
        return configFiles;
    }
}
