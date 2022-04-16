package com.infosys.UTAF;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.WebDriver;

import com.infosys.UTAF.extend.UTAFAppFunctions;

public class UTAFDriverBridge2 extends UTAFCommonFunctions2 {
	// public static ThreadLocal<WebDriver> driverobj = new
	// ThreadLocal<WebDriver>();
	public static WebDriver driverobj;
	public static List<String> allMethods = new ArrayList<String>();

	UTAFDriverBridge2() {
		try {
			
			Class<?> classobj = Class.forName("com.infosys.UTAF.extend.UTAFAppFunctions");// UTAFAppFunctions.class;
			// get list of methods
			Method[] methods = classobj.getMethods();
			for (Method method : methods) {
				allMethods.add(method.getName());
			}
		} catch (Exception ex) {
			UTAFLog.fatal("Class not found : com.infosys.UTAF.extend.UTAFAppFunctions");
		}
	}

	public static void reusableFunctions(String currTestStepUDF, String[] currTestStepIP) throws Exception {
		// String locator = ExcelRead.mapOR.get(currTestStepIP[0]);
		boolean contains = false;//Arrays.stream(currTestStepIP).anyMatch("NOTAPPLICABLE"::equals);
		for (String vstring : currTestStepIP) {
			if(vstring != null && !(vstring.isEmpty()) && vstring.contains("NOTAPPLICABLE"))
			{
				contains = true;
				break;
			}
		} 
		if (contains) {
			UTAFFwVars.utafFWTCStatus = "INFO";
		} else {
			try {
				currTestStepUDF = currTestStepUDF.trim();
				switch (currTestStepUDF) {
				case "cfGlSelSetBrowser":
					//if(!(currTestStepIP[1] == null))
						driverobj = cfGlSelSetBrowser(currTestStepIP);
					//else
						//driverobj = cfGlSelSetBrowser(currTestStepIP[0]);
					break;
				case "cfGlSelLaunchURL":
					cfGlSelLaunchURL(driverobj, currTestStepIP[0]);
					break;

				// Element Clicks
				case "cfGlSelElementClickWait":
					cfGlSelElementClickWait(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelElementClick":
					cfGlSelElementClick(driverobj, currTestStepIP[0]);
					break;
				case "cfGlJsElementClick":
					cfGlJsElementClick(driverobj, currTestStepIP[0]);
					break;

				// Frames
				case "cfGlSelSwitchToFrame":
					cfGlSelSwitchToFrame(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelSwitchToDefaultContent":
					cfGlSelSwitchToDefaultContent(driverobj);
					break;
				case "cfGlSwitchIframeElement":
					cfGlSwitchIframeElement(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSwitchIframeIterate":
					cfGlSwitchIframeIterate(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelSwitchToFrameName":
					cfGlSelSwitchToFrameName(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;

				case "cfGlSelSendValue":
					cfGlSelSendValue(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelClearAndSendValue":
					cfGlSelClearAndSendValue(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;

				case "cfGlSelSelectDropDown":
					cfGlSelSelectDropDown(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelSelectDropDownIndex":
					cfGlSelSelectDropDownIndex(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelSelectDropDownValue":
					cfGlSelSelectDropDownValue(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelSelectDropDownText":
					cfGlSelSelectDropDownText(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelGetTextAndCompare":
					cfGlSelGetTextAndCompare(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelGetAttrAndCompare":
					cfGlSelGetAttrAndCompare(driverobj, currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;
				case "cfGlSelAlertAccept":
					cfGlSelAlertAccept(driverobj);
					break;
				case "cfGlSelAlertAcceptIfExist":
					cfGlSelAlertAcceptIfExist(driverobj);
					break;
				case "cfGlThreadSleep":
					cfGlThreadSleep(currTestStepIP[0]);
					break;

				case "cfGlSelectAutoComplete":
					cfGlSelectAutoComplete(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelGetText":
					cfGlSelGetText(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelSimpleGetText":
					cfGlSelSimpleGetText(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "screenshot":
					captureScreenshot(currTestStepIP[0], driverobj);
					break;
				case "cfGlSelClearTextBox":
					cfGlSelClearTextBox(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelValidateTitle":
					cfGlSelValidateTitle(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelDrawLine":
					cfGlSelDrawLine(driverobj, currTestStepIP[0]);
					break;
				case "cfGlElementScrollTillVisible":
					cfGlElementScrollTillVisible(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelPageScroll":
					cfGlSelPageScroll(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelGetAttrValueAndCompare":
					cfGlSelGetAttrValueAndCompare(driverobj, currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;
				case "cfGlSelSimpleGetAttribute":
					cfGlSelSimpleGetAttribute(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelWaitForElementToBeClickable":
					cfGlSelWaitForElementToBeClickable(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelIsElementDisplayed":
					cfGlSelIsElementDisplayed(driverobj, currTestStepIP[0]);
					break;
				case "cfGlActionClick":
					cfGlActionClick(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelRightClick":
					cfGlSelRightClick(driverobj, currTestStepIP[0]);
					break;
				case "cfSelgetAttribute":
					cfGlSelGetAttribute(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelMultipleClicksArr":
					cfGlSelMultipleClicksArr(driverobj, currTestStepIP[0]);
					break;
				case "cfGlJsMultipleElementClick":
					cfGlJsMultipleElementClick(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				/*case "cfGlUpdateInNPSTrack":
					cfGlUpdateInNPSTrack(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2], currTestStepIP[3],
							currTestStepIP[4], currTestStepIP[5]);
					break;
				*/
					
				case "cfGlXTempGetUnixTime":
					cfGlXTempGetUnixTime(currTestStepIP[0]);
					break;

				case "cfGlXTempGetDate":
					cfGlXTempGetDate(currTestStepIP[0], currTestStepIP[1]);
					break;

				case "cfGlCopyFile":
					cfGlCopyFile(currTestStepIP[0], currTestStepIP[1]);
					break;

				case "cfGlModifyTextFile":
					cfGlModifyTextFile(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;

				// case "cfGlSFTPPutLocation":
				// cfGlSFTPPutLocation(currTestStepIP[0], currTestStepIP[1]);
				// break;

				case "alertValidation":
					cfGlAlertValidation(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelAlertGetText":
					cfGlSelAlertGetText(driverobj, currTestStepIP[0]);
					break;
				case "cfGlSelAlertGetTextAndCompare":
					cfGlSelAlertGetTextAndCompare(driverobj, currTestStepIP[0]);
					break;
				case "cfGlDBConnectCouchBase":
					cfGlDBConnectCouchBase(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2], currTestStepIP[3]);
					break;
				case "cfGlDBConnectSQL":
					cfGlDBConnectSQL(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2], currTestStepIP[3]);
					break;
				case "cfGlADDRunTimeVar":
					cfGlADDRunTimeVar(currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlSelGetAttributeStore ":
					cfGlSelGetAttributeStore(driverobj, currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;
				case "cfGlSelGetCSSAttr":
					cfGlSelGetCSSAttr(driverobj, currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;
				case "cfGlSelGetCSSAttrAndCompare":
					cfGlSelGetCSSAttrAndCompare(driverobj, currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
					break;
				case "cfGlSelElementCountAndCompare":
					cfGlSelElementCountAndCompare(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGLRandomName":
					cfGLRandomName(currTestStepIP[0]);
					break;
				case "cfGlSelRefresh":
					cfGlSelRefresh(driverobj);
					break;
				case "cfRefreshHealthCheck":
					cfRefreshHealthCheck();
					break;
				case "cfGlSelWaitForElementToBeClickSeconds":
					cfGlSelWaitForElementToBeClickSeconds(driverobj, currTestStepIP[0], currTestStepIP[1]);
					break;
				case "cfGlReport":
					// cfGlReport(driverobj,currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]));
					break;
				case "cfGLSelMaximize":
					cfGLSelMaximize(driverobj);
					// cfGlReport(driverobj,currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]));
					break;
				case "cfGlJsElementSimpleClick":
					cfGlJsElementSimpleClick(driverobj, currTestStepIP[0]);
					// cfGlReport(driverobj,currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]));
					break;
				case "cfGLJsMoveToElement":
					cfGLJsMoveToElement(driverobj, currTestStepIP[0]);
					// cfGlReport(driverobj,currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]));
					break;
				case "cfGlSelElementSimpleClick":
					cfGlSelSimpleElementClick(driverobj, currTestStepIP[0]);
					// cfGlReport(driverobj,currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]));
					break;

				
				
					
				case "cfGlSerSendRequest":
					cfGlSerSendRequest(currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3],currTestStepIP[4],currTestStepIP[5],currTestStepIP[6]);
					break;
				
				case "cfGlUpdateXrayStatus":
					cfGlUpdateXrayStatus(currTestStepIP[0]);
					break;
				
				case "cfGlSerPreCondition":
					cfGlSerPreCondition(currTestStepIP[0]);
					break;
				case "cfGlSerValidateValueInResponse":
					cfGlSerValidateValueInResponse(currTestStepIP[0]);
					break;
				case "cfGlSerValidateStatusCode":
					cfGlSerValidateStatusCode(currTestStepIP[0]);
					break;
				case "cfGlSerValidateStatusLine":
					cfGlSerValidateStatusLine(currTestStepIP[0]);
					break;
				case "cfGlSerValidateContentType":
					cfGlSerValidateContentType(currTestStepIP[0]);
					break;
				case "cfGlSerValidateResponseHeaders":
					cfGlSerValidateResponseHeaders(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfGlSerValidateTagCount":
					cfGlSerValidateTagCount(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfGlSerValidateTagValue":
					cfGlSerValidateTagValue(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfGlSerStoreTagValues":
					cfGlSerStoreTagValues(currTestStepIP[0]);
					break;
				case "cfGlSerValidateDB":
					cfGlSerValidateDB(currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]);
					break;
				case "cfAddReportingvVars":
					cfAddReportingvVars(currTestStepIP[0],currTestStepIP[1],currTestStepIP[2]);
					break;
				case "cfAddReportInVars":
					cfAddReportInVars(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfAddReportOutVars":
					cfAddReportOutVars(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfAddReportEnvVars":
					cfAddReportEnvVars(currTestStepIP[0],currTestStepIP[1]);
					break;
				case "cfGlSelfHealingOn":
					cfGlSelfHealingOn();
					break;
				case "cfGlSendJmsMessage":
					cfGlSendSonicJmsMessage(currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3], currTestStepIP[4],currTestStepIP[5],currTestStepIP[6]);
					break;
				case "cfGlBrowseJmsMessage":
					cfGlBrowseSonicJmsMessage(currTestStepIP[0],currTestStepIP[1],currTestStepIP[2],currTestStepIP[3]);
					break;
				
				default:
					UTAFAppFunctions uaf = new UTAFAppFunctions(driverobj);
					UTAFDriverBridge2 ua = new UTAFDriverBridge2();
					String getClassName = "com.infosys.UTAF.extend.UTAFAppFunctions";
					//System.out.println("Test" + currTestStepIP[0].isEmpty());
					if(!(currTestStepIP[0] == null) && currTestStepIP[0].startsWith("getClass")){
						currTestStepIP[0] = currTestStepIP[0].replace("getClass", "");
						getClassName = UTAFFwVars.utafFWProps.getProperty(currTestStepIP[0]);
					}
					
					updateAllMethodsName(getClassName);
					UTAFFwVars.utafThrowException = false;
					
					
					if (allMethods.contains(currTestStepUDF)) {
						// main(currTestStepIP);
						Class<?> c = Class.forName(getClassName);
						Class[] argTypes = new Class[] { String[].class };
						Method appMethod = c.getDeclaredMethod(currTestStepUDF, argTypes);
						System.out.format("invoking %s.main()%n", c.getName());
						appMethod.invoke(null, (Object) currTestStepIP);

					} else {
						if(!currTestStepUDF.equalsIgnoreCase("afTearDown"))
							throw new Exception("Error In reusable Functions - Invalid Step : " + currTestStepUDF);
					}
					/*Class<?> c = Class.forName("com.prx.appclasses.TestAppFunctions3");
					Method[] appMethod2= c.getMethods();
					for (Method method : appMethod2) {
						if(method.getName().equalsIgnoreCase("afSayHelloThree")){
							method.invoke(null, (Object) currTestStepIP);
						}
					}*/
					break;
				}
				UTAFCommonFunctions2.selfhealreports(driverobj);
			} catch (Exception ex) {
				cfGLGenericExceptionHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() + "\n" + UTAFCommonFunctions2.cfGlGetStackTrace(ex));
				try {
					if (UTAFFwVars.utafReportFlag.equalsIgnoreCase("N")) {
						if(UTAFFwVars.utafFWTCSkipFlag.equalsIgnoreCase("Y")){
							cfGlReport(driverobj, "SKIP", UTAFCommonFunctions2.cfGlGetStackTrace(ex), false, false);
						}
						else if((UTAFFwVars.utafFWTCError == "") || UTAFFwVars.utafFWTCError.equalsIgnoreCase("EMPTY"))
							cfGlReport(driverobj, "FAIL", UTAFCommonFunctions2.cfGlGetStackTrace(ex), false, false);
						else 
							cfGlReport(driverobj, "FAIL", UTAFFwVars.utafFWTCError, false, false);
					}
				} catch (Exception ex1) {
					UTAFFwVars.utafThrowException = false;
				}
			}catch (Error ex) {
				cfGLGenericExceptionHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage());
				try {
					if (UTAFFwVars.utafReportFlag.equalsIgnoreCase("N")) {
						if(UTAFFwVars.utafFWTCSkipFlag.equalsIgnoreCase("Y")){
							cfGlReport(driverobj, "SKIP", ex.getMessage(), false, false);
						}
						else if((UTAFFwVars.utafFWTCError == "") || UTAFFwVars.utafFWTCError.equalsIgnoreCase("EMPTY"))
							cfGlReport(driverobj, "FAIL", ex.getMessage(), false, false);
						else 
							cfGlReport(driverobj, "FAIL", UTAFFwVars.utafFWTCError, false, false);
					}
				} catch (Exception ex1) {
					UTAFFwVars.utafThrowException = false;
				}
			}
		}
	}

	public static void updateAllMethodsName(String classNames) {
		try {
			allMethods = new ArrayList<String>();
			Class<?> classobj = Class.forName(classNames);
			
			// get list of methods
			Method[] methods = classobj.getMethods();
			for (Method method : methods) {
				allMethods.add(method.getName());
			}
		} catch (Exception ex) {
			UTAFLog.fatal("Class not found : " + classNames);
		}
	}
}