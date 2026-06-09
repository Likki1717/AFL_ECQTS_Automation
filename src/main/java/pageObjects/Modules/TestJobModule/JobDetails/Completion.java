package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Completion extends BaseClass {

	public static boolean isSeqNumberTestDisplayed() {
		return isElementDisplayed(ByName.name("ISE Seq Mark"), 5);
	}

	public static WebElement ISE_Seq_Number() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::Edit[1]"));
	}

	public static WebElement ISE_Seq_Number_uoM() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::ComboBox[1]"));
	}

	public static WebElement OSE_Seq_Number() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::Edit[1]"));
	}

	public static WebElement OSE_Seq_Number_uoM() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::ComboBox[1]"));
	}

	public static WebElement ISE_Print_Verified() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Print Verified']/following::ComboBox[1]"));
	}

	public static WebElement OSE_Print_Verified() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Print Verified']/following::ComboBox[1]"));
	}

	public static WebElement reelItem() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Item']/following::Edit[1]"));
	}

	public static WebElement jacketColor() {
		return driver.findElement(By.xpath("//Text[@Name='Jacket Color']/following::ComboBox[1]"));
	}

	public static WebElement OSE_Print_Spacing() {
		return driver.findElement(By.xpath("//Text[@Name='OSE PRINT SPACING']/following::ComboBox[1]"));
	}

	public static WebElement ISE_Print_Spacing() {
		return driver.findElement(By.xpath("//Text[@Name='ISE PRINT SPACING']/following::ComboBox[1]"));
	}

	public static WebElement okButton() {
		return driver.findElement(By.xpath("//Button[@Name='OK']"));
	}

	public static boolean isInvalidMeterMarksPopupDisplayed() {
		return isElementDisplayed(ByName.name("Invalid Meter Marks"), 30);
	}
	
	public static WebElement completionTabIseTestResult() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::ComboBox/following::Text"));
		}

	public static WebElement completionTabOseTestResult() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::ComboBox/following::Text"));
		}
}
