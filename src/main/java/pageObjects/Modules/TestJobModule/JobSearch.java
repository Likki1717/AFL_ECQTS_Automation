package pageObjects.Modules.TestJobModule;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class JobSearch extends BaseClass {

	public static WebElement orgField() {
		return driver.findElementByAccessibilityId("OrgIdDropdown");
	}

	public static WebElement locationField() {
		return driver.findElementByAccessibilityId("LocationEntry");
	}

	public static WebElement operatorLine_Cell() {
		return driver.findElementByAccessibilityId("OperatorIdEntry");
	}

	public static WebElement jobNumber() {
		return driver.findElementByName("Job Number");
	}

	public static boolean isJobWarningsPopupDisplayed() {
		return isElementDisplayed(ByName.name("Job Warnings/Errors"), 1);
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

	public static boolean isGoButtonDisplayed() {
		return isElementDisplayed(ByName.name("GO"), 1);
	}

	public static boolean isGoButtonNotDisplayed() throws Exception {
		return isElementNotDisplayed(ByName.name("GO"), 120);
	}

	public static boolean isJobNumberLabelDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Job Number *']"), 10);
	}

	public static boolean isCutNumberHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Cut Number']"), 1);
	}

	public static boolean isUserHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='User']"), 1);
	}

	public static boolean isDateHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Date']"), 1);
	}

	public static boolean isProcessHeaderDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Process']"), 1);
	}

	public static List<?> listOfRowsInCutNumberFieldTable() {

		return driver.findElementsByXPath("//ListItem[@Name='Microsoft.Maui.Controls.Platform.ItemTemplateContext']");
	}

	public static List<?> listOfRowsInCutNumberInfoFieldTable() {

		return driver.findElementsByXPath("//ListItem[@Name='Microsoft.Maui.Controls.Platform.ItemTemplateContext']");
	}

	public static boolean isCutNumberInfoHeaderDisplayed() {
		return isElementDisplayed(
				By.xpath("//Edit[@Name='Search Cut Number Info']/following-sibling::Text[@Name='Cut Number Info']"), 1);
	}

	public static WebElement createNewJobCheckBox() {
		return driver.findElement(By.xpath("//CheckBox[@AutomationId='IsCreateNewJobBox']"));
	}

	public static WebElement itemNumberTextBox() {
		return driver.findElement(By.xpath("//Edit[@Name='Item Number']"));
	}

	public static WebElement createButton() {
		return driver.findElement(By.xpath("//Button[@Name='Create']"));
	}

	public static String getReelId() {
		return driver.findElement(By.xpath("//Text[contains(@Name, 'Reel ID')]/following-sibling::Text[1]"))
				.getAttribute("Name");
	}

}