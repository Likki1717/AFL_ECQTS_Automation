package pageObjects.Modules.FiberTest;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import base.TestData;

public class JobDetailsPage extends BaseClass {

	public static WebElement jobNumber() {
		return driver.findElement(By.xpath("//Text[@AutomationId='JobNumberValueLabel']"));
	}

	public static WebElement cutNumber() {
		return driver.findElement(By.xpath("//Text[@AutomationId='CutNumberValueLabel']"));
	}

	public static WebElement cutNumberinfo() {
		return driver.findElement(By.xpath("//Text[@AutomationId='CutNumberInfoValueLabel']"));
	}

	public static WebElement OTDR_Length() {
		return driver.findElement(By.xpath("//Text[@Name='OTDR Length:']/following-sibling::Text"));
	}

	public static WebElement protectionLayer() {
		return driver.findElementByName("Protection Layer");
	}

	public static WebElement OTDR_Settings() {
		return driver.findElementByName("OTDR Settings");
	}

	public static WebElement optics() {
		return driver.findElementByName("Optics");
	}

	public static WebElement bufferTube() {
		return driver.findElement(By.xpath(
				"//ListItem[@AutomationId='OpticsButton']//Text[@Name='"+TestData.bufferTube+"']"));
	}

	public static WebElement completion() throws Exception {
		return driver.findElement(By.xpath("//ListItem[@Name='ECS.Entities.Qts.FiberTab']//Text[@Name='Completion']"));
	}

	public static WebElement reports() {
		return driver.findElement(By.xpath("//ListItem[@Name='ECS.Entities.Qts.FiberTab']//Text[@Name='Reports']"));
	}

//	public static String get_Test_Status() {
//		int incomplete_Tests = Integer
//				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusIncompleteValueLabel']"))
//						.getText().split(":")[1].trim());
//		int passed_Tests = Integer
//				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusSuccessValueLabel']"))
//						.getText().split(":")[1].trim());
//		int failed_Tests = Integer
//				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusFailureValueLabel']"))
//						.getText().split(":")[1].trim());
//		if (ISE_Seq_Mark_Test_Result.equalsIgnoreCase("PASS")) {
//			passed_Tests = passed_Tests - 1;
//			incomplete_Tests = incomplete_Tests + 1;
//		} else if (ISE_Seq_Mark_Test_Result.equalsIgnoreCase("FAIL")) {
//			failed_Tests = failed_Tests - 1;
//			incomplete_Tests = incomplete_Tests + 1;
//		}
//		if (OSE_Seq_Mark_Test_Result.equalsIgnoreCase("PASS")) {
//			passed_Tests = passed_Tests - 1;
//			incomplete_Tests = incomplete_Tests + 1;
//		} else if (OSE_Seq_Mark_Test_Result.equalsIgnoreCase("FAIL")) {
//			failed_Tests = failed_Tests - 1;
//			incomplete_Tests = incomplete_Tests + 1;
//		}
//		ISE_Seq_Mark_Test_Result = "Incomplete";
//		OSE_Seq_Mark_Test_Result = "Incomplete";
//		return "Incomplete: " + incomplete_Tests + ", Passed: " + passed_Tests + ", Failed: " + failed_Tests;
//	}

	public static String getActualTestResultsCounts() {
		int incomplete_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusIncompleteValueLabel']"))
						.getText().split(":")[1].trim());
		int passed_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusSuccessValueLabel']"))
						.getText().split(":")[1].trim());
		int failed_Tests = Integer
				.parseInt(driver.findElement(By.xpath("//Text[@AutomationId='OverallStatusFailureValueLabel']"))
						.getText().split(":")[1].trim());
		return "Incomplete: " + incomplete_Tests + ", Passed: " + passed_Tests + ", Failed: " + failed_Tests;
	}

	public static WebElement helixFactor() {
		return driver.findElementByAccessibilityId("HelixFactorValueLabel");
	}

	public static WebElement okButton() {
		return driver.findElementByAccessibilityId("AcceptButton");
	}

	public static boolean isProtectionLayerTabDisplayed() {
		return isElementDisplayed(ByName.name("Protection Layer"), 50);
	}

	public static boolean isMissingFiberIdWarningPopupDisplayed() {
		return isElementDisplayed(ByName.name("Missing Fiber Id; Please create a NCMIR;"), 50);
	}

}
