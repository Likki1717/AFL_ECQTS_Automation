package pageObjects.Modules.TestJobModule;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class JobSearch extends BaseClass {

	public static WebElement orgField()
	{
		return driver.findElementByAccessibilityId("OrgIdDropdown");
	}
	
	public static WebElement jobNumber() {
		return driver.findElementByName("Job Number");
	}

	public static boolean isJobWarningsPopupDisplayed() {
		return isElementDisplayed(ByName.name("Job Warnings/Errors"), 120);
	}

	public static WebElement searchCutNumber() {
		return driver.findElement(By.xpath("//Edit[@Name='Search Cut Number']"));
	}

	public static WebElement searchCutNumberInfo() {
		return driver.findElement(By.xpath("//Edit[@Name='Search Cut Number Info']"));
	}

	public static WebElement goButton() {
		return driver.findElement(By.xpath("//Button[@Name='GO']"));
	}

	public static boolean isJobNumberLabelDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Job Number *']"), 10);
	}

	public static boolean cutNumberHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Cut Number']"), 5);
	}

	public static boolean userHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='User']"), 5);
	}

	public static boolean dateHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Date']"), 5);
	}

	public static boolean processHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Process']"), 5);
	}

	public static List<?> listOfRowsInCutNumber() {

		return driver.findElementsByXPath("//ListItem[@Name='Microsoft.Maui.Controls.Platform.ItemTemplateContext']");
	}

	public static List<?> listOfRowsInCutNumberInfo() {

		return driver.findElementsByXPath("//ListItem[@Name='Microsoft.Maui.Controls.Platform.ItemTemplateContext']");
	}
	
	public static boolean cutNumberInfoHeader() {
		return isElementDisplayed(By.xpath("//Text[@Name='Cut Number Info']"), 5);	
	}
}
