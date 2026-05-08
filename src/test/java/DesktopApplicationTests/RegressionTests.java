package DesktopApplicationTests;

import org.testng.annotations.Test;

import base.BaseClass;

public class RegressionTests extends BaseClass
{
	@Test
	public static void runRegressionTestSuite() throws Exception
	{
		clearPreviousSessionData();
		launchWinAppDriver();
		launchOpenVpnAppAndConnect();
		launch_ECQTS_Application();
		verifyIncorrectCredentials();
		loginToApplication();
		verifyBuildVersion();
		deleteAllExistingConnectionProfiles();
		createConnectionProfiles();
		editConnectionProfile();
		updateTestSettings();
//		importPrysmianJob();
//		importSwindonJob();
//		importTaihanJob();
		softAssert.assertAll();
	}
}