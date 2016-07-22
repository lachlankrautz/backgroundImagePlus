package co.notime.intellijPlugin.backgroundImageSwitch.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class Settings implements Configurable {

    private static final String FOLDER = "BackgroundImagesFolder";

    private JTextField imageFolder;
    private JPanel rootPanel;

    @Nls
    @Override
    public String getDisplayName() {
        return "Background Image";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String storedFolder      = prop.getValue(FOLDER);
        String uiFolder          = imageFolder.getText();
        if (storedFolder == null) {
            storedFolder = "";
        }
        return !storedFolder.equals(uiFolder);
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(FOLDER, imageFolder.getText());
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        imageFolder.setText(prop.getValue(FOLDER));
    }

    @Override
    public void disposeUIResources() {}
}
