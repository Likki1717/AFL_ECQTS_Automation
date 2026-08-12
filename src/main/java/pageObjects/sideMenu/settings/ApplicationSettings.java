package pageObjects.sideMenu.settings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class ApplicationSettings extends BaseClass {
	
	public static WebElement cameraSourceDropDown()
	{
		return driver.findElementByAccessibilityId("CameraSourceDropdown");
	}
	
	public static boolean isCameraSourceDropDownDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("CameraSourceDropdown"), 3);
	}
	
	public static WebElement integratedCamera()
	{
		return driver.findElement(By.xpath("//ListItem[contains(@Name, 'Integrated Camera')]"));
	}
	
	public static WebElement externalCamera()
	{
		return driver.findElement(By.xpath("//ListItem[not(contains(@Name, 'Camera'))]"));
	}
	
	public static boolean isExternalCameraDisplayed() {
		return isElementDisplayed(By.xpath("//ListItem[not(contains(@Name, 'Camera'))]"), 2);
	}
}
