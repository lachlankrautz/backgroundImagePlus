package co.notime.intellijPlugin.backgroundImagePlus

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import java.awt.EventQueue
import java.io.File

class RandomBackgroundTask : Runnable {

    private val imagesHandler: ImagesHandler = ImagesHandler()

    override fun run() {
        val prop = PropertiesComponent.getInstance()

        val folder = prop.getValue(Settings.FOLDER, "")
        if (folder.isEmpty()) {
            NotificationCenter.notice("Image folder not set")
            return
        }
        val file = File(folder)
        if (!file.exists()) {
            NotificationCenter.notice("Image folder not set")
            return
        }

        val image = imagesHandler.getRandomImage(folder)
        if (image == null) {
            NotificationCenter.notice("No image found")
            return
        }
        if (image.contains(",")) {
            NotificationCenter.notice("Intellij wont load images with ',' character\n$image")
        }

        val o = prop.getInt(Settings.OPACITY, 15)
        val opacity = if (o < 0 || o > 100) {
            NotificationCenter.notice("opacity must be between [0,100],Your value is not within this range,The value is set to a default value of 15")
            15
        } else o

        //默认透明度设为25
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, "$image,$opacity")
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, "$image,$opacity")
        // 添加相关事件
        EventQueue.invokeLater {
            IdeBackgroundUtil.repaintAllWindows()
        }
    }

    companion object {
        val instance: RandomBackgroundTask by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RandomBackgroundTask() }
    }
}
