package pageObjects.Modules.TestJobModule;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class JobDetailsPage extends BaseClass {

	public static WebElement OTDR_Length() {
		return driver.findElement(By.xpath("//Text[@Name='OTDR Length:']/following-sibling::Text"));
	}

	public static WebElement protectionLayer() {
		return driver.findElementByName("Protection Layer");
	}

	public static WebElement OTDR_Settings() {
		return driver.findElementByName("OTDR Settings");
	}

	public static boolean isOtdrSettingsTabDisplayed() {
		return isElementDisplayed(ByName.name("OTDR Settings"), 5);
	}

	public static boolean isWtcTabDisplayed() {
		return isElementDisplayed(ByName.name("WTC"), 3);
	}

	public static WebElement wtcTab() {
		return driver.findElementByName("WTC");
	}

	public static WebElement opticsTab() {
		return driver.findElementByName("Optics");
	}

	public static boolean isBufferTubeDisplayed() {
		return isElementDisplayed(By.xpath("//ListItem/Text"), 3);
	}

	public static WebElement firstBufferTubeTab() {
		return driver.findElement(By.xpath("//Text[@AutomationId='OpticsButton']/../following-sibling::ListItem/Text"));
	}

	public static WebElement reportsTab() {
		return driver.findElement(By.xpath("//Text[@Name='Reports']"));
	}

	public static boolean isCompletionTabDisplayed() {
		return isElementDisplayed(ByName.name("Completion"), 10);
	}

	public static WebElement completionTab() {
		return driver.findElement(By.xpath("//Text[@Name='Completion']"));
	}

	public static String getActualTestResultsCounts() {
		int incomplete_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusIncompleteValueLabel']"))
						.getText().split(":")[1].trim());
		int passed_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusSuccessValueLabel']"))
						.getText().split(":")[1].trim());
		int failed_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusFailureValueLabel']"))
						.getText().split(":")[1].trim());
		return "Incomplete: " + incomplete_Tests + ", Passed: " + passed_Tests + ", Failed: " + failed_Tests;
	}

	public static WebElement helixFactor() {
		return driver.findElementByAccessibilityId("HelixFactorValueLabel");
	}

	public static boolean isProtectionLayerTabDisplayed() {
		return isElementDisplayed(ByName.name("Protection Layer"), 5);
	}

	public static boolean isMissingFiberIdWarningPopupDisplayed() {
		return isElementDisplayed(ByName.name("Missing Fiber Id; Please create a NCMIR;"), 2);
	}

	public static WebElement org() {
		return driver.findElement(By.xpath("//Text[@Name='Org Code:']/following-sibling::Text"));
	}

	public static WebElement jobNumber() {
		return driver.findElement(By.xpath("//Text[@Name='Job Number:']/following-sibling::Text"));
	}

	public static WebElement cutNumber() {
		return driver.findElement(By.xpath("//Text[@Name='Cut Number:']/following-sibling::Text"));
	}

	public static WebElement cutNumberInfo() {
		return driver.findElement(By.xpath("//Text[@Name='Cut Info:']/following-sibling::Text"));
	}

	public static WebElement editAdjLengthIcon() {
		return driver.findElementByAccessibilityId("EditAdjustedLengthButton");
	}

	public static WebElement adjLengthInputField() {
		return driver.findElementByAccessibilityId("AdjustedLengthEntry");
	}

	public static WebElement adjLengthSaveIcon() {
		return driver.findElementByAccessibilityId("SaveAdjustedLengthButton");
	}

	public static String getAdjLengthValue() {
		return driver.findElementByAccessibilityId("AdjustedLengthValueLabel").getAttribute("Name");
	}

	public static WebElement ribbonPosition(int ribbonNumber) {
		return driver.findElement(By
				.xpath("(//Text[@AutomationId='OpticsButton']/../following-sibling::ListItem)[" + ribbonNumber + "]"));
	}

	public static int countOfBufferTubesListedInJobDetailsPage() {
		return driver.findElements(By.xpath(
				"//Text[@AutomationId='OpticsButton']/../following-sibling::ListItem/Text/following-sibling::Text[contains(@Name, '%')]"))
				.size();
	}

	public static boolean isLoadingLargeJobPopupDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Download All Fibers DataButton"), 2);
	}

	public static WebElement downloadAllFibersDataButton() {
		return driver.findElementByAccessibilityId("Download All Fibers DataButton");
	}
	
	public static WebElement itemNumber() {
		return driver.findElement(By.xpath("//Text[@AutomationId='ItemNumberValueLabel']"));
	}

}
