package pageObjects.sideMenu.settings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class TestSettings extends BaseClass {

	public static WebElement testSettingsButton() {
		return driver.findElementByXPath("//Text[@Name='Test Settings']");
	}

	public static boolean isDisplayRealTimeToogleDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Display Realtime Plot']"), 3);
	}

	public static WebElement displayRealTimetoggle() {
		return driver.findElementByXPath("//Text[@Name='Display Realtime Plot']/following::Button[1]");
	}

	public static WebElement enableOfflineReportsToggle() {
		return driver.findElementByXPath("//Text[@Name='Enable Offline Reports']/following::Button[1]");
	}

}
