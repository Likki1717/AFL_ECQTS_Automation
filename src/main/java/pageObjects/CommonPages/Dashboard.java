package pageObjects.CommonPages;

import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class Dashboard extends BaseClass {

	public static void waitUntilOpenNavigationButtonIsDisplayed()
	{
		
	}
	
	public static WebElement openNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Open Navigation']");
	}
	
	public static WebElement closeNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Close Navigation']");
	}

	public static boolean isOpenNavigationButtonDisplayed() {
		return isElementDisplayed(ByName.name("Open Navigation"), 5);
	}

	public static boolean isFiberTestModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Fiber TestButton"), 10);
	}

	public static WebElement fiberTestModule() {
		return driver.findElementByAccessibilityId("Fiber TestButton");
	}
	
	public static WebElement wtcTestModule() {
		return driver.findElementByAccessibilityId("WTC TestButton");
	}

	public static WebElement importDataModule() {
		return driver.findElementByAccessibilityId("Import DataButton");
	}

	public static boolean isImportDataModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Import DataButton"), 1);
	}

	public static WebElement backArrow() {
		return driver.findElementByAccessibilityId("NavigationViewBackButton");
	}

	public static boolean isLoaderNotDisplayed() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 1);
	}

	public static boolean isLoaderDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 2);
	}
	
	public static void waitUntilLoaderIsNotDisplayed() throws InterruptedException {
		while (isElementDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 1)) {
			Thread.sleep(1000);
		}
	}
	
	public static boolean isOkButtonDisplayed() {
		return isElementDisplayed(ByName.name("OK"), 5);
	}

	public static WebElement okButton() {
		return driver.findElementByName("OK");
	}
	
	public static void waitUntilOkButtonIsDisplayed() throws InterruptedException {
		while (!isElementDisplayed(ByName.name("OK"), 1)) {
			Thread.sleep(1000);
		}
	}
	
	public static boolean isSyncStatusPopupDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("SyncStatusMessage"), 1);
	}
	
	public static void waitUntilFileNameTextBoxIsDisplayed() throws Exception
	{
		while (!isElementDisplayed(ByName.name("File name:"), 1)) {
			Thread.sleep(1000);
		}
	}
	
	public static String getWarningMessage()
	{
		return driver.findElement(By.xpath("//Text[contains(@AutomationId, 'WarningsMessage')]")).getText();
	}

}
