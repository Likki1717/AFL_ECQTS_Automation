package base;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
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
import org.testng.asserts.SoftAssert;

import io.appium.java_client.windows.WindowsDriver;
import pageObjects.CommonPages.Dashboard;
import pageObjects.CommonPages.SideMenu;
import pageObjects.CommonPages.SignIn;
import pageObjects.Modules.FiberTest.JobDetailsPage;
import pageObjects.Modules.FiberTest.JobSearch;
import pageObjects.Modules.FiberTest.JobDetails.Completion;
import pageObjects.Modules.FiberTest.JobDetails.FiberResults;
import pageObjects.Modules.FiberTest.JobDetails.OTDR_Settings;
import pageObjects.Modules.FiberTest.JobDetails.ProtectionLayer;
import pageObjects.Modules.FiberTest.JobDetails.Reports;
import pageObjects.Modules.ImportData.Import;
import pageObjects.Modules.ImportData.Prysmian;
import pageObjects.sideMenu.About;
import pageObjects.sideMenu.Settings;
import pageObjects.sideMenu.settings.ApplicationSettings;
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
				stopExecution();
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
				System.out.println("*****Close any open app / file and run the script again*****");
				stopExecution();
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
			robot.delay(3000);
			for (int i = 0; i < 4; i++) {
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
			}
			robot.delay(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(500);
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
			stopExecution();
		}
	}

	public static void copyPasteAndClickEnter(String textToCopy) {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);

		robot.keyRelease(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		robot.delay(200);

		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);

		robot.delay(200);
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
			Thread.sleep(3000);
			try {
				driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
			} catch (Exception e) {
				Thread.sleep(3000);
				System.out.println("****Trying to Relaunch App from Catch block****");
				driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
			}
			actions = new Actions(driver);
		} catch (Exception e) {
			System.out.println("****Exception in launch_ECQTS_Application()****");
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

	public static void loginToApplication() throws InterruptedException {
		try {
			SignIn.usernameField().clear();
			SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
			SignIn.passwordField().clear();
			SignIn.passwordField().sendKeys(TestData.ecqtsAppPassword());
			SignIn.signInButton().click();
			Dashboard.isLoaderNotDisplayed();
			softAssert.assertTrue(Dashboard.isFiberTestModuleDisplayed(),
					"Tried for 40 secs, Fiber Test module was not visible after login");
		} catch (Exception e) {
			System.out.println("****Exception in loginToApplication()****");
			stopExecution();
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
		try {
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
				ConnectionProfiles.findAddressTextBox().sendKeys("SIMULATO");
				ConnectionProfiles.switchModuleTextBox().clear();
				ConnectionProfiles.switchModuleTextBox().sendKeys("1");
				ConnectionProfiles.testSwitchConnectionButton().click();
				softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
						"Tried for 15 seconds, Connection failure popup is not displayed");
				ConnectionProfiles.okButton().click();
				ConnectionProfiles.findAddressTextBox().clear();
				ConnectionProfiles.findAddressTextBox().sendKeys("SIMULATOR");
				ConnectionProfiles.testSwitchConnectionButton().click();
				softAssert.assertTrue(ConnectionProfiles.isConnectionSuccessfulPopupDisplayed(),
						"Tried for 10 seconds, WTC Connection successful popup is not displayed");
				ConnectionProfiles.okButton().click();
				ConnectionProfiles.workerNumberTextBox().sendKeys(connectionProfileName.split("-")[1]);
				ConnectionProfiles.spoonTextBox().sendKeys(connectionProfileName.split("-")[1]);
			}
			ConnectionProfiles.saveProfileButton().click();
			if ((connectionProfileName.equals("Office OTDR") && TestData.useOfficeOtdr)
					|| !connectionProfileName.equals("Office OTDR")) {
				validateSuccessfulConnectionPopup();
			} else {
				validateFailureConnectionPopup();
			}
		} catch (Exception e) {
			System.out.println("****Exception in createProfile() - " + connectionProfileName + "****");
		}
	}

	public static void createConnectionProfiles() {
		createProfile(TestData.jgrOneProfileName, TestData.simulatorIP_Address, TestData.simulatorPort);
		createProfile(TestData.jgrTwoProfileName, TestData.simulatorIP_Address, TestData.simulatorPort);
		createProfile(TestData.officeOtdrProfileName, TestData.officeOTDR_IP_Address, TestData.officeOTDR_Port);
		createProfile(TestData.simulatorProfileName, TestData.simulatorIP_Address, TestData.simulatorPort);
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
		}
	}

	public static void validateFailureConnectionPopup() {
		try {
			wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.elementToBeClickable(ConnectionProfiles.testConnection()));
			ConnectionProfiles.testConnection().click();
			softAssert.assertTrue(ConnectionProfiles.isConnectionFailurePopupDisplayed(),
					"Tried for 15 seconds, Connection failed popup is not displayed");
			ConnectionProfiles.okButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in validateFailureConnectionPopup()****");
		}
	}

	public static void editConnectionProfile() {
		try {
			ConnectionProfiles.editButton().click();
			ConnectionProfiles.instrumentTypeDropdown().click();
			ConnectionProfiles.instrumentType_Anritsu_MT_9085().click();
			softAssert.assertTrue(
					ConnectionProfiles.ipAddressTextBox().getAttribute("Value.Value")
							.contains(TestData.anritsu_9085_Ip_Address),
					"Ip address did not change when we changed instrument from Simulator to Anritsu 9085");
			ConnectionProfiles.ipAddressTextBox().clear();
			ConnectionProfiles.ipAddressTextBox().sendKeys(TestData.anritsu_9085_Ip_Address);
			ConnectionProfiles.saveProfileButton().click();
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
			ConnectionProfiles.saveProfileButton().click();
		} catch (Exception e) {
			System.out.println("****Exception in editConnectionProfile()****");
		}
	}

	public static void updateTestSettings() {
		try {
			if (!Settings.isTestSettingsButtonDisplayed()) {
				Dashboard.openNavigationButton().click();
				SideMenu.settingsButton().click();
			}
			Settings.isTestSettingsButtonDisplayed();
			Settings.testSettingsButton().click();
			softAssert.assertTrue(TestSettings.isDisplayRealTimePlotToogleDisplayed(),
					"Waited for 3 secs, Display real time plot settings toggle is not displayed ");
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
					.contains("Integrated Camera")) {
				ApplicationSettings.cameraSourceDropDown().click();
				if (TestData.useExternalCamera) {
					if (ApplicationSettings.isExternalCameraDisplayed()) {
						ApplicationSettings.externalCamera().click();
					} else {
						System.out.println("****External camera is not available to select****");
					}
				} else {
					ApplicationSettings.integratedCamera().click();
				}
			}
		} catch (Exception e) {
			System.out.println("****Exception in updateApplicationSettings()****");
		}
	}

	public static void importJob() {

	}

	public static void importPrysmianJob() {
		try {
			while (!Dashboard.isImportDataModuleDisplayed()) {
				Dashboard.openNavigationButton().click();
				SideMenu.dashboardButton().click();
				Thread.sleep(1000);
			}
			Dashboard.importDataModule().click();
			softAssert.assertTrue(Import.isPrysmianTypeDisplayed(),
					"Waited for 5 seconds, Prysmian option on the Import side menu is not displayed");
			Thread.sleep(500);
			Import.prysmianType().click();
			softAssert.assertTrue(Dashboard.isLoaderDisplayed(),
					"Waited for 5 seconds, Screen loader did not display");
			softAssert.assertTrue(Dashboard.isLoaderNotDisplayed(),
					"Waited for 10 seconds but still the screen loader is displayed");
			Thread.sleep(500);
			Prysmian.orgDropDownField().sendKeys(TestData.prysmianOrg);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Prysmian.cutNumber().sendKeys(TestData.prysmianCutNumber);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(500);
			Prysmian.itemOrgCode().sendKeys(TestData.prysmianItemOrgCode);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(1000);
			Prysmian.cutNumberInfo().sendKeys(TestData.prysmiancutNumberInfo);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(1000);
			Prysmian.importType().sendKeys(TestData.prysmianImportType);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Prysmian.uploadFileButton().click();
			softAssert.assertTrue(Prysmian.isFileNameTextBoxDisplayed(),
					"Waited for 10 seconds, file name text box was not visible");

			// Multi-file string
			String filesToUpload = "\"" + TestData.prysmianAttenuationFilePath + "\" \""
					+ TestData.prysmianJacketOdFilePath + "\"";

			// Send file paths
			Prysmian.fileNameTextBox().click();
			Prysmian.fileNameTextBox().sendKeys(filesToUpload);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			if (!Prysmian.cutNumberInfo().getText().equals(TestData.prysmiancutNumberInfo)) {
				System.out.println(
						"****Cut number info did not get selected as given in test data, hence stopping execution****");
				stopExecution();
			}

			Prysmian.submitButton().click();
			Dashboard.isLoaderDisplayed();
			if (Dashboard.isLoaderNotDisplayed()) {
				Thread.sleep(2000);
				if (Prysmian.isImportSuccessfulPopupDisplayed()) {
					String prysmianJobNumber = Prysmian.getJobNumberFromImportSuccessFulPopup();
					Prysmian.okButton().click();
					searchJobAndNavigationToJobDetailsPage(TestData.prysmianOrg, prysmianJobNumber,
							TestData.prysmianCutNumber, TestData.prysmiancutNumberInfo);
				} else if (Import.isPrysmianTypeDisplayed()) {
					System.out
							.println("****Import Failed - loader is not displayed but still on the Prysmian page****");
					stopExecution();
				} else if (Import.isWarningsErrorsPopupDisplayedOtherThanMissingFiberId()) {
					System.out
							.println("****Import Failed - found warning/errors popup, other than missing fiber id****");
					stopExecution();
				} else if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
					JobDetailsPage.okButton().click();
				}
			} else {
				System.out.println("****Waited more than 10 mins but loader is still displayed while importing****");
				stopExecution();
			}

			// Waiting for protection layer tab to confirm data is loaded

			softAssert.assertTrue(JobDetailsPage.isProtectionLayerTabDisplayed(),
					"Tried for 5 seconds, Protection Layer tab is not displayed");

			System.out.println("Prysmian Import Job Number: " + JobDetailsPage.jobNumber().getText().trim());

			// Validating OTDR length is not 0 and its as expected

			softAssert.assertEquals(JobDetailsPage.OTDR_Length().getText(), TestData.prysmianExpectedOtdrLength,
					"OTDR Length mismatch. Expected: " + TestData.prysmianExpectedOtdrLength + " but found: "
							+ JobDetailsPage.OTDR_Length().getText());

			// Validating test results count

			verifyTestsCount(TestData.prysmianExpectedIncompleteTests, TestData.prysmianExpectedPassedTests,
					TestData.prysmianExpectedFailedTests);

			// Validating Helix Factor
			softAssert.assertEquals(JobDetailsPage.helixFactor().getText().trim(), TestData.prysmianExpectedHelixFactor,
					"Prysmian Import Job Helix Factor is not as expected, \n Expected Helix Factor was: "
							+ TestData.prysmianExpectedHelixFactor + " But found: "
							+ JobDetailsPage.helixFactor().getText().trim());

			verifyJobDetailsHeader(TestData.prysmianOrg, TestData.prysmianJobNumberStartsWith,
					TestData.prysmianCutNumber, TestData.prysmiancutNumberInfo);

		} catch (Exception e) {
			System.out.println("****Exception in importPrysmianJob()****");
		}
	}

