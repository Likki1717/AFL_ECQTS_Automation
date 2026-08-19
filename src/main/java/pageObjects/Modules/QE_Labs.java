package pageObjects.Modules;

import org.openqa.selenium.By;

import base.BaseClass;

public class QE_Labs extends BaseClass {

	public static String get_QE_Labs_Comming_Soon_Text() {
		isElementDisplayed(By.xpath("//Text[@Name='QE Labs']"), 3);
		return driver.findElement(By.xpath("//*[@AutomationId='ComingSoon..Message']")).getText();
	}
}
