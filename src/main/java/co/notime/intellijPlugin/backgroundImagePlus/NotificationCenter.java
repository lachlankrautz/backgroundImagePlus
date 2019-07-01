package co.notime.intellijPlugin.backgroundImagePlus;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
class NotificationCenter {

    static void notice(String message) {
        Notification n = new Notification(
                "extras",
                "Notice",
                message,
                NotificationType.INFORMATION);
        Notifications.Bus.notify(n);
    }

}
