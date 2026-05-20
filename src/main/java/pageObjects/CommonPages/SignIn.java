package pageObjects.CommonPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class SignIn extends BaseClass {

	public static boolean isUsernameFieldDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("LoginIdEntry"), 10);
	}
	
	public static WebElement usernameField() {
		return driver.findElementByAccessibilityId("LoginIdEntry");
	}

	public static WebElement passwordField() {
		return driver.findElementByAccessibilityId("PasswordEntry");
	}

	public static WebElement signInButton() {
		return driver.findElementByAccessibilityId("LoginButton");
	}

	public static boolean isloginFailureDisplayed() {
		return isElementDisplayed(By.xpath("//Text[@Name='Login Failed']"), 5);
	}

	public static WebElement okButtonOnLoginFailurePopup() {
		return driver.findElement(By.xpath("//Button[@Name='OK']"));
	}

}
