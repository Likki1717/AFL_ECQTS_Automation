package base;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.Socket;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import pageObjects.CommonPages.Dashboard;
import pageObjects.CommonPages.SideMenu;
import pageObjects.CommonPages.SignIn;
import pageObjects.Modules.CopyResults;
import pageObjects.Modules.DownTime;
import pageObjects.Modules.QE_Labs;
import pageObjects.Modules.ImportData.Import;
import pageObjects.Modules.TestJobModule.JobDetailsPage;
import pageObjects.Modules.TestJobModule.JobSearch;
import pageObjects.Modules.TestJobModule.JobDetails.Completion;
import pageObjects.Modules.TestJobModule.JobDetails.FiberResults;
import pageObjects.Modules.TestJobModule.JobDetails.OTDR_Settings;
import pageObjects.Modules.TestJobModule.JobDetails.Optics;
import pageObjects.Modules.TestJobModule.JobDetails.ProtectionLayer;
import pageObjects.Modules.TestJobModule.JobDetails.Reports;
import pageObjects.Modules.TestJobModule.JobDetails.WTC;
import pageObjects.sideMenu.About;
import pageObjects.sideMenu.Settings;
import pageObjects.sideMenu.settings.ApplicationSettings;
import pageObjects.sideMenu.settings.ConnectionProfiles;
import pageObjects.sideMenu.settings.TestSettings;

@Listeners(TestFailureListener.class)
public class BaseClass {
	public static WindowsDriver<?> driver;
	public static WindowsDriver<WindowsElement> desktopSession;
	public static Actions actions;
	public static WebDriverWait wait;
	public static Robot robot;
	public static DesiredCapabilities capabilities;
	public static SoftAssert softAssert = new SoftAssert();
	public static boolean isAppLaunched = false;
	public static boolean shouldRemoveSalesOrder = false;
	public static boolean isGetLengthAndEditAdjLengthDoneForWtcJob = false;
	public static boolean isExecutiveProfileLoggedIn = false;

	@BeforeClass
	public static void applicationSetupAndLaunch() throws Exception {

		clearPreviousSessionData();

		launchWinAppDriver();

		launchOpenVpnAppAndConnect();

		launch_ECQTS_Application();

		verifyIncorrectCredentials();

		validateRecoverPasswordButtonAvailability();

		loginToApplication();

		updateTestSettings();

		updateApplicationSettings();

	}

	public static void clearPreviousSessionData() throws Exception {

		if (!TestData.isAppLogInRequired) {
			softAssert.fail("As app is in logged in state, Previous session data has not been cleared");
			return;
		}

		File systemUsernameFolder = new File("C:\\Users\\" + TestData.systemUsername + "");

		// Stop execution if folder does not exists
		if (!systemUsernameFolder.exists()) {
			Assert.fail(
					"\n****Update systemUsername value in Test Data, present value is - " + TestData.systemUsername);
		}

		int deleteAttempts = 0;

		File secureStorageFolder = new File(TestData.secureStorageFolderPath);

		// Keep trying to delete the secureStorageFolder folder if it exists, this will
		// make sure the app is in Logged Out state
		while (secureStorageFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(secureStorageFolder);
			} else {
				// If deletion failed, likely due to files being in use
				Assert.fail("****Close the existing open App and run the script again****");
			}
		}

		deleteAttempts = 0;

		File powerSyncFolder = new File(TestData.powerSyncFolderPath);

		// Keep trying to delete the secureStorageFolder folder if it exists, this will
		// make sure the app is in Logged Out state
		while (powerSyncFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(powerSyncFolder);
			} else {
				// If deletion failed, likely due to files being in use
				Assert.fail("****Close the existing open App and run the script again****");
			}
		}

		deleteAttempts = 0;
		File OcrReportFolder = new File(TestData.OCR_Report_Path);

		// Delete the OCR Report folder if it exists, to verify if the newly downloaded
		// Ocr file is available in this folder
		while (OcrReportFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(OcrReportFolder);
			} else {
				// If deletion failed, likely due to files being in use
				Assert.fail("****Close the existing open OCR file and run the script again****");
			}
		}

		deleteAttempts = 0;
		File SorReportFolder = new File(TestData.SOR_Files_Path);

		// Delete the SOR files folder if it exists, to verify if the newly downloaded
		// sor file is available in this folder
		while (SorReportFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(SorReportFolder);
			} else {
				// If deletion failed, likely due to files being in use
				Assert.fail("*****Close any open app / file and run the script again*****");
			}
		}

		// Create SOR files folder it if missing, to verify if the nelwy downloaded Sor
		// file is available in this folder
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
		launchDependentApplication(TestData.winAppDriverPath);
		waitForWinAppDriver();
		robot = new Robot();
	}

	public static void launchOpenVpnAppAndConnect() throws Exception {
		if (TestData.useOfficeOtdr) {
			launchDependentApplication(TestData.openVpnAppPath);
			Thread.sleep(5000);
			for (int i = 0; i < 4; i++) {
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
			}
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(3000);
		}
	}

	public static void launchDependentApplication(String applicationPath) throws Exception {
		try {
//			robot.keyPress(KeyEvent.VK_WINDOWS);
//			robot.keyPress(KeyEvent.VK_R);
//			robot.keyRelease(KeyEvent.VK_R);
//			robot.keyRelease(KeyEvent.VK_WINDOWS);
//			robot.delay(300);
//			copyPasteAndClickEnter(applicationPath);
			new ProcessBuilder("powershell", "-Command", "Start-Process -FilePath '" + applicationPath + "'").start();
		} catch (Exception e) {
			Assert.fail("****Exception in launchDependentApplication()****");
		}
	}

	public static void copyPasteAndClickEnter(String textToCopy) throws Exception {

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);

		robot.keyRelease(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.delay(200);

		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);

		robot.delay(200);
