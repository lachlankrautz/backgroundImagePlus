package com.notime.intellijPlugin.backgroundImagePlus;

import com.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
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
        int opacity = prop.getInt(Settings.OPACITY, Settings.OPACITY_SPINNER_DEFAULT);
        image += "," + opacity;
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, image);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
        //IdeBackgroundUtil.repaintAllWindows(); //the new version idea don't need to do this
    }
    
}
