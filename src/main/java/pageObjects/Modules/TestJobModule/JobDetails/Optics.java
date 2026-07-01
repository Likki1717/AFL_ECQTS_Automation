package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;

import base.BaseClass;

public class Optics extends BaseClass {

	public static void waitUntilColorFieldIsDisplayed() throws Exception {
		while(!isElementDisplayed(By.xpath("//Text[@Name='Color']"), 1))
		{
			Thread.sleep(1000);
		}
	}
	
	public static int countOfBufferTubesInOpticsPage() {
		return driver.findElements(By.xpath("//Text[@Name='Color']/../List/ListItem[@Name='Microsoft.Maui.Controls.Platform.ItemTemplateContext']"))
				.size();
	}
}
