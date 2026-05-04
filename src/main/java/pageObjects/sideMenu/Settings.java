package pageObjects.sideMenu;

import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Settings extends BaseClass{
	public static WebElement connectionProfilesButton()
	{
		return driver.findElementByAccessibilityId("Connection ProfilesButton");
	}
	
	public static WebElement applicationSettingsButton()
	{
		return driver.findElementByAccessibilityId("Application SettingsButton");
	}
	
	public static WebElement testSettingesButton()
	{
		return driver.findElementByAccessibilityId("Test SettingsButton");
	}
}
