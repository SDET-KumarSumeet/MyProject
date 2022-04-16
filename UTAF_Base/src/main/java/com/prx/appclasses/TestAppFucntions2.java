package com.prx.appclasses;

import org.openqa.selenium.WebDriver;

import com.infosys.UTAF.UTAFCommonFunctions2;

public class TestAppFucntions2 extends UTAFCommonFunctions2{
	public static WebDriver driver = null;

	public TestAppFucntions2(WebDriver driver) {
		this.driver = driver;
	
		// this.clickCF = new ClickFunctions();
	}
	
	public static void afSayHelloTwo(String[] inputParams) throws Exception{

		String vName = inputParams[0];
		System.out.println("Hello" + vName + "From TestAppFucntions2 ");
		//UTAFCommonFunctions2.cfGlJsElementSimpleClick(driver, inputParam[0]);
	}
}