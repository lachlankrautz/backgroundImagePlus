package co.notime.intellijPlugin.backgroundImagePlus.action;

import co.notime.intellijPlugin.backgroundImagePlus.BackgroundService;
import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import co.notime.intellijPlugin.backgroundImagePlus.RandomBackgroundTask;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class RandomBackground extends AnAction {

    public RandomBackground() {
        super("Random Background Image");
        PropertiesComponent prop = PropertiesComponent.getInstance();
        if (prop.getBoolean(Settings.AUTO_CHANGE, false)) {
            BackgroundService.start();
        }
    }

    @Override
    public void actionPerformed(AnActionEvent evt) {
        RandomBackgroundTask task = new RandomBackgroundTask();
        task.run();
        PropertiesComponent prop = PropertiesComponent.getInstance();
        if (prop.getBoolean(Settings.AUTO_CHANGE, false)) {
            BackgroundService.restart();
        }
    }

}
