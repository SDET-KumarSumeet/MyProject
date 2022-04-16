package com.infosys.UTAF;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.infosys.UTAF.extend.UTAFAppFunctions;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import jxl.Sheet;
import jxl.Workbook;

/*************************************************************************************
 * Class : UTAFRead Purpose : This class is driver for seting up the execution.
 * input data
 **************************************************************************************/
public class UTAFRead2 extends UTAFDriverBridge2 implements xlColMapper {
	public static HashMap<String, String> mapOR = new HashMap<String, String>();
	public static HashMap<String, String> runTimeVar = new HashMap<String, String>();
	public static HashMap<String, String> hmConfigProps = new HashMap<String, String>();
	public static String testCaseError, currTestCaseName, currTestCaseId, currServiceID, htmlReportPath,
			excelReportPath;
	public static String testCaseStatus = "none", testCasePrint = "none";
	public static String screenshotPath = "";
	public static ExtentReports extent;
	public static ExtentTest test;
	public static boolean tempFlag=true;
	public final static String vReadOnlyPath=  "//ap9181/utafreadonly/";
	public final static String vUtafMachinePath=  "//ap9181/utafmachinelog/";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		/*long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(beforeUsedMem);*/
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date testCaseStartTime = new Date();
		Date testCaseEndTime = new Date();
		boolean tcFound = false;
		// Get Hostname
		java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
		UTAFFwVars.utafFWMachineName = localMachine.getHostName();
					
		System.setProperty("tempLogPath", vUtafMachinePath + "/utaf_"+ UTAFFwVars.utafFWMachineName +"_logs.log");
		PropertyConfigurator.configure( vReadOnlyPath +"log4j.properties");
		try {
			
			UTAFFwVars.utafFWConfigFilePath = args[0];
			UTAFLog.info("Config properties path : -" + UTAFFwVars.utafFWConfigFilePath);
			UTAFLog.debug("Hostname of local machine: " + localMachine.getHostName());

		} catch (Exception ex) {
			UTAFFwVars.utafFWConfigFilePath = "C:\\Data\\Automation\\UTAF_Automation\\UTAF_CONFIG.properties";
			UTAFLog.warn("Config properties path was not passed from command line, setting up default path : " + UTAFFwVars.utafFWConfigFilePath);
		}
		
		try {
			
			UTAFFwVars.utafFWProps = UTAFCommonFunctions2.cfGlReadPropFile(UTAFFwVars.utafFWProps,
					UTAFFwVars.utafFWConfigFilePath);
			Properties utafBaseFWProps = new Properties();
			
			utafBaseFWProps = UTAFCommonFunctions2.cfGlReadPropFile(utafBaseFWProps,
					vReadOnlyPath + "templates/Base.properties");
			
			
			UTAFFwVars.utafFWProps.putAll(utafBaseFWProps);
			UTAFFwVars.utafSYSProps = System.getProperties();

			setFrameworkVariables();
			
			UTAFFwVars.utafFWExeFrom = cfGlGetFrameworkProperty("EXE_SRC");
			UTAFLog.debug("Execution Source : " + UTAFFwVars.utafFWExeFrom);

			// Getting the project path variable from properties file
			UTAFFwVars.utafFWFolderPath = UTAFFwVars.utafFWProps.getProperty("PROJECT.PATH");
			UTAFLog.debug("Project Path :" + UTAFFwVars.utafFWFolderPath);
			
			UTAFFwVars.utafFXLReportPath = UTAFFwVars.utafFWFolderPath
					+ UTAFFwVars.utafFWProps.getProperty("REPORT.SHEET");
			UTAFFwVars.utafFWHtmlReportPath = UTAFFwVars.utafFWFolderPath;
					//+ UTAFFwVars.utafFWProps.getProperty("REPORT.PATH");
			UTAFFwVars.utafFWENV = cfGlGetFrameworkProperty("ENV");
			
			
			DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
			Date dateobj = new Date();
			
			if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
				
				//UTAFInteractions.cfUTAFRetTCName();
				UTAFInteractions.cfUTAFVDIHealthCheck(UTAFFwVars.utafFWMachineName);
				UTAFInteractions.cfUTAFRetTCNameRest();
				if(!UTAFFwVars.utafFWDBCase){
					String vShadow = UTAFInteractions.cfUTAFGetShadowTestCase(UTAFFwVars.utafFWMachineName);
					if(!vShadow.contains("not"))
					{
						UTAFInteractions.cfUTAFRetTCNameRest();
					}
				if (!UTAFFwVars.utafFWDBCase) {
					UTAFInteractions.cfUTAFVDIValuesUpdate(UTAFFwVars.utafFWMachineName, "NA", "0", "0", "Y");
					throw new Exception("No Test Case for execution");
				}
				}
				
			} else {
				UTAFFwVars.utafFWTCName = "Empty";
				UTAFFwVars.utafFWTCReference = "Empty";
				if(UTAFFwVars.utafFWProps.containsKey("Language"))
					
				UTAFFwVars.utafFWTCLanguage = UTAFFwVars.utafFWProps.getProperty("Language");
				else{
					UTAFFwVars.utafFWTCLanguage ="EN";
				}
				UTAFRead2.runTimeVar.put("utafFWTCLang", UTAFFwVars.utafFWTCLanguage);
				UTAFFwVars.utafFWTSEID = "" +df.format(dateobj);
			}
			

			String vConfigEnv="";
			if(UTAFFwVars.utafFWConfigEnv.equalsIgnoreCase("Y"))
				vConfigEnv= UTAFFwVars.utafFWENV + ".";
			
			
			
			UTAFFwVars.utafFWXLWorkbook = UTAFFwVars.utafFWFolderPath
					+ cfGlGetFrameworkProperty(vConfigEnv + "EXCEL.SHEET");
			UTAFLog.debug(UTAFFwVars.utafFWFolderPath + cfGlGetFrameworkProperty(vConfigEnv + "EXCEL.SHEET"));
			
			String reportPath = UTAFFwVars.utafFWHtmlReportPath + "Reports\\" + UTAFFwVars.utafFWTSEID + "\\UTAF_REPORT.html";
			


