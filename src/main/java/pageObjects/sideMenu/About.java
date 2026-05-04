package pageObjects.sideMenu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class About extends BaseClass {
	public static WebElement versionNumber() {
		return driver.findElement(By.xpath("//Text[contains(@Name,'Version')]"));
	}

	public static WebElement portalLink() {
		return driver.findElement(
				By.xpath("//Text[contains(@Name,'Portal: https://www.ecqts') and contains(@Name, '.aflglobal.com')]"));
	}

	public static WebElement additionalInformationButton() {
		return driver.findElement(By.xpath("//Button[@Name='Additional Info']"));
	}

	public static WebElement appLink() {
		return driver.findElement(By.xpath(
				"//Text[contains(@Name,'http://install.ecqts.aflglobal.com.s3.us-east-1.amazonaws.com/maui') and contains(@Name, '/index.html')]"));
	}

	public static WebElement checkForUpdatesButton() {
		return driver.findElement(By.xpath("//Button[@Name='Check for Updates']"));
	}

	public static boolean isLatestVersionMessageDisplayed() {
		return isElementDisplayed(
				By.xpath("//Text[contains(@Name, 'You') and contains (@Name, 're on the latest version.')]"), 5);
	}
}
