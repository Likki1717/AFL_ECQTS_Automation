package pageObjects.sideMenu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class Settings extends BaseClass {
	public static WebElement connectionProfilesButton() {
		isElementDisplayed(ByAccessibilityId.AccessibilityId("Connection ProfilesButton"), 5);
		return driver.findElementByAccessibilityId("Connection ProfilesButton");
	}

	public static WebElement applicationSettingsButton() {
		return driver.findElementByAccessibilityId("Application SettingsButton");
	}

	public static WebElement testSettingsButton() {
		return driver.findElementByAccessibilityId("Test SettingsButton");
	}

	public static boolean isTestSettingsButtonDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Test Settings']"), 3);
	}
}
