package pageObjects.CommonPages;

import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy;

public class Dashboard extends BaseClass {

	public static WebElement openNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Open Navigation']");
	}

	public static boolean isFiberTestModuleDisplayed() {
		return isElementDisplayed(MobileBy.AccessibilityId("Fiber TestButton"), 40000);
	}

	public static WebElement FiberTestModule() {
		return driver.findElementByAccessibilityId("Fiber TestButton");
	}
}
