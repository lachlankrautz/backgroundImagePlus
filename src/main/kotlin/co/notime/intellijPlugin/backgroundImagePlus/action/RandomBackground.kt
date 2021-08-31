package co.notime.intellijPlugin.backgroundImagePlus.action

import co.notime.intellijPlugin.backgroundImagePlus.BackgroundService
import co.notime.intellijPlugin.backgroundImagePlus.RandomBackgroundTask.Companion.instance
import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
class RandomBackground : AnAction("Random Background Image") {
    override fun actionPerformed(evt: AnActionEvent) {
        val prop = PropertiesComponent.getInstance()
        instance.run()
        if (prop.getBoolean(Settings.AUTO_CHANGE, false)) {
            BackgroundService.restart()
        }
    }

    init {
        val prop = PropertiesComponent.getInstance()
        if (prop.getBoolean(Settings.AUTO_CHANGE, false)) {
            BackgroundService.start()
        }
    }
}