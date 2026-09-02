package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class Reports extends BaseClass {

	public static boolean isJobWarnings_ErrorsPopupDisplayed() {
		return isElementDisplayed(ByName.name("Job Warnings/Errors"), 70);
	}

	public static boolean isJobWarnings_ErrorsPopupNotDisplayed() {
		return !isElementDisplayed(ByName.name("Job Warnings/Errors"), 5);
	}

	public static void waitUntilDownloadOCR_ReportIsDisplayed() throws Exception {
		while (!isElementDisplayed(By.xpath("//Text[@Name='Optical Characteristics']"), 5)) {
			Thread.sleep(1000);
		}
	}

	public static boolean isDownloadOCR_ReportDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Optical Characteristics']"), 2);
	}

	public static WebElement opticalCharacteristics() {
		return driver.findElement(By.xpath("//Text[@Name='Optical Characteristics']"));
	}

	public static boolean isGeneratingReportsInBackgroundTextDisplayed() {
		return isElementDisplayed(ByName.name("Generating reports in background!"), 10);
	}

	public static WebElement overrideTypeDropdown() {
		return driver.findElement(By.xpath("//Text[@Name='Override Type:']/following-sibling::ComboBox"));
	}

	public static WebElement overrideCheckbox() {
		return driver.findElement(By.xpath("//Text[@Name='Override Type:']/following-sibling::CheckBox"));
	}

	public static WebElement overrideComment() {
		return driver.findElement(By.xpath("//Text[@Name='Override Type:']/following-sibling::Edit"));
	}

	public static WebElement overrideSaveButton() {
		return driver.findElement(By.xpath("//Text[@Name='Override Type:']/following-sibling::Button"));
	}

	public static WebElement shippingLabelReport() {
		return driver.findElement(By.xpath("//Text[@Name='Shipping Label']"));
	}
	
	public static WebElement jacketTestReport() {
		return driver.findElement(By.xpath("//Text[@Name='Jacket Test']"));
	}
	
	public static WebElement failedTestsReport() {
		return driver.findElement(By.xpath("//Text[@Name='Failed Tests']"));
	}

	public static boolean isMeterMarkInputFieldDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("MeterMarkLengthEntry"), 1);
	}

	public static WebElement meterMarkInputField() {
		return driver.findElementByAccessibilityId("MeterMarkLengthEntry");
	}

	public static String getMeterMarkValue() {
		return driver.findElementByAccessibilityId("MeterMarkLengthEntry").getAttribute("Value.Value");
	}

	public static String getMeterMarkUoM() {
		return driver.findElementByAccessibilityId("MeterMarkUomLabel").getAttribute("Name");
	}

	public static WebElement checkMeterMarkButton() {
		return driver.findElementByAccessibilityId("ValidateMeterMarkButton");
	}

	public static String getMeterMarkValidationStatus() {
		return driver.findElementByAccessibilityId("MeterMarkValidationStatusLabel").getAttribute("Name");
	}

}
