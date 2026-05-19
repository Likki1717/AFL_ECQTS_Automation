package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {
		launchOpenVpnAppAndConnect();
		clearPreviousSessionData();
		launchWinAppDriver();
		launch_ECQTS_Application();
		verifyIncorrectCredentials();
		loginToApplication();
		verifyBuildVersion();
		deleteAllExistingConnectionProfiles();
		createConnectionProfiles();
		editConnectionProfile();
		updateTestSettings();
		updateApplicationSettings();
		importPrysmianJob();
//		importSwindonJob();
//		importTaihanJob();
		searchJobAndNavigationToJobDetailsPage(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo);
		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo);
		enterProtectionLayerValues();
		runGetLengthTest();
		enterCompletionLayerValues();
		download_OCR_Report();
//		verifyTestsCount(String expectedIncompleteTestsCount, String expectedPassedTestsCount,
//				String expectedFailedTestsCount);
		softAssert.assertAll();
	}
}