package pageObjects.sideMenu.settings;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByName;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class ConnectionProfiles extends BaseClass{

	public static boolean isDeleteProfileButtonDisplayed()
	{
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("DeleteButton"), 3);
	}
	
	public static WebElement deleteProfileButton()
	{
		return driver.findElementByAccessibilityId("DeleteButton");
	}
	
	public static WebElement yesButtonOnDeleteProfilePopup()
	{
		return driver.findElementByAccessibilityId("AcceptButton");
	}
	
	public static boolean isNoProfilesFoundTextDisplayed()
	{
		return isElementDisplayed(ByName.name("No existing profiles found."), 5);
	}

}
