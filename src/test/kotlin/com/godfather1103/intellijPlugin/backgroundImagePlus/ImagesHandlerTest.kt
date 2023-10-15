package com.godfather1103.intellijPlugin.backgroundImagePlus

import org.junit.Test

class ImagesHandlerTest {
    private val imagesHandler: ImagesHandler = ImagesHandler()

    @Test
    fun getRandomImage() {
        println(imagesHandler.getRandomImage("/Users/admin/Pictures/"))
    }
}