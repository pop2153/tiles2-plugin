package org.intellij.tiles2;

import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

import com.intellij.CommonBundle;

/**
 * resource bundle for project
 *
 * @author jacky
 */
public class Tiles2Bundle {
    private static Reference ourBundle;
    protected static final String PATH_TO_BUNDLE = "org.intellij.tiles2.Tiles2Bundle";

    private Tiles2Bundle() {
    }

    /**
     * get resource bundle value from org.intellij.tiles2.Tiles2Bundle
     *
     * @param key        key name
     * @param parameters parameters for inline
     * @return value
     */
    public static String message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE)String key, Object... parameters) {
        return CommonBundle.message(getBundle(), key, parameters);
    }

    /**
     * get the resource bundle for org.intellij.tiles2.Tiles2Bundle
     *
     * @return ResourceBundle object
     */
    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (ourBundle != null)
            bundle = (ResourceBundle) ourBundle.get();
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(PATH_TO_BUNDLE);
            ourBundle = new SoftReference(bundle);
        }
        return bundle;
    }

}
