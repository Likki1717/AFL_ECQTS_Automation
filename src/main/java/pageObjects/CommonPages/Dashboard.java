package pageObjects.CommonPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;
import io.appium.java_client.MobileBy.ByAccessibilityId;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import pageObjects.Modules.TestJobModule.JobDetails.FiberResults;

public class Dashboard extends BaseClass {

	public static void waitUntilOpenNavigationButtonIsDisplayed() throws Exception {
		while (!isElementDisplayed(ByXPath.xpath("//Button[@Name='Open Navigation']"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static WebElement openNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Open Navigation']");
	}

	public static WebElement closeNavigationButton() {
		return driver.findElementByXPath("//Button[@Name='Close Navigation']");
	}

	public static boolean isOpenNavigationButtonDisplayed() {
		return isElementDisplayed(ByName.name("Open Navigation"), 2);
	}

	public static boolean isFiberTestModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Fiber TestButton"), 10);
	}

	public static WebElement fiberTestModule() {
		return driver.findElementByAccessibilityId("Fiber TestButton");
	}

	public static WebElement wtcTestModule() {
		return driver.findElementByAccessibilityId("WTC TestButton");
	}

	public static WebElement importDataModule() {
		return driver.findElementByAccessibilityId("Import DataButton");
	}

	public static boolean isImportDataModuleDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("Import DataButton"), 2);
	}

	public static WebElement copyResultsModule() {
		return driver.findElementByAccessibilityId("Copy ResultsButton");
	}

	public static WebElement downTimeModule() {
		return driver.findElement(By.xpath("//Text[@Name='Down Time']"));
	}

	public static WebElement tightBufferModule() {
		return driver.findElement(By.xpath("//Text[@Name='Tight Buffer']"));
	}

	public static WebElement PK_FiberTestModule() {
		return driver.findElement(By.xpath("//Text[@Name='PK Fiber Test']"));
	}

	public static WebElement QE_LabsModule() {
		return driver.findElement(By.xpath("//Text[@Name='QE Labs']"));
	}

	public static WebElement backArrow() {
		return driver.findElementByAccessibilityId("NavigationViewBackButton");
	}

	public static boolean isLoaderNotDisplayed() {
		return isElementNotDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 1);
	}

	public static boolean isLoaderDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 3);
	}

	public static void waitUntilLoaderIsNotDisplayed() throws Exception {
		while (isElementDisplayed(ByAccessibilityId.AccessibilityId("LottiePlayer"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static boolean isOkButtonDisplayed() {
		return isElementDisplayed(ByName.name("OK"), 2);
	}

	public static WebElement okButton() {
		return driver.findElementByName("OK");
	}

	public static void waitUntilOkButtonIsDisplayed() throws Exception {
		while (!isElementDisplayed(ByName.name("OK"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static WebElement cancelButton() {
		return driver.findElementByAccessibilityId("CancelButton");
	}

	public static WebElement continueButton() {
		return driver.findElementByName("Continue");
	}

	public static boolean isCancelButtonVisible() {
		return isElementDisplayed(ByName.name("CancelButton"), 10);
	}

	public static boolean isSyncStatusPopupDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("SyncStatusMessage"), 1);
	}

	public static void waitUntilFileNameTextBoxIsDisplayed() throws Exception {
		while (!isElementDisplayed(ByName.name("File name:"), 1)) {
			Thread.sleep(1000);
		}
	}

	public static void waitUntilWebAddressBarIsDisplayed(WindowsDriver<WindowsElement> browserDriver) throws Exception {
		wait = new WebDriverWait(browserDriver, 30);
		while (true) {
			try {
				wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@Name='Address and search bar']")));
				break;
			} catch (Exception e) {
			}
		}
	}

	public static WebElement webAddressBar(WindowsDriver<WindowsElement> browserDriver) {
		return browserDriver.findElement(By.xpath("//*[@Name='Address and search bar']"));
	}

	public static String getMessageDisplayedOnPopup() {
		return driver.findElement(By.xpath("//Text[contains(@AutomationId, 'Message')]")).getAttribute("Name");
	}

	public static void waitUntilStopButtonsAreNotDisplayed() throws Exception {
		wait = new WebDriverWait(driver, 500);
		wait.until(driver -> driver.findElements(By.name("Stop")).isEmpty());
	}

	public static WebElement stopButton() {
		return driver.findElement(By.xpath("//*[contains(@Name, 'Stop')]"));
	}

	public static void clickStopOrCancelButton(String buttonText) {
		if (buttonText.equals("OK")) {
			cancelButton().click();
		} else if (buttonText.equals("STOP")) {
			stopButton().click();
		}

	}

	public static void clickContinueButtonAndIgnoreIfOkButton(String buttonText) {
		if (buttonText.equals("STOP")) {
			continueButton().click();
		}
	}

	public static boolean isStopButtonDisplayed() {
		return isElementDisplayed(ByAccessibilityId.AccessibilityId("StopButton"), 10);
	}

	public static void waitUntilStopButtonIsDisplayed() throws InterruptedException {
		while (!isElementDisplayed(ByAccessibilityId.AccessibilityId("StopButton"), 5)) {
			Thread.sleep(1000);
		}
	}

	public static String waitUntilDisplayedAndReturn_OK_Or_STOP_Or_COMPLETED() {
		String buttonText = "";
		while (buttonText.equals("")) {
			if (isElementDisplayed(ByName.name("OK"), 1)) {
				buttonText = "OK";
			} else if (isElementDisplayed(ByAccessibilityId.AccessibilityId("StopButton"), 1)) {
				buttonText = "STOP";
			} else if (FiberResults.isTestsCompletedTextDisplayed()) {
				buttonText = "COMPLETED";
			}
		}
		return buttonText;
	}
}
