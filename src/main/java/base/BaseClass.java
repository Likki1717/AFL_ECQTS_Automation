package base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.windows.WindowsDriver;
import pageObjects.CommonPages.Dashboard;
import pageObjects.CommonPages.SideMenu;
import pageObjects.CommonPages.SignIn;
import pageObjects.sideMenu.About;

public class BaseClass {
	public static WindowsDriver<?> driver;
	public static Actions actions;
	public static WebDriverWait wait;
	public static Robot robot;
	public static DesiredCapabilities capabilities;

	public static void clearPreviousSessionData() throws Exception {
		int deleteAttempts = 0;

		// Folder path that needs to be cleared before starting a new session
		File localStateFolder = new File(TestData.localStateFolderPath);

		// Keep trying to delete the folder if it exists
		while (localStateFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(localStateFolder);
			} else {
				// If deletion failed, likely due to files being in use
				System.out.println("****Close any open app / file and run the script again****");
				System.exit(0);
			}
		}
		// Ensure SOR directory exists; create it if missing
		File sorFolder = new File(TestData.SOR_Files_Path);
		if (!sorFolder.exists()) {
			sorFolder.mkdirs();
		}
	}

	public static void deleteDirectory(File directory) {
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				deleteDirectory(file);
			}
		}
		directory.delete();
	}

	public static void launchWinAppDriver() throws Exception {
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_WINDOWS);
			robot.keyPress(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_WINDOWS);
			robot.delay(500);
			for (char c : TestData.winAppDriverPath.toCharArray()) {
				if (c == ':') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_SEMICOLON);
					robot.keyRelease(KeyEvent.VK_SEMICOLON);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
				}

			}
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("****Exception in launchWinAppDriver()****");
			System.exit(0);
		}
	}

	public static void launchApplication() throws Exception {
		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", TestData.appId());
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("automationName", "Windows");
			driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
			actions = new Actions(driver);
			wait = new WebDriverWait(driver, 40000);
		} catch (Exception e) {
			System.out.println("****Exception in launchApplication()****");
			System.exit(0);
		}
	}

	public static void loginToApplication() {
		try {
			SignIn.usernameField().sendKeys(TestData.username);
			SignIn.passwordField().sendKeys(TestData.password);
			SignIn.signInButton().click();
			isFiberTestModuleVisible();
		} catch (Exception e) {
			System.out.println("****Exception in loginToApplication()****");
			System.exit(0);
		}
	}
	
	public static boolean isFiberTestModuleVisible()
	{
		return Dashboard.FiberTestModule().isDisplayed();
	}

	public static void verifyBuildVersion() throws Exception {
		try {
			Dashboard.openNavigationOption().click();
			SideMenu.aboutOption().click();
			String appVersion = About.versionNumber().getText();
			if (!appVersion.contains(TestData.expectedAppVersion)) {
				System.out.println("****EXPECTED APP VERSION : " + TestData.expectedAppVersion + " BUT FOUND : " + appVersion);
			}
		} catch (Exception e) {
			System.out.println("****Exception in verifyBuildVersion()****");
		}
	}
}