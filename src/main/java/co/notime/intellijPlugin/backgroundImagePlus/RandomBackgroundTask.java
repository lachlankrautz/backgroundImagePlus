package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.io.File;

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
        int opacity = prop.getInt(Settings.OPACITY, 15);
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
        if (opacity < 0 || opacity > 100) {
            NotificationCenter.notice("opacity must be between [0,100],Your value is not within this range,The value is set to a default value of 15");
            opacity = 15;
        }

        //默认透明度设为25
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, image + "," + opacity);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image + "," + opacity);
        IdeBackgroundUtil.repaintAllWindows();
    }

}
