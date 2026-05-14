package pageObjects.Modules.FiberTest;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class JobSearch extends BaseClass {
	 
		public static WebElement jobNumber() {
			return driver.findElementByName("Job Number");
		}
		public static boolean isJobWarningsPopupDisplayed(){
			return isElementDisplayed(ByName.name("Job Warnings/Errors"), 50);
		}
		public static WebElement searchCutNumber() {
			return driver.findElement(By.xpath("//Edit[@Name='Search Cut Number']"));
		}
		public static WebElement searchCutNumberInfo() {
			return driver.findElement(By.xpath("//Edit[@Name='Search Cut Number Info']"));
		}
		public static WebElement goButton() {
			return driver.findElement(By.xpath("//Button[@Name='GO']"));
		}
		public static WebElement okButton() {
			return driver.findElementByXPath("//Button[@Name='OK']");
		}
	}
