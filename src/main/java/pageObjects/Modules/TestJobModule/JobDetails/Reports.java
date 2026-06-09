package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Reports extends BaseClass {

	public static boolean isJobWarnings_ErrorsPopupDisplayed() {
		return isElementDisplayed(ByName.name("Job Warnings/Errors"), 70);
	}

	public static WebElement okButton() {
		return driver.findElement(By.xpath("//Button[@Name='OK']"));
	}

	public static boolean isDownloadOCR_ReportDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Optical Characteristics']"), 10);
	}

	public static WebElement opticalCharacteristics() {
		return driver.findElement(By.xpath("//Text[@Name='Optical Characteristics']"));
	}

	public static boolean isGeneratingReportsInBackgroundTextDisplayed() {
		return isElementDisplayed(ByName.name("Generating reports in background!"), 10);
	}
}
