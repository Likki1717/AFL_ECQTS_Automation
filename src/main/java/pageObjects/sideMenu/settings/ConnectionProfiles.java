package pageObjects.sideMenu.settings;

import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy;

public class ConnectionProfiles extends BaseClass{

	public static boolean isDeleteProfileButtonDisplayed()
	{
		return isElementDisplayed(MobileBy.AccessibilityId(""), 2000);
	}
	
	public static WebElement deleteProfileButton()
	{
		return driver.findElementByAccessibilityId("");
	}
	
	public static WebElement yesButtonOnDeleteProfilePopup()
	{
		return driver.findElementByAccessibilityId("");
	}
}