//	public static void importSwindonJob() {
//		try {
//			Side_Menu.dashboardButton().click();
//			Thread.sleep(1000);
//			Dashboard_Page.importDataModule().click();
//			Import_Page.swindonType().click();
//			Swindon_Import_Page.orgDropDownField().sendKeys(TestData.swindon_Org);
//			Swindon_Import_Page.cutNumber().click();
//			Swindon_Import_Page.cutNumber().sendKeys(TestData.swindon_Cut_Number());
//			Swindon_Import_Page.itemOrgCode().sendKeys(TestData.swindon_Item_Org_Code);
//			Swindon_Import_Page.cutNumberInfo().sendKeys("ZTEST01");
//			Swindon_Import_Page.importType().sendKeys(TestData.swindon_Import_Type);
//			robot.mouseWheel(100);
//			Thread.sleep(1000);
//			Swindon_Import_Page.uploadFileButton().click();
//			Thread.sleep(3000);
//			actions.moveToElement(Swindon_Import_Page.address_Bar()).click().build().perform();
//			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
//			int currentX = (int) mousePosition.getX();
//			int currentY = (int) mousePosition.getY();
//			robot.mouseMove(currentX + 70, currentY);
//			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//			actions.sendKeys(Keys.BACK_SPACE).build().perform();
//			actions.sendKeys(TestData.swindon_Files_Path).build().perform();
//			actions.sendKeys(Keys.ENTER).build().perform();
//			Thread.sleep(1000);
//			Swindon_Import_Page.jacketOdFile().click();
//			actions.keyDown(Keys.CONTROL).build().perform();
//			Swindon_Import_Page.attenuationFile().click();
//			actions.keyUp(Keys.CONTROL).build().perform();
//			actions.sendKeys(Keys.ENTER).build().perform();
//			Thread.sleep(1000);
//			try {
//				Swindon_Import_Page.fileBeingUsedPopup();
//				System.out.println(
//						"****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED SWINDON FILE(S) AND RUN THE SCRIPT AGAIN****");
//				Swindon_Import_Page.warning_Popup_ok_Button().click();
//				stopExecution();
//			} catch (Exception e) {
//
//			}
//			Swindon_Import_Page.submitButton().click();
//			Thread.sleep(Duration.ofSeconds(40));
//			boolean clickedOnOkButton = false;
//			int attemptsCount = 0;
//			while (clickedOnOkButton == false && attemptsCount < 400) {
//				try {
//					Swindon_Import_Page.warning_Popup_ok_Button().click();
//					clickedOnOkButton = true;
//				} catch (Exception e) {
//					Thread.sleep(2000);
//					attemptsCount++;
//					if (attemptsCount == 400) {
//						System.out.println("****Waited too long for missing fiber id popup****");
//					}
//				}
//			}
//			Thread.sleep(3000);
//			System.out.println("Job Number: " + JobDetailsHeader.job_Number().getText().trim());
//			if (!JobDetailsHeader.test_Results()
//					.equalsIgnoreCase("Incomplete: " + TestData.swindon_Expected_Incomplete_Tests + ", Passed: "
//							+ TestData.swindon_Expected_Passed_Tests + ", Failed: "
//							+ TestData.swindon_Expected_Failed_Tests)) {
//				System.out.println("****Swindon Import Job Status Count is not as expected****");
//				System.out.println(
//						"Expected Test Status was: " + "Incomplete: " + TestData.swindon_Expected_Incomplete_Tests
//								+ ", Passed: " + TestData.swindon_Expected_Passed_Tests + ", Failed: "
//								+ TestData.swindon_Expected_Failed_Tests);
//				System.out.println("Current  Status found is: " + JobDetailsHeader.test_Results());
//			}
//
//			if (!JobDetailsHeader.helix_Factor().getText().trim()
//					.equalsIgnoreCase(TestData.swindon_Expected_Helix_Factor)) {
//				System.out.println("\n****Swindon Import Job Helix Factor is not as expected****");
//				System.out.println("Expected Helix Factor was: " + TestData.swindon_Expected_Helix_Factor);
//				System.out
//						.println("Current  Helix Factor found is: " + JobDetailsHeader.helix_Factor().getText().trim());
//			}
//		} catch (Exception e) {
//			System.out.println("****Exception in importSwindonJob()****");
//		}
//	}

