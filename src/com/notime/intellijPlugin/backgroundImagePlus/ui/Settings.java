package com.notime.intellijPlugin.backgroundImagePlus.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.notime.intellijPlugin.backgroundImagePlus.BackgroundService;
import org.apache.commons.lang.StringUtils;
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
    public static final String KEEP_SAME_IMAGE = "BackgroundImagesKeepSameImage";
    public static final String INTERVAL = "BackgroundImagesInterval";
    public static final String TIME_UNIT = "BackgroundImagesTimeUnit";
    public static final String RADIO_BUTTON = "BackgroundImagesRadioButton";
    public static final Integer INTERVAL_SPINNER_DEFAULT = 0;
    public static final Integer TIME_UNIT_DEFAULT = 1;
    
    private TextFieldWithBrowseButton imageFolder;
    private JPanel rootPanel;
    private JSpinner intervalSpinner;
    private JCheckBox autoChangeCheckBox;
    
    private JComboBox<String> timeUnitBox;
    
    private JRadioButton editorRadioButton;
    private JRadioButton frameRadioButton;
    private JRadioButton bothRadioButton;
    private JCheckBox keepSameImageCheckBox;
    
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
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        imageFolder.addBrowseFolderListener(new TextBrowseFolderListener(descriptor) {
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
                String path = file == null ? "" : file.getAbsolutePath();
                imageFolder.setText(path);
            }
        });
        autoChangeCheckBox.addActionListener(e -> intervalSpinner.setEnabled(autoChangeCheckBox.isSelected()));
        return rootPanel;
    }
    
    @Override
    public boolean isModified() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String storedFolder = prop.getValue(FOLDER);
        String uiFolder = imageFolder.getText();
        if (storedFolder == null) {
            storedFolder = "";
        }
        return !storedFolder.equals(uiFolder)
                || intervalModified(prop)
                || timeUnitModified(prop)
                || radioButtonModified(prop)
                || prop.getBoolean(AUTO_CHANGE) != autoChangeCheckBox.isSelected()
                || prop.getBoolean(KEEP_SAME_IMAGE) != keepSameImageCheckBox.isSelected();
    }
    
    private boolean intervalModified(PropertiesComponent prop) {
        int storedInterval = prop.getInt(INTERVAL, INTERVAL_SPINNER_DEFAULT);
        int uiInterval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();
        return storedInterval != uiInterval;
    }
    
    private boolean timeUnitModified(PropertiesComponent prop) {
        int timeUnit = timeUnitBox.getSelectedIndex();
        int storedTimeUnit = prop.getInt(TIME_UNIT, TIME_UNIT_DEFAULT);
        return storedTimeUnit != timeUnit;
    }
    
    private boolean radioButtonModified(PropertiesComponent prop) {
        String storedText = prop.getValue(RADIO_BUTTON, IdeBackgroundUtil.EDITOR_PROP + "," + IdeBackgroundUtil.EDITOR_PROP);
        return !storedText.equals(radioButtonText());
    }
    
    private void selectRadioButton(String text) {
        if (StringUtils.isEmpty(text)) {
            text = IdeBackgroundUtil.EDITOR_PROP + "," + IdeBackgroundUtil.FRAME_PROP;
        }
        editorRadioButton.setSelected(IdeBackgroundUtil.EDITOR_PROP.equals(text));
        frameRadioButton.setSelected(IdeBackgroundUtil.FRAME_PROP.equals(text));
        bothRadioButton.setSelected((IdeBackgroundUtil.EDITOR_PROP + "," + IdeBackgroundUtil.FRAME_PROP).equals(text));
    }
    
    private String radioButtonText() {
        String text;
        if (editorRadioButton.isSelected()) {
            text = IdeBackgroundUtil.EDITOR_PROP;
        } else if (frameRadioButton.isSelected()) {
            text = IdeBackgroundUtil.FRAME_PROP;
        } else {
            text = IdeBackgroundUtil.EDITOR_PROP + "," + IdeBackgroundUtil.FRAME_PROP;
        }
        return text;
    }
    
    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        
        boolean autoChange = autoChangeCheckBox.isSelected();
        int interval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();
        int timeUnit = timeUnitBox.getSelectedIndex();
        
        prop.setValue(FOLDER, imageFolder.getText());
        prop.setValue(INTERVAL, interval, INTERVAL_SPINNER_DEFAULT);
        prop.setValue(AUTO_CHANGE, autoChange);
        prop.setValue(KEEP_SAME_IMAGE, keepSameImageCheckBox.isSelected());
        prop.setValue(TIME_UNIT, timeUnit, TIME_UNIT_DEFAULT);
        prop.setValue(RADIO_BUTTON, radioButtonText());
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
        intervalSpinner.setValue(prop.getInt(INTERVAL, INTERVAL_SPINNER_DEFAULT));
        autoChangeCheckBox.setSelected(prop.getBoolean(AUTO_CHANGE, false));
        keepSameImageCheckBox.setSelected(prop.getBoolean(KEEP_SAME_IMAGE, false));
        intervalSpinner.setEnabled(autoChangeCheckBox.isSelected());
        timeUnitBox.setSelectedIndex(prop.getInt(TIME_UNIT, TIME_UNIT_DEFAULT));
        selectRadioButton(prop.getValue(RADIO_BUTTON));
    }
    
    @Override
    public void disposeUIResources() {
    }
    
    private void createUIComponents() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        intervalSpinner = new JSpinner(new SpinnerNumberModel(prop.getInt(INTERVAL, INTERVAL_SPINNER_DEFAULT), 0, 1000, 5));
        timeUnitBox = new ComboBox<>();
        timeUnitBox.addItem("SECONDS");
        timeUnitBox.addItem("MINUTES");
        timeUnitBox.addItem("HOURS");
        timeUnitBox.addItem("DAYS");
        timeUnitBox.setSelectedIndex(prop.getInt(TIME_UNIT, TIME_UNIT_DEFAULT));
    }
    
}
