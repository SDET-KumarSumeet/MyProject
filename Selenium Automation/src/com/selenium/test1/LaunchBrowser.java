package com.selenium.test1;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LaunchBrowser {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		System.setProperty("webdriver.chrome.driver", "D:\\SeleniumWebdriver\\BrowserDriver\\chromedriver_win32\\chromedriver.exe");

	ChromeDriver driver = new ChromeDriver();
	driver.get("http://www.google.com");
	driver.manage().window().maximize();
	//driver.quit();

	
	
	
		
	}

}
