package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

		clearPreviousSessionData();

		launchWinAppDriver();

//		launchOpenVpnAppAndConnect();

		launch_ECQTS_Application();

//		verifyIncorrectCredentials();
//
//		validateRecoverPasswordButtonAvailability();
//
//		loginToApplication();
//
//		verifyBuildVersion();
//
//		verifyCopyResults();
//
//		verifyDownTime();
//
//		editDownTime();
//
//		importPrysmianJob();
//
//		importTaihanJob();
//
//		importSwindonJob();
//
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
//		searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.jobSearchOrg,
//				TestData.fiberTestJobSearchJobNumber, TestData.fiberTestJobSearchCutNumber,
//				TestData.fiberTestJobSearchCutNumberInfo);
//
//		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
//				TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
//				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
//
//		enterProtectionLayerValues();
//
//		runGetLengthTest(TestData.fiberTestModuleName);
//
//		editAdjLength();
//
//		verifyOpticsPage();
//
//		runFiberTest(TestData.numberOfFibersToTest);
//
//		download_1310_And_1550_SOR_Files();
//
//		enterCompletionLayerValues();
//
//		download_OCR_Report();
//
//		verifyTestResultsCount(TestData.fiberTestExpectedIncompleteTestsCount,
//				TestData.fiberTestExpectedPassedTestsCount, TestData.fiberTestExpectedFailedTestsCount,
//				"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);
//
//		verify_SOR_OCR_Files_Downloaded();
		
		searchJobAndNavigationToJobDetailsPage(TestData.tightBufferModuleName, TestData.jobSearchOrg,
				TestData.tightBufferJobNumber, TestData.tightBufferTestJobSearchCutNumber,
				TestData.tightBufferTestJobSearchCutNumberInfo);

		verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.tightBufferJobNumber,
				TestData.tightBufferTestJobSearchCutNumber, TestData.tightBufferTestJobSearchCutNumberInfo,
				"Tight Buffer test with Job # " + TestData.tightBufferJobNumber);

		softAssert.assertAll();

	}
}