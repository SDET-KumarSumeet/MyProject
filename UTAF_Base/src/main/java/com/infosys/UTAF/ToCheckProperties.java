package com.infosys.UTAF;

import java.io.File;


import jxl.Sheet;
import jxl.Workbook;

public class ToCheckProperties {

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Data\\Automation\\UTAF_AUTOMATION\\UTAF_DEMO.xls");
		
		UTAFCommonFunctions2.setPropFile("C:\\Data\\Automation\\UTAF_AUTOMATION\\UTAF_DEMO.properties");

		Workbook.getWorkbook(file);
		Workbook wb = Workbook.getWorkbook(file);
		String vKey = "";
		Sheet tsSSheet = wb.getSheet("TestSteps");
		int tcSheetRowCount = tsSSheet.getRows();
		for (int tcSheetIterator = 1; tcSheetIterator < tcSheetRowCount; tcSheetIterator++) {
			String vTCName = tsSSheet.getCell(0, tcSheetIterator).getContents();
			String vStepName = tsSSheet.getCell(3, tcSheetIterator).getContents();
			String StepDesc = tsSSheet.getCell(2, tcSheetIterator).getContents();
			String StepType= tsSSheet.getCell(6, tcSheetIterator).getContents();
			
			vKey = tsSSheet.getCell(7, tcSheetIterator).getContents();
			if(!((vKey == null) || vKey.isEmpty())){
				if(vKey.contains("GPWE")){
					if(!UTAFCommonFunctions2.objectMapProps.containsKey(vKey)){
					System.out.println(tcSheetIterator + "|" + vTCName + "|" + vStepName + "|"+ StepType + "|" + vKey);
					}
				}
			}
			vKey = tsSSheet.getCell(8, tcSheetIterator).getContents();
			if(!((vKey == null) || vKey.isEmpty())){
				if(vKey.contains("GPWE")){
					if(!UTAFCommonFunctions2.objectMapProps.containsKey(vKey)){
					System.out.println(tcSheetIterator + "|" + vTCName + "|" + vStepName + "|"+ StepType + "|" + vKey);
					}
				}
			}
			vKey = tsSSheet.getCell(9, tcSheetIterator).getContents();
			if(!((vKey == null) || vKey.isEmpty())){
				if(vKey.contains("GPWE")){
					if(!UTAFCommonFunctions2.objectMapProps.containsKey(vKey)){
					System.out.println(tcSheetIterator + "|" + vTCName + "|" + vStepName + "|"+ StepType + "|" + vKey);
					}
				}
			}
		}
		wb.close();
	}
}
