package org.intellij.tiles2.dom.converters;

import com.intellij.javaee.web.WebRoot;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.OrderedSet;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * resolve psi file under web root in a module
 *
 * @author Jacky
 */
public abstract class WebResourceFileConverterBase extends ResolvingConverter<PsiFile> implements CustomReferenceConverter {

    @Nullable
    public PsiFile fromString(@Nullable @NonNls String s, final ConvertContext context) {
        if (s == null) return null;
        final PsiReference[] references = createReferences((GenericDomValue) context.getInvocationElement(), context.getReferenceXmlElement(), context);
        if (references.length == 0) return null;
        final PsiElement element = references[references.length - 1].resolve();
        return element instanceof PsiFile ? (PsiFile) element : null;
    }

    @Nullable
    public String toString(@Nullable PsiFile psiFile, final ConvertContext context) {
        if (psiFile == null) return null;
        final VirtualFile file = psiFile.getVirtualFile();
        if (file == null) return null;
        Set<VirtualFile> root = getRootForFile(file, context);
        if (root.size() == 0) return null;
        for (VirtualFile virtualFile : root) {
            String name = VfsUtil.getRelativePath(file, virtualFile, '/');
            if (name != null) return name;
        }
        return null;
    }

    @NotNull
    protected Set getRootForFile(final VirtualFile file, final ConvertContext context) {
        Set<VirtualFile> webRoots = new HashSet<VirtualFile>();
        Collection<WebFacet> webFacets = WebFacet.getInstances(context.getModule());
        for (WebFacet webFacet : webFacets) {
            List<WebRoot> roots = webFacet.getWebRoots(false);
            for (WebRoot root : roots) {
                webRoots.add(root.getFile());
            }
        }
        return webRoots;
    }

    @NotNull
    public Collection<? extends PsiFile> getVariants(final ConvertContext context) {
        return Collections.emptyList();
    }

    @Nullable
    public PsiElement resolve(final PsiFile o, final ConvertContext context) {
        return isFileAccepted(o) ? super.resolve(o, context) : null;
    }

    @NotNull
    public PsiReference[] createReferences(GenericDomValue genericDomValue, PsiElement element, ConvertContext context) {
        final String s = genericDomValue.getStringValue();
        if (s == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        final int offset = ElementManipulators.getOffsetInElement(element);
        final FileReferenceSet set = createReferenceSet(context, s, element, offset);
        return set.getAllReferences();
    }

    protected abstract boolean isFileAccepted(final PsiFile file);

    protected FileReferenceSet createReferenceSet(final ConvertContext context, final String text, final PsiElement element,
                                                  final int startInElement) {
        return new FileReferenceSet(text, element, startInElement, null, true) {
            protected boolean isSoft() {
                return false;
            }

            @NotNull
            public Collection<PsiFileSystemItem> computeDefaultContexts() {
                final OrderedSet<PsiFileSystemItem> result = new OrderedSet<PsiFileSystemItem>();
                return addDefaultRoots(result, context);
            }

            @Override
            protected Condition<PsiElement> createCondition() {
                return new Condition<PsiElement>() {
                    public boolean value(PsiElement psiElement) {
                        final boolean isFile = element instanceof PsiFile;
                        return isFile && !isFileAccepted((PsiFile) element);
                    }
                };
            }

        };
    }

    protected Collection<PsiFileSystemItem> addDefaultRoots(final Collection<PsiFileSystemItem> result, final ConvertContext context) {
        final Module module = context.getModule();
        if (module != null) {
            final PsiManager psiManager = context.getPsiManager();
            addModuleDefaultRoots(result, module, psiManager);
            for (Module dependency : ModuleRootManager.getInstance(module).getDependencies()) {
                addModuleDefaultRoots(result, dependency, psiManager);
            }
        }
        return result;
    }

    protected void addModuleDefaultRoots(final Collection<PsiFileSystemItem> result, final Module module, final PsiManager psiManager) {
        final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
        for (VirtualFile root : rootManager.getSourceRoots()) {
            ContainerUtil.addIfNotNull(psiManager.findDirectory(root), result);
        }
        for (VirtualFile root : rootManager.getContentRoots()) {
            ContainerUtil.addIfNotNull(psiManager.findDirectory(root), result);
        }
    }

    public String getErrorMessage(@Nullable final String s, final ConvertContext context) {
        return "Resolve web resource file failed!";
    }

}