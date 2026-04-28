package pageObjects.CommonPages;

import org.openqa.selenium.WebElement;

import base.BaseClass;

public class SignIn extends BaseClass {

	public static WebElement usernameField() {
		return driver.findElementByAccessibilityId("LoginIdEntry");
	}

	public static WebElement passwordField() {
		return driver.findElementByAccessibilityId("PasswordEntry");
	}

	public static WebElement signInButton() {
		return driver.findElementByAccessibilityId("LoginButton");
	}

}
