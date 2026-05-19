package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;
import base.TestData;

public class RunFiberTestsInLoop extends BaseClass{

	@Test
	public static void runFiberTestAndSwitchToSettingsAndRepeatInLoop() throws Exception {
		clearPreviousSessionData();
		launchOpenVpnAppAndConnect();
		launchWinAppDriver();
		launch_ECQTS_Application();
		loginToApplication();
		updateTestSettings();
		updateApplicationSettings();
		searchJobAndNavigationToJobDetailsPage(TestData.jobSearchOrg, TestData.fiberTestJobSearchJobNumber, TestData.fiberTestJobSearchCutNumber,
				TestData.fiberTestJobSearchCutNumberInfo);
		runGetLengthTest();
		runTestInLoop();
		softAssert.assertAll();
	}
}
