package co.notime.intellijPlugin.backgroundImageSwitch;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class RandomBackground extends AnAction {

    public RandomBackground() {
        super("Random Background Image");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String folder = prop.getValue(SetImageFolder.FOLDER);
        if (folder == null) {
            Notification n = new Notification(
                    "extras",
                    "Error",
                    "Image folder not set",
                    NotificationType.ERROR);
            Notifications.Bus.notify(n);
            return;
        }
        String image = getRandomImage(folder);
        if (image == null) {
            Notification n = new Notification(
                    "extras",
                    "Error",
                    "Image not found",
                    NotificationType.ERROR);
            Notifications.Bus.notify(n);
            return;
        }
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
        IdeBackgroundUtil.repaintAllWindows();
    }

    private String getRandomImage (String folder) {
        List<String> images = new ArrayList<String>();
        collectImages(images, folder);
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(images.size());
        return images.get(index);
    }

    private void collectImages (List<String> images, String folder) {
        File root   = new File(folder);
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                collectImages(images, f.getAbsolutePath());
            }
            else {
                images.add(f.getAbsolutePath());
            }
        }
    }

}
