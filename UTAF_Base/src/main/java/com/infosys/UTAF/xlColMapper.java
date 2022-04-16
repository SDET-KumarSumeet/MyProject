package com.infosys.UTAF;

/*
 * This interface was developed for easy maintenance of excel sheet columns
 * 
 */

public interface xlColMapper {
	
	//testSuite Sheet variables
	int xlTsTestSuiteName = 1;
	int xlTsTestSuiteExeFlag = 2;
	
	//testCase Sheet variables
	int xlTcID = 0;
	int xlTcTestSuiteName = 1;
	int xlTcTestCaseName = 2;
	int xlTcTestCaseExeFlag = 3;
	int xlTcTestCaseStartRow = 4;
	int xlTcTestCaseStepCount = 5;
	int xlTcTestCaseIPStart = 6;
	//int xlTcTestCaseIPCount = 35;	
	
	//testSteps Sheet variables
	//int tcTestSuiteName = 2;
	int xlTstTestCaseName = 0;
	int xlTstStepDescription = 2;
	int xlTstStepKeyword = 3;
	int xlTstStepExeFlag = 4;
	int xlTstStepReportFlag = 5;
	int xlTstStepStype = 6;
	int xlTstTestStepIPStart = 7;
	//int xlTstTestStepIPCount = 20;
	
	//bpc Sheet variables
	int xlBpcName = 0;
	int xlBpcStepDescription = 2;
	int xlBpcStepKeyword = 3;
	int xlBpcStepExeFlag = 4;
	int xlBpcStepReportFlag = 5;
	int xlBpcStepStype = 6;
	int xlBpcTestStepIPStart = 7;
	//int xlBpcTestStepIPCount = 20;
	
	//keyword sheet varables
	int xlKWType = 0;
	int xlMWKey = 1;
	int xlKWValue = 2;
	

	

}
