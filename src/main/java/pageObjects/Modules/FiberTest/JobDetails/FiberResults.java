package pageObjects.Modules.FiberTest.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class FiberResults extends BaseClass {

	public static WebElement presence_Of_Attn_Test() {
		return driver.findElement(By.xpath("//Text[contains(@Name,'ATTN')]"));
	}

	public static WebElement download_SOR_Button() {
		return driver.findElement(By.xpath("(//Custom[contains(@AutomationId,'SorDownloadButton')])[1]"));
	}

	public static WebElement address_Bar() {
		return driver.findElement(By.xpath("//ToolBar[contains(@Name,'Address')]"));
	}

	public static WebElement save_Button() {
		return driver.findElement(By.xpath("//Button[@Name='Save']"));
	}

	public static WebElement tests_Count_First_Fiber() {
		return driver.findElement(By.xpath("(//Text[contains(@AutomationId,'QuantityTestsCount')])[1]"));
	}

	public static WebElement tests_Count_Second_Fiber() {
		return driver.findElement(By.xpath("(//Text[contains(@AutomationId,'QuantityTestsCount')])[2]"));
	}

	public static WebElement run_Tests_Button() {
		return driver.findElement(By.xpath("(//Button[@Name='Run Tests'])[1]"));
	}
	
	public static boolean isRunTestsButtonDisplayed() {
		return isElementDisplayed(By.xpath("(//Button[@Name='Run Tests'])[1]"), 10);
	}
	
	public static boolean isReTestsButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("RetestButton"), 120);
	}
	
	public static WebElement reTestButton() {
		return driver.findElementByAccessibilityId("RetestButton");
	}
	
	public static boolean isOkButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("AcceptButton"), 30);
	}

	public static WebElement ok_Button() {
		return driver.findElementByAccessibilityId("AcceptButton");
	}

	public static WebElement cancel_Button() {
		return driver.findElementByAccessibilityId("CancelButton");
	}

	public static WebElement continue_Button() {
		return driver.findElementByAccessibilityId("ContinueButton");
	}

	public static WebElement stop_Button() {
		return driver.findElement(By.xpath("//Button[@AutomationId='StopButton']"));
	}

	public static boolean isGoToFiberButtonVisible() {
		return isElementDisplayed(By.xpath("//Button[@Name='Go to Fiber']"), 10);
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
