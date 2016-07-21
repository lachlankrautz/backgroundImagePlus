package co.notime.intellijPlugin.backgroundImageSwitch;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class SetImageFolder extends AnAction {

    static final String FOLDER = "BackgroundImagesFolder";

    public SetImageFolder() {
        super("Set Image Folder");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(
                project,
                "Select dir",
                "Set Background Images Dir",
                Messages.getQuestionIcon());

        if (txt == null) {
            return;
        }

        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(FOLDER, txt);

        Notification n = new Notification(
                "extras",
                "Extras",
                "Set images folder to: " + txt,
                NotificationType.INFORMATION);
        Notifications.Bus.notify(n);
    }

}
