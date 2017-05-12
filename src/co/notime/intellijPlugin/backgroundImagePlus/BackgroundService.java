package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 */
public class BackgroundService {

    private static ScheduledExecutorService service = null;
    private static int runningInterval = 0;

    public static void start () {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        int interval = prop.getInt(Settings.INTERVAL, 0);
        if (runningInterval == interval || interval == 0) {
            return;
        }
        if (service != null) {
            stop();
        }
        RandomBackgroundTask task = new RandomBackgroundTask();
        service = Executors.newSingleThreadScheduledExecutor();
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

    public static void stop () {
        if (service != null && !service.isTerminated()) {
            service.shutdownNow();
        }
        service = null;
        runningInterval = 0;
    }

    public static void restart () {
        stop();
        start();
    }

}
