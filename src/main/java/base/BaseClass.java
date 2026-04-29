package base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.Socket;
import java.net.URI;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
			waitForWinAppDriver();
		} catch (Exception e) {
			System.out.println("****Exception in launchWinAppDriver()****");
			System.exit(0);
		}
	}

	public static boolean isPortOpen(String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitForWinAppDriver() throws InterruptedException {
		int waited = 0;

		while (waited < 10) {
			if (isPortOpen("127.0.0.1", 4723)) {
				return;
			}
			Thread.sleep(500);
			waited++;
		}
		throw new RuntimeException("WinAppDriver not started within 5 seconds time out");
	}

	public static void launchApplication() throws Exception {
		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", TestData.appId());
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("automationName", "Windows");
			Thread.sleep(3000);
			driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
			actions = new Actions(driver);
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println("****Exception in launchApplication()****");
			System.exit(0);
		}
	}
	
	public static void verifyIncorrectCredentials()
	{
		
	}

	public static void loginToApplication() {
		try {
			SignIn.usernameField().sendKeys(TestData.username);
			SignIn.passwordField().sendKeys(TestData.password);
			SignIn.signInButton().click();
			Assert.assertTrue(Dashboard.isFiberTestModuleDisplayed(),
					"Tried for 40 secs, Fiber Test module was not visible after login");
		} catch (Exception e) {
			System.out.println("****Exception in loginToApplication()****");
			System.exit(0);
		}
	}

	public static boolean isElementDisplayed(By locator, int timeoutMilliSeconds) {
		try {
			wait = new WebDriverWait(driver, timeoutMilliSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void verifyBuildVersion() throws Exception {
		try {
			Dashboard.openNavigationButton().click();
			SideMenu.aboutButton().click();
			String appVersion = About.versionNumber().getText();
			Assert.assertTrue(appVersion.contains(TestData.expectedAppVersion),
					"Expected build version was " + TestData.expectedAppVersion + " but found " + appVersion);
		} catch (Exception e) {
			System.out.println("****Exception in verifyBuildVersion()****");
		}
	}
	
	public static void deleteAllExistingConnectionProfiles()
	{
		
	}
	
	public static void createConnectionProfile() {
		try {
			Side_Menu.settingsButton();
			Thread.sleep(2000);
			try {
				Connection_Profiles_Page.connection_Profiles_Dropdown().click();
			} catch (Exception e) {
				actions.moveToElement(Connection_Profiles_Page.no_Connection_Profiles_Text()).build().perform();
			}
			int profilesCount = Connection_Profiles_Page.list_Of_Profiles().size();
//		System.out.println("Total Existing Profiles - " + profilesCount);
			for (int i = 0; i < profilesCount; i++) {
				// System.out.println("Profile " + (i+1) + " --> " +
				// Connection_Profiles_Page.list_Of_Profiles().get(i).getText());
				if (Connection_Profiles_Page.list_Of_Profiles().get(i).getText()
						.equalsIgnoreCase(Test_Data.connection_Profile_Name)) {
					System.out.println("\n****" + Test_Data.connection_Profile_Name
							+ " profile already EXISTS, hence not creating it again ****");
//					Connection_Profiles_Page.connection_Profiles_Dropdown().click();
					Connection_Profiles_Page.selectNeededProfile().click();
					return;
				}
				if (i == (profilesCount - 1)) {
					Connection_Profiles_Page.connection_Profiles_Dropdown().click();
				}
			}
			Connection_Profiles_Page.create_New_Connection_Profile_Button().click();
			Connection_Profiles_Page.connection_Profile_Name().click();
			Connection_Profiles_Page.connection_Profile_Name().sendKeys(Test_Data.connection_Profile_Name);
			Connection_Profiles_Page.connection_Profile_Instrument_Type()
					.sendKeys(Test_Data.connection_Profile_Instrument_Type);
			Thread.sleep(1000);
			Connection_Profiles_Page.connection_Profile_IpAddress().click();
			Connection_Profiles_Page.connection_Profile_IpAddress().clear();
			Connection_Profiles_Page.connection_Profile_IpAddress().sendKeys(Test_Data.connection_Profile_IpAddress);
			Connection_Profiles_Page.connection_Profile_Port().click();
			Connection_Profiles_Page.connection_Profile_Port().clear();
			Connection_Profiles_Page.connection_Profile_Port().sendKeys(Test_Data.connection_Profile_Port);			
			actions.sendKeys(Keys.PAGE_DOWN).build().perform();
			Thread.sleep(500);
			actions.moveToElement(Connection_Profiles_Page.save_Button()).click().build().perform();
		} catch (Exception e) {
			System.out.println("****Exception in create_Connection_Profile()****");
			e.printStackTrace();
		}
	}
}