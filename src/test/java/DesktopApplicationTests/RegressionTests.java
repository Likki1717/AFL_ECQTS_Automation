package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

		clearPreviousSessionData();

		launchWinAppDriver();
		
		launch_ECQTS_Application();
		
		launchOpenVpnAppAndConnect();
		
		verifyIncorrectCredentials();
		
		loginToApplication();
		
		verifyBuildVersion();
		
		deleteAllExistingConnectionProfiles();
		
		createConnectionProfiles();
		
		editConnectionProfile();
		
		updateTestSettings();
		
		updateApplicationSettings();
		
		importPrysmianJob();
		
		importSwindonJob();
		
		importTaihanJob();
		
		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestJobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo);
		
		verifyJobDetailsHeader(TestData.fiberTestJobSearchOrg, TestData.fiberTestJobSearchJobNumber,
				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
		
		enterProtectionLayerValues();
		
		runGetLengthTest();
		
		editAdjLength();
		
		runFiberTest(TestData.numberOfFibersToTest);
		
		download_1310_And_1550_SOR_Files();
		
		enterCompletionLayerValues();
		
		download_OCR_Report();
		
		verifyTestResultsCount(TestData.fiberTestExpectedIncompleteTestsCount,
				TestData.fiberTestExpectedPassedTestsCount, TestData.fiberTestExpectedFailedTestsCount,
				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
		
		verify_SOR_OCR_Files_Downloaded();
		
		softAssert.assertAll();
		
	}
}