package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {
		clearPreviousSessionData();
		launchWinAppDriver();
		launchOpenVpnAppAndConnect();
		launch_ECQTS_Application();
//		verifyIncorrectCredentials();
		loginToApplication();
//		verifyBuildVersion();
//		deleteAllExistingConnectionProfiles();
//		createConnectionProfiles();
//		editConnectionProfile();
		updateTestSettings();
		updateApplicationSettings();
//		importPrysmianJob();
//		importSwindonJob();
//		importTaihanJob();
		searchJobAndNavigationToJobDetailsPage(TestData.jobSearchJobNumber, TestData.jobSearchCutNumber,
				TestData.jobSearchCutNumberInfo);
		runGetLengthTest();
		runTestInLoop();
		softAssert.assertAll();
	}
}