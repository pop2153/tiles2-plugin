package org.intellij.tiles2.dom.converters;

import com.intellij.codeInsight.lookup.LookupValueFactory;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.Icons;
import com.intellij.util.xml.*;
import org.intellij.tiles2.dom.Bean;
import org.intellij.tiles2.providers.XmlAttributeValuePsiReference;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * converter for java method
 *
 * @author jacky
 */
public class JavaMethodConverter extends Converter<PsiMethod> implements CustomReferenceConverter<PsiMethod> {

    public String toString(@Nullable PsiMethod psiMethod, ConvertContext context) {
        if (psiMethod != null) {
            return StringUtil.decapitalize(psiMethod.getName().replaceAll("set", ""));
        }
        return "";
    }

    @Nullable public PsiMethod fromString(@Nullable @NonNls String methodName, ConvertContext context) {
        if (methodName != null && methodName.trim().length() > 0) {
            Bean bean = context.getInvocationElement().getParentOfType(Bean.class, false);
            if (bean != null) {
                PsiClass psiClass = bean.getClasstype().getValue();
                if (psiClass != null) {
                    PsiMethod[] methods = psiClass.findMethodsByName("set" + StringUtil.capitalize(methodName), true);
                    if (methods.length > 0) return methods[0];
                }
            }
        }
        return null;
    }

    @NotNull
    public PsiReference[] createReferences(final GenericDomValue<PsiMethod> psiMethodGenericDomValue, PsiElement element, final ConvertContext context) {
        return new PsiReference[]{new XmlAttributeValuePsiReference((XmlAttributeValue) element) {
            @Override @Nullable
            public PsiElement resolve() {
                return fromString(getCanonicalText(), context);
            }

            @Override public Object[] getVariants() {
                List<Object> variants = new ArrayList<Object>();
                Bean bean = context.getInvocationElement().getParentOfType(Bean.class, false);
                if (bean != null) {
                    PsiClass psiClass = bean.getClasstype().getValue();
                    if (psiClass != null) {
                        PsiMethod[] methods = psiClass.getAllMethods();
                        for (PsiMethod method : methods) {
                            if (method.getName().startsWith("set") && method.getParameterList().getParametersCount() == 1) {
                                String fieldName = StringUtil.decapitalize(method.getName().replaceAll("set", ""));
                                    variants.add(LookupValueFactory.createLookupValue(fieldName, Icons.FIELD_ICON));
                            }
                        }
                    }
                }
                return variants.toArray();
            }

            @Override public boolean isSoft() {
                return false;
            }
        }};
    }
}