			if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
				UTAFFwVars.utafFWHtmlReportPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
						+ UTAFFwVars.utafFWTSEID  + "\\" + UTAFFwVars.utafFWTCReference + "_" +  UTAFFwVars.utafFWTCName + ".html";
				
			} else if(UTAFFwVars.utafFWProps.containsKey("SCREENSHOT.PATH")){
				UTAFFwVars.utafFWHtmlReportPath = UTAFFwVars.utafFWProps.getProperty("SCREENSHOT.PATH")
						+ UTAFFwVars.utafFWTSEID + "\\UTAF_REPORT.html";
			}else {
				UTAFFwVars.utafFWHtmlReportPath = reportPath;
			}
			
			extent = new ExtentReports(UTAFFwVars.utafFWHtmlReportPath, true);
			extent.addSystemInfo("Environment", UTAFFwVars.utafFWENV).addSystemInfo("User",
					UTAFFwVars.utafSYSProps.getProperty("user.name"));
			extent.loadConfig(new File(UTAFFwVars.utafFWFolderPath + "extent-config.xml"));
			UTAFLog.info(UTAFFwVars.utafFWHtmlReportPath);
			
			if((UTAFFwVars.utafFWTCLanguage.equalsIgnoreCase("EMPTY") || UTAFFwVars.utafFWTCLanguage.isEmpty() 
					|| UTAFFwVars.utafFWTCLanguage == null || UTAFFwVars.utafFWTCLanguage == ""))
			UTAFCommonFunctions2.setPropFile(
					UTAFFwVars.utafFWFolderPath + cfGlGetFrameworkProperty( vConfigEnv + "ELEMENT.PROPERTIES.FILE"));
			else
				{UTAFCommonFunctions2.setPropFile(
						UTAFFwVars.utafFWFolderPath + cfGlGetFrameworkProperty(vConfigEnv + "ELEMENT.PROPERTIES.FILE." + UTAFFwVars.utafFWTCLanguage.toUpperCase()) );
				if(UTAFFwVars.utafFWConfigLang.equalsIgnoreCase("Y"))
				UTAFFwVars.utafFWXLWorkbook = UTAFFwVars.utafFWFolderPath
						+ cfGlGetFrameworkProperty(vConfigEnv + "EXCEL.SHEET" + "." +UTAFFwVars.utafFWTCLanguage);
				}
			
				
			UTAFLog.info(UTAFFwVars.utafFWXLWorkbook);
			File file = new File(UTAFFwVars.utafFWXLWorkbook);

			Workbook.getWorkbook(file);
			Workbook wb = Workbook.getWorkbook(file);

			Sheet tsSheet = wb.getSheet("TestSuites");
			int tsSheetRowCount = tsSheet.getRows();
			UTAFLog.info(UTAFFwVars.utafFWXLWorkbook);
			HashMap<String, Integer> tcNameHash = new HashMap<String, Integer>();
			for (int tsSheetIterator = 1; tsSheetIterator < tsSheetRowCount; tsSheetIterator++) {
				String currTestSuiteName = tsSheet.getCell(xlTsTestSuiteName, tsSheetIterator).getContents();
				String currTestSuiteFlag = tsSheet.getCell(xlTsTestSuiteExeFlag, tsSheetIterator).getContents();
				if (currTestSuiteFlag.equalsIgnoreCase("y")) {
					
					Sheet tcSheet = wb.getSheet("TestCases");
					int tcSheetRowCount = tcSheet.getRows();
					int tcSheetColCount = tcSheet.getColumns();
					if(tcNameHash.isEmpty()){
						for (int tcSheetIterator = 1; tcSheetIterator < tcSheetRowCount; tcSheetIterator++) {
							tcNameHash.put(tcSheet.getCell(xlTcID, tcSheetIterator).getContents(), tcSheetIterator);
						}
					}
					int startRow = 1;
					if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
						//startRow=tcNameHash.get(UTAFFwVars.utafFWTCName);
					}
					
					for (int tcSheetIterator = startRow; tcSheetIterator < tcSheetRowCount; tcSheetIterator++) {
						currTestCaseId = tcSheet.getCell(xlTcID, tcSheetIterator).getContents();
						currTestCaseName = tcSheet.getCell(xlTcTestCaseName, tcSheetIterator).getContents();
						String currTestCaseFlag = tcSheet.getCell(xlTcTestCaseExeFlag, tcSheetIterator).getContents();
						if ((currTestCaseId.equalsIgnoreCase(UTAFFwVars.utafFWTCName)
								|| currTestCaseFlag.equalsIgnoreCase("y") ) && (tcSheet.getCell(1, tcSheetIterator)
										.getContents().equalsIgnoreCase(currTestSuiteName))) {
							
							try{
								//UTAFFwVars.utafFWTCName=currTestCaseId;
								testCaseStartTime = new Date();
							tcFound = true;
							UTAFFwVars.utafFWTCSkipFlag = "N";
							runTimeVar = new HashMap<String, String>();
							cfGlSerPreCondition("Preconditons");
							UTAFFwVars.envVar = new HashMap<String, String>();
							UTAFFwVars.outVar = new HashMap<String, String>();
							UTAFFwVars.inVar = new HashMap<String, String>();
							runTimeVar.put("utafFWTSEID",UTAFFwVars.utafFWTSEID);
							runTimeVar.put("utafFWTCName",currTestCaseId);
							UTAFRead2.runTimeVar.put("utafFWTCLang", UTAFFwVars.utafFWTCLanguage);
							
							UTAFFwVars.utafFWTCError ="";
							UTAFFwVars.utafFWTCComment = "";
//							if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
//								UTAFFwVars.utafFWHtmlReportPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
//										+ UTAFFwVars.utafFWTSEID  + "\\" + UTAFFwVars.utafFWTCName + ".log";
//								System.setProperty("tempLogPath", UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
//										+ UTAFFwVars.utafFWTSEID  + "\\" + UTAFFwVars.utafFWTCName + ".log");
//							}
//							else
							

							if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
								System.setProperty("tempLogPath", UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
										+ UTAFFwVars.utafFWTSEID  + "\\" + UTAFFwVars.utafFWTCReference + "_" +  UTAFFwVars.utafFWTCName + ".log");
							}else{
							System.setProperty("tempLogPath", UTAFFwVars.utafFWFolderPath + "logs\\" + currTestCaseId
									+ df.format(dateobj) + ".log");}
							PropertyConfigurator.configure(vReadOnlyPath + "log4j.properties");
							//PropertyConfigurator.configure("log4j.properties");
							UTAFLog.info("Using UTAF : 1.0.93");
							UTAFLog.info("Starting the execution of : " + currTestCaseName);
							UTAFLog.info("Starting the execution of : " + currTestCaseId);
							
							if(UTAFFwVars.utafFWReportTestCaseFlag.equalsIgnoreCase("Y"))
								UTAFFwVars.utafFWReportTCName = currTestCaseId + "_" + currTestCaseName;
							else
								UTAFFwVars.utafFWReportTCName = currTestCaseId;
							
							test = extent.startTest(UTAFFwVars.utafFWReportTCName );
							DateFormat df1 = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
							Date dateobj1 = new Date();
							if (!UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
								UTAFFwVars.utafFWTCReference = "TC_" + df1.format(dateobj1);
							}
							int currTestCaseStartRow = Integer
									.parseInt(tcSheet.getCell(xlTcTestCaseStartRow, tcSheetIterator).getContents());
							int currTestCaseStepCount = Integer
									.parseInt(tcSheet.getCell(xlTcTestCaseStepCount, tcSheetIterator).getContents());
							String[] currTestCaseIP = new String[UTAFFwVars.xlTcTestCaseIPCount];

							for (int tcIPIterator = xlTcTestCaseIPStart; tcIPIterator < UTAFFwVars.xlTcTestCaseIPCount
									+ xlTcTestCaseIPStart; tcIPIterator++) {
								currTestCaseIP[tcIPIterator - xlTcTestCaseIPStart] = tcSheet
										.getCell(tcIPIterator, tcSheetIterator).getContents();
								String testCaseIP_Updated = currTestCaseIP[tcIPIterator - xlTcTestCaseIPStart];
								/*
								 * Method to get TCIP data from DB Test Case to
								 * be failed if no data present in the DB
								 */
								if (testCaseIP_Updated.contains("xutaf_data")) {
									UTAFUdmBridge.udm_getValue(testCaseIP_Updated);
									if (testCaseIP_Updated.equalsIgnoreCase("fail")) {
										UTAFFwVars.utafFWTCStatus = "FAIL";
										System.out.println("Test Case Failed due to unavailability of data in DB : "
												+ currTestCaseIP[tcIPIterator - xlTcTestCaseIPStart]);
										System.out.println("Test Case Status : " + UTAFFwVars.utafFWTCStatus);
										test.log(LogStatus.FAIL, "Test step failed for : Get Data from DB");
										test.log(LogStatus.FAIL, "with error as  : Data Unavailability for - "
												+ currTestCaseIP[tcIPIterator - xlTcTestCaseIPStart]);
										closeTestCase();
									}
									System.out.println(runTimeVar.get(testCaseIP_Updated));
									runTimeVar.put("TCIP_" + (tcIPIterator - xlTcTestCaseIPStart + 1),
											runTimeVar.get(testCaseIP_Updated));
								} else {
									runTimeVar.put("TCIP_" + (tcIPIterator - xlTcTestCaseIPStart + 1),
											testCaseIP_Updated);
								}

							}

							Sheet tsSSheet = wb.getSheet("TestSteps");
							Sheet bpcSheet = wb.getSheet("BPCs");

							int tsSSheetColCount = tsSSheet.getColumns();

							for (int tsSSheetIterator = currTestCaseStartRow
									- 1; tsSSheetIterator < (currTestCaseStartRow + currTestCaseStepCount
											- 1); tsSSheetIterator++) {
								UTAFFwVars.utafReportFlag = "N";
								UTAFFwVars.utafFWTCError = "";
								String currTestStepType = tsSSheet.getCell(xlTstStepStype, tsSSheetIterator)
										.getContents();
								String currTestStepUDF = tsSSheet.getCell(xlTstStepKeyword, tsSSheetIterator)
										.getContents();
								String currTestStepDescription = tsSSheet
										.getCell(xlTstStepDescription, tsSSheetIterator).getContents();
								runTimeVar.put("utafFWTCStep",currTestStepDescription);
								String currTestStepReport = tsSSheet.getCell(xlTstStepReportFlag, tsSSheetIterator)
										.getContents();
								String currTestStepExec = tsSSheet.getCell(xlTstStepExeFlag, tsSSheetIterator)
										.getContents();
								String[] currTestStepIP = new String[UTAFFwVars.xlTstTestStepIPCount];

								for (int tsSIPIterator = xlTstTestStepIPStart; tsSIPIterator < UTAFFwVars.xlTstTestStepIPCount
										+ xlTstTestStepIPStart; tsSIPIterator++) {
									

									if (!tsSSheet.getCell(tsSIPIterator, tsSSheetIterator).getContents().isEmpty()) {
										try{
										byte[] utf823 = currTestStepIP[tsSIPIterator - xlTstTestStepIPStart].getBytes("UTF-8");
										currTestStepIP[tsSIPIterator - xlTstTestStepIPStart] = new String(utf823, "UTF-8");
									    System.out.println(currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]);}
										catch(Exception ex){
											
										}
										currTestStepIP[tsSIPIterator - xlTstTestStepIPStart] = tsSSheet
												.getCell(tsSIPIterator, tsSSheetIterator).getContents();
										if (currTestStepIP[tsSIPIterator - xlTstTestStepIPStart].contains("::")) {
											currTestStepIP[tsSIPIterator - xlTstTestStepIPStart] = retRunTimeVar(
													currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]);
										}
										if (currTestStepIP[tsSIPIterator - xlTstTestStepIPStart].contains("${")) {
											currTestStepIP[tsSIPIterator - xlTstTestStepIPStart] = UTAFCommonFunctions2
													.replaceString(
															currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]);
										}

										System.out.println(currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]);
										System.out.println(tsSIPIterator);
										if (currTestStepIP[tsSIPIterator - xlTstTestStepIPStart].contains("UTAFENV")) {
											if (currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]
													.contains("UTAFPropValue")) {
												String[] str = currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]
														.split("UTAFPropValue");
												String temVar = str[1].replace("UTAFENV", UTAFFwVars.utafFWENV);
												currTestStepIP[tsSIPIterator - xlTstTestStepIPStart] = "UTAFPropValue"
														.concat(cfGlGetFrameworkProperty(temVar));
											} else {
												String temVar = currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]
														.replace("UTAFENV", UTAFFwVars.utafFWENV);
												currTestStepIP[tsSIPIterator
														- xlTstTestStepIPStart] = cfGlGetFrameworkProperty(temVar);
											}
										}
										if (currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]
												.contains("UTAFPropValue")) {
											currTestStepIP[tsSIPIterator
													- xlTstTestStepIPStart] = currTestStepIP[tsSIPIterator
															- xlTstTestStepIPStart].replace("UTAFPropValue", "");
											runTimeVar.put("UTAFPropValue",
													currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]
															.replace("UTAFPropValue", ""));
										}
										if (currTestStepIP[tsSIPIterator
                                                              - xlTstTestStepIPStart].contains("_TEXT_")) {                                                                        
											currTestStepIP[tsSIPIterator
                                                              - xlTstTestStepIPStart] = UTAFCommonFunctions2.cfGlGetElementProperty(currTestStepIP[tsSIPIterator- xlTstTestStepIPStart]);
										} 
										runTimeVar.put("TSIP_" + (tsSIPIterator - xlTstTestStepIPStart + 1),
												currTestStepIP[tsSIPIterator - xlTstTestStepIPStart]);
									}
								}
								System.out.println("This is current row : " + tsSSheetIterator);
								System.out.println("This is current stype " + currTestStepUDF);

								if ((currTestStepType.equalsIgnoreCase("BPC")
										|| currTestStepType.equalsIgnoreCase("Case"))
										&& currTestStepExec.equalsIgnoreCase("Y")) {
									
									int bpcStartRow = 0, bpcStepCount = 0;
									
									if (currTestStepType.equalsIgnoreCase("Case")) {
										String steps = bpcCount(file, currTestStepIP[0]); // "bpcLogin");
										String[] bpcDetails = steps.split("_");
										bpcStartRow = Integer.parseInt(bpcDetails[0]);
										bpcStepCount = Integer.parseInt(bpcDetails[1]);
										System.out.println(bpcStartRow + "," + bpcStepCount);
									} else if (currTestStepType.equalsIgnoreCase("BPC")) {
										String steps = bpcCount(file, currTestStepUDF); // "bpcLogin");
										String[] bpcDetails = steps.split("_");
										bpcStartRow = Integer.parseInt(bpcDetails[0]);
										bpcStepCount = Integer.parseInt(bpcDetails[1]);
									} else {
										bpcStartRow = Integer.parseInt(currTestStepIP[0]);
										bpcStepCount = Integer.parseInt(currTestStepIP[1]);
									}
									for (int bpcRowIterator = bpcStartRow - 1; bpcRowIterator < (bpcStartRow
											+ bpcStepCount - 1); bpcRowIterator++) {
										String currBPCTestStepType = bpcSheet.getCell(xlBpcStepStype, bpcRowIterator)
												.getContents();
										String currBPCTestStepUDF = bpcSheet.getCell(xlBpcStepKeyword, bpcRowIterator)
												.getContents();
										String currBPCTestStepDescription = bpcSheet
												.getCell(xlBpcStepDescription, bpcRowIterator).getContents();
										runTimeVar.put("utafFWTCStep",currBPCTestStepDescription);
										String currBPCTestStepReport = bpcSheet
												.getCell(xlBpcStepReportFlag, bpcRowIterator).getContents();
										String currBPCTestStepExec = bpcSheet.getCell(xlBpcStepExeFlag, bpcRowIterator)
												.getContents();
										try{
										if (currBPCTestStepDescription
												.contains("${")) {
											currBPCTestStepDescription = UTAFCommonFunctions2.replaceString(
													currTestStepDescription);
										}}catch(Exception ex){}
										String[] currBPCTestStepIP = new String[UTAFFwVars.xlBpcTestStepIPCount];

										for (int bpcIPIterator = xlBpcTestStepIPStart; bpcIPIterator < bpcSheet
												.getColumns(); bpcIPIterator++) {
											if (!bpcSheet.getCell(bpcIPIterator, bpcRowIterator).getContents()
													.isEmpty()) {
												currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart] = bpcSheet
														.getCell(bpcIPIterator, bpcRowIterator).getContents();
												System.out.println(
														currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]);

												if (currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
														.contains("::")) {
													currBPCTestStepIP[bpcIPIterator
															- xlBpcTestStepIPStart] = retRunTimeVar(
																	currBPCTestStepIP[bpcIPIterator
																			- xlBpcTestStepIPStart]);
												}
												if (currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
														.contains("${")) {
													currBPCTestStepIP[bpcIPIterator
															- xlBpcTestStepIPStart] = UTAFCommonFunctions2
																	.replaceString(currBPCTestStepIP[bpcIPIterator
																			- xlBpcTestStepIPStart]);
												}
												if (currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
														.contains("UTAFENV")) {
													if (currBPCTestStepIP[bpcIPIterator - xlTstTestStepIPStart]
															.contains("UTAFPropValue")) {
														String[] str = currBPCTestStepIP[bpcIPIterator - xlTstTestStepIPStart]
																.split("UTAFPropValue");
														String temVar = str[1].replace("UTAFENV", UTAFFwVars.utafFWENV);
														currBPCTestStepIP[bpcIPIterator - xlTstTestStepIPStart] = "UTAFPropValue"
																.concat(cfGlGetFrameworkProperty(temVar));
													} else {
														String temVar = currBPCTestStepIP[bpcIPIterator - xlTstTestStepIPStart]
																.replace("UTAFENV", UTAFFwVars.utafFWENV);
														currBPCTestStepIP[bpcIPIterator
																- xlTstTestStepIPStart] = cfGlGetFrameworkProperty(temVar);
													}/*
													String temVar = currBPCTestStepIP[bpcIPIterator
															- xlBpcTestStepIPStart].replace("UTAFENV",
																	UTAFFwVars.utafFWENV);
													currBPCTestStepIP[bpcIPIterator
															- xlBpcTestStepIPStart] = UTAFFwVars.utafFWProps
																	.getProperty(temVar);*/
												}
												if (currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
														.contains("UTAFPropValue")) {
													runTimeVar.put("UTAFPropValue",
															currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
																	.replace("UTAFPropValue", ""));
													currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
														= currBPCTestStepIP[bpcIPIterator - xlBpcTestStepIPStart]
															.replace("UTAFPropValue", "");

												}
												if (currBPCTestStepIP[bpcIPIterator
                                                                      - xlBpcTestStepIPStart].contains("_TEXT_")) {                                                                        
													currBPCTestStepIP[bpcIPIterator
                                                                      - xlBpcTestStepIPStart] = UTAFCommonFunctions2.cfGlGetElementProperty(currBPCTestStepIP[bpcIPIterator- xlBpcTestStepIPStart]);
												}
												

												

											}
										}
										if (currBPCTestStepExec.equalsIgnoreCase("Y")) {
											UTAFFwVars.utafReportFlag = "N";
											UTAFFwVars.utafFWTCError = "";
											UTAFLog.info("This is current row  in BPC: " + bpcRowIterator);
											UTAFLog.info("------------------------------------------------");
											UTAFLog.info("Step Name is : " + currBPCTestStepDescription);
											testCasePrint = "none";
											testCaseStatus = "none";
											UTAFFwVars.utafFWSSPath = "EMPTY";
											System.out.println(UTAFFwVars.utafFWTCStatus
													+ " --------before reusable function-----------------------------------");
											
											String[] currBPCTestStepReportList = currBPCTestStepReport.split(",");
											UTAFFwVars.utafReportFlag = currBPCTestStepReportList[0].trim();
											UTAFFwVars.utafFWTCError ="";
											reusableFunctions(currBPCTestStepUDF, currBPCTestStepIP);
											System.out.println(UTAFFwVars.utafFWTCStatus
													+ " --------after reusable function-----------------------------------");
											
											if (currBPCTestStepReportList[0].equalsIgnoreCase("Y")) {
												if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
													if (currBPCTestStepReportList[1].equalsIgnoreCase("Y")) {
														UTAFCommonFunctions2.captureScreenshot("", driverobj);
														test.log(LogStatus.FAIL, currBPCTestStepDescription + " --> \n" +UTAFFwVars.utafFWTCError,
																test.addScreenCapture(UTAFFwVars.utafFWSSPath));
														UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", currBPCTestStepDescription + " -- " +UTAFFwVars.utafFWTCError,UTAFFwVars.utafFWSSPath);
														//UTAFInteractions.cfUTAFUpdateTestSteps(currBPCTestStepDescription,UTAFFwVars.utafFWTCStatus,UTAFFwVars.utafFWSSPath);
													} else {
														test.log(LogStatus.FAIL, currBPCTestStepDescription + " --> \n" + UTAFFwVars.utafFWTCError, "");
														UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", currBPCTestStepDescription + " -- " +UTAFFwVars.utafFWTCError,"");
														//UTAFInteractions.cfUTAFUpdateTestSteps(currBPCTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
													}
													System.out.println(
															"--------To break from BPC-----------------------------------");

													break;
												} else if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("skip")) {
													System.out.println(
															"--------Skipped step-----------------------------------");
													test.log(LogStatus.SKIP, currBPCTestStepDescription,"");
													UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "SKIP", currBPCTestStepDescription,"");
													break;
													//UTAFInteractions.cfUTAFUpdateTestSteps(currBPCTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
												} else {
													if (currBPCTestStepReportList[1].equalsIgnoreCase("Y")) {
														UTAFCommonFunctions2.captureScreenshot("", driverobj);
														test.log(LogStatus.PASS, currBPCTestStepDescription,
																test.addScreenCapture(UTAFFwVars.utafFWSSPath));
														UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", currBPCTestStepDescription,UTAFFwVars.utafFWSSPath);
														//UTAFInteractions.cfUTAFUpdateTestSteps(currBPCTestStepDescription,UTAFFwVars.utafFWTCStatus,UTAFFwVars.utafFWSSPath);
													} else {
														test.log(LogStatus.PASS,
																 currBPCTestStepDescription);
														UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", currBPCTestStepDescription,"");
														//UTAFInteractions.cfUTAFUpdateTestSteps(currBPCTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
													}
												}
											} else if (currBPCTestStepReportList[0].equalsIgnoreCase("N")
													&& UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
												break;
											}
											else if (currBPCTestStepReportList[0].equalsIgnoreCase("N")
													&& UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("SKIP")) {
												break;
											}
											System.out.println("-------------------------------------------");
										}

									}
									
									if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
										System.out.println(
												"-------------------To break from test steps------------------------");
										break;
									}
									else if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("skip")) {
										System.out.println(
												"-------------------To break from test steps------------------------");
										break;
									}

								} else {
									if (currTestStepExec.equalsIgnoreCase("Y")) {
										System.out.println("This is current row : " + tsSSheetIterator);
										System.out.println("-------------------------------------------");
										System.out.println("Step Name is : " + currTestStepDescription);
										testCasePrint = "none";
										UTAFFwVars.utafFWTCStatus = "Empty";
										UTAFFwVars.utafFWSSPath = "EMPTY";
										// -------------------------Runnable----------------------
										String[] currTestStepReportList = currTestStepReport.split(",");
										UTAFFwVars.utafReportFlag = currTestStepReportList[0].trim();
										UTAFFwVars.utafFWTCError ="";
										reusableFunctions(currTestStepUDF, currTestStepIP);
										System.out.println("testcase status" + UTAFFwVars.utafFWTCStatus);
										
										if (currTestStepReportList[0].equalsIgnoreCase("Y")) {
											if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
												if (currTestStepReportList[1].equalsIgnoreCase("Y")) {
													UTAFCommonFunctions2.captureScreenshot("", driverobj);
													test.log(LogStatus.FAIL, currTestStepDescription + " --> \n" + UTAFFwVars.utafFWTCError,
															test.addScreenCapture(UTAFFwVars.utafFWSSPath));
													UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", currTestStepDescription + " -- " + UTAFFwVars.utafFWTCError,UTAFFwVars.utafFWSSPath);
													//UTAFInteractions.cfUTAFUpdateTestSteps(currTestStepDescription,UTAFFwVars.utafFWTCStatus,UTAFFwVars.utafFWSSPath);
												} else {
													test.log(LogStatus.FAIL, currTestStepDescription + " --> \n" +UTAFFwVars.utafFWTCError,"");
													UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", currTestStepDescription + " -- " + UTAFFwVars.utafFWTCError,"");
													//UTAFInteractions.cfUTAFUpdateTestSteps(currTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
												}
												break;
											} else if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("skip")) {
												System.out.println(
														"--------Skipped step-----------------------------------");
												test.log(LogStatus.SKIP, currTestStepDescription,"");
												UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "SKIP", currTestStepDescription,"");
												break;
												//UTAFInteractions.cfUTAFUpdateTestSteps(currTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
											} else {
												if (currTestStepReportList[1].equalsIgnoreCase("Y")) {
													UTAFCommonFunctions2.captureScreenshot("", driverobj);
													test.log(LogStatus.PASS, currTestStepDescription,
															test.addScreenCapture(UTAFFwVars.utafFWSSPath));
													UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", currTestStepDescription,UTAFFwVars.utafFWSSPath);
													//UTAFInteractions.cfUTAFUpdateTestSteps(currTestStepDescription,UTAFFwVars.utafFWTCStatus,UTAFFwVars.utafFWSSPath);
												} else {
													test.log(LogStatus.PASS, currTestStepDescription,"");
													UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", currTestStepDescription,"");
													//UTAFInteractions.cfUTAFUpdateTestSteps(currTestStepDescription,UTAFFwVars.utafFWTCStatus,"");
												}
											}
										} else if (currTestStepReportList[0].equalsIgnoreCase("N")
												&& UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
											break;
										}
										else if (currTestStepReportList[0].equalsIgnoreCase("N")
												&& UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("skip")) {
											break;
										}
										System.out.println("-------------------------------------------");
									}
								}
							}
							System.out.println("********************** Test Case Status :  " + UTAFFwVars.utafFWTCStatus
									+ " *******************");
							
							boolean quitFlag = true;
							if(UTAFFwVars.utafFWTCSession.equalsIgnoreCase("Y")){
								quitFlag = false;
							}else if(UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("EXCEL") && UTAFFwVars.utafFWQuitFlag.equalsIgnoreCase("N") ){
								quitFlag = false;
							}
							//check dependecny flag and order
							//assignt test case
							//
							if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("SKIP")) {
								try{
								reusableFunctions("afTearDown", currTestCaseIP);}catch(Exception ex){
									UTAFLog.warn("afTearDown call : " + ex.getMessage());
								}
								if (extent != null)
									extent.endTest(test);
								if (driverobj != null && quitFlag) {
									driverobj.quit();
								}
								if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
									try{
									//UTAFInteractions.cfUTAFUpdateTCStatus("Completed", UTAFFwVars.utafFWTCReference);
									UTAFInteractions.cfUTAFUpdateTCStatusRest("SKIPPED", UTAFFwVars.utafFWTCReference);
									//UTAFInteractions.cfUTAFUpdateTestSteps(UTAFFwVars.utafFWTCError, "PASS","");
									}catch(Exception ex){
										UTAFLog.debug(ex.getMessage());
									}
									
								}
							} else if(UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("FAIL")){
								try{
									reusableFunctions("afTearDown", currTestCaseIP);}catch(Exception ex){
										UTAFLog.warn("afTearDown call : " + ex.getMessage());
									}
								if (extent != null)
								{
								extent.endTest(test);}
							if (driverobj != null && quitFlag) {
								driverobj.quit();
							}
							if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
								//UTAFInteractions.cfUTAFUpdateTCStatus("Failed", UTAFFwVars.utafFWTCReference);
								try{
								UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
								//UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", UTAFFwVars.utafFWTCError,"");
								//UTAFInteractions.cfUTAFUpdateTestSteps(UTAFFwVars.utafFWTCError, "FAIL","");
									}catch(Exception ex){
										UTAFLog.debug(ex.getMessage());
									}
									
								//UTAFInteractions.cfUTAFCheckFailedTestcase(UTAFFwVars.utafFWTCReference,UTAFFwVars.utafFWTSID,UTAFFwVars.utafFWTSEID);
							}
							}else {
								try{
									reusableFunctions("afTearDown", currTestCaseIP);}catch(Exception ex){
										UTAFLog.warn("catch afTearDown call : " + ex.getMessage());
									}
								if (extent != null)
									extent.endTest(test);
								if (driverobj != null && quitFlag) {
									driverobj.quit();
								}
								if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
									try{
									//UTAFInteractions.cfUTAFUpdateTCStatus("Completed", UTAFFwVars.utafFWTCReference);
									UTAFInteractions.cfUTAFUpdateTCStatusRest("PASSED", UTAFFwVars.utafFWTCReference);
									//UTAFInteractions.cfUTAFUpdateTestSteps(UTAFFwVars.utafFWTCError, "PASS","");
									}catch(Exception ex){
										UTAFLog.debug(ex.getMessage());
									}
									
								}

							}
							//reusableFunctions("afEndProcess",);
						}catch(Exception ex){
							UTAFLog.fatal(ex.getMessage());
							if (extent != null)
								{test.log(LogStatus.FAIL, ex.getMessage(),"");
									extent.endTest(test);}
								if (driverobj != null && UTAFFwVars.utafFWTCSession.equalsIgnoreCase("N")) {
									driverobj.quit();
								}
								if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
									//UTAFInteractions.cfUTAFUpdateTCStatus("Failed", UTAFFwVars.utafFWTCReference);
									try{
									UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
									UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", ex.getMessage(),"");
									//UTAFInteractions.cfUTAFUpdateTestSteps(UTAFFwVars.utafFWTCError, "FAIL","");
										}catch(Exception ex2){
											UTAFLog.debug(ex2.getMessage());
										}
										
									//UTAFInteractions.cfUTAFCheckFailedTestcase(UTAFFwVars.utafFWTCReference,UTAFFwVars.utafFWTSID,UTAFFwVars.utafFWTSEID);
								}
						}
							finally{
								
							}}
						//String vShadow = UTAFInteractions.cfUTAFGetShadowTestCase(UTAFFwVars.utafFWMachineName);
						
					}
				}
				if(tsSheetRowCount == (tsSheetIterator+1))
				{
					if(UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")&& (!tcFound) && (!UTAFFwVars.utafFWTCReference.equalsIgnoreCase("EMPTY"))){
						UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCName + " is not available in sheet ";
						UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", UTAFFwVars.utafFWTCName + " is not available in sheet ","");
						UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
					}
				}
				if((tsSheetRowCount == (tsSheetIterator+1))&& UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")&& (tcFound || UTAFFwVars.utafFWGroupSeq != 0)){
					testCaseEndTime = new Date();
					SendEmail.sendReportByMail(UTAFFwVars.utafFWHtmlReportPath, currTestCaseId);
					boolean vShadowFlag = false;
					boolean langFlagNotChange=true;
					try {
						if (UTAFFwVars.utafFWScriptWaitFlag.equalsIgnoreCase("Y")) {
							int difference2 = (int) ((testCaseEndTime.getTime() - testCaseStartTime.getTime())
									/ (1000));
							if (difference2 != 0 && difference2 < 60)
								Thread.sleep((70 - difference2) * 1000);
						}
					} catch (Exception exTime) {
					}
					
					boolean newCaseFlag = false;
					if (!(UTAFFwVars.utafFWGroupSeq > (UTAFFwVars.utafFWGroupLen - 1))
							&& !(UTAFFwVars.utafFWTCGroup.equalsIgnoreCase(""))) {
						if (!UTAFInteractions.cfUTAFGetTestsuiteAbortStatus(UTAFFwVars.utafFWTSEID)) {
							if (UTAFInteractions.cfGetCurrTestCase(UTAFFwVars.utafFWGroupSeq, true))
								UTAFFwVars.utafFWDBCase = true;
							UTAFFwVars.utafFWGroupSeq = UTAFFwVars.utafFWGroupSeq + 1;
							newCaseFlag = true;
						} else {
							UTAFFwVars.utafFWDBCase = false;
							newCaseFlag = false;
							UTAFFwVars.utafFWGroupSeq = UTAFFwVars.utafFWGroupLen;
						}
					}
					else{
							String tempLang = UTAFFwVars.utafFWTCLanguage;
							UTAFInteractions.cfSetGroupParamDefault();
							UTAFInteractions.cfUTAFRetTCNameRest();
							if(!UTAFFwVars.utafFWTCLanguage.equalsIgnoreCase(tempLang)){
								langFlagNotChange=false;
							}
							vShadowFlag=true;
						}
					
					if(UTAFFwVars.utafFWDBCase){
						
						newCaseFlag =true;
						vShadowFlag=false;
					}else{
						vShadowFlag=true;
					}
					
					if(vShadowFlag){
					String vShadow = UTAFInteractions.cfUTAFGetShadowTestCase(UTAFFwVars.utafFWMachineName);
					if(!vShadow.contains("not"))
					{
						String tempLang = UTAFFwVars.utafFWTCLanguage;
						UTAFInteractions.cfUTAFRetTCNameRest();
						if(UTAFFwVars.utafFWDBCase){
						newCaseFlag =true;}
						if(!UTAFFwVars.utafFWTCLanguage.equalsIgnoreCase(tempLang)){
							langFlagNotChange=false;
						}
						}
					}
					if(newCaseFlag && langFlagNotChange){
						if (extent != null) {
							extent.flush();
							extent.close();
						}
						
						UTAFFwVars.utafFWHtmlReportPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
									+ UTAFFwVars.utafFWTSEID  + "\\" + UTAFFwVars.utafFWTCReference + "_" +  UTAFFwVars.utafFWTCName + ".html";
						
						extent = new ExtentReports(UTAFFwVars.utafFWHtmlReportPath,true);
						extent.addSystemInfo("Environment", UTAFFwVars.utafFWENV).addSystemInfo("User",
								UTAFFwVars.utafSYSProps.getProperty("user.name"));
						extent.loadConfig(new File(UTAFFwVars.utafFWFolderPath + "extent-config.xml"));
						UTAFLog.info(UTAFFwVars.utafFWHtmlReportPath);
						tcFound=false;
						tsSheetIterator=0;
					}
				}
			}
			wb.close();
			tearDown();
			/*
			if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB") ) {
				if ((!UTAFFwVars.utafFWTCReference.equalsIgnoreCase("EMPTY")) && !tcFound) {
					UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCName + " is not available in sheet ";
				//UTAFInteractions.cfUTAFUpdateTCStatus("Failed", UTAFFwVars.utafFWTCReference);
				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL", UTAFFwVars.utafFWTCName + " is not available in sheet ","");
				UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
				} 
			} 
			*/
			//if (UTAFFwVars.utafFWProps.getProperty("SEND_MAIL_AFTER_TEST").equalsIgnoreCase("Y"))
				
		} catch (Exception ex) {
			UTAFLog.error("In catch of UTAFRead2 script : "+ ex.getMessage());
			try {
				if (UTAFFwVars.utafFWDBCase && UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
					UTAFLog.fatal(ex.getMessage() + "\n" + ex.fillInStackTrace());
					UTAFLog.fatal("Execution stopped in UTAFRead2 script : " + ex.getMessage());
					UTAFFwVars.utafFWTCError = ex.getMessage();
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL",
							UTAFFwVars.utafFWTCError + ex.getMessage(), "");
					UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
					UTAFInteractions.cfUTAFUpdateTestSuiteExecutionRest(UTAFFwVars.utafFWTSEID);
				}
			} catch (Exception ex1) {
			}
		}
		SendEmail.sendReportByMail(UTAFFwVars.utafFWHtmlReportPath, currTestCaseId);
		/*long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(afterUsedMem);
		System.out.println(beforeUsedMem-afterUsedMem);*/
		System.exit(0);
	}

	private static void tearDown() {
		try {
			if (extent != null) {
				extent.flush();
				extent.close();
			}
			if (driverobj != null) {
				driverobj.quit();
			}
		} catch (Exception ex) {
			UTAFLog.warn(ex.getMessage());
		}
		try {
			if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
				UTAFInteractions.cfUTAFVDIValuesUpdate(UTAFFwVars.utafFWMachineName, "NA", "0", "0", "Y");
				UTAFInteractions.cfUTAFUpdateVDI(UTAFFwVars.utafFWMachineName, "Y");
			}
		} catch (Exception ex) {
			UTAFLog.info(ex.getMessage());
			UTAFLog.info(ex.getStackTrace() + "");
		}
	}

	private static void closeTestCase() {
		extent.endTest(test);
	}

	public static String retRunTimeVar(String inputParam) {
		UTAFLog.debug("Inside retRunTimeVar " + inputParam);
		try{
		while (inputParam.contains("::")) {
			int indexOF = inputParam.indexOf("::");
			int spaceIndex = indexOF + inputParam.substring(indexOF).indexOf(" ") + 1;
			String runTimeVariableKey = inputParam.substring(indexOF + 2, spaceIndex);
			String newrunTimeVariableKey = runTimeVariableKey.replaceAll("\\s+", "");
			if (!runTimeVar.containsKey(newrunTimeVariableKey)) {
				runTimeVar.put(newrunTimeVariableKey, newrunTimeVariableKey);
			}
			String runTimeVariableValue = runTimeVar.get(newrunTimeVariableKey);
			inputParam = inputParam.substring(0, indexOF).concat(runTimeVariableValue)
					.concat(inputParam.substring(spaceIndex));
			UTAFLog.debug("Variable key is : " + runTimeVariableKey);
			UTAFLog.debug("Variable value is : " + runTimeVariableValue);
		}}catch(Exception ex){
			UTAFLog.error("Error in getting params"  + inputParam +  " " +  ex.getMessage());
		}
		return inputParam;
	}
	
	public static boolean utafWriteReport(String reportParams, String vMessage) {
		boolean vBreak = false;
		String[] currTestStepReportList = reportParams.split(",");
		if (currTestStepReportList[0].equalsIgnoreCase("Y")) {
			if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
				if (currTestStepReportList[1].equalsIgnoreCase("Y")) {
					UTAFCommonFunctions2.captureScreenshot("", driverobj);
					test.log(LogStatus.FAIL, vMessage,
							test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				} else {
					test.log(LogStatus.FAIL, vMessage);
				}
				vBreak = true;
			} else if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("skip")) {
				test.log(LogStatus.SKIP, vMessage);
				vBreak = true;
			} else {
				if (currTestStepReportList[1].equalsIgnoreCase("Y")) {
					UTAFCommonFunctions2.captureScreenshot("", driverobj);
					test.log(LogStatus.PASS, vMessage,
							test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				} else {
					test.log(LogStatus.PASS, vMessage);
				}
			}
		} else if (currTestStepReportList[0].equalsIgnoreCase("N")
				&& UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("fail")) {
			vBreak = true;
		}
		return vBreak;
	}

	public static void sendMailVBS() {
		try {
			String pathSendEmailVBS = UTAFFwVars.utafFWFolderPath + "SendMail.vbs";
			pathSendEmailVBS = pathSendEmailVBS.replace("\\", "/");
			Thread.sleep(2000);
			Process p = Runtime.getRuntime().exec("cmd /c start " + pathSendEmailVBS);
			p.waitFor();
			Thread.sleep(2000);
		} catch (Exception ex) {
			System.out.println("Failed in sending mail :" + ex.getMessage());
		}
		// return inputParam;
	}
	
	public static void setFrameworkVariables() {
		try {
			if (UTAFFwVars.utafFWProps.containsKey("IMPLICITWAIT.TIMEOUT"))
				UTAFFwVars.utafFWImplicitWait = Integer.parseInt(cfGlGetFrameworkProperty("IMPLICITWAIT.TIMEOUT").trim());

			if (UTAFFwVars.utafFWProps.containsKey("EXPLICITWAIT.TIMEOUT"))
				UTAFFwVars.utafFWExplicitWait = Integer.parseInt(cfGlGetFrameworkProperty("EXPLICITWAIT.TIMEOUT").trim());

			if (UTAFFwVars.utafFWProps.containsKey("DOCREADY.TIMEOUT"))
				UTAFFwVars.utafFWDocReadyTimeout = Integer.parseInt(cfGlGetFrameworkProperty("DOCREADY.TIMEOUT").trim());

			if (UTAFFwVars.utafFWProps.containsKey("xlTcTestCaseIPCount"))
				UTAFFwVars.xlTcTestCaseIPCount = Integer
						.parseInt(UTAFFwVars.utafFWProps.getProperty("xlTcTestCaseIPCount").trim());

			if (UTAFFwVars.utafFWProps.containsKey("xlTstTestStepIPCount"))
				UTAFFwVars.xlTstTestStepIPCount = Integer
						.parseInt(UTAFFwVars.utafFWProps.getProperty("xlTstTestStepIPCount").trim());

			if (UTAFFwVars.utafFWProps.containsKey("xlBpcTestStepIPCount"))
				UTAFFwVars.xlBpcTestStepIPCount = Integer
						.parseInt(UTAFFwVars.utafFWProps.getProperty("xlBpcTestStepIPCount").trim());
			
			if (UTAFFwVars.utafFWProps.containsKey("ReportTestCaseFlag"))
				UTAFFwVars.utafFWReportTestCaseFlag = UTAFFwVars.utafFWProps.getProperty("ReportTestCaseFlag").trim();
			
			if (UTAFFwVars.utafFWProps.containsKey("ConfigEnv"))
				UTAFFwVars.utafFWConfigEnv = UTAFFwVars.utafFWProps.getProperty("ConfigEnv").trim();
			
			if (UTAFFwVars.utafFWProps.containsKey("ConfigLang"))
				UTAFFwVars.utafFWConfigLang = UTAFFwVars.utafFWProps.getProperty("ConfigLang").trim();
			
			if (UTAFFwVars.utafFWProps.containsKey("QuitFlag"))
				UTAFFwVars.utafFWQuitFlag = UTAFFwVars.utafFWProps.getProperty("QuitFlag").trim();
			
			if (UTAFFwVars.utafFWProps.containsKey("SelfHealFlag"))
				UTAFFwVars.utafFWSelfHealFlag = UTAFFwVars.utafFWProps.getProperty("SelfHealFlag").trim();
			
			if (UTAFFwVars.utafFWProps.containsKey("ScriptWaitFlag"))
				UTAFFwVars.utafFWScriptWaitFlag = UTAFFwVars.utafFWProps.getProperty("ScriptWaitFlag").trim();
			if (UTAFFwVars.utafFWProps.containsKey("UTAFAPI.TIMEOUT")){
				UTAFFwVars.utafAPIConnectionTimeOut = UTAFFwVars.utafFWProps.getProperty("UTAFAPI.TIMEOUT").trim();
				UTAFLog.info("UTAF API TIME OUT SET : " + UTAFFwVars.utafAPIConnectionTimeOut);}
			
		} catch (Exception ex) {
			
		}
	}


	
	public static String bpcCount(File workbook, String bpcName) throws Exception {
		String bpcStart_Count = "";
		int bpcStartRow = 0;
		try {
			// Get the workbook instance for XLSX file
			Workbook wb = Workbook.getWorkbook(workbook);
			// Iterating to the BPCs sheet
			Sheet tsSheet = wb.getSheet("BPCs");
			// Finding the no of rows present
			int noOfRows = tsSheet.getRows();

			System.out.println(noOfRows + " No. of rows present on Datasheet");
			// Determining the column number for "BPC_Name"
			String curdataName;
			// Fetching the no of columns
			int columnNoOfBPC_Name = -1;
			// Iterating for all the columns
			for (int columnNo = 0; columnNo < noOfRows; columnNo++) {
				curdataName = tsSheet.getCell(0, columnNo).getContents();
				// System.out.println(curdataName);
				if (curdataName.equals(bpcName)) {
					columnNoOfBPC_Name = columnNo;
					// System.out.println(columnNoOfBPC_Name);
					break;
				}
			}
			if (columnNoOfBPC_Name == -1) {
				throw new Exception("Column name: " + bpcName + " not present in the sheet: 'BPCs'");
			}
			// Iterating the entire sheet for finding the bpc count
			String bpc_Name;
			int count = 0;
			bpcStartRow = columnNoOfBPC_Name;
			boolean bpcNameFound = false;
			for (int rowCount = columnNoOfBPC_Name; rowCount < noOfRows; rowCount++) {
				bpc_Name = tsSheet.getCell(0, rowCount).getContents();
				if (bpcName.equals(bpc_Name)) {
					if (!bpcNameFound) {
						bpcStartRow = rowCount;
					}
					bpcNameFound = true;
					count++;
				}
			}
			wb.close();
			bpcStartRow = bpcStartRow + 1;
			bpcStart_Count = Integer.toString(bpcStartRow);
			bpcStart_Count = bpcStart_Count + "_";
			System.out.println(bpcStart_Count + " Count " + count);
			return bpcStart_Count + count;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return bpcStart_Count + "-1";
		}

	}

}