//	public static void importTaihanJob() {
//		try {
//			Side_Menu.dashboardButton().click();
//			Thread.sleep(1000);
//			Dashboard_Page.importDataModule().click();
//			Import_Page.taihanType().click();
//			Taihan_Import_Page.orgDropDownField().sendKeys(TestData.taihan_Org);
//			Taihan_Import_Page.cutNumber().click();
//			Taihan_Import_Page.cutNumber().sendKeys(TestData.taihan_Cut_Number());
//			Taihan_Import_Page.itemOrgCode().sendKeys(TestData.taihan_Item_Org_Code);
//			Taihan_Import_Page.cutNumberInfo().sendKeys("ZTEST01");
//			Taihan_Import_Page.importType().sendKeys(TestData.taihan_Import_Type);
//			robot.mouseWheel(100);
//			Thread.sleep(1000);
//			Taihan_Import_Page.uploadFileButton().click();
//			Thread.sleep(3000);
//			actions.moveToElement(Taihan_Import_Page.address_Bar()).click().build().perform();
//			Point mousePosition = MouseInfo.getPointerInfo().getLocation();
//			int currentX = (int) mousePosition.getX();
//			int currentY = (int) mousePosition.getY();
//			robot.mouseMove(currentX + 70, currentY);
//			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//			actions.sendKeys(Keys.BACK_SPACE).build().perform();
//			actions.sendKeys(TestData.taihan_Files_Path).build().perform();
//			actions.sendKeys(Keys.ENTER).build().perform();
//			Thread.sleep(1000);
//			Taihan_Import_Page.attenuationFile().click();
//			actions.sendKeys(Keys.ENTER).build().perform();
//			Thread.sleep(1000);
//			try {
//				Taihan_Import_Page.fileBeingUsedPopup();
//				System.out.println(
//						"****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****\n****CLOSE THE ALREADY OPENED TAIHAN FILE(S) AND RUN THE SCRIPT AGAIN****");
//				Taihan_Import_Page.warning_Popup_ok_Button().click();
//				stopExecution();
//			} catch (Exception e) {
//
//			}
//			Taihan_Import_Page.submitButton().click();
//			Thread.sleep(Duration.ofSeconds(20));
//			boolean clickedOnOkButton = false;
//			int attemptsCount = 0;
//			while (clickedOnOkButton == false && attemptsCount < 400) {
//				try {
//					Swindon_Import_Page.warning_Popup_ok_Button().click();
//					clickedOnOkButton = true;
//				} catch (Exception e) {
//					Thread.sleep(2000);
//					attemptsCount++;
//					if (attemptsCount == 400) {
//						System.out.println("****Waited too long for missing fiber id popup****");
//					}
//				}
//			}
//			Thread.sleep(3000);
//			System.out.println("Job Number: " + JobDetailsHeader.job_Number().getText().trim());
//			if (!JobDetailsHeader.test_Results()
//					.equalsIgnoreCase("Incomplete: " + TestData.taihan_Expected_Incomplete_Tests + ", Passed: "
//							+ TestData.taihan_Expected_Passed_Tests + ", Failed: "
//							+ TestData.taihan_Expected_Failed_Tests)) {
//				System.out.println("****Taihan Import Job Status Count is not as expected****");
//				System.out.println(
//						"Expected Test Status was: " + "Incomplete: " + TestData.taihan_Expected_Incomplete_Tests
//								+ ", Passed: " + TestData.taihan_Expected_Passed_Tests + ", Failed: "
//								+ TestData.taihan_Expected_Failed_Tests);
//				System.out.println("Current  Status found is: " + JobDetailsHeader.test_Results());
//			}
//
//			if (!JobDetailsHeader.helix_Factor().getText().trim()
//					.equalsIgnoreCase(TestData.taihan_Expected_Helix_Factor)) {
//				System.out.println("\n****Taihan Import Job Helix Factor is not as expected****");
//				System.out.println("Expected Helix Factor was: " + TestData.taihan_Expected_Helix_Factor);
//				System.out
//						.println("Current  Helix Factor found is: " + JobDetailsHeader.helix_Factor().getText().trim());
//			}
//
//			if (!JobDetailsHeader.otdr_Length().getText().trim()
//					.equalsIgnoreCase(TestData.taihan_Expected_OTDR_Length)) {
//				System.out.println("\n****Taihan Import Job OTDR Length is not as expected****");
//				System.out.println("Expected OTDR length was: " + TestData.taihan_Expected_OTDR_Length);
//				System.out.println("Current  OTDR length found is: " + JobDetailsHeader.otdr_Length().getText().trim());
//			}
//		} catch (Exception e) {
//			System.out.println("****Exception in importTaihanJob()****");
//		}
//	}

	public static void searchJobAndNavigationToJobDetailsPage(String org, String jobNumber, String cutNumber,
			String cutNumberInfo) throws InterruptedException {
		while (!Dashboard.isImportDataModuleDisplayed()) {
			Dashboard.openNavigationButton().click();
			SideMenu.dashboardButton().click();
			Thread.sleep(1000);
		}
		Dashboard.fiberTestModule().click();
		JobSearch.isJobNumberLabelDisplayed();
		JobSearch.orgField().click();
		JobSearch.orgField().sendKeys(org);
		JobSearch.jobNumber().sendKeys(jobNumber);
		Thread.sleep(500);
		actions.sendKeys(Keys.ENTER).perform();
		JobSearch.isJobWarningsPopupDisplayed();
		JobSearch.okButton().click();
		wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(JobSearch.searchCutNumber()));
		while (!JobSearch.searchCutNumber().equals(driver.switchTo().activeElement())) {
			JobSearch.searchCutNumber().click();
			Thread.sleep(1000);
		}
		softAssert.assertTrue(JobSearch.cutNumberHeaderDisplayed(),
				"Tried for 5 seconds, Cut Number Header in cut number table is not displayed ");
		softAssert.assertTrue(JobSearch.userHeaderDisplayed(),
				"Tried for 5 seconds, User Header in cut number table is not displayed ");
		softAssert.assertTrue(JobSearch.dateHeaderDisplayed(),
				"Tried for 5 seconds, Date Header in cut number table is not displayed ");
		softAssert.assertTrue(JobSearch.processHeaderDisplayed(),
				"Tried for 5 seconds, Process Header in cut number table is not displayed ");
		softAssert.assertTrue(JobSearch.listOfRowsInCutNumber().size() > 0, "No rows present in Cut number table");
		JobSearch.searchCutNumber().sendKeys(cutNumber);
		actions.sendKeys(Keys.ENTER).perform();
		JobSearch.searchCutNumberInfo().clear();
		softAssert.assertTrue(JobSearch.cutNumberInfoHeader(),
				"Tried for 5 seconds, Cut Number Info header  in cut number info table is not displayed ");
		softAssert.assertTrue(JobSearch.dateHeaderDisplayed(),
				"Tried for 5 seconds, Date Header in cut number info table is not displayed ");
		softAssert.assertTrue(JobSearch.listOfRowsInCutNumberInfo().size() > 0,
				"No rows present in Cut Number Info table");
		JobSearch.searchCutNumberInfo().sendKeys(cutNumberInfo);
		actions.sendKeys(Keys.ENTER).perform();
		JobSearch.goButton().click();
		softAssert.assertTrue(JobDetailsPage.isProtectionLayerTabDisplayed(),
				"Tried for 50 seconds, Protection Layer tab is not displayed");
	}

	public static void verifyJobDetailsHeader(String org, String jobNumber, String cutNumber, String cutNumberInfo) {
		softAssert.assertEquals(JobDetailsPage.org().getText().trim(), org,
				"Org Code is not as expected, \n Expected org code: " + org + " But found: "
						+ JobDetailsPage.org().getText().trim());

		softAssert.assertTrue(JobDetailsPage.jobNumber().getText().trim().contains(jobNumber),
				"Job number  is not as expected, \n Expected Job number: " + jobNumber + " But found: "
						+ JobDetailsPage.jobNumber().getText().trim());

		softAssert.assertEquals(JobDetailsPage.cutNumber().getText().trim(), cutNumber,
				"Cut number  is not as expected, \n Expected cut number: " + cutNumber + " But found: "
						+ JobDetailsPage.cutNumber().getText().trim());

		softAssert.assertEquals(JobDetailsPage.cutNumberInfo().getText().trim(), cutNumberInfo,
				"Cut number info is not as expected, \n Expected cut number info: " + cutNumberInfo + " But found: "
						+ JobDetailsPage.cutNumberInfo().getText().trim());
	}

	public static void verifyTestsCount(String expectedIncompleteTestsCount, String expectedPassedTestsCount,
			String expectedFailedTestsCount) {

		String expectedTestResultsCounts = "Incomplete: " + expectedIncompleteTestsCount + ", Passed: "
				+ expectedPassedTestsCount + ", Failed: " + expectedFailedTestsCount;

		softAssert.assertEquals(JobDetailsPage.getActualTestResultsCounts(), expectedTestResultsCounts,
				"Prysmian Import Job results count is not as expected, \n Expected: " + expectedTestResultsCounts
						+ " But found: " + JobDetailsPage.getActualTestResultsCounts());

	}

	public static void enterProtectionLayerValues() {
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
	}

	public static void runGetLengthTest() {
		wait = new WebDriverWait(driver, 20);
		JobDetailsPage.isOtdrSettingsTabDisplayed();
		wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.OTDR_Settings()));
		JobDetailsPage.OTDR_Settings().click();
		softAssert.assertTrue(OTDR_Settings.isConnectionProfileDropDownDisplayed(),
				"Waited for 50 seconds, connection profiles drop down on OTDR settings page is not displayed");
		OTDR_Settings.connectionProfile().sendKeys(TestData.OTDR_Settings_ConnectionProfile);
		OTDR_Settings.launchLength().clear();
		OTDR_Settings.launchLength().sendKeys(TestData.OTDR_Settings_LaunchLength);
		OTDR_Settings.cutLength().clear();
		OTDR_Settings.cutLength().sendKeys(TestData.OTDR_Settings_CutLength);
		OTDR_Settings.horizontal().clear();
		OTDR_Settings.horizontal().sendKeys(TestData.OTDR_Settings_Horizontal);
		OTDR_Settings.vertical().clear();
		OTDR_Settings.vertical().sendKeys(TestData.OTDR_Settings_Vertical);
		OTDR_Settings.getLengthButton().click();
		softAssert.assertTrue(OTDR_Settings.isOkButtonDisplayed(),
				"Waited for 50 seconds, Ok button on get length popup is not displayed");
		wait.until(ExpectedConditions.elementToBeClickable(OTDR_Settings.okButton()));
		OTDR_Settings.okButton().click();
		softAssert.assertTrue(OTDR_Settings.isGetLengthHistoryDropDownFieldDisplayed(),
				"Waited for 50 seconds, Get Length history drop down field is not displayed");
	}

	public static void runTestInLoop() throws InterruptedException {
		JobDetailsPage.isProtectionLayerTabDisplayed();
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.bufferTubeTab()));
		while (!FiberResults.isRunTestsButtonDisplayed()) {
			JobDetailsPage.bufferTubeTab().click();
		}
		FiberResults.showMoreInfoButton().click();
		wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
		FiberResults.runTestsButtonOfFirstFiber().click();
		while (true) {
			FiberResults.isOkButtonDisplayed();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.okButton()));
