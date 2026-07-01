package pageObjects.Modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class CopyResults extends BaseClass {

	public static WebElement orgDropDown() {
		return driver.findElement(By.xpath("//*[@AutomationId='OrgIdDropdown']"));
	}

	public static void waitUntilCopyJobPopupIsDisplayed() throws Exception {
		while (!isElementDisplayed(By.xpath("//Text[@Name='Copy Job']"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static WebElement sourceJobNumber() {
		return driver.findElement(By.xpath("//*[@Name='Source']/following::*[@AutomationId='JobNumberEntry'][1]"));
	}

	public static WebElement sourceCutNumber() {
		return driver.findElement(By.xpath("//*[@Name='Source']/following::Edit[@Name='Search Cut Number'][1]"));
	}

	public static WebElement sourceCutNumberInfo() {
		return driver.findElement(By.xpath("//*[@Name='Source']/following::Edit[@Name='Search Cut Number Info'][1]"));
	}

	public static WebElement destinationJobNumber() {
		return driver.findElement(By.xpath("//*[@Name='Destination']/following::*[@AutomationId='JobNumberEntry'][1]"));
	}

	public static WebElement destinationCutNumber() {
		return driver.findElement(By.xpath("//*[@Name='Destination']/following::Edit[@Name='Search Cut Number'][1]"));
	}

	public static WebElement destinationCutNumberInfo() {
		return driver
				.findElement(By.xpath("//*[@Name='Destination']/following::Edit[@Name='Search Cut Number Info'][1]"));
	}
	
	public static boolean isProcessButtonDisplayed()
	{
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("ProcessButton"), 1);
	}

	public static WebElement processButton() {
		return driver.findElementByAccessibilityId("ProcessButton");
	}
	
	public static boolean isJobCopySuccessfullPopupDisplayed()
	{
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("JobSuccessAlertMessage"), 1);
	}	

}
