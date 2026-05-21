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
import pageObjects.sideMenu.About;
import pageObjects.sideMenu.Settings;
import pageObjects.sideMenu.settings.ApplicationSettings;
import pageObjects.sideMenu.settings.ConnectionProfiles;
import pageObjects.sideMenu.settings.TestSettings;

public class BaseClass {
	public static WindowsDriver<?> driver;
	public static WindowsDriver<?> desktopSession;
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
			new ProcessBuilder("taskkill", "/F", "/PID", TestData.appId()).start();
			Thread.sleep(3000);
			capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", TestData.appId());
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("automationName", "Windows");
			driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities);
		} catch (Exception e) {
			System.out.println("****Normal launch of ECQTS app failed, Trying Root attach****");
			while (true) {
				try {
					DesiredCapabilities capabilities2 = new DesiredCapabilities();
					capabilities2.setCapability("app", "Root");
					capabilities2.setCapability("platformName", "Windows");
					capabilities2.setCapability("deviceName", "WindowsPC");
					desktopSession = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities2);
					String windowHandle = desktopSession.findElementByName("Login").getAttribute("NativeWindowHandle");
					int handle = Integer.parseInt(windowHandle);
					String hexHandle = Integer.toHexString(handle);
					DesiredCapabilities capabilities3 = new DesiredCapabilities();
					capabilities3.setCapability("appTopLevelWindow", hexHandle);
					driver = new WindowsDriver<>(URI.create("http://127.0.0.1:4723").toURL(), capabilities3);
					break;
				} catch (Exception e1) {

				}

			}
		}
	}

	public static void verifyIncorrectCredentials() {
		softAssert.assertTrue(SignIn.isUsernameFieldDisplayed(),
				"Waited for 10 seconds, username field is not displayed");
		SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
		SignIn.passwordField().sendKeys("Invalid password");
		SignIn.signInButton().click();
		softAssert.assertTrue(SignIn.isloginFailureDisplayed(),
				"Tried for 5 secs, Login failed message was not visible");
		SignIn.okButtonOnLoginFailurePopup().click();
	}

	public static void loginToApplication() throws InterruptedException {
		try {
			softAssert.assertTrue(SignIn.isUsernameFieldDisplayed(),
					"Waited for 10 seconds, username field is not displayed");
			SignIn.usernameField().clear();
			SignIn.usernameField().sendKeys(TestData.ecqtsAppUsername());
			SignIn.passwordField().clear();
			SignIn.passwordField().sendKeys(TestData.ecqtsAppPassword());
			SignIn.signInButton().click();
			Dashboard.isLoaderNotDisplayed();
			softAssert.assertTrue(Dashboard.isFiberTestModuleDisplayed(),
					"Tried for 10 secs, Fiber Test module was not visible after login");
			actions = new Actions(driver);
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
			softAssert.assertEquals(appVersion, TestData.expectedAppVersion, "Build version mismatch.");
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

	public static void importJob(String importType, String importFilesToUpload, String OTDR_Length, String helixFactor,
			String JobNumberStartsWith) throws InterruptedException {
		while (!Dashboard.isImportDataModuleDisplayed()) {
			Dashboard.openNavigationButton().click();
			SideMenu.isDashboardButtonDisplayed();
			SideMenu.dashboardButton().click();
			Thread.sleep(1000);
		}
		Dashboard.importDataModule().click();

		softAssert.assertTrue(Import.isSelectImportTextDisplayed(),
				"Waited for 5 seconds, Select an import type from the left panel is not displayed");

		Thread.sleep(1000);

		if (importType.equalsIgnoreCase("Prysmian")) {
			Import.prysmianType().click();

		} else if (importType.equalsIgnoreCase("Swindon")) {
			Import.swindonType().click();

		} else if (importType.equalsIgnoreCase("Taihan")) {
			Import.taihanType().click();
		}

		softAssert.assertTrue(Dashboard.isLoaderDisplayed(),
				importType + " Import - Waited for 5 seconds, Screen loader did not display");
		softAssert.assertTrue(Dashboard.isLoaderNotDisplayed(),
				importType + " Import - Waited for 20 seconds but still the screen loader is displayed");
		Thread.sleep(500);
		Import.orgDropDownField().sendKeys(TestData.importOrg);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Import.cutNumber().sendKeys(TestData.importCutNumber);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		Import.itemOrgCode().sendKeys(TestData.importItemOrgCode);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		Import.cutNumberInfo().sendKeys(TestData.importcutNumberInfo);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(1000);
		Import.importType().sendKeys(TestData.importType);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Import.uploadFileButton().click();
		softAssert.assertTrue(Import.isFileNameTextBoxDisplayed(), importType
				+ " Import - Waited for 10 seconds, to enter file name, the file name text box was not visible");

		// Send file paths
		Import.fileNameTextBox().click();
		Import.fileNameTextBox().sendKeys(importFilesToUpload);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		if (!Import.cutNumberInfo().getText().equals(TestData.importcutNumberInfo)) {
			System.out.println("****" + importType
					+ " Import - Cut number info did not get selected as given in test data, hence stopping execution****");
			stopExecution();
		}

		Thread.sleep(1000);
		Import.submitButton().click();
		Dashboard.isLoaderDisplayed();

		boolean eitherImportCompletedOrGotError = false;

		while (!eitherImportCompletedOrGotError) {

			if (Dashboard.isLoaderNotDisplayed() || Import.isImportSuccessfulPopupDisplayed()
					|| Import.isWarningsErrorsPopupDisplayedOtherThanMissingFiberId()) {
				eitherImportCompletedOrGotError = true;
				Thread.sleep(2000);
				if (Import.isImportSuccessfulPopupDisplayed()) {
					String importJobNumber = Import.getJobNumberFromImportSuccessFulPopup();
					System.out.println(
							importType + " Import Job number fetched from Import Complete popup : " + importJobNumber);
					Import.okButton().click();
					searchJobAndNavigationToJobDetailsPage(TestData.importOrg, importJobNumber,
							TestData.importCutNumber, TestData.importcutNumberInfo);
				} else if (Import.isWarningsErrorsPopupDisplayedOtherThanMissingFiberId()) {
					System.out.println("****" + importType
							+ " Import Failed - found warning/errors popup, other than missing fiber id****");
					stopExecution();
				} else if (Import.isPrysmianTypeDisplayed()) {
					System.out.println("****" + importType
							+ " Import Failed - loader is not displayed but still on the import page****");
					stopExecution();
				}
			}
		}

		if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
			JobDetailsPage.okButton().click();
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
				TestData.importcutNumberInfo, importType);
	}

	public static void importPrysmianJob() {
		try {
			// Multi-file string
			String prysmianFilePath = "\"" + TestData.prysmianAttenuationFilePath + "\" \""
					+ TestData.prysmianJacketOdFilePath + "\"";

			importJob("prysmian", prysmianFilePath, TestData.prysmianExpectedOtdrLength,
					TestData.prysmianExpectedHelixFactor, TestData.prysmianJobNumberStartsWith);

			// Validating test results count
			verifyTestResultsCount(TestData.prysmianExpectedIncompleteTests, TestData.prysmianExpectedPassedTests,
					TestData.prysmianExpectedFailedTests, "Prysmian Import");
		} catch (Exception e) {
			System.out.println("****Exception in importPrysmianJob()****");
		}
	}

	public static void importSwindonJob() {

		try {
			// Multi-file string
			String swindonFilePath = "\"" + TestData.swindonAttenuationFilePath + "\" \""
					+ TestData.swindonJacketOdFilePath + "\"";

			importJob("swindon", swindonFilePath, TestData.swindonExpectedOtdrLength,
					TestData.swindonExpectedHelixFactor, TestData.swindonJobNumberStartsWith);

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

			importJob("taihan", taihanFilePath, TestData.taihanExpectedOtdrLength, TestData.taihanExpectedHelixFactor,
					TestData.taihanJobNumberStartsWith);

			verifyTestResultsCount(TestData.taihanExpectedIncompleteTests, TestData.taihanExpectedPassedTests,
					TestData.swindonExpectedFailedTests, "Taihan Import");

		} catch (Exception e) {
			System.out.println("****Exception in importTaihanJob()****");
		}

	}

	public static void searchJobAndNavigationToJobDetailsPage(String org, String jobNumber, String cutNumber,
			String cutNumberInfo) throws InterruptedException {
		while (!Dashboard.isImportDataModuleDisplayed()) {
			Dashboard.openNavigationButton().click();
			SideMenu.isDashboardButtonDisplayed();
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
		Dashboard.isLoaderDisplayed();
		Dashboard.isLoaderNotDisplayed();
		softAssert.assertTrue(JobDetailsPage.isOtdrSettingsTabDisplayed(),
				"Tried for 50 seconds, Protection Layer tab is not displayed");
	}

	public static void verifyJobDetailsHeader(String org, String jobNumber, String cutNumber, String cutNumberInfo,
			String whichTestBeingPerformed) {

		softAssert.assertEquals(JobDetailsPage.org().getText().trim(), org,
				"Org is not as expected in " + whichTestBeingPerformed);

		softAssert.assertEquals(JobDetailsPage.jobNumber().getText().trim().split("-")[0], jobNumber,
				"Job number is not as expected in " + whichTestBeingPerformed);

		softAssert.assertEquals(JobDetailsPage.cutNumber().getText().trim(), cutNumber,
				"Cut number is not as expected in " + whichTestBeingPerformed);

		softAssert.assertEquals(JobDetailsPage.cutNumberInfo().getText().trim(), cutNumberInfo,
				"Cut number info is not as expected in " + whichTestBeingPerformed);
	}

	public static void verifyTestResultsCount(String expectedIncompleteTestsCount, String expectedPassedTestsCount,
			String expectedFailedTestsCount, String whichTestBeingPerformed) {

		String expectedTestResultsCounts = "Incomplete: " + expectedIncompleteTestsCount + ", Passed: "
				+ expectedPassedTestsCount + ", Failed: " + expectedFailedTestsCount;

		String actualTestResultsCount = JobDetailsPage.getActualTestResultsCounts();

		softAssert.assertEquals(actualTestResultsCount, expectedTestResultsCounts,
				"Test results count mismatch in " + whichTestBeingPerformed);
	}

	public static void enterProtectionLayerValues() {
		try {
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
		Dashboard.isLoaderNotDisplayed();
		softAssert.assertTrue(OTDR_Settings.isGetLengthHistoryDropDownFieldDisplayed(),
				"Waited for 50 seconds, Get Length history drop down field is not displayed");
	}

	public static void runFiberTest(int numberOfFibersToTest) throws InterruptedException {
		wait = new WebDriverWait(driver, 30);
		int fibersTested = 0;
		boolean startTestFromFirstBufferTube = true;
		boolean startTestingInNewBufferTube = true;
		while (numberOfFibersToTest != fibersTested) {
// Below if block is needed only to click on the first buffer tube, which is at start of test 
// OR when all tests are completed and still number of fibers to test are less than overall fibers tested
			if (startTestFromFirstBufferTube) {
				JobDetailsPage.isBufferTubeDisplayed();
				wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.bufferTubeTab()));
				while (!FiberResults.isRunTestsButtonDisplayed()) {
					JobDetailsPage.bufferTubeTab().click();
				}
				startTestFromFirstBufferTube = false;
			}
// Below if blocked is needed to click on Show more info button and to click on Run Test button of first fiber,
// which is at start of test OR when moved to new buffer tube			
			if (startTestingInNewBufferTube) {
				FiberResults.isRunTestsButtonDisplayed();
				FiberResults.showMoreInfoButton().click();
				wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
				FiberResults.runTestsButtonOfFirstFiber().click();
				startTestingInNewBufferTube = false;
			}
			Dashboard.isLoaderDisplayed();
			Dashboard.isLoaderNotDisplayed();
			FiberResults.isOkButtonDisplayed();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.okButton()));
			Thread.sleep(1000);
			FiberResults.okButton().click();
			fibersTested++;
			FiberResults.isGoToFiberButtonVisible();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.goToFiberButton()));
			FiberResults.goToFiberButton().click();
			FiberResults.isContinueTestsButtonDisplayed();
			if (numberOfFibersToTest == fibersTested) {
				FiberResults.stopButton().click();
			} else {
				FiberResults.continueButton().click();
				if (FiberResults.isTestsCompletedTextDisplayed()) {
					FiberResults.okButton().click();
					startTestingInNewBufferTube = true;
					if (!FiberResults.isRunTestsButtonDisplayed()) {
						startTestFromFirstBufferTube = true;
					}
				}
			}
		}
	}

	public static void runTestInLoopAlongWithSwitchingToSettingsPage() throws InterruptedException {
		JobDetailsPage.isProtectionLayerTabDisplayed();
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.bufferTubeTab()));
		while (!FiberResults.isRunTestsButtonDisplayed()) {
			JobDetailsPage.bufferTubeTab().click();
		}
		FiberResults.showMoreInfoButton().click();

		while (true) {
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
			FiberResults.runTestsButtonOfFirstFiber().click();
			FiberResults.isOkButtonDisplayed();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.okButton()));
			Thread.sleep(5000);
			FiberResults.okButton().click();
			FiberResults.isGoToFiberButtonVisible();
			wait.until(ExpectedConditions.elementToBeClickable(FiberResults.goToFiberButton()));
			FiberResults.goToFiberButton().click();
			FiberResults.isContinueTestsButtonDisplayed();
