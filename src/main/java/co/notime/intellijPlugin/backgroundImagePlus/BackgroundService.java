package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.util.concurrent.*;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class BackgroundService {

    private static ScheduledExecutorService service;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("BackgroundService-%d").build();
        service = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
    }

    private static int runningInterval = 0;

    public static void start() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        int interval = prop.getInt(Settings.INTERVAL, 0);
        if (runningInterval == interval || interval == 0) {
            return;
        }
        if (service != null) {
            stop();
        }
        RandomBackgroundTaskKt task = RandomBackgroundTaskKt.Companion.getInstance();
        try {
            int delay = prop.isValueSet(IdeBackgroundUtil.EDITOR_PROP)
                    ? interval
                    : 0;
            service.scheduleAtFixedRate(task, delay, interval, TimeUnit.MINUTES);
            runningInterval = interval;
        } catch (RejectedExecutionException e) {
            stop();
        }
    }

    public static void stop() {
        if (service != null && !service.isTerminated()) {
            service.shutdownNow();
        }
        runningInterval = 0;
    }

    public static void restart() {
        stop();
        start();
    }

}
