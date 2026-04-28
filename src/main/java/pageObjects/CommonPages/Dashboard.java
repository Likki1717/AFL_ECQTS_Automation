package pageObjects.CommonPages;

import org.openqa.selenium.WebElement;

import base.BaseClass;

public class Dashboard extends BaseClass{
	
	public static WebElement openNavigationOption() {
		return driver.findElementByName("Open Navigation");
	}
}