//		 Copy path to clipboard
		StringSelection selection = new StringSelection(textToCopy);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

		// Paste using CTRL + V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);

		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.delay(500);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public static boolean isPortOpen(String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitForWinAppDriver() throws Exception {
		int waited = 0;

		while (waited < 30) {
			if (isPortOpen("127.0.0.1", 4723)) {
				return;
			}
			Thread.sleep(500);
			waited++;
		}
		throw new RuntimeException("WinAppDriver not started within 15 seconds time out");
	}

	public static void launch_ECQTS_Application() throws Exception {
		try {
			new ProcessBuilder("taskkill", "/F", "/PID", TestData.appId()).start();
			Thread.sleep(3000);
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", TestData.appId());
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("automationName", "Windows");
			driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
		} catch (Exception e) {
			System.out.println("****Normal launch of ECQTS application failed, Trying Root attach****");
			attachExistingApplication();
		}
		Dashboard.waitUntilLoaderIsNotDisplayed();
		dismissSyncStatusPopupIfDisplayed();
		if (!TestData.isAppLogInRequired) {
			Dashboard.waitUntilLoaderIsNotDisplayed();
			softAssert.assertTrue(Dashboard.isOpenNavigationButtonDisplayed(),
					"Tried for 3 secs, side navigation option was not visible after app launch");
		}
		actions = new Actions(driver);
		isAppLaunched = true;
	}

	public static void attachExistingApplication() throws Exception {
		boolean applicationAttached = false;
		while (!applicationAttached) {
			try {
				DesiredCapabilities capabilities2 = new DesiredCapabilities();
				capabilities2.setCapability("app", "Root");
				capabilities2.setCapability("platformName", "Windows");
				capabilities2.setCapability("deviceName", "WindowsPC");
				desktopSession = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities2);
				String windowHandle = desktopSession.findElementByClassName("WinUIDesktopWin32WindowClass")
						.getAttribute("NativeWindowHandle");
				int handle = Integer.parseInt(windowHandle);
				String hexHandle = "0x" + Integer.toHexString(handle);
				DesiredCapabilities capabilities3 = new DesiredCapabilities();
				capabilities3.setCapability("appTopLevelWindow", hexHandle);
				driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities3);
				applicationAttached = true;
				dismissSyncStatusPopupIfDisplayed();
			} catch (Exception e) {
				System.out.println("****Root attach failed, trying again****");
				Thread.sleep(2000);
			} finally {
				if (desktopSession != null) {
					desktopSession.quit();
				}
			}
		}
	}

	public static void verifyIncorrectCredentials() throws Exception {
		if (!TestData.isAppLogInRequired) {
			softAssert.fail("As app is in logged in state, cannot verify incorrect credentials scenario");
			return;
		}
		softAssert.assertTrue(SignIn.isUsernameFieldDisplayed(),
				"Username field is not displayed within 10 seconds of app launch");
		SignIn.usernameField().clear();
		SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
		SignIn.passwordField().sendKeys("Invalid password");
		SignIn.signInButton().click();
		softAssert.assertTrue(SignIn.isloginFailureDisplayed(),
				"Tried for 5 secs, Login failed message was not visible");
		SignIn.okButtonOnLoginFailurePopup().click();
	}

	public static void validateRecoverPasswordButtonAvailability() throws Exception {
		if (!TestData.isAppLogInRequired) {
			softAssert.fail("As app is in logged in state, cannot verify recover password button availability");
			return;
		}
		softAssert.assertTrue(SignIn.isRecoverPasswordButtonDisplayed(),
				"Recover Password button is not displayed on the app login page");
	}

	public static void loginToApplication() throws Exception {
		try {
			if (!TestData.isAppLogInRequired) {
				softAssert.fail("As app is in logged in state, cannot verify login scenario");
				return;
			}
			softAssert.assertTrue(SignIn.isUsernameFieldDisplayed(),
					"Username field is not displayed within 10 seconds of app launch");
			SignIn.usernameField().clear();
			SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
			SignIn.passwordField().clear();
			SignIn.passwordField().sendKeys(TestData.ecqtsAppPassword());
			SignIn.signInButton().click();
			Dashboard.waitUntilLoaderIsNotDisplayed();
			softAssert.assertTrue(Dashboard.isFiberTestModuleDisplayed(),
					"Tried for 10 secs, Fiber Test module was not visible after login");
			TestData.isAppLogInRequired = false;
		} catch (Exception e) {
			Assert.fail("****Exception in loginToApplication()****");
		}
	}

	public static boolean isElementDisplayed(By locator, int timeOutInSeconds) {
		try {
			wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isElementNotDisplayed(By locator, int timeoutSeconds) {
		try {
			wait = new WebDriverWait(driver, timeoutSeconds);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void verifyAboutPage() throws Exception {
		try {

			Dashboard.openNavigationButton().click();
			SideMenu.aboutButton().click();
			String appVersion = About.versionNumber().getText();
			Assert.assertEquals(appVersion, TestData.expectedAppVersion(), "Build version mismatch.");
			String expectedEnvironment = (TestData.testEnvironment.equals("Dev")
					|| TestData.testEnvironment.equals("QA")) ? "-" + TestData.testEnvironment.toLowerCase() : "";
			String expectedPortalLink = "https://www.ecqts" + expectedEnvironment + ".aflglobal.com";
			softAssert.assertTrue(About.portalLink().getText().contains(expectedPortalLink),
					"Environment specific portal link is not displayed in About page");
			About.additionalInformationButton().click();
			softAssert.assertTrue(
					About.appLink().getText().contains(TestData.testEnvironment.replaceAll("\\s+", "").toLowerCase()),
					"Environment specific app link is not displayed in About page");
			About.checkForUpdatesButton().click();
			softAssert.assertTrue(About.isLatestVersionMessageDisplayed(),
					"Tried for 5 seconds, you're on latest version text was not visible");
		} catch (Exception e) {
			System.out.println("****Exception in verifyBuildVersion()****");
		}
	}

	public static void verify_Delete_Create_And_Edit_Connection_Profiles() {

		deleteAllExistingConnectionProfiles();

		createConnectionProfiles();

		editConnectionProfile();
	}

	public static void deleteAllExistingConnectionProfiles() {
		try {
			dismissSyncStatusPopupIfDisplayed();
			Dashboard.openNavigationButton().click();
			SideMenu.settingsButton().click();
			Settings.connectionProfilesButton().click();
			dismissSyncStatusPopupIfDisplayed();
			while (ConnectionProfiles.isDeleteProfileButtonDisplayed()) {
				ConnectionProfiles.deleteProfileButton().click();
				ConnectionProfiles.yesButtonOnDeleteProfilePopup().click();
			}
			softAssert.assertTrue(ConnectionProfiles.isNoProfilesFoundTextDisplayed(),
					"Tried for 5 seconds, No existing profiles found text was not visible");
		} catch (Exception e) {
			System.out.println("****Exception in deleteAllExistingConnectionProfiles()****");
		}
	}

	public static void createProfile(String connectionProfileName, String ipAddress, String port) {
		try {
			dismissSyncStatusPopupIfDisplayed();
			ConnectionProfiles.createNewProfileButton().click();
			ConnectionProfiles.nameTextBox().sendKeys(connectionProfileName);
			ConnectionProfiles.instrumentTypeDropdown().click();
			if ((TestData.testEnvironment.equals("Dev") || TestData.testEnvironment.equals("QA"))
					&& !connectionProfileName.equals("Office OTDR")) {
				ConnectionProfiles.instrumentType_Simulator().click();
			} else {
				ConnectionProfiles.instrumentType_Anritsu_MT_9085().click();
			}
			ConnectionProfiles.ipAddressTextBox().clear();
			ConnectionProfiles.ipAddressTextBox().sendKeys(ipAddress);
			ConnectionProfiles.portTextBox().clear();
			ConnectionProfiles.portTextBox().sendKeys(port);
			if (connectionProfileName.contains("JGR")) {
				ConnectionProfiles.switchTypeDropdown().click();
				ConnectionProfiles.switchType_JGR_Switch().click();
				ConnectionProfiles.switchModuleTextBox().clear();
				ConnectionProfiles.switchModuleTextBox().sendKeys("1");
				if (connectionProfileName.contains(TestData.connectionProfileName_JGR_One)) {
					ConnectionProfiles.findAddressTextBox().clear();
					ConnectionProfiles.findAddressTextBox().sendKeys("SIMULATO");
					ConnectionProfiles.testSwitchConnectionButton().click();
					softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
							"Connection failure popup is not displayed for " + connectionProfileName);
					ConnectionProfiles.okButton().click();
					ConnectionProfiles.findAddressTextBox().clear();
					ConnectionProfiles.findAddressTextBox().sendKeys(" SIMULATOR ");
					ConnectionProfiles.testSwitchConnectionButton().click();
					softAssert.assertTrue(ConnectionProfiles.isConnectionSuccessfulPopupDisplayed(),
							"Connection successful popup is not displayed for " + connectionProfileName);
					ConnectionProfiles.okButton().click();
				} else {
					ConnectionProfiles.findAddressTextBox().clear();
					ConnectionProfiles.findAddressTextBox().sendKeys("SIMULATOR");
				}
				ConnectionProfiles.workerNumberTextBox().sendKeys(connectionProfileName.split("-")[1]);
				ConnectionProfiles.spoonTextBox().sendKeys(connectionProfileName.split("-")[1]);
			}
			ConnectionProfiles.saveProfileButton().click();
			if ((connectionProfileName.equals(TestData.connectionProfileName_Office_OTDR) && TestData.useOfficeOtdr)
					|| !connectionProfileName.equals(TestData.connectionProfileName_Office_OTDR)) {
				validateSuccessfulConnectionPopup(connectionProfileName);
			} else {
				validateFailureConnectionPopup(connectionProfileName);
			}
		} catch (Exception e) {
			System.out.println("****Exception in createProfile() - " + connectionProfileName + "****");
		}
	}

	public static void createConnectionProfiles() {
		createProfile(TestData.connectionProfileName_JGR_One, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);
		createProfile(TestData.connectionProfileName_JGR_Two, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);
		createProfile(TestData.connectionProfileName_Office_OTDR, TestData.connectionProfile_Office_OTDR_IP_Address,
				TestData.connectionProfile_Office_OTDR_Port);
		createProfile(TestData.connectionProfileName_Simulator, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);
	}

	public static void validateSuccessfulConnectionPopup(String connectionProfileName) {
		try {
			wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.elementToBeClickable(ConnectionProfiles.testConnection()));
			ConnectionProfiles.testConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionSuccessfulPopupDisplayed(),
					"Connection successful popup is not displayed for " + connectionProfileName);
			ConnectionProfiles.okButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in validateSuccessfulConnectionPopup()****");
		}
	}

	public static void validateFailureConnectionPopup(String connectionProfileName) {
		try {
			wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.elementToBeClickable(ConnectionProfiles.testConnection()));
			ConnectionProfiles.testConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
					"Connection failed popup is not displayed for " + connectionProfileName);
			ConnectionProfiles.okButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in validateFailureConnectionPopup()****");
		}
	}

	public static void editConnectionProfile() {
		try {
			ConnectionProfiles.editButton().click();
			ConnectionProfiles.instrumentTypeDropdown().click();
			if (TestData.testEnvironment.equals("Dev") || TestData.testEnvironment.equals("QA")) {
				ConnectionProfiles.instrumentType_Anritsu_MT_9085().click();
			} else {
				ConnectionProfiles.instrumentType_Anritsu_MT_9083().click();
			}
			softAssert.assertTrue(
					ConnectionProfiles.ipAddressTextBox().getAttribute("Value.Value")
							.contains(TestData.connectionProfile_Anritsu_9085_IP_Address),
					"Ip Address did not change when connection profile instrument type is changed");
			ConnectionProfiles.ipAddressTextBox().clear();
			ConnectionProfiles.ipAddressTextBox().sendKeys(TestData.connectionProfile_Anritsu_9085_IP_Address);
			robot.mouseWheel(3);
			ConnectionProfiles.saveProfileButton().click();
			ConnectionProfiles.testConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
					"Connection failed popup is not displayed for Simulator profile during Edit and changing instrument type");
			ConnectionProfiles.okButton().click();
			ConnectionProfiles.editButton().click();
			softAssert.assertTrue(ConnectionProfiles.ipAddressTextBox().getAttribute("Value.Value")
					.contains(TestData.connectionProfile_Anritsu_9085_IP_Address));
			ConnectionProfiles.instrumentTypeDropdown().click();
			if (TestData.testEnvironment.equals("Dev") || TestData.testEnvironment.equals("QA")) {
				ConnectionProfiles.instrumentType_Simulator().click();
			} else {
				ConnectionProfiles.instrumentType_Anritsu_MT_9085().click();
			}
			ConnectionProfiles.ipAddressTextBox().clear();
			ConnectionProfiles.ipAddressTextBox().sendKeys(TestData.connectionProfile_Simulator_IP_Address);
			robot.mouseWheel(3);
			ConnectionProfiles.saveProfileButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in editConnectionProfile()****");
		}
	}

	public static void updateTestSettings() {
		try {
			if (!TestSettings.isDisplayRealTimePlotToogleDisplayed()) {
				if (Dashboard.isOpenNavigationButtonDisplayed()) {
					Dashboard.openNavigationButton().click();
					Thread.sleep(1000);
				}
				SideMenu.settingsButton().click();
				Settings.isTestSettingsButtonDisplayed();
				Settings.testSettingsButton().click();
			}

			if (TestData.useOfficeOtdr != TestSettings.displayRealTimetoggle().isSelected()) {
				TestSettings.displayRealTimetoggle().click();
			}
			if (TestSettings.enableOfflineReportsToggle().isSelected()) {
				TestSettings.enableOfflineReportsToggle().click();
			}
		} catch (Exception e) {
			System.out.println("****Exception in updateTestSettings()****");
		}
	}

	public static void updateApplicationSettings() {
		if (!TestData.useExternalCamera) {
			return;
		}
		try {
			if (!Settings.isTestSettingsButtonDisplayed()) {
				Dashboard.openNavigationButton().click();
				SideMenu.settingsButton().click();
			}
			Settings.isTestSettingsButtonDisplayed();
			Settings.applicationSettingsButton().click();
			softAssert.assertTrue(ApplicationSettings.isCameraSourceDropDownDisplayed(),
					"Waited for 3 secs, Camera source drop down is not displayed ");
			if (TestData.useExternalCamera == ApplicationSettings.cameraSourceDropDown().getText()
					.contains("Integrated")) {
				ApplicationSettings.cameraSourceDropDown().click();
				if (ApplicationSettings.isExternalCameraDisplayed()) {
					ApplicationSettings.externalCamera().click();
				} else {
					System.out.println("****External camera is not available to select****");
				}
//				if (TestData.useExternalCamera) {
//					if (ApplicationSettings.isExternalCameraDisplayed()) {
//						ApplicationSettings.externalCamera().click();
//					} else {
//						System.out.println("****External camera is not available to select****");
//					}
//				} 
//				else {
//					try {
//						ApplicationSettings.integratedCamera().click();
//					} catch (Exception e) {
//						System.out.println("****Integrated camera is not available to select****");
//					}
//				}
			}
		} catch (Exception e) {
			System.out.println("****Exception in updateApplicationSettings()****");
		}
	}

	public static void importJob(String importType, String importFilesToUpload, String OTDR_Length, String helixFactor,
			String JobNumberStartsWith, String itemNumber) throws Exception {
		navigateToModule(TestData.importDataModuleName);

		softAssert.assertTrue(Import.isSelectImportTextDisplayed(),
				"Waited for 5 seconds, Select an import type from the left panel is not displayed");

		Thread.sleep(1000);

		if (importType.equalsIgnoreCase(TestData.prysmianImportModuleName)) {
			Import.prysmianType().click();

		} else if (importType.equalsIgnoreCase(TestData.swindonImportModuleName)) {
			Import.swindonType().click();

		} else if (importType.equalsIgnoreCase(TestData.taihanImportModuleName)) {
			Import.taihanType().click();
		}

		Dashboard.isLoaderDisplayed();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Thread.sleep(500);
		Import.orgDropDownField().click();
		Import.orgDropDownField().sendKeys(TestData.importOrg);
		actions.sendKeys(Keys.TAB).build().perform();
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_TAB);
		Import.cutNumber().sendKeys(TestData.importCutNumber);
		actions.sendKeys(Keys.TAB).build().perform();
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		Import.itemOrgCode().sendKeys(TestData.importItemOrgCode);
		actions.sendKeys(Keys.TAB).build().perform();
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		Import.cutNumberInfo().sendKeys(TestData.importcutNumberInfo);
		Thread.sleep(1000);
		actions.sendKeys(Keys.TAB).build().perform();
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		Import.importType().sendKeys(TestData.importType);
		actions.sendKeys(Keys.TAB).build().perform();
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_TAB);
		Import.uploadFileButton().click();
		Dashboard.waitUntilFileNameTextBoxIsDisplayed();
		Import.fileNameTextBox().click();
		Import.fileNameTextBox().sendKeys(importFilesToUpload);
		Thread.sleep(500);
		actions.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(2000);
