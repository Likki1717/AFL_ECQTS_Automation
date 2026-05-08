package base;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.Socket;
import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.windows.WindowsDriver;
import pageObjects.CommonPages.Dashboard;
import pageObjects.CommonPages.SideMenu;
import pageObjects.CommonPages.SignIn;
import pageObjects.Modules.ImportData.ImportSideMenu;
import pageObjects.sideMenu.About;
import pageObjects.sideMenu.Settings;
import pageObjects.sideMenu.settings.ConnectionProfiles;
import pageObjects.sideMenu.settings.TestSettings;

public class BaseClass {
	public static WindowsDriver<?> driver;
	public static Actions actions;
	public static WebDriverWait wait;
	public static Robot robot;
	public static DesiredCapabilities capabilities;
	public static SoftAssert softAssert = new SoftAssert();

	public static void clearPreviousSessionData() throws Exception {
		int deleteAttempts = 0;

		// Folder path that needs to be cleared before starting a new session
		File secureStorageFolder = new File(TestData.secureStorageFolderPath);

		// Keep trying to delete the secureStorageFolder folder if it exists, this will
		// make sure the app is not logged In
		while (secureStorageFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(secureStorageFolder);
			} else {
				// If deletion failed, likely due to files being in use
				System.out.println("****Close any open app / file and run the script again****");
				System.exit(0);
			}
		}

		File OcrReportFolder = new File(TestData.OCR_Report_Path);

		// Delete the OCR Report folder if it exists, to verify if the newly downloaded
		// Ocr file is available in this folder
		while (OcrReportFolder.exists()) {
			if (deleteAttempts == 0) {
				deleteAttempts++;
				deleteDirectory(OcrReportFolder);
			} else {
				// If deletion failed, likely due to files being in use
				System.out.println("****Close any open app / file and run the script again****");
				System.exit(0);
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
	}

	public static void launchOpenVpnAppAndConnect() throws Exception {
		if (TestData.useOfficeOtdr) {
			launchDependentApplication(TestData.openVpnAppPath);
			robot.delay(2000);
			for (int i = 0; i < 4; i++) {
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
			}
			robot.delay(2000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(2000);
			copyPasteAndClickEnter(TestData.vpnAppPassword);
		}
	}

	public static void launchDependentApplication(String applicationPath) throws Exception {
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_WINDOWS);
			robot.keyPress(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_WINDOWS);
			robot.delay(300);
			copyPasteAndClickEnter(applicationPath);
		} catch (Exception e) {
			System.out.println("****Exception in launchDependentApplication()****");
			System.exit(0);
		}
	}

	public static void copyPasteAndClickEnter(String textToCopy) {
		// Copy path to clipboard
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

	public static void launch_ECQTS_Application() throws Exception {
		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", TestData.appId());
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("automationName", "Windows");
			driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
			actions = new Actions(driver);
//			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("****Exception in launchApplication()****");
			System.exit(0);
		}
	}

	public static void verifyIncorrectCredentials() {
		SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
		SignIn.passwordField().sendKeys("Invalid password");
		SignIn.signInButton().click();
		softAssert.assertTrue(SignIn.isloginFailureDisplayed(),
				"Tried for 5 secs, Login failed message was not visible");
		SignIn.okButtonOnLoginFailurePopup().click();
	}

	public static void loginToApplication() {
		try {
			SignIn.usernameField().clear();
			SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
			SignIn.passwordField().clear();
			SignIn.passwordField().sendKeys(TestData.ecqtsAppPassword());
			SignIn.signInButton().click();
			softAssert.assertTrue(Dashboard.isFiberTestModuleDisplayed(),
					"Tried for 40 secs, Fiber Test module was not visible after login");
		} catch (Exception e) {
			System.out.println("****Exception in loginToApplication()****");
			System.exit(0);
		}
	}

