package co.notime.intellijPlugin.backgroundImagePlus.ui;

import co.notime.intellijPlugin.backgroundImagePlus.ScheduledExecutorServiceHandler;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class Settings implements Configurable {

    public static final String FOLDER = "BackgroundImagesFolder";
    public static final String TIME_EXECUTION = "BackgroundImagesTimeExecution";

    private JTextField imageFolder;
    private JPanel rootPanel;
    private JButton chooser;
    private JTextField timeExecution;

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
        chooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                String current = imageFolder.getText();
                if (!current.isEmpty()) {
                    fc.setCurrentDirectory(new File(current));
                }
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(rootPanel);

                File file = fc.getSelectedFile();
                String path = file == null
                        ? ""
                        : file.getAbsolutePath();
                imageFolder.setText(path);
            }
        });
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        PropertiesComponent prop    = PropertiesComponent.getInstance();
        String storedFolder         = prop.getValue(FOLDER);
        String storedTimeExecution  = prop.getValue(TIME_EXECUTION);
        String uiFolder             = imageFolder.getText();
        String uiTimeExecution      = timeExecution.getText();
        if (storedFolder == null) {
            storedFolder = "";
        }
        if (storedTimeExecution == null) {
            storedTimeExecution = "";
        }

        return !storedFolder.equals(uiFolder) || !storedTimeExecution.equals(uiTimeExecution);
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(FOLDER, imageFolder.getText());

        String timeExecutionValue = timeExecution.getText();
        prop.setValue(TIME_EXECUTION, timeExecutionValue);

        ScheduledExecutorServiceHandler.shutdownExecution();
        ActionManager.getInstance().getAction("randomBackgroundImage").actionPerformed(null);
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        imageFolder.setText(prop.getValue(FOLDER));
        timeExecution.setText(prop.getValue(TIME_EXECUTION));
    }

    @Override
    public void disposeUIResources() {}
}
