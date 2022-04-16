package com.infosys.UTAF;

import java.util.HashMap;
import java.util.Properties;

public class UTAFFwVars {
	public static String utafFWTCError = "";
	public static String utafFWTCComment = "";
	public static String utafFWTCName = "EMPTY";
	public static String utafFWTCID = "EMPTY";
	public static String utafFWTSID = "EMPTY";
	public static String utafFWTSEID = "EMPTY";
	public static String utafFWTSApp = "EMPTY";
	public static String utafFWTCStatus = "EMPTY";
	public static String utafFWTCFlowType = "EMPTY";
	public static String utafFWTCChannel = "EMPTY";
	public static String utafFWTCLanguage = "EMPTY";
	public static boolean utafStepFlag = false;
	public static String utafFWTCGroup = "";
	public static String utafFWTCSequence = "";
	public static String utafFWTCSession = "";
	public static String utafFWTCDefect = "";
	public static String utafFWReportTCName = "";
	public static String utafFWTCSkipFlag = "N";
	public static int utafFWGroupLen = 0;
	public static int utafFWGroupSeq = 0;
	public static String utafFWQuitFlag = "Y";
	public static String utafFWTCJIRAID = "";
	public static String utafFWTSJIRAID = "";
	
	public static String utafFWTSEmail = "";
	public static String utafFWSelfHealFlag = "Y";
	public static String utafFWScriptWaitFlag = "Y";
	public static boolean utafFWSelfHealON = true;
	
	public static String utafAPIConnectionTimeOut = "60";
	
	public static String utafFWReportTestCaseFlag = "N";
	public static String utafFWConfigEnv= "N";
	public static String utafFWConfigLang= "N";
	public static String utafFWENV = "EMPTY";
	public static String utafFWTempENV = "EMPTY";
	public static String utafFPlatform = "EMPTY";
	public static String utafFWTCReference = "EMPTY";
	public static boolean utafFWDBCase = false;
	
	public static Properties utafFWProps = new Properties();
	public static Properties utafSYSProps = new Properties();
	
	
	
	public static int utafFWImplicitWait = 10;
	public static int utafFWExplicitWait = 30;
	public static int utafFWDocReadyTimeout = 30;
	
	public static String utafFWFolderPath = "EMPTY";
	public static String utafFWSSPath = "EMPTY";
	public static String utafFWXLWorkbook = "EMPTY";
	public static String utafFWHtmlReportPath = "EMPTY";
	public static String utafFXLReportPath = "EMPTY";
	public static String utafWebDriverPath = "EMPTY";
	public static String utafFWMachineName = "EMPTY";
	
	public static String utafFWConfigFilePath = "C:\\Data\\Automation\\UTAF_Automation\\utaf_config.properties";
	public static String utafFWExeFrom = "EMPTY";
	public static boolean utafThrowException = false;
	public static String utafReportFlag = "N";
	
	
	public static int xlTcTestCaseIPCount = 35;
	public static int xlTstTestStepIPCount = 20;
	public static int xlBpcTestStepIPCount = 20;
	
	public static HashMap<String, String> envVar = new HashMap<String, String>();
	public static HashMap<String, String> inVar = new HashMap<String, String>();
	public static HashMap<String, String> outVar = new HashMap<String, String>();
	
	public static boolean prgExitFlag = false;
}
