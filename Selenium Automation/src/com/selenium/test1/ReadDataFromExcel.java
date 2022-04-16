package com.selenium.test1;

import com.utility.Xls_Reader;

public class ReadDataFromExcel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Xls_Reader reader = new Xls_Reader("C:\\Users\\Kumar\\eclipse-workspace\\Selenium Automation\\src\\com\\testdata\\testData.xlsx");
		String FullName = reader.getCellData("testDataSheet", "FullName", 2);
		System.out.println(FullName);
		

	}

}
