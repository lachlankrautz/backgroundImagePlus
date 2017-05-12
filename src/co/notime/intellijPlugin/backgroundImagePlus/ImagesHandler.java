package co.notime.intellijPlugin.backgroundImagePlus;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
class ImagesHandler {

    private MimetypesFileTypeMap typeMap;

    ImagesHandler() {
        typeMap = new MimetypesFileTypeMap();
    }

    /**
     * @param folder folder to search for images
     * @return random image or null
     */
    String getRandomImage(String folder) {
        if (folder.isEmpty()) {
            return null;
        }
        List<String> images = new ArrayList<>();
        collectImages(images, folder);
        int count = images.size();
        if (count == 0) {
            return null;
        }
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(images.size());
        return images.get(index);
    }

    private void collectImages(List<String> images, String folder) {
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
            } else {
                if (!isImage(f)) {
                    continue;
                }
                images.add(f.getAbsolutePath());
            }
        }
    }

    private boolean isImage(File file) {
        String[] parts = typeMap.getContentType(file).split("/");
        return parts.length != 0 && parts[0].equals("image");
    }

}
