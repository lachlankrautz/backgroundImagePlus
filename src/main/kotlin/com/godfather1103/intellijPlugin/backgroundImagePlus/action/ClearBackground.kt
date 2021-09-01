package com.godfather1103.intellijPlugin.backgroundImagePlus.action

import com.godfather1103.intellijPlugin.backgroundImagePlus.BackgroundService
import com.godfather1103.intellijPlugin.backgroundImagePlus.ui.Settings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
class ClearBackground : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val prop = PropertiesComponent.getInstance()
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, null)
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null)
        prop.setValue(Settings.AUTO_CHANGE, false)
        BackgroundService.stop()
        IdeBackgroundUtil.repaintAllWindows()
    }
}