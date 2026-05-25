package pageObjects.Modules.ImportData;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Import extends BaseClass {

	public static WebElement standardType() {
		return driver.findElementByName("Standard");
	}

	public static WebElement prysmianType() {
		return driver.findElementByName("Prysmian");
	}

	public static boolean isPrysmianTypeDisplayed() {
		return isElementDisplayed(ByName.name("Prysmian"), 1);
	}

	public static WebElement swindonType() {
		return driver.findElementByName("Swindon");
	}

	public static WebElement taihanType() {
		return driver.findElementByName("Taihan");
	}

	public static WebElement qtsType() {
		return driver.findElementByName("QTS");
	}

	public static boolean isWarningsErrorsPopupDisplayedOtherThanMissingFiberId() {
		if (isElementDisplayed(By.xpath("//Text[contains(@Name,'Missing Fiber Id; Please create a NCMIR;')]"), 1)) {
			return false;
		} else {
			return isElementDisplayed(By.xpath("//Text[contains(@Name,'Warnings/Errors')]"), 1);
		}
	}

	public static boolean isSelectImportTextDisplayed() {
		return isElementDisplayed(ByName.name("Select an import type from the left panel"), 5);
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
	
	public static WebElement submitButton() {
		return driver.findElement(By.xpath("//Button[@Name='Submit']"));
	}
	
	public static WebElement fileNameTextBox() {
		return driver.findElement(By.xpath("//ComboBox[@Name='File name:']/Edit"));
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
		return isElementDisplayed(By.xpath("//Text[contains(@Name,'Job imported successfully')]"), 1);
	}
	
	public static WebElement okButton() {
		return driver.findElement(By.xpath("//Button[@Name='OK']"));
	}
}