//		robot.keyPress(KeyEvent.VK_ENTER);
//		robot.keyRelease(KeyEvent.VK_ENTER);

		while (!Import.cutNumberInfo().getText().equals(TestData.importcutNumberInfo)) {
			Import.cutNumberInfo().sendKeys(TestData.importcutNumberInfo);
			Thread.sleep(1000);
			actions.sendKeys(Keys.TAB).build().perform();
			Thread.sleep(1000);
		}

		Import.submitButton().click();
		Dashboard.isLoaderDisplayed();

		boolean eitherImportCompletedOrGotError = false;
		boolean isManualJobSearchNeeded = false;

		while (!eitherImportCompletedOrGotError) {

			if (Dashboard.isLoaderNotDisplayed() || Import.isImportSuccessfulPopupDisplayed()
					|| Import.isWarningsErrorsPopupDisplayedOtherThanMissingFiberId()) {
				eitherImportCompletedOrGotError = true;
				if (Import.isImportSuccessfulPopupDisplayed()) {
					isManualJobSearchNeeded = true;
					String importJobNumber = Import.getJobNumberFromImportSuccessFulPopup();
					System.out.println("* " + importType
							+ " Import Complete popup displayed instead of navigating to job details page.");
					Import.okButton().click();
					String module = "";
					if (TestData.importType.equals("Fiber")) {
						module = TestData.fiberTestModuleName;
					} else {
						module = TestData.wtcTestModuleName;
					}
					searchJobAndNavigationToJobDetailsPage(module, TestData.importOrg, importJobNumber,
							TestData.importCutNumber, TestData.importcutNumberInfo);
				} else if (Import.isWarningsErrorsPopupDisplayedOtherThanMissingFiberId()) {
					softAssert.fail(importType + " Import Failed, found warning/errors popup - "
							+ Dashboard.getMessageDisplayedOnPopup());
					Dashboard.okButton().click();
					return;
				} else if (Import.isPrysmianTypeDisplayed()) {
					String errorMessage = "";
					try {
						errorMessage = Import.getErrorDisplayedAboveSubmitButton();
					} catch (Exception e) {
						errorMessage = "Could not fetch the error message, script needs to be modified";
					}
					softAssert
							.fail(importType + " Import Failed, found error message on import page - " + errorMessage);
					return;
				}
			}
		}

		if (!isManualJobSearchNeeded && JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
			Dashboard.okButton().click();
		}

		// Waiting for buffer tube to display so that we can confirm data is loaded

		softAssert.assertTrue(JobDetailsPage.isBufferTubeDisplayed(),
				importType + " Import completed - Waited for 3 seconds, buffer tube is not displayed");

		System.out.println(importType + " Import Job Number : " + JobDetailsPage.jobNumber().getText().trim());

		// Validating OTDR length is not 0 and its as expected

		softAssert.assertEquals(JobDetailsPage.OTDR_Length().getText(), OTDR_Length,
				importType + " Import completed - OTDR Length mismatch.");

		// Validating Helix Factor
		softAssert.assertEquals(JobDetailsPage.helixFactor().getText().trim(), helixFactor,
				importType + " Import completed - Helix Factor mismatch.");

		verifyJobDetailsHeader(TestData.importOrg, JobNumberStartsWith, TestData.importCutNumber,
				TestData.importcutNumberInfo, "After " + importType + " Import", itemNumber);
	}

	public static void verify_All_Three_Imports() {
		importPrysmianJob();

		importTaihanJob();

		importSwindonJob();
	}

	public static void importPrysmianJob() {
		try {
			// Multi-file string
			String prysmianFilePath = "\"" + TestData.prysmianAttenuationFilePath + "\" \""
					+ TestData.prysmianJacketOdFilePath + "\"";

			importJob(TestData.prysmianImportModuleName, prysmianFilePath, TestData.prysmianExpectedOtdrLength,
					TestData.prysmianExpectedHelixFactor, TestData.prysmianJobNumberStartsWith,
					TestData.prysmianJobExpectedItemNumber);

			// Validating test results count
			verifyTestResultsCount(TestData.prysmianExpectedIncompleteTests, TestData.prysmianExpectedPassedTests,
					TestData.prysmianExpectedFailedTests, "Prysmian Import");
		} catch (Exception e) {
			System.out.println("****Exception in importPrysmianJob()****");
		}
	}

	public static void importSwindonJob() {
		try {
			String swindonFilePath = "\"" + TestData.swindonAttenuationFilePath + "\" \""
					+ TestData.swindonJacketOdFilePath + "\"";

			importJob(TestData.swindonImportModuleName, swindonFilePath, TestData.swindonExpectedOtdrLength,
					TestData.swindonExpectedHelixFactor, TestData.swindonJobNumberStartsWith,
					TestData.swindonJobExpectedItemNumber);

			verifyTestResultsCount(TestData.swindonExpectedIncompleteTests, TestData.swindonExpectedPassedTests,
					TestData.swindonExpectedFailedTests, "Swindon Import");

		} catch (Exception e) {
			System.out.println("****Exception in importSwindonJob()****");
		}
	}

	public static void importTaihanJob() {

		try {
			// Multi-file string
			String taihanFilePath = "\"" + TestData.taihanAttenuationFilePath + "\"";

			importJob(TestData.taihanImportModuleName, taihanFilePath, TestData.taihanExpectedOtdrLength,
					TestData.taihanExpectedHelixFactor, TestData.taihanJobNumberStartsWith,
					TestData.taihanJobExpectedItemNumber);

			verifyTestResultsCount(TestData.taihanExpectedIncompleteTests, TestData.taihanExpectedPassedTests,
					TestData.taihanExpectedFailedTests, "Taihan Import");

		} catch (Exception e) {
			System.out.println("****Exception in importTaihanJob()****");
		}

	}

	public static void navigateToModule(String module) throws Exception {
		while (!Dashboard.isImportDataModuleDisplayed()) {
			if (Dashboard.isOpenNavigationButtonDisplayed()) {
				Dashboard.openNavigationButton().click();
				Thread.sleep(1000);
			}
			if (!SideMenu.isDashboardButtonDisplayed()) {
				Dashboard.openNavigationButton().click();
			}
			SideMenu.dashboardButton().click();
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		switch (module) {
		case TestData.fiberTestModuleName:
			Dashboard.fiberTestModule().click();
			break;

		case TestData.wtcTestModuleName:
			Dashboard.wtcTestModule().click();
			break;

		case TestData.copyResultsModuleName:
			Dashboard.copyResultsModule().click();
			break;

		case TestData.importDataModuleName:
			Dashboard.importDataModule().click();
			break;

		case TestData.downTimeModuleName:
			Dashboard.downTimeModule().click();
			break;

		case TestData.tightBufferModuleName:
			Dashboard.tightBufferModule().click();
			break;

		case TestData.PK_FiberTestModuleName:
			Dashboard.PK_FiberTestModule().click();
			break;

		case TestData.QE_LabsModuleName:
			Dashboard.QE_LabsModule().click();
			break;

		default:
			Assert.fail("** Modify switch case in navigateToModule method to include " + module + " module");
			break;
		}
		Thread.sleep(1000);
	}

	public static void searchJobAndNavigationToJobDetailsPage(String module, String org, String jobNumber,
			String cutNumber, String cutNumberInfo) throws Exception {
		navigateToModule(module);
		softAssert.assertTrue(JobSearch.isJobNumberLabelDisplayed(),
				"Job Number field in not displayed, probably Job search popup did not open");
		JobSearch.orgField().click();
		JobSearch.orgField().sendKeys(org);
		Thread.sleep(500);
		actions.sendKeys(Keys.TAB).build().perform();
		Thread.sleep(500);
		JobSearch.locationField().click();
		JobSearch.locationField().sendKeys(TestData.jobSearchLocation);
		Thread.sleep(500);
		actions.sendKeys(Keys.TAB).build().perform();
		Thread.sleep(500);
		JobSearch.operatorLine_Cell().click();
		JobSearch.operatorLine_Cell().sendKeys(TestData.jobSearchOperator);
		Thread.sleep(500);
		actions.sendKeys(Keys.TAB).build().perform();
		Thread.sleep(500);

		if (TestData.tightBufferModuleName.equals(module)) {
			JobSearch.createNewJobCheckBox().click();
			JobSearch.itemNumberTextBox().sendKeys(TestData.tightBufferExpectedItemNumber);
			while (true) {
				JobSearch.createButton().click();
				Dashboard.waitUntilLoaderIsNotDisplayed();
				if (Dashboard.isOkButtonDisplayed()) {
					Dashboard.okButton().click();
				} else {
					String actualJobNumber = JobSearch.jobNumber().getText();
					TestData.tightBufferJobNumber = actualJobNumber;

					softAssert.assertTrue(actualJobNumber.startsWith(TestData.expectedTightBufferJobNumberStartsWith),
							"Expected Tight Buffer Job Number to start with: "
									+ TestData.expectedTightBufferJobNumberStartsWith + " but found: "
									+ actualJobNumber);
					break;
				}
			}
		}

		do {
			JobSearch.jobNumber().click();
			if (!TestData.tightBufferModuleName.equals(module)) {
				JobSearch.jobNumber().clear();
				JobSearch.jobNumber().sendKeys(jobNumber);
			}
			Thread.sleep(500);
			actions.sendKeys(Keys.ENTER).perform();
		} while (!Dashboard.isLoaderDisplayed());
		Dashboard.waitUntilOkButtonIsDisplayed();
		while (true) {
			if (JobSearch.isJobWarningsPopupDisplayed()) {
				Dashboard.okButton().click();
			} else {
				Dashboard.okButton().click();
				JobSearch.jobNumber().click();
				Thread.sleep(500);
				actions.sendKeys(Keys.ENTER).perform();
				Dashboard.waitUntilOkButtonIsDisplayed();
				continue;
			}
			wait = new WebDriverWait(driver, 150);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(JobSearch.searchCutNumber()));
				break;
			} catch (Exception e) {
				Dashboard.waitUntilOkButtonIsDisplayed();
				Dashboard.okButton().click();
				JobSearch.jobNumber().click();
				Thread.sleep(500);
				actions.sendKeys(Keys.ENTER).perform();
				Dashboard.waitUntilOkButtonIsDisplayed();
			}
		}
		if (jobNumber.equals(TestData.fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification)) {
			Dashboard.waitUntilLoaderIsNotDisplayed();
			JobSearch.salesOrderValue().click();
			if (shouldRemoveSalesOrder) {
				JobSearch.salesOrderClearButton().click();
				JobSearch.salesOrder().click();
				softAssert.assertEquals(JobSearch.getReelId(), "",
						"Reel ID should not be displayed after Sales Order is removed.");
				softAssert.assertEquals(JobSearch.salesOrder().getAttribute("Value.Value"), null,
						"Sales Order should be cleared after Sales Order is removed.");
			} else {
				softAssert.assertEquals(JobSearch.getReelId(), TestData.fiberTestExpectedReelId,
						"Mismatch in Reel Id in Job Search popup.");
				softAssert.assertEquals(JobSearch.salesOrder().getAttribute("Value.Value"),
						TestData.fiberTestExpectedSalesOrderForReelIdAndSalesOrderVerification,
						"Mismatch in Sales Order in Job Search popup.");
			}
		} else {
			JobSearch.salesOrder().click();
		}
		while (!JobSearch.searchCutNumber().equals(driver.switchTo().activeElement())) {
			JobSearch.searchCutNumber().click();
			Thread.sleep(1000);
		}

		if (!module.equals(TestData.tightBufferModuleName)) {
			softAssert.assertTrue(JobSearch.isCutNumberHeaderDisplayed(),
					"Cut Number header in cut number field table is not displayed ");
			softAssert.assertTrue(JobSearch.isUserHeaderDisplayed(),
					"User header in cut number field table is not displayed ");
			softAssert.assertTrue(JobSearch.isDateHeaderDisplayed(),
					"Date Header in cut number field table is not displayed ");
			softAssert.assertTrue(JobSearch.isProcessHeaderDisplayed(),
					"Process Header in cut number field table is not displayed ");
			softAssert.assertTrue(JobSearch.listOfRowsInCutNumberFieldTable().size() > 0,
					"No rows present in Cut number field table");
		}

		JobSearch.searchCutNumber().sendKeys(cutNumber);
		actions.sendKeys(Keys.ENTER).perform();
		JobSearch.searchCutNumberInfo().clear();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		softAssert.assertTrue(JobSearch.isCutNumberInfoHeaderDisplayed(),
				"Cut Number Info header in cut number info field table is not displayed ");
		softAssert.assertTrue(JobSearch.isDateHeaderDisplayed(),
				"Date Header in cut number info field table is not displayed ");
		softAssert.assertTrue(JobSearch.listOfRowsInCutNumberInfoFieldTable().size() > 0,
				"No rows present in Cut Number Info field table");
		JobSearch.searchCutNumberInfo().sendKeys(cutNumberInfo);
		actions.sendKeys(Keys.ENTER).perform();
		Thread.sleep(1000);
		while (!JobSearch.searchCutNumberInfo().getText().contains(cutNumberInfo)) {
			JobSearch.searchCutNumberInfo().click();
			JobSearch.searchCutNumberInfo().clear();
			JobSearch.searchCutNumberInfo().sendKeys(cutNumberInfo);
			actions.sendKeys(Keys.ENTER).perform();
			Thread.sleep(1000);
		}
		JobSearch.goButton().click();
		while (!JobSearch.isGoButtonNotDisplayed()) {
			Dashboard.waitUntilOkButtonIsDisplayed();
			Dashboard.okButton().click();
			if (JobSearch.isGoButtonDisplayed()) {
				JobSearch.goButton().click();
			}
		}
		Thread.sleep(2000);
		dismissSyncStatusPopupIfDisplayed();
		if (JobDetailsPage.isLoadingLargeJobPopupDisplayed()) {
			JobDetailsPage.downloadAllFibersDataButton().click();
			Thread.sleep(1000);
		}
		if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
			Dashboard.okButton().click();
		}
		Dashboard.waitUntilLoaderIsNotDisplayed();
	}

	public static void verifyJobDetailsHeader(String org, String jobNumber, String cutNumber, String cutNumberInfo,
			String whichTestBeingPerformed, String itemNumber) {

		Assert.assertEquals(JobDetailsPage.org().getText().trim(), org, whichTestBeingPerformed + " - Org mismatch.");

		if (jobNumber.contains("-")) {
			Assert.assertEquals(JobDetailsPage.jobNumber().getText().trim(), jobNumber,
					whichTestBeingPerformed + " - Job number mismatch.");
		} else {
			Assert.assertEquals(JobDetailsPage.jobNumber().getText().trim().split("-")[0], jobNumber,
					whichTestBeingPerformed + " - Job number mismatch.");
		}

		Assert.assertEquals(JobDetailsPage.cutNumber().getText().trim(), cutNumber,
				whichTestBeingPerformed + " - Cut number mismatch.");

		Assert.assertEquals(JobDetailsPage.cutNumberInfo().getText().trim(), cutNumberInfo,
				whichTestBeingPerformed + " - Cut number info mismatch.");

		softAssert.assertEquals(JobDetailsPage.itemNumber().getText().trim(), itemNumber,
				whichTestBeingPerformed + " - Item Number mismatch.");
	}

	public static void verifyTestResultsCount(String expectedIncompleteTestsCount, String expectedPassedTestsCount,
			String expectedFailedTestsCount, String whichTestBeingPerformed) throws Exception {

		String expectedTestResultsCounts = "Incomplete: " + expectedIncompleteTestsCount + ", Passed: "
				+ expectedPassedTestsCount + ", Failed: " + expectedFailedTestsCount;

		Dashboard.waitUntilLoaderIsNotDisplayed();

		String actualTestResultsCount = JobDetailsPage.getActualTestResultsCounts();

		softAssert.assertEquals(actualTestResultsCount, expectedTestResultsCounts,
				"Test results count mismatch in " + whichTestBeingPerformed);
	}

	public static void enterProtectionLayerValues(String module) {
		try {
			JobDetailsPage.protectionLayer().click();
			Thread.sleep(1000);
			if (TestData.tightBufferModuleName.equals(module)) {
				ProtectionLayer.TB_Jacket_Adhesion().sendKeys("2");
				return;
			}
			ProtectionLayer.j1NomialODVertical().sendKeys("1000");
			ProtectionLayer.j1NomialODHorizontal().sendKeys("1500");
			ProtectionLayer.j1_1stRipcord().click();
			ProtectionLayer.j1_1stRipcord().sendKeys("RIP00106");
			ProtectionLayer.j1MinSpotWall().sendKeys("1600");
			ProtectionLayer.j190DegWall().sendKeys("1000");
			ProtectionLayer.j1180DegWall().sendKeys("1000");
			ProtectionLayer.editJ1270DegWall().sendKeys("2000");
			ProtectionLayer.core1Lay().sendKeys("40");
			ProtectionLayer.FRP_Nomial_OD().sendKeys("1600");
		} catch (Exception e) {
			System.out.println(
					"****Could not enter all the values in Protection Layer, possibly this job does not have all fields****");
		}
	}

	public static void runGetLengthTest(String module) throws Exception {
		dismissSyncStatusPopupIfDisplayed();
		JobDetailsPage.waitUntilOtdrSettingsTabIsDisplayed();
		wait = new WebDriverWait(driver, 20);
		JobDetailsPage.OTDR_Settings().click();
		dismissSyncStatusPopupIfDisplayed();
		OTDR_Settings.waitUntilConnectionProfileDropDownIsDisplayed();
		OTDR_Settings.connectionProfile().sendKeys(TestData.OTDR_Settings_ConnectionProfile_Name(module));
		OTDR_Settings.launchLength().clear();
		OTDR_Settings.launchLength().sendKeys(TestData.OTDR_Settings_LaunchLength(module));
		OTDR_Settings.cutLength().clear();
		OTDR_Settings.cutLength().sendKeys(TestData.OTDR_Settings_CutLength(module));
		OTDR_Settings.horizontal().clear();
		OTDR_Settings.horizontal().sendKeys(TestData.OTDR_Settings_Horizontal);
		OTDR_Settings.vertical().clear();
		OTDR_Settings.vertical().sendKeys(TestData.OTDR_Settings_Vertical);
		if (module.equals(TestData.wtcTestModuleName)) {
			OTDR_Settings.launchLength2().clear();
			OTDR_Settings.launchLength2().sendKeys(TestData.OTDR_Settings_LaunchLength2);
			OTDR_Settings.manufactureLength().clear();
			OTDR_Settings.manufactureLength().sendKeys(TestData.OTDR_Settings_manufacturedLength);
		}
		dismissSyncStatusPopupIfDisplayed();
		OTDR_Settings.getLengthButton().click();
		dismissSyncStatusPopupIfDisplayed();
		softAssert.assertTrue(OTDR_Settings.isOkButtonDisplayed(),
				"Waited for 50 seconds, Ok button on get length popup is not displayed");
		wait.until(ExpectedConditions.elementToBeClickable(OTDR_Settings.okButton()));
		OTDR_Settings.okButton().click();
		Dashboard.isLoaderDisplayed();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		softAssert.assertTrue(OTDR_Settings.isGetLengthHistoryDropDownFieldDisplayed(),
				"Waited for 50 seconds, Get Length history drop down field is not displayed");
	}

	public static void editAdjLength(String adjLengthValue) throws Exception {
		JobDetailsPage.editAdjLengthIcon().click();
		Thread.sleep(1000);
		JobDetailsPage.adjLengthInputField().click();
		JobDetailsPage.adjLengthInputField().clear();
		JobDetailsPage.adjLengthInputField().sendKeys(adjLengthValue);
		JobDetailsPage.adjLengthSaveIcon().click();
		Thread.sleep(1000);
		softAssert.assertEquals(JobDetailsPage.getAdjLengthValue(), adjLengthValue + " m",
				"Adj length did not get updated.");
	}

	public static void verifyOpticsPage() throws Exception {
		JobDetailsPage.opticsTab().click();
		Optics.waitUntilColorFieldIsDisplayed();
		softAssert.assertEquals(Optics.countOfBufferTubesInOpticsPage(),
				JobDetailsPage.countOfBufferTubesListedInJobDetailsPage(),
				"Mismatch in buffer tubes count between optics page and job details page");
	}

	public static void verify_Down_Time_Tracker_Module() throws Exception {
		navigateToModule(TestData.downTimeModuleName);
		Dashboard.waitUntilLoaderIsNotDisplayed();
		String defaultSelectedStartTime = "";
		String defaultSelectedEndTime = "";
		boolean isEditButtonDisplayed = DownTime.isEditButtonDisplayed();

		if (isEditButtonDisplayed) {
			defaultSelectedStartTime = DownTime.getSavedStartTime();
			defaultSelectedStartTime = defaultSelectedStartTime.substring(defaultSelectedStartTime.length() - 8);
			defaultSelectedEndTime = DownTime.getSavedEndTime();
			defaultSelectedEndTime = defaultSelectedEndTime.substring(defaultSelectedEndTime.length() - 8);
			DownTime.editButton().click();
			Thread.sleep(1000);
		} else {
			defaultSelectedStartTime = DownTime.startTimePicker().getText();
			defaultSelectedEndTime = DownTime.endTimePicker().getText();
		}

		if (!DownTime.startDateSelectionCalendar().getAttribute("Value.Value").replaceAll("\\p{Cf}", "").trim()
				.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))) {
			DownTime.startDateSelectionCalendar().click();
			DownTime.todaysDate().click();
			Thread.sleep(1000);
		}

		DownTime.startTimePicker().click();
		Thread.sleep(2000);
		selectTime(defaultSelectedStartTime, 11, 59, "PM");

		if (!DownTime.endDateSelectionCalendar().getAttribute("Value.Value").replaceAll("\\p{Cf}", "").trim()
				.equals(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))) {
			DownTime.endDateSelectionCalendar().click();
			DownTime.tomorrowsDate().click();
			Thread.sleep(500);
		}

		DownTime.endTimePicker().click();
		Thread.sleep(1000);
		selectTime(defaultSelectedEndTime, 12, 01, "AM");

		DownTime.reasonComboBox().click();
		DownTime.reasonComboBox().sendKeys(TestData.expectedDownTimeReason);
		DownTime.selectReason(TestData.expectedDownTimeReason).click();

		DownTime.commentTextBox().clear();
		DownTime.commentTextBox().sendKeys("Creating record for Testing purpose from veltris side");

		DownTime.saveButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();

		if (DownTime.isWarningMessageDisplayed()) {
			String errorMessageOnPopup = "";
			try {
				errorMessageOnPopup = Dashboard.getMessageDisplayedOnPopup();
			} catch (Exception e) {
			}
			softAssert.fail("Unable to create down time record, got warning pop up - " + errorMessageOnPopup);
			Dashboard.okButton().click();
			return;
		}

		softAssert.assertEquals(DownTime.getTotalDownTime(), TestData.expectedTotalDownTime,
				"Before editing, total down time mismatch.");

		softAssert.assertEquals(DownTime.getSavedStartTime(), TestData.expectedStartDateTime,
				"Before editing, down time Start Date Time mismatch.");

		softAssert.assertEquals(DownTime.getSavedEndTime(), TestData.expectedEndDateTime,
				"Before editing, down time End Date Time mismatch.");

		softAssert.assertEquals(DownTime.getSavedReason(), TestData.expectedDownTimeReason,
				"Before editing, down time Reason mismatch.");

		editDownTime();

	}

	public static void editDownTime() throws Exception {
		DownTime.editButton().click();
		String defaultSelectedEndTime = DownTime.getSavedEndTime();
		defaultSelectedEndTime = defaultSelectedEndTime.substring(defaultSelectedEndTime.length() - 8);
		DownTime.endTimePicker().click();
		Thread.sleep(1000);
		selectTime(defaultSelectedEndTime, 12, 00, "AM");
		DownTime.reasonComboBox().click();
		DownTime.reasonComboBox().sendKeys(TestData.newExpectedDownTimeReason);
		DownTime.selectReason(TestData.newExpectedDownTimeReason).click();
		DownTime.saveButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();

		if (DownTime.isWarningMessageDisplayed()) {
			String errorMessageOnPopup = "";
			try {
				errorMessageOnPopup = Dashboard.getMessageDisplayedOnPopup();
			} catch (Exception e) {
			}
			softAssert.fail("Unable to update down time record, got warning pop up - " + errorMessageOnPopup);
			Dashboard.okButton().click();
			Dashboard.cancelButton().click();
			return;
		}

		softAssert.assertEquals(DownTime.getTotalDownTime(), TestData.newExpectedTotalDownTime,
				"After editing down time, total down time mismatch.");

		softAssert.assertEquals(DownTime.getSavedStartTime(), TestData.expectedStartDateTime,
				"After editing, down time Start Date Time mismatch.");

		softAssert.assertEquals(DownTime.getSavedEndTime(), TestData.newExpectedEndDateTime,
				"After editing, down time End Date Time mismatch.");

		softAssert.assertEquals(DownTime.getSavedReason(), TestData.newExpectedDownTimeReason,
				"After editing, down time Reason mismatch.");

		Dashboard.cancelButton().click();
	}

	public static void selectTime(String defaultSelectedTime, int targetHour, int targetMinute, String targetMeridian)
			throws Exception {

		defaultSelectedTime = defaultSelectedTime.replaceAll("\\p{Cf}", "");
		int defaultSelectedHour = Integer.parseInt(defaultSelectedTime.split(":")[0].trim());
		int defaultSelectedMinute = Integer.parseInt(defaultSelectedTime.split(":")[1].split(" ")[0].trim());

		// Hour
		int differenceBetweenTargetHourAndDefaultSelectedHour = targetHour - defaultSelectedHour;
//		System.out.println("Calculating hours..");
//		System.out.println("Target Hour -> " + targetHour);
//		System.out.println("Default Selected Hour -> " + defaultSelectedHour);
//		System.out.println("Difference in Hrs -> " + differenceBetweenTargetHourAndDefaultSelectedHour);
		int direction = KeyEvent.VK_DOWN;
		int keyboardScrollsCount = differenceBetweenTargetHourAndDefaultSelectedHour;
		if (keyboardScrollsCount > 6) {
			direction = KeyEvent.VK_UP;
			keyboardScrollsCount = 12 - keyboardScrollsCount;
		} else if (keyboardScrollsCount < 0) {
			keyboardScrollsCount = 0 - keyboardScrollsCount;
			direction = KeyEvent.VK_UP;
			if (keyboardScrollsCount > 6) {
				direction = KeyEvent.VK_DOWN;
				keyboardScrollsCount = 12 - keyboardScrollsCount;
			}
		}
//		System.out.println("Keyboard scrolls to do -> " + keyboardScrollsCount);
//		if(direction == Keys.ARROW_DOWN)
//		{
//			System.out.println("Direction to scroll -> Downwards");
//		}
//		else
//		{
//			System.out.println("Direction to scroll -> Upwards");
//		}
		for (int i = 0; i < keyboardScrollsCount; i++) {
//			actions.sendKeys(direction).build().perform();
			robot.keyPress(direction);
			robot.keyRelease(direction);
			Thread.sleep(200);
		}

		Thread.sleep(500);
//		actions.sendKeys(Keys.TAB).build().perform();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);

		// Miniute
		if (defaultSelectedMinute == 0) {
			defaultSelectedMinute = 60;
		}
		int differenceBetweenTargetMinuteAndDefaultSelectedMinute = targetMinute - defaultSelectedMinute;
//		System.out.println("Calculating minutes..");
//		System.out.println("Target Minute -> " + targetMinute);
//		System.out.println("Default Selected Minute -> " + defaultSelectedMinute);
//		System.out.println("Difference in Mins -> " + differenceBetweenTargetMinuteAndDefaultSelectedMinute);
		direction = KeyEvent.VK_DOWN;
		keyboardScrollsCount = differenceBetweenTargetMinuteAndDefaultSelectedMinute;
		if (keyboardScrollsCount > 30) {
			direction = KeyEvent.VK_UP;
			keyboardScrollsCount = 60 - keyboardScrollsCount;
		} else if (keyboardScrollsCount < 0) {
			direction = KeyEvent.VK_UP;
			keyboardScrollsCount = 0 - keyboardScrollsCount;
			if (keyboardScrollsCount > 30) {
				direction = KeyEvent.VK_DOWN;
				keyboardScrollsCount = 60 - keyboardScrollsCount;
			}
		}
//		System.out.println("Keyboard scrolls to do -> " + keyboardScrollsCount);
//		if(direction == Keys.ARROW_DOWN)
//		{
//			System.out.println("Direction to scroll -> Downwards");
//		}
//		else
//		{
//			System.out.println("Direction to scroll -> Upwards");
//		}
		for (int i = 0; i < keyboardScrollsCount; i++) {
//			actions.sendKeys(direction).build().perform();
			robot.keyPress(direction);
			robot.keyRelease(direction);
			Thread.sleep(200);
		}

		Thread.sleep(500);
//		actions.sendKeys(Keys.TAB).build().perform();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);

		// AM PM
		if (targetMeridian.equals("AM")) {
//			actions.sendKeys(Keys.ARROW_UP).build().perform();
			robot.keyPress(KeyEvent.VK_UP);
			robot.keyRelease(KeyEvent.VK_UP);
		} else {
//			actions.sendKeys(Keys.ARROW_DOWN).build().perform();
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
		}
		Thread.sleep(500);

		// Tick Icon
//		actions.sendKeys(Keys.TAB).build().perform();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);

//		actions.sendKeys(Keys.ENTER).build().perform();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(500);
	}

	public static void runFiberTest(String module, int numberOfFibersToTest) throws Exception {
		wait = new WebDriverWait(driver, 30);
		int fibersTested = 0;
		boolean startTestFromFirstBufferTube = true;
		boolean startTestingInNewBufferTube = true;
		while (numberOfFibersToTest != fibersTested) {
// Below if block is needed only to click on the first buffer tube, which is at start of test 
// OR when all tests are completed and still number of fibers to test are less than overall fibers tested
			Dashboard.waitUntilLoaderIsNotDisplayed();
			if (startTestFromFirstBufferTube) {
				JobDetailsPage.isBufferTubeDisplayed();
				wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.firstBufferTubeTab()));
				JobDetailsPage.firstBufferTubeTab().click();
				startTestFromFirstBufferTube = false;
			}
