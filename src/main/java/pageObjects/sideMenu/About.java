package pageObjects.sideMenu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class About extends BaseClass {
	public static WebElement versionNumber() {
		return driver.findElement(By.xpath("//Text[contains(@Name,'Version')]"));
	}

}
