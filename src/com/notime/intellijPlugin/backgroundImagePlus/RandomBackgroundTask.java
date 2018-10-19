package com.notime.intellijPlugin.backgroundImagePlus;

import com.intellij.ide.util.PropertiesComponent;
import com.notime.intellijPlugin.backgroundImagePlus.ui.Settings;

import java.io.File;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 * Modified by He Lili   Date: 19/10/18
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
        String[] radioButton = prop.getValue(Settings.RADIO_BUTTON).split(",");
        boolean keepSameImage = prop.getBoolean(Settings.KEEP_SAME_IMAGE);
        if (keepSameImage && radioButton.length == 2) {
        
        }
        String image = null;
        for (int i = 0; i < radioButton.length; i++) {
            String type = radioButton[i];
            if (folder == null || folder.isEmpty()) {
                NotificationCenter.notice("Image folder not set");
                return;
            }
            File file = new File(folder);
            if (!file.exists()) {
                NotificationCenter.notice("Image folder not set");
                return;
            }
            String lastImage = prop.getValue(type);
            if (!keepSameImage || i <= 0) {
                image = imagesHandler.getRandomImage(folder, lastImage);
            }
            if (image == null) {
                NotificationCenter.notice("No image found");
                return;
            }
            if (image.contains(",")) {
                NotificationCenter.notice("Intellij wont load images with ',' character\n" + image);
            }
            if (lastImage != null && lastImage.contains(",")) {
                prop.setValue(type, image + lastImage.substring(lastImage.indexOf(",")));
            } else {
                prop.setValue(type, image);
            }
        }
    }
    
}
