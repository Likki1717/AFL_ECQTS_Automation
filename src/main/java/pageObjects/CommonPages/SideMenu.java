package pageObjects.CommonPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class SideMenu extends BaseClass {

	public static WebElement logOutButton() {
		return driver.findElementByAccessibilityId("LogoutButton");
	}

	public static WebElement dashboardButton() {
		return driver.findElement(By.xpath("//Group[@AutomationId='MenuItemsHost']/ListItem[@Name='Dashboard']"));
	}
	
	public static boolean  isDashboardButtonDisplayed() throws Exception {
		return isElementDisplayed(By.xpath("//Group[@AutomationId='MenuItemsHost']/ListItem[@Name='Dashboard']"),3);
	}
	
	public static boolean isSettingsButtonDisplayed()
	{
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("SettingsButton"), 3);
	}

	public static WebElement aboutButton() {
		return driver.findElementByName("About");
	}

	public static WebElement settingsButton() {
		return driver.findElementByAccessibilityId("SettingsButton");
	}

}