	public static boolean isElementDisplayed(By locator, int timeoutSeconds) {
		try {
			wait = new WebDriverWait(driver, timeoutSeconds);
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
			softAssert.assertTrue(appVersion.contains(TestData.expectedAppVersion),
					"Expected build version was " + TestData.expectedAppVersion + " but found " + appVersion);
			String expectedTextInPortalLink = (TestData.testEnvironment.equals("Dev")
					|| TestData.testEnvironment.equals("QA")) ? TestData.testEnvironment.toLowerCase()
							: TestData.prodWebUrl;
			softAssert.assertTrue(About.portalLink().getText().contains(expectedTextInPortalLink),
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

	public static void deleteAllExistingConnectionProfiles() {
		try {
			Dashboard.openNavigationButton().click();
			SideMenu.settingsButton().click();
			Settings.connectionProfilesButton().click();
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
		ConnectionProfiles.createNewProfile().click();
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
		ConnectionProfiles.Port().clear();
		ConnectionProfiles.Port().sendKeys(port);
		if (connectionProfileName.contains("JGR")) {
			ConnectionProfiles.switchTypeDropdown().click();
			ConnectionProfiles.JGR_Switch().click();
			ConnectionProfiles.find_Address().sendKeys("SIMULATO");
			ConnectionProfiles.switchModule().clear();
			ConnectionProfiles.switchModule().sendKeys("1");
			ConnectionProfiles.testSwitchConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
					"Tried for 15 seconds, Connection failure popup is not displayed");
			ConnectionProfiles.okButton().click();
			ConnectionProfiles.find_Address().clear();
			ConnectionProfiles.find_Address().sendKeys("SIMULATOR");
			ConnectionProfiles.testSwitchConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionSuccessfulPopupDisplayed(),
					"Tried for 10 seconds, WTC Connection is successful ");
			ConnectionProfiles.okButton().click();
			ConnectionProfiles.workerNumber().sendKeys(connectionProfileName.split("-")[1]);
			ConnectionProfiles.spoon().sendKeys(connectionProfileName.split("-")[1]);
		}
		ConnectionProfiles.Save().click();
		if((connectionProfileName.equals("Office OTDR") && TestData.useOfficeOtdr) || !connectionProfileName.equals("Office OTDR"))
		{
			validateSuccessfulConnectionPopup();
		}
	}

	public static void createConnectionProfiles() {
		createProfile("JGR-1", TestData.simulatorIP_Address, TestData.simulatorPort);
		createProfile("JGR-2", TestData.simulatorIP_Address, TestData.simulatorPort);
		createProfile("Office OTDR", TestData.officeOTDR_IP_Address, TestData.officeOTDR_Port);
		createProfile("Simulator", TestData.simulatorIP_Address, TestData.simulatorPort);
	}

	public static void validateSuccessfulConnectionPopup() {
		try {
			wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.elementToBeClickable(ConnectionProfiles.testConnection()));
			ConnectionProfiles.testConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionSuccessfulPopupDisplayed(),
					"Tried for 15 seconds, Connection successful popup is not displayed");
			ConnectionProfiles.okButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in validateSuccessfulConnectionPopup()****");
			System.exit(0);
		}

	}

