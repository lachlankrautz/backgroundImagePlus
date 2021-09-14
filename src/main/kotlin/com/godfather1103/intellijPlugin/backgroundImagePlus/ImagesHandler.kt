package com.godfather1103.intellijPlugin.backgroundImagePlus

import java.io.File
import java.nio.file.Files
import java.util.*

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
internal class ImagesHandler {

    /**
     * @param folder folder to search for images
     * @return random image or null
     */
    fun getRandomImage(folder: String): String? {
        if (folder.isEmpty()) {
            return null
        }
        val images: MutableList<String> = ArrayList()
        collectImages(images, folder)
        val count = images.size
        if (count == 0) {
            return null
        }
        val randomGenerator = Random()
        val index = randomGenerator.nextInt(images.size)
        return images[index]
    }

    private fun collectImages(images: MutableList<String>, folder: String) {
        val root = File(folder)
        if (!root.exists()) {
            return
        }
        val list = root.listFiles() ?: return
        for (f in list) {
            if (f.isDirectory) {
                collectImages(images, f.absolutePath)
            } else {
                if (!isImage(f)) {
                    continue
                }
                images.add(f.absolutePath)
            }
        }
    }

    private fun isImage(file: File): Boolean {
        val s = Files
            .probeContentType(file.toPath()) ?: ""
        val parts = s.split("/".toRegex()).toTypedArray()
        return parts.isNotEmpty() && "image" == parts[0]
    }

}