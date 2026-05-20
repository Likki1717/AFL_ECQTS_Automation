package pageObjects.Modules.ImportData;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Prysmian extends BaseClass {

	public static WebElement addressBar() {
		return driver.findElement(By.xpath("//ToolBar[contains(@Name,'Address')]"));
	}

	public static WebElement jacketOdFile() {
		return driver.findElement(By.xpath("//ListItem[contains(@Name, 'Jacket')]/Edit[@Name='Name']"));
	}

	public static WebElement attenuationFile() {
		return driver.findElement(By.xpath("//ListItem[contains(@Name, 'Attenuation')]/Edit[@Name='Name']"));
	}

	public static WebElement fileBeingUsedPopup() {
		return driver.findElement(By.xpath(
				"//Text[contains(@Name,'The Process Cannot Access The File Because It Is Being Used By Another Process')]"));
	}

}
