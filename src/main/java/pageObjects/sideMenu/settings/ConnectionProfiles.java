package pageObjects.sideMenu.settings;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;

public class ConnectionProfiles extends BaseClass{

	public static boolean isDeleteProfileButtonDisplayed()
	{
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("DeleteButton"), 3);
	}
	
	public static WebElement deleteProfileButton()
	{
		return driver.findElementByAccessibilityId("DeleteButton");
	}
	
	public static WebElement yesButtonOnDeleteProfilePopup()
	{
		return driver.findElementByAccessibilityId("AcceptButton");
	}
	
	public static boolean isNoProfilesFoundTextDisplayed()
	{
		return isElementDisplayed(ByName.name("No existing profiles found."), 5);
	}
	
	public static WebElement createNewProfileButton() {
		return driver.findElementByName("Create New");
	}
 
	public static WebElement nameTextBox() {
		return driver.findElementByXPath("//Text[@Name='Name']/following::Edit[1]");
	}
 
	public static WebElement instrumentTypeDropdown() {
		return driver.findElementByXPath("//ComboBox[@Name='Instrument Type']");
	}
 
	public static WebElement instrumentType_Simulator() {
		return driver.findElementByXPath("//Text[@Name='Simulator']");
	}
 
	public static WebElement ipAddressTextBox() {
		return driver.findElementByXPath("//Text[@Name='IP Address']/following::Edit[1]");
	}
 
	public static WebElement portTextBox() {
		return driver.findElementByXPath("//Text[@Name='Port']/following::Edit[1]");
	}
 
	public static WebElement saveProfileButton() {
		return driver.findElementByXPath("//Button[@Name='Save']");
	}
 
	public static WebElement switchTypeDropdown() {
		return driver.findElementByXPath("//ComboBox[@Name='Switch Type']");
	}
 
	public static WebElement switchType_JGR_Switch() {
		return driver.findElementByXPath("//Text[@Name='JGR Switch']");
	}
 
	public static WebElement findAddressTextBox() {
		return driver.findElementByXPath("//Button[@Name='Find Address']/following::Edit[1]");
	}
 
	public static WebElement testSwitchConnectionButton() {
		return driver.findElementByXPath("//Button[@Name='Test Switch Connection']");
	}
 
	public static boolean isConnectionSuccessfulPopupDisplayed() {
		return isElementDisplayed(By.xpath("//Text[contains(@Name, 'Connection Successful')]"), 15);
	}
	
	public static boolean isConnectionFailurePopupDisplayed() {
		return isElementDisplayed(By.xpath("//Text[contains(@Name, 'Connection Failed')]"), 15);
	}
 
	public static WebElement switchModuleTextBox() {
		return driver.findElementByXPath("//Text[@Name='Switch Module']/following::Edit[1]");
	}
 
	public static WebElement workerNumberTextBox() {
		return driver.findElementByXPath("//Text[@Name='Worker Number']/following::Edit[1]");
	}
 
	public static WebElement spoonTextBox() {
		return driver.findElementByXPath("//Text[@Name='Spoon']/following::Edit[1]");
	}
 
	public static WebElement instrumentType_Anritsu_MT_9085() {
		return driver.findElementByXPath("//Text[@Name='Anritsu MT-9085']");
	}
 
	public static WebElement editButton() {
		return driver.findElementByXPath("//Button[@Name='Edit']");
	}
 
	public static WebElement cancelButton() {
		return driver.findElementByXPath("//Button[@Name='Cancel']");
	}
 
	public static WebElement testConnection() {
		return driver.findElementByXPath("//Button[@Name='Test Connection']");
	}
 
	public static WebElement okButton() {
		return driver.findElementByName("OK");
	}

}
