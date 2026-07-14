package pageObjects.Modules;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class DownTime extends BaseClass {

	public static WebElement startTimePicker() {
		return driver.findElements(By.xpath("//Button[@AutomationId='FlyoutButton']")).get(0);
	}

	public static WebElement endDateSelection() {
		return driver.findElements(By.className("CalendarDatePicker")).get(1);
	}

	public static WebElement todayDate() {
		return driver.findElement(By.xpath("//DataItem[contains(@Name,'today')]"));
	}

	public static WebElement endTimePicker() {
		return driver.findElements(By.xpath("//Button[@AutomationId='FlyoutButton']")).get(1);
	}

	public static WebElement reasonComboBox() {
		return driver.findElement(By.xpath("//Text[@Name='Reason *']/following::ComboBox[1]"));
	}

	public static WebElement selectReason(String reason) {
		return driver.findElement(By.name(reason));
	}

	public static WebElement commentTextBox() {
		return driver.findElement(By.xpath("//Text[@Name='Comment']/following::Edit[1]"));
	}

	public static WebElement saveButton() {
		return driver.findElement(By.xpath("//Button[@AutomationId='SaveButton']"));
	}

	public static WebElement editButton() {
		return driver.findElement(By.xpath("//*[@ClassName='ListView']//Button"));
	}

	public static String getTotalDownTime() {
		return driver.findElement(By.xpath("//Text[contains(@Name,'Total Down Time')]")).getText();
	}

	public static boolean isWarningMessageDisplayed() {
		return isElementDisplayed(By.name("Warning"), 2);
	}

	public static String getSavedReason() {
		return driver.findElement(By.xpath("//*[@ClassName='ListView']//ListItem//Text[1]")).getText();
	}

	public static String getSavedStartTime() {
		return driver.findElement(By.xpath("//*[@ClassName='ListView']//ListItem//Text[2]")).getText();
	}

	public static String getSavedEndTime() {
		return driver.findElement(By.xpath("//*[@ClassName='ListView']//ListItem//Text[3]")).getText();
	}

}
