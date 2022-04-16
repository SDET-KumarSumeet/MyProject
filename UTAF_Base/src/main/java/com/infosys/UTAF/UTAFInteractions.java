package com.infosys.UTAF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.json.JSONObject;

import com.relevantcodes.extentreports.LogStatus;

public class UTAFInteractions {
	static String[] testCaselist = null;

	public static void cfUTAFRetTCNameRest() throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFFwVars.utafFWDBCase = false;
		String value = "";
		String defect = "";
		try {
			cfSetGroupParamDefault();
			String vRestURL = UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.getTestCaseForExecution"); 
			String vRestURLUpdateStatus =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestcaseParam");
			String vRestURLUpdateTestSuite =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestSuiteExecution");
			String jsonString = "{ \"tce_status\": \"NEW\", \"tce_machine\": \"" + UTAFFwVars.utafFWMachineName	+ "\" }";
			UTAFLog.info("Befor cfUTAFRetTCNameRest ");
			String response = UTAFCommonFunctions2.cfGLRestCallDirectH(vRestURL, jsonString, "POST");
			UTAFLog.info("After cfUTAFRetTCNameRest ");
			if(response.contains("tce_Id")){
					UTAFFwVars.utafFWDBCase = true;
					JSONObject obj = new JSONObject(response);
					for (Object key : obj.keySet()) {
						// based on the key types
						String keyStr = (String) key;
						System.out.println(keyStr);
						System.out.println(obj.get(keyStr) + "");
						switch (keyStr) {
						case "ts_id":
							UTAFFwVars.utafFWTSID = (int) obj.get(keyStr) + "";
							value = value + "1";
							break;
						case "tce_Id":
							UTAFFwVars.utafFWTCReference = (int) obj.get(keyStr) + "";
							value = value + "1";
							break;
						case "tse_id":
							UTAFFwVars.utafFWTSEID = (int) obj.get(keyStr) + "";
							value = value + "1";

							break;
						case "tc_name":
							UTAFFwVars.utafFWTCName = (String) obj.get(keyStr);
							value = value + "1";
							break;
						case "ts_environment":
							UTAFFwVars.utafFWENV = (String) obj.get(keyStr);
							if(UTAFFwVars.utafFWTempENV.equalsIgnoreCase("EMPTY"))
								UTAFFwVars.utafFWTempENV = UTAFFwVars.utafFWENV;
							if(UTAFFwVars.utafFWConfigEnv.equalsIgnoreCase("Y") && !UTAFFwVars.utafFWTempENV.equalsIgnoreCase(UTAFFwVars.utafFWENV)){
								UTAFFwVars.utafFWDBCase = false;
								throw new Exception("env flag change detected");
								
							}
							value = value + "1";
							break;
						case "ts_platform":
							UTAFFwVars.utafFPlatform = (String) obj.get(keyStr);
							value = value + "1";
							break;
						case "ts_language":
							UTAFFwVars.utafFWTCLanguage = (String) obj.get(keyStr);
							value = value + "1";
							UTAFRead2.runTimeVar.put("utafFWTCLang", UTAFFwVars.utafFWTCLanguage);
							break;
						case "tc_group":
							if ((obj.get(keyStr) + "").equalsIgnoreCase("null")) {
								UTAFFwVars.utafFWTCGroup = "";
								value = value + "1";
							} else {
								UTAFFwVars.utafFWTCGroup = (String) obj.get(keyStr);
								value = value + "1";
							}
							break;
						case "tc_session":
							if ((obj.get(keyStr) + "").equalsIgnoreCase("null")) {
								UTAFFwVars.utafFWTCSession = "";
								value = value + "1";
							} else {
								UTAFFwVars.utafFWTCSession = (String) obj.get(keyStr);
								value = value + "1";
							}
							break;
						case "tc_sequence":
							if ((obj.get(keyStr) + "").equalsIgnoreCase("null")) {
								UTAFFwVars.utafFWTCSequence = "";
								value = value + "1";
							} else {
								UTAFFwVars.utafFWTCSequence = (String) (obj.get(keyStr) + "");
								value = value + "1";
							}
							break;

						case "tc_defect":
							if (!(obj.get(keyStr).equals(null)))
								defect = (String) obj.get(keyStr);
							// value = value + "1";
							break;
						case "tc_jira":
							if (!(obj.get(keyStr).equals(null))) {
								UTAFCommonFunctions2.cfGlADDRunTimeVar("varTCJIRAID", (String) obj.get(keyStr));
								UTAFFwVars.utafFWTCJIRAID = (String) obj.get(keyStr);
							}
							// value = value + "1";
							break;
						case "ts_jira_exec_id":
							if (!(obj.get(keyStr).equals(null))) {
								UTAFCommonFunctions2.cfGlADDRunTimeVar("varTSJIRAID", (String) obj.get(keyStr));
								UTAFFwVars.utafFWTSJIRAID = (String) obj.get(keyStr);
							}
							// value = value + "1";
							break;

						}
						if (value.equalsIgnoreCase("1111111111"))
							break;
					}
			vRestURLUpdateTestSuite = vRestURLUpdateTestSuite + UTAFFwVars.utafFWTSEID;
			
			if(!(UTAFFwVars.utafFWTCGroup.equalsIgnoreCase("")|| UTAFFwVars.utafFWTCGroup.equalsIgnoreCase("EMPTY"))){
				testCaselist = cfReturnTestGroup(UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH"), UTAFFwVars.utafFWTSEID, UTAFFwVars.utafFWTCGroup);
				cfGetCurrTestCase(UTAFFwVars.utafFWGroupSeq,false);
				UTAFFwVars.utafFWGroupSeq = UTAFFwVars.utafFWGroupSeq +1;
			}
			
			Calendar c = Calendar.getInstance();
			//2020-09-27 17:48:55
			String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
			// \"start_time\": \"" + todayDate + "\",
			// System.out.println();
			System.out.println("\n Test case reference : " + UTAFFwVars.utafFWTCReference + "\n" + "Test case name : "
					+ UTAFFwVars.utafFWTCName + "\n" + "Test case env  : " + UTAFFwVars.utafFWENV + "\n"
					+ "Test case platforcm : " + UTAFFwVars.utafFPlatform);
			if (value.equalsIgnoreCase("1111111111")) {
				jsonString = "{ \"tce_status\": \"IN PROGRESS\",  \"tce_start_time\": \"" + todayDate + "\",\"tce_defect\": \"" + defect + "\", \"tce_id\": " + UTAFFwVars.utafFWTCReference + " }";
				response = UTAFCommonFunctions2.cfGLRestCallDirectH(vRestURLUpdateStatus, jsonString, "PUT");
				obj = new JSONObject(response);
				for (Object key : obj.keySet()) {
					// based on the key types
					String keyStr = (String) key;
					switch (keyStr) {
					case "tce_status":
						if (((String) obj.get(keyStr)).equalsIgnoreCase("IN PROGRESS")) {
							value = "1";
						} else {
							value = "2";
						}
						break;
					}
				}
			}
			if(UTAFRead2.runTimeVar.containsKey("varTCJIRAID") && UTAFRead2.runTimeVar.containsKey("varTSJIRAID")){
				UTAFCommonFunctions2.cfGlUpdateXrayStatus("EXECUTING");
			}
			cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", "Starting Execution ","");
			if (value.equalsIgnoreCase("1")) {
				response = UTAFCommonFunctions2.cfGLRestCallDirectH(vRestURLUpdateTestSuite, jsonString, "GET");
				obj = new JSONObject(response);
				for (Object key : obj.keySet()) {
					// based on the key types
					String keyStr = (String) key;
					switch (keyStr) {
					case "status":
						if (((String) obj.get(keyStr)).equalsIgnoreCase("IN PROGRESS")) {
							value = "1";
						} else {
							value = "2";
						}
						break;
					}
				}
			}
			}
		} catch (Exception ex) {
			UTAFLog.warn(ex.getMessage());
		} finally {
			try{
				if(UTAFFwVars.utafFWDBCase)
				UTAFInteractions.cfUTAFVDIValuesUpdate(UTAFFwVars.utafFWMachineName,"Execution Started", UTAFFwVars.utafFWTCReference,UTAFFwVars.utafFWTSEID,"N");}
				catch(Exception ex){
					
				}
		}
		UTAFLog.debug("Out cfUTAFRetTCNameRest");
		}
	}

	public static void cfUTAFUpdateTCStatusRest(String vStatus, String vRef) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.info("In cfUTAFUpdateTCStatusRest");

			String value = "";
			String response = "";
			String jsonString = "";
			String vRestURLUpdateStatus = "";
			try {
				if (vStatus.equalsIgnoreCase("PASSED"))
					UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCComment;
				UTAFLog.info(UTAFFwVars.utafFWTCError);
				if (!(UTAFFwVars.utafFWTCError == null)){
				if (UTAFFwVars.utafFWTCError.contains("\n")) {
					UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.split("\n")[0];
				}
				UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.replace("\n", "");
				UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.replace("\\", "/");
				UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.replace("\"", "");
				}else{
					UTAFFwVars.utafFWTCError = "Null pointer exception";
				}
				if (UTAFFwVars.utafFWTCError.length() > 253) {
					UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.substring(0, 254);
				}
				//UTAFFwVars.utafFWTCError = "Failed to Execute javascript click on locator : previousPageButtonUnderApprovedTransactionsTable_DONE------cfGlJsElementSimpleClick : cfGlSelSimpleElementSearch : no such element: Unable to locate element: \"method\":\"xpath\",\"selector\":\".//bol-filter/following::bol-pagination/div/button[text()=Previous page]\"";
				//String s= URLEncoder.encode(UTAFFwVars.utafFWTCError, "UTF-8").replaceAll("%(..)%(..)", "%u$1$2");
				UTAFLog.info(UTAFFwVars.utafFWTCError);
				vRestURLUpdateStatus = UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestcaseParam");
				if(UTAFFwVars.utafFWTCError.equalsIgnoreCase("") || UTAFFwVars.utafFWTCError.equalsIgnoreCase(" ") )
					jsonString = "{ \"tce_status\": \"" + vStatus + "\", \"tce_id\": " + vRef + " }";
				else
					jsonString = "{ \"tce_status\": \"" + vStatus + "\", \"tce_comment\": \"" + UTAFFwVars.utafFWTCError
					+ "\", \"tce_id\": " + vRef + " }";
				response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateStatus, jsonString, "PUT");
				UTAFLog.info(response);
				JSONObject obj = new JSONObject(response);
				obj = new JSONObject(response);
				for (Object key : obj.keySet()) {
					// based on the key types
					String keyStr = (String) key;
					switch (keyStr) {
					case "tce_status":
						if (((String) obj.get(keyStr)).equalsIgnoreCase(vStatus)) {
							value = "1";
						} else {
							value = "2";
						}
						break;
					}
				}
				if (!value.equalsIgnoreCase("1")) {
					UTAFCommonFunctions2.cfGLGenericExceptionHandling(
							Thread.currentThread().getStackTrace()[1].getMethodName(),
							"No records to update in testcase_execution table for : " + vRef);
				}
				try {
					String vTextReport = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
							+ UTAFFwVars.utafFWTSEID + "/" + vRef + "_Data.txt";
					String vDataString = "";
					if(!UTAFFwVars.inVar.isEmpty()){
					for (String name: UTAFFwVars.inVar.keySet()){
							String vValue = "Null";
							if (!(UTAFFwVars.inVar.get(name) == null))
								vValue = UTAFFwVars.inVar.get(name);
			            vDataString = vDataString.concat(name).concat(":").concat(vValue).concat("utafinvar");
			            System.out.println(name + " " + value);  
					} 
					vDataString=vDataString.concat("utafvarsplit");
					}else{
						vDataString=vDataString.concat("utafinvar");
						vDataString=vDataString.concat("utafvarsplit");
					}
					
					if(!UTAFFwVars.outVar.isEmpty()){
					for (String name: UTAFFwVars.outVar.keySet()){
							String vValue = "Null";
							if (!(UTAFFwVars.outVar.get(name) == null))
								vValue = UTAFFwVars.outVar.get(name);  
			            vDataString = vDataString.concat(name).concat(":").concat(vValue).concat("utafoutvar");
			            System.out.println(name + " " + value);  
					} 
					vDataString=vDataString.concat("utafvarsplit");
					}else{
						vDataString=vDataString.concat("utafoutvar");
						vDataString=vDataString.concat("utafvarsplit");
					}
					if(!UTAFFwVars.envVar.isEmpty()){
					for (String name: UTAFFwVars.envVar.keySet()){
							String vValue = "Null";
							if (!(UTAFFwVars.envVar.get(name) == null))
								vValue = UTAFFwVars.envVar.get(name);  
			            vDataString = vDataString.concat(name).concat(":").concat(vValue).concat("utafenvvar");
			            System.out.println(name + " " + value);  
					} }else{
						vDataString=vDataString.concat("utafenvvar");
						vDataString=vDataString.concat("utafvarsplit");
					}
					FileOutputStream outputStream = new FileOutputStream(vTextReport, true);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
					BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
					if ((new File(vTextReport).exists()))
						bufferedWriter.newLine();
					bufferedWriter.write(vDataString);
					bufferedWriter.close();
				} catch (Exception ex2) {
					UTAFLog.warn("Error in writing test Case data in text file : " + ex2.getMessage());
				}
				try{
					if(!(UTAFFwVars.utafFWTCJIRAID.equalsIgnoreCase("") || UTAFFwVars.utafFWTSJIRAID.equalsIgnoreCase(""))){
						UTAFCommonFunctions2.cfGlUpdateXrayStatus(vStatus);
						//UTAFCommonFunctions2.cfGlUpdateXrayStatusN(vStatus.substring(1, 4));
					}
					
				}catch(Exception ex){}
				
			} catch (Exception ex) {
				
				UTAFLog.warn("Update test case status request : " + jsonString);
				UTAFLog.warn("Error in updating  testcase status : " + ex.getMessage());
				try {
					String vTextReport = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
							+ UTAFFwVars.utafFWTSEID + "/" + vRef + "_Error.txt";
					String vTextDesc = vStatus.concat("utafsplit").concat(UTAFFwVars.utafFWTCError);
					FileOutputStream outputStream = new FileOutputStream(vTextReport, true);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
					BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
					if ((new File(vTextReport).exists()))
						bufferedWriter.newLine();
					bufferedWriter.write(vTextDesc);
					bufferedWriter.close();
				} catch (Exception ex2) {
					UTAFLog.warn("Error in writing test Case status in text file : " + ex2.getMessage());
				}
				try {
					jsonString = "{ \"tce_status\": \"" + vStatus
							+ "\", \"tce_comment\": \"Refer logs unexpected error\", \"tce_id\": " + vRef + " }";
					response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateStatus, jsonString, "PUT");
				} catch (Exception ex3) {

				}
				/*
				 * UTAFCommonFunctions2.cfGLGenericExceptionHandling(
				 * Thread.currentThread().getStackTrace()[1].getMethodName(),
				 * ex.getMessage() + "\n12" +
				 * UTAFCommonFunctions2.cfGlGetStackTrace(ex));
				 */
			}
			try{
				UTAFInteractions.cfUTAFVDIValuesUpdate(UTAFFwVars.utafFWMachineName,"NA", vRef,UTAFFwVars.utafFWTSEID,"N");}
				catch(Exception ex){
					
				}
			try{
				UTAFCommonFunctions2.cfRefreshHealthCheck();}
				catch(Exception ex){
					
				}
			
			UTAFLog.debug("Out cfUTAFUpdateTCStatusRest");
		}
	}
	
	public static void cfUTAFUpdateTCStatusStartRest(String vStatus, String vRef,String vDefect) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.info("In cfUTAFUpdateTCStatusRest");

			String value = "";
			String response = "";
			String jsonString = "";
			String vRestURLUpdateStatus = "";
			try {
				if (vStatus.equalsIgnoreCase("PASSED"))
					UTAFFwVars.utafFWTCError = "";
				if (!(vDefect == null)){
				if (UTAFFwVars.utafFWTCError.contains("\n")) {
					UTAFFwVars.utafFWTCError = UTAFFwVars.utafFWTCError.split("\n")[0];
				}
				vDefect = vDefect.replace("\n", "");
				vDefect = vDefect.replace("\\", "/");
				vDefect= vDefect.replace("\"", "");
				}
				Calendar c = Calendar.getInstance();
				//2020-09-27 17:48:55
				String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
				vRestURLUpdateStatus = UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestcaseParam");
				jsonString = "{ \"tce_status\": \"" + vStatus + "\",   \"tce_start_time\": \"" + todayDate + "\" , \"tce_defect\": \"" + vDefect
						+ "\", \"tce_id\": " + vRef + " }";
				response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateStatus, jsonString, "PUT");
				UTAFLog.info(response);
				JSONObject obj = new JSONObject(response);
				obj = new JSONObject(response);
				for (Object key : obj.keySet()) {
					// based on the key types
					String keyStr = (String) key;
					switch (keyStr) {
					case "tce_status":
						if (((String) obj.get(keyStr)).equalsIgnoreCase(vStatus)) {
							value = "1";
						} else {
							value = "2";
						}
						break;
					}
				}
				if (!value.equalsIgnoreCase("1")) {
					UTAFCommonFunctions2.cfGLGenericExceptionHandling(
							Thread.currentThread().getStackTrace()[1].getMethodName(),
							"No records to update in testcase_execution table for : " + vRef);
				}
			} catch (Exception ex) {
				
				UTAFLog.warn("Update test case status request : " + jsonString);
				UTAFLog.warn("Error in updating  testcase status : " + ex.getMessage());
				try {
					String vTextReport = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
							+ UTAFFwVars.utafFWTSEID + "/" + vRef + "_Error.txt";
					String vTextDesc = vStatus.concat("utafsplit").concat(UTAFFwVars.utafFWTCError);
					FileOutputStream outputStream = new FileOutputStream(vTextReport, true);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
					BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
					if ((new File(vTextReport).exists()))
						bufferedWriter.newLine();
					bufferedWriter.write(vTextDesc);
					bufferedWriter.close();
				} catch (Exception ex2) {
					UTAFLog.warn("Error in writing test Case status in text file : " + ex2.getMessage());
				}
				try {
					jsonString = "{ \"tce_status\": \"" + vStatus
							+ "\", \"tce_comment\": \"Refer logs unexpected error\", \"tce_id\": " + vRef + " }";
					response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateStatus, jsonString, "PUT");
				} catch (Exception ex3) {

				}
				/*
				 * UTAFCommonFunctions2.cfGLGenericExceptionHandling(
				 * Thread.currentThread().getStackTrace()[1].getMethodName(),
				 * ex.getMessage() + "\n12" +
				 * UTAFCommonFunctions2.cfGlGetStackTrace(ex));
				 */
			}
			try{
				cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "PASS", "Starting Execution" ,"");}
				catch(Exception ex){
					
				}
			try{
			UTAFInteractions.cfUTAFVDIValuesUpdate(UTAFFwVars.utafFWMachineName,"Execution Started", vRef,UTAFFwVars.utafFWTSEID,"N");}
			catch(Exception ex){
				
			}
			try{
				UTAFCommonFunctions2.cfRefreshHealthCheck();}
				catch(Exception ex){
					
				}
			UTAFLog.debug("Out cfUTAFUpdateTCStatusRest");
		}
	}
	
	public static void cfUTAFUpdateTestStep(String vRef, String vStatus, String vDesc, String vSSPath) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {

			UTAFLog.info("In cfUTAFUpdateTestStep");
			UTAFFwVars.utafStepFlag = true;
			String response = "";
			String jsonString = "";
			vDesc = vDesc.replace("'", "");
			vDesc = vDesc.replace("{", "");
			vDesc = vDesc.replace("}", "");
			if (vDesc.contains("\n")) {
				vDesc = vDesc.split("\n")[0];
			}
			vDesc = vDesc.replace("\n", "");
			// System.out.println(vDesc);
			String value = "";
			Calendar c = Calendar.getInstance();
			String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
			String vRestURLUpdateTestStep = "";
			try {
				UTAFLog.info(vDesc);
				
				try {
					String vTextReport = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
							+ UTAFFwVars.utafFWTSEID + "/" + vRef + ".txt";
					String vTextDesc = vRef.concat("utafsplit").concat(vDesc).concat("utafsplit").concat(vStatus)
							.concat("utafsplit").concat(vSSPath).concat("utafsplit").concat(todayDate);
							//.concat("utafsplit").concat(UTAFFwVars.utafFWReportTCName);
					FileOutputStream outputStream = new FileOutputStream(vTextReport, true);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
					BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
					if ((new File(vTextReport).exists()))
						bufferedWriter.newLine();
					bufferedWriter.write(vTextDesc);
					bufferedWriter.close();
				} catch (Exception ex2) {
					UTAFLog.warn("Error in writing report in text file : " + ex2.getMessage());
				}
				 /*vRestURLUpdateTestStep = UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTempReport");
				// vRestURLUpdateTestStep =
				// "http://ap8825:9999/utaf-service/updateTempReport";
				jsonString = "{ \"stepStatus\": \"" + vStatus + "\", \"tc_ID\": " + vRef + ", \"stepDesc\": \"" + vDesc
						+ "\", \"stepScreen\": \"" + vSSPath + "\" , \"stepLogTime\": \"" + todayDate + "\" }";
				response = UTAFCommonFunctions2.cfGLRestCallDirectInt(vRestURLUpdateTestStep, jsonString, "PUT");
				UTAFLog.info(response);
				JSONObject obj = new JSONObject(response);
				obj = new JSONObject(response);
				for (Object key : obj.keySet()) {
					// based on the key types
					String keyStr = (String) key;
					switch (keyStr) {
					case "stepStatus":
						if (((String) obj.get(keyStr)).equalsIgnoreCase(vStatus)) {
							value = "1";
						} else {
							value = "2";
						}
						break;
					}
				}
				if (!value.equalsIgnoreCase("1")) {
					UTAFCommonFunctions2.cfGLGenericExceptionHandlingThrow(
							Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Error in updating test step  : " + vRef);
				}*/
			} catch (Exception ex) {
				// UTAFCommonFunctions2.cfGlGetElementProperty(varName)
				// UTAFCommonFunctions2.cfGLGenericPassHandling(vMethodName,
				// vMessage);
				UTAFLog.warn("Update test step inputs request : " + jsonString);
				UTAFLog.warn("Error in updating test steps: " + ex.getMessage());
				try{
				jsonString = "{ \"stepStatus\": \"" + vStatus + "\", \"tc_ID\": " + vRef + ", \"stepDesc\": \"" + "error in updating steps in report, check logs"
						+ "\", \"stepScreen\": \"" + vSSPath + "\" , \"stepLogTime\": \"" + todayDate + "\" }";
				response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateTestStep, jsonString, "PUT");}
				catch(Exception ex2){
					UTAFLog.warn("Error in updating test steps: " + ex2.getMessage());
				}
				/*
				 * UTAFCommonFunctions2.cfGLGenericExceptionHandlingThrow(
				 * Thread.currentThread().getStackTrace()[1].getMethodName(),
				 * ex.getMessage() + "\nc" +
				 * UTAFCommonFunctions2.cfGlGetStackTrace(ex));
				 */
			}
			try{
				//cfUTAFVDIHealthCheck(UTAFFwVars.utafFWMachineName);
				}
				catch(Exception ex){
					
				}
			UTAFFwVars.utafStepFlag = false;
			UTAFLog.debug("Out cfUTAFUpdateTestStep");
		}
	}
	
	
	public static void cfUTAFUpdateTestStepVars(String vRef, String vStatus, String vDesc, String vSSPath) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.info("In cfUTAFUpdateTestStepVars");
			Calendar c = Calendar.getInstance();
			String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
			try {
				String vTextReport = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH") + UTAFFwVars.utafFWTSEID
						+ "/" + vRef + "_data.txt";
				String vTextDesc = vRef.concat("utafsplit").concat(vDesc).concat("utafsplit").concat(vStatus)
						.concat("utafsplit").concat(vSSPath).concat("utafsplit").concat(todayDate);
				// .concat("utafsplit").concat(UTAFFwVars.utafFWReportTCName);
				FileOutputStream outputStream = new FileOutputStream(vTextReport, true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				if ((new File(vTextReport).exists()))
					bufferedWriter.newLine();
				bufferedWriter.write(vTextDesc);
				bufferedWriter.close();
			} catch (Exception ex2) {
				UTAFLog.warn("Error in writing report in data text file : " + ex2.getMessage());
			}
			UTAFLog.debug("Out cfUTAFUpdateTestStepVars");
		}
	}
	public static String cfUTAFGetShadowTestCase(String vMachineName) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
		UTAFLog.debug("In cfUTAFGetShadowTestCacse");
		String value = "";
		try {
			String vRestURLGetShadowTestCase =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.getShadowTestCase");
			vRestURLGetShadowTestCase = vRestURLGetShadowTestCase + vMachineName;
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLGetShadowTestCase, "", "GET");
			JSONObject obj = new JSONObject(response);
			obj = new JSONObject(response);
			for (Object key : obj.keySet()) {
				// based on the key types
				String keyStr = (String) key;
				switch (keyStr) {
				case "message":
					value = (String) obj.get(keyStr);
					break;
				}
			}
			if (value.contains("not")) {
				UTAFLog.debug("No shadow test case available for : " + vMachineName);
			}
			return value;
		} catch (Exception ex) {
			UTAFLog.warn(
					"warning in cfUTAFGetShadowTestCase " + ex.getMessage());
		}
		UTAFLog.debug("Out cfUTAFGetShadowTestCase");
		return value;}else {return "";}
	}
	
	public static void cfUTAFUpdateVDI(String vMachineName, String vdiFlag) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.debug("In cfUTAFUpdateVDI");
		String value = "";
		try {
			String vRestURLUpdateVDI =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateVDI");
			String jsonString = "{ \"vdiname\": \"" + vMachineName + "\",  \"vdiavailable\": \"" + vdiFlag + "\"}";
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateVDI, jsonString, "POST");
			JSONObject obj = new JSONObject(response);
			obj = new JSONObject(response);
			for (Object key : obj.keySet()) {
				// based on the key types
				String keyStr = (String) key;
				switch (keyStr) {
				case "message":
					if (((String) obj.get(keyStr)).contains("not")) {
						value = "2";
					} else {
						value = "1";
					}
					break;
				}
			}
			if (!value.equalsIgnoreCase("1")) {
				UTAFLog.error( "cfUTAFUpdateVDI " + response);
			}
		} catch (Exception ex) {
			UTAFLog.warn(
					"Warning in cfUTAFUpdateVDI " + ex.getMessage());
		}
		UTAFLog.debug("Out cfUTAFUpdateVDI");
	}
	}
	
	public static void cfUTAFVDIHealthCheck(String vMachineName) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.debug("In cfUTAFVDIHealthCheck");
		try {
			String vdiHealthCheck =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.vdiHealthCheck");
			String jsonString = "{ \"vdi_name\": \"" + vMachineName + "\",  \"vdi_runningstatus\": \"UP\"}";
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vdiHealthCheck, jsonString, "POST");
			UTAFLog.info(response);
		} catch (Exception ex) {
			UTAFLog.warn(
					"Warning in cfUTAFVDIcHealthCheck " + ex.getMessage());
		}
		UTAFLog.debug("Out cfUTAFVDIHealthCheck");
	}
	
	}
	
	public static void cfUTAFVDIValuesUpdate(String vMachineName, String currAction, String vRef, String vTSEID, String vAvailable) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFLog.debug("In cfUTAFVDIValuesUpdate");
		try {
			if(currAction == null || currAction.equalsIgnoreCase("") || currAction.equalsIgnoreCase("NA"))
				currAction = "NA";
			if(vTSEID == null || vTSEID.equalsIgnoreCase(""))
				vTSEID = "0";
			if(currAction.equalsIgnoreCase("NA")){
				currAction= "NA";
			}
			else if(UTAFFwVars.utafFWTCReference.equalsIgnoreCase("EMPTY")|| UTAFFwVars.utafFWTCReference == null || UTAFFwVars.utafFWTCReference.equalsIgnoreCase(""))
				currAction= "NA";
			else
				currAction = "Executing " + UTAFFwVars.utafFWTCReference;
			String updateVDIPoolRest =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateVDIPool");
			String jsonString = "{ \"vdi_name\": \"" + vMachineName + "\",  \"vdi_currAction\": \" "  + currAction  + "\" ,  \"runningstatus\": \" " + "ACTIVE" + "\" , "
					+ " \"tseid\": " + vTSEID  + ", \"vdi_available\": \"" + vAvailable + "\"}";
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(updateVDIPoolRest, jsonString, "POST");
			UTAFLog.info(response);
		} catch (Exception ex) {
			UTAFLog.warn(
					"Warning in cfUTAFVDIValuesUpdate " + ex.getMessage());
		}
		UTAFLog.debug("Out cfUTAFVDIValuescUpdate");
	}
	
	}
	
	public static void cfUTAFCheckFailedTestcase(String vTCEID, String vTSID, String vTSEID) throws Exception {
		UTAFLog.debug("In cfUTAFCheckFailedTestcase");
		String value = "";

		try {
			String vRestURLUpdateStatus =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.checkFailedTestcase");
			String jsonString = "{ \"ts_id\": " + vTSID + ", \"tce_id\": " + vTCEID + " , \"tse_id\": " + vTSEID + " }";
			String response = UTAFCommonFunctions2.cfGLRestCallDirect(vRestURLUpdateStatus, jsonString, "POST");
			JSONObject obj = new JSONObject(response);
			obj = new JSONObject(response);
			for (Object key : obj.keySet()) {
				// based on the key types
				String keyStr = (String) key;
			/*	switch (keyStr) {
				case "tce_status":
					if (((String) obj.get(keyStr)).equalsIgnoreCase(vStatus)) {
						value = "1";
					} else {
						value = "2";
					}
					break;
				}*/
			}
			if (!value.equalsIgnoreCase("1")) {
				UTAFCommonFunctions2.cfGLGenericExceptionHandling(
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						"No records to update in testcase execution table for : " + vTCEID);
			}
		} catch (Exception ex) {
			UTAFCommonFunctions2.cfGLGenericExceptionHandling(
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n1" + UTAFCommonFunctions2.cfGlGetStackTrace(ex));
		}
		UTAFLog.debug("Out cfUTAFCheckFailedTestcase");
	}
	
	public static String cfUTAFUpdateTestSuiteExecutionRest(String vRef) throws Exception {
		UTAFLog.debug("In cfUTAFUpdateTestSuiteExecutionRest");
		String vStatus = "";
		try {
			String vRestURLUpdateTestSuite =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestSuiteExecutionNew");
			vRestURLUpdateTestSuite = vRestURLUpdateTestSuite + vRef + "/" + UTAFFwVars.utafFWTCReference;
			/*if(UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("Fail")){
				vRestURLUpdateTestSuite = vRestURLUpdateTestSuite + vRef + "/" + UTAFFwVars.utafFWTCReference;
			}else
			{
				vRestURLUpdateTestSuite =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.updateTestSuiteExecution");
				vRestURLUpdateTestSuite = vRestURLUpdateTestSuite + vRef;
			}*/
			
			//String jsonString = "{ \"tce_status\": \"" + vStatus + "\", \"tce_id\": " + vRef + " }";
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLUpdateTestSuite, "", "GET");
			JSONObject obj = new JSONObject(response);
			obj = new JSONObject(response);
			for (Object key : obj.keySet()) {
				// based on the key types
				String keyStr = (String) key;
				switch (keyStr) {
				case "status":
					System.out.println( "Status for " + vRef +  " is " + (String) obj.get(keyStr) );
					vStatus = (String) obj.get(keyStr);
					break;
				}
			}
			
		} catch (Exception ex) {
			/*UTAFCommonFunctions2.cfGLGenericExceptionHandling(
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n1" + UTAFCommonFunctions2.cfGlGetStackTrace(ex));*/
		}
		UTAFLog.debug("Out cfUTAFUpdateTestSuiteExecutionRest");
		return vStatus;
	}
	
	public static boolean cfUTAFGetTestsuiteAbortStatus(String vRef) throws Exception {
		UTAFLog.debug("In cfUTAFGetTestsuiteAbortStatus");
		try {
			String vRestURLGetTestsuiteAbortStatus =  UTAFFwVars.utafFWProps.getProperty("UTAF.RestAPI.getTestsuiteAbortStatus");
			vRestURLGetTestsuiteAbortStatus = vRestURLGetTestsuiteAbortStatus + vRef;
			String response = UTAFCommonFunctions2.cfGLRestCallDirectIntAuth(vRestURLGetTestsuiteAbortStatus, "", "GET");
			response=response.toString();
			if(response.equalsIgnoreCase("true")){
				return true;
			}
			
		} catch (Exception ex) {
			/*UTAFCommonFunctions2.cfGLGenericExceptionHandling(
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n1" + UTAFCommonFunctions2.cfGlGetStackTrace(ex));*/
		}
		UTAFLog.debug("Out cfUTAFGetTestsuiteAbortStatus");
		return false;
	}
	
	public static String[] cfReturnTestGroup(String vPath,String vTSEID, String vTSGroup) throws IOException, ClassNotFoundException, SQLException {
		String[]stepsList = null;
		int rows = 0;
		try {
			UTAFLog.info("Creating group test cases : " + vTSEID + " " +  vTSGroup);
			//UTAFFwVars.utafFWMachineName = "BGC-BE-QTP235";
			//File file=new File(vPath + "/" +vTCName + "2.txt" );    //creates a new file instance  
			String fileContent= UTAFCommonFunctions2.cfGlReadFileAsString(vPath  + UTAFFwVars.utafFWMachineName + "/"
					+ vTSEID + "_"+  vTSGroup + ".txt");
			UTAFLog.info(fileContent);
			String[] fileLines= fileContent.split("\n");
			UTAFLog.info(fileLines.length +"");
			stepsList = new String[fileLines.length-1];
			int baseNumber = 1000;
			String vDefect = "";
			String vTCJIraid = "";
			String vTSJIraid = "";
			while(rows<fileLines.length-1)  
			{  if(fileLines[rows+1].contains("|")){
				UTAFLog.info(fileLines[rows+1]);
				String[] stepRow = fileLines[rows+1].split("\\|");
					try {
						if (stepRow[10].isEmpty())
							vDefect = " ";
						else
							vDefect = stepRow[10];
					} catch (Exception ex) {
						vDefect = " ";
					}
					try {
						if (stepRow[11].isEmpty())
							vTCJIraid = "";
						else
							vTCJIraid = stepRow[11];
					} catch (Exception ex) {
						vTCJIraid = "";
					}
					try {
						if (stepRow[12].isEmpty())
							vTSJIraid = "";
						else
							vTSJIraid = stepRow[12];
					} catch (Exception ex) {
						vTSJIraid = "";
					}
				//stepsList[rows] = (baseNumber + Integer.parseInt(stepRow[8])) + "|"+ stepRow[9]+ "|" + stepRow[0] + "|" + vDefect + "|" + stepRow[3];
				stepsList[rows] = (baseNumber + Integer.parseInt(stepRow[8])) + "|"+ stepRow[9]+ "|" + stepRow[0] + "|" + vDefect  +
						"|" + vTCJIraid + 
						"|" + vTSJIraid +
						"|" + stepRow[3];
				System.out.println(stepsList[rows]);
				
			}
			rows++;
			}
			Arrays.sort(stepsList);
			UTAFFwVars.utafFWGroupLen=stepsList.length;
			UTAFFwVars.utafFWGroupSeq=0;
			
		} catch (Exception ex) {
			try{
			UTAFFwVars.utafFWTCError = ex.getMessage();
			UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL",
					"Error In group testCase Call" + ex.getMessage(), "");
			UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
			UTAFInteractions.cfUTAFUpdateTestSuiteExecutionRest(UTAFFwVars.utafFWTSEID);}catch(Exception ex2){}
			ex.printStackTrace();
			UTAFLog.error(ex.getMessage()+ "");
		} 
		return stepsList;
	}
	
	public static boolean cfGetCurrTestCase(int vSequence, boolean reqFlag) {
		
		try {
			UTAFFwVars.utafFWDBCase = false;
			String currRow = testCaselist[vSequence];
			String[] testParam = currRow.split("\\|");
			UTAFFwVars.utafFWTCSequence =testParam[0];
			UTAFFwVars.utafFWTCSession = testParam[1];
			UTAFFwVars.utafFWTCReference =testParam[2];
			UTAFFwVars.utafFWDBCase = true;
			UTAFFwVars.utafFWTCDefect =testParam[3];
			UTAFFwVars.utafFWTCJIRAID = testParam[4];
			UTAFFwVars.utafFWTSJIRAID = testParam[5];
			
			System.out.println(UTAFFwVars.utafFWTCDefect.length() );
			UTAFFwVars.utafFWTCName = testParam[6];
			//UTAFFwVars.utafFWTCName = currRow.substring(testParam[0].length()+ testParam[1].length() + testParam[2].length() 
				//	+  testParam[3].length()  + 4);
			if(reqFlag)
				cfUTAFUpdateTCStatusStartRest("IN PROGRESS",UTAFFwVars.utafFWTCReference, UTAFFwVars.utafFWTCDefect);
			return true;
		} catch (Exception ex) {
			try{
				UTAFFwVars.utafFWTCError = ex.getMessage();
				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, "FAIL",
						"Error In group testCase Call" + ex.getMessage(), "");
				UTAFInteractions.cfUTAFUpdateTCStatusRest("FAILED", UTAFFwVars.utafFWTCReference);
				UTAFInteractions.cfUTAFUpdateTestSuiteExecutionRest(UTAFFwVars.utafFWTSEID);}catch(Exception ex2){}
				ex.printStackTrace();
				UTAFLog.error(ex.getMessage()+ "");
			return false;
		}
	}
	
	public static void cfSetGroupParamDefault() {
		
		try {
			UTAFFwVars.utafFWGroupLen = 0;
			UTAFFwVars.utafFWGroupSeq = 0;
			UTAFFwVars.utafFWTCGroup = "";
			UTAFFwVars.utafFWTCSequence = "";
			UTAFFwVars.utafFWTCSession = "";
			UTAFFwVars.utafFWTCDefect = "";
			UTAFFwVars.utafFWTCJIRAID = "";
			UTAFFwVars.utafFWTSJIRAID = "";
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			
		}
	}
}