// Below if blocked is needed to click on Show more info button and to click on Run Test button of first fiber,
// which is at start of test OR when moved to new buffer tube			
			if (startTestingInNewBufferTube) {
				Dashboard.waitUntilLoaderIsNotDisplayed();
				FiberResults.isRunTestsButtonDisplayed();
				FiberResults.showMoreInfoButton().click();
				FiberResults.isRunTestsButtonDisplayed();
				wait = new WebDriverWait(driver, 40);
				wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
				FiberResults.runTestsButtonOfFirstFiber().click();
				startTestingInNewBufferTube = false;
			}
			Dashboard.waitUntilOkButtonIsDisplayed();
			wait = new WebDriverWait(driver, 40);
			wait.until(ExpectedConditions.elementToBeClickable(Dashboard.okButton()));
			Thread.sleep(1000);
			Dashboard.okButton().click();
			fibersTested++;
			FiberResults.isGoToFiberButtonVisible();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.goToFiberButton()));
			FiberResults.goToFiberButton().click();
			if (module.equals(TestData.tightBufferModuleName)
					&& (TestData.testEnvironment.equals("Dev") || TestData.testEnvironment.equals("QA"))) {
				FiberResults.waitUntilTestsCompletedTextIsDisplayed();
				return;
			}
			if (!TestData.useOfficeOtdr && module.equals(TestData.fiberTestModuleName)) {
				Dashboard.waitUntilOkButtonIsDisplayed();
			} else {
				FiberResults.waitUntilStopTestsButtonIsDisplayed();
			}
