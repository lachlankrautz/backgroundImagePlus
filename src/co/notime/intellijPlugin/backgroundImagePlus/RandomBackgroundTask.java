package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.io.File;
import java.util.Locale;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
public class RandomBackgroundTask implements Runnable {

    private ImagesHandler imagesHandler;

    public RandomBackgroundTask() {
        imagesHandler = new ImagesHandler();
    }

    @Override
    public void run() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String folder = prop.getValue(Settings.FOLDER);
        if (folder == null || folder.isEmpty()) {
            NotificationCenter.notice("Image folder not set");
            return;
        }
        File file = new File(folder);
        if (!file.exists()) {
            NotificationCenter.notice("Image folder not set");
            return;
        }
        String image = imagesHandler.getRandomImage(folder);
        if (image == null) {
            NotificationCenter.notice("No image found");
            return;
        }
        if (image.contains(",")) {
            NotificationCenter.notice("Intellij wont load images with ',' character\n" + image);
        }

        // Use the parameters of the previous image
        String orgImage = prop.getValue(IdeBackgroundUtil.EDITOR_PROP);
        if (orgImage != null){
            String value = orgImage;
            String[] split = value.split(",");
            int opacity = split.length > 1? StringUtil.parseInt(split[1], 15):15;
            String fill = split.length > 2?split[2]:"scale";
            String place = split.length > 3?split[3]:"center";
            image = image + "," + opacity + "," + (fill + "," + place).toLowerCase(Locale.ENGLISH);
        }

        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
        // NotificationCenter.notice("Image: " + image.replace(folder + File.separator, ""));
        IdeBackgroundUtil.repaintAllWindows();
    }

}
