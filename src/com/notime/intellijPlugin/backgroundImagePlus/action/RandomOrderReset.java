package com.notime.intellijPlugin.backgroundImagePlus.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.notime.intellijPlugin.backgroundImagePlus.BackgroundService;
import com.notime.intellijPlugin.backgroundImagePlus.ImagesHandlerSingleton;
import com.notime.intellijPlugin.backgroundImagePlus.RandomBackgroundTask;
import com.notime.intellijPlugin.backgroundImagePlus.ui.Settings;

public class RandomOrderReset extends AnAction {
    
    @Override
    public void actionPerformed(AnActionEvent e) {
        ImagesHandlerSingleton.instance.resetRandomImageList();
        PropertiesComponent prop = PropertiesComponent.getInstance();
        if (prop.getBoolean(Settings.AUTO_CHANGE, false)) {
            BackgroundService.restart();
        } else {
            new RandomBackgroundTask().run();
        }
    }
}