//			if (TestData.useOfficeOtdr) {
//			FiberResults.waitUntilStopTestsButtonIsDisplayed();
//			} else {
//				Dashboard.waitUntilOkButtonIsDisplayed();
//				Dashboard.okButton().click(); // Ok button on Max Attenutation Popup
//				Dashboard.waitUntilOkButtonIsDisplayed();
//			}
//			System.out.println("Number of fiberes to test - " + numberOfFibersToTest);
//			System.out.println("Number of FIber TESTED - " + fibersTested);
			if (numberOfFibersToTest == fibersTested) {
//				System.out.println("Expected number of fibers are TESTED");
//				if (TestData.useOfficeOtdr) {
				FiberResults.stopButton().click();
//				} else {
//					System.out.println("Need to click on CANCEL button to stop further fibers testing");
//					Dashboard.cancelButton().click();
//				}
			} else {
				if (TestData.useOfficeOtdr) {
					FiberResults.continueButton().click();
				} else {
					Dashboard.okButton().click();
				}
				if (FiberResults.isTestsCompletedTextDisplayed()) {
					FiberResults.continueButton().click();
					startTestingInNewBufferTube = true;
					if (!FiberResults.isRunTestsButtonDisplayed()) {
						startTestFromFirstBufferTube = true;
					}
				}
			}
		}
	}

	public static void downloadSorFiles() throws Exception {
		Dashboard.waitUntilLoaderIsNotDisplayed();
		for (int i = 1; i <= 4; i++) {
//			FiberResults.waitUntil_SOR_DownloadIcon_IsDisplayed(i);
			FiberResults.SOR_DownloadIcon(i).click();
			Dashboard.waitUntilFileNameTextBoxIsDisplayed();
			actions.keyDown(Keys.ALT).sendKeys("d").keyUp(Keys.ALT).build().perform();
			Thread.sleep(500);
			copyPasteAndClickEnter(TestData.SOR_Files_Path);
			Thread.sleep(500);

			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_S);

			robot.keyRelease(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_ALT);
			i = i + 2;
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Thread.sleep(1000);
		}
	}

	public static void verify_Copy_Results_Module() throws Exception {

		do {
			navigateToModule(TestData.copyResultsModuleName);
			CopyResults.waitUntilCopyJobPopupIsDisplayed();

			CopyResults.orgDropDown().click();
			CopyResults.orgDropDown().sendKeys(TestData.copyJobOrg);
			actions.sendKeys(Keys.TAB).build().perform();

			CopyResults.sourceJobNumber().click();
			CopyResults.sourceJobNumber().clear();
			CopyResults.sourceJobNumber().sendKeys(TestData.copyJobSourceJobNumber);
			Thread.sleep(1000);
			actions.sendKeys(Keys.TAB).build().perform();
			Dashboard.isLoaderDisplayed();
			Dashboard.waitUntilLoaderIsNotDisplayed();

			CopyResults.sourceCutNumber().click();
			CopyResults.sourceCutNumber().clear();
			CopyResults.sourceCutNumber().sendKeys(TestData.copyJobSourceCutNumber);
			Thread.sleep(500);
			actions.sendKeys(Keys.TAB).build().perform();
			Dashboard.isLoaderDisplayed();
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Thread.sleep(1000);

			CopyResults.sourceCutNumberInfo().sendKeys(TestData.copyJobSourceCutNumberInfo);
			actions.sendKeys(Keys.TAB).build().perform();

			CopyResults.destinationJobNumber().click();
			CopyResults.destinationJobNumber().clear();
			CopyResults.destinationJobNumber().sendKeys(TestData.copyJobDestinationJobNumber);
			Thread.sleep(1000);
			actions.sendKeys(Keys.TAB).build().perform();
			Dashboard.isLoaderDisplayed();
			Dashboard.waitUntilLoaderIsNotDisplayed();

			CopyResults.destinationCutNumber().click();
			CopyResults.destinationCutNumber().clear();
			CopyResults.destinationCutNumber().sendKeys(TestData.copyJobDestinationCutNumber);
			Thread.sleep(500);
			actions.sendKeys(Keys.TAB).build().perform();
			Dashboard.isLoaderDisplayed();
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Thread.sleep(1000);

			CopyResults.destinationCutNumberInfo().sendKeys(TestData.copyJobDestinationCutNumberInfo);
			actions.sendKeys(Keys.TAB).build().perform();

			if (!CopyResults.destinationCutNumberInfo().getText().contains(TestData.copyJobDestinationCutNumberInfo)
					|| !CopyResults.processButton().isEnabled()) {
				Dashboard.cancelButton().click();
				Thread.sleep(1000);
			} else {
				while (CopyResults.isProcessButtonDisplayed()) {
					if (CopyResults.processButton().isEnabled()) {
						CopyResults.processButton().click();
					} else {
						Dashboard.cancelButton().click();
					}
					Thread.sleep(1000);
				}
			}
		} while (!Dashboard.isLoaderDisplayed());

		Dashboard.waitUntilOkButtonIsDisplayed();
		dismissSyncStatusPopupIfDisplayed();
		Dashboard.waitUntilOkButtonIsDisplayed();

		boolean isJobCopied = CopyResults.isJobCopySuccessfullPopupDisplayed();
		softAssert.assertTrue(isJobCopied, "Issue in copy job, Did not find Copy Job successfull popup");
		Dashboard.okButton().click();

		if (isJobCopied) {
			searchJobAndNavigationToJobDetailsPage(TestData.copyJobModule, TestData.copyJobOrg,
					TestData.copyJobDestinationJobNumber, TestData.copyJobDestinationCutNumber,
					TestData.copyJobDestinationCutNumberInfo);

			verifyJobDetailsHeader(TestData.copyJobOrg, TestData.copyJobDestinationJobNumber,
					TestData.copyJobDestinationCutNumber, TestData.copyJobDestinationCutNumberInfo,
					"After copy results, On destination Job", TestData.copyJobDestinationJobExpectedItemNumber);

			Dashboard.waitUntilLoaderIsNotDisplayed();

			Thread.sleep(2000);

			verifyTestResultsCount(TestData.copyJobDestinationJobExpectedIncompleteTestsBeforeCompletionLayer,
					TestData.copyJobDestinationJobExpectedPassedTestsBeforeCompletionLayer,
					TestData.copyJobDestinationJobExpectedFailedTestsBeforeCompletionLayer,
					"destination Job before clicking on completion tab for Job # " + TestData.copyJobSourceJobNumber + ". ");

			softAssert.assertEquals(JobDetailsPage.OTDR_Length().getText(),
					TestData.copyJobDestinationJobExpectedOtdrLength,
					"After copying job, On destination job - OTDR Length mismatch.");

			softAssert.assertEquals(JobDetailsPage.helixFactor().getText().trim(),
					TestData.copyJobDestinationJobExpectedHelixFactor,
					"After copying job, On destination job - Helix Factor mismatch.");

			JobDetailsPage.completionTab().click();

			Dashboard.waitUntilLoaderIsNotDisplayed();

			Thread.sleep(2000);

			verifyTestResultsCount(TestData.copyJobDestinationJobExpectedIncompleteTestsAfterCompletionLayer,
					TestData.copyJobDestinationJobExpectedPassedTestsAfterCompletionLayer,
					TestData.copyJobDestinationJobExpectedFailedTestsAfterCompletionLayer,
					"destination Job after clicking on completion tab for Job # " + TestData.copyJobSourceJobNumber);

		} else {
			Dashboard.cancelButton().click();
		}
	}

	public static void enterIseOseValuesAndVerifyOverrideMeterMarkFlow(String iseValue, String oseValue,
			String expectedSeqMarkTestResult, String meterMarkValue, String uomValue,
			String expectedMeterMarkValidationStatus) throws Exception {

		softAssert.assertTrue(JobDetailsPage.isCompletionTabDisplayed(), "Completion tab is not displayed.");
		JobDetailsPage.completionTab().click();
		Completion.is_ISE_SeqMark_Test_Displayed();
		Completion.iseSeqMark().clear();
		Completion.oseSeqMark().clear();
		if (!uomValue.equals("")) {
			Completion.iseSeqMark_UoM().sendKeys(uomValue);
			Completion.oseSeqMark_UoM().sendKeys(uomValue);
		}
		Completion.iseSeqMark().sendKeys(iseValue);
		Completion.oseSeqMark().sendKeys(oseValue);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);

		String seqMarkTextMismatchMessage = "Default message which is modified later based on expected seq mark test result";
		if (expectedSeqMarkTestResult.equals("PASS")) {
			seqMarkTextMismatchMessage = "Seq Mark test result was supposed to be Pass.";
		} else if (expectedSeqMarkTestResult.equals("FAIL")) {
			seqMarkTextMismatchMessage = "Seq Mark test result was supposed to be Fail.";
			Dashboard.waitUntilOkButtonIsDisplayed();
			Dashboard.okButton().click();
//			if (Dashboard.isOkButtonDisplayed()) {
//				Dashboard.okButton().click();
//			}
		} else if (expectedSeqMarkTestResult.equals("Incomplete")) {
			seqMarkTextMismatchMessage = "Seq Mark test result was supposed to be Incomplete";
		}
		Dashboard.waitUntilLoaderIsNotDisplayed();
		softAssert.assertEquals(Completion.getIseTestResult(), expectedSeqMarkTestResult, seqMarkTextMismatchMessage);
		softAssert.assertEquals(Completion.getOseTestResult(), expectedSeqMarkTestResult, seqMarkTextMismatchMessage);

		JobDetailsPage.reportsTab().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		Dashboard.okButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Reports.waitUntilDownloadOCR_ReportIsDisplayed();
		boolean isMeterMarkFieldDisplayed = Reports.isMeterMarkInputFieldDisplayed();
		if (TestData.testEnvironment.contains("executive")) {
			if (expectedSeqMarkTestResult.equals("PASS")) {
				softAssert.assertFalse(isMeterMarkFieldDisplayed,
						"Meter Mark input field is not supposed to be displayed as the Ise/Ose Seq mark tests have Passed.");
			} else if (expectedSeqMarkTestResult.equals("FAIL")) {
				softAssert.assertTrue(isMeterMarkFieldDisplayed,
						"Meter Mark input field is supposed to be displayed as the Ise/Ose Seq mark tests have Failed.");
			} else if (expectedSeqMarkTestResult.equals("Incomplete")) {
				softAssert.assertTrue(isMeterMarkFieldDisplayed,
						"Meter Mark input field is supposed to be displayed as the Ise/Ose Seq mark tests are Incomplete.");
			}
			if (isMeterMarkFieldDisplayed) {
				softAssert.assertEquals(Reports.getMeterMarkValue(), null,
						"Mismatch in the default value displayed in meter mark field.");
				softAssert.assertEquals(Reports.getMeterMarkValidationStatus(), "Incomplete",
						"Mismatch in the default meter mark validation status.");
				String expectedUoM = uomValue;
				if (uomValue.equals("")) {
					expectedUoM = "m";
				}
				softAssert.assertEquals(Reports.getMeterMarkUoM(), expectedUoM,
						"Mismatch in the UoM displayed for meter mark field.");
				Reports.meterMarkInputField().clear();
				Reports.meterMarkInputField().sendKeys(meterMarkValue);
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
				Reports.checkMeterMarkButton().click();
				Dashboard.waitUntilOkButtonIsDisplayed();
				if (expectedMeterMarkValidationStatus.equalsIgnoreCase("Incomplete")) {
					softAssert.assertEquals(Dashboard.getMessageDisplayedOnPopup(), "A value must be provided",
							"Mismatch in the text displayed on Meter Mark validation popup");
				} else if (expectedMeterMarkValidationStatus.equals("Pass")) {
					softAssert.assertTrue(Dashboard.getMessageDisplayedOnPopup().contains("Status: Pass"),
							"Mismatch in Meter Mark validation success popup, expected Status: Pass");
				} else if (expectedMeterMarkValidationStatus.equals("Fail")) {
					softAssert.assertTrue(Dashboard.getMessageDisplayedOnPopup().contains("Status: Fail"),
							"Mismatch in Meter Mark validation success popup, expected Status: Fail");
				}
				Dashboard.okButton().click();
				softAssert.assertEquals(Reports.getMeterMarkValue(), meterMarkValue,
						"Mismatch in the value displayed in meter mark field.");
				softAssert.assertEquals(Reports.getMeterMarkUoM(), expectedUoM,
						"Mismatch in the UoM displayed for meter mark field.");
				softAssert.assertEquals(Reports.getMeterMarkValidationStatus(), expectedMeterMarkValidationStatus,
						"Mismatch in the meter mark validation status.");
			}
			if (!Reports.overrideCheckbox().isSelected()) {
				Reports.overrideCheckbox().click();
				Thread.sleep(1000);
			}
			Reports.overrideComment().sendKeys("Testing override from veltris side in meter mark validation flow");
			Reports.overrideSaveButton().click();
			Dashboard.waitUntilOkButtonIsDisplayed();
			String expectedOverridePopupMessage = "This is default message which is updated later based on meter mark validation status";
			if (!expectedMeterMarkValidationStatus.equals("Pass")) {
				expectedOverridePopupMessage = "Please ensure Meter Mark validation is successful before overriding the Hold for Approval.";
			} else {
				expectedOverridePopupMessage = "Comments Saved Successfully!";
			}
			softAssert.assertEquals(Dashboard.getMessageDisplayedOnPopup(), expectedOverridePopupMessage,
					"Mismatch in override popup message in mark validation flow");
			Dashboard.okButton().click();
		} else {
			softAssert.assertFalse(isMeterMarkFieldDisplayed,
					"Meter Mark input field is not supposed to be displayed to a Tester.");
		}
		String expectedMessageOnDownloadReportWarningPopup = "Default message which is changed later based on role and meter mark validation status";
		if (!expectedMeterMarkValidationStatus.equals("Pass")) {
			if (TestData.testEnvironment.contains("executive")) {
				expectedMessageOnDownloadReportWarningPopup = "Please ensure Meter Mark validation is successful before downloading the report.";
			} else {
				expectedMessageOnDownloadReportWarningPopup = "Meter Mark validation must be completed successfully before downloading the report. Please contact your Team Lead or Manager to complete the validation.";
			}
		} else {
			expectedMessageOnDownloadReportWarningPopup = "report in background and will be available soon";
		}
		Reports.shippingLabelReport().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		softAssert.assertTrue(
				Dashboard.getMessageDisplayedOnPopup().contains(expectedMessageOnDownloadReportWarningPopup),
				"Mismatch in the meter mark validation text displayed on popup while downloading report.");
		Dashboard.okButton().click();
		if (expectedMeterMarkValidationStatus.equals("Pass")) {
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Dashboard.waitUntilOkButtonIsDisplayed();
			softAssert.assertEquals(Dashboard.getMessageDisplayedOnPopup(), "Report Successfully Created",
					"Mismtach in report download success popup.");
			Dashboard.okButton().click();
			Thread.sleep(1000);
			JobDetailsPage.reportsTab().click();
		}
		Reports.jacketTestReport().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		softAssert.assertTrue(
				Dashboard.getMessageDisplayedOnPopup().contains(expectedMessageOnDownloadReportWarningPopup),
				"Mismatch in the meter mark validation text displayed on popup while downloading report.");
		Dashboard.okButton().click();
		if (expectedMeterMarkValidationStatus.equals("Pass")) {
			Thread.sleep(2000);
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Thread.sleep(1000);
			JobDetailsPage.reportsTab().click();
		}
		Reports.failedTestsReport().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		softAssert.assertTrue(
				Dashboard.getMessageDisplayedOnPopup().contains(expectedMessageOnDownloadReportWarningPopup),
				"Mismatch in the meter mark validation text displayed on popup while downloading report.");
		Dashboard.okButton().click();
		if (expectedMeterMarkValidationStatus.equals("Pass")) {
			Thread.sleep(2000);
			Dashboard.waitUntilLoaderIsNotDisplayed();
			Thread.sleep(1000);
			JobDetailsPage.reportsTab().click();
		}
	}

	public static void verify_Override_Meter_Mark_Validation_With_Tester_Role() throws Exception {

		TestData.useOfficeOtdr = false;

		updateTestSettings();

		searchJobAndNavigationToJobDetailsPage(TestData.wtcTestModuleName, TestData.jobSearchOrg,
				TestData.wtcTestJobSearchJobNumber, TestData.wtcTestJobSearchCutNumber,
				TestData.wtcTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.wtcTestJobSearchJobNumber,
				TestData.wtcTestJobSearchCutNumber, TestData.wtcTestJobSearchCutNumberInfo,
				"WTC test with Job # " + TestData.wtcTestJobSearchJobNumber, TestData.wtcTestExpectedItemNumber);

		if (!isGetLengthAndEditAdjLengthDoneForWtcJob) {
			runGetLengthTest(TestData.wtcTestModuleName);
			editAdjLength(TestData.wtcTestEditAdjLengthValue);
			isGetLengthAndEditAdjLengthDoneForWtcJob = true;
		}

		Dashboard.waitUntilLoaderIsNotDisplayed();
		JobDetailsPage.opticsTab().click();
		robot.mouseWheel(100);
		enterIseOseValuesAndVerifyOverrideMeterMarkFlow("", "", "Incomplete", null, "", "Incomplete");
		enterIseOseValuesAndVerifyOverrideMeterMarkFlow("1000", "2000", "FAIL", "999.999", "ft", "Fail");
	}

	public static void verify_Override_Meter_Mark_Validation_With_Executive_Role() throws Exception {

		if (!isExecutiveProfileLoggedIn) {
			if (!TestData.isAppLogInRequired) {
				log_Out_And_Close_Application();
			}
			if (!isAppLaunched) {
				launch_ECQTS_Application();
			}
			TestData.testEnvironment += " executive";
			loginToApplication();
			isExecutiveProfileLoggedIn = true;
		}

		TestData.useOfficeOtdr = false;

		updateTestSettings();

		searchJobAndNavigationToJobDetailsPage(TestData.wtcTestModuleName, TestData.jobSearchOrg,
				TestData.wtcTestJobSearchJobNumber, TestData.wtcTestJobSearchCutNumber,
				TestData.wtcTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.wtcTestJobSearchJobNumber,
				TestData.wtcTestJobSearchCutNumber, TestData.wtcTestJobSearchCutNumberInfo,
				"WTC test with Job # " + TestData.wtcTestJobSearchJobNumber, TestData.wtcTestExpectedItemNumber);

		if (!isGetLengthAndEditAdjLengthDoneForWtcJob) {
			runGetLengthTest(TestData.wtcTestModuleName);
			editAdjLength(TestData.wtcTestEditAdjLengthValue);
			isGetLengthAndEditAdjLengthDoneForWtcJob = true;
		}

		Dashboard.waitUntilLoaderIsNotDisplayed();
		JobDetailsPage.opticsTab().click();
		robot.mouseWheel(100);
		enterIseOseValuesAndVerifyOverrideMeterMarkFlow("1000", "2000", "FAIL", "1000.001", "m", "Fail");
		enterIseOseValuesAndVerifyOverrideMeterMarkFlow("2765", "0", "FAIL", "2765", "m", "Pass");
		JobDetailsPage.completionTab().click();
		softAssert.assertTrue(Completion.is_ISE_SeqMark_Test_Displayed(),
				"Waited for 5 seconds, ISE Sequence test is not displayed ");
		Completion.iseSeqMark().clear();
		Completion.iseSeqMark_UoM().sendKeys("ft");
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		JobDetailsPage.reportsTab().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		Dashboard.okButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Reports.waitUntilDownloadOCR_ReportIsDisplayed();
		softAssert.assertEquals(Reports.getMeterMarkValue(), "2765",
				"Meter mark value was supposed to save and display the same as the validation was passed before switching to completion tab.");
		softAssert.assertEquals(Reports.getMeterMarkUoM(), "m",
				"Meter mark UoM was supposed to save and display the same as the validation was passed before switching to completion tab.");
		softAssert.assertEquals(Reports.getMeterMarkValidationStatus(), "Pass",
				"Meter mark validation status was supposed to save and display the same as the validation was passed before switching to completion tab.");
		enterIseOseValuesAndVerifyOverrideMeterMarkFlow("2634", "0", "PASS", "NA as SeqMarkTest passed", "m", "Pass");
	}

	public static void takeDump(String label, int delayInSecondsWhileTakingDumpBeforeClosingPowerShell)
			throws Exception {
		if (label.equals("afterJobSearch")) {
			launchDependentApplication("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe");
			Thread.sleep(3000);
			actions.sendKeys("Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass\n").build().perform();
			actions.sendKeys("cd D:\\tmp\\memdumps\\\n").build().perform();
		} else {
			try {
				Thread.sleep(2000);
				actions.keyDown(Keys.ALT).sendKeys(Keys.TAB).keyUp(Keys.ALT).build().perform();
				Thread.sleep(1000);
			} catch (Exception e) {
				launchDependentApplication("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe");
				Thread.sleep(3000);
				actions.sendKeys("Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass\n").build().perform();
				actions.sendKeys("cd D:\\tmp\\memdumps\\\n").build().perform();
			}
		}
		actions.sendKeys(".\\capture-dump.ps1\n").build().perform();
		Thread.sleep(1000);
		actions.sendKeys(label).build().perform();
		actions.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(delayInSecondsWhileTakingDumpBeforeClosingPowerShell * 1000);
//		actions.keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).build().perform();
	}

	public static void runFiberTestForAllFibersInJob(boolean shouldNavigateToSettingsAndBackToFiberResults,
			int numberOfFibersToTestBeforeTakingDump, int delayInSecondsWhileTakingDumpBeforeClosingPowerShell)
			throws Exception {
		Dashboard.waitUntilLoaderIsNotDisplayed();
		dismissSyncStatusPopupIfDisplayed();
		JobDetailsPage.isProtectionLayerTabDisplayed();
		wait = new WebDriverWait(driver, 40);
		WebElement bufferTube = driver.findElement(By
				.xpath("//Text[@AutomationId='OpticsButton']/../following-sibling::ListItem/Text[@Name='10-YELLOW']"));
		wait.until(ExpectedConditions.elementToBeClickable(bufferTube));
		while (!FiberResults.isRunTestsButtonDisplayed()) {
			bufferTube.click();
		}
		FiberResults.showMoreInfoButton().click();
		int i = 1;
		while (i <= 288) {
			Dashboard.waitUntilLoaderIsNotDisplayed();
			dismissSyncStatusPopupIfDisplayed();
			By dynamicRunTestButtonXpath = By.xpath("//Text[@Name='" + i + " / 288']/..//Button[@Name='Run Tests']");
			while (true) {
				try {
					dismissSyncStatusPopupIfDisplayed();
					while (!isElementDisplayed(dynamicRunTestButtonXpath, 2)) {
						System.out.println(
								"**Run test for fiber " + i + " / 288 is not displayed hence scrolling down**");
						int j = i - 1;
						By previousFiberNumberXpath = By.xpath("//Text[@Name='" + j + " / 288']");
						driver.findElement(previousFiberNumberXpath).click();
						robot.mouseWheel(2);
					}
					System.out.println("\nNext test to run - " + i + "/288");
					WebElement dynamicRunTestButton = driver.findElement(dynamicRunTestButtonXpath);
					dynamicRunTestButton.click();
					break;
				} catch (Exception e) {
					dismissSyncStatusPopupIfDisplayed();
					System.out.println("**Clicking on Run test for fiber " + i + " / 288 failed, trying again**");
				}
			}

			System.out.println("  - Clicked on Run test");

			while (true) {
				try {
					dismissSyncStatusPopupIfDisplayed();
					Dashboard.waitUntilOkButtonIsDisplayed();
					wait = new WebDriverWait(driver, 40);
					wait.until(ExpectedConditions.elementToBeClickable(Dashboard.okButton()));
					Thread.sleep(1000);
					Dashboard.okButton().click();
					i++;
					break;
				} catch (Exception e) {
					dismissSyncStatusPopupIfDisplayed();
					System.out.println("**Clicking on Ok button in graph failed, trying again**");
				}
			}

			System.out.println("  - Clicked on Ok button in graph");

			Thread.sleep(2000);
			dismissSyncStatusPopupIfDisplayed();

			if (FiberResults.isGoToFiberButtonVisible()) {
				wait = new WebDriverWait(driver, 20);
				try {
					dismissSyncStatusPopupIfDisplayed();
					wait.until(ExpectedConditions.elementToBeClickable(FiberResults.goToFiberButton()));
					FiberResults.goToFiberButton().click();
					System.out.println("  - Clicked on Go to fiber button");
				} catch (Exception e) {
					System.out.println("**Clicking on Go to fiber button failed**");
				}
			} else {
				System.out.println("**Go to fiber button was not visible**");
			}

			while (true) {
				try {
					dismissSyncStatusPopupIfDisplayed();
					wait = new WebDriverWait(driver, 40);
					if (TestData.useOfficeOtdr) {
						FiberResults.waitUntilStopTestsButtonIsDisplayed();
						FiberResults.stopButton().click();
						System.out.println("  - Clicked on Stop test button");
					} else {
						Dashboard.waitUntilOkButtonIsDisplayed();
						Dashboard.cancelButton().click();
						System.out.println("  - Clicked on Cancel button to stop tests");
					}
					break;
				} catch (Exception e) {
					dismissSyncStatusPopupIfDisplayed();
					System.out.println("**Clicking on Stop test button failed, trying again**");
				}
			}

			dismissSyncStatusPopupIfDisplayed();
			int testedFibers = i - 1;
			if (testedFibers % numberOfFibersToTestBeforeTakingDump == 0) {
				int additionalDelay = (testedFibers / 50) * 5;
				takeDump("dump_After_" + (i - 1) + "_Fiber_Tests",
						delayInSecondsWhileTakingDumpBeforeClosingPowerShell + additionalDelay);
				System.out.println("TOOK DUMP after " + (i - 1) + " fibers are tested");
			}

			if (Dashboard.isOpenNavigationButtonDisplayed() && shouldNavigateToSettingsAndBackToFiberResults) {
				try {
					Dashboard.openNavigationButton().click();
				} catch (Exception e) {
					System.out.println(
							"**Clicking on Open Navigation failed, moving ahead to test next fiber without navigating to settings**");
				}
				if (SideMenu.isSettingsButtonDisplayed()) {
					SideMenu.settingsButton().click();
					try {
						Dashboard.backArrow().click();
					} catch (Exception e) {
						System.out.println("**Trying to click on Back Arrow from Catch block**");
						Thread.sleep(3000);
						Dashboard.backArrow().click();
					}
					System.out.println("  - Clicked on Back Arrow from settings page");
					dismissSyncStatusPopupIfDisplayed();

				} else {
					System.out.println(
							"**Dashboard button in Open Navigation side menu is not displayed, closing navigation and moving ahead to test next fiber without navigating to settings**");
					try {
						Dashboard.closeNavigationButton().click();
					} catch (Exception e) {
						System.out.println("**Clicking on close navigation failed, moving ahead to test next fiber**");
					}
				}
			} else if (!shouldNavigateToSettingsAndBackToFiberResults) {

			} else {
				System.out.println(
						"**Did not find Open navigation, moving ahead to test next fiber without navigating to settings**");
			}
		}
	}

	public static void enterCompletionLayerValues(String module) {
		try {
			Dashboard.waitUntilLoaderIsNotDisplayed();
			JobDetailsPage.wait_Until_OSE_Button_Is_Enabled();
//			JobDetailsPage.isCompletionTabDisplayed();
			JobDetailsPage.completionTab().click();
			if (TestData.tightBufferModuleName.equals(module)) {
				softAssert.assertTrue(Completion.isReelSizeDisplayed(),
						"Waited for 10 seconds, Reel Size is not displayed ");
				Completion.reelSize().sendKeys("42in Wood Reel");
				actions.sendKeys(Keys.ENTER).perform();
				Completion.jacketColor().sendKeys("2");
				return;
			}
			softAssert.assertTrue(Completion.is_ISE_SeqMark_Test_Displayed(),
					"Waited for 10 seconds, ISE Sequence test is not displayed ");

			Completion.oseSeqMark().sendKeys("1");
			Completion.oseSeqMark_UoM().sendKeys("m");

			Completion.iseSeqMark_UoM().sendKeys("m");
			if (TestData.fiberTestModuleName.equals(module)) {
				Completion.iseSeqMark().sendKeys(TestData.fiberTestCompletionTabIseSeqValue);

				Dashboard.isLoaderDisplayed();
				Dashboard.waitUntilLoaderIsNotDisplayed();

				Thread.sleep(1000);
				softAssert.assertEquals(Completion.getIseTestResult(), "PASS",
						"Mismatch in Completion tab ISE Seq test result.");
				softAssert.assertEquals(Completion.getOseTestResult(), "PASS",
						"Mismatch in Completion tab OSE Seq test result.");

				Completion.OSE_Print_Spacing().sendKeys("2");
				Completion.ISE_Print_Spacing().sendKeys("2");
			}
			Completion.iseSeqMark().clear();
			Completion.iseSeqMark()
					.sendKeys(String.valueOf(Integer.parseInt(TestData.fiberTestCompletionTabIseSeqValue) + 1));

			Dashboard.isLoaderDisplayed();

			softAssert.assertTrue(Completion.isInvalidMeterMarksPopupDisplayed(),
					"Waited for 30 seconds, Invalid Meter Marks popup is not visible");

			if (Completion.isInvalidMeterMarksPopupDisplayed()) {
				Completion.okButton().click();
				Thread.sleep(2000);
			}

			softAssert.assertEquals(Completion.getIseTestResult(), "FAIL",
					"Mismatch in Completion tab ISE Seq test result.");
			softAssert.assertEquals(Completion.getOseTestResult(), "FAIL",
					"Mismatch in Completion tab OSE Seq test result.");

			Completion.ISE_Print_Verified().sendKeys("2");
			Completion.OSE_Print_Verified().sendKeys("2");

			Completion.reelItem().sendKeys(TestData.fiberTestReelItem);

			Completion.jacketColor().sendKeys("2");

			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);

			Thread.sleep(1000);

		} catch (Exception e) {
			System.out.println(
					"****Could not enter all the values in Completion tab, possibly this job does not have all fields****");
		}
	}

	public static void log_Out_And_Close_Application() throws Exception {
		if (Dashboard.isOpenNavigationButtonDisplayed()) {
			Dashboard.openNavigationButton().click();
			Thread.sleep(1000);
		}
		if (!SideMenu.isDashboardButtonDisplayed()) {
			Dashboard.openNavigationButton().click();
		}
		SideMenu.logOutButton().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		Dashboard.okButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Thread.sleep(2000);
		softAssert.assertEquals(SignIn.getLoggedOutText(), "You are now logged out. Restart the app to log in again.",
				"Mismatch in logged out page text.");
		SignIn.closeAppIcon().click();
		isAppLaunched = false;
		TestData.isAppLogInRequired = true;
	}

	public static void verify_Anomaly_Status() throws Exception {

		if (!TestData.isAppLogInRequired) {
			log_Out_And_Close_Application();
		}

		if (!isAppLaunched) {
			launch_ECQTS_Application();
		}

		TestData.testEnvironment += " executive";

		loginToApplication();

		isExecutiveProfileLoggedIn = true;

		TestData.useOfficeOtdr = true;

		updateTestSettings();

		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.jobSearchOrg,
				TestData.fiberTestJobSearchJobNumber, TestData.fiberTestJobSearchCutNumber,
				TestData.fiberTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber, TestData.fiberTestExpectedItemNumber);

		runGetLengthTest(TestData.fiberTestModuleName);

		runFiberTest(TestData.fiberTestModuleName, TestData.numberOfFibersToTestForAnomalyVerification);

		FiberResults.enterSpliceGainValues();

		Dashboard.waitUntilLoaderIsNotDisplayed();
		JobDetailsPage.wait_Until_OSE_Button_Is_Enabled();
		JobDetailsPage.completionTab().click();

		softAssert.assertEquals(JobDetailsPage.getAnomalyStatus(), "Pending",
				"Before clicking on Reports tab, Anomaly status is not as expected.");
		JobDetailsPage.anomalyInfoIcon().click();
		JobDetailsPage.waitUntilAnomalyDetailsPopupIsDisplayed();
		softAssert.assertEquals(Dashboard.getMessageDisplayedOnPopup(), "No fibers with detected anomalies.",
				"Before clicking on Reports tab, Anomaly Details popup message is not as expected.");
		Dashboard.okButton().click();
		JobDetailsPage.reportsTab().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		String holdForApprovalMessage = Dashboard.getMessageDisplayedOnPopup();
