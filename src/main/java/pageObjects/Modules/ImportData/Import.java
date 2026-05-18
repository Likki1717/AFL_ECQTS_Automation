package pageObjects.Modules.ImportData;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class Import extends BaseClass {

	public static WebElement standardType() {
		return driver.findElementByName("Standard");
	}

	public static WebElement prysmianType() {
		return driver.findElementByName("Prysmian");
	}

	public static boolean isPrysmianTypeDisplayed() {
		return isElementDisplayed(ByName.name("Prysmian"), 1);
	}

	public static WebElement swindonType() {
		return driver.findElementByName("Swindon");
	}

	public static WebElement taihanType() {
		return driver.findElementByName("Taihan");
	}

	public static WebElement qtsType() {
		return driver.findElementByName("QTS");
	}

	public static boolean isImportLoaderNotDisplayed() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 610);
	}

	public static boolean isWarningsErrorsPopupDisplayedOtherThanMissingFiberId() {
		if (isElementDisplayed(By.xpath("//Text[contains(@Name,'Missing Fiber Id; Please create a NCMIR;')]"), 1)) {
			return false;
		} else {
			return isElementDisplayed(By.xpath("//Text[contains(@Name,'Warnings/Errors')]"), 1);
		}
	}
}
