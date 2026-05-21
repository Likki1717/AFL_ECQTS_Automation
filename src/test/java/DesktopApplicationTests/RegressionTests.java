package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

//		launchOpenVpnAppAndConnect();

		clearPreviousSessionData();

		launchWinAppDriver();
		
		launch_ECQTS_Application();
		
//		verifyIncorrectCredentials();
		
		loginToApplication();
		
//		verifyBuildVersion();
		
//		deleteAllExistingConnectionProfiles();
//		
//		createConnectionProfiles();
//		
//		editConnectionProfile();
//		
//		updateTestSettings();
//		
//		updateApplicationSettings();
//		
//		importPrysmianJob();
//		
//		importSwindonJob();
		
		importTaihanJob();
		
		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestJobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo);
		
		verifyJobDetailsHeader(TestData.fiberTestJobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
		
		enterProtectionLayerValues();
		
		runGetLengthTest();
		
		runFiberTest(TestData.numberOfFibersToTest);
		
		enterCompletionLayerValues();
		
		download_OCR_Report();
		
		verifyTestResultsCount(TestData.fiberTestExpectedIncompleteTestsCount,
				TestData.fiberTestExpectedPassedTestsCount, TestData.fiberTestExpectedFailedTestsCount,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
		
		softAssert.assertAll();
		
	}
}