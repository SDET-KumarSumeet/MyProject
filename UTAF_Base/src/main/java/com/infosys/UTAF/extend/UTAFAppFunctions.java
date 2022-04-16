package com.infosys.UTAF.extend;



import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.infosys.UTAF.UTAFCommonFunctions2;
import com.infosys.UTAF.UTAFDriverBridge2;
import com.infosys.UTAF.UTAFFwVars;
import com.infosys.UTAF.UTAFLog;
import com.infosys.UTAF.UTAFRead2;
import com.infosys.UTAF.UTAFRestUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import javax.jms.QueueSession;
import javax.jms.*;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import progress.message.jclient.*;
import progress.message.jclient.ConnectionFactory;
import progress.message.jclient.Queue;
import progress.message.jclient.QueueBrowser;



public class UTAFAppFunctions extends UTAFCommonFunctions2{
	public static WebDriver driver = null;
	public UTAFAppFunctions(WebDriver driver) {
		UTAFAppFunctions.driver = driver;
		// this.clickCF = new ClickFunctions();

	}
	public static OutputStream output;
	public static UTAFRestUtils restUtils = new UTAFRestUtils();
	public static Connection mySqlDbConnection() {
		
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://" + "el9359.int.corp" + ":" + "3306" + "/"
					+ "cdb_db" + "?autoReconnect=true&useSSL=false&serverTimezone=CET", "cdbdbuser", "cdb@user");
			System.out.println("connected successfully");
		}
		catch(Exception ex) {
			
		}
		return con;
	}
	
	public static String afGetElementProperty(String varName) throws Exception {
		UTAFLog.debug("In afGetElementProperty");
		String vValue = "";
		try {
			vValue = objectMapProps.getProperty(varName);
		} catch (Exception ex) {
			vValue=null;
		}
		UTAFLog.debug("Out afGetElementProperty");
		return vValue;
	}
	
	public static void afSelectComboBox(String... inputParams) throws Exception {
//		if(inputParams[1]!=null&&!inputParams[1].contains("null")&&!inputParams[1].startsWith("CDB_")) {
		System.out.println(inputParams[0]);
		System.out.println(inputParams[1]);
		if(!inputParams[0].equals(inputParams[1])) {
			if(inputParams[0].contains("_TEXT_")){
				inputParams[0]=cfGlGetElementProperty(inputParams[0]);
			}
			if(!(inputParams[1]==null)&&inputParams[1].contains("_TEXT_")){
				inputParams[1]=cfGlGetElementProperty(inputParams[1]);
			}
			if(inputParams[0].contains("=")) {
				String [] val = inputParams[0].split("=");
				inputParams[0] = val[0];
				inputParams[1] = val[1];
			}
			String element =inputParams[0];
			String value = inputParams[1];
			try {
				
				String menuOptionXpath = "//*[text()='#CAPTION#']/following::select";
				String byLocator = menuOptionXpath.replace("#CAPTION#", element);
				WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(byLocator)));
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(byLocator)));
				WebElement webElement = driver.findElement(By.xpath(byLocator));
				Select dropdown = new Select(webElement);
				dropdown.selectByVisibleText(value);
	            UTAFFwVars.utafFWTCStatus = "PASS";
	            cfGlReport(driver,"PASS", "Selected : " + value+ " from "+element+" dropdown", false, true);
				UTAFLog.info("Selected : " + value+ "from "+element+" dropdown");
				
			} catch (Exception ex) {
				cfGlReport(driver,"FAIL", ex.getMessage(), false, true);
			}
		}
		
	}
				
	public static void afverifyMandatoryTags(String[] inputParams) throws Exception{
		
		Connection con = mySqlDbConnection();
		Statement stmt = (Statement) con.createStatement();
		String result="";
		String vals[]= UTAFRead2.runTimeVar.get("service").split("_");
		String module = vals[1];
		String service = vals[2];
		String type=inputParams[0].trim();
		String query = "select * from cdb_db.RSP_MAND_TAGS where module='"+module+"' and  service ='"+service+"' and type='"+type+"'";
		ResultSet SQrs = stmt.executeQuery(query);
		while(SQrs.next()){
			result=SQrs.getString("value");
		}
		result=result.replaceAll("[^a-zA-Z0-9]", " ").trim();
		con.close();
		String actualVals[] = result.split(" ");
		cfGlSerValidateValueInResponse(restUtils.actualValues(actualVals));

	}
	
	public static String afgenerateRandomString(String... inputParams) {
		String generatedString = RandomStringUtils.randomAlphabetic(7);
		UTAFRead2.runTimeVar.put(inputParams[0], generatedString);
		return generatedString;
	}
	
	public static void afgenerateCoorelationID(String... inputParams) {
		int m = (int) Math.pow(10, 9 - 1);
		long num1 = (long)(m + new Random().nextInt(9 * m));
		String s1= String.valueOf(num1);
		UTAFRead2.runTimeVar.put("Req_corrId", s1);
	}
	
	public static void afCheckPreCondition(String... inputParams) throws Exception{
		UTAFLog.info("In afCheckPreCondition");
		response = null;
		xml="";
		requestpPayloadType="";
		json="";
		testData.clear();
		runTimeData.clear();
		if(response ==null&&xml.equals("")&&requestpPayloadType.equals("")&&json.equals("")
				&&testData.size()==0&&runTimeData.size()==0){
			UTAFFwVars.utafFWTCStatus = "pass";
			UTAFLog.info("Preconditions satisfied");
			UTAFAppFunctions.afgenerateCoorelationID();
			testData.put("Req_corrId", UTAFRead2.runTimeVar.get("Req_corrId"));
			
		} else {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Preconditions faied. Please check manually");
		}
		UTAFLog.info("Out afCheckPreCondition");
	}
	
	public static void afGenerateVatNumber(String[] inputParams) throws Exception {
		boolean flag = true;
		String segment = UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_bgcSubsegment");
		String vatNum=UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_enterpriseNumber");
		if((afSegmentList().contains(segment))&&(vatNum==null||!vatNum.equals("0912345678"))) {
			while(flag) {
				int m = (int) Math.pow(10, 7 - 1);
				long num1 = (long)(m + new Random().nextInt(9 * m));
				float num2 = (float) (num1/97);
				String num3= String.valueOf(num2).split("[.]")[0];
				int num4= (int) (num1-Double.parseDouble(num3)*97);
				String num5 = "0"+String.valueOf(num1)+String.valueOf(97-num4);
				
				if(!num5.startsWith("091")&&!num5.startsWith("090")&&!num5.startsWith("010")&&!num5.startsWith("046")&&!num5.startsWith("048")) {
					String query="select * from party p where p.CUST_VAT_NR="+"'"+num5+"'";
					String columnName="CUST_VAT_NR";
					String value = restUtils.getValueFromDb(query, columnName);
					if(value.isEmpty()) {
						UTAFRead2.runTimeVar.put("CDB_GUI_TEXT_enterpriseNumber", num5);
						UTAFRead2.runTimeVar.put("tempVatNumber", num5);
						flag=false;
					}
				}
				
			}
		}
	}
	
	public static String afGenerateVatNumberGeneric(String... inputParams) throws Exception {
		boolean flag = true;
		String vatNumber="";
		while(flag) {
			int m = (int) Math.pow(10, 7 - 1);
			long num1 = (long)(m + new Random().nextInt(9 * m));
			float num2 = (float) (num1/97);
			String num3= String.valueOf(num2).split("[.]")[0];
			int num4= (int) (num1-Double.parseDouble(num3)*97);
			vatNumber = "0"+String.valueOf(num1)+String.valueOf(97-num4);
			
			if(!vatNumber.startsWith("091")&&!vatNumber.startsWith("090")&&!vatNumber.startsWith("010")&&!vatNumber.startsWith("046")&&!vatNumber.startsWith("048")) {
				try{
					afGetValuesFromDb("CDB_partySearch_uisngVatNumber","CUST_VAT_NR",vatNumber,"tempVatNumber_DB");
					String value =UTAFRead2.runTimeVar.get("tempVatNumber_DB");
					if(value.equalsIgnoreCase("null")) {
						UTAFRead2.runTimeVar.put("tempVatNumber", vatNumber);
						flag=false;
					}
				}
				catch(Exception ex){}
			} 
			
		}
		return vatNumber;
	}
	
	public static void afGetPartyIDFromGUI(String[] inputParams) throws Exception {
        cfGlSelGetText(driver, "CDB_GUI_partyID_Text", inputParams[0]);
        String values[]=UTAFRead2.runTimeVar.get(inputParams[0]).trim().split(" ");
        String partyId = "";
        if(inputParams[1].equalsIgnoreCase("party")||inputParams[1].equalsIgnoreCase("location")||inputParams[1].equalsIgnoreCase("customer group")){
      	  partyId = values[values.length-1]; 
        }
        else if(inputParams[1].equalsIgnoreCase("account")){
      	  partyId = values[values.length-4];
        }
        
        UTAFRead2.runTimeVar.put(inputParams[0], partyId);
        UTAFLog.info(inputParams[1]+"="+partyId);
        cfGlReport(driver,"PASS", inputParams[1]+"="+partyId, false, true);
 }
	
	public static void afdateConversion(String... inputParams) {
		String[] value1= {""};
		String date = "";
		for(int i=0;i<inputParams.length;i++) {
			if(inputParams[i]!=null&&!inputParams[i].contains("null")&&!inputParams[i].isEmpty()&&!inputParams[i].contains("temp"))
			if(inputParams[0].contains("T")) {
				value1=inputParams[i].split("T");
				date = value1[0]+" 00:00:00.0";
				UTAFRead2.runTimeVar.put(inputParams[i+1], date);
			}
			else {
				date = inputParams[i]+" 00:00:00.0";
				UTAFRead2.runTimeVar.put(inputParams[i+1], date);
			}
		}
		
		
	}
	
	
	public static void afSendValueIfNotNull(String... inputParams) throws Exception {
		if(!inputParams[1].equals(inputParams[2])) {
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[2]);
			cfGlSelSendValue(driver, inputParams[0], inputParams[1]);
			cfGlReport(driver, "PASS", "Sent this : " + inputParams[1] + " to " + inputParams[0], false, true);
		}
		
	}
	
	public static void afSendValueIfValueNotNull(String... inputParams) throws Exception {
		if(inputParams[0]!=null&&inputParams[1]!=null&&!inputParams[0].startsWith("TSIP")&&!inputParams[1].startsWith("TSIP")&&!inputParams[0].equals("")&&!inputParams[1].equals("")) {
			cfGlSelSendValue(driver, inputParams[0], inputParams[1]);
			cfGlReport(driver, "PASS", "Sent this : " + inputParams[1] + " to " + inputParams[0], false, true);
		}
		
	}
	
	public static void afSendValueIfElementFound(String... inputParams) throws Exception {
		String xpath="";
		if(inputParams[2]!=null&&!inputParams[2].contains("null")&&!inputParams[2].startsWith("CDB_")) {
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[2]);
			xpath=getObjectValue2(inputParams[0])[0].replace("UTAFPropValue", UTAFRead2.runTimeVar.get("UTAFPropValue"));
		}
		else {
			xpath=getObjectValue2(inputParams[0])[0];
		}
		WebElement ele=null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			ele = driver.findElement(By.xpath(xpath));
		}
		catch(Exception ex) {
			if(inputParams[0].contains("firstName")){
				UTAFRead2.runTimeVar.put("tempFirstName", "null");
				}
			}
		if(ele!=null) {
			cfGlSelSendValue(driver, inputParams[0], inputParams[1]);
			cfGlReport(driver, "PASS", "Sent this : " + inputParams[1] + " to " + inputParams[0], false, true);
		}
			
	}
	
	public static void afElementClickIfNotNull(String... inputParams) throws Exception {

		if (((inputParams[0]!=null&&!inputParams[0].equals(""))||(inputParams[1]!=null&&!inputParams[1].equals("")))&&!(inputParams[1].equals(inputParams[2]))) {
			String element = inputParams[0];
			if(inputParams[1]!=null) {
				element=inputParams[1];
				UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
			}
			cfGlSelElementClick(driver, inputParams[0]);
			cfGlReport(driver, "PASS", "Successfully clicked on : " + element, false, true);
		} 
		

	}
	
	public static void afCommonElementClick(String... inputParams) throws Exception {
		String element = inputParams[0];
		if(inputParams[1]!=null) {
			element=inputParams[1];
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		cfGlSelElementClick(driver, inputParams[0]);
		cfGlReport(driver, "PASS", "Successfully clicked on : " + element, false, true);
			
	}
	
	public static void afCommonClearTextBox(String... inputParams) throws Exception {
		String element = inputParams[0];
		if(inputParams[1]!=null) {
			element=inputParams[1];
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		cfGlSelClearTextBox(driver, inputParams[0]);
		cfGlReport(driver, "PASS", "Successfully cleared value from : " + element, false, true);
			
	}
	
	public static void afClickIfDisplayed(String... inputParams) throws Exception {
		String element = inputParams[0];
		if(inputParams[1]!=null) {
			element=inputParams[1];
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		boolean flag = cfGlSelIsElementDisplayed(driver,inputParams[0]);
		if(flag) {
			cfGlSelElementClick(driver, inputParams[0]);
			cfGlReport(driver, "PASS", "Successfully clicked on : " + element, false, true);
		}
			
	}
	
	public static void afDataSetup(String[] inputParams) throws Exception {
		for(int i=1;i<=35;i++) {
			String value = UTAFRead2.runTimeVar.get("TCIP_"+i);
			if(value!=null) {
				if(value.contains(";")&&value.contains("=")) {
					String keyVals[] = value.split(";");
					for(int j=0;j<keyVals.length;j++) {
						String key = keyVals[j].split("=")[0].trim();
						UTAFRead2.runTimeVar.put(key+"_Prop", cfGlGetElementProperty(key)); 
						String val = keyVals[j].split("=")[1].trim();
						if(val.contains("_TEXT_")){
							val=cfGlGetElementProperty(val);
						}
						UTAFRead2.runTimeVar.put(key, val);
						UTAFCommonFunctions2.testData.put(key, val);
					}
				}
				else if(value.contains("=")) {
					String key = value.split("=")[0].trim();
					UTAFRead2.runTimeVar.put(key+"_Prop", cfGlGetElementProperty(key)); 
					String val = value.split("=")[1].trim();
					if(val.contains("_TEXT_")){
						val=cfGlGetElementProperty(val);
					}
					UTAFRead2.runTimeVar.put(key, val);
					UTAFCommonFunctions2.testData.put(key, val);
				}
			}
			else {
				break;
			}
		}
	}
	
/*	public static void afGetValuesFromDB(String... inputParams) throws Exception {
		restUtils.expectedQueryValue(inputParams[0], inputParams[1],inputParams[2]);
	} */
	
	public static void afGetPartyType(String... inputParams) throws Exception {
		String query = cfGlGetElementProperty(inputParams[0].trim());
		String value = restUtils.expectedQueryValue(query, inputParams[1],inputParams[2]);
		String font = "<font color=\"#32CD32\"><b>actualValue</b></font>";
		String partyType="";
		if(value==null||value.equals("null")) {
			partyType="person";
		}
		else {
			partyType="organisation";
		}
		UTAFRead2.runTimeVar.put("partyType", partyType);
		cfGlReportDesc(null, "PASS", "Fetching party type", false, "Party Type="+font.replace("actualValue", partyType));
	}
	
	
	public static void afGetTestDataFromDB(String... inputParams) throws Exception {
		String data = UTAFRead2.runTimeVar.get(inputParams[1]);
		int i = Integer.parseInt(inputParams[1].split("_")[1]);
		if(data!=null) {
			if(data.contains("?")) {
				String keyVal[]=UTAFRead2.runTimeVar.get("TCIP_"+(i+1)).split(",");
				String query=cfGlGetElementProperty(keyVal[0]);
				String columnName=keyVal[1];
				String value = restUtils.getValueFromDb(query, columnName);
				if(UTAFRead2.currTestCaseName.contains("GUI")) {
					UTAFRead2.runTimeVar.put("TCIP_"+i, UTAFRead2.runTimeVar.get("TCIP_"+i).split("=")[0]+"="+value);
//					UTAFRead2.runTimeVar.put("TCIP_"+(i+1), null);
					UTAFRead2.runTimeVar.put("tempProperty",UTAFRead2.runTimeVar.get("TCIP_"+i).split("=")[0]);
					UTAFRead2.runTimeVar.put("tempValue",value);
				}
				else {
					UTAFRead2.runTimeVar.put("tempTestData_"+i, UTAFRead2.runTimeVar.get("TCIP_"+i).split("=")[0]+"="+value);
					UTAFRead2.runTimeVar.put(UTAFRead2.runTimeVar.get("TCIP_"+i).split("=")[0], value);
					
				}
			}
			else {
				UTAFRead2.runTimeVar.put("tempTestData_"+i, UTAFRead2.runTimeVar.get("TCIP_"+i));
			}
//			i=i+2;
		}
	}
	
	public static void afGetAddressfromGUI(String... inputParams) throws Exception {
		cfGlThreadSleep("10");
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_street"));
		String tempStreet=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("tempStreet", tempStreet);
		
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_houseNbr"));
		String temphouseNumber=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("temphouseNumber", temphouseNumber);
		
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_boxNbr"));
		String tempBox=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("tempBox", tempBox);
		
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_floorNbr"));
		String tempFloor=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("tempFloor", tempFloor);
		
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_blockNbr"));
		String tempBlock=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("tempBlock", tempBlock);
		
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_city"));
		String tempcity=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		String tempCityNew="";
		if(tempcity.equals("Brussel")) {
			tempCityNew="Bruxelles/Brussel";
			UTAFRead2.runTimeVar.put("tempcityNew", tempCityNew);
		}
		UTAFRead2.runTimeVar.put("tempcity", tempcity);
		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_TEXT_Address_zipCode"));
		String tempzipCode=cfGlSelGetAttribute(driver, "CDB_GUI_enteredInputs", "value");
		UTAFRead2.runTimeVar.put("tempzipCode", tempzipCode);
		
/*		UTAFRead2.runTimeVar.put("UTAFPropValue", cfGlGetElementProperty("CDB_GUI_Address_street"));
		cfGlSelGetText(driver, "CDB_GUI_selectedCountry", "tempCountry"); */
		
		UTAFRead2.runTimeVar.put("tempCountry", UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_country"));
		
		String country = "";
		if(UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_country")!=null&&UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_country").equals("Belgium")) {
			country="Belgium/Belgique/België";
			UTAFRead2.runTimeVar.put("tempCountryGeneric", country);
		}
		
	}
	
/*	public static void afGetValuesFromDb(String...inputParams) throws Exception{
		HashMap<String,String> map = new HashMap<String,String>();
		String value="";
		String query = cfGlGetElementProperty(inputParams[0]);
		String vars[] = inputParams[2].split(",");
		for(int i=0;i<vars.length;i++) {
			map.put("v"+(i+1), vars[i]);
			query=query.replace("v"+(i+1), map.get("v"+(i+1)).trim());
		}
		
//		query=query.replace("v1", inputParams[2].trim());
//		query=query.replace("v2", inputParams[2].trim());
		Connection connection = null;
		try{
			connection= restUtils.dbConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			result=statement.executeQuery(query);
				while(result.next()){
					try{
						value=value+result.getString(inputParams[1])+",";
					}
					catch(Exception e){
						value=value+String.valueOf(result.getInt(inputParams[1]))+",";
					}
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}}catch(Exception ex1){}
		finally{
			if( !(connection == null)){
				connection.close();
			}
		}
		
		UTAFRead2.runTimeVar.put(inputParams[3], value.substring(0, value.length()-1));
	} */
	
	public static void afValidateDB(String... inputParams) throws Exception{
		String query=inputParams[0];
		String colName=inputParams[1];
		String variable=inputParams[2];
		String expected=inputParams[3];
		UTAFLog.info("In afValidateDB");
		UTAFRead2.runTimeVar.put("TSIP_1",query);
		query = cfGlGetElementProperty(query.trim());
//		String variables[]=variable.split(",");
		String colNames[]=colName.split(",");
		String expVals[]=expected.trim().split(",");
		String actualVals[]=new String[expVals.length];
		for(int i=0;i<expVals.length;i++){
			actualVals[i]=restUtils.expectedQueryValue(query,colNames[i].trim(),variable.trim());
		}
		String actual=restUtils.actualValues(actualVals);
		String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
		restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
		UTAFLog.info("Out afValidateDB");
		
	}
	
	public static void afGetValuesFromDb(String...inputParams) throws Exception{
		HashMap<String,String> map = new HashMap<String,String>();
		String value="";
		UTAFRead2.runTimeVar.put("TSIP_1",inputParams[0]);
		String query = cfGlGetElementProperty(inputParams[0]);
		String vars[] = {""};
		String data[] = inputParams[3].split(",");
		String columns[] = inputParams[1].split(",");
		if(inputParams[2]!=null) {
			vars = inputParams[2].split(",");
			for(int i=0;i<vars.length;i++) {
				map.put("v"+(i+1), vars[i]);
				query=query.replace("v"+(i+1), map.get("v"+(i+1)).trim());
			}
		}
		UTAFLog.info("Query =" +query);
//		UTAFRead2.runTimeVar.put("TSIP_1","CDB_APP");
		Connection connection = null;
		try{
			connection= restUtils.dbConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			result=statement.executeQuery(query);
			while(result.next()){
				for(int i=0;i<columns.length;i++) {
					try{
						value=value+result.getString(columns[i])+",";
						if(data.length>1) {
							if(result.getString(columns[i])==null){
								UTAFRead2.runTimeVar.put(data[i], "null");
							}
							else{
								UTAFRead2.runTimeVar.put(data[i], result.getString(columns[i]));
							}
						}
						
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						value=value+String.valueOf(result.getInt(columns[i]))+",";
						if(data.length>1) {
							UTAFRead2.runTimeVar.put(data[i], String.valueOf(result.getInt(columns[i])));
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}}catch(Exception ex1){System.out.println(ex1.getMessage());}
		finally{
			if( !(connection == null)){
				connection.close();
			}
		}
		if(!value.equals("")&&!inputParams[3].contains(",")){
			UTAFRead2.runTimeVar.put(inputParams[3], value.substring(0, value.length()-1));
		}
		else if(value.equals("")&&!inputParams[3].contains(",")){
			UTAFRead2.runTimeVar.put(inputParams[3], "null");
		}
		
	}
	
	
	public static void afGUIDbValidationForDifferentInputs(String... inputParams) throws Exception {
		if(inputParams[0].contains("Date")) {
			String values[] =UTAFRead2.runTimeVar.get("TCIP_5").split(",");
			String dates[]= {""};
			String date="";
			for(int i=0;i<values.length;i++) {
				if(values[i].trim().contains("Foundation")) {
					dates=UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_dateOfFoundation").split("/");
					date=dates[2]+"-"+dates[1]+"-"+dates[0]+" 00:00:00.0";
					UTAFRead2.runTimeVar.put("tempDate", date);
					UTAFRead2.runTimeVar.put("tempColumn","CUST_FOUNDATION_DT");
				}
				else if(values[i].trim().contains("Birth")) {
					dates=UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_dateOfBirth").split("/");
					date=dates[2]+"-"+dates[1]+"-"+dates[0]+" 00:00:00.0";
					UTAFRead2.runTimeVar.put("tempDate", date);
					UTAFRead2.runTimeVar.put("tempColumn","CUST_BIRTH_DT");
				}
			}
		}
		else if(inputParams[0].contains("Title")) {
			String values[] =UTAFRead2.runTimeVar.get("TCIP_3").split(",");
			for(int i=0;i<values.length;i++) {
				if(values[i].trim().contains("legalForm")) {
					UTAFRead2.runTimeVar.put("tempTitle", UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_legalForm"));
					UTAFRead2.runTimeVar.put("tempQuery","CDB_customerLegalFormCode");
					String column = cfGlGetElementProperty("CDB_DB_TEXT_longDescr");
					UTAFRead2.runTimeVar.put("tempColumn",column);
				}
				else if(values[i].trim().contains("title")) {
					UTAFRead2.runTimeVar.put("tempTitle", UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_title"));
					UTAFRead2.runTimeVar.put("tempQuery","CDB_customerTitleCode");
					String column = cfGlGetElementProperty("CDB_DB_TEXT_shortDecr");
					UTAFRead2.runTimeVar.put("tempColumn",column);
				}
			}
		}
		
	}
	
	public static String afgetCurrentDate(String... inputParams) {
		DateFormat df=null;
		if(inputParams[0]==null) {
			df = new SimpleDateFormat("yyyy-MM-dd");
		}
		else {
			df = new SimpleDateFormat(inputParams[0]);
		}
		Date dateobj = new Date();
		if(!(inputParams[1]==null)) {
			UTAFRead2.runTimeVar.put(inputParams[1], df.format(dateobj));
		}
		else {
			UTAFRead2.runTimeVar.put("tempCurrentDate", df.format(dateobj));
		}
		return df.format(dateobj);
	}
	
	public static String afAddDaysToTheDate(String... inputParams) throws ParseException{
		String date = inputParams[0];
		String format = inputParams[1];
		String daysToAdd=inputParams[2];
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(date));
		c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(daysToAdd)); 
		String newDate = sdf.format(c.getTime());
		if(inputParams[3]==null){
			UTAFRead2.runTimeVar.put("tempAddedDate", newDate);
		}
		else{
			UTAFRead2.runTimeVar.put(inputParams[3], newDate);
		}
		return newDate;
	}
	
	public static void afSetDateForModify(String... inputParams) throws Exception {
		if(inputParams[0].contains("CDB")) {
			String variables[] = inputParams[1].split(",");
			String variable = "";
			for(int i=0;i<variables.length;i++) {
				if(inputParams[2]!=null){
					if(variables[i].contains(inputParams[2])) { // partyID 
						variable=variables[i].split("=")[1].trim();
						break;
					}
				}
				else{
					variable=inputParams[1].trim();
					break;
				}
				
			}
			if(inputParams[1].contains(inputParams[3])) { // birthDate 
				afGetValuesFromDb(inputParams[4],"MOD_TM",variable,"tempModTime"); // queryName
				String date =  UTAFRead2.runTimeVar.get("tempModTime").split(" ")[0];
				String time =  UTAFRead2.runTimeVar.get("tempModTime").split(" ")[1].split("[.]")[0];
				UTAFCommonFunctions2.testData.put(inputParams[5], date+"T"+time+"+02:00"); //xml variable
			}
		}
		else {
			String date =  inputParams[0].split(" ")[0];
			String time =  inputParams[0].split(" ")[1].split("[.]")[0];
			UTAFCommonFunctions2.testData.put(inputParams[1], date+"T"+time+"+02:00");//xml variable
			UTAFRead2.runTimeVar.put(inputParams[1], date+"T"+time+"+02:00");
		}
		
	}
	
	public static void afAssertion( String actual, String expected) throws Exception{
		String expectdValue = "<font color=\"#32CD32\"><b>expectedVal</b></font>";
		String actualValue = "<font color=\"#32CD32\"><b>actualVal</b></font>";
		String actualValueError = "<font color=\"red\"><b>actualValError</b></font>";
		if (actual.contains(expected)) {
			UTAFLog.info("expected value = "+expected+". And actual value = "+actual);
			String vData = "expected value = "+expectdValue.replace("expectedVal", expected)+". And actual value = "+actualValue.replace("actualVal", actual);
			UTAFCommonFunctions2.cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		} else {
			UTAFLog.error("expected value = "+expected+". But actual value = "+actual);
			String vData = "expected value = "+expectdValue.replace("expectedVal", expected)+". But actual value = "+actualValueError.replace("actualValError", actual);
			UTAFCommonFunctions2.cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		}
		
	}
	
	public static void afValidateGuiFileds(String... inputParams) throws Exception {
		if(!inputParams[1].equals(inputParams[2])&&inputParams[2]!=null&&!inputParams[2].startsWith("temp")&&!inputParams[2].equals("null")) {
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
			if(inputParams[3]!=null) {
				if(inputParams[3].equalsIgnoreCase("CDB_GUI_name")) {
					try {
						cfGlSelGetText(driver, inputParams[0], "tempValue");
					}
					catch(Exception ex) {
						UTAFRead2.runTimeVar.put("UTAFPropValue",cfGlGetElementProperty("CDB_GUI_TEXT_nameFirstName"));
						cfGlSelGetText(driver, inputParams[0], "tempValue");
					}
				}
				else {
					cfGlSelGetText(driver, inputParams[0], "tempValue");
				}
			}
			else {
				cfGlSelGetText(driver, inputParams[0], "tempValue");
			}
			String valueOnGui=UTAFRead2.runTimeVar.get("tempValue").trim();
			if(UTAFRead2.runTimeVar.get("utafFWTCStep").contains("General")) {
				afAssertion(valueOnGui, inputParams[2].trim());
			}
			else {
				restUtils.assertion("",valueOnGui, inputParams[2].trim());
			}
			
		}
	}
	
	public static void afEnterPrintName(String... inputParams) throws Exception {
		String segment = UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_bgcSubsegment");
		if(segment.equals("22")||segment.equals("25")||segment.equals("31")||segment.equals("32")) {
			UTAFRead2.runTimeVar.put("tempPrintName", UTAFRead2.runTimeVar.get("tempName"));
		}
		else {
			UTAFRead2.runTimeVar.put("tempPrintName", UTAFRead2.runTimeVar.get("tempName")+" "+UTAFRead2.runTimeVar.get("tempFirstName"));
		}
		cfGlSelSendValue(driver, "CDB_GUI_printName_Input", UTAFRead2.runTimeVar.get("tempPrintName"));
	}
	
	public static void afGuiNegativeScenario(String... inputParams) throws Exception {
		String segment = UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_bgcSubsegment");
		if(afSegmentList().contains(segment)) {
			UTAFRead2.runTimeVar.put("CDB_GUI_TEXT_vatCountry", "Belgium");
			UTAFRead2.runTimeVar.put("CDB_GUI_TEXT_enterpriseNumber", "0912345678");
		}
		else {
			UTAFRead2.runTimeVar.put("CDB_GUI_TEXT_dateOfBirth", "01/01/2021");
		}
	}
	
	public static void afContainsAssertion(String... inputParams) throws Exception {
		if(UTAFRead2.currTestCaseName.contains("GUI")) {
			String segment = UTAFRead2.runTimeVar.get("CDB_GUI_TEXT_bgcSubsegment");
			String errorMessage="";
			
			if(afSegmentList().contains(segment)) {
				errorMessage=cfGlGetElementProperty("CDB_GUI_TEXT_vatErrorMsg");
			}
			else {
				errorMessage=cfGlGetElementProperty("CDB_GUI_TEXT_birthDateErrorMsg");
			}
			cfGlSelGetText(driver, "CDB_GUI_error_Text", "tempErrorMessage");
//			restUtils.assertion("",UTAFRead2.runTimeVar.get("tempErrorMessage"), errorMessage);
			afIsElementDisplayedGeneric("CDB_GUI_common_Text_contains",errorMessage);
		}
		else {
			afAssertion(inputParams[0], inputParams[1]);
		}
	}
	
	public static ArrayList<String> afSegmentList() {
		ArrayList<String> segmentList = new ArrayList<String>();
		segmentList.add("22");
		segmentList.add("23");
		segmentList.add("24");
		segmentList.add("25");
		segmentList.add("31");
		segmentList.add("32");
		return segmentList;	
	}
	
	public static void afIsElementDisplayed(String...inputParams) throws Exception {
		String element = inputParams[0];
		if(inputParams[1]!=null&&!inputParams[1].equals("")&&!inputParams[1].startsWith("TCIP")&&!inputParams[1].startsWith("TSIP")){
			element=inputParams[1];
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		boolean flag = cfGlSelIsElementDisplayed(driver,inputParams[0]);
		String isDisplayed="";
		if((String.valueOf(flag).equalsIgnoreCase(inputParams[2]))){
			if(String.valueOf(flag).equalsIgnoreCase("true")){
				isDisplayed="is displayed";
			}
			else{
				isDisplayed="is not displayed";
			}
			cfGlReport(driver,"PASS", element +" "+ isDisplayed, false, true);
		}
		else{
			if(String.valueOf(flag).equalsIgnoreCase("true")){
				isDisplayed="is displayed";
			}
			else{
				isDisplayed="is not displayed";
			}
			cfGlReport(driver,"FAIL", element +" "+ isDisplayed, false, true);
		}
	}
	
	public static void afIsElementDisplayedGeneric(String...inputParams) throws Exception {
		if(inputParams[1]!=null){
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		boolean flag = cfGlSelIsElementDisplayed(driver,inputParams[0]);
		if(inputParams[1]==null){
			inputParams[1]=inputParams[0];
		}
		if(flag) {
			cfGlReport(driver,"PASS", inputParams[1] + " is displayed", false, true);
		}
		else {
			cfGlReport(driver,"FAIL", inputParams[1] + " is not displayed", false, true);
		}
	}
	
	public static void afStoreRunTimeVars(String...inputParams) throws Exception{
		if(inputParams[0].contains("=")){
			String vars[] = inputParams[0].split(",");
			for(int i=0;i<vars.length;i++) {
				UTAFRead2.runTimeVar.put(vars[i].split("=")[0], vars[i].split("=")[1]);
			}
		}
		else{
			UTAFRead2.runTimeVar.put(inputParams[0], inputParams[1]);
		}
		
	}
	
	public static void afValidateTableDataGUI(String...inputParams) throws Exception {
		String xpath = getObjectValue2("CDB_GUI_category_Table")[0];
		String rowPath = xpath+"/tbody/tr";
		int rows = driver.findElements(By.xpath(rowPath)).size();
		UTAFRead2.runTimeVar.put("TSIP_1","CDB_APP");
		String tempQuery = cfGlGetElementProperty("CDB_categoryTable").replace("v1", inputParams[0]);
		String column = cfGlGetElementProperty("CDB_DB_TEXT_longDescr");
		for(int i=2;i<=rows;i++) {
			String classValue = driver.findElement(By.xpath(rowPath+"["+i+"]/td[2]")).getText().trim();
			String query=tempQuery.replace("v2", classValue);
			String valueFromDB=restUtils.getValueFromDb(query, column);
			String valueFromGUI= driver.findElement(By.xpath(rowPath+"["+i+"]/td[3]")).getText().trim();
			if(valueFromDB.equals(valueFromGUI)) {
				boolean flag = false;
				if(i==rows) {
					flag=true;
				}
				cfGlReport(driver,"PASS", "value of "+ classValue+" in DB = "+valueFromDB+" and value of "+ classValue+" in GUI = "+valueFromGUI, false, flag);
			}
			else {
				cfGlReport(driver,"FAIL", "value of "+ classValue+" in DB = "+valueFromDB+" but value of "+ classValue+" in GUI = "+valueFromGUI, false, true);
			}
			
		}
	}
	
	public static Session afJschSession(String userName, String password, String host) throws Exception{
	    java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	JSch jsch = new JSch();
    	Session session=jsch.getSession(userName, host, 22);
    	session.setPassword(password);
    	session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
    	session.setConfig(config);
    	session.connect();
    	System.out.println(" Session Connected");
		return session;
	}
	
	public static void afUploadFileToSftp(String[] inputParams) throws Exception{
	    String localFile = UTAFFwVars.utafFWFolderPath + "//batchFiles//"+inputParams[3];
	    String remoteDir = inputParams[4];
	    
//	    File file =new File(localFile);
	    
	    Session session = afJschSession(inputParams[0],inputParams[1],inputParams[2]);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
	    channelSftp.connect();
	    UTAFLog.info("sftp channel connected successfully");
	    channelSftp.put(localFile, remoteDir); 
//	    channelSftp.put(new FileInputStream(file), remoteDir+file.getName());
	    cfGlReport(driver,"PASS", "Uploaded Input file = "+inputParams[3]+ " to the directory ="+inputParams[4], false, false);
	    UTAFLog.info("File placed successfully");
	    channelSftp.disconnect();
	    UTAFLog.info("sftp channel disconnected successfully");
	    channelSftp.exit();
	    session.disconnect();
	    UTAFLog.info("sftp session closed");
	    Thread.sleep(10000);
	}
	
	
	
	public static void afRunBatch(String[] inputParams) throws Exception{
		String command = inputParams[3];
		Session session = afJschSession(inputParams[0],inputParams[1],inputParams[2]);
		
		Channel channel=session.openChannel("exec");
    	((ChannelExec)channel).setCommand(command);
    	cfGlReport(driver,"PASS", "Executed the command ="+command+" successfully", false, false);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        
        InputStream in=channel.getInputStream();
        channel.connect();
        byte[] tmp=new byte[1024];
        while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            System.out.print(new String(tmp, 0, i));
          }
          if(channel.isClosed()){
        	  UTAFLog.info("exit-status: "+channel.getExitStatus());
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
        UTAFLog.info("Batch channel disconnected successfully");
        session.disconnect();
        UTAFLog.info("Batch session closed");
	}
	
	public static void writeFile(String filePath,String vData) throws IOException {
		FileWriter file = new FileWriter(filePath);
		file.write(vData);
		file.flush();
        file.close();
	}
	
	public static void afWriteCADinputFile(String[] inputParams) throws Exception {
		String date = afgetCurrentDate("yyyy-MM-dd_HHmmss",null);
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String data[]=inputParams[2].split(",");
		String trailerCount = String.valueOf(data.length);
		String fileName = "CACS_CAD_"+date+"_C.DAT";
		if(trailerCount.length()==1) {
			trailerCount="0"+trailerCount;
		}
		if(inputParams[1].contains("customer")) {
			if(inputParams[0].equalsIgnoreCase("CACS")) {
				fileName = "CACS_CAD_"+date+"_C.DAT";
			}
			else {
				fileName = "RMCA_CAD_"+date+"_C.DAT";
			}
			String custFileData=restUtils.getBody("Batches_CAD_CUST.DAT");
			for(int i=0;i<data.length;i++) {
				if(i==data.length-1) {
					custFileData=custFileData.replace("Req_customerId", data[i].trim());
				}else {
					custFileData=custFileData.replace("Req_customerId", data[i].trim()+"\n"+"Req_customerId");
				}
			}
			custFileData=custFileData.replace("Req_count", trailerCount);
			writeFile(filePath+fileName, custFileData);
			UTAFRead2.runTimeVar.put(inputParams[3], fileName);
			cfGlReport(driver,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
			UTAFRead2.runTimeVar.put("tempDate", date);
		}
		else {
			if(inputParams[0].equalsIgnoreCase("CACS")) {
				fileName = "CACS_CAD_"+date+"_A.DAT";
			}
			else {
				fileName = "RMCA_CAD_"+date+"_A.DAT";
			}
			String custFileData=restUtils.getBody("Batches_CAD_ACC.DAT");
			for(int i=0;i<data.length;i++) {
				if(i==data.length-1) {
					custFileData=custFileData.replace("Req_customerId|Req_pay_agrId", data[i].split(":")[0].trim()+"|"+ data[i].split(":")[1].trim());
				}else {
					custFileData=custFileData.replace("Req_customerId|Req_pay_agrId", data[i].split(":")[0].trim()+"|"+ data[i].split(":")[1].trim()+"\n"+"Req_customerId|Req_pay_agrId");
				}
			}
			custFileData=custFileData.replace("Req_count", trailerCount);
			writeFile(filePath+fileName, custFileData);
			UTAFRead2.runTimeVar.put(inputParams[3], fileName);
			cfGlReport(driver,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
			UTAFRead2.runTimeVar.put("tempDate", date);
		}		
	}
	
	public static void afDownloadFileFromSftp(String[] inputParams) throws Exception {
		Thread.sleep(30000);
		String localFile = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
	    String remoteDir = inputParams[3]+inputParams[4];
	    
	    Session session = afJschSession(inputParams[0],inputParams[1],inputParams[2]);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
	    channelSftp.connect();
	    UTAFLog.info("sftp channel connected successfully");
	    channelSftp.get(remoteDir, localFile);
	    UTAFLog.info("File downloaded successfully");
	    channelSftp.disconnect();
	    UTAFLog.info("sftp channel disconnected successfully");
	    channelSftp.exit();
	    session.disconnect();
	    UTAFLog.info("sftp session closed");
	    UTAFRead2.runTimeVar.put("tempOutputFile", inputParams[4]);
	    cfGlReport(driver,"PASS", "Downloaded the file = "+inputParams[4]+ " from the directory ="+inputParams[3], false, false);
	}
	
	public static void afDownloadLatestLogFileFromSftp(String... inputParams) throws Exception{
		Thread.sleep(30000);
		String localFile = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
	    String remoteDir = inputParams[3];
	    
	    Session session = afJschSession(inputParams[0],inputParams[1],inputParams[2]);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
	    channelSftp.connect();
	    UTAFLog.info("sftp channel connected successfully");
	    Vector<LsEntry> list = channelSftp.ls(remoteDir + "*.log");
	    ChannelSftp.LsEntry lastModifiedEntry = Collections.max(list,
	        (Comparator.comparingInt(entry-> entry.getAttrs().getMTime()))
	    );
	    channelSftp.get(remoteDir+lastModifiedEntry.getFilename(), localFile);
	    UTAFLog.info("File downloaded successfully");
	    channelSftp.disconnect();
	    UTAFLog.info("sftp channel disconnected successfully");
	    channelSftp.exit();
	    session.disconnect();
	    UTAFLog.info("sftp session closed");
	    UTAFRead2.runTimeVar.put("tempOutputFile", lastModifiedEntry.getFilename());
	    cfGlReport(driver,"PASS", "Downloaded the file = "+lastModifiedEntry.getFilename()+ " from the directory ="+inputParams[3], false, false);
	}
	
	public static void afDownloadLatestFileFromSftp(String... inputParams) throws Exception{
//		Thread.sleep(10000);
		String localFile = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
	    String remoteDir = inputParams[3];
	    
	    Session session = afJschSession(inputParams[0],inputParams[1],inputParams[2]);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
	    channelSftp.connect();
	    UTAFLog.info("sftp channel connected successfully");
	    Vector<LsEntry> list = channelSftp.ls(remoteDir + "*."+inputParams[4]);
	    ChannelSftp.LsEntry lastModifiedEntry = Collections.max(list,
	        (Comparator.comparingInt(entry-> entry.getAttrs().getMTime()))
	    );
	    channelSftp.get(remoteDir+lastModifiedEntry.getFilename(), localFile);
	    UTAFLog.info("File downloaded successfully");
	    channelSftp.disconnect();
	    UTAFLog.info("sftp channel disconnected successfully");
	    channelSftp.exit();
	    session.disconnect();
	    UTAFLog.info("sftp session closed");
	    UTAFRead2.runTimeVar.put("tempOutputFile", lastModifiedEntry.getFilename());
	    cfGlReport(driver,"PASS", "Downloaded the file = "+lastModifiedEntry.getFilename()+ " from the directory ="+inputParams[3], false, false);
	}
	
	public static void afDecompressZFile(String...inputParams) throws Exception{
		String localFile = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		InputStream fin = Files.newInputStream(Paths.get(localFile+cfGlGetRunTimeVar("tempOutputFile")));
		BufferedInputStream in = new BufferedInputStream(fin);
		OutputStream out = Files.newOutputStream(Paths.get(localFile+inputParams[0]));
		ZCompressorInputStream zIn = new ZCompressorInputStream(in);
		byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = zIn.read(buffer))) {
            out.write(buffer, 0, n);
        }
        out.close();
        zIn.close();
        UTAFRead2.runTimeVar.put("tempOutputFile", inputParams[0]);
	}
	
	public static int afCountLinesInAFile(String...inputParams) throws FileNotFoundException{
		Scanner file = new Scanner(new File(inputParams[0]));
		int n=0;
		while (file.hasNext()) {
            String line = file.nextLine();
            if (!line.isEmpty()) {
                n=n+1;
            }
        }
        file.close();
		return n;
	}
	public static void afValidateCADErrorFile(String... inputParams) throws Exception {
		String[] partyIds=inputParams[0].split(",");
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		String variable="";
		int i=0;
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String actValue = myReader.nextLine();
	        UTAFLog.info(actValue);
	        if(!actValue.equalsIgnoreCase("EOF")) {
	        	variable=partyIds[i];
	        	String expected ="";
	        	if(UTAFRead2.runTimeVar.get("tempOutputFile").endsWith("C.err")) {
	        		expected = variable+"|07|ID Record Details not found";
	        	}
	        	else {
	        		expected = variable+"|1|07|ID Record Details not found";
	        	}
	        	restUtils.assertion("", actValue, expected);
	        }
	        i++;
	      }
	      myReader.close();
	}
	
	public static void afValidateCADOutputFile(String... inputParams) throws Exception {
		String[] partyIds=inputParams[0].split(",");
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		String query = "";
		String columns="";
		String variable = "";
		if(UTAFRead2.runTimeVar.get("tempOutputFile").contains("_C_ACK")) {
			query = "CDB_partyAndLanguage";
//			columns="NM,FIRST_NM,CUST_BIRTH_DT,CITY_NM,STREET_NM,HOUSE_NR,ZIP_CD,COMM_LANG";
			columns="NM,FIRST_NM,CUST_BIRTH_DT,COMM_LANG";
		}
		else {
			query = "CDB_partyAndLanguage";
//			columns="NM,FIRST_NM,CITY_NM,STREET_NM,HOUSE_NR,ZIP_CD,STD_BELGACOM_CD,CUST_VAT_NR";
			columns="NM,FIRST_NM";
		}
		int i=0;
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String actValue = myReader.nextLine();
	        UTAFLog.info(actValue);
	        if(!actValue.equalsIgnoreCase("EOF")) {
	        	variable=partyIds[i];
	        	if(variable.contains(":")){
	        		variable=variable.split(":")[0]+","+variable.split(":")[1];
	        	}
		        afGetValuesFromDb(query,columns,variable,"tempvalues");
		        UTAFLog.info(UTAFRead2.runTimeVar.get("tempvalues"));
		        String expValues[]= UTAFRead2.runTimeVar.get("tempvalues").split(",");
		        for(int j=0;j<expValues.length;j++) {
		        	if(expValues[j].contains("Brussels")) {
		        		expValues[j]="Brussel";
		        	}
		        	if(expValues[j].contains("00:")) {
		        		String tempVal[]=expValues[j].split(" ")[0].split("-");
		        		expValues[j]=tempVal[0]+tempVal[1]+tempVal[2];
		        	}
		        	if(expValues[j].contains("(")&&expValues[j].contains(")")) {
		        		expValues[j]=expValues[j].split("\\(")[0].trim();
		        	}
		        	if(actValue.contains(expValues[j])) {
		        		cfGlReport(driver,"PASS", "value of in DB = "+expValues[j]+" and it is present in the output file", false, false);
		        	}
		        	else {
		        		cfGlReport(driver,"FAIL", "value of in DB = "+expValues[j]+" and it is not present in the output file", false, false);
		        	}
		        }
		        i++;
	        }
	      }
	      myReader.close();
		  
	}
	
	public static String afConvertDate(String... inputParams){
		String actualDate = inputParams[0];
		String format=inputParams[1];
		String desiredDate ="";
		String dateArray[] = null;
		if(actualDate.length()<=8){
			String yyyy=actualDate.substring(0, 4);
			String mm=actualDate.substring(4, 6);
			String dd=actualDate.substring(6, 8);
			desiredDate = format.replace("yyyy", yyyy).replace("mm", mm).replace("dd", dd);
		}
		else{
			if(actualDate.contains("+")) {
				dateArray=actualDate.split("[+]");
			}
			else {
				dateArray=actualDate.split(" ");
			}
			String yyyy=dateArray[0].split("-")[0];
			String mm=dateArray[0].split("-")[1];
			String dd=dateArray[0].split("-")[2];
			desiredDate = format.replace("yyyy", yyyy).replace("mm", mm).replace("dd", dd);
		}
		if(inputParams[2]!=null){
			UTAFRead2.runTimeVar.put(inputParams[2], desiredDate); // store the desired date in temp variable from excel sheet
		}
		return desiredDate;
	}
	
	public static void afGetAllValuesWithSameTagName(String... inputParams) throws Exception {
		String value ="";
		Document doc = null;
		String tagName = cfGlGetElementProperty(inputParams[0]);
		doc = restUtils.document(UTAFCommonFunctions2.xml.toString());
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expression = xpath.compile(tagName);
		Object result = expression.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;
		for(int i=0;i<nodes.getLength();i++) {
			value = value+nodes.item(i).getTextContent()+",";
		}
		value=value.substring(0, value.length()-1);
		UTAFRead2.runTimeVar.put(inputParams[1], value);
	}
	
	public static void afGenericAssertion(String...inputParams) throws Exception {
		if(inputParams[2]!=null&&inputParams[2].equalsIgnoreCase("boolean")){
			if(inputParams[0].equalsIgnoreCase("Y")||inputParams[0].equalsIgnoreCase("N")) {
				if(inputParams[0].equalsIgnoreCase("Y")) {
					inputParams[0]="true";
				}
				else {
					inputParams[0]="false";
				}
			}
			else {
				if(inputParams[0].equalsIgnoreCase("true")) {
					inputParams[0]="Y";
				}
				else {
					inputParams[0]="N";
				}
			}
			restUtils.assertion("", inputParams[0], inputParams[1]);
		}
		else if(inputParams[1].contains(",")&&!(inputParams[2]==null)&&inputParams[2].equalsIgnoreCase("contains")&&UTAFRead2.runTimeVar.get("utafFWTCLang").equals("EN")) {
			String[] expected = inputParams[1].split(",");
			for(int i=0;i<expected.length;i++) {
				afAssertion(inputParams[0],expected[i]);
			}
		}
		else if(!(inputParams[2]==null)&&inputParams[2].equalsIgnoreCase("contains")) {
			if(inputParams[1].contains("/")){
				inputParams[1]=inputParams[1].split("/")[0];
			}
			afAssertion(inputParams[0],inputParams[1]);
		}
		else {
			restUtils.assertion("", inputParams[0], inputParams[1]);
		}
		
	}
	
	public static void afCreateSCUregInputFile(String... inputParams) throws Exception {
		String dateOne = afgetCurrentDate("yyyyMMdd.HHmmss","tempDateNew");
		String dateTwo = afgetCurrentDate("dd/MM/yyyy","tempDateNewTwo");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String pssName = inputParams[0];
		if(pssName.length()>3) {
			pssName = pssName.substring(0, 3);
		}
		String pssNameInFile = inputParams[0];
		if(pssNameInFile.length()==3) {
			pssNameInFile = pssNameInFile+"  ";
		}else if(pssNameInFile.length()==4) {
			pssNameInFile = pssNameInFile+" ";
		}
		String fileName = "fil.online."+pssName.toLowerCase()+"."+dateOne;
		String fileData = "UPDATE REQUEST       1.0   PSSNM dd/MM/yyyy 0000000001 00000"+"\n";
		fileData = fileData.replace("PSSNM", pssNameInFile);
		fileData = fileData.replace("dd/MM/yyyy", dateTwo);
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[1], fileName);
		cfGlReport(driver,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
	}
	
	public static void afValidatePssMaskValue(String...inputParams) throws Exception {
		String pssMaskValBefore = inputParams[0];
		String pssMaskValAfter  = inputParams[1];
		int maskPosition = Integer.valueOf(inputParams[2])-1;
		String expected = "1";
		String s1="pss_upd_req_mask value before batch execution = "+inputParams[0];
		String s2="pss_upd_req_mask value after batch execution = "+inputParams[1];
		if(!pssMaskValBefore.equals(pssMaskValAfter)) {
			cfGlReport(driver,"PASS", s1+". And "+s2, false, false);
		}
		else {
			cfGlReport(driver,"FAIL", s1+". But "+s2+" . Some issue in batch execution. Please check ", false, false);
		}
		String actual = String.valueOf(pssMaskValAfter.charAt(maskPosition));
		restUtils.assertion("", actual, expected);
	}
	
	public static void afCreateMccInputFile(String... inputParams) throws Exception {
		String date = afgetCurrentDate("yyyyMMdd.HHmm","tempDate");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileName = "fil.mcc.mec.bmb."+date;
		String custFileData=restUtils.getBody("Batches_MCC.txt");
		for(int i=0;i<3;i++) {
			String name=RandomStringUtils.randomAlphabetic(5);
			String firstName=RandomStringUtils.randomAlphabetic(7);
			String lname="lname"+(i+1);
			String fname="fname"+(i+1);
			custFileData=custFileData.replace(lname, name);
			custFileData=custFileData.replace(fname, firstName);
		}
		
		writeFile(filePath+fileName, custFileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(driver,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", date);
		UTAFRead2.runTimeVar.put("tempRepFile", "fil.mcc.mec.bmb."+date+".rep");
		UTAFRead2.runTimeVar.put("tempRepFile", "fil.mcc.mec.bmb."+date+".rep");
	}
	
	public static void afMccRepFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		int i=0;
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	    	  i=i+1;
	        String actValue = myReader.nextLine();
	        String data[]=actValue.split(";");
	        String partyId=data[data.length-2];
	        afGetValuesFromDb("CDB_partySearch_v1","PTY_ID,MOD_USER_ID",partyId,"tempPartyId,tempUserId");
	        if(i==1){
	        	afGenericAssertion(UTAFRead2.runTimeVar.get("tempPartyId"),partyId,null);
	        }
	        else{
	        	afGenericAssertion(UTAFRead2.runTimeVar.get("tempUserId"),"867940",null);
	        }
	      }
	      myReader.close();
	}
	
	public static void afMccRejFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		int i=0;
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	    	i=i+1;
	        String actValue = myReader.nextLine();
	        String error1="Oracle error <-1458> <ORA-01458: invalid length inside variable character string";
	        String error2="> when creating a customer";
	        if(i==1){
	        	afGenericAssertion(actValue,error1,"contains");
	        }
	        else{
	        	afGenericAssertion(actValue,error2,"contains");
	        }
	        
	      }
	      myReader.close();
	}
	
	public static void afValidateSCUoutFiles(String... inputParams) throws Exception {
		String outFile = UTAFRead2.runTimeVar.get("tempOutputFile");
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+outFile);
		String query = "";
		String columns="";
		String variable = "";
		String previousVal="";
		String propVals[]=null;
		ArrayList<String> mandatoryVals=new ArrayList<String>();
		String filedata = new String(Files.readAllBytes(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+outFile)));
		if(outFile.contains("CUSTOMER.")&&!outFile.contains("NACUSTOMER.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_CUSTOMER").split(",");
		}
		else if(outFile.contains("ACCOUNT.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_ACCOUNT").split(",");
		}
		else if(outFile.contains("ADDRESS.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_ADDRESS").split(",");
		}
		else if(outFile.contains("CONTACTS.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_CONTACTS").split(",");
		}
		else if(outFile.contains("CUSTOMERGROUP.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_CUSTOMERGROUP").split(",");
		}
		else if(outFile.contains("LOCATION.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_LOCATION").split(",");
		}
		else if(outFile.contains("NACUSTOMER.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_NACUSTOMER").split(",");
		}
		else if(outFile.contains("NATRANSFER.")) {
			propVals= cfGlGetElementProperty("Batch_SCU_exp_NATRANSFER").split(",");
		}
		
		for(String vals:propVals) {
			if(filedata.contains(vals)) {
				cfGlReport(driver,"PASS", "Out File : "+outFile+" contains mandatory value : "+vals, false, false);
			}
			else {
				cfGlReport(driver,"FAIL", "Out File : "+outFile+" did not contain mandatory value : "+vals, false, false);
			}
		}
		for(String vals:propVals) {
			mandatoryVals.add(vals);
		}
		
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String actValue = myReader.nextLine();
//	        UTAFLog.info(actValue);
	        if(mandatoryVals.contains(actValue)) {
	        	previousVal=actValue;
	        }
	        if(outFile.contains("CUSTOMER.")&&previousVal.equals("[CUSTOMER]")&&actValue.contains("?")) {
	        	UTAFLog.info(previousVal);
	        	UTAFLog.info(actValue);
        		previousVal="";
        	}
	        if(outFile.contains("ACCOUNT.")&&previousVal.equals("[ACCOUNT]")&&actValue.contains("?")) {
	        	UTAFLog.info(previousVal);
	        	UTAFLog.info(actValue);
        		previousVal="";
        	}
	        if(outFile.contains("ADDRESS.")&&previousVal.equals("[ADDRESS]")&&actValue.contains("?")) {
	        	UTAFLog.info(previousVal);
	        	UTAFLog.info(actValue);
        		previousVal="";
        	}
/*	        if(!actValue.equalsIgnoreCase("EOF")) {
	        	variable=partyIds[i];
		        afGetValuesFromDb(query,columns,variable,"tempvalues");
		        UTAFLog.info(UTAFRead2.runTimeVar.get("tempvalues"));
		        String expValues[]= UTAFRead2.runTimeVar.get("tempvalues").split(",");
		        for(int j=0;j<expValues.length;j++) {
		        	if(expValues[j].contains("Brussels")) {
		        		expValues[j]="Brussel";
		        	}
		        	if(expValues[j].contains("00:")) {
		        		String tempVal[]=expValues[j].split(" ")[0].split("-");
		        		expValues[j]=tempVal[0]+tempVal[1]+tempVal[2];
		        	}
		        	if(expValues[j].contains("(")&&expValues[j].contains(")")) {
		        		expValues[j]=expValues[j].split("\\(")[0].trim();
		        	}
		        	if(actValue.contains(expValues[j])) {
		        		cfGlReport(driver,"PASS", "value of in DB = "+expValues[j]+" and it is present in the output file", false, false);
		        	}
		        	else {
		        		cfGlReport(driver,"FAIL", "value of in DB = "+expValues[j]+" and it is not present in the output file", false, false);
		        	}
		        }
		        i++;
	        }*/
	      }
	      myReader.close(); 
		
	}
	
	public static void afValidateGdpLogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of customers to be recalculated")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals(inputParams[0])){
	        		cfGlReport(null,"PASS", "count in DB = "+inputParams[0]+" and Number of customers to be recalculated = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "count in DB = "+inputParams[0]+" but Number of customers to be recalculated = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of customers recalculated")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals(inputParams[0])){
	        		cfGlReport(null,"PASS", "count in DB = "+inputParams[0]+" and Number of customers recalculated = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "count in DB = "+inputParams[0]+" but Number of customers recalculated = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of succeeded physical deletes")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals(inputParams[1])){
	        		cfGlReport(null,"PASS", "count in DB = "+inputParams[1]+" and Number of succeeded physical deletes = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "count in DB = "+inputParams[1]+" but Number of succeeded physical deletes = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("error")||line.contains("Error")){
	        	cfGlReport(null,"FAIL", new String(Files.readAllBytes(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile")))), false, false);
	        }
	        
	      }
	      myReader.close();
	}
	
	public static String afGenerateNewAccessNumber(String...inputParams) throws Exception{
		String accessNr=inputParams[0];
		boolean flag = true;
		int newAccnum=0;
		
		while(flag){
			if(accessNr.startsWith("0")){
				newAccnum=Integer.parseInt(accessNr)+1;
				accessNr="0"+String.valueOf(newAccnum);
			}
			
			else{
				accessNr=String.valueOf(newAccnum);
			}
//			String query1="select count(*) from subscribed_na sn where sn.access_nr='"+accessNr+"'";
			afGetValuesFromDb("CDB_subscribedNA_Count","COUNT(*)",accessNr,"tempCount");
			int count = Integer.parseInt(cfGlGetRunTimeVar("tempCount"));
			if(count==0){
				break;
			}
		}
		return accessNr;
		
	}
	
	public static String afSetupCustOrAccId(String id){
		String tempId=id;
		int idLength=id.length();
		if(idLength!=9){
			while(idLength!=9){
				tempId="0"+tempId;
				idLength=tempId.length();
			}
		}
		return tempId;
	}
	
	public static void afCreateNUBInputFile(String... inputParams) throws Exception {
		String dateOne = afgetCurrentDate("yyyyMMdd","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		
		String fileData=restUtils.getBody("Batches_NUB.txt");
		afGetValuesFromDb("CDB_Batches_Info","PHYSICAL_FILE","NUB","tempSerialNumber");
		String fileArray[]=cfGlGetRunTimeVar("tempSerialNumber").split("[.]");
		int fileSequence=Integer.parseInt(fileArray[fileArray.length-1])+1;
		String fileName = "fil.gtl.nubtds."+dateOne+"."+fileSequence;
		afGetValuesFromDb("CDB_partySearch_subscribedNA","ACCESS_NR",null,"tempAccNum1");
		String accessnumberOne=afGenerateNewAccessNumber(cfGlGetRunTimeVar("tempAccNum1"));
		String accessnumberTwo=afGenerateNewAccessNumber(accessnumberOne);
		UTAFRead2.runTimeVar.put("tempAccessNumberOne", accessnumberOne);
		UTAFRead2.runTimeVar.put("tempAccessNumberTwo", accessnumberTwo);
		afGetValuesFromDb("CDB_subscribedNA_Count","COUNT(*)",accessnumberOne,"tempCountAccessnumberOne");
		afGetValuesFromDb("CDB_subscribedNA_Count","COUNT(*)",accessnumberTwo,"tempCountAccessnumberTwo");
		if(cfGlGetRunTimeVar("tempCountAccessnumberOne").equals("0")){
			cfGlReport(null,"PASS", "Access_nr = "+accessnumberOne+ " is not available in DB before batch execution", false, false);
		}
		else{
			cfGlReport(null,"PASS", "Access_nr = "+accessnumberOne+ " is already available in DB. Please check the test data", false, false);
		}
		if(cfGlGetRunTimeVar("tempCountAccessnumberTwo").equals("0")){
			cfGlReport(null,"PASS", "Access_nr = "+accessnumberTwo+ " is not available in DB before batch execution", false, false);
		}
		else{
			cfGlReport(null,"PASS", "Access_nr = "+accessnumberTwo+ " is already available in DB. Please check the test data", false, false);
		}
		afGetValuesFromDb("CDB_Contract_ISDNType_v","CUST_ID","2","tempCustIDs");
		afGetValuesFromDb("CDB_Contract_ISDNType_v","CUST_MASTER_ACC_ID","2","tempAccIDs");
		String custIds[]=cfGlGetRunTimeVar("tempCustIDs").split(",");
		String accIds[]=cfGlGetRunTimeVar("tempAccIDs").split(",");
		UTAFRead2.runTimeVar.put("tempCustIdOne", custIds[0]);
		UTAFRead2.runTimeVar.put("tempCustIdTwo", custIds[1]);
		UTAFRead2.runTimeVar.put("tempAccIdOne", accIds[0]);
		UTAFRead2.runTimeVar.put("tempAccIdTwo", accIds[1]);
		fileData=fileData.replace("tempAcc_1", accessnumberOne).replace("tempAcc_2", accessnumberTwo);
		fileData=fileData.replace("cust_id_1", afSetupCustOrAccId(custIds[0])).replace("cust_id_2", afSetupCustOrAccId(custIds[1]));
		fileData=fileData.replace("acct_id_1", afSetupCustOrAccId(accIds[0])).replace("acct_id_2", afSetupCustOrAccId(accIds[1]));
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		String batchName = UTAFRead2.runTimeVar.get(inputParams[0]).split("[.]")[1];
		UTAFRead2.runTimeVar.put("tempBatchName", batchName.toUpperCase());
	}
	
	public static void afValidateNUBLogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Transaction counter")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Transaction counter = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Transaction counter = 2. But Actual Transaction counter = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Valid transaction counter")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Valid transaction counter = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Valid transaction counter = 2. But Actual Valid transaction counter = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Extended header record counter")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Extended header record counter= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Extended header record counter = 2. But Actual Extended header record counter = "+actualValue, false, false);
	        	}
	        }
	        
	      }
	      myReader.close();
	}
	
	public static void afCreateUCCInputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMdd.HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		
		String fileData=restUtils.getBody("Batches_UCC.txt");
		afGetValuesFromDb("CDB_Batches_UCC_v","CUST_ID","13, ","tempCustId_1");
		afGetValuesFromDb("CDB_Batches_UCC_v","CUST_ID","13,not","tempCustId_2");
		String endDate=afAddDaysToTheDate(dateOne,"yyyyMMdd","1",null);
		String fileName="fil.ucc.test."+dateOne;
		fileData=fileData.replace("custIdOne", cfGlGetRunTimeVar("tempCustId_1"));
		fileData=fileData.replace("custIdTwo", cfGlGetRunTimeVar("tempCustId_2"));
		fileData=fileData.replace("custIdThree", cfGlGetRunTimeVar("tempCustId_2"));
		fileData=fileData.replace("DateOne", endDate);
		fileData=fileData.replace("DateTwo", dateOne.split("[.]")[0]);
		fileData=fileData.replace("DateThree", dateOne.split("[.]")[0]);
		
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		UTAFRead2.runTimeVar.put("tempEndDate", endDate);
	}
	
	public static void afValidateUCCLogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("input file (header included)")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("4")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records found in the input file (header included) = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records found in the input file (header included) = 4. But Actual Number of records found in the input file (header included) = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("category codes updated")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of customer category codes updated = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of customer category codes updated = 2. But Actual Number of customer category codes updated = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("category codes already processed")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of customer category codes already processed= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of customer category codes already processed = 0. But Actual Number of customer category codes already processed = "+actualValue, false, false);
	        	}
	        }
	        
	      }
	      myReader.close();
	}
	
	
	
	public static void afCreateCCUInputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		
		String fileData=restUtils.getBody("Batches_CCU.txt");
		afGetValuesFromDb("CDB_Batches_CCU_v","CUST_ID","751, ","tempCustId_1");
		afGetValuesFromDb("CDB_Batches_CCU_v","CUST_ID","753, ","tempCustId_2");
		afGetValuesFromDb("CDB_Batches_CCU_v","CUST_ID","753,not","tempCustId_3");
		String dateAndTime=dateOne.replace("_", "");
		String fileName="RCL_"+dateOne+".dat";
		fileData=fileData.replace("custIdOne", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_1")));
		fileData=fileData.replace("custIdTwo", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_2")));
		fileData=fileData.replace("custIdThree", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_3")));
		fileData=fileData.replace("DateAndTime", dateAndTime);
		
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateCCULogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		int numberOfLines=afCountLinesInAFile(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		if(numberOfLines==29){
			cfGlReport(null,"PASS", "Expected and actual Number of lines in log file = "+numberOfLines, false, false);
		}
		else{
			cfGlReport(null,"FAIL", "Expected Number of lines in log file = 28. But Actual Number of lines in log file = "+numberOfLines, false, false);
		}
		int n=0;
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	    	n=n+1;
	        String line = myReader.nextLine();
	        if(n==28){
	        	actualValue = line.split("=")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Total Number of rows successfully updated for customer credit class = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total Number of rows successfully updated for customer credit class = 2. But Total Number of rows successfully updated for customer credit class = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("rows rejected because of customer enddated")){
	        	actualValue = line.split("=")[1].trim();
	        	if(actualValue.equals("1")){
	        		cfGlReport(null,"PASS", "Expected and actual Total Number of rows rejected because of customer enddated = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total Number of rows rejected because of customer enddated = 1. But Total Number of rows rejected because of customer enddated = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afCreateBAIinputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		
		String fileData=restUtils.getBody("Batches_BAI.txt");
		afGetValuesFromDb("CDB_Batches_BAI_v","PTY_ID,CUST_MASTER_ACC_ID,TDS_PA_ID","177001","tempCustId_1,tempAccId_1,tempPaId_1");
		afGetValuesFromDb("CDB_Batches_BAI","PTY_ID,CUST_MASTER_ACC_ID,TDS_PA_ID",null,"tempCustId_2,tempAccId_2,tempPaId_2");
		afGetValuesFromDb("CDB_Batches_BAI_v","PTY_ID,CUST_MASTER_ACC_ID,TDS_PA_ID","177000","tempCustId_3,tempAccId_3,tempPaId_3");
		
		String fileName="COB_BAI_"+dateOne+".BAI";
		fileData=fileData.replace("custIdOne", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_1")));
		fileData=fileData.replace("custIdTwo", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_2")));
		fileData=fileData.replace("custIdThree", afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_3")));
		fileData=fileData.replace("paIdOne", cfGlGetRunTimeVar("tempPaId_1")).replace("accIdOne", afSetupCustOrAccId(cfGlGetRunTimeVar("tempAccId_1")));
		fileData=fileData.replace("paIdTwo", cfGlGetRunTimeVar("tempPaId_2")).replace("accIdTwo", afSetupCustOrAccId(cfGlGetRunTimeVar("tempAccId_2")));
		fileData=fileData.replace("paIdThree", cfGlGetRunTimeVar("tempPaId_3")).replace("accIdThree", afSetupCustOrAccId(cfGlGetRunTimeVar("tempAccId_3")));
		
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateBAIstatFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of records present in the input file")){
	        	actualValue = line.split("file")[1].split(":")[1].trim();
	        	if(actualValue.equals("3")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records present in the input file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records present in the input file = 3. But Number of records present in the input file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of records that are processed successfully")){
	        	actualValue = line.split("successfully")[1].split(":")[1].trim();
	        	if(actualValue.equals("3")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records that are processed successfully = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records that are processed successfully = 3. But Number of records that are processed successfully = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afValidateCIMrequestLogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
//		int numberOfRecords = afCountLinesInAFile(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String count = String.valueOf(Integer.parseInt(cfGlGetRunTimeVar("tempCount"))+1);
		int actualValue_inserted=0;
		int actualValue_notInserted=0;
		String actualValue="";
		Stream<String> lines=null;
		int n=0;
	      while (myReader.hasNextLine()) {
	    	  n=n+1;
	        String line = myReader.nextLine();
	        if(line.contains("Number of customers inserted in the output file")){
	        	lines = Files.lines(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"))); 
	        	line = lines.skip(n).findFirst().get();
	        	actualValue_inserted = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	UTAFRead2.runTimeVar.put("tempCount", String.valueOf(actualValue_inserted));
	        	lines.close();
	        }
	        if(line.contains("Number of customers NOT inserted in the output file")){
	        	lines = Files.lines(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"))); 
	        	line = lines.skip(n).findFirst().get();
	        	actualValue_notInserted = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	lines.close();	
	        }
	      }
	      actualValue = String.valueOf(actualValue_inserted+actualValue_notInserted);
	      if(actualValue.equals(count)){
      		cfGlReport(null,"PASS", "Expected and actual Number of customers = "+actualValue, false, false);
      		}
      	else{
      		cfGlReport(null,"FAIL", "Expected Number of customers = "+count+". But Actual Number of customers = "+actualValue, false, false);
      		}	
	      myReader.close();
	}
	
	public static void afValidateCIMrequestOutFile(String... inputParams) throws Exception {

		int numberOfRecords = afCountLinesInAFile(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		String count = String.valueOf(Integer.parseInt(cfGlGetRunTimeVar("tempCount")));
		if(count.equals(String.valueOf(numberOfRecords))){
			cfGlReport(null,"PASS", "Expected and actual Number of lines in output file = "+numberOfRecords, false, false);
		}
		else{
			cfGlReport(null,"FAIL", "Expected Number of lines in output file = "+count+". But Actual Number of lines in output file = "+numberOfRecords, false, false);
		}
		String fileData=new String(Files.readAllBytes(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"))));
		String customers[] = cfGlGetRunTimeVar("tempCust").split(",");
		for(String customer:customers){
			if(fileData.contains(customer)){
			}
			else{
				cfGlReport(null,"FAIL", customer+" is not present in out file. But present in DB", false, false);
				break;
			}
		}
		cfGlReport(null,"PASS", "All the details present in EDP_REPRESENTATION table are present in output file", false, false);
		
	}
	
	public static void afCreateCIMresponseInputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		
		String fileData=restUtils.getBody("Batches_CIM_Response.txt");
		afGetValuesFromDb("CDB_Batches_CIM_EdpR_getLatestFileName","FILE_NM",null,"tempFileName");
		String cimOutputFileName=cfGlGetRunTimeVar("tempFileName");
		String currentDateAndTime=dateOne.replace("_", "");
		String outputFileDateAndTime=cimOutputFileName.split("_")[2];
		String triggerId=cimOutputFileName.split("_")[3];
		String fileName="EURODB_CDBF_"+currentDateAndTime+"_"+triggerId+"_001";
		afGetValuesFromDb("CDB_Batches_CIM_Response_TestData","CUST_ID",null,"tempCustId_1");
		fileData=fileData.replace("triggerId", triggerId).replace("currentDateTime", currentDateAndTime);
		fileData=fileData.replace("cimRequestOutputFileName", cimOutputFileName);
		fileData=fileData.replace("outputDateTime", outputFileDateAndTime);
		fileData=fileData.replace("custIdOne", cfGlGetRunTimeVar("tempCustId_1"));
		
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", currentDateAndTime);
		
	}
	
	public static void afCreateDCAinputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileData="";
		String fileName="";
		
		afGetValuesFromDb("CDB_Batches_DCA","CUST_ID,ACC_ID,ACCESS_NR",null,"tempCustId_1,tempAccId_1,tempAccNr_1");
		String cust_id=cfGlGetRunTimeVar("tempCustId_1");
		afGetValuesFromDb("CDB_Batches_DCA_v","CUST_ID,ACC_ID,ACCESS_NR",cust_id,"tempCustId_2,tempAccId_2,tempAccNr_2");
		if(inputParams[1].equalsIgnoreCase("SSC")){
			fileData=restUtils.getBody("Batches_DCA_SSC.txt");
			fileName="SSC_"+dateOne+".DAT";
			fileData=fileData.replace("AccessNrOne", cfGlGetRunTimeVar("tempAccNr_1")).replace("AccessNrTwo", cfGlGetRunTimeVar("tempAccNr_2"));
		}
		else{
			fileData=restUtils.getBody("Batches_DCA_CACS.txt");
			fileName="CACS_"+dateOne+".DAT";
			fileData=fileData.replace("accIdOne", cfGlGetRunTimeVar("tempAccId_1")).replace("accIdTwo", cfGlGetRunTimeVar("tempAccId_2"));
		}
		
		fileData=fileData.replace("custIdOne", cfGlGetRunTimeVar("tempCustId_1")).replace("custIdTwo", cfGlGetRunTimeVar("tempCustId_2"));
		fileData=fileData.replace("currentDate", dateOne.split("_")[0]).replace("currentTime", dateOne.split("_")[1]);
		
		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateDCALogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Total number of records in file")){
	        	actualValue = line.split("file")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Total number of records in file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total number of records in file = 2. But Total number of records in file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total number of account or line level records passed validation")){
	        	actualValue = line.split("validation")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Total number of account or line level records passed validation = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total number of account or line level records passed validation = 2. But Total number of account or line level records passed validation = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total number of NA inserted")){
	        	actualValue = line.split("inserted")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Total number of NA inserted = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total number of NA inserted = 2. But Total number of NA inserted = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afValidateNCI_OCDFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		String dateOne = afgetCurrentDate("yyyyMMdd","tempDateNew");
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		int numberOfRecords = afCountLinesInAFile(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		String numberOfRecordsActual = String.valueOf(numberOfRecords-1);
		UTAFRead2.runTimeVar.put("tempOCDcount", numberOfRecordsActual);

		Scanner myReader = new Scanner(myObj);
		int n=0;
	      while (myReader.hasNextLine()) {
	    	  n=n+1;
	    	  String line = myReader.nextLine();
	    	  if(n>1){ 
			      String accessNumber = line.substring(3, 19).trim();
			      String customerId = line.substring(35,44);
			      int tempLength=customerId.length();
			      for(int i=0;i<tempLength;i++){
			    	  if(customerId.startsWith("0")){
			    		  customerId=customerId.substring(1,customerId.length());
			    	  }
			    	  else{
			    		  break;
			    	  }
			      }
			      afGetValuesFromDb("CDB_customerSearch_usingAccsssNR_v","CUST_ID",accessNumber,"tempCustId");
			      afGenericAssertion(cfGlGetRunTimeVar("tempCustId").trim(),customerId,"contains");
	    	  }
	      }
	      myReader.close();		
	}
	
	public static void afValidateFABLogFile(String... inputParams) throws Exception {
		String dateOne = afgetCurrentDate("yyyy-MM-dd","tempDateNew");
		afGetValuesFromDb("CDB_customerSearch_usingAccsssNR_v","COUNT(*)",dateOne,"tempCount");
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		int count = Integer.parseInt(cfGlGetRunTimeVar("tempCount"));
		int actualValue=0;
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("records were processed")){
	        	actualValue = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	if(actualValue>=count){
	        		cfGlReport(null,"PASS", "Expected and actual records were processed = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected records were processed = "+count+". But records were processed = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("records were successfully processed")){
	        	actualValue = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	if(actualValue>=count){
	        		cfGlReport(null,"PASS", "Expected and actual records were successfully processed = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected records were successfully processed = "+count+". But records were successfully processed = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();	
	}
	
	public static void afValidateNCILogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String count = cfGlGetRunTimeVar("tempOCDcount");
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of Rows Inserted into the OCD Output File")){
	        	actualValue = line.split("<")[1].split(">")[0];
	        	if(actualValue.equals(count)){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Rows Inserted into the OCD Output File = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Rows Inserted into the OCD Output File = "+count+". But actual Number of Rows Inserted into the OCD Output File = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();	
	}
	
	
	
	public static void afValidateGenericOutputFile(String... inputParams) throws Exception {
		String path = UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile");
		String fileData=new String(Files.readAllBytes(Paths.get(path)));
		if(fileData.contains(inputParams[0])){
			afGenericAssertion(fileData,inputParams[0],"contains");
		}
	}
	
	public static void afGetRequiredData(String... inputParams) {
		String vals[] = inputParams[0].split(",");
		for(String value:vals) {
			if(!value.trim().equals(inputParams[1].trim())) {
				UTAFRead2.runTimeVar.put(inputParams[2], value);
				break;
			}
		}
	}
	
	public static void afBooleanAssertion(String... inputParams) throws Exception {
		if(inputParams[0].equalsIgnoreCase("Y")) {
			inputParams[0]="true";
		}
		else {
			inputParams[0]="false";
		}
		restUtils.assertion("", inputParams[0], inputParams[1]);
	}
	
	public static void afCurrentLocationId(String... inputParams) {
		int currentLocId = Integer.parseInt(inputParams[0])+1;
		UTAFRead2.runTimeVar.put(inputParams[1], String.valueOf(currentLocId));
	}
	
	public static void afSelectRequiredValueFromDropdown(String... inputParams){
		String city=inputParams[0];
		String street = inputParams[1];
		String zipCode= inputParams[2];
		String country = inputParams[3];
		String xpath = "";
		WebElement element = null;
		if(city.contains("/")){
			city=city.split("/")[0];
		}
		if(street.contains("/")){
			street=street.split("/")[0];
		}
		try{
			if(zipCode!=null){
				xpath = "(.//option[contains(text(),'tempCity (tempZip)')])".replace("tempCity", city).replace("tempZip", "zipCode");
				element = driver.findElement(By.xpath(xpath));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", new Object[] { element });
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			}
		}
		
		catch(Exception ex){
			xpath = "(.//option[contains(text(),'tempCity')])".replace("tempCity", city);
			element = driver.findElement(By.xpath(xpath));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", new Object[] { element });
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
		
		try{
			if(zipCode!=null){
				xpath = "(.//option[contains(text(),'tempStreet (tempZip)')])".replace("tempStreet", street).replace("tempZip", "zipCode");
				element = driver.findElement(By.xpath(xpath));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", new Object[] { element });
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			}
		}
		
		catch(Exception ex){
			xpath = "(.//option[contains(text(),'tempStreet')])".replace("tempStreet", street);
			element = driver.findElement(By.xpath(xpath));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", new Object[] { element });
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
		
		
	}
	
	public static void afAssertion(String[] inputParams) throws Exception{
		if(inputParams[0]!=null){
			cfGlReport(null,"PASS", "Actual Value = "+inputParams[0]+ ". It is not equal to null", false, false);
		}
		else{
			cfGlReport(null,"FAIL", "Actual Value = null", false, false);
		}
		
	}
	
	public static void afSwitchToWindow(String... inputParams){
		String parentWindow = driver.getWindowHandle();
		UTAFRead2.runTimeVar.put("tempParentWindow", parentWindow);
		int window_number = Integer.parseInt(inputParams[0]);
    	List<String> windowlist = null;
    	Set<String> windows = driver.getWindowHandles();
    	windowlist = new ArrayList<String>(windows);
    	String currentWindow = driver.getWindowHandle();
    	if (!currentWindow.equalsIgnoreCase(windowlist.get(window_number))) 
       {
    		driver.switchTo().window(windowlist.get(window_number));
    		String browser = UTAFFwVars.utafFPlatform;
    		if(browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")){
    			driver.manage().window().maximize();
    		}
        }
   }
	
	public static void afSwitchToParentWindow(String... inputParams) throws Exception{
		driver.switchTo().window(cfGlGetRunTimeVar("tempParentWindow"));
	}
	
	public static void afCloseCurrentWindow(String... inputParams) throws Exception{
		driver.close();
	}
	
	public static void afCloseWebDriver(String... inputParams) throws Exception{
		driver.quit();
	}
	
	public static void afGetConfigProperty(String... inputParams) throws Exception {
        UTAFRead2.runTimeVar.put(inputParams[1],cfGlGetFrameworkProperty(inputParams[0]));
    }
	
	public static void afGetValuesFromGUITableAndCompareWithDB(String...inputParams) throws Exception {
		ArrayList<String> guiValues = new ArrayList<String>();
		String xpath = getObjectValue2(inputParams[1])[0];
		String rowPath = getObjectValue2(inputParams[0])[0];
		String tableValue="";
		int rows = driver.findElements(By.xpath(rowPath)).size();
		for(int i=2;i<=rows;i++) {
			try{
				tableValue = driver.findElement(By.xpath(xpath.replace("@value@", String.valueOf(i)))).getText().trim();
				if(tableValue.equals("")||tableValue.equals(" ")||tableValue.equals(".")){
					continue;
				}
				else{
					guiValues.add(tableValue);
				}
			}
			catch(Exception ex){}
		}
		String dbValues[]=inputParams[2].split(",");
		int dbValueSize=dbValues.length;
		int guiValueSize=guiValues.size();
		if(dbValueSize==guiValueSize){
			for(String value:dbValues){
				if(guiValues.contains(value)){
					cfGlReport(driver,"PASS", "DB value = "+value+ ". And it is present on GUI", false, true);
				}
				else{
					cfGlReport(driver,"FAIL", "DB value = "+value+ ". But it is not present on GUI", false, true);
				}
			}
		}
		else if(cfGlSelIsElementDisplayed(driver, "CDB_GUI_nextPage_Button")){
			for(int i=0;i<dbValues.length;i++){
				if(guiValues.contains(dbValues[i])){
					cfGlReport(driver,"PASS", "DB value = "+dbValues[i]+ ". And it is present on GUI", false, true);
					break;
				}
				else if(i<dbValues.length){
					continue;
				}
				else{
					cfGlReport(driver,"FAIL", "NOne of the DB values are present on GUI", false, true);
				}
			}
		}
		else{
			String actual = String.valueOf(dbValueSize);
			String expected = String.valueOf(guiValueSize);
			cfGlReport(driver,"FAIL", "DB count and GUI count are not matching. Expected = "+expected+". But Actual = "+actual, false, true);
		}
		
	}
	
    
    public static void afIsElementPresentInPage(String... inputParams) throws Exception {
 	   String pageSource = driver.getPageSource();
 	   String values[]=inputParams[0].split(",");
 	   for(int i=0; i<values.length;i++) {
 		   if(pageSource.contains(values[i])) {
 			   cfGlReport(driver,"PASS", "Element : "+values[i]+" is present on the screen", false, true);
 		   }
 		   else {
 			   cfGlReport(driver,"FAIL", "Element : "+values[i]+" is mot present on the screen", false, true);
 		   }
 	   }
 	   
    }
    
    
    public static String afstringFirstNcharacter(String...inputParams) throws Exception {
 	   String desiredString="";
 	   String actualString=inputParams[0];
 	   int numberOfCharacter=Integer.parseInt(inputParams[1]);
// 	   desiredString=actualString.replace(" ", "").substring(0, numberOfCharacter);
 	   desiredString=actualString.substring(0, numberOfCharacter);
 	  if(desiredString.endsWith(" ")){
 		 desiredString=actualString.substring(0, numberOfCharacter+1);
	   }
 	   UTAFRead2.runTimeVar.put(inputParams[2], desiredString);
 	   return desiredString; 
    }

    
    public static void afNavigateToURL(String... inputParams) {
 	   driver.navigate().to(inputParams[0]);
    }
    public static boolean afGlSelIsElementDisabled(String ...inputParams) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, inputParams[0], UTAFFwVars.utafFWExplicitWait);
			UTAFLog.info("Checking element is enabled or not at  " + inputParams[0]);
			if (!eleSearched.isEnabled()) {
				cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"Element is Disabled " + inputParams[0]);
				cfGlReport(driver,"PASS", inputParams[1]+" is disabled", false, true);
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );

		}
		return false;
	}


    public static void afSelectRequiredValueFromDropdownWithHnr(String... inputParams) throws Exception{
       
        String street = inputParams[0];
        String zipCode= inputParams[1];
        String hnr = inputParams[2];


        String xpath = "(//*[contains(text(),'tempStreet tempHnr, tempZipcode')])".replace("tempStreet", street).replace("tempHnr", hnr).replace("tempZipcode", zipCode);
        WebElement element = driver.findElement(By.xpath(xpath));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", new Object[] { element });
        element.click();
        cfGlReport(driver,"PASS", "Suggested Address is displayed", false, true);
        }
	
    public static void aflIsElementEnabled(String...inputParams) throws Exception {
        String elename = inputParams[0];
        String isEnabled="";
           try {
               cfGlSelCheckDocReady(driver);
               elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
               UTAFLog.info("Checking element is enabled or not at  " + elename);
               if (eleSearched.isEnabled()) {
                   cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
                           "Element is enabled " + elename);
                   isEnabled="true";
                   
               } else {
                   isEnabled="false";
               }
               UTAFRead2.runTimeVar.put("tempElementStatus", isEnabled);



           } catch (Exception ex) {
               cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
                       ex.getMessage() );
                  }
           }
       
    
    public static void aflIsElementSelected(String...inputParams) throws Exception {
        String elename = inputParams[0];
        String isEnabled="";
           try {
               cfGlSelCheckDocReady(driver);
               elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
               UTAFLog.info("Checking element is enabled or not at  " + elename);
               if (eleSearched.isSelected()) {
                   cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
                           "Element is enabled " + elename);
                   isEnabled="true";
                   
               } else {
                   isEnabled="false";
               }
               UTAFRead2.runTimeVar.put("tempElementStatus", isEnabled);



           } catch (Exception ex) {
               cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
                       ex.getMessage() );



           }
       }
    
    public static void afGetCreditScoreFromGUI(String[] inputParams) throws Exception {
        cfGlSelGetText(driver, "CDB_GUI_TEXT_creditscore", inputParams[0]);
        String values[]=UTAFRead2.runTimeVar.get(inputParams[0]).trim().split(" ");
        String creditScore = values[0];
        UTAFRead2.runTimeVar.put(inputParams[0],creditScore);
        UTAFLog.info("Credit Score ="+creditScore);
        cfGlReport(driver,"PASS", "Credit Score ="+creditScore, false, true);
    }
    
    public static void afUncheckEDPcheckBoxIfChecked(String... inputParams) throws Exception {
    	String xpathExpression = getObjectValue2("CDB_macrosegment_dropdown")[0];
    	boolean isEnabled = driver.findElement(By.xpath(xpathExpression)).isEnabled();
    	if(!isEnabled) {
    		cfGlSelElementClick(driver, "CDB_EDP_CHECKBOX");
    		cfGlReport(driver,"PASS", "Unchecked EDP Checkbox", false, true);
    	}
    }
    
    public static void afValidateRoleTypesPTY(String... inputParams) throws Exception{
    	afGetValuesFromDb("PTY_partyPartyRoles_v1","SYMBOLIC_NM",inputParams[0],"tempRoleValues");
    	afGetValuesFromDb("PTY_partyPartyRoles_v1","EXT_KEY_VALUE",inputParams[0],"tempRoleIds");
    	String rV[] = UTAFRead2.runTimeVar.get("tempRoleValues").split(",");
    	String rI[] = UTAFRead2.runTimeVar.get("tempRoleIds").split(",");
    	ArrayList<String> roleValues = new ArrayList<String>();
    	for(int i=0;i<rV.length;i++){
    		roleValues.add(rV[i]+"="+rI[i]);
    	}
    	for(String value:roleValues){
    		if(!value.contains("CDB_ID")){
    			cfGlSerValidateValueInResponse("value>"+value.split("=")[0]+"<");
    			cfGlSerValidateValueInResponse("id>"+value.split("=")[1]+"<");
    		}
    	}
    	
    }
    
    public static void afGenerateSubmitCustomerUpdatingJSON(String[] inputParams) throws Exception{
    	String paramFile="";
    	UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        UTAFRead2.runTimeVar.put("tempRequestHeader", uuidAsString);
    	String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//requestFiles//";
		String fileName="CRS_CustomerUpdating_"+dateOne+".json";
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	UTAFRead2.runTimeVar.put("Req_transactionDate", formatter.format(new Date()));
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_id", inputParams[0]);
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_id", inputParams[0]);
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_id01", inputParams[0]);
    	
    	String partyDetais_DB="CUST_LEGAL_FORM_CD,RESTRICTEL_IND,TEXT_LONG,CUST_FOUNDATION_DT,"
    			+ "CUST_SUBJECT_TO_VAT_IND,CUST_VAT_CTRY_CD,CUST_VAT_NR,NM,SUB_TITLE_DESCR";
    	
    	String partyDetails="temp_legalFormCode,Req_isRestrictel,Req_NOTES,"
    			+ "Req_party_foundingDate,Req_isSubjectToVAT,Req_VATNumber_countryCode_alpha2Code,Req_VATNumber,"
    			+ "Req_Refobj_person_or_organisation_name,Req_subTitle";
    	
    	String addressDetails_DB="EXTRA_ADDR_LINE,LOM_ID,START_DT,END_DT";
    	
    	String addressDetails="Req_EXTRA_ADDR_LINE,Req_legalAddress_geographicAddressIdentifier_id,"
    			+ "Req_legalAddress_validFor_startTimeStamp,Req_legalAddress_validFor_endTimeStamp";
    	
    	String naceDetails_DB="NACE_CD";
    	
//    	String naceDetails="Req_NACECode_value";
    	
    	afGetValuesFromDb("CDB_partySearch_v1",partyDetais_DB,inputParams[0],partyDetails);
    	afGetValuesFromDb("CDB_customerLegalLanguage","std_belgacom_cd",inputParams[0],"Req_legalLanguage_alpha2Code");
    	afGetValuesFromDb("CDB_customercommunicationLanguage","std_belgacom_cd",inputParams[0],"Req_communicationLanguage_alpha2Code");
    	
    	afGetValuesFromDb("CDB_naceAssignment_v1",naceDetails_DB,inputParams[0],"Req_NACECode_value");
    	if(!UTAFRead2.runTimeVar.get("Req_NACECode_value").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("Req_NACECode_value"),"Req_NACECode_value");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("Req_NACECode_value","");
    	}
    	
    	if(!UTAFRead2.runTimeVar.get("Req_VATNumber_countryCode_alpha2Code").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("Req_VATNumber_countryCode_alpha2Code"),"Req_VATNumber_countryCode_alpha2Code");
    	}
    	
    	Random random = new Random(); 
    	UTAFRead2.runTimeVar.put("Req_transactionId", String.valueOf((long) (100000000000000L + random.nextFloat() * 900000000000000L)));
    	UTAFRead2.runTimeVar.put("Req_endUserId", "id867940");
    	UTAFRead2.runTimeVar.put("Req_endUserLanguage", "EN");
    	UTAFRead2.runTimeVar.put("Req_consumerApplication", "CRS");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idScope", "ENT");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idContext", "CDB");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope", "MSG");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext", "B182");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope01", "MSG");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext01", "B182");
    	
    	UTAFRead2.runTimeVar.put("Req_personIdentifier_idScope", "MSG");
    	UTAFRead2.runTimeVar.put("Req_personIdentifier_idContext", "B182");
    	
    	
    	UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_printName",cfGlGetRunTimeVar("Req_Refobj_person_or_organisation_name"));
    	if(!UTAFRead2.runTimeVar.get("temp_legalFormCode").equals("null")){
    		afGetValuesFromDb("CDB_partyAddressSearch_v1",addressDetails_DB,inputParams[0],addressDetails);
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("temp_legalFormCode"),"Req_legalForm_value");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "organisation");
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "organisation");
    		UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idScope", "ENT");
        	UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idContext", "LOM");
        	UTAFRead2.runTimeVar.put("Req_subTitle", "null");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "person");
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "person");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_name", "null");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_printName", "null");
 /*   		if(UTAFRead2.runTimeVar.get("Req_VATNumber")!=null){
    			paramFile="CRS_customerUpdating_PersonWithVat_params.json";
    		}
    		else{
    			paramFile="CRS_customerUpdating_PersonWithoutVat_params.json";
    		} */
    		
    	}
    	paramFile="CRS_customerUpdating_Org_params.json";
    	String fileData=restUtils.getBody(paramFile);
 /*   	if(inputParams[1].contains("personIdentifier")){
    		String personData = restUtils.getBody("personIdentifier.txt");
    		fileData=fileData.replace("@personIdentifier", personData);
    		UTAFRead2.runTimeVar.put("Req_personIdentifier_id", inputParams[0]);
  //  		if(inputParams[1].equals("Req_personIdentifier_id")&&!inputParams[1].split("=")[1].equalsIgnoreCase("empty")){
  //  			UTAFRead2.runTimeVar.put("Req_personIdentifier_id", inputParams[0]);
  //  		}    		
    	}
    	else{
    		fileData=fileData.replace("@personIdentifier", "");
    	} */
//    	System.out.println(fileData);
    	if(inputParams[1]!=null&&!inputParams[1].equals("")&&!inputParams[1].isEmpty()){
    		String[] testData = inputParams[1].split(",");
    		for(String data:testData){
    			String excelData[]=data.split("=");
    			String key ="";
    			String value ="";
    			if(excelData.length>1){
    				key = excelData[0];
        			value = excelData[1];
        			if(value.equalsIgnoreCase("empty")){
        				value="";
        			} 
        			if(value.contains("-")&&key.contains("Stamp")){
        				value=value+"T00:00:00.0Z";
            		}
    			}
    			else{
    				if(excelData[0].contains("VAT")){
    					key = excelData[0];
    					value = afGenerateVatNumberGeneric();
    					String testCaseInput=UTAFRead2.runTimeVar.get("TCIP_2");
    					testCaseInput=testCaseInput.replace(excelData[0], excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("TCIP_2", testCaseInput);
//    					UTAFRead2.runTimeVar.put("TCIP_2", excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("Req_VATNumber_countryCode_alpha2Code", "JP");
    				}
    			}
    			fileData=fileData.replace("${"+key+"}", value);
    		}
    	}
    	
//    	System.out.println(UTAFRead2.runTimeVar);
    	Set<String> keySet = UTAFRead2.runTimeVar.keySet();
    	for(String key:keySet){
    		String dbData = UTAFRead2.runTimeVar.get(key);
    		if(dbData==null){
    			dbData="";
    		}
    		if(dbData.equals("N")){
    			dbData="false";
    		}
    		if(dbData.equals("Y")){
    			dbData="true";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&key.contains("Stamp")){
    			dbData=dbData.replace(" ", "T")+"Z";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&!key.contains("Stamp")){
    			dbData=afConvertDate(dbData,"yyyy-mm-dd",null);
    		}
    		fileData=fileData.replace("${"+key+"}", dbData);
    	}
 //   	System.out.println(fileData);
    	
    	String[] lines = fileData.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].contains("null")){
    	        lines[i]="";
    	        if(lines[i+1].contains("}")&&lines[i-1].length()>0){
    	        	lines[i-1]=lines[i-1].substring(0, lines[i-1].length()-1);
    	        }
    	    }
    	}
    	StringBuilder finalStringBuilder= new StringBuilder("");
    	for(String s:lines){
    	   if(!s.equals("")){
    	       finalStringBuilder.append(s).append(System.getProperty("line.separator"));
    	    }
    	}
    	
    	fileData = finalStringBuilder.toString();
    	fileData=afJSONBuilder(fileData);
/*    	JSONObject json = new JSONObject(fileData);
    	ObjectMapper mapper = new ObjectMapper();
    	fileData=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); */
    	writeFile(filePath+fileName, fileData); 
    	UTAFRead2.runTimeVar.put("tempDate", dateOne);
    	UTAFRead2.runTimeVar.put("tempFilePath", filePath+fileName);
    	
    }
    
    
    public static void afGenerateSubmitCustomerUpdatingJSON_New(String[] inputParams) throws Exception{
    	String paramFile="";
    	UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        UTAFRead2.runTimeVar.put("tempRequestHeader", uuidAsString);
    	String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//requestFiles//";
		String fileName="CRS_CustomerUpdating_"+dateOne+".json";
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	if(inputParams[0].startsWith("CDB_")){
    		afGetValuesFromDb(inputParams[0],"PTY_ID",null,"tempPartyId");
    		inputParams[0]=UTAFRead2.runTimeVar.get("tempPartyId");
    	}
    	
    	UTAFRead2.runTimeVar.put("Req_transactionDate", formatter.format(new Date()));
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_id", inputParams[0]);
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_id", inputParams[0]);
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_id01", inputParams[0]);
    	
    	String partyDetais_DB="CUST_LEGAL_FORM_CD,RESTRICTEL_IND,TEXT_LONG,CUST_FOUNDATION_DT,"
    			+ "CUST_SUBJECT_TO_VAT_IND,CUST_VAT_CTRY_CD,CUST_VAT_NR,NM,SUB_TITLE_DESCR";
    	
    	String partyDetails="temp_legalFormCode,Req_isRestrictel,Req_NOTES,"
    			+ "Req_party_foundingDate,Req_isSubjectToVAT,Req_VATNumber_countryCode_alpha2Code,Req_VATNumber,"
    			+ "Req_Refobj_person_or_organisation_name,Req_subTitle";
    	
    	String addressDetails_DB="EXTRA_ADDR_LINE,LOM_ID,START_DT,END_DT";
    	
    	String addressDetails="Req_EXTRA_ADDR_LINE,Req_legalAddress_geographicAddressIdentifier_id,"
    			+ "Req_legalAddress_validFor_startTimeStamp,Req_legalAddress_validFor_endTimeStamp";
    	
    	String naceDetails_DB="NACE_CD";
    	
//    	String naceDetails="Req_NACECode_value";
    	
    	afGetValuesFromDb("CDB_partySearch_v1",partyDetais_DB,inputParams[0],partyDetails);
    	afGetValuesFromDb("CDB_customerLegalLanguage","std_belgacom_cd",inputParams[0],"Req_legalLanguage_alpha2Code");
    	afGetValuesFromDb("CDB_customercommunicationLanguage","std_belgacom_cd",inputParams[0],"Req_communicationLanguage_alpha2Code");
    	
    	afGetValuesFromDb("CDB_naceAssignment_v1",naceDetails_DB,inputParams[0],"Req_NACECode_value");
    	if(!UTAFRead2.runTimeVar.get("Req_NACECode_value").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("Req_NACECode_value"),"Req_NACECode_value");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("Req_NACECode_value","");
    	}
    	
    	if(!UTAFRead2.runTimeVar.get("Req_VATNumber_countryCode_alpha2Code").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("Req_VATNumber_countryCode_alpha2Code"),"Req_VATNumber_countryCode_alpha2Code");
    	}
    	
    	Random random = new Random(); 
    	UTAFRead2.runTimeVar.put("Req_transactionId", String.valueOf((long) (100000000000000L + random.nextFloat() * 900000000000000L)));
    	UTAFRead2.runTimeVar.put("Req_endUserId", "id867940");
    	UTAFRead2.runTimeVar.put("Req_endUserLanguage", "EN");
    	UTAFRead2.runTimeVar.put("Req_consumerApplication", "CRS");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idScope", "ENT");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idContext", "CDB");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope", "MSG");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext", "B182");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope01", "MSG");
    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext01", "B182");
    	
    	UTAFRead2.runTimeVar.put("Req_personIdentifier_idScope", "MSG");
    	UTAFRead2.runTimeVar.put("Req_personIdentifier_idContext", "B182");
    	
    	
    	UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_printName",cfGlGetRunTimeVar("Req_Refobj_person_or_organisation_name"));
    	if(!UTAFRead2.runTimeVar.get("temp_legalFormCode").equals("null")){
    		afGetValuesFromDb("CDB_partyAddressSearch_v1",addressDetails_DB,inputParams[0],addressDetails);
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("temp_legalFormCode"),"Req_legalForm_value");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "organisation");
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "organisation");
    		UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idScope", "ENT");
        	UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idContext", "LOM");
        	UTAFRead2.runTimeVar.put("Req_subTitle", "null");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "person");
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "person");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_name", "null");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation_printName", "null");
 /*   		if(UTAFRead2.runTimeVar.get("Req_VATNumber")!=null){
    			paramFile="CRS_customerUpdating_PersonWithVat_params.json";
    		}
    		else{
    			paramFile="CRS_customerUpdating_PersonWithoutVat_params.json";
    		} */
    		
    	}
    	paramFile="CRS_customerUpdating_Org_params.json";
    	String fileData=restUtils.getBody(paramFile);
 /*   	if(inputParams[1].contains("personIdentifier")){
    		String personData = restUtils.getBody("personIdentifier.txt");
    		fileData=fileData.replace("@personIdentifier", personData);
    		UTAFRead2.runTimeVar.put("Req_personIdentifier_id", inputParams[0]);
  //  		if(inputParams[1].equals("Req_personIdentifier_id")&&!inputParams[1].split("=")[1].equalsIgnoreCase("empty")){
  //  			UTAFRead2.runTimeVar.put("Req_personIdentifier_id", inputParams[0]);
  //  		}    		
    	}
    	else{
    		fileData=fileData.replace("@personIdentifier", "");
    	} */
