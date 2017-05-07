package co.notime.intellijPlugin.backgroundImagePlus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
public class ScheduledExecutorServiceHandler {

   private static ScheduledExecutorService service;
   private Builder builder;

   private ScheduledExecutorServiceHandler(Builder builder) {
      this.builder = builder;
      service = Executors.newScheduledThreadPool(1);
   }

   public void scheduleAtFixedRate() {
      service.scheduleAtFixedRate(builder.task, builder.initialDelay, builder.period, builder.unit);
   }

   public static void shutdownExecution() {
      if (service != null && !service.isTerminated()) {
         service.shutdownNow();
      }
   }

   public static final class Builder {
      private RandomBackgroundTask task;
      private int initialDelay;
      private int period;
      private TimeUnit unit;

      private Builder() {
      }

      public static Builder createScheduler() {
         return new Builder();
      }

      public Builder withTask(RandomBackgroundTask task) {
         this.task = task;
         return this;
      }

      public Builder withInitialDelay(int initialDelay) {
         this.initialDelay = initialDelay;
         return this;
      }

      public Builder withPeriod(int period) {
         this.period = period;
         return this;
      }

      public Builder withTimeUnit(TimeUnit unit) {
         this.unit = unit;
         return this;
      }

      public ScheduledExecutorServiceHandler build() {
         return new ScheduledExecutorServiceHandler(this);
      }
   }
}
