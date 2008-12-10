package org.intellij.tiles2;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

/**
 * project component for Tiles 2
 *
 * @author jacky
 */
public class Tiles2ProjectComponent implements ProjectComponent {
    private Tiles2ConfigModelFactory modelFactory;
    private Project project;

    public Tiles2ProjectComponent(Project project, DomManager domManager) {
        this.project = project;
        modelFactory = new Tiles2ConfigModelFactory(domManager);
    }

    public static Tiles2ProjectComponent getInstance(Project project) {
        return project.getComponent(Tiles2ProjectComponent.class);
    }

    public void initComponent() {
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
