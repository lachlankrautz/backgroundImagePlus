package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class RandomBackground extends AnAction {

    private MimetypesFileTypeMap typeMap;

    public RandomBackground() {
        super("Random Background Image");
        typeMap = new MimetypesFileTypeMap();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String folder = prop.getValue(Settings.FOLDER);
        if (folder == null || folder.isEmpty()) {
            notice("Image folder not set");
            return;
        }
        File file = new File(folder);
        if (!file.exists()) {
            notice("Image folder not set");
            return;
        }
        String image = getRandomImage(folder);
        if (image == null) {
            notice("No image found");
            return;
        }
        // notice("Setting image to: " + image);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
        IdeBackgroundUtil.repaintAllWindows();
    }

    private void notice (String message) {
        Notification n = new Notification(
                "extras",
                "Notice",
                message,
                NotificationType.INFORMATION);
        Notifications.Bus.notify(n);
    }

    /**
     *
     * @param folder folder to search for images
     * @return random image or null
     */
    private String getRandomImage (String folder) {
        if (folder.isEmpty()) {
            return null;
        }
        List<String> images = new ArrayList<String>();
        collectImages(images, folder);
        int count = images.size();
        if (count == 0) {
            return null;
        }
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(images.size());
        return images.get(index);
    }

    private void collectImages (List<String> images, String folder) {
        File root = new File(folder);
        if (!root.exists()) {
            return;
        }
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                collectImages(images, f.getAbsolutePath());
            }
            else {
                if (!isImage(f)) {
                    continue;
                }
                images.add(f.getAbsolutePath());
            }
        }
    }

    private boolean isImage (File file) {
        String[] parts = typeMap.getContentType(file).split("/");
        return parts.length != 0 && parts[0].equals("image");
    }

}
