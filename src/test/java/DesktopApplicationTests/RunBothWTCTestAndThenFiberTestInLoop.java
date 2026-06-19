package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;
import pageObjects.CommonPages.Dashboard;
import pageObjects.Modules.TestJobModule.JobDetailsPage;

public class RunBothWTCTestAndThenFiberTestInLoop extends BaseClass {

	@Test
	public static void runWtcTestInLoop() throws Exception {

		TestData.useOfficeOtdr = false;

		clearPreviousSessionData();

		launchWinAppDriver();

		launch_ECQTS_Application();

		loginToApplication();

		verifyBuildVersion();

		deleteAllExistingConnectionProfiles();

		createProfile(TestData.connectionProfileName_JGR_One, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Two, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Three, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Four, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);

		createProfile(TestData.connectionProfileName_JGR_Five, TestData.connectionProfile_Simulator_IP_Address,
				TestData.connectionProfile_Simulator_Port);

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

			TestData.useOfficeOtdr = true;

			launchOpenVpnAppAndConnect();

			createProfile(TestData.connectionProfileName_Office_OTDR, TestData.connectionProfile_Office_OTDR_IP_Address,
					TestData.connectionProfile_Office_OTDR_Port);

			updateTestSettings();

			updateApplicationSettings();

			searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.fiberTestJobSearchOrg,
					"45193192-2869194", TestData.getCurrentDateTimeStamp(), TestData.fiberTestJobSearchCutNumberInfo);
			if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
				Dashboard.okButton().click();
			}
//			takeDump("afterJobSearch", 1);
			runGetLengthTest(TestData.fiberTestModuleName);
			runFiberTestForAllFibersInJob(true, 200, 1);
		}
	}
}
