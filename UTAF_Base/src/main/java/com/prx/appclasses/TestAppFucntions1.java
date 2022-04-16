package com.prx.appclasses;

import org.openqa.selenium.WebDriver;

import com.infosys.UTAF.UTAFCommonFunctions2;
import com.infosys.UTAF.extend.UTAFAppFunctions;

public class TestAppFucntions1 extends UTAFCommonFunctions2{
	public static WebDriver driver = null;

	public TestAppFucntions1() {
		this.driver = UTAFAppFunctions.driver;
	
		// this.clickCF = new ClickFunctions();
	}
	
	public static void afSayHelloOne(String[] inputParams) throws Exception{

		String vName = inputParams[0];
		System.out.println("Hello" + vName + "From TestAppFucntions1 ");
		//UTAFCommonFunctions2.cfGlJsElementSimpleClick(driver, inputParam[0]);
	}
}
