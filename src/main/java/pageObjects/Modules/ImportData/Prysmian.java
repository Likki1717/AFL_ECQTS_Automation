package pageObjects.Modules.ImportData;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByName;

import base.BaseClass;

public class Prysmian extends BaseClass {

	public static boolean isScreenLoadingMessageDisplayed() {
		return isElementDisplayed(ByName.name("Loading..."), 5);
	}

	public static boolean isScreenLoadingMessageNotDisplayed() {
		return isElementNotDisplayed(ByName.name("Loading..."), 10);
	}

	public static WebElement orgDropDownField() {
		return driver.findElementByAccessibilityId("OrgDropdown");
	}

	public static WebElement cutNumber() {
		return driver.findElementByAccessibilityId("CutNumberId");
	}

	public static WebElement itemOrgCode() {
		return driver.findElement(By.xpath("//Text[@Name='Item Org Code *']/following-sibling::ComboBox"));
	}

	public static WebElement cutNumberInfo() {
		return driver.findElement(By.xpath("//Text[@Name='Cut Number Info']/following-sibling::ComboBox"));
	}

	public static WebElement importType() {
		return driver.findElement(By.xpath("//Text[@Name='Import Type *']/following-sibling::ComboBox"));
	}

	public static WebElement uploadFileButton() {
		return driver.findElementByName("Upload CSV");
	}

	public static WebElement addressBar() {
		return driver.findElement(By.xpath("//ToolBar[contains(@Name,'Address')]"));
	}

	public static WebElement jacketOdFile() {
		return driver.findElement(By.xpath("//ListItem[contains(@Name, 'Jacket')]/Edit[@Name='Name']"));
	}

	public static WebElement attenuationFile() {
		return driver.findElement(By.xpath("//ListItem[contains(@Name, 'Attenuation')]/Edit[@Name='Name']"));
	}

	public static WebElement submitButton() {
		return driver.findElement(By.xpath("//Button[@Name='Submit']"));
	}

	public static WebElement okButton() {
		return driver.findElement(By.xpath("//Button[@Name='OK']"));
	}

	public static WebElement fileBeingUsedPopup() {
		return driver.findElement(By.xpath(
				"//Text[contains(@Name,'The Process Cannot Access The File Because It Is Being Used By Another Process')]"));
	}

	public static WebElement fileNameTextBox() {
		return driver.findElement(By.xpath("//ComboBox[@Name='File name:']/Edit"));
	}

	public static boolean isFileNameTextBoxDisplayed() {
		return isElementDisplayed(ByName.name("File name:"), 10);
	}

	public static String getJobNumberFromImportSuccessFulPopup() {
		// Name Job imported successfully, but we couldn't load the job details right
		// now.
		// Please go to FIBER Test and search for Job Number: 90043053-159 to view the
		// job details.
		return driver.findElement(By.xpath("//Text[contains(@Name,'Job imported successfully')]")).getText()
				.split("Job Number: ")[1].split(" ")[0];
	}

	public static boolean isImportSuccessfulPopupDisplayed() {
		return isElementDisplayed(By.xpath("//Text[contains(@Name,'Job imported successfully')]"), 3);
	}
}