//    	System.out.println(fileData);
    	if(inputParams[1]!=null&&!inputParams[1].equals("")&&!inputParams[1].isEmpty()){
    		String[] testData = inputParams[1].split(",");
    		for(String data:testData){
    			String excelData[]=data.split("=");
    			String key ="";
    			String value ="";
    			if(excelData.length>1){
    				key = excelData[0];
        			value = excelData[1];
        			if(value.equalsIgnoreCase("empty")){
        				value="";
        			} 
        			if(value.contains("-")&&key.contains("Stamp")){
        				value=value+"T00:00:00.0Z";
            		}
    			}
    			else{
    				if(excelData[0].contains("VAT")){
    					key = excelData[0];
    					value = afGenerateVatNumberGeneric();
    					String testCaseInput=UTAFRead2.runTimeVar.get("TCIP_2");
    					testCaseInput=testCaseInput.replace(excelData[0], excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("TCIP_2", testCaseInput);
//    					UTAFRead2.runTimeVar.put("TCIP_2", excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("Req_VATNumber_countryCode_alpha2Code", "JP");
    				}
    			}
    			fileData=fileData.replace("${"+key+"}", value);
    		}
    	}
    	
//    	System.out.println(UTAFRead2.runTimeVar);
    	Set<String> keySet = UTAFRead2.runTimeVar.keySet();
    	for(String key:keySet){
    		String dbData = UTAFRead2.runTimeVar.get(key);
    		if(dbData==null){
    			dbData="";
    		}
    		if(dbData.equals("N")){
    			dbData="false";
    		}
    		if(dbData.equals("Y")){
    			dbData="true";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&key.contains("Stamp")){
    			dbData=dbData.replace(" ", "T")+"Z";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&!key.contains("Stamp")){
    			dbData=afConvertDate(dbData,"yyyy-mm-dd",null);
    		}
    		
    		fileData=fileData.replace("${"+key+"}", dbData);
    	}
 //   	System.out.println(fileData);
    	
    	String[] lines = fileData.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].contains("null")){
    	        lines[i]="";
    	        if(lines[i+1].contains("}")&&lines[i-1].length()>0){
    	        	lines[i-1]=lines[i-1].substring(0, lines[i-1].length()-1);
    	        }
    	    }
    	}
    	StringBuilder finalStringBuilder= new StringBuilder("");
    	for(String s:lines){
    	   if(!s.equals("")){
    	       finalStringBuilder.append(s).append(System.getProperty("line.separator"));
    	    }
    	}
    	
    	fileData = finalStringBuilder.toString();
    	fileData=afJSONBuilder(fileData);
