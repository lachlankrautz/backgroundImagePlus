package co.notime.intellijPlugin.backgroundImageSwitch;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Author: Lachlan Krautz
 * Date:   21/07/16
 */
public class RandomBackground extends AnAction {

    public RandomBackground() {
        super("Random Background Image");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(
                project,
                "What is your name?",
                "Input Your Name",
                Messages.getQuestionIcon());
        Messages.showMessageDialog(
                project,
                "Hello, " + txt + "!\n I am glad to see you.",
                "Information",
                Messages.getInformationIcon());
    }
}
