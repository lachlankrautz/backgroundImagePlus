package co.notime.intellijPlugin.backgroundImagePlus;

import co.notime.intellijPlugin.backgroundImagePlus.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.concurrent.TimeUnit;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class RandomBackground extends AnAction {

    public RandomBackground() {
        super("Random Background Image");
        actionPerformed(null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        RandomBackgroundTask task = RandomBackgroundTask.Builder.createTask().withProp(prop).build();
        int timeExecution = getTimeExecution(prop);

        if(timeExecution != -1) {
           ScheduledExecutorServiceHandler scheduler = ScheduledExecutorServiceHandler.
                 Builder.
                 createScheduler().withTask(task).
                 withInitialDelay(0).
                 withPeriod(timeExecution).
                 withTimeUnit(TimeUnit.SECONDS).
                 build();

           scheduler.scheduleAtFixedRate();
        } else {
           task.run();
        }
    }

   private int getTimeExecution(PropertiesComponent prop) {
        try {
           String timeExecution = prop.getValue(Settings.TIME_EXECUTION);
           if (timeExecution.isEmpty()) {
              return -1;
           }
           return Integer.valueOf(timeExecution);
        } catch (NumberFormatException e) {
           NotificationCenter.notice("Please, specify a valid integer number as execution time.");
        }
        return -1;
   }

}
