package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.io.File;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
class RandomBackgroundTask implements Runnable {

   private PropertiesComponent prop;
   private ImagesHandler imagesHandler;

   private RandomBackgroundTask(Builder builder) {
      this.prop = builder.prop;
      imagesHandler = new ImagesHandler();
   }

   @Override
   public void run() {
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
      // notice("Setting image to: " + image);
      prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
      prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
      IdeBackgroundUtil.repaintAllWindows();
   }

   public static final class Builder {
      private PropertiesComponent prop;

      private Builder() {
      }

      public static Builder createTask() {
         return new Builder();
      }

      public Builder withProp(PropertiesComponent prop) {
         this.prop = prop;
         return this;
      }

      public RandomBackgroundTask build() {
         return new RandomBackgroundTask(this);
      }
   }
}
