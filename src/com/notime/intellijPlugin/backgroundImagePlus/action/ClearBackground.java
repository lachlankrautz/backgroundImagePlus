package com.notime.intellijPlugin.backgroundImagePlus.action;

import com.notime.intellijPlugin.backgroundImagePlus.BackgroundService;
import com.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class ClearBackground extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, null);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
        prop.setValue(Settings.AUTO_CHANGE, false);
        BackgroundService.stop();
    }

}
