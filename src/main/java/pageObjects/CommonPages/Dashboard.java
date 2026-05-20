package pageObjects.CommonPages;

import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class Dashboard extends BaseClass {

	public static WebElement openNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Open Navigation']");
	}

	public static boolean isOpenNavigationButtonDisplayed() {
		return isElementDisplayed(ByName.name("Open Navigation"), 1);
	}

	public static boolean isFiberTestModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Fiber TestButton"), 40);
	}

	public static WebElement fiberTestModule() {
		return driver.findElementByAccessibilityId("Fiber TestButton");
	}

	public static WebElement importDataModule() {
		return driver.findElementByAccessibilityId("Import DataButton");
	}

	public static boolean isImportDataModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Import DataButton"), 2);
	}

	public static WebElement backArrow() {
		return driver.findElementByAccessibilityId("NavigationViewBackButton");
	}

	public static boolean isLoaderNotDisplayed() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 10);
	}

	public static boolean isLoaderDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 10);
	}
}
