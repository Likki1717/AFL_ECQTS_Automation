package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class WTC extends BaseClass {

	public static WebElement previousRunTestsButton() {
		int count = driver.findElementsByXPath("//Text[@Name='PASS']/../Button[@Name='Run Tests']").size();
		return driver.findElementByXPath("(//Text[@Name='PASS']/../Button[@Name='Run Tests'])[" + count + "]");
	}

	public static WebElement runTestsButton() {
		return driver.findElementByXPath("//Text[@Name='INCOMPLETE']/../Button[@Name='Run Tests']");
	}

	public static WebElement selectWorkerField() {
		return driver.findElement(
				By.xpath("//Text[@Name='INCOMPLETE']/../Button[@Name='Run Tests']/following-sibling::ComboBox"));
	}

	public static void waitUntilTestIsCompletedForSelectedWorker(String worker) throws Exception {
		boolean testCompleted = false;
		while (!testCompleted) {
			try {
				if (driver.findElement(By.xpath("//Button[@Name='Stop']/following-sibling::ComboBox")).getText()
						.contains(worker)) {
					Thread.sleep(1000);
				} else {
					testCompleted = true;
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				testCompleted = true;
			}
		}
	}

	public static WebElement incompleteStatus() {
		return driver
				.findElement(By.xpath("//Text[@Name='INCOMPLETE']"));
	}

	public static WebElement checkButton() {
		return driver
				.findElement(By.xpath("//Text[@Name='INCOMPLETE']/../Button[contains(@AutomationId,'CheckButton')]"));
	}

	public static boolean isRunTestsButtonDisplayed() {
		return isElementDisplayed(ByXPath.xpath("//Button[@Name='Run Tests']"), 3);
	}

	public static void waitUntilStopButtonIsNotDisplayed() throws InterruptedException {
		while (isElementDisplayed(ByName.name("Stop"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static boolean isErrorMessageDisplayed() {
		return isElementDisplayed(ByName.name("Error Message"), 1);
	}
}