//		System.out.println(holdForApprovalMessage);
		int incompleteOrFailedTestsCountFromHoldForApprovalPopup = holdForApprovalMessage
				.split("Incomplete or Failed").length - 1;
		int anomalyFibersCountFromHoldForApprovalPopup = holdForApprovalMessage.split("Anomaly -").length - 1;
//		System.out.println("Failed Count : " + incompleteOrFailedTestsCountFromHoldForApprovalPopup);
//		System.out.println("Anomaly Count" + anomalyFibersCountFromHoldForApprovalPopup);
		softAssert.assertEquals(incompleteOrFailedTestsCountFromHoldForApprovalPopup,
				TestData.expectedIncompleteOrFailedTestsOnHoldForApprovalPopupInAnomalyVerificationFlow,
				"Mismatch in Incomplete or Failed tests count displayed on hold for approval popup.");
		softAssert.assertEquals(anomalyFibersCountFromHoldForApprovalPopup, TestData.expectedAnomalyFibersCount,
				"Mismatch in Anomaly detected fibers count displayed on hold for approval popup.");
		Dashboard.okButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Reports.waitUntilDownloadOCR_ReportIsDisplayed();
		softAssert.assertEquals(JobDetailsPage.getAnomalyStatus(), TestData.expectedAnomalyStatus,
				"After clicking on Reports tab, Anomaly status is not as expected.");
		JobDetailsPage.anomalyInfoIcon().click();
		JobDetailsPage.waitUntilAnomalyDetailsPopupIsDisplayed();
		String anomalyDetailsMessage = Dashboard.getMessageDisplayedOnPopup();
