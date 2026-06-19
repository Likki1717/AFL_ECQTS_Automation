package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;
import pageObjects.CommonPages.Dashboard;
import pageObjects.Modules.TestJobModule.JobDetailsPage;

public class RunWTCTestsInLoop extends BaseClass {

	@Test
	public static void runWtcTestInLoop() throws Exception {
		
		TestData.useOfficeOtdr = false;

		clearPreviousSessionData();

		launchWinAppDriver();

		launch_ECQTS_Application();

		loginToApplication();
		
		verifyBuildVersion();

		deleteAllExistingConnectionProfiles();

		createProfile(TestData.connectionProfileName_JGR_One, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Two, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Three, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Four, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Five, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);

		updateTestSettings();

		updateApplicationSettings();

		while (true) {
			searchJobAndNavigationToJobDetailsPage(TestData.wtcTestModuleName, TestData.wtcTestJobSearchOrg,
					TestData.wtcTestJobSearchJobNumber, TestData.getCurrentDateTimeStamp(),
					TestData.wtcTestJobSearchCutNumberInfo);
			if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
				Dashboard.okButton().click();
			}
//			takeDump("afterJobSearch", 1);
			runGetLengthTest(TestData.wtcTestModuleName);
			runWtcTestForAllRibbonsInJob(200, 1);
		}
	}
}