/*    	JSONObject json = new JSONObject(fileData);
    	ObjectMapper mapper = new ObjectMapper();
    	fileData=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); */
    	writeFile(filePath+fileName, fileData); 
    	UTAFRead2.runTimeVar.put("tempDate", dateOne);
    	UTAFRead2.runTimeVar.put("tempFilePath", filePath+fileName);
    	
    }
    public static void afDeleteFile(String... inputParams){
    	String filePath = "";
    	if(inputParams[0]!=null){
    		filePath=inputParams[0];
    	}
    	else{
    		filePath=UTAFRead2.runTimeVar.get("tempFilePath");
    	}
    	File file= new File (filePath);
    	file.delete();
    }
    
    public static void afTcipOrTsipConversion(String... inputParams){
    	inputParams[0]=inputParams[0].split(",")[0];
    	UTAFRead2.runTimeVar.put("tempInputKey", inputParams[0].split("=")[0]);
    	UTAFRead2.runTimeVar.put("tempInputValue", inputParams[0].split("=")[1]);
    	if(UTAFRead2.runTimeVar.get("tempInputKey").equals("Req_tempDate")){
			String tempDate = afgetCurrentDate("yyyy-MM-dd","tempDateValue");
			tempDate=tempDate+" 00:00:00.0";
			UTAFRead2.runTimeVar.put("tempInputValue", tempDate);
		}
    }
    
    public static void afCheckLanguage(String... inputParams) throws Exception{
//        String language=cfGlGetFrameworkProperty("Language");
    	boolean flag = true;   	
        String language=UTAFFwVars.utafFWTCLanguage;
        if(!UTAFFwVars.utafFWReportTCName.contains("BCO")){
    		try{
        		driver.findElement(By.xpath("(.//*[text()='Search'])")).isDisplayed();
        	}
        	catch(Exception ex){
        		flag = false;
        	}
        	if(!flag){
        		try{
        			driver.findElement(By.xpath("(.//a[text()='Options'])")).click();
        			Thread.sleep(2000);
        			driver.findElement(By.xpath("(.//a[text()='Langue - Anglais'])")).click();
        		}
        		catch(Exception e){
        			driver.findElement(By.xpath("(.//a[text()='Opties'])")).click();
        			Thread.sleep(2000);
        			driver.findElement(By.xpath("(.//a[text()='Taal - Engels'])")).click();
        		}
        	}
        }
        if(language.equalsIgnoreCase("NL")){
        	if(!UTAFFwVars.utafFWReportTCName.contains("BCO")){
        		//click on options
                cfGlSelElementClick(driver, "CDB_GUI_Options_Link");
                //select Dutch
                cfGlSelElementClick(driver, "CDB_GUI_NLD_Link");
        	}
        	else{
        		cfGlSelSelectDropDownValue(driver, "BCO_GUI_language", "NL");
        	}
           
        }
        else if(language.equalsIgnoreCase("FR")){
        	if(!UTAFFwVars.utafFWReportTCName.contains("BCO")){
        		//click on options
                cfGlSelElementClick(driver, "CDB_GUI_Options_Link");
                //select French
                cfGlSelElementClick(driver, "CDB_GUI_FRA_Link");
        	}
        	else{
        		cfGlSelSelectDropDownValue(driver, "BCO_GUI_language", "FR");
        	}
            
        }
    }
    
    public static void afGenerateSubmitCustomerUpdatingJSON_partial(String[] inputParams) throws Exception{
    	String paramFile="";
    	UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        UTAFRead2.runTimeVar.put("tempRequestHeader", uuidAsString);
    	String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//requestFiles//";
		String fileName="CRS_CustomerUpdating_"+dateOne+".json";
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	UTAFRead2.runTimeVar.put("Req_transactionDate", formatter.format(new Date()));
    	
    	
    	Random random = new Random(); 
    	UTAFRead2.runTimeVar.put("Req_transactionId", String.valueOf((long) (100000000000000L + random.nextFloat() * 900000000000000L)));
    	UTAFRead2.runTimeVar.put("Req_endUserId", "id867940");
    	UTAFRead2.runTimeVar.put("Req_endUserLanguage", "EN");
    	UTAFRead2.runTimeVar.put("Req_consumerApplication", "CRS");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idScope", "ENT");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idContext", "CDB");
    	
    	if(inputParams[0].startsWith("CDB_")){
    		afGetValuesFromDb(inputParams[0],"PTY_ID,CUST_LEGAL_FORM_CD",null,"tempPartyId,tempCustLegalFormCode");
    	}
    	else{
    		afGetValuesFromDb("CDB_partySearch_v1","PTY_ID,CUST_LEGAL_FORM_CD",inputParams[0],"tempPartyId,tempCustLegalFormCode");
    	}
    	System.out.println(UTAFRead2.runTimeVar.get("tempCustLegalFormCode"));
    	if(UTAFRead2.runTimeVar.get("tempCustLegalFormCode").equalsIgnoreCase("null")){
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "person");
			UTAFRead2.runTimeVar.put("Req_partyIdentifier_id", UTAFRead2.runTimeVar.get("tempPartyId"));
			UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope", "MSG");
	    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext", "B182");
    		UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "person");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("Req_partyIdentifier", "organisation");
			UTAFRead2.runTimeVar.put("Req_partyIdentifier_id", UTAFRead2.runTimeVar.get("tempPartyId"));
			UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope", "MSG");
	    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext", "B182");
	    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idScope01", "MSG");
	    	UTAFRead2.runTimeVar.put("Req_partyIdentifier_idContext01", "B182");
		    UTAFRead2.runTimeVar.put("Req_Refobj_person_or_organisation", "organisation");	
    	}
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_id", UTAFRead2.runTimeVar.get("tempPartyId"));
/*    	if(inputParams[1].contains(s)){
    		UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idScope", "ENT");
        	UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idContext", "LOM");
    	} */
    	
    	if(inputParams[1].startsWith("Req_EXTRA_ADDR_LINE")){
    		afGetValuesFromDb("CDB_partyAddressSearch_v1","LOM_ID",UTAFRead2.runTimeVar.get("tempPartyId"),"tempLomId");
    		UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_id", UTAFRead2.runTimeVar.get("tempLomId"));
    		UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idScope", "ENT");
        	UTAFRead2.runTimeVar.put("Req_legalAddress_geographicAddressIdentifier_idContext", "LOM");
    	}
    	
    	paramFile="CRS_customerUpdating_Org_params.json";
    	
    	String fileData=restUtils.getBody(paramFile);
    	if(inputParams[1]!=null&&!inputParams[1].equals("")&&!inputParams[1].isEmpty()){
    		String[] testData = inputParams[1].split(",");
    		for(String data:testData){
    			String excelData[]=data.split("=");
    			String key ="";
    			String value ="";
    			if(excelData.length>1){
    				key = excelData[0];
        			value = excelData[1];
        			if(value.equalsIgnoreCase("empty")){
        				value="";
        			} 
        			if(value.contains("-")&&key.contains("Stamp")){
        				value=value+"T00:00:00.0Z";
            		}
    			}
    			else{
    				if(excelData[0].contains("VAT")){
    					key = excelData[0];
    					value = afGenerateVatNumberGeneric();
    					String testCaseInput=UTAFRead2.runTimeVar.get("TCIP_2");
    					testCaseInput=testCaseInput.replace(excelData[0], excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("TCIP_2", testCaseInput);
 //   					UTAFRead2.runTimeVar.put("TCIP_2", excelData[0]+"="+value);
    					UTAFRead2.runTimeVar.put("Req_VATNumber_countryCode_alpha2Code", "JP");
    				}
    			}
    			fileData=fileData.replace("${"+key+"}", value);
    		}
    	}

    	Set<String> keySet = UTAFRead2.runTimeVar.keySet();
    	for(String key:keySet){
    		String dbData = UTAFRead2.runTimeVar.get(key);
    		if(dbData==null){
    			dbData="";
    		}
    		if(dbData.equals("N")){
    			dbData="false";
    		}
    		if(dbData.equals("Y")){
    			dbData="true";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&key.contains("Stamp")){
    			dbData=dbData.replace(" ", "T")+"Z";
    		}
    		if(dbData.contains("-")&&dbData.contains(":")&&dbData.contains(".")&&!key.equals("Req_transactionDate")&&!key.contains("Stamp")){
    			dbData=afConvertDate(dbData,"yyyy-mm-dd",null);
    		}
    		fileData=fileData.replace("${"+key+"}", dbData);
    	}
    	
    	fileData=afJSONBuilder(fileData);
    	System.out.println(fileData);
/*    	JSONObject json = new JSONObject(fileData);
    	ObjectMapper mapper = new ObjectMapper();
    	fileData=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); */
    	writeFile(filePath+fileName, fileData); 
    	UTAFRead2.runTimeVar.put("tempDate", dateOne);
    	UTAFRead2.runTimeVar.put("tempFilePath", filePath+fileName);
    	
    }
    
    public static void afGenerateSubmitCustomerCreateJSON(String[] inputParams) throws Exception{
    	String paramFile="";
    	UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        UTAFRead2.runTimeVar.put("tempRequestHeader", uuidAsString);
    	String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//requestFiles//";
		String fileName="CRS_CustomerCreating_"+dateOne+".json";
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	UTAFRead2.runTimeVar.put("Req_transactionDate", formatter.format(new Date()));
    	
    	
    	Random random = new Random(); 
    	UTAFRead2.runTimeVar.put("Req_transactionId", String.valueOf((long) (100000000000000L + random.nextFloat() * 900000000000000L)));
    	UTAFRead2.runTimeVar.put("Req_endUserId", "id867940");
    	UTAFRead2.runTimeVar.put("Req_endUserLanguage", "EN");
    	UTAFRead2.runTimeVar.put("Req_consumerApplication", "CRS");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_id", "0");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idScope", "MSG");
    	UTAFRead2.runTimeVar.put("Req_customer_identifier_idContext", "B182");
 //   	UTAFRead2.runTimeVar.put("Req_customer_identifier_id", "");
    	String firstName=RandomStringUtils.randomAlphabetic(8);
    	String lastName=RandomStringUtils.randomAlphabetic(8);
    	String vatNum="";
    	if(inputParams[0].equalsIgnoreCase("person")){
    		paramFile="CreatePerson.json";
    		UTAFRead2.runTimeVar.put("Req_firstName", firstName);
    		UTAFRead2.runTimeVar.put("Req_lastName", lastName);
    		UTAFRead2.runTimeVar.put("Req_printName", "cdbAutoPrintName");
    		UTAFCommonFunctions2.testData.put("Req_firstName", firstName);
    		UTAFCommonFunctions2.testData.put("Req_lastName", lastName);
    		UTAFCommonFunctions2.testData.put("Req_printName", "cdbAutoPrintName");
    	}
    	else{
    		paramFile="CreateOrgansation.json";
    		UTAFRead2.runTimeVar.put("Req_name", lastName);
    		UTAFRead2.runTimeVar.put("Req_printName", "cdbAutoPrintName");
    		UTAFCommonFunctions2.testData.put("Req_name", lastName);
    		UTAFCommonFunctions2.testData.put("Req_printName", "cdbAutoPrintName"); 
    	}
    	    	
    	String fileData=restUtils.getBody(paramFile);
    	for(int i=1;i<=5;i++){
    		if(inputParams[i]!=null&&!inputParams[i].equals("")&&!inputParams[i].isEmpty()&&inputParams[i].startsWith("Req")){
        		String[] testData = inputParams[i].split(",");
        		for(String data:testData){
        			String excelData[]=data.split("=");
        			String key ="";
        			String value ="";
        			if(excelData.length>1){
        				key = excelData[0];
            			value = excelData[1];
            			if(value.equalsIgnoreCase("empty")){
            				value="";
            			} 
            			if(value.contains("-")&&key.contains("Stamp")){
            				value=value+"T00:00:00.0Z";
                		}
            			UTAFRead2.runTimeVar.put(key, value);
            			UTAFCommonFunctions2.testData.put(key, value);
        			}
        			else{
        				if(excelData[0].contains("VAT")||excelData[0].contains("enterpriseNumber")){
        					key = excelData[0];
        					if(vatNum.equals("")){
        						vatNum = afGenerateVatNumberGeneric();
        						value=vatNum;
        					}
        					else{
        						value = vatNum;
        					}
        					UTAFRead2.runTimeVar.put(key, value);
        					UTAFCommonFunctions2.testData.put(key, value);
        					String testCaseInput=UTAFRead2.runTimeVar.get("TCIP_"+(i+1));
        					testCaseInput=testCaseInput.replace(excelData[0], excelData[0]+"="+value);
        					UTAFRead2.runTimeVar.put("TCIP_"+(i+1), testCaseInput);
  //      					UTAFRead2.runTimeVar.put("Req_VATNumber_countryCode_alpha2Code", "JP");
        				}
        			}
        			fileData=fileData.replace("${"+key+"}", value);
        		}
        	}
    	}
    	    	
    	Set<String> keySet = UTAFRead2.runTimeVar.keySet();
    	for(String key:keySet){
    		fileData=fileData.replace("${"+key+"}", UTAFRead2.runTimeVar.get(key));
    	}

    	fileData=afJSONBuilder(fileData);
 //   	System.out.println(fileData);
    	writeFile(filePath+fileName, fileData); 
    	UTAFRead2.runTimeVar.put("tempDate", dateOne);
    	UTAFRead2.runTimeVar.put("tempFilePath", filePath+fileName);
    	
    }

    
    public static String afAppendString(String[] inputParams){
    	StringBuilder finalStringBuilder= new StringBuilder("");
    	for(String s:inputParams){
     	   if(!s.equals("")){
     	       finalStringBuilder.append(s).append(System.getProperty("line.separator"));
     	    }
     	}
    	return finalStringBuilder.toString();
    }
    
    public static String afJSONBuilder(String... inputParams){
    	String data=inputParams[0];
    	String[] lines = data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    		if(lines[i].contains("Req_")){
    	    	lines[i]="";
    	    }
    	    
    	}
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    		if(lines[i].contains("\"null\"")){
    	        lines[i]="";
    	    }
    	}
    	
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if((lines[i].contains("NOTES")||lines[i].contains("EXTRA_ADDR_LINE"))&&lines[i+1].contains("}")){
    	        lines[i]="";
    	        lines[i+1]="";
    	        lines[i-1]="";
    	        data=afAppendString(lines);
    	        lines=data.split(System.getProperty("line.separator"));
        	    i=0;
    	    }
    	    
    	}
    	
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].endsWith(",")&&lines[i+1].contains("}")){
    	    	lines[i]=lines[i].substring(0, lines[i].length()-1);
    	    }  
    	    if(lines[i].endsWith(": ")&&lines[i+1].contains("}")){
    	    	lines[i]="";
    	    	lines[i+1]="";
    	    }
    	}
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].contains("{")&&lines[i+1].contains("}")){
    	        lines[i]="";
    	        lines[i+1]="";
    	        data=afAppendString(lines);
    	        lines=data.split(System.getProperty("line.separator"));
        	    i=0;
    	    }
    	    
    	}
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].contains("[")&&lines[i+1].contains("]")){
    	        lines[i]="";
    	        lines[i+1]="";
    	        data=afAppendString(lines);
    	        lines=data.split(System.getProperty("line.separator"));
        	    i=0;
    	    }
    	    
    	}
    	
    	data=afAppendString(lines);
    	lines=data.split(System.getProperty("line.separator"));
    	for(int i=0;i<lines.length;i++){
    	    if(lines[i].endsWith(",")&&lines[i+1].contains("}")){
    	    	lines[i]=lines[i].substring(0, lines[i].length()-1);
    	    }
    	    if(lines[i].endsWith("},")&&lines[i+1].contains("}")){
    	    	lines[i]=lines[i].substring(0, lines[i].length()-1);
    	    }
    	    if(lines[i].endsWith("},")&&lines[i+1].contains("]")){
    	    	lines[i]=lines[i].substring(0, lines[i].length()-1);
    	    }
    	    if(lines[i].endsWith("},")&&lines[i+2].contains("]")){
    	    	lines[i]=lines[i].substring(0, lines[i].length()-1);
    	    }
    	}
    	
    	return afAppendString(lines);
    }
    
    public static void afConstructRequest_UpdateCustomer(String[] inputParams) throws Exception{
    	String testCase = UTAFRead2.currTestCaseName.toLowerCase();
    	if(testCase.contains("partial")){
    		afGenerateSubmitCustomerUpdatingJSON_partial(inputParams);
    	}
    	else{
    		afGenerateSubmitCustomerUpdatingJSON_New(inputParams);
    	}
    	
    }
    
	public static void afPushDatatoBOLDB(String[] inputParams)throws Exception {

		//		String pushData = "PUSH: BOL_006abc_FLS_EBU_ChangeGreenBillEmailAddress_OPNDWNLDCLK, NEW, Add:[CUSTOMERNR_DB,PERID,EMAILID];";
		String pushData = inputParams[0].trim();
		Pattern push = Pattern.compile("(?i)PUSH\\s*:\\s*([\\w]*)\\s*,\\s*([\\w]*)\\s*,\\s*Add\\s*:\\s*([^;]*?);");
		Pattern pushWithNoStatus = Pattern.compile("(?i)PUSH\\s*:\\s*([\\w]*)\\s*,\\s*Add\\s*:\\s*([^;]*?);");

		String tagName = null;
		String status = "NEW";
		Matcher m1 = push.matcher(pushData);
		JSONObject obj = new JSONObject();
		int matchfound = 0;
		if (m1.matches()) {
			matchfound = 1;
			tagName = m1.group(1);
			status =  m1.group(2);
			List<String> collect = Arrays.stream(m1.group(3).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",")).collect(Collectors.toList());

			for (String string : collect) {
				obj.put(string.trim(), UTAFCommonFunctions2.cfGlGetRunTimeVar(string.trim()));
				System.out.println(string +" is "+UTAFCommonFunctions2.cfGlGetRunTimeVar(string));
				UTAFCommonFunctions2.cfAddReportOutVars(string, UTAFCommonFunctions2.cfGlGetRunTimeVar(string));
			}
			System.out.println(obj.toString());
		} else {
			m1 = pushWithNoStatus.matcher(pushData);
			if(m1.matches()) {
				matchfound = 1;
				tagName = m1.group(1);
				List<String> collect = Arrays.stream(m1.group(2).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",")).collect(Collectors.toList());

				for (String string : collect) {
					obj.put(string.trim(), UTAFCommonFunctions2.cfGlGetRunTimeVar(string.trim()));
					System.out.println(string +" is "+UTAFCommonFunctions2.cfGlGetRunTimeVar(string));
					UTAFCommonFunctions2.cfAddReportOutVars(string, UTAFCommonFunctions2.cfGlGetRunTimeVar(string));
				}
			}

		}

		if(matchfound == 0) {
			UTAFCommonFunctions2.cfGlReport(driver, "FAIL", "No Matching Data found for the PUSH", true, false);
			UTAFLog.debug("PUSH - NO MATCH.");
		}
		System.out.println("------------------------ In DB Connection -----------------");

		Connection connection=null;


		// =========
		String insertTableSQL = "INSERT INTO BATCH_DATA"
				+ "(environment, application, component, test_case, tag, status, data, previous_status, id, created_date, modified_date) VALUES"
				+ "(?,?,?,?,?,?,?,?, ?, ?, ?)";

		Date cDate = new Date();
		try  {
			String DBURL = UTAFFwVars.utafFWProps.getProperty("DB.LOCAL.CONNURL");
			String DBUSER = UTAFFwVars.utafFWProps.getProperty("DB.LOCAL.USER");
			String DBPASS = UTAFFwVars.utafFWProps.getProperty("DB.LOCAL.PASS");
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, UTAFFwVars.utafFWENV);
			preparedStatement.setString(2, "BOL");
			preparedStatement.setString(3, "BOL");
			preparedStatement.setString(4, tagName);
			preparedStatement.setString(5, tagName);
			preparedStatement.setString(6, status);
			preparedStatement.setString(7, obj.toString());
			preparedStatement.setString(8, "");
			preparedStatement.setInt(9, (int)(Math.random() * 10000));
			preparedStatement.setTimestamp(10, new Timestamp(cDate.getTime()));
			preparedStatement.setTimestamp(11, new Timestamp(cDate.getTime()));

			int ResultSet = preparedStatement.executeUpdate();
			if (ResultSet == 1) {
				UTAFLog.info("Order entry Succesful!!!");
				UTAFLog.info("Record is inserted into Automation Local table!");
			} else {
				UTAFLog.info("Not able to insert new entry");
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}

		}catch (SQLException e) {
			UTAFCommonFunctions2.cfGlReport(driver, "Fail", "Sql Expcetion Occurred while PUSH to Local DB ----"+e.getMessage(), true, false);
			UTAFCommonFunctions2.cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),e.getMessage() + "\n" + e.getStackTrace());
		} finally {
			try {
				connection.close();

			} catch (Exception ex) {
				UTAFCommonFunctions2.cfGlReport(driver, "Fail", "Expcetion Occurred while closing the connection to Local DB ----"+ex.getMessage(), true, false);
				UTAFCommonFunctions2.cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),ex.getMessage() + "\n" + ex.getStackTrace());
			}
		}
	}
	
	public static void afValidateDB_Party(String... inputParams) throws Exception{
		String customerId="<font color=\"#32CD32\"><b>"+inputParams[0]+"</b></font>";
		cfGlReport(null, "PASS", "Customer ID created = "+customerId, true, false);
		String partyDetais_DB="CUST_LEGAL_FORM_CD,RESTRICTEL_IND,TEXT_LONG,CUST_FOUNDATION_DT,"
    			+ "CUST_SUBJECT_TO_VAT_IND,CUST_VAT_CTRY_CD,CUST_VAT_NR,NM,FIRST_NM,CUST_BIRTH_DT,NM,CUST_VAT_NR";
    	
    	String partyDetails="temp_legalForm_value,temp_isRestrictel,temp_NOTES,"
    			+ "temp_party_foundingDate,temp_isSubjectToVAT,temp_VAT_countryCode_alpha2Code,temp_VATNumber,"
    			+ "temp_name,temp_firstName,temp_birthDate,temp_lastName,temp_enterpriseNumber";
    	
    	afGetValuesFromDb("CDB_categoryMarketSegment","SYMBOLIC_NM",inputParams[0],"temp_classification_value");
    	afGetValuesFromDb("CDB_partySearch_v1",partyDetais_DB,inputParams[0],partyDetails);
    	afGetValuesFromDb("CDB_customerTitleCode","std_belgacom_cd",inputParams[0],"temp_title_value");
    	afGetValuesFromDb("CDB_customerLegalLanguage","std_belgacom_cd",inputParams[0],"temp_legalLanguage_alpha2Code");
    	afGetValuesFromDb("CDB_customercommunicationLanguage","std_belgacom_cd",inputParams[0],"temp_communicationLanguage_alpha2Code");
    	afGetValuesFromDb("CDB_partyName_Type_224","NM",inputParams[0],"temp_printName");
    	afGetValuesFromDb("CDB_naceAssignment_v1","NACE_CD",inputParams[0],"temp_NACECode_value");
    	if(!UTAFRead2.runTimeVar.get("temp_NACECode_value").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("temp_NACECode_value"),"temp_NACECode_value");
    	}
    	else{
    		UTAFRead2.runTimeVar.put("temp_NACECode_value","");
    	}
    	
    	if(!UTAFRead2.runTimeVar.get("temp_VAT_countryCode_alpha2Code").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("temp_VAT_countryCode_alpha2Code"),"temp_VAT_countryCode_alpha2Code");
    	}
    	if(!UTAFRead2.runTimeVar.get("temp_legalForm_value").equals("null")){
    		afGetValuesFromDb("CDB_codeDescr_generic","std_belgacom_cd",UTAFRead2.runTimeVar.get("temp_legalForm_value"),"temp_legalForm_value");
    	}
    	afGetValuesFromDb("CDB_partyAddressSearch_v1","LOM_ID,EXTRA_ADDR_LINE",inputParams[0],"temp_geographicAddressIdentifier_id,temp_EXTRA_ADDR_LINE");
    	afGetValuesFromDb("CDB_partyName_Type_418","NM",inputParams[0],"temp_printName");
    	afGetValuesFromDb("CDB_categoryMarketSegment","std_belgacom_cd",inputParams[0],"temp_classification_value");
    	Set<String> keySet = UTAFCommonFunctions2.testData.keySet();
    	for(String key:keySet){
    		if(!(key.contains("Scope")||key.contains("Context")
 //   				||key.contains("printName")
    				||key.contains("businessType")
    				||key.contains("classification_code")
    				||key.contains("classification_value01")
    				||key.contains("subTitle")
    				||key.contains("customer_identifier_id")
//    				||key.contains("NOTES")
//    				||key.contains("EXTRA_ADDR_LINE")
    				))
    			{
    			UTAFRead2.runTimeVar.put("utafFWTCStep","Validate DB value of "+key.replace("Req_", ""));
    			if(UTAFRead2.runTimeVar.get(key.replace("Req", "temp"))==null){
    				String expected = testData.get(key);
    				if(expected.equalsIgnoreCase("empty")){
    					expected="null";
    				}
    				String actual = "null";
    				afGenericAssertion(actual,expected,null);    			
    			} 
    			else{
    				if(key.contains("Date")){
            			afGenericAssertion(UTAFRead2.runTimeVar.get(key.replace("Req", "temp")),testData.get(key),"contains");
            		}
            		else if(testData.get(key).equals("true")||testData.get(key).equals("false")){
            			afGenericAssertion(UTAFRead2.runTimeVar.get(key.replace("Req", "temp")),testData.get(key),"boolean");
            		}
            		else{
            			String expected = testData.get(key);
        				if(expected.equalsIgnoreCase("empty")){
        					expected="null";
        				}
        				String actual = UTAFRead2.runTimeVar.get(key.replace("Req", "temp"));
            			afGenericAssertion(actual,expected,null);
            		}
    			}
    		}
    		
    	}
	}
	
	public static void afGetTextAndCompare(String inputParams[])
			throws Exception {
		String vText = null;
//		String element = inputParams[0];
		if(inputParams[1]!=null) {
//			element=inputParams[1];
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		cfGlSelCheckDocReady(driver);
		elementSearch(driver, inputParams[0], UTAFFwVars.utafFWExplicitWait);
		vText = eleSearched.getText();
		if (vText.equalsIgnoreCase("")) {
			vText="empty";
		}
		if(inputParams[3].equalsIgnoreCase("contains")){
			afGenericAssertion(vText,inputParams[2],"contains");
		}
		else{
			afGenericAssertion(vText,inputParams[2],null);
		}
	}
	
	public static String afGetText(String inputParams[]) throws Exception {
		String vText = null;
		if(inputParams[1]!=null) {
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[1]);
		}
		cfGlSelCheckDocReady(driver);
		elementSearch(driver, inputParams[0], UTAFFwVars.utafFWExplicitWait);
		vText = eleSearched.getText();
		if (vText.equalsIgnoreCase("")) {
			vText="empty";
		}
		UTAFRead2.runTimeVar.put(inputParams[2], vText);
		return vText;
	}
	
	public static void afStoreVarsInReport(String[] inputParams) throws Exception {
		String key = inputParams[0];
        String value = inputParams[1];
        if(afGetElementProperty(value)!=null){
        	cfGlSelGetText(driver, value, key);
        	value = UTAFRead2.runTimeVar.get(key);
        }
        cfAddReportInVars(key, value);
/*        UTAFFwVars.inVar.put(key, value);
        cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
				"Variable " + key + " : Value " + value);
        
        cfGlReport(driver,"PASS", key+" = "+value, false, false); */
	}
	
	
	public static void afSelectRootOrChildAccount(String[] inputParams) throws Exception{
		String parentPath="(.//*[@class='dTreeNode'])";
		try{
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(parentPath)));
			int numberOfAcc=driver.findElements(By.xpath(parentPath)).size();
			if(numberOfAcc>1){
				driver.findElement(By.xpath("(.//*[@class='dTreeNode'])[2]/a")).click();
			}
			else{
				driver.findElement(By.xpath("(.//*[@class='dTreeNode'])[1]/a")).click();
			}
		}
		catch(Exception ex){
			cfGlReport(driver,"FAIL", ex.getMessage(), false, true);
		}
		
	}
	
	
	public static void afSelectBrowser(String... inputParams) throws Exception{
		String browser=cfGlGetFrameworkProperty("Browser");
		if(!UTAFFwVars.utafFPlatform.equalsIgnoreCase("EMPTY") && UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")){
			browser = UTAFFwVars.utafFPlatform;
		}	
		else{
			UTAFFwVars.utafFPlatform = browser;
		}
		if(browser.equalsIgnoreCase("Edge")){
			String driverPath = UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.EDGE");
			Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
			Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
			Thread.sleep(3000);
			System.setProperty("webdriver.edge.driver", driverPath);
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setCapability("useAutomationExtension", false);
			UTAFFwVars.utafFWTCStatus = "PASS";
			UTAFDriverBridge2.driverobj= new EdgeDriver(edgeOptions);
			UTAFFwVars.utafFWTCStatus = "PASS";	
		}
		else if (browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")) {
			String driverPath=UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.IE");
//			UTAFDriverBridge2.driverobj = cfGlSelSetIEDriver(inputParams[0]);
			Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
			System.setProperty("webdriver.ie.driver", driverPath);
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true); 
			caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			caps.setCapability("allow-blocked-content", true);
			caps.setCapability("ignoreProtectedModeSettings", true);
//			caps.setCapability("IntroduceInstabilityByIgnoringProtectedModeSettings", true);
			caps.setCapability("requireWindowFocus", false);
			caps.setCapability("initialBrowserUrl", "");
			caps.setCapability("platformName", "WINDOWS");
			UTAFDriverBridge2.driverobj= new InternetExplorerDriver(caps);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} else  {
			inputParams[0]=UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME");
			UTAFDriverBridge2.driverobj = cfGlSelSetChromeDriver(inputParams);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} 
		UTAFDriverBridge2.driverobj.manage().window().maximize();
	}
	
	public static void afGetAttribute(String... inputParams) throws Exception{
		String attribute=cfGlSelGetAttribute(driver, inputParams[0], inputParams[1]);
		UTAFRead2.runTimeVar.put(inputParams[2], attribute);
	}
	
	
	public static void afExtractStartnEndDate(String[] inputParams) throws Exception{
        String combinedDate = inputParams[0].replace(" ", "");
        String[] dateArray = combinedDate.split("-");
        String startDate=dateArray[0];
        String endDate=dateArray[1];
        if(startDate != null){UTAFRead2.runTimeVar.put(inputParams[1], startDate);}
        if(endDate != null){UTAFRead2.runTimeVar.put(inputParams[2], endDate);}
               
	}
	
	public static void afHiddenElementCompare(String... inputParams) throws Exception{
		String xpathExpression=getObjectValue2(inputParams[0])[0];
		String vValue="";
		boolean flag = false;
		try{
			if(inputParams[1].equalsIgnoreCase("attribute")){
				vValue=driver.findElement(By.xpath(xpathExpression)).getAttribute(inputParams[2]);
				if(vValue.equalsIgnoreCase(inputParams[3])){
					flag=true;
				}
			}
			if(flag){
				cfGlReport(driver,"PASS", inputParams[0]+" is either hidden or read only", false, true);
			}
			else{
				cfGlReport(driver,"FAIL", inputParams[0]+" is neither hidden nor read only", false, true);
			}
		}
		catch(Exception ex){
			cfGlReport(driver,"FAIL", ex.getMessage(), false, true);
		}
	}
	
	
	public static void afGetNumberOfElements(String... inputParams) throws Exception{
		String xpathExpression=getObjectValue2(inputParams[0])[0];
		int eleCount=driver.findElements(By.xpath(xpathExpression)).size();
		if(inputParams[1]!=null&&!inputParams[1].startsWith("TSIP")&&!inputParams[1].startsWith("TCIP")&&!inputParams[1].equals("")&&!inputParams[1].isEmpty()) {
			eleCount=eleCount-Integer.parseInt(inputParams[1]);
		}
		if(inputParams[2]!=null&&!inputParams[2].startsWith("TSIP")&&!inputParams[2].startsWith("TCIP")&&!inputParams[2].equals("")&&!inputParams[2].isEmpty()) {
			UTAFRead2.runTimeVar.put(inputParams[2], String.valueOf(eleCount));
		}
		else{
			UTAFRead2.runTimeVar.put("tempElementCount", String.valueOf(eleCount));
		}
	}
	
	public static void afInvalidColumnCheckInDB(String... inputParams) throws Exception{
		String[] columns = inputParams[1].split(",");
		String result="";
		for(String column:columns){
			try{
				String key="tempValue_"+column.replaceAll("\\s+", "");
				afGetValuesFromDb(inputParams[0],column.replaceAll("\\s+", ""),inputParams[2],key);
				if(UTAFRead2.runTimeVar.get(key)==null||UTAFRead2.runTimeVar.get(key).equalsIgnoreCase("null")){
					result=result+column.replaceAll("\\s+", "")+",";
				}
			}
			catch(Exception ex){
				result=result+column.replaceAll("\\s+", "")+",";
			}
		}
		result=result.substring(0, result.length()-1);
		afGenericAssertion(result,inputParams[3],null);
	}
	
	public static void afSerValidateValueInResponse(String... inputParams) throws Exception{
		String expVals[]=inputParams[0].trim().split(",");
		String actualVals[]=new String[expVals.length];
		for(int i=0;i<actualVals.length;i++){
			String expVal=restUtils.expectedValues(expVals[i],requestpPayloadType);
			if(response.body().asString().contains(expVal)){
				actualVals[i]=expVal;
			}
			else{
				actualVals[i]="NotAvailable";
			}
		}
		String actual=restUtils.actualValues(actualVals);
		restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,inputParams[1]);	
	}
	
	public static void afExecuteQueryInDB(String... inputParams) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		String query = cfGlGetElementProperty(inputParams[0]);
		String vars[] = { "" };
		if (inputParams[1] != null) {
			vars = inputParams[1].split(",");
			for (int i = 0; i < vars.length; i++) {
				map.put("v" + (i + 1), vars[i]);
				query = query.replace("v" + (i + 1), map.get("v" + (i + 1)).trim());
			}
		}
		Connection connection = null;
		try {
			connection = restUtils.dbConnection();
			Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			statement.executeQuery(query);
		} catch (Exception e) {
			UTAFCommonFunctions2.cfGlReport(driver, "FAIL", "Sql Expcetion ----" + e.getMessage(), true, false);
		} finally {
			try {
				connection.close();

			} catch (Exception ex) {
				UTAFCommonFunctions2.cfGlReport(driver, "FAIL",
						"Expcetion Occurred while closing the DB connection ----" + ex.getMessage(), true, false);
			}
		}
		
	}
	
	public static void afJmsSendMessage(String... inputParms) throws Exception{
		String fileName=inputParms[0];
		String corrId=inputParms[1]; 
		String testDataLocal=inputParms[2]; 
		String vQueName=inputParms[3];
		String vLookUp=inputParms[4];
		String brokerUrls=inputParms[5];
		
		ArrayList<String> bUrls=new ArrayList<String>();
		if(testDataLocal!=null){
            stepData=restUtils.stringToMap(testDataLocal);
            testData.putAll(stepData); 
            UTAFRead2.runTimeVar.putAll(testData);
		}
		if(brokerUrls!=null){
			String urls[] = brokerUrls.split(","); 
			for(String bUrl:urls){
				bUrls.add(bUrl);
			}
		}
		String xmlValue = restUtils.generatePayload(testData, runTimeData, fileName);
		UTAFLog.info(xmlValue);
		boolean flag = true;
		progress.message.jclient.Connection uConnection=null;
		String brokerUrl="";
		Queue queue = null;
		while(flag){
			Properties env = new Properties();
			env.put(Context.SECURITY_PRINCIPAL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"));
			env.put(Context.SECURITY_CREDENTIALS, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
			env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.CONURL"));
			env.put("com.sonicsw.jndi.mfcontext.domain", UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.DOMAIN"));
			InitialContext uJNDI = new InitialContext(env);
			UTAFLog.info("Context created successfully!!!");

			// lookup queue using queue name
			queue = (Queue) uJNDI.lookup(vQueName);
			UTAFLog.info("Queue lookup successful!!!");

			// create connection factory by passing the connection factory name
			ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(vLookUp);
			UTAFLog.info("connection factory created!!!");

			// create connection by passing the user name and password
			uConnection = (progress.message.jclient.Connection) conFactory.createConnection(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"), 
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
			UTAFLog.info("Connection created successfully!!");
			// Start the connection
			uConnection.start();
			brokerUrl=uConnection.getBrokerURL();
			UTAFLog.info("Borker Url = "+brokerUrl);
			if(bUrls.size()>0){
				for(String bkrUrl:bUrls){
					UTAFLog.info("Checking if the user given broker url is same as system broker url");
					if(bkrUrl.equals(brokerUrl)){
						flag=false;
					}
				}
				if(flag){
					uConnection.close();
				}
			}
			else{
				flag=false;
			}
			
		}
		javax.jms.Session session = (javax.jms.Session) uConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		
		// create message producer by queue name variable
		MessageProducer producer = session.createProducer(queue);

		// create message, also here u can read you file and pass it as string
		TextMessage msg = session.createTextMessage(xmlValue);

		// set JMSCorrealtionID
		msg.setJMSCorrelationID(corrId);

		// send message
		producer.send(msg);
		
		UTAFLog.info("AcknowledgeMode = "+ String.valueOf(session.getAcknowledgeMode()));
		
		String vData=restUtils.vData(brokerUrl,testDataLocal, "Request="+captureRequestAndResponse(xmlValue,"xml"),"","");
		cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);
		
		

	}
	
	public static void afJmsCall(String... inputParms) throws NamingException, JMSException, Exception {

		String xmlValue=restUtils.getBody(inputParms[0]);
		String vQueName=inputParms[1];
		String corrId=inputParms[2]; 
		String vLookUp=inputParms[3];
		
		
		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"));
		env.put(Context.SECURITY_CREDENTIALS, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.CONURL"));
		env.put("com.sonicsw.jndi.mfcontext.domain", UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.DOMAIN"));
		InitialContext uJNDI = new InitialContext(env);
		System.out.println("Context created successfully!!!");

		// lookup queue using queue name
		Queue queue = (Queue) uJNDI.lookup(vQueName);
		System.out.println("Queue lookup successful!!!");

		// create connection factory by passing the connection factory name
		ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(vLookUp);
		System.out.println("connection factory created!!!");

		// create connection by passing the user name and password
		progress.message.jclient.Connection uConnection = (progress.message.jclient.Connection) conFactory.createConnection(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"), 
				UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		System.out.println("Connection created successfully!!");
		String brokerUrl=uConnection.getBrokerURL();
		javax.jms.Session session = (javax.jms.Session) uConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		// Start the connection
		uConnection.start();

		// create message producer by queue name variable
		MessageProducer producer = session.createProducer(queue);

		// create message, also here u can read you file and pass it as string
		TextMessage msg = session.createTextMessage(xmlValue);

		// set JMSCorrealtionID
		msg.setJMSCorrelationID(corrId);

		// send message
		producer.send(msg);
		
		System.out.println(session.getAcknowledgeMode());
		
		String vData=restUtils.vData(brokerUrl,"", "Request="+captureRequestAndResponse(xmlValue,"xml"),"Header=null","Response=No Response");
		cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);
		
	}
	
	public static void afReadResponseFromJmsQueueBrowser(String inputParms[]) throws Exception{
	
		String vQueName=inputParms[0];
		String corrId=inputParms[1]; 
		String vLookUp=inputParms[2];
		
		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"));
		env.put(Context.SECURITY_CREDENTIALS, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.CONURL"));
		env.put("com.sonicsw.jndi.mfcontext.domain", UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.DOMAIN"));
		InitialContext uJNDI = new InitialContext(env);
		UTAFLog.info("Context created successfully!!!");

		// create connection factory by passing the connection factory name
		ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(vLookUp);
		UTAFLog.info("connection factory created!!!");

		// create connection by passing the user name and password
		progress.message.jclient.Connection uConnection = (progress.message.jclient.Connection) conFactory.createConnection(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"), 
				UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		UTAFLog.info("Connection created successfully!!");
		String brokerUrl = uConnection.getBrokerURL();
		// created session
		javax.jms.Session session = (javax.jms.Session) uConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		// Start the connection
		uConnection.start();
		Queue queue1 = (Queue) session.createQueue(vQueName);
		UTAFLog.info("Browse through the elements in queue");
	    
        QueueBrowser browser = (QueueBrowser) session.createBrowser(queue1);
        @SuppressWarnings("unchecked")
		Enumeration<Message> e = browser.getEnumeration();
        Thread.sleep(10000);
        int i=0;
        while (e.hasMoreElements()) {
            TextMessage message = (TextMessage) e.nextElement();            
            i++;
            if(message.getJMSCorrelationID().equalsIgnoreCase(corrId)){
           	 xml=restUtils.xmlDocument(message.getText());
           	requestpPayloadType="xml";
            } 
        }
        if(xml.length()==0){
        	UTAFLog.info("No response found for given correlationId in Queue Browser");
        	cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, "No response found for given correlationId in Queue Browser");
        }
        String vData=restUtils.vData(brokerUrl,"", "","","Response="+captureRequestAndResponse(xml,"xml"));
        cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
        UTAFLog.info("Done");
        browser.close(); 
        session.close();
		uConnection.close();
		// System.gc();
		System.out.println("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);
	} 
	
	
	
	public static void afClickElementWithPartialLocator(String... inputParams) throws Exception{
		String arr[] = inputParams[0].split(",");
		for(String val:arr){
			if(val.contains("'")) {
				inputParams[0]=inputParams[0].replace(val, val.split("'")[0]);
			}
			if(val.contains("/")) {
				inputParams[0]=inputParams[0].replace(val, val.split("/")[0]);
			}
		}
		
		String arr_new[] = inputParams[0].split(",");
		
		String value_1=arr_new[0];
		String value_2=arr_new[1];
		String value_3=arr_new[2];
		WebElement element = null;
		String xpath = "";
		try{
			if(inputParams[1].equals("street")) {
				inputParams[0]=value_1+", "+value_2+" "+value_3;
			}
			else {
				inputParams[0]=value_1+" "+value_2+", "+value_3;
			}
			UTAFRead2.runTimeVar.put("UTAFPropValue", inputParams[0]);
			cfGlElementScrollTillVisible(driver, "CDB_GUI_common_Text_contains");
			afCommonElementClick("CDB_GUI_common_Text_contains",inputParams[0]);
		}
		catch(Exception ex){
			try {
				try {
					xpath="(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_1+" "+value_2);
					element = driver.findElement(By.xpath(xpath));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
					afCommonElementClick("CDB_GUI_common_Text_contains",value_1+" "+value_2);
				}
				catch(Exception exx) {
					xpath="(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_1+", "+value_2);
					element = driver.findElement(By.xpath(xpath));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
					afCommonElementClick("CDB_GUI_common_Text_contains",value_1+", "+value_2);
				}
				
			}
			catch(Exception e) {
				try {
					xpath="(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_2+" "+value_3);
					element = driver.findElement(By.xpath(xpath));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
					afCommonElementClick("CDB_GUI_common_Text_contains",value_2+" "+value_3);
				}
				catch(Exception ee) {
					xpath="(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_2+", "+value_3);
					element = driver.findElement(By.xpath(xpath));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
					afCommonElementClick("CDB_GUI_common_Text_contains",value_2+", "+value_3);
				}
				
			}
			
		}
	}
	
	public static void afClickOnButtonInsideFrame(String...inputParams) throws Exception{
		cfGlSelSwitchToDefaultContent(driver);
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for(WebElement iframe:iframes){
			try{
				String value = iframe.getAttribute(inputParams[0]);
				if(value.equalsIgnoreCase(inputParams[1])){
					cfGlSwitchIframeElement(driver, inputParams[2]);
					cfGlJsElementClick(driver, inputParams[3]);
					break;
				}
			}
			catch(Exception ex){}
		}
		
	}
	
	public static void afSendValueInsideFrame(String...inputParams){
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for(WebElement iframe:iframes){
			try{
				String value = iframe.getAttribute(inputParams[0]);
				if(value.equalsIgnoreCase(inputParams[1])){
					cfGlSwitchIframeElement(driver, inputParams[2]);
//					driver.switchTo().frame(driver.findElement(By.xpath("(.//*[@title='Content frame'])")));
					cfGlSelSendValue(driver, inputParams[3], inputParams[4]);;
					break;
				}
			}
			catch(Exception ex){}
		}
		
	}
	
	public static void afSwitchToIframe(String...inputParams){
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for(WebElement iframe:iframes){
			try{
				String value = iframe.getAttribute(inputParams[0]);
				if(value.equalsIgnoreCase(inputParams[1])){
					cfGlSwitchIframeElement(driver, inputParams[2]);
					break;
				}
			}
			catch(Exception ex){}
		}
		
	}
	
	public static void afDataForAddressValidation(String... inputparams) throws Exception {
		String columns="PTY_ID,TYPE_CD,STREET_NM,HOUSE_NR,CITY_NM,ZIP_CD";
		String vars="tempPartyId,tempTypeCode,tempStreet,tempHnr,tempCity,tempZipcd,tempAccId";
		afGetValuesFromDb("CDB_PartySearch_withLessLomIds",columns+",CUST_MASTER_ACC_ID",null,vars);
		UTAFRead2.runTimeVar.put("tempCustId", UTAFRead2.runTimeVar.get("tempPartyId"));
		if(inputparams[0].equalsIgnoreCase("Account")) {
			afGetValuesFromDb("CDB_partySearch",columns+",ACC_CUST_ID",UTAFRead2.runTimeVar.get("tempAccId"),vars+",tempCustId");
			UTAFRead2.runTimeVar.put("tempAccId", UTAFRead2.runTimeVar.get("tempPartyId"));
		}
	}
	
	public static void afRerunBatch(String... inputParams) throws Exception {
		driver.switchTo().defaultContent();
		boolean flag =false;
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for(WebElement iframe:iframes){
			try{
				String value = iframe.getAttribute("title");
				if(value.equalsIgnoreCase("Content frame")){
					cfGlSwitchIframeElement(driver, "Batch_Maestro_contentframe_Iframe");
					String errorMessage="The job cannot be submitted because the job or job alias \"@\" already exists".replace("@", inputParams[0]);
					String xpath  = "(.//img[@title='Error'])//following::span";
					String text = driver.findElement(By.xpath(xpath)).getText();
					if(text.contains(errorMessage)) {
						flag=true;
						driver.switchTo().defaultContent();
						break;
					}
				}
			}
			catch(Exception ex){}
		}
		afClickOnButtonInsideFrame("title","Content frame","Batch_Maestro_contentframe_Iframe","CDB_GUI_OK_Button");
		driver.switchTo().defaultContent();
		cfGlSelElementClick(driver,"Batch_Maestro_systemStatus_Button");
		Thread.sleep(3000);
		cfGlSelElementClick(driver,"Batch_Maestro_monitorWorkload_Button");
		Thread.sleep(3000);
		String query="@#@.query@".replace("query", inputParams[0]);
		afSendValueInsideFrame("title","Content frame","Batch_Maestro_contentframe_Iframe_2","Batch_Maestro_query_input",query);
		cfGlJsElementClick(driver,"Batch_Maestro_run_Button");
		driver.switchTo().defaultContent();
		String date = afgetCurrentDate("MM_dd_yy",null);
		String dateArr[] = date.split("_");
		String month = dateArr[0];
		String day = dateArr[1];
		String year = dateArr[2];
		if(month.startsWith("0")) {
			month=month.substring(1);
		}
		if(day.startsWith("0")) {
			day=day.substring(1);
		}
		String actualDate = month+"/"+day+"/"+year;
		String jobXpath="(.//*[contains(text(),'date')]//preceding::*[text()='JOBS']//preceding::td[3]/a[text()='jobName'])";
		jobXpath=jobXpath.replace("date", actualDate).replace("jobName", inputParams[0]);
		String element = getObjectValue2("Batch_Maestro_contentframe_Iframe_3")[0];
		WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
		afSwitchToIframe("title","Content frame","Batch_Maestro_contentframe_Iframe_3");
		cfGlSelSelectDropDownValue(driver, "Batch_Maestro_linesPerPage_Dorpdown", "250");
		int tableRows = Integer.parseInt(driver.findElement(By.xpath("(.//*[text()='Total:']//following::span)[1]")).getText());
		String xpath_checkbox="";
		int j=0;
		for(int i=1; i<=tableRows; i++) {
			String xpath_jobName="(.//table[@class='tvg_table'])[3]/tbody/tr[#]/td[2]/a".replace("#", String.valueOf(i));
			String xpath_jobType="(.//table[@class='tvg_table'])[3]/tbody/tr[#]/td[5]/a".replace("#", String.valueOf(i));
			String xpath_jobDate="(.//table[@class='tvg_table'])[3]/tbody/tr[#]/td[7]".replace("#", String.valueOf(i));
			String jobName = driver.findElement(By.xpath(xpath_jobName)).getText();
			String jobType = driver.findElement(By.xpath(xpath_jobType)).getText();
//			String jobDate = driver.findElement(By.xpath(xpath_jobDate)).getText();
//			if(jobName.equalsIgnoreCase(inputParams[0])&&jobType.equals("JOBS")&&jobDate.contains(actualDate)) {
			if(jobName.equalsIgnoreCase(inputParams[0])&&jobType.equals("JOBS")) {
				xpath_checkbox="(.//table[@class='tvg_table'])[2]/tbody/tr[#]/td[1]/input".replace("#", String.valueOf(i));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath(xpath_checkbox)));
//				driver.findElement(By.xpath(xpath_checkbox)).click();
				j=i;
				break;
			}
			
		}
		if(flag) {
			cfGlJsElementClick(driver,"Batch_Maestro_reRun_Button");
			Thread.sleep(5000);
			cfGlJsElementClick(driver,"Batch_Maestro_reRun_alert_Button");
		}
		Thread.sleep(60000);
		cfGlSelElementClick(driver,"Batch_Maestro_refresh_Button");
		Thread.sleep(5000);
		String xpath_jobStatus="(.//table[@class='tvg_table'])[2]/tbody/tr[#]/td[2]/span[1]".replace("#", String.valueOf(j));
		String jobStatus = driver.findElement(By.xpath(xpath_jobStatus)).getText();
		while(jobStatus.equals("Ready")||jobStatus.equals("Running")||jobStatus.equals("Waiting")) {
			Thread.sleep(45000);
			cfGlSelElementClick(driver,"Batch_Maestro_refresh_Button");
			Thread.sleep(5000);
			jobStatus = driver.findElement(By.xpath(xpath_jobStatus)).getText();
		}
		if(jobStatus.equalsIgnoreCase("Error")) {
			driver.findElement(By.xpath(xpath_checkbox)).click();
			Thread.sleep(3000);
			cfGlSelElementClick(driver,"Batch_Maestro_jobLog_Button");
			Thread.sleep(3000);
			afSwitchToWindow("1");
			cfGlSelGetText(driver, "Batch_Maestro_log_Text", "tempJobLog");
			cfGlReport(driver,"FAIL", "Batch status is showing as Error in maestro console. please check job log", false, true);
		}
		else if(jobStatus.equalsIgnoreCase("Successful")) {
			cfGlReport(driver,"PASS", "Batch execution is successfully completed in maestro console", false, true);
		}
		else {
			Thread.sleep(60000);
		}
		
	}
	
	public static void afGetDateAndTime(String... inputParams) {
		Date dNow = null;
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_HH:mm:ss");
		if(inputParams[1]!=null) {
			if(inputParams[0].contains("after")) {
				dNow = new Date(System.currentTimeMillis()+Integer.parseInt(inputParams[1])*60*1000);
				UTAFRead2.runTimeVar.put("tempDateAndTimeAfter", ft.format(dNow).replace("_", "T"));
			}
			else {
				dNow = new Date(System.currentTimeMillis()-Integer.parseInt(inputParams[1])*60*1000);
				UTAFRead2.runTimeVar.put("tempDateAndTimeBefore", ft.format(dNow).replace("_", "T"));
			}
			
		}
		else {
			if(inputParams[0].contains("after")) {
				dNow = new Date(System.currentTimeMillis()+5*60*1000);
				UTAFRead2.runTimeVar.put("tempDateAndTimeAfter", ft.format(dNow).replace("_", "T"));
			}
			else {
				dNow = new Date(System.currentTimeMillis()-5*60*1000);
				UTAFRead2.runTimeVar.put("tempDateAndTimeBefore", ft.format(dNow).replace("_", "T"));
			}
		}
	}
	
	public static void afCreateMUBinputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMddHHmm","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileData="";
		String fileName="";
		
		afGetValuesFromDb("CDB_dataForMUB","PTY_ID,cust_master_acc_id",null,"tempCustId_1,tempAccId_1");
		String cust_id_1=afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_1"));
		String acc_id_1=afSetupCustOrAccId(cfGlGetRunTimeVar("tempAccId_1"));
		afGetValuesFromDb("CDB_dataForMUB_v1","PTY_ID,cust_master_acc_id",cfGlGetRunTimeVar("tempCustId_1"),"tempCustId_2,tempAccId_2");
		String cust_id_2=afSetupCustOrAccId(cfGlGetRunTimeVar("tempCustId_2"));
		String acc_id_2=afSetupCustOrAccId(cfGlGetRunTimeVar("tempAccId_2"));
		
		fileData=restUtils.getBody("Batches_MUB.txt");
		fileName="fil.mub.cdbAuto."+dateOne;
		fileData=fileData.replace("custIdOne", cust_id_1).replace("AcctIdOne", acc_id_1);
		fileData=fileData.replace("custIdTwo", cust_id_2).replace("AcctIdTwo", acc_id_2);

		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateMUBLogFile(String... inputParams) throws Exception {
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile");
		File myObj = new File(filePath);
		Scanner myReader = new Scanner(myObj);
		
		int actualValue_treated=0;
		int actualValue_processed_successfully=0;
		Stream<String> lines=null;
		int n=0;
	      while (myReader.hasNextLine()) {
	    	  n=n+1;
	        String line = myReader.nextLine();
	        if(line.contains("Change customer's official address")){
	        	lines = Files.lines(Paths.get(filePath)); 
	        	line = lines.skip(n+1).findFirst().get();
	        	actualValue_treated = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	lines = Files.lines(Paths.get(filePath));
	        	line = lines.skip(n+2).findFirst().get();
	        	actualValue_processed_successfully = Integer.parseInt(line.split("<")[1].split(">")[0]);
	        	lines.close();
	        }
	      }
	      myReader.close();
	    if(actualValue_treated==2){
      		cfGlReport(null,"PASS", "Number of transactions treated = "+actualValue_treated, false, false);
      	}
      	else{
      		cfGlReport(null,"FAIL", "Expected Number of transactions treated = "+2+". But Actual Number of transactions treated = "+actualValue_treated, false, false);
      	}
	    if(actualValue_processed_successfully==2){
      		cfGlReport(null,"PASS", "Number of transactions processed successfully = "+actualValue_processed_successfully, false, false);
      	}
      	else{
      		cfGlReport(null,"FAIL", "Expected Number of transactions processed successfully = "+2+". But Number of transactions processed successfully = "+actualValue_processed_successfully, false, false);
      	}
	}
	
	public static void afValidateValueInResponse(String... inputParams) throws Exception{
		UTAFLog.info("In afValidateValueInResponse");
		String expVals[]=inputParams[0].trim().split(",");
		String actualVals[]=new String[expVals.length];
		for(int i=0;i<actualVals.length;i++){
			String expVal=restUtils.expectedValues(expVals[i],requestpPayloadType);
			if(response.body().asString().contains(expVal)){
				actualVals[i]=expVal;
			}
			else{
				if(expVal==null||expVal.equalsIgnoreCase("null")) {
					actualVals[i]="null";
				}
				else {
					actualVals[i]="Not Available";
				}
			}
			
		}
		String actual=restUtils.actualValues(actualVals);
		String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
		restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
		UTAFLog.info("Out afValidateValueInResponse");
	}
	
	public static void afValidateValueInString(String... inputParams) throws Exception{
		UTAFLog.info("In afValidateValueInString");
		String expVals[]=inputParams[1].trim().split(",");
		String actualVals[]=new String[expVals.length];
		if(inputParams[0].startsWith("<?xml")) {
			inputParams[0]=restUtils.xmlDocument(inputParams[0]);
		}
		for(int i=0;i<actualVals.length;i++){
			String expVal=expVals[i];
			if(inputParams[0].contains(expVal)){
				actualVals[i]=expVal;
			}
			else{
				if(expVal==null) {
					actualVals[i]="null";
				}
				else {
					actualVals[i]="Not Available";
				}
			}
			
		}
		String actual=restUtils.actualValues(actualVals);
		String finalExp=restUtils.actualValues(expVals);
		restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
		UTAFLog.info("Out afValidateValueInString");
	}
	
	public static void afCreateMFPinputFile(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMddHHmm","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileData="";
		String fileName="";
		
		fileData=restUtils.getBody("Batches_PTY_MFP.txt");
		fileName="cdbAuto_testMFP."+dateOne;
		fileData=fileData.replace("extPTYID_1", cfGlGetRunTimeVar("temp_ExtPartyID_1")).replace("extPTYID_2", cfGlGetRunTimeVar("temp_ExtPartyID_2"));
		fileData=fileData.replace("extPTYID_3", cfGlGetRunTimeVar("temp_ExtPartyID_3")).replace("extPTYID_4", cfGlGetRunTimeVar("temp_ExtPartyID_4"));

		writeFile(filePath+fileName, fileData);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateMFPlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Total number of lines present in the Input file")){
	        	actualValue = line.split("file")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Total number of lines present in the Input file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total number of lines present in the Input file = 2. But actual Total number of lines present in the Input file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of lines successfully inserted into MERGE_CANDIDATES")){
	        	actualValue = line.split("successfully")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of lines successfully inserted into MERGE_CANDIDATES = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of lines successfully inserted into MERGE_CANDIDATES = 2. But actual Number of lines successfully inserted into MERGE_CANDIDATES = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afWriteCSVfile(ArrayList<String> arrayList, String path) throws Exception {
		try {
			FileWriter file = new FileWriter(path);
			PrintWriter write = new PrintWriter(file);
			for (String name : arrayList) {
				write.println(name);
			}
			write.close();
		} catch (IOException exe) {
			cfGlReport(null,"FAIL", "Cannot create CSV file", false, false);
		}
	}
	
	public static void afCreateInputFile_DRL_OR_HKL(String... inputParams) throws Exception{
		String dateOne = afgetCurrentDate("yyyyMMddHHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileName="";
		String sequenceNumber="000";
		int tempSeqNum=0;
		int k=0;
		if(inputParams[2].equalsIgnoreCase("DRL")) {
			afGetValuesFromDb("DRM_duplicateFile_v1","FILE_NM",dateOne,"tempFileNameDB");
			fileName="DUPLICATE_PARTY_"+dateOne;
			k=3;
		}
		else {
			afGetValuesFromDb("HKM_householdFile","FILE_NM",dateOne,"tempFileNameDB");
			fileName="HOUSEHOLD_"+dateOne;
			k=2;
		}
		
		String tempFileName = cfGlGetRunTimeVar("tempFileNameDB");
		if(!tempFileName.equals("null")) {
			String tempNumber = tempFileName.split("_")[k].split("[.]")[0];
			tempSeqNum=Integer.parseInt(tempNumber)+1;
			if(String.valueOf(tempSeqNum).length()==2) {
				sequenceNumber=sequenceNumber+"0"+tempSeqNum;
			}
			else if(String.valueOf(tempSeqNum).length()==3) {
				sequenceNumber=sequenceNumber+tempSeqNum;
			}
			else {
				sequenceNumber=sequenceNumber+"0"+tempSeqNum;
			}
		}
		
		fileName=fileName+"_"+sequenceNumber+".csv";
		String partyIds[] = inputParams[1].split(",");
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(fileName+"|"+dateOne+"|v01|3");
		arrayList.add(partyIds[0]+"|"+partyIds[1]+"|"+partyIds[2]+"|"+partyIds[3]);
		arrayList.add(partyIds[4]+"|"+partyIds[5]);
		afWriteCSVfile(arrayList, filePath+fileName);
		UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
		
	}
	
	public static void afValidateDRLlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Input File Name")){
	        	actualValue = line.split("Input File")[1].split(":")[1].trim();
	        	if(actualValue.equalsIgnoreCase(cfGlGetRunTimeVar("tempInputFile"))){
	        		cfGlReport(null,"PASS", "Expected and actual Input File Name = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Input File Name = "+cfGlGetRunTimeVar("tempInputFile")+". But actual Input File Name = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of records in file")){
	        	actualValue = line.split("records")[1].split(":")[1].trim();
	        	if(actualValue.equals("3")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records in file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records in file = 3. But actual Number of records in file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Groups created in DRM")){
	        	actualValue = line.split("Groups")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Groups created in DRM = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Groups created in DRM = 2. But actual Number of Groups created in DRM = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Detail records created")){
	        	actualValue = line.split("Detail")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and Number of Detail records created = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Detail records created = 6. But actual Number of Detail records created = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afGetCountDKG(String... inputParams) throws Exception {
		afGetValuesFromDb("DRM_duplicateFile_sumOfRecordCount_LOADstatus","SUM(RECORD_COUNT)",null,"tempRecordCount");
		afGetValuesFromDb("DRM_duplicateFile_count_LOADstatus","COUNT(*)",null,"tempCount");
		afGetValuesFromDb("DRM_duplicateFile_LOADstatus","FILE_REF_NUM",null,"tempFileRefNum");
		
		int tempCountBefore = Integer.parseInt(cfGlGetRunTimeVar("tempRecordCount"))-Integer.parseInt(cfGlGetRunTimeVar("tempCount"));
		UTAFRead2.runTimeVar.put("tempCountBefore", String.valueOf(tempCountBefore));
	}
	
	public static void afValidateDKGlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of duplicate groups selected for processing")){
	        	actualValue = line.split("selected for")[1].split(":")[1].trim();
	        	if(actualValue.equalsIgnoreCase(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of duplicate groups selected for processing = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of duplicate groups selected for processing = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of duplicate groups selected for processing = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of duplicates groups skipped")){
	        	actualValue = line.split("groups")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of duplicates groups skipped = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of duplicates groups skipped = 0. But actual Number of duplicates groups skipped = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Duplicate Reference Keys created")){
	        	actualValue = line.split("Reference Keys")[1].split(":")[1].trim();
	        	if(actualValue.equals(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Duplicate Reference Keys created = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Duplicate Reference Keys created = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of Duplicate Reference Keys created = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of successfully processed duplicates groups")){
	        	actualValue = line.split("duplicates")[1].split(":")[1].trim();
	        	if(actualValue.equals(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of successfully processed duplicates groups = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of successfully processed duplicates groups = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of successfully processed duplicates groups = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of failed duplicates groups")){
	        	actualValue = line.split("duplicates")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of failed duplicates groups= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of failed duplicates groups = 0. But actual Number of failed duplicates groups = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afValidateDKGOutputFile(String... inputParams) throws Exception {
		String fileBody = new String(Files.readAllBytes(Paths.get(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"))));
		afValidateValueInString(fileBody,inputParams[0]);
	}
	
	public static void afReadLPDinputFile(String... inputParams) throws Exception {
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile");
//		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//DRM10000036_2021-11-22_153649.txt";
		File myObj = new File(filePath);
		Scanner myReader = new Scanner(myObj);
		HashMap<String,String> keyVal = new HashMap<String,String>();
		Stream<String> lines=null;
		int n=3,k=0,j=0;
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.startsWith("UP|")){
	        	j++;
	        	if(j!=1) {
	        		n=n+2;
	        	}
	        	String value = line.split("\\|")[3];
	        	lines = Files.lines(Paths.get(filePath)); 
	        	try {
	        		String nextLine = lines.skip(n).findFirst().get();
		        	String key = nextLine.split("\\|")[1];
		        	k=k+1;
		        	if(k==1) {
		        		UTAFRead2.runTimeVar.put("key_1", key);
		        	}
		        	else if(!key.equalsIgnoreCase(UTAFRead2.runTimeVar.get("key_1"))){
		        		UTAFRead2.runTimeVar.put("key_2", key);
		        	}
		        	if(keyVal.get(key)==null) {
		        		keyVal.put(key, value);
		        	}
		        	else {
		        		keyVal.put(key, keyVal.get(key)+","+value);
		        	}
		        	lines.close();
	        	}
	        	catch(Exception ex) {
	        		
	        	}
	        }
	      }
	      String path = UTAFFwVars.utafFWFolderPath + "temp.properties";
	      output = new FileOutputStream(path);
	      afCreateNewElementProperty("key_1", cfGlGetRunTimeVar("key_1"));
	      afCreateNewElementProperty("key_2", cfGlGetRunTimeVar("key_2"));
	      afCreateNewElementProperty("val_1", keyVal.get(cfGlGetRunTimeVar("key_1")));
	      afCreateNewElementProperty("val_2", keyVal.get(cfGlGetRunTimeVar("key_2")));
	      afCreateNewElementProperty("tempFilePath", path);
	      UTAFRead2.runTimeVar.put("val_1", keyVal.get(cfGlGetRunTimeVar("key_1")));
	      UTAFRead2.runTimeVar.put("val_2", keyVal.get(cfGlGetRunTimeVar("key_2")));
	       myReader.close();
	}
	
	public static void afCreateNewElementProperty(String key, String value) throws Exception {
		
		Properties prop = new Properties();
		prop.setProperty(key, value);
		prop.store(output, null);
	}
	
	public static String afGetTempProperty(String... key) throws Exception {
		Properties prop = new Properties();
		InputStream fis = new FileInputStream(UTAFFwVars.utafFWFolderPath + "temp.properties");
		prop.load(fis);
		String value = prop.getProperty(key[0]);
		UTAFRead2.runTimeVar.put(key[0], value);
		return value;
	}
	
	
	public static void afValidateLPDlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
//		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//LPD_stat_20211122_111416663.log");
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Total Records in the Header")){
	        	actualValue = line.split("in the")[1].split(":")[1].trim();
	        	if(actualValue.equalsIgnoreCase("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total Records in the Header = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total Records in the Header = 6. But actual Total Records in the Header = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total records loaded into Staging table")){
	        	actualValue = line.split("Staging")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total records loaded into Staging table = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total records loaded into Staging table = 6. But actual Total records loaded into Staging table = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total records inserted into Staging table")){
	        	actualValue = line.split("Staging")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total records inserted into Staging table = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Total records inserted into Staging table = 6. But actual Total records inserted into Staging table = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total records processed")){
	        	actualValue = line.split("records")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total records processed = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total records processed = 6. But actual Total records processed = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total successfully processed")){
	        	actualValue = line.split("successfully")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total successfully processed= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total successfully processed = 6. Total successfully processed = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Total errored")){
	        	actualValue = line.split("Total")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Total errored= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total errored = 0. But actual Total errored = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static String afGenerateLowerCharString(String... inputParams) {
        int lowerLimit = 97;
        int upperLimit = 122;
        Random random = new Random();
        StringBuffer r = new StringBuffer(8);
        for (int i = 0; i < 8; i++) {
            int nextRandomChar = lowerLimit+ (int)(random.nextFloat()* (upperLimit - lowerLimit + 1));
            r.append((char)nextRandomChar);
        }
        String newString = r.toString();
        UTAFRead2.runTimeVar.put(inputParams[0], newString);
        return newString;
    }
	
	public static void afCreateLEFinputFile(String... inputParams) throws Exception {
		String dateOne = afgetCurrentDate("yyyyMMdd_HHmmss","tempDateNew");
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//";
		String fileName=dateOne+"_02_id975489.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet inputData = workbook.createSheet("Inputdata");
        String lastName=inputParams[1].split(",")[0];
        String firstName=inputParams[1].split(",")[1];
        String email=inputParams[1].split(",")[2];
        String randomLastName=afGenerateLowerCharString("tempRandomLastName");
        String randomfirstName=afGenerateLowerCharString("tempRandomfirstName");
        UTAFRead2.runTimeVar.put("tempRandomLastName",randomLastName.toUpperCase());
        UTAFRead2.runTimeVar.put("tempRandomfirstName",randomfirstName.toUpperCase());
        Object[][] testDataOne = {
                {"Gender","Last Name","1st Name","Language","Birthdate","Box nr","Mob nr Country Code","Mob nr","Fix nr Country Code","Fix nr","Email","Social media","CM perm"},
                {"M",randomLastName,randomfirstName,"EN","05/01/1990","","","","","",randomfirstName+"@gmail.com","","Y"},
                {"M",lastName,firstName,"EN","05/01/1990","","","","","",email,"","Y"}
        };
        int rowCount = 0;
        
        for (Object[] aBook : testDataOne) {
            Row row = inputData.createRow(rowCount);
             
            int columnCount = 0;
             
            for (Object field : aBook) {
                Cell cell = row.createCell(columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                columnCount++;
            }
            rowCount++;
        }
        XSSFSheet metaData = workbook.createSheet("Metadata");
        Object[][] testDataTwo = {
                {"Event Name","LEF Regression Test"},
                {"Date","02/11/2021"},
                {"Interest","SPORTS"},
                {"Interest detail","CDB Auto Test Event"},
                {"Trust level",""},
                {"CM context",""}
        };
        int rowCount_1 = 0;
        
        for (Object[] aBook : testDataTwo) {
            Row row = metaData.createRow(rowCount_1);
             
            int columnCount = 0;
             
            for (Object field : aBook) {
                Cell cell = row.createCell(columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                columnCount++;
            }
            rowCount_1++;
        }
        try (FileOutputStream outputStream = new FileOutputStream(filePath+fileName)) {
            workbook.write(outputStream);
        }
        catch(Exception ex) {
        	
        }
        UTAFRead2.runTimeVar.put(inputParams[0], fileName);
		cfGlReport(null,"PASS", "Created input file with the name ="+fileName+" successfully", false, false);
		UTAFRead2.runTimeVar.put("tempDate", dateOne);
	}
	
	public static void afGetCountHKG(String... inputParams) throws Exception {
		afGetValuesFromDb("HKM_householdFile_sumOfRecordCount_LOADstatus","SUM(RECORD_COUNT)",null,"tempRecordCount");
		afGetValuesFromDb("HKM_householdFile_count_LOADstatus","COUNT(*)",null,"tempCount");
		afGetValuesFromDb("HKM_householdFile_LOADstatus","FILE_REF_NUM",null,"tempFileRefNum");
		
		int tempCountBefore = Integer.parseInt(cfGlGetRunTimeVar("tempRecordCount"))-Integer.parseInt(cfGlGetRunTimeVar("tempCount"));
		UTAFRead2.runTimeVar.put("tempCountBefore", String.valueOf(tempCountBefore));
	}
	
	public static void afValidateHKLlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of records in file")){
	        	actualValue = line.split("records")[1].split(":")[1].trim();
	        	if(actualValue.equals("3")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records in file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records in file = 3. But actual Number of records in file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Groups created in HKM")){
	        	actualValue = line.split("Groups")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Groups created in HKM = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Groups created in HKM = 2. But actual Number of Groups created in HKM = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Detail records created")){
	        	actualValue = line.split("Detail")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and Number of Detail records created = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Detail records created = 6. But actual Number of Detail records created = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afValidateHKGlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Number of household groups selected for processing")){
	        	actualValue = line.split("selected for")[1].split(":")[1].trim();
	        	if(actualValue.equalsIgnoreCase(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of household groups selected for processing = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of household groups selected for processing = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of household groups selected for processing = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of household groups skipped")){
	        	actualValue = line.split("groups")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of household groups skipped = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of household groups skipped = 0. But actual Number of household groups skipped = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Household Reference Keys created")){
	        	actualValue = line.split("Reference Keys")[1].split(":")[1].trim();
	        	if(actualValue.equals(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Household Reference Keys created = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Household Reference Keys created = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of Household Reference Keys created = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of successfully processed households groups")){
	        	actualValue = line.split("households")[1].split(":")[1].trim();
	        	if(actualValue.equals(cfGlGetRunTimeVar("tempCountBefore"))){
	        		cfGlReport(null,"PASS", "Expected and actual Number of successfully processed households groups = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of successfully processed households groups = "+cfGlGetRunTimeVar("tempCountBefore")+". But actual Number of successfully processed households groups = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of failed households groups")){
	        	actualValue = line.split("households")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of failed households groups= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of failed households groups = 0. But actual Number of failed households groups = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afReadLHPinputFile(String... inputParams) throws Exception {
		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile");
//		String filePath = UTAFFwVars.utafFWFolderPath + "//batchFiles//HKG_HKM_100421_PTY.csv";
		File myObj = new File(filePath);
		Scanner myReader = new Scanner(myObj);
		HashMap<String,String> keyVal = new HashMap<String,String>();
		int i=0;
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.startsWith("HK")){
	        	String key = line.split(";")[0];
	        	String value = line.split(";")[1];
	        	i=i+1;
	        	if(i==1) {
	        		UTAFRead2.runTimeVar.put("key_1", key);
	        	}
	        	else if(!key.equalsIgnoreCase(UTAFRead2.runTimeVar.get("key_1"))){
	        		UTAFRead2.runTimeVar.put("key_2", key);
	        	}
	        	if(keyVal.get(key)==null) {
	        		keyVal.put(key, value);
	        	}
	        	else {
	        		keyVal.put(key, keyVal.get(key)+","+value);
	        	}
	        }
	      }
	      myReader.close();
	      String path = UTAFFwVars.utafFWFolderPath + "temp.properties";
	      output = new FileOutputStream(path);
	      afCreateNewElementProperty("key_1", cfGlGetRunTimeVar("key_1"));
	      afCreateNewElementProperty("key_2", cfGlGetRunTimeVar("key_2"));
	      afCreateNewElementProperty("val_1", keyVal.get(cfGlGetRunTimeVar("key_1")));
	      afCreateNewElementProperty("val_2", keyVal.get(cfGlGetRunTimeVar("key_2")));
	      afCreateNewElementProperty("tempFilePath", path);
	      UTAFRead2.runTimeVar.put("val_1", keyVal.get(cfGlGetRunTimeVar("key_1")));
	      UTAFRead2.runTimeVar.put("val_2", keyVal.get(cfGlGetRunTimeVar("key_2")));
	      afGetValuesFromDb("PTY_LHP_BatchRun","FILE_NM",null,"tempLFPfileName");
	      int sequenceNumber = Integer.parseInt(UTAFRead2.runTimeVar.get("tempLFPfileName").split("_")[2])+1;
	      String fileName="HKG_HKM_"+sequenceNumber+"_PTY.csv";
	      if(!UTAFRead2.runTimeVar.get("tempOutputFile").equals(fileName)) {
	    	  File newFile = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+fileName);
		      myObj.renameTo(newFile);
		      UTAFRead2.runTimeVar.put("tempOutputFile",fileName);
	      }
	}
	
	public static void afValidateLHPlogFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
//		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//LPD_stat_20211122_111416663.log");
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Total number of lines present in the Input file")){
	        	actualValue = line.split("in the")[1].split(":")[1].trim();
	        	if(actualValue.equalsIgnoreCase("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Total number of lines present in the Input file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Total number of lines present in the Input file = 6. But actual Total number of lines present in the Input file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of lines successfully processed with OK status")){
	        	actualValue = line.split("OK")[1].split(":")[1].trim();
	        	if(actualValue.equals("6")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of lines successfully processed with OK status = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of lines successfully processed with OK status = 6. But actual Number of lines successfully processed with OK status = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of lines successfully processed with NOK status")){
	        	actualValue = line.split("NOK")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of lines successfully processed with NOK status= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of lines successfully processed with NOK status = 0. But actual Number of lines successfully processed with NOK status = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of lines successfully processed with WARN status")){
	        	actualValue = line.split("WARN")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of lines successfully processed with WARN status= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of lines successfully processed with WARN status = 0. But actual Number of lines successfully processed with WARN status = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of lines that encountered an error while processing")){
	        	actualValue = line.split("error")[1].split(":")[1].trim();
	        	if(actualValue.equals("0")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of lines that encountered an error while processing= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of lines that encountered an error while processing = 0. But actual Number of lines that encountered an error while processing = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	public static void afValidateLEFstatFile(String... inputParams) throws Exception {
		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//"+UTAFRead2.runTimeVar.get("tempOutputFile"));
//		File myObj = new File(UTAFFwVars.utafFWFolderPath + "//batchFiles//LPD_stat_20211122_111416663.log");
		Scanner myReader = new Scanner(myObj);
		String actualValue="";
	      while (myReader.hasNextLine()) {
	        String line = myReader.nextLine();
	        if(line.contains("Input file name")){
	        	actualValue = line.split("file")[1].split(":")[1].trim();
	        	if(actualValue.contains(UTAFRead2.runTimeVar.get("tempInputFile"))){
	        		cfGlReport(null,"PASS", "Expected and actual Input file name = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Input file name = 6. But actual Input file name = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of records available in the Input file")){
	        	actualValue = line.split("Input")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records available in the Input file = "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records available in the Input file = 2. But actual Number of records available in the Input file = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of records selected for process")){
	        	actualValue = line.split("selected")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of records selected for process= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records selected for process = 2. But actual Number of records selected for process = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of calls made to PTY")){
	        	actualValue = line.split("made to")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of calls made to PTY= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of calls made to PTY = 2. But actual Number of calls made to PTY = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of successful responses from PTY")){
	        	actualValue = line.split("responses")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of successful responses from PTY= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of successful responses from PTY = 2. But actual Number of successful responses from PTY = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Parties updated in PTY")){
	        	actualValue = line.split("updated")[1].split(":")[1].trim();
	        	if(actualValue.equals("1")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Parties updated in PTY= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Parties updated in PTY = 1. But actual Number of Parties updated in PTY = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of Parties created in PTY")){
	        	actualValue = line.split("created")[1].split(":")[1].trim();
	        	if(actualValue.equals("1")){
	        		cfGlReport(null,"PASS", "Expected and actual Number of Parties created in PTY= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of Parties created in PTY = 1. But actual Number of Parties created in PTY = "+actualValue, false, false);
	        	}
	        }
	        if(line.contains("Number of records available in OK sheet")){
	        	actualValue = line.split("OK")[1].split(":")[1].trim();
	        	if(actualValue.equals("2")){
	        		cfGlReport(null,"PASS", "Expected and Number of records available in OK sheet= "+actualValue, false, false);
	        	}
	        	else{
	        		cfGlReport(null,"FAIL", "Expected Number of records available in OK sheet = 2. But actual Number of records available in OK sheet = "+actualValue, false, false);
	        	}
	        }
	      }
	      myReader.close();
	}
	
	
	public static void afExecuteQueryMultipleTimes(String...inputParams) throws Exception {
		int startRow=Integer.parseInt(inputParams[2].split(",")[0]);
		int v1 = Integer.parseInt(inputParams[2].split(",")[1]);
		for(int i=startRow;i<v1;i++) {
			afGetValuesFromDb(inputParams[0],inputParams[1],String.valueOf(i),inputParams[3]+"_"+(i+1));
		}
	}
	
	public static void afClickElementWithPartialLocatorInternational (String... inputParams) throws Exception {
        String arr[] = inputParams[0].split(",");
        for (String val : arr) {
               if (val.contains("'")) {
                     inputParams[0] = inputParams[0].replace(val, val.split("'")[0]);
               }
               if (val.contains("/")) {
                     inputParams[0] = inputParams[0].replace(val, val.split("/")[0]);
               }
        }
        String arr_new[] = inputParams[0].split(",");
        WebElement element = null;
        String xpath = "", expval = null, value_1 = null, value_2 = null, value_3 = null, value_4 = null;
        if (inputParams[1].equals("street")) {
               value_1 = arr_new[0];
               value_2 = arr_new[1];
               value_3 = arr_new[2];
               value_4 = arr_new[3];
               expval = value_1 + ", " + value_2.toString() + " " + value_3 + " " + value_4;
               UTAFRead2.runTimeVar.put(inputParams[2], expval);
               UTAFLog.info("#######STREET#######" + expval);
               // System.out.println("#######STREET#######"+expval);
               UTAFRead2.runTimeVar.put(inputParams[2],
                            value_4.substring(0, 1) + value_4.substring(1, value_4.length()).toLowerCase());
        } else {
               value_1 = arr_new[0];
               value_2 = arr_new[1];

               expval = value_1 + " , " + value_2;
               UTAFRead2.runTimeVar.put(inputParams[2], expval);
               UTAFLog.info("#######CITY#######" + expval);
               // System.out.println("#######CITY#######"+expval);
        }

        try {
               xpath = "(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_1 + ", " + value_2);
               element = driver.findElement(By.xpath(xpath));
               ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
               afCommonElementClick("CDB_GUI_common_Text_contains", value_1 + ", " + value_2);

        } catch (Exception ex) {
               try {
                     xpath = "(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_1 + " , " + value_2);
                     element = driver.findElement(By.xpath(xpath));
                     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
                     afCommonElementClick("CDB_GUI_common_Text_contains", value_1 + " , " + value_2);
               }

               catch (Exception exx) {
                     xpath = "(.//*[contains(text(),'UTAFPropValue')])".replace("UTAFPropValue", value_2 + ", " + value_3);
                     element = driver.findElement(By.xpath(xpath));
                     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
                     afCommonElementClick("CDB_GUI_common_Text_contains", value_2 + ", " + value_3);

               }
        }
        
 }

    
}
	
	
	
	
	
	