	public static void editConnectionProfile() {
		ConnectionProfiles.editButton().click();
		ConnectionProfiles.instrumentTypeDropdown().click();
		ConnectionProfiles.instrumentType_Anritsu_MT_9085().click();
		softAssert.assertTrue(ConnectionProfiles.ipAddressTextBox().getAttribute("Value.Value")
				.contains(TestData.anritsu_9085_Ip_Address), "Ip address did not change when we changed instrument from Simulator to Anritsu 9085");
		ConnectionProfiles.ipAddressTextBox().clear();
		ConnectionProfiles.ipAddressTextBox().sendKeys(TestData.anritsu_9085_Ip_Address);
		ConnectionProfiles.Save().click();
		ConnectionProfiles.testConnection().click();
		softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
				"Tried for 15 seconds, Connection failed popup is not displayed");
		ConnectionProfiles.okButton().click();
		ConnectionProfiles.editButton().click();
		softAssert.assertTrue(ConnectionProfiles.ipAddressTextBox().getAttribute("Value.Value")
				.contains(TestData.anritsu_9085_Ip_Address));
		ConnectionProfiles.instrumentTypeDropdown().click();
		ConnectionProfiles.instrumentType_Simulator().click();
		ConnectionProfiles.ipAddressTextBox().clear();
		ConnectionProfiles.ipAddressTextBox().sendKeys(TestData.simulatorIP_Address);
		ConnectionProfiles.Save().click();
	}

	public static void updateTestSettings() {
		TestSettings.testSettingsButton().click();

		softAssert.assertTrue(TestSettings.isDisplayRealTimeToogleDisplayed(),
				"Tried for 3 secs, Display real time plot text is not displayed ");

		if (TestSettings.displayRealTimetoggle().isEnabled()) {
			TestSettings.displayRealTimetoggle().click();
		}
		if (TestSettings.enableOfflineReportsToggle().isEnabled()) {
			TestSettings.enableOfflineReportsToggle().click();
		}
	}

	public static void importPrysmianJob() {
		try {
			while (!Dashboard.isImportDataModuleDisplayed()) {
				Dashboard.openNavigationButton().click();
				SideMenu.dashboardButton().click();
			}
			Dashboard.importDataModule().click();
			ImportSideMenu.prysmianType().click();
			Prysmian_Import_Page.orgDropDownField().sendKeys(Test_Data.prysmian_Org);
			Prysmian_Import_Page.cutNumber().click();
			Prysmian_Import_Page.cutNumber().sendKeys(Test_Data.prysmian_Cut_Number());
			Prysmian_Import_Page.itemOrgCode().sendKeys(Test_Data.prysmian_Item_Org_Code);
			Prysmian_Import_Page.cutNumberInfo().sendKeys("ZTEST01");
			Prysmian_Import_Page.importType().sendKeys(Test_Data.prysmian_Import_Type);
			robot.mouseWheel(100);
			Thread.sleep(1000);
			Prysmian_Import_Page.uploadFileButton().click();
			Thread.sleep(3000);
			actions.moveToElement(Prysmian_Import_Page.address_Bar()).click().build().perform();
			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
			int currentX = (int) mousePosition.getX();
			int currentY = (int) mousePosition.getY();
			robot.mouseMove(currentX + 70, currentY);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			actions.sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(Test_Data.prysmian_Files_Path).build().perform();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			Prysmian_Import_Page.jacketOdFile().click();
			actions.keyDown(Keys.CONTROL).build().perform();
			Prysmian_Import_Page.attenuationFile().click();
			actions.keyUp(Keys.CONTROL).build().perform();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			try {
				Prysmian_Import_Page.fileBeingUsedPopup();
				System.out.println(
						"****CLOSE THE ALREADY OPENED PRYSMIAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED PRYSMIAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED PRYSMIAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED PRYSMIAN FILE(S) AND RUN THE SCRIPT AGAIN****");
				Prysmian_Import_Page.warning_Popup_ok_Button().click();
				System.exit(0);
			} catch (Exception e) {

			}
			Prysmian_Import_Page.submitButton().click();
			Thread.sleep(Duration.ofSeconds(40));
			boolean clickedOnOkButton = false;
			int attemptsCount = 0;
			while (clickedOnOkButton == false && attemptsCount < 400) {
				try {
					Prysmian_Import_Page.warning_Popup_ok_Button().click();
					clickedOnOkButton = true;
				} catch (Exception e) {
					Thread.sleep(2000);
					attemptsCount++;
					if (attemptsCount == 400) {
						System.out.println("****Waited too long for missing fiber id popup****");
					}
				}
			}
			Thread.sleep(3000);
			System.out.println("Job Number: " + Job_Details_Page.job_Number().getText().trim());
			if (!Job_Details_Page.test_Results()
					.equalsIgnoreCase("Incomplete: " + Test_Data.prysmian_Expected_Incomplete_Tests + ", Passed: "
							+ Test_Data.prysmian_Expected_Passed_Tests + ", Failed: "
							+ Test_Data.prysmian_Expected_Failed_Tests)) {
				System.out.println("****Prysmian Import Job Status Count is not as expected****");
				System.out.println(
						"Expected Test Status was: " + "Incomplete: " + Test_Data.prysmian_Expected_Incomplete_Tests
								+ ", Passed: " + Test_Data.prysmian_Expected_Passed_Tests + ", Failed: "
								+ Test_Data.prysmian_Expected_Failed_Tests);
				System.out.println("Current  Status found is: " + Job_Details_Page.test_Results());
			}

			if (!Job_Details_Page.helix_Factor().getText().trim()
					.equalsIgnoreCase(Test_Data.prysmian_Expected_Helix_Factor)) {
				System.out.println("\n****Prysmian Import Job Helix Factor is not as expected****");
				System.out.println("Expected Helix Factor was: " + Test_Data.prysmian_Expected_Helix_Factor);
				System.out
						.println("Current  Helix Factor found is: " + Job_Details_Page.helix_Factor().getText().trim());
			}

		} catch (Exception e) {
			System.out.println("****Exception in importPrysmianJob()****");
			e.printStackTrace();
		}
	}

	public static void importSwindonJob() {

		try {
			Side_Menu.dashboardButton().click();
			Thread.sleep(1000);
			Dashboard_Page.importDataModule().click();
			Import_Page.swindonType().click();
			Swindon_Import_Page.orgDropDownField().sendKeys(Test_Data.swindon_Org);
			Swindon_Import_Page.cutNumber().click();
			Swindon_Import_Page.cutNumber().sendKeys(Test_Data.swindon_Cut_Number());
			Swindon_Import_Page.itemOrgCode().sendKeys(Test_Data.swindon_Item_Org_Code);
			Swindon_Import_Page.cutNumberInfo().sendKeys("ZTEST01");
			Swindon_Import_Page.importType().sendKeys(Test_Data.swindon_Import_Type);
			robot.mouseWheel(100);
			Thread.sleep(1000);
			Swindon_Import_Page.uploadFileButton().click();
			Thread.sleep(3000);
			actions.moveToElement(Swindon_Import_Page.address_Bar()).click().build().perform();
			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
			int currentX = (int) mousePosition.getX();
			int currentY = (int) mousePosition.getY();
			robot.mouseMove(currentX + 70, currentY);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			actions.sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(Test_Data.swindon_Files_Path).build().perform();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			Swindon_Import_Page.jacketOdFile().click();
			actions.keyDown(Keys.CONTROL).build().perform();
			Swindon_Import_Page.attenuationFile().click();
			actions.keyUp(Keys.CONTROL).build().perform();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			try {
				Swindon_Import_Page.fileBeingUsedPopup();
				System.out.println(
						"****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****");
				Swindon_Import_Page.warning_Popup_ok_Button().click();
				System.exit(0);
			} catch (Exception e) {

			}
			Swindon_Import_Page.submitButton().click();
			Thread.sleep(Duration.ofSeconds(40));
			boolean clickedOnOkButton = false;
			int attemptsCount = 0;
			while (clickedOnOkButton == false && attemptsCount < 400) {
				try {
					Swindon_Import_Page.warning_Popup_ok_Button().click();
					clickedOnOkButton = true;
				} catch (Exception e) {
					Thread.sleep(2000);
					attemptsCount++;
					if (attemptsCount == 400) {
						System.out.println("****Waited too long for missing fiber id popup****");
					}
				}
			}
			Thread.sleep(3000);
			System.out.println("Job Number: " + Job_Details_Page.job_Number().getText().trim());
			if (!Job_Details_Page.test_Results()
					.equalsIgnoreCase("Incomplete: " + Test_Data.swindon_Expected_Incomplete_Tests + ", Passed: "
							+ Test_Data.swindon_Expected_Passed_Tests + ", Failed: "
							+ Test_Data.swindon_Expected_Failed_Tests)) {
				System.out.println("****Swindon Import Job Status Count is not as expected****");
				System.out.println(
						"Expected Test Status was: " + "Incomplete: " + Test_Data.swindon_Expected_Incomplete_Tests
								+ ", Passed: " + Test_Data.swindon_Expected_Passed_Tests + ", Failed: "
								+ Test_Data.swindon_Expected_Failed_Tests);
				System.out.println("Current  Status found is: " + Job_Details_Page.test_Results());
			}

			if (!Job_Details_Page.helix_Factor().getText().trim()
					.equalsIgnoreCase(Test_Data.swindon_Expected_Helix_Factor)) {
				System.out.println("\n****Swindon Import Job Helix Factor is not as expected****");
				System.out.println("Expected Helix Factor was: " + Test_Data.swindon_Expected_Helix_Factor);
				System.out
						.println("Current  Helix Factor found is: " + Job_Details_Page.helix_Factor().getText().trim());
			}
		} catch (Exception e) {
			System.out.println("****Exception in swindon_Import()****");
			e.printStackTrace();
		}
	}

	public static void importTaihanJob() {
		try {
			Side_Menu.dashboardButton().click();
			Thread.sleep(1000);
			Dashboard_Page.importDataModule().click();
			Import_Page.taihanType().click();
			Taihan_Import_Page.orgDropDownField().sendKeys(Test_Data.taihan_Org);
			Taihan_Import_Page.cutNumber().click();
			Taihan_Import_Page.cutNumber().sendKeys(Test_Data.taihan_Cut_Number());
			Taihan_Import_Page.itemOrgCode().sendKeys(Test_Data.taihan_Item_Org_Code);
			Taihan_Import_Page.cutNumberInfo().sendKeys("ZTEST01");
			Taihan_Import_Page.importType().sendKeys(Test_Data.taihan_Import_Type);
			robot.mouseWheel(100);
			Thread.sleep(1000);
			Taihan_Import_Page.uploadFileButton().click();
			Thread.sleep(3000);
			actions.moveToElement(Taihan_Import_Page.address_Bar()).click().build().perform();
			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
			int currentX = (int) mousePosition.getX();
			int currentY = (int) mousePosition.getY();
			robot.mouseMove(currentX + 70, currentY);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			actions.sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(Test_Data.taihan_Files_Path).build().perform();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			Taihan_Import_Page.attenuationFile().click();
			actions.sendKeys(Keys.ENTER).build().perform();
			Thread.sleep(1000);
			try {
				Taihan_Import_Page.fileBeingUsedPopup();
				System.out.println(
						"****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****");
				Taihan_Import_Page.warning_Popup_ok_Button().click();
				System.exit(0);
			} catch (Exception e) {

			}
			Taihan_Import_Page.submitButton().click();
			Thread.sleep(Duration.ofSeconds(20));
			boolean clickedOnOkButton = false;
			int attemptsCount = 0;
			while (clickedOnOkButton == false && attemptsCount < 400) {
				try {
					Swindon_Import_Page.warning_Popup_ok_Button().click();
					clickedOnOkButton = true;
				} catch (Exception e) {
					Thread.sleep(2000);
					attemptsCount++;
					if (attemptsCount == 400) {
						System.out.println("****Waited too long for missing fiber id popup****");
					}
				}
			}
			Thread.sleep(3000);
			System.out.println("Job Number: " + Job_Details_Page.job_Number().getText().trim());
			if (!Job_Details_Page.test_Results()
					.equalsIgnoreCase("Incomplete: " + Test_Data.taihan_Expected_Incomplete_Tests + ", Passed: "
							+ Test_Data.taihan_Expected_Passed_Tests + ", Failed: "
							+ Test_Data.taihan_Expected_Failed_Tests)) {
				System.out.println("****Taihan Import Job Status Count is not as expected****");
				System.out.println(
						"Expected Test Status was: " + "Incomplete: " + Test_Data.taihan_Expected_Incomplete_Tests
								+ ", Passed: " + Test_Data.taihan_Expected_Passed_Tests + ", Failed: "
								+ Test_Data.taihan_Expected_Failed_Tests);
				System.out.println("Current  Status found is: " + Job_Details_Page.test_Results());
			}

			if (!Job_Details_Page.helix_Factor().getText().trim()
					.equalsIgnoreCase(Test_Data.taihan_Expected_Helix_Factor)) {
				System.out.println("\n****Taihan Import Job Helix Factor is not as expected****");
				System.out.println("Expected Helix Factor was: " + Test_Data.taihan_Expected_Helix_Factor);
				System.out
						.println("Current  Helix Factor found is: " + Job_Details_Page.helix_Factor().getText().trim());
			}

			if (!Job_Details_Page.otdr_Length().getText().trim()
					.equalsIgnoreCase(Test_Data.taihan_Expected_OTDR_Length)) {
				System.out.println("\n****Taihan Import Job OTDR Length is not as expected****");
				System.out.println("Expected OTDR length was: " + Test_Data.taihan_Expected_OTDR_Length);
				System.out.println("Current  OTDR length found is: " + Job_Details_Page.otdr_Length().getText().trim());
			}
		} catch (Exception e) {
			System.out.println("****Exception in taihan_Import()****");
			e.printStackTrace();
		}
	}
}