//			FiberResults.continueButton().click();
//			if (FiberResults.isTestsCompletedTextDisplayed()) {
//				FiberResults.okButton().click();
//				if (!FiberResults.isRunTestsButtonDisplayed()) {
//					actions.moveToElement(JobDetailsPage.bufferTubeTab()).build().perform();
//					JobDetailsPage.bufferTubeTab().click();
//				}
//				FiberResults.isRunTestsButtonDisplayed();
//				FiberResults.showMoreInfoButton().click();
//				wait.until(ExpectedConditions.elementToBeClickable(FiberResults.runTestsButtonOfFirstFiber()));
//				FiberResults.runTestsButtonOfFirstFiber().click();
//			}
//			FiberResults.isGoToFiberButtonNotVisible();
			FiberResults.stopButton().click();
			Thread.sleep(1000);
			Dashboard.openNavigationButton().click();
			SideMenu.settingsButton().click();
			Settings.isTestSettingsButtonDisplayed();
			Thread.sleep(1000);
			try {
				Settings.testSettingsButton().click();
				softAssert.assertTrue(TestSettings.isDisplayRealTimePlotToogleDisplayed(),
						"Waited for 3 secs, Display real time plot settings toggle is not displayed ");
			} catch (Exception e) {

			}
			Dashboard.backArrow().click();
//			verifyTestsCount(TestData.fiberTestExpectedIncompleteTestsCount, TestData.fiberTestExpectedPassedTestsCount,
//					TestData.fiberTestExpectedFailedTestsCount);
//			break;
		}
	}

	public static void enterCompletionLayerValues() {

		try {
			softAssert.assertTrue(JobDetailsPage.isCompletionTabDisplayed(),
					"Waited for 10 seconds, completion tab is not displayed");
			wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(JobDetailsPage.completionTab()));
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
		} catch (Exception e) {
			System.out.println(
					"****Could not enter all the values in Completion tab, possibly this job does not have all fields****");
		}
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
		Dashboard.isLoaderDisplayed();
		Dashboard.isLoaderNotDisplayed();
		Dashboard.isLoaderDisplayed();
		Dashboard.isLoaderNotDisplayed();
		JobDetailsPage.reportsTab().click();
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