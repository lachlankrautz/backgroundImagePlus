package com.notime.intellijPlugin.backgroundImagePlus;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: He Lili
 * Date:   2018/10/22
 */
public enum ImagesHandlerSingleton {
    
    /**
     * 实例
     */
    instance;
    
    private MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
    
    private List<String> randomImageList = null;
    
    private String lastFolder = null;
    
    public String getRandomImage(String folder) {
        // 文件夹改动或者完成了一次循环
        if (!folder.equals(lastFolder) || randomImageList == null || randomImageList.size() == 0) {
            randomImageList = this.getRandomImageList(folder);
        }
        lastFolder = folder;
        while (randomImageList != null && randomImageList.size() > 0 && !isImage(new File(randomImageList.get(0)))) {
            randomImageList.remove(0);
        }
        return randomImageList == null || randomImageList.size() == 0 ? null : randomImageList.remove(0);
    }
    
    public void resetRandomImageList() {
        randomImageList = lastFolder == null ? null : this.getRandomImageList(lastFolder);
    }
    
    private List<String> getRandomImageList(String folder) {
        if (folder.isEmpty()) {
            return null;
        }
        List<String> images = new ArrayList<>();
        collectImages(images, folder);
        Collections.shuffle(images);
        return images;
    }
    
    private void collectImages(List<String> images, String folder) {
        File root = new File(folder);
        if (root.exists()) {
            File[] list = root.listFiles();
            if (list != null) {
                for (File f : list) {
                    if (f.isDirectory()) {
                        collectImages(images, f.getAbsolutePath());
                    } else {
                        if (isImage(f)) {
                            images.add(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
    
    private boolean isImage(File file) {
        String[] parts = typeMap.getContentType(file).split("/");
        return parts.length != 0 && "image".equals(parts[0]);
    }
    
}
