package pageObjects.CommonPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class SideMenu extends BaseClass {

	public static WebElement logOutButton() {
		return driver.findElementByAccessibilityId("LogoutButton");
	}

	public static WebElement dashboardButton() {
		return driver.findElement(By.xpath("//Text[@Name='Dashboard']"));
	}

	public static WebElement aboutButton() {
		return driver.findElementByName("About");
	}

	public static WebElement settingsButton() {
		return driver.findElementByAccessibilityId("SettingsButton");
	}

}
