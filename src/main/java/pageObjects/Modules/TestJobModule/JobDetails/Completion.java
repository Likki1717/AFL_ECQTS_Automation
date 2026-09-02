package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Completion extends BaseClass {

	public static boolean is_ISE_SeqMark_Test_Displayed() {
		return isElementDisplayed(ByName.name("ISE Seq Mark"), 5);
	}

	public static WebElement iseSeqMark() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::Edit[1]"));
	}

	public static WebElement iseSeqMark_UoM() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::ComboBox[1]"));
	}

	public static WebElement oseSeqMark() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::Edit[1]"));
	}

	public static WebElement oseSeqMark_UoM() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::ComboBox[1]"));
	}

	public static WebElement ISE_Print_Verified() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Print Verified']/following::ComboBox[1]"));
	}

	public static WebElement OSE_Print_Verified() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Print Verified']/following::ComboBox[1]"));
	}

	public static WebElement reelItem() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Item']/following-sibling::Edit"));
	}

	public static String getReelItemResult() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Item']/following-sibling::Text[4]"))
				.getAttribute("Name");
	}

	public static WebElement iseReelLabel() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Reel Label']/following-sibling::Text[3]"));
	}

	public static String getIseReelLabelResult() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Reel Label']/following-sibling::Text[5]"))
				.getAttribute("Name");
	}

	public static WebElement oseReelLabel() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Reel Label']/following-sibling::Text[3]"));
	}

	public static String getOseReelLabelResult() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Reel Label']/following-sibling::Text[5]"))
				.getAttribute("Name");
	}

	public static WebElement reelLabel() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Label']/following-sibling::Text[3]"));
	}

	public static String getReelLabelResult() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Label']/following-sibling::Text[5]"))
				.getAttribute("Name");
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

	public static String getIseTestResult() {
		return driver.findElement(By.xpath("//Text[@Name='ISE Seq Mark']/following::ComboBox/following::Text")).getText().trim();
	}

	public static String getOseTestResult() {
		return driver.findElement(By.xpath("//Text[@Name='OSE Seq Mark']/following::ComboBox/following::Text")).getText().trim();
	}

	public static WebElement reelSize() {
		return driver.findElement(By.xpath("//Text[@Name='Reel Size']/following::ComboBox[1]"));
	}

	public static boolean isReelSizeDisplayed() {
		return isElementDisplayed(ByName.name("Reel Size"), 5);
	}
}