//		System.out.println("After clicking on Reports Tab, Anomaly Details Message - " + anomalyDetailsMessage);
		softAssert.assertTrue(anomalyDetailsMessage.contains("The following fibers have detected anomalies:"),
				"After clicking on Reports tab, Anomaly Details popup message is expected to show detected anomalies.");
		int anomalyFibersCountFromAnomalyDetailsMessage = anomalyDetailsMessage.contains(":")
				? anomalyDetailsMessage.split(":", 2)[1].trim().split("\\R").length
				: 0;
//		System.out.println(
//				"anomalyFibersCountFromAnomalyDetailsMessage - " + anomalyFibersCountFromAnomalyDetailsMessage);
		softAssert.assertEquals(anomalyFibersCountFromAnomalyDetailsMessage, TestData.expectedAnomalyFibersCount,
				"Mismatch in anomalies detected fibers count in anomaly details popup.");
		Dashboard.okButton().click();
		Reports.overrideTypeDropdown().sendKeys("Other");
		Reports.overrideCheckbox().click();
		Thread.sleep(1000);
		Reports.overrideComment().sendKeys("Testing Overriding Other Failure Reasons");
		Reports.overrideSaveButton().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		Dashboard.okButton().click();
		softAssert.assertEquals(JobDetailsPage.getAnomalyStatus(), TestData.expectedAnomalyStatus,
				"Anomaly status after overriding others should not change.");
		JobDetailsPage.completionTab().click();
		Completion.is_ISE_SeqMark_Test_Displayed();
		JobDetailsPage.reportsTab().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		holdForApprovalMessage = Dashboard.getMessageDisplayedOnPopup();
//		System.out.println(holdForApprovalMessage);
		incompleteOrFailedTestsCountFromHoldForApprovalPopup = holdForApprovalMessage
				.split("Incomplete or Failed").length - 1;
		anomalyFibersCountFromHoldForApprovalPopup = holdForApprovalMessage.split("Anomaly -").length - 1;
//		System.out.println("Failed Count : " + incompleteOrFailedTestsCountFromHoldForApprovalPopup);
//		System.out.println("Anomaly Count" + anomalyFibersCountFromHoldForApprovalPopup);
		softAssert.assertEquals(incompleteOrFailedTestsCountFromHoldForApprovalPopup, 0,
				"After overriding others, Mismatch in Incomplete or Failed tests count displayed on hold for approval popup.");
		softAssert.assertEquals(anomalyFibersCountFromHoldForApprovalPopup, TestData.expectedAnomalyFibersCount,
				"After overriding others, Mismatch in Anomaly detected fibers count displayed on hold for approval popup.");
		Dashboard.okButton().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Reports.waitUntilDownloadOCR_ReportIsDisplayed();
		Reports.overrideTypeDropdown().sendKeys("Anomaly");
		Reports.overrideCheckbox().click();
		Thread.sleep(1000);
		Reports.overrideComment().sendKeys("Testing Overriding Anomaly");
		Reports.overrideSaveButton().click();
		Dashboard.waitUntilOkButtonIsDisplayed();
		Dashboard.okButton().click();
		softAssert.assertEquals(JobDetailsPage.getAnomalyStatus(), "Overridden",
				"Anomaly status after overriding anomaly has not changed");
		JobDetailsPage.completionTab().click();
		Completion.is_ISE_SeqMark_Test_Displayed();
		JobDetailsPage.reportsTab().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		softAssert.assertTrue(Reports.isDownloadOCR_ReportDisplayed(),
				"Download OCR Report is not displayed after overriding others and anomaly and navigating back to completion and reports");
		softAssert.assertTrue(Reports.isJobWarnings_ErrorsPopupNotDisplayed(),
				"Job warning error popup is displayed even after overriding other and anomaly");
		JobDetailsPage.anomalyInfoIcon().click();
		JobDetailsPage.waitUntilAnomalyDetailsPopupIsDisplayed();
		anomalyDetailsMessage = Dashboard.getMessageDisplayedOnPopup();
//		System.out.println("After clicking on Reports Tab, Anomaly Details Message - " + anomalyDetailsMessage);
		softAssert.assertTrue(anomalyDetailsMessage.contains("The following fibers have detected anomalies:"),
				"After clicking on Reports tab, Anomaly Details popup message is not as expected.");
		anomalyFibersCountFromAnomalyDetailsMessage = anomalyDetailsMessage.contains(":")
				? anomalyDetailsMessage.split(":", 2)[1].trim().split("\\R").length
				: 0;
