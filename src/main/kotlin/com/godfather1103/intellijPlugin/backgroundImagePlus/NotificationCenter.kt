package com.godfather1103.intellijPlugin.backgroundImagePlus

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
internal object NotificationCenter {
    fun notice(message: String) {
        val n = Notification(
            Notifications.SYSTEM_MESSAGES_GROUP_ID,
            "Notice",
            message,
            NotificationType.INFORMATION
        )
        Notifications.Bus.notify(n)
    }
}