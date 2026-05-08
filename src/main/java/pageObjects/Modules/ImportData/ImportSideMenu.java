package pageObjects.Modules.ImportData;

import org.openqa.selenium.WebElement;

import base.BaseClass;

public class ImportSideMenu extends BaseClass{

	public static WebElement standardType()
	{
		return driver.findElementByName("Standard");
	}
	
	public static WebElement prysmianType()
	{
		return driver.findElementByName("Prysmian");
	}
	
	public static WebElement swindonType()
	{
		return driver.findElementByName("Swindon");
	}
	
	public static WebElement taihanType()
	{
		return driver.findElementByName("Taihan");
	}
	
	public static WebElement qtsType()
	{
		return driver.findElementByName("QTS");
	}
}
