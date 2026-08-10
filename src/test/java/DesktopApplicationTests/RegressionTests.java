package DesktopApplicationTests;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

		Exception exception = null;

		try {

//BeforeClass applicationSetupAndLaunch includes - clearPreviousSessionData, launchWinAppDriver, launchOpenVpnAppAndConnect, 
//launch_ECQTS_Application, verifyIncorrectCredentials, validateRecoverPasswordButtonAvailability, loginToApplication, verifyBuildVersion	

//			verifyCopyResults();
//
//			verifyDownTime();
//
//			editDownTime();
//
//			importPrysmianJob();
//
//			importTaihanJob();
//
//			importSwindonJob();
//
//			deleteAllExistingConnectionProfiles();
//
//			createConnectionProfiles();
//
//			editConnectionProfile();
//			
//			updateApplicationSettings();

			verifyFiberTestModule();

			verifyTightBufferModule();

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
//
//			download_OCR_Report();
//
//			verifyTestResultsCount(TestData.PK_FiberTestExpectedIncompleteTestsCount,
//					TestData.PK_FiberTestExpectedPassedTestsCount, TestData.PK_FiberTestExpectedFailedTestsCount,
//					"PK Fiber Test with Job # " + TestData.PK_FiberTestJobSearchJobNumber);
//			
//			verify_SOR_OCR_Files_Downloaded();

			verify_Reel_Id_And_Sales_Order_In_Job_Search_popup();
			
			verify_Reel_Id_And_Sales_Order_In_Completion_Tab();

//			logOutAndCloseApplication();

			verifyAnomalyStatus();

		} catch (Exception e) {
			exception = e;
		} finally {
			try {
				actions.keyDown(Keys.ALT).sendKeys(Keys.TAB).sendKeys(Keys.TAB).keyUp(Keys.ALT).build().perform();
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