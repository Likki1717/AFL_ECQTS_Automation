package DesktopApplicationTests;

import java.awt.event.KeyEvent;

import org.testng.annotations.Test;

import base.BaseClass;

public class RegressionTests extends BaseClass {
	@Test
	public static void runRegressionTestSuite() throws Exception {

		Exception exception = null;

		try {

//BeforeClass - applicationSetupAndLaunch includes - clearPreviousSessionData, launchWinAppDriver, launchOpenVpnAppAndConnect, 
//launch_ECQTS_Application, verifyIncorrectCredentials, validateRecoverPasswordButtonAvailability, loginToApplication	

			verifyAboutPage();

			verify_Delete_Create_And_Edit_Connection_Profiles();

			verify_Fiber_Test_Module();

//			verify_WTC_Test_Module();

			verify_PK_Fiber_Test_Module();

			verify_Tight_Buffer_Module();

			verify_Copy_Results_Module();

			verify_All_Three_Imports();

			verify_Down_Time_Tracker_Module();

			verify_If_SOR_And_OCR_Files_Downloaded();

			verify_Reel_Id_And_Remove_Sales_Order_Flow();

			verify_Override_Meter_Mark_Validation_With_Tester_Role();

			log_Out_And_Close_Application();

			verify_Anomaly_Status();

			verify_QE_Labs();

			verify_Override_Meter_Mark_Validation_With_Executive_Role();

		} catch (Exception e) {
			exception = e;
		} finally {
			try {
				try {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_TAB);
					robot.keyRelease(KeyEvent.VK_TAB);
					robot.keyPress(KeyEvent.VK_TAB);
					robot.keyRelease(KeyEvent.VK_TAB);
					robot.keyRelease(KeyEvent.VK_ALT);
//					actions.keyDown(Keys.ALT).sendKeys(Keys.TAB).sendKeys(Keys.TAB).keyUp(Keys.ALT).build().perform();
				} catch (Exception e) {
				}
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