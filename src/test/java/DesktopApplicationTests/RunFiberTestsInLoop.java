package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;
import pageObjects.CommonPages.Dashboard;
import pageObjects.Modules.TestJobModule.JobDetailsPage;

public class RunFiberTestsInLoop extends BaseClass {

	@Test
	public static void runFiberTestInLoop() throws Exception {

		clearPreviousSessionData();

		launchOpenVpnAppAndConnect();

		launchWinAppDriver();

		launch_ECQTS_Application();

		loginToApplication();

		deleteAllExistingConnectionProfiles();

		if (TestData.useOfficeOtdr) {
			createProfile(TestData.connectionProfileName_Office_OTDR, TestData.connectionProfile_Office_OTDR_IP_Address, TestData.connectionProfile_Office_OTDR_Port);
		} else {
			createProfile(TestData.connectionProfileName_Simulator, TestData.connectionProfile_Simulator_IP_Address, TestData.connectionProfile_Simulator_Port);
		}

		updateTestSettings();

		updateApplicationSettings();

		while (true) {
			searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.fiberTestJobSearchOrg,
					"45193192-2869194", TestData.getCurrentDateTimeStamp(),
					TestData.fiberTestJobSearchCutNumberInfo);
			if (JobDetailsPage.isMissingFiberIdWarningPopupDisplayed()) {
				Dashboard.isOkButtonDisplayed();
				Dashboard.okButton().click();
			}
			takeDump("afterJobSearch", 1);
			runGetLengthTest(TestData.fiberTestModuleName);
			runFiberTestForAllFibersInJob(true, 20, 1);
		}
	}
}
