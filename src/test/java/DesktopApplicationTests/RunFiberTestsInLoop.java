package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;
import pageObjects.CommonPages.Dashboard;
import pageObjects.Modules.FiberTest.JobDetailsPage;

public class RunFiberTestsInLoop extends BaseClass {

	@Test
	public static void runFiberTestAndSwitchToSettingsAndRepeatInLoop() throws Exception {
//		clearPreviousSessionData();
		launchOpenVpnAppAndConnect();
		launchWinAppDriver();
		launch_ECQTS_Application();
//		loginToApplication();
//		deleteAllExistingConnectionProfiles();
//		createProfile(TestData.officeOtdrProfileName, TestData.officeOTDR_IP_Address, TestData.officeOTDR_Port);
//		updateTestSettings();
//		updateApplicationSettings();
		while (true) {
//			searchJobAndNavigationToJobDetailsPage(TestData.fiberTestJobSearchOrg, "45193192-2869194",
//					new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()),
//					TestData.fiberTestJobSearchCutNumberInfo);
//			if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
//				Dashboard.isOkButtonDisplayed();
//				Dashboard.okButton().click();
//			}
//			runGetLengthTest();
			runTestInLoopAlongWithSwitchingToSettingsPage();
		}
	}
}
