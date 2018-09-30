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
		String lastImage = prop.getValue(IdeBackgroundUtil.EDITOR_PROP);//get last settings of image
		if (lastImage != null && lastImage.contains(",")) {//when params setted,keep them
			image += lastImage.substring(lastImage.indexOf(','));
		}
		prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
		prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
		// NotificationCenter.notice("Image: " + image.replace(folder + File.separator, ""));
        //IdeBackgroundUtil.repaintAllWindows(); //the new version idea don't need to do this
    }

}
