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
		launchApplication();
		verifyIncorrectCredentials();
		loginToApplication();
		verifyBuildVersion();
		deleteAllExistingConnectionProfiles();
		createConnectionProfile();
	}
}