package org.intellij.tiles2.filters;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.patterns.InitialPatternCondition;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.intellij.tiles2.Tiles2Constants;
import org.jetbrains.annotations.Nullable;

/**
 * struts2 result type of Tiles reference filter
 *
 * @author jacky
 */
public class Struts2ReferenceDefinitionFilter implements ElementPattern<XmlTag> {
    public boolean accepts(@Nullable Object o) {
        return false;
    }

    public boolean accepts(@Nullable Object o, ProcessingContext processingContext) {
        return false;
    }

    public ElementPatternCondition<XmlTag> getCondition() {
        return new ElementPatternCondition<XmlTag>(new InitialPatternCondition<XmlTag>(XmlTag.class) {
            @Override
            public boolean accepts(@Nullable Object o, ProcessingContext processingContext) {
                if (o instanceof XmlTag) {
                    XmlTag tag = (XmlTag) o;
                    String resultType = tag.getAttributeValue("type");
                    if ("tiles".equals(resultType) && tag.getName().equals("result") && tag.getNamespace().equals(Tiles2Constants.STRUTS_CONFIG_DTD)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
