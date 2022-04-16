package com.selenium4.nokia;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class readGoogleSuggestion {
	private static WebDriver driver;

	@BeforeClass
	public void testSetUp() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions opt = new ChromeOptions();
		opt.addArguments("--lang=en-ca"); // Setting the default Language of Chrome browser as EN-US
		driver = new ChromeDriver(opt);
	}

	@Test
	public void searchOnGooglePage() throws InterruptedException, IOException{
		driver.get("https://www.google.com");
		// Take Screenshot
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File("./drivers/sc1.png"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4)); // Implicit Wait: Will wait for max 4 seconds if element is not found
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".classlocator")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("")));
		Thread.sleep(1000); // Pause the Execution for 1 second.
		WebElement Ikgaakkoord = driver.findElement(By.xpath("//div[text()='I agree']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("window.scrollBy(0,1000)");
		js.executeScript("arguments[0].scrollIntoView();", Ikgaakkoord); // Vertical scroll till the Web element is found															// found
		Thread.sleep(1000);// Pause the Execution for 1 second.
		Ikgaakkoord.click();
		WebElement Search = driver.findElement(By.xpath("//input[@name='q']"));
		Search.clear();
		Search.sendKeys("Nokia");
		Search.click();
		Thread.sleep(2000);// Pause the Execution for 2 second.
		List<WebElement> myList = driver.findElements(By.xpath("//ul[@role='listbox']//li/descendant::div[@role='option']"));
		// Printing the List options stored in myList
		for (int j = 0; j < myList.size(); j++) {
			System.out.println(myList.get(j).getText());
		}
		// Click on suggested option as per choice
		for (int i = 0; i < myList.size(); i++) {
			if (myList.get(i).getText().contains("smartphone")) {
				myList.get(i).click();
				break;
			}
		}
		Thread.sleep(3000);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}
	

}
