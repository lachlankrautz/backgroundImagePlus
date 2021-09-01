package com.godfather1103.intellijPlugin.backgroundImagePlus.ui

import com.godfather1103.intellijPlugin.backgroundImagePlus.BackgroundService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import org.jetbrains.annotations.Nls
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
class Settings : Configurable {
    private var imageFolder: TextFieldWithBrowseButton? = null
    private var rootPanel: JPanel? = null
    private var intervalSpinner: JSpinner? = null
    private var opacitySpinner: JSpinner? = null
    private var autoChangeCheckBox: JCheckBox? = null
    private var measurement: JLabel? = null
    private var opacityLabel: JLabel? = null

    override fun getDisplayName(): @Nls String? {
        return "Background Image"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent? {
        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
        imageFolder!!.addBrowseFolderListener(object : TextBrowseFolderListener(descriptor) {
            override fun actionPerformed(e: ActionEvent) {
                val fc = JFileChooser()
                val current = imageFolder!!.text
                if (!current.isEmpty()) {
                    fc.currentDirectory = File(current)
                }
                fc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                fc.showOpenDialog(rootPanel)
                val file = fc.selectedFile
                val path = if (file == null) "" else file.absolutePath
                imageFolder!!.text = path
            }
        })
        autoChangeCheckBox!!.addActionListener { e: ActionEvent? ->
            intervalSpinner!!.isEnabled = autoChangeCheckBox!!.isSelected
        }
        return rootPanel
    }

    override fun isModified(): Boolean {
        val prop = PropertiesComponent.getInstance()
        var storedFolder = prop.getValue(FOLDER)
        val uiFolder = imageFolder!!.text
        if (storedFolder == null) {
            storedFolder = ""
        }
        return (storedFolder != uiFolder
                || opacityModified(prop)
                || intervalModified(prop)
                || prop.getBoolean(AUTO_CHANGE) != autoChangeCheckBox!!.isSelected)
    }

    private fun intervalModified(prop: PropertiesComponent): Boolean {
        val storedInterval = prop.getInt(INTERVAL, 0)
        val uiInterval = (intervalSpinner!!.model as SpinnerNumberModel).number.toInt()
        return storedInterval != uiInterval
    }

    private fun opacityModified(prop: PropertiesComponent): Boolean {
        val opacity = (opacitySpinner!!.model as SpinnerNumberModel).number.toInt()
        val storedOpacity = prop.getInt(OPACITY, 15)
        return storedOpacity != opacity
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val prop = PropertiesComponent.getInstance()
        val autoChange = autoChangeCheckBox!!.isSelected
        val interval = (intervalSpinner!!.model as SpinnerNumberModel).number.toInt()
        val opacity = (opacitySpinner!!.model as SpinnerNumberModel).number.toInt()
        prop.setValue(FOLDER, imageFolder!!.text)
        prop.setValue(INTERVAL, interval, 0)
        prop.setValue(AUTO_CHANGE, autoChange)
        prop.setValue(OPACITY, opacity, 15)
        intervalSpinner!!.isEnabled = autoChange
        if (autoChange && interval > 0) {
            BackgroundService.restart()
        } else {
            BackgroundService.stop()
        }
    }

    override fun reset() {
        val prop = PropertiesComponent.getInstance()
        imageFolder!!.setText(prop.getValue(FOLDER))
        intervalSpinner!!.value =
            prop.getInt(INTERVAL, 0)
        autoChangeCheckBox!!.isSelected =
            prop.getBoolean(AUTO_CHANGE, false)
        intervalSpinner!!.isEnabled = autoChangeCheckBox!!.isSelected
        opacitySpinner!!.value = prop.getInt(OPACITY, 15)
    }

    override fun disposeUIResources() {}
    private fun createUIComponents() {
        val prop = PropertiesComponent.getInstance()
        intervalSpinner = JSpinner(SpinnerNumberModel(prop.getInt(INTERVAL, 0), 0, 1000, 5))
    }

    companion object {
        const val FOLDER = "BackgroundImagesFolder"
        const val AUTO_CHANGE = "BackgroundImagesAutoChange"
        const val INTERVAL = "BackgroundImagesInterval"
        const val OPACITY = "BackgroundImagesOpacity"
    }
}