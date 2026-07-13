package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class OTDR_Settings extends BaseClass {

	public static String fiberStartValue() {
		WebElement fiberStartValue = driver.findElement(By.xpath(
				"//Text[@Name='Parameters']/following-sibling::Custom//Text[@Name='Fiber Start: ']/following-sibling::Text"));
		actions.moveToElement(driver.findElementByName("Get Length History")).click().build().perform();
		actions.sendKeys(Keys.PAGE_DOWN).build().perform();
		return fiberStartValue.getText();
	}

	public static WebElement connectionProfile() {
		return driver.findElementByAccessibilityId("ConnectionProfileDropdown");
	}

	public static void waitUntilConnectionProfileDropDownIsDisplayed() {
		while (!isElementDisplayed(ByAccessibilityId.AccessibilityId("ConnectionProfileDropdown"), 50)) {
		}
	}

	public static WebElement launchLength() {
		return driver.findElementByAccessibilityId("LaunchLengthEntry");
	}

	public static WebElement launchLength2() {
		return driver.findElementByAccessibilityId("LaunchLengthWtcEntry");
	}

	public static WebElement cutLength() {
		return driver.findElementByAccessibilityId("CutLengthEntry");
	}

	public static WebElement manufactureLength() {
		return driver.findElementByAccessibilityId("ManufacturedLengthEntry");
	}

	public static WebElement horizontal() {
		return driver.findElementByAccessibilityId("HorizontalEntry");
	}

	public static WebElement vertical() {
		return driver.findElementByAccessibilityId("VerticalEntry");
	}

	public static WebElement getLengthButton() {
		return driver.findElementByAccessibilityId("GetLengthButton");
	}

	public static WebElement okButton() {
		return driver.findElementByAccessibilityId("AcceptButton");
	}

	public static boolean isOkButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("AcceptButton"), 50);
	}

	public static WebElement getLengthHistory() {
		return driver.findElement(By.xpath("//Custom[@AutomationId='SalesOrderPicker']/Custom/Text"));
	}

	public static boolean isGetLengthHistoryDropDownFieldDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("OtdrSettingsHistoryPicker"), 50);
	}
}
