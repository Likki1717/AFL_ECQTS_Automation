package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class WTC extends BaseClass {
	
	public static WebElement runTestsButton()
	{
		return driver.findElementByXPath("//Button[@Name='Run Tests']");
	}

	public static WebElement selectWorkerField()
	{
		return driver.findElement(By.xpath("//Button[@Name='Run Tests']/following-sibling::ComboBox"));
	}
	
	public static WebElement checkButton(int checkButtonPosition)
	{
		return driver.findElement(By.xpath("(//Button[contains(@AutomationId,'CheckButton')])["+checkButtonPosition+"]"));
	}
	
	public static boolean isRunTestsButtonDisplayed()
	{
		return isElementDisplayed(ByXPath.xpath("//Button[@Name='Run Tests']"), 5);
	}
	
	public static void waitUntilStopButtonIsNotDisplayed() throws InterruptedException {
		while (isElementDisplayed(ByName.name("Stop"), 1)) {
			Thread.sleep(1000);
		}
	}
}
