package pageObjects.Modules.FiberTest.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
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
	
	public static boolean isContinueTestsButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("ContinueButton"), 120);
	}
	
	public static boolean isTestsCompleteDisplayed() {
		return isElementDisplayed(ByName.name("Tests Complete"), 5);
	}
	
	public static WebElement stopButton() {
		return driver.findElementByAccessibilityId("StopButton");
	}
	
	public static WebElement cancelButton() {
		return driver.findElementByAccessibilityId("CancelButton");
	}
	
	public static boolean isOkButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("AcceptButton"), 30);
	}

	public static WebElement okButton() {
		return driver.findElementByAccessibilityId("AcceptButton");
	}

	public static WebElement continueButton() {
		return driver.findElementByAccessibilityId("ContinueButton");
	}
	
	public static WebElement goToFiberButton() {
		return driver.findElementByAccessibilityId("GoToFiberButton");
	}

	public static boolean isGoToFiberButtonVisible() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("GoToFiberButton"), 10);
	}
	
	public static boolean isGoToFiberButtonNotVisible() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("GoToFiberButton"), 10);
	}

	public static WebElement showTracesButton()
	{
		return driver.findElementByAccessibilityId("ShowTracesInfoFilter");
	}
	
	public static WebElement showMoreInfoButton()
	{
		return driver.findElementByAccessibilityId("ShowMoreInfoFilter");
	}
}
