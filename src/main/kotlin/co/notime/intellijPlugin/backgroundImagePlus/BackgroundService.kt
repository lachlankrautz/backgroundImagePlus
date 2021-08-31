package co.notime.intellijPlugin.backgroundImagePlus

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
object BackgroundService {
    private var service: ScheduledExecutorService? = null
    private val namedThreadFactory = ThreadFactoryBuilder().setNameFormat("BackgroundService-%d").build()

    private fun makeScheduledExecutorService(): ScheduledExecutorService {
        return ScheduledThreadPoolExecutor(1, namedThreadFactory)
    }

    private var runningInterval = 0

    @JvmStatic
    fun start() {
        val prop = PropertiesComponent.getInstance()
        val interval = prop.getInt(Settings.INTERVAL, 0)
        if (runningInterval == interval || interval == 0) {
            return
        }
        if (service != null) {
            stop()
        }
        service = makeScheduledExecutorService()
        val task = RandomBackgroundTask()
        try {
            val delay = if (prop.isValueSet(IdeBackgroundUtil.EDITOR_PROP)) interval else 0
            service!!.scheduleAtFixedRate(task, delay.toLong(), interval.toLong(), TimeUnit.MINUTES)
            runningInterval = interval
        } catch (e: RejectedExecutionException) {
            stop()
        }
    }

    @JvmStatic
    fun stop() {
        if (service != null && !service!!.isTerminated) {
            service!!.shutdownNow()
        }
        service = null
        runningInterval = 0
    }

    fun restart() {
        stop()
        start()
    }
}