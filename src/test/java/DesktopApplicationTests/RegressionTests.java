package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

		Exception exception = null;

		try {
			clearPreviousSessionData();

			launchWinAppDriver();

			launchOpenVpnAppAndConnect();

			launch_ECQTS_Application();

			verifyIncorrectCredentials();

			validateRecoverPasswordButtonAvailability();

			loginToApplication();

			verifyBuildVersion();

			verifyCopyResults();

			verifyDownTime();

			editDownTime();

			importPrysmianJob();

			importTaihanJob();

			importSwindonJob();

			deleteAllExistingConnectionProfiles();

			createConnectionProfiles();

			editConnectionProfile();

			updateTestSettings();

			updateApplicationSettings();

			searchJobAndNavigationToJobDetailsPage(TestData.fiberTestModuleName, TestData.jobSearchOrg,
					TestData.fiberTestJobSearchJobNumber, TestData.fiberTestJobSearchCutNumber,
					TestData.fiberTestJobSearchCutNumberInfo);

			verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber,
					TestData.fiberTestJobSearchCutNumber, TestData.fiberTestJobSearchCutNumberInfo,
					"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber, TestData.fiberTestExpectedItemNumber);

			enterProtectionLayerValues(TestData.fiberTestModuleName);

			runGetLengthTest(TestData.fiberTestModuleName);

			editAdjLength();

			verifyOpticsPage();

			runFiberTest(TestData.fiberTestModuleName, TestData.numberOfFibersToTest);

			downloadSorFiles();

			enterCompletionLayerValues(TestData.fiberTestModuleName);

			download_OCR_Report();

			verifyTestResultsCount(TestData.fiberTestExpectedIncompleteTestsCount,
					TestData.fiberTestExpectedPassedTestsCount, TestData.fiberTestExpectedFailedTestsCount,
					"Fiber test with Job # " + TestData.fiberTestJobSearchJobNumber);

			TestData.useOfficeOtdr = false;

			updateTestSettings();

			searchJobAndNavigationToJobDetailsPage(TestData.tightBufferModuleName, TestData.jobSearchOrg,
					TestData.tightBufferJobNumber, TestData.tightBufferTestJobSearchCutNumber,
					TestData.tightBufferTestJobSearchCutNumberInfo);

			verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.tightBufferJobNumber,
					TestData.tightBufferTestJobSearchCutNumber, TestData.tightBufferTestJobSearchCutNumberInfo,
					"Tight Buffer test with Job # " + TestData.tightBufferJobNumber, TestData.tightBufferExpectedItemNumber);

			enterProtectionLayerValues(TestData.tightBufferModuleName);

			runGetLengthTest(TestData.tightBufferModuleName);

			verifyOpticsPage();

			runFiberTest(TestData.tightBufferModuleName, 1);

			downloadSorFiles();

			enterCompletionLayerValues(TestData.tightBufferModuleName);

			download_OCR_Report();

			verifyTestResultsCount(TestData.tightBufferExpectedIncompleteTestsCount,
					TestData.tightBufferExpectedPassedTestsCount, TestData.tightBufferExpectedFailedTestsCount,
					"Tight Buffer with Job # " + TestData.tightBufferJobNumber);

//			searchJobAndNavigationToJobDetailsPage(TestData.PK_FiberTestModuleName, TestData.jobSearchOrg,
//					TestData.PK_FiberTestJobSearchJobNumber, TestData.PK_FiberTestJobSearchCutNumber,
//					TestData.PK_FiberTestJobSearchCutNumberInfo);
// 
//			verifyJobDetailsHeader(TestData.jobSearchOrg, TestData.PK_FiberTestJobSearchJobNumber,
//					TestData.PK_FiberTestJobSearchCutNumber, TestData.PK_FiberTestJobSearchCutNumberInfo,
//					"PK Fiber test with Job # " + TestData.PK_FiberTestJobSearchJobNumber , TestData.PK_FiberTestExpectedItemNumber);
//			
//			enterProtectionLayerValues(TestData.PK_FiberTestModuleName);
//			
//			enterCompletionLayerValues(TestData.PK_FiberTestModuleName);
			
//			download_OCR_Report();
			
//			verifyTestResultsCount(TestData.PK_FiberTestExpectedIncompleteTestsCount,
//					TestData.PK_FiberTestExpectedPassedTestsCount, TestData.PK_FiberTestExpectedFailedTestsCount,
//					"PK Fiber Test with Job # " + TestData.PK_FiberTestJobSearchJobNumber);
//			
//			verify_SOR_OCR_Files_Downloaded();
			
			verify_Reel_Id_In_Job_Search_popup();
			
			verify_Reel_Id_In_Completion_Tab();

		} catch (Exception e) {
			exception = e;
		} finally {
			try {
				softAssert.assertAll();
			} catch (AssertionError e) {
				e.printStackTrace();
			}
		}

		if (exception != null) {
			throw exception;
		}
	}
}