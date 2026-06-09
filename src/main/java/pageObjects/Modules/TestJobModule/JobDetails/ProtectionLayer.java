package pageObjects.Modules.TestJobModule.JobDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseClass;

public class ProtectionLayer extends BaseClass {
	
	public static WebElement j1NomialODVertical() {
		return driver.findElement(By.xpath("//*[@Name='J1 Nominal OD Vertical']/following::Edit[1]"));
	}

	public static WebElement j1NomialODHorizontal() {
		return driver.findElement(By.xpath("//*[@Name='J1 Nominal OD Horizontal']/following::Edit[1]"));
	}

	public static WebElement j1_1stRipcord() {
		return driver.findElement(By.xpath("//*[@Name='J1 1st Ripcord']/following::ComboBox[1]"));
	}

	public static WebElement j1MinSpotWall() {
		return driver.findElement(By.xpath("//*[@Name='J1 Min Spot Wall']/following::Edit[1]"));
	}

	public static WebElement j190DegWall() {
		return driver.findElement(By.xpath("//*[@Name='J1 90 Deg Wall']/following::Edit[1]"));
	}

	public static WebElement j1180DegWall() {
		return driver.findElement(By.xpath("//*[@Name='J1 180 Deg Wall']/following::Edit[1]"));
	}

	public static WebElement editJ1270DegWall() {
		return driver.findElement(By.xpath("//*[@Name='J1 270 Deg Wall']/following::Edit[1]"));
	}

	public static WebElement core1Lay() {
		return driver.findElement(By.xpath("//*[@Name='Core #1 Lay']/following::Edit[1]"));
	}

	public static WebElement FRP_Nomial_OD() {
		return driver.findElement(By.xpath("//*[@Name='FRP Nominal OD']/following::Edit[1]"));
	}
}