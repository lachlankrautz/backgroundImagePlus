package com.notime.intellijPlugin.backgroundImagePlus;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Author: Lachlan Krautz
 * Date:   22/07/16
 * Modified by He Lili   Date: 18/10/18
 */
public class BackgroundService {
    
    private static ScheduledThreadPoolExecutor executor = null;
    
    public static void start() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        int interval = prop.getInt(Settings.INTERVAL, Settings.INTERVAL_SPINNER_DEFAULT);
        int timeUnit = prop.getInt(Settings.TIME_UNIT, Settings.TIME_UNIT_DEFAULT);
        if (interval == 0) {
            return;
        }
        if (executor != null) {
            stop();
        }
        RandomBackgroundTask task = new RandomBackgroundTask();
        executor = new ScheduledThreadPoolExecutor(1, new MyThreadFactory("RandomBackgroundTask"));
        try {
            int delay = prop.isValueSet(IdeBackgroundUtil.EDITOR_PROP) ? interval : 0;
            TimeUnit timeUnitEnum;
            switch (timeUnit) {
                case 0:
                    timeUnitEnum = TimeUnit.SECONDS;
                    break;
                case 1:
                    timeUnitEnum = TimeUnit.MINUTES;
                    break;
                case 2:
                    timeUnitEnum = TimeUnit.HOURS;
                    break;
                case 3:
                    timeUnitEnum = TimeUnit.DAYS;
                    break;
                default:
                    timeUnitEnum = TimeUnit.MINUTES;
                    break;
            }
            executor.scheduleAtFixedRate(task, delay, interval, timeUnitEnum);
        } catch (RejectedExecutionException e) {
            stop();
        }
    }
    
    public static void stop() {
        if (executor != null && !executor.isTerminated()) {
            executor.shutdownNow();
        }
        executor = null;
    }
    
    public static void restart() {
        stop();
        start();
    }
    
    public static class MyThreadFactory implements ThreadFactory {
        
        private int counter;
        
        private String name;
        
        MyThreadFactory(String name) {
            counter = 0;
            this.name = name;
        }
        
        @Override
        public Thread newThread(@NotNull Runnable run) {
            Thread t = new Thread(run, name + "-Thread-" + counter);
            counter++;
            return t;
        }
        
    }
    
}
