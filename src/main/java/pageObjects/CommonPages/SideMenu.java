package pageObjects.CommonPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class SideMenu extends BaseClass {

	public static WebElement logOutButton() {
		return driver.findElementByAccessibilityId("LogoutButton");
	}

	public static WebElement dashboardOption() {
		return driver.findElement(By.xpath("//Text[@Name='Dashboard']"));
	}

	public static WebElement aboutOption() {
		return driver.findElementByAccessibilityId("AboutButton");
	}

	public static WebElement settingsButton() {
		return driver.findElementByAccessibilityId("SettingsButton");
	}

}
