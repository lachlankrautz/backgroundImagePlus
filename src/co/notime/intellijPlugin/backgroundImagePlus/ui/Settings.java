package co.notime.intellijPlugin.backgroundImagePlus.ui;

import co.notime.intellijPlugin.backgroundImagePlus.BackgroundService;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class Settings implements Configurable {

    public static final String FOLDER = "BackgroundImagesFolder";
    public static final String AUTO_CHANGE = "BackgroundImagesAutoChange";
    public static final String INTERVAL = "BackgroundImagesInterval";
    public static final String TMP_FOLDER = "BackgroundImagesTmpFolder";

    private TextFieldWithBrowseButton imageFolder;
    private JPanel rootPanel;
    private JSpinner intervalSpinner;
    private JCheckBox autoChangeCheckBox;

    @SuppressWarnings("unused")
    private JLabel measurement;
    private TextFieldWithBrowseButton tmpFolderForZips;

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
        FileChooserDescriptor imageFolderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        FileChooserDescriptor tmpFolderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        class SetFieldWithBrowser extends TextBrowseFolderListener {
            private TextFieldWithBrowseButton directoryField;

            public SetFieldWithBrowser(FileChooserDescriptor descriptor, TextFieldWithBrowseButton directoryField) {
                super(descriptor);
                this.directoryField = directoryField;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                String current = directoryField.getText();
                if (!current.isEmpty()) {
                    fc.setCurrentDirectory(new File(current));
                }
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(rootPanel);

                File file = fc.getSelectedFile();
                String path = file == null
                        ? ""
                        : file.getAbsolutePath();
                directoryField.setText(path);
            }
        }
        imageFolder.addBrowseFolderListener(new SetFieldWithBrowser(imageFolderDescriptor,imageFolder));
        tmpFolderForZips.addBrowseFolderListener(new SetFieldWithBrowser(tmpFolderDescriptor, tmpFolderForZips));
        autoChangeCheckBox.addActionListener(e -> intervalSpinner.setEnabled(autoChangeCheckBox.isSelected()));
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
        return !storedFolder.equals(uiFolder)
                || intervalModified(prop)
                || prop.getBoolean(AUTO_CHANGE) != autoChangeCheckBox.isSelected()
                || !prop.getValue(TMP_FOLDER,"").equals(tmpFolderForZips.getText());
    }

    private boolean intervalModified (PropertiesComponent prop) {
        int storedInterval = prop.getInt(INTERVAL, 0);
        int uiInterval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();
        return storedInterval != uiInterval;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent prop = PropertiesComponent.getInstance();

        boolean autoChange = autoChangeCheckBox.isSelected();
        int interval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();

        prop.setValue(FOLDER, imageFolder.getText());
        prop.setValue(TMP_FOLDER, tmpFolderForZips.getText());
        prop.setValue(INTERVAL, interval, 0);
        prop.setValue(AUTO_CHANGE, autoChange);
        intervalSpinner.setEnabled(autoChange);

        if (autoChange && interval > 0) {
            BackgroundService.start();
        } else {
            BackgroundService.stop();
        }
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        imageFolder.setText(prop.getValue(FOLDER));
        tmpFolderForZips.setText(prop.getValue(TMP_FOLDER));
        intervalSpinner.setValue(prop.getInt(INTERVAL, 0));
        autoChangeCheckBox.setSelected(prop.getBoolean(AUTO_CHANGE, false));
        intervalSpinner.setEnabled(autoChangeCheckBox.isSelected());
    }

    @Override
    public void disposeUIResources() {}

    private void createUIComponents() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        intervalSpinner = new JSpinner(new SpinnerNumberModel(prop.getInt(INTERVAL, 0), 0, 1000, 5));
    }

}