//		System.out.println(
//				"anomalyFibersCountFromAnomalyDetailsMessage - " + anomalyFibersCountFromAnomalyDetailsMessage);
		softAssert.assertEquals(anomalyFibersCountFromAnomalyDetailsMessage, TestData.expectedAnomalyFibersCount,
				"After overriding anomaly, Mismatch in anomalies detected fibers count in anomaly details popup.");
		Dashboard.okButton().click();
	}

	public static void verify_QE_Labs() throws Exception {

		if (!isExecutiveProfileLoggedIn) {
			if (!TestData.isAppLogInRequired) {
				log_Out_And_Close_Application();
			}

			if (!isAppLaunched) {
				launch_ECQTS_Application();
			}
			TestData.testEnvironment += " executive";
			loginToApplication();
			isExecutiveProfileLoggedIn = true;
		}

		try {
			navigateToModule(TestData.QE_LabsModuleName);
			softAssert.assertEquals(QE_Labs.get_QE_Labs_Comming_Soon_Text(),
					"QE Labs navigation will be restored shortly.", "Mismatch in QE Labs popup message.");
			Dashboard.okButton().click();
		} catch (Exception e) {
			softAssert.fail("Unable to verify QE Labs");
		}
	}

	public static void download_OCR_Report() throws Exception {

		if (!Reports.isDownloadOCR_ReportDisplayed()) {
			JobDetailsPage.reportsTab().click();
//			Reports.isJobWarnings_ErrorsPopupDisplayed();
			Dashboard.waitUntilOkButtonIsDisplayed();
			Dashboard.okButton().click();
		}
		Reports.waitUntilDownloadOCR_ReportIsDisplayed();
		Reports.opticalCharacteristics().click();
		softAssert.assertTrue(Reports.isGeneratingReportsInBackgroundTextDisplayed(),
				"Waited for 10 seconds, Generating Reports in background text is not displayed  ");
		Dashboard.okButton().click();
		Dashboard.isLoaderDisplayed();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		Dashboard.isLoaderDisplayed();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		JobDetailsPage.reportsTab().click();
	}

	public static void verify_If_SOR_And_OCR_Files_Downloaded() {
		softAssert.assertEquals(getFilesCount(TestData.OCR_Report_Path), 2, "Mismatch in downloaded OCR report count.");
		softAssert.assertEquals(getFilesCount(TestData.SOR_Files_Path), 4, "Mismatch in downloaded SOR files count.");
	}

	public static int getFilesCount(String folderPath) {

		File folder = new File(folderPath);
		File[] files = folder.listFiles(file -> file.isFile());
		if (files != null) {
			return files.length;
		}
		return 0;
	}

	public static void dismissSyncStatusPopupIfDisplayed() {
		try {
			if (Dashboard.isSyncStatusPopupDisplayed()) {
				Dashboard.okButton().click();
				System.out.println("\n**Closed ECQTS Sync Status popup**");
			}
		} catch (Exception e) {
			// Popup not present or already closed — safe to ignore
		}
	}

	public static void verify_Reel_Id_And_Remove_Sales_Order_Flow() throws Exception {

		verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Job_Search_Popup();

		verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Completion_Tab();

		shouldRemoveSalesOrder = true;

		verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Job_Search_Popup();

		if (JobDetailsPage.salesOrder().getText().trim().equals("")) {

			verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Completion_Tab();

			JobDetailsPage.reportsTab().click();
			Dashboard.waitUntilOkButtonIsDisplayed();
			Dashboard.okButton().click();
			Reports.shippingLabelReport().click();
			Dashboard.waitUntilOkButtonIsDisplayed();
			try {
				softAssert.assertEquals(Dashboard.getMessageDisplayedOnPopup(),
						"A sales order must be selected to generate a shipping label.",
						"Mismatch in error displayed while downloading shipping label report.");
			} catch (Exception e) {
				softAssert.fail(
						"Did not find popup with message as : A sales order must be selected to generate a shipping label.");
			}
			Dashboard.okButton().click();
		} else {
			softAssert.fail(
					"As sales order was not removed, skipping the verication of reel id changes in completion tab and reports tab.");
		}

		shouldRemoveSalesOrder = false;
	}

	public static void verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Job_Search_Popup() throws Exception {

		navigateToModule(TestData.fiberTestModuleName);

		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.jobSearchOrg,
				TestData.fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg,
				TestData.fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification,
				TestData.fiberTestExpectedItemNumberForReelIdAndSalesOrderVerification);

		if (shouldRemoveSalesOrder) {
			TestData.fiberTestExpectedSalesOrderForReelIdAndSalesOrderVerification = "";
		}
		softAssert.assertEquals(JobDetailsPage.salesOrder().getText().trim(),
				TestData.fiberTestExpectedSalesOrderForReelIdAndSalesOrderVerification,
				"Sales order mismatch in job details page");
	}

	public static void verify_Reel_Id_And_Remove_Sales_Order_Changes_In_Completion_Tab() throws Exception {

		JobDetailsPage.completionTab().click();
		Dashboard.waitUntilLoaderIsNotDisplayed();
		String message = "";
		if (!shouldRemoveSalesOrder) {
			Completion.reelItem().sendKeys(TestData.fiberTestReelItemForReelIdAndSalesOrderVerification);
			Dashboard.isLoaderDisplayed();
			Dashboard.isLoaderNotDisplayed();
			Thread.sleep(5000);
			Dashboard.isLoaderNotDisplayed();
			message = "Fiber test with sales order for Job # ";
		} else {
			message = "Fiber test after removing sales order for Job # ";
			Thread.sleep(2000);
		}

		verifyTestResultsCount(TestData.incompleteTestCountForReelIdAndSalesOrderVerification,
				TestData.passedTestCountForReelIdAndSalesOrderVerification,
				TestData.failedTestCountForReelIdAndSalesOrderVerification,
				message + TestData.fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification + ".");

		softAssert.assertEquals(Completion.reelItem().getText(), TestData.fiberTestReelItemForReelIdAndSalesOrderVerification,
				"Reel item mismatch in Completion Tab.");
		softAssert.assertEquals(Completion.reelItem().getAttribute("IsKeyboardFocusable"), "True",
				"Reel Item field should be editable.");
		softAssert.assertEquals(Completion.getReelItemResult(), "PASS", "Reel Item result was supposed to be Pass.");
		try {
			softAssert.assertEquals(Completion.iseReelLabel().getText(), TestData.fiberTestExpectedIseReelLabel,
					"ISE Reel Label mismatch in Completion Tab.");
			softAssert.assertEquals(Completion.iseReelLabel().getAttribute("IsKeyboardFocusable"), "False",
					"ISE Reel Label field should be non-editable.");
			softAssert.assertEquals(Completion.getIseReelLabelResult(), "PASS",
					"ISE Reel Label result was supposed to be Pass.");

			softAssert.assertEquals(Completion.oseReelLabel().getText(), TestData.fiberTestExpectedOseReelLabel,
					"OSE Reel Label mismatch in Completion Tab.");
			softAssert.assertEquals(Completion.oseReelLabel().getAttribute("IsKeyboardFocusable"), "False",
					"OSE Reel Label field should be non-editable.");
			softAssert.assertEquals(Completion.getOseReelLabelResult(), "PASS",
					"OSE Reel Label result was supposed to be Pass.");

			softAssert.assertEquals(Completion.reelLabel().getText(), TestData.fiberTestExpectedReelLabel,
					"Reel Label mismatch in Completion Tab.");
			softAssert.assertEquals(Completion.reelLabel().getAttribute("IsKeyboardFocusable"), "False",
					"Reel Label field should be non-editable.");
			softAssert.assertEquals(Completion.getReelLabelResult(), "PASS",
					"Reel Label result was supposed to be Pass.");
		} catch (Exception e) {
			if (!shouldRemoveSalesOrder) {
				softAssert.fail(
						"Reel Label is not available after entering steel Reel Item when sales order was selected.");
			} else {
				softAssert.fail(
						"Reel Label is not available after removing sales order for a Job which had reel label when sales order was selected.");
			}
		}

	}

	public static void runWtcTestForAllRibbonsInJob(int numberOfRibbonsToTestBeforeTakingDump,
			int delayInSecondsWhileTakingDumpBeforeClosingPowerShell) throws Exception {
		Dashboard.waitUntilLoaderIsNotDisplayed();
		dismissSyncStatusPopupIfDisplayed();
		JobDetailsPage.isWtcTabDisplayed();
		JobDetailsPage.wtcTab().click();
		dismissSyncStatusPopupIfDisplayed();
		WTC.isRunTestsButtonDisplayed();
		int numberOfRibbonsTested = 0;
		for (int i = 1; numberOfRibbonsTested < 72; i++) {
			dismissSyncStatusPopupIfDisplayed();
			String nextWorkerToSelect = "JGR-" + i + "-" + i + "-" + i;
//			if (numberOfRibbonsTested >= 5) {
//				WTC.waitUntilTestIsCompletedForSelectedWorker(nextWorkerToSelect);
//			}
//			do {
			try {
				WTC.incompleteStatus().click();
				robot.mouseWheel(-1);
				WTC.selectWorkerField().click();
				actions.sendKeys(nextWorkerToSelect).build().perform();
				actions.sendKeys(Keys.TAB).build().perform();
			} catch (Exception e) {

			}
//			} while (!WTC.selectWorkerField().getText().contains(nextWorkerToSelect));
			while (true) {
				try {
					WTC.checkButton().click();
					Dashboard.waitUntilOkButtonIsDisplayed();
					while (Dashboard.isOkButtonDisplayed()) {
						try {
							Thread.sleep(1000);
							Dashboard.cancelButton().click();
							Thread.sleep(1000);
						} catch (Exception e) {
						}
					}
					break;
				} catch (Exception e) {
					if (WTC.isErrorMessageDisplayed()) {
						Dashboard.okButton().click();
					}
				}
			}
			while (true) {
				try {
					WTC.runTestsButton().click();
					break;
				} catch (Exception e) {
					System.out.println("*Unable to click on Run Test button, trying again.");
					Thread.sleep(1000);
				}
			}
			numberOfRibbonsTested++;
			robot.mouseWheel(3);
			System.out.println("Ribbons Tested - " + numberOfRibbonsTested);
			JobDetailsPage.ribbonPosition(numberOfRibbonsTested).click();
			robot.mouseWheel(1);
			if (numberOfRibbonsTested % numberOfRibbonsToTestBeforeTakingDump == 0) {
				int additionalDelay = (numberOfRibbonsTested / 50) * 5;
				takeDump("dump_After_" + numberOfRibbonsTested + "_Ribbon_Tests",
						delayInSecondsWhileTakingDumpBeforeClosingPowerShell + additionalDelay);
				System.out.println("TOOK DUMP after " + numberOfRibbonsTested + " Ribbons are tested");
			}
			if (i == 5) {
				i = 0;
			}
		}
		WTC.waitUntilStopButtonIsNotDisplayed();
	}

	public static void verify_Fiber_Test_Module() throws Exception {

		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.jobSearchOrg,
				TestData.fiberTestJobSearchJobNumber, TestData.fiberTestJobSearchCutNumber,
				TestData.fiberTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber, TestData.fiberTestExpectedItemNumber);

		enterProtectionLayerValues(TestData.fiberTestModuleName);

		runGetLengthTest(TestData.fiberTestModuleName);

		editAdjLength(TestData.fiberTestEditAdjLengthValue);

		verifyOpticsPage();

		runFiberTest(TestData.fiberTestModuleName, TestData.numberOfFibersToTest);

		downloadSorFiles();

		enterCompletionLayerValues(TestData.fiberTestModuleName);

		download_OCR_Report();

		verifyTestResultsCount(TestData.fiberTestExpectedIncompleteTestsCount,
				TestData.fiberTestExpectedPassedTestsCount, TestData.fiberTestExpectedFailedTestsCount,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
	}

	public static void verify_WTC_Test_Module() throws Exception {
//		if(!isGetLengthAndEditAdjLengthDoneForWtcJob)
//		{
//			runGetLengthTest(TestData.wtcTestModuleName);
//			editAdjLength(TestData.wtcTestEditAdjLengthValue);
//			isGetLengthAndEditAdjLengthDoneForWtcJob = true;
//		}
	}

	public static void verify_Tight_Buffer_Module() throws Exception {

		TestData.useOfficeOtdr = false;

		updateTestSettings();

		searchJobAndNavigationToJobDetailsPage(TestData.tightBufferModuleName, TestData.jobSearchOrg,
				TestData.tightBufferJobNumber, TestData.tightBufferTestJobSearchCutNumber,
				TestData.tightBufferTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.tightBufferJobNumber,
				TestData.tightBufferTestJobSearchCutNumber, TestData.tightBufferTestJobSearchCutNumberInfo,
				"Tight Buffer test with Job # " + TestData.tightBufferJobNumber,
				TestData.tightBufferExpectedItemNumber);

		enterProtectionLayerValues(TestData.tightBufferModuleName);

		runGetLengthTest(TestData.tightBufferModuleName);

		verifyOpticsPage();

		runFiberTest(TestData.tightBufferModuleName, 1);

		downloadSorFiles();

		enterCompletionLayerValues(TestData.tightBufferModuleName);

		download_OCR_Report();

		verifyTestResultsCount(TestData.tightBufferExpectedIncompleteTestsCount,
				TestData.tightBufferExpectedPassedTestsCount, TestData.tightBufferExpectedFailedTestsCount,
				"Tight Buffer with Job # " + TestData.tightBufferJobNumber);
	}

	public static void verify_PK_Fiber_Test_Module() throws Exception {

		try {
			searchJobAndNavigationToJobDetailsPage(TestData.PK_FiberTestModuleName, TestData.jobSearchOrg,
					TestData.PK_FiberTestJobSearchJobNumber, TestData.PK_FiberTestJobSearchCutNumber,
					TestData.PK_FiberTestJobSearchCutNumberInfo);

			verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.PK_FiberTestJobSearchJobNumber,
					TestData.PK_FiberTestJobSearchCutNumber, TestData.PK_FiberTestJobSearchCutNumberInfo,
					"PK Fiber test with Job # " + TestData.PK_FiberTestJobSearchJobNumber,
					TestData.PK_FiberTestExpectedItemNumber);

			enterProtectionLayerValues(TestData.PK_FiberTestModuleName);

			verifyOpticsPage();

//		runFiberTest(TestData.PK_FiberTestModuleName, 1);

//		downloadSorFiles();

			enterCompletionLayerValues(TestData.PK_FiberTestModuleName);

//		download_OCR_Report();

			verifyTestResultsCount(TestData.PK_FiberTestExpectedIncompleteTestsCount,
					TestData.PK_FiberTestExpectedPassedTestsCount, TestData.PK_FiberTestExpectedFailedTestsCount,
					"PK Fiber Test with Job # " + TestData.PK_FiberTestJobSearchJobNumber);
		} catch (Exception e) {
			softAssert.fail("Unable to verify PK Fiber Test module");
		}
	}

}