//			Thread.sleep(20000);
			FiberResults.okButton().click();
			FiberResults.isGoToFiberButtonVisible();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.goToFiberButton()));
			FiberResults.goToFiberButton().click();
			FiberResults.isContinueTestsButtonDisplayed();
			FiberResults.continueButton().click();
			if (FiberResults.isTestsCompleteDisplayed()) {
				FiberResults.okButton().click();
				if (!FiberResults.isRunTestsButtonDisplayed()) {
					actions.moveToElement(JobDetailsPage.bufferTubeTab()).build().perform();
					JobDetailsPage.bufferTubeTab().click();
				}
				FiberResults.isRunTestsButtonDisplayed();
				FiberResults.showMoreInfoButton().click();
				wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
				FiberResults.runTestsButtonOfFirstFiber().click();
			}
//			FiberResults.isGoToFiberButtonNotVisible();
//			Thread.sleep(1000);
//			Dashboard.openNavigationButton().click();
//			SideMenu.settingsButton().click();
//			Settings.isTestSettingsButtonDisplayed();
//			Thread.sleep(1000);
//			try {
//				Settings.testSettingsButton().click();
//				softAssert.assertTrue(TestSettings.isDisplayRealTimePlotToogleDisplayed(),
//						"Waited for 3 secs, Display real time plot settings toggle is not displayed ");
//			} catch (Exception e) {
//
//			}
//			Dashboard.backArrow().click();
			verifyTestsCount(TestData.fiberTestExpectedIncompleteTestsCount, TestData.fiberTestExpectedPassedTestsCount,
					TestData.fiberTestExpectedFailedTestsCount);
			break;
		}
	}

	public static void enterCompletionLayerValues() {

		JobDetailsPage.completionTabDisplayed();
		actions.moveToElement(JobDetailsPage.completionTab()).click().build().perform();
		softAssert.assertTrue(Completion.isSeqNumberTestDisplayed(),
				"Waited for 5 seconds, ISE Sequence test is not displayed ");
		Completion.ISE_Seq_Number().sendKeys("1000");
		Completion.ISE_Seq_Number_uoM().sendKeys("ft");

		Completion.OSE_Seq_Number().sendKeys("100");
		Completion.OSE_Seq_Number_uoM().sendKeys("ft");

		softAssert.assertTrue(Completion.isInvalidMeterMarksPopupDisplayed(),
				"Waited for 20 seconds, Invalid Meter Marks text is not visible");

		Completion.okButton().click();

		Completion.ISE_Print_Verified().sendKeys("2");
		Completion.OSE_Print_Verified().sendKeys("2");

		Completion.reelItem().sendKeys("REL00235");
		Completion.jacketColor().sendKeys("2");

		Completion.OSE_Print_Spacing().sendKeys("2");
		Completion.ISE_Print_Spacing().sendKeys("2");
	}

	public static void download_OCR_Report() {
		
		JobDetailsPage.reportsTab().click();
		Reports.isJobWarnings_ErrorsPopupDisplayed();
		Reports.okButton().click();
		Reports.isDownloadOCR_ReportDisplayed();
		Reports.opticalCharacteristics().click();
		softAssert.assertTrue(Reports.isGeneratingReportsInBackgroundTextDisplayed(),
				"Waited for 10 seconds, Generating Reports in background text is not displayed  ");
		Reports.okButton().click();
		Dashboard.isLoaderNotDisplayed();
	}

	public static void stopExecution() throws InterruptedException {
		
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
		System.exit(0);
	}
}