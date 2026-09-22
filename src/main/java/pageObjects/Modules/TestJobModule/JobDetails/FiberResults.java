package pageObjects.Modules.TestJobModule.JobDetails;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import base.TestData;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class FiberResults extends BaseClass {

	public static WebElement presenceOfAttnTest() {
		return driver.findElement(By.xpath("//Text[contains(@Name,'ATTN')]"));
	}

	public static WebElement download_SOR_Button() {
		return driver.findElement(By.xpath("(//Custom[contains(@AutomationId,'SorDownloadButton')])[1]"));
	}

	public static WebElement addressBar() {
		return driver.findElement(By.xpath("//ToolBar[contains(@Name,'Address')]"));
	}

	public static WebElement saveButton() {
		return driver.findElement(By.xpath("//Button[@Name='Save']"));
	}

	public static WebElement testsCountFirstFiber() {
		return driver.findElement(By.xpath("(//Text[contains(@AutomationId,'QuantityTestsCount')])[1]"));
	}

	public static WebElement testsCountSecond_Fiber() {
		return driver.findElement(By.xpath("(//Text[contains(@AutomationId,'QuantityTestsCount')])[2]"));
	}

	public static WebElement runTestsButtonOfFirstFiber() {
		return driver.findElement(By.xpath("(//Button[@Name='Run Tests'])[1]"));
	}

	public static boolean isRunTestsButtonDisplayed() {
		return isElementDisplayed(By.xpath("(//Button[@Name='Run Tests'])[1]"), 3);
	}

	public static boolean isTestsCompletedTextDisplayed() {
		return isElementDisplayed(ByName.name("Tests Complete"), 1);
	}

	public static void waitUntilTestsCompletedTextIsDisplayed() {
		while (!isElementDisplayed(ByName.name("Tests Complete"), 5)) {
		}
	}

	public static WebElement goToFiberButton() {
		return driver.findElementByAccessibilityId("GoToFiberButton");
	}

	public static void enterSpliceGainValues() {
		for (int i = 0; i < (2 * TestData.numberOfFibersToTestForAnomalyVerification); i++) {
			WebElement spliceGainElement = driver.findElement(
					By.xpath("(//Text[contains(@Name, 'Splice Gain')]/following-sibling::Edit[1])[(" + i + "+1)]"));
			spliceGainElement.clear();
			spliceGainElement.sendKeys("1");
		}
	}

	public static boolean isGoToFiberButtonVisible() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("GoToFiberButton"), 10);
	}

	public static boolean isGoToFiberButtonNotVisible() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("GoToFiberButton"), 10);
	}

	public static WebElement showTracesButton() {
		return driver.findElementByAccessibilityId("ShowTracesInfoFilter");
	}

	public static WebElement showMoreInfoButton() {
		return driver.findElementByAccessibilityId("ShowMoreInfoFilter");
	}

	public static WebElement SOR_DownloadIcon(int sorToBeDownloaded) {
		return driver.findElementByXPath(
				"(//Text[contains(@Name, '0:')]/following-sibling::Image)[" + sorToBeDownloaded + "]");
	}

	public static void waitUntil_SOR_DownloadIcon_IsDisplayed(int sorToBeDownloaded) {
		isElementDisplayed(
				ByXPath.xpath("(//Text[contains(@Name, '0:')]/following-sibling::Image)[" + sorToBeDownloaded + "]"),
				100);
	}

	public static boolean isLastUpdatedFieldDisplayed() {
		return isElementDisplayed(ByName.name("Last Updated"), 5);
	}

	public static WebElement fiberID(int fiberPosition) {
		return driver.findElement(By.xpath("(//Edit[contains(@AutomationId, 'StrandId')])[" + fiberPosition + "]"));
	}

	public static int getTestCount(int fiberPosition) {
		return Integer.parseInt(driver
				.findElement(
						By.xpath("(//Text[@Name='Test Count:']/following-sibling::Text[1])[" + fiberPosition + "]"))
				.getText().trim());
	}

	public static WebElement reTestButton() {
		return driver.findElementByAccessibilityId("RetestButton");
	}
}
