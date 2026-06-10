package base;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import org.openqa.selenium.Keys;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestFailureListener extends BaseClass implements ITestListener{

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            // 1. Beep sound
            Toolkit.getDefaultToolkit().beep();

            // 2. Desktop notification
            if (SystemTray.isSupported()) {

                SystemTray tray = SystemTray.getSystemTray();

                Image image = Toolkit.getDefaultToolkit()
                        .createImage(new byte[0]);

                TrayIcon trayIcon = new TrayIcon(image, "Automation");
                trayIcon.setImageAutoSize(true);

                try {
                    tray.add(trayIcon);

                    trayIcon.displayMessage(
                            "Test Failed",
                            result.getName() + " has failed.",
                            TrayIcon.MessageType.ERROR);

                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }

            // 3. ALT + TAB
            actions.keyDown(Keys.ALT)
                   .sendKeys(Keys.TAB)
                   .sendKeys(Keys.TAB)
                   .keyUp(Keys.ALT)
                   .build()
                   .perform();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}