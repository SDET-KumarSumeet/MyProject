package com.selenium4.nokia;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class test_1 {

	private WebDriver driver;

	@BeforeClass
	public void testSetUp() {
		// System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		ChromeOptions opt = new ChromeOptions();
		opt.addArguments("--lang=en-ca");
		driver = new ChromeDriver(opt);
	}

	@Test
	public void searchOnGooglePage() throws InterruptedException {
		driver.get("https://google.com");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
		// driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		// driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
		Thread.sleep(5000);

		WebElement Ikgaakkoord = driver.findElement(By.xpath("//div[text()='I agree']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("window.scrollBy(0,1000)");
		js.executeScript("arguments[0].scrollIntoView();", Ikgaakkoord);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		Thread.sleep(5000);
		Ikgaakkoord.click();
		WebElement Search = driver.findElement(By.xpath("//input[@name='q']"));
		Search.sendKeys("Nokia.com");
		driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
		Search.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}

}