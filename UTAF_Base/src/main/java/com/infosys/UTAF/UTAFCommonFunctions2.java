package com.infosys.UTAF;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;
import com.infosys.UTAF.enums.ScubeEnv;
import com.infosys.UTAF.enums.ScubeStatus;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;



public class UTAFCommonFunctions2 {


	public static Properties objectMapProps;
	public static ThreadLocal<WebDriver> driverobj = new ThreadLocal<WebDriver>();
	public static WebElement eleSearched;
	public static Robot robot;
	public static String OrderId;
	static String quoteNum = "";
	public static Connection conTandem = null;
	public static Statement stmtTandem = null;
	public static ResultSet resultSetRet = null;
	

	
	public static UTAFRestUtils restUtils = new UTAFRestUtils();
    public static Response response;
    public static String xml;
    public static String requestpPayloadType;
    public static String json;
    public boolean flag;
    public static Map<String, String> stepData = new HashMap<String,String>();
    public static Map<String,String> testData = new LinkedHashMap<String,String>();
    public static HashMap<String,String> runTimeData = new LinkedHashMap<String,String>();


	/**
	 * This method helps in closing the browsers and selenium drivers.
	 * 
	 * @param browser
	 *            name as String
	 * @author INFOSYS UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlKillBrowserAndDriver(String strBrowserName) throws Exception {
		UTAFLog.info("Inside cfGlKillBrowserAndDriver");
		try {
			if (strBrowserName.equalsIgnoreCase("Chrome") || strBrowserName.equalsIgnoreCase("Google Chrome")) {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
			} else if (strBrowserName.equalsIgnoreCase("FF") || strBrowserName.equalsIgnoreCase("firefox")) {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
			} else if (strBrowserName.equalsIgnoreCase("IE") || strBrowserName.equalsIgnoreCase("Internet explorer")) {
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe /T");
			} else if (strBrowserName.equalsIgnoreCase("EDGE") ) {
				Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM msedge.exe /T");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlKillBrowserAndDriver", ex.getMessage());
		}
		UTAFLog.info("Out cfGlKillBrowserAndDriver");
	}

	/**
	 * This method helps in setting the UTAF Variables in exception case
	 * 
	 * @param mehtod_name
	 * @param error_message
	 * @author INFOSYS UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGLGenericExceptionHandlingThrow(String vMethodName, String vMessage) throws Exception {
		if(UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("SKIP")){
			
		}
		else if(!UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("FAIL")){
		UTAFFwVars.utafFWTCStatus = "FAIL";
		UTAFLog.error("Error in " + vMethodName + " : " + vMessage);
		UTAFFwVars.utafFWTCError = vMessage;
		UTAFFwVars.utafFWTCStatus = "FAIL";
		}
		//UTAFFwVars.utafThrowException = true;
		cfGLThrowException(vMethodName + " : " + vMessage);
	}

	public static void cfGLGenericExceptionHandling(String vMethodName, String vMessage) {
		if (UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("SKIP")) {

		} else if (!UTAFFwVars.utafFWTCStatus.equalsIgnoreCase("FAIL")) {
			UTAFLog.error("Error in " + vMethodName + " : " + vMessage);
			UTAFFwVars.utafFWTCError = vMessage;
			UTAFFwVars.utafFWTCStatus = "FAIL";
		}
	}
	
	public static void cfGLGenericExceptionHandlingMsg(String vMethodName, String vMessage) {
		UTAFLog.error ("Error in " + vMethodName + " : " + vMessage);
		UTAFFwVars.utafFWTCError =  vMessage;
		UTAFFwVars.utafFWTCStatus = "FAIL";
	}

	/**
	 * This method helps in setting the UTAF Variables in pass case
	 * 
	 * @param mehtod
	 *            name
	 * @param message/error
	 *            message
	 * @author INFOSYS UTAF Team
	 * @version 1.0
	 * @since 01-Jul-2018
	 */
	public static void cfGLGenericPassHandling(String vMethodName, String vMessage) {
		UTAFLog.info("In function " + vMethodName + " : " + vMessage);
		UTAFLog.info("In function " + vMethodName + " : Pass");
		UTAFFwVars.utafFWTCError = "";
		UTAFFwVars.utafFWTCStatus = "PASS";
	}

	/**
	 * cfGlThreadSleep method stops the execution for given period of time.
	 * 
	 * @param seconds
	 * @author INFOSYS UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlThreadSleep(String vSeconds) throws Exception {
		UTAFLog.info("In cfGlThreadSleep");
		long millSec = 1000; //(long)Integer.parseInt(vSeconds) * 1000;
		try {
			millSec = (long)Integer.parseInt(vSeconds) * 1000;
			Thread.sleep(millSec);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlThreadSleep",
					ex.getMessage());

		}
		UTAFLog.info("Out cfGlThreadSleep");
	}

	/**
	 * cfGlReadPropFile method load the properties from the properties file path
	 * 
	 * @param properties
	 * @param properties
	 *            file path
	 * @return properties
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static Properties cfGlReadPropFile(Properties prop, String propFilePath) throws Exception {
		UTAFLog.info("In cfGlReadPropFile");
		InputStream input = null;
		try {
			input = new FileInputStream(propFilePath);
			prop.load(input);
		} catch (IOException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlReadPropFile",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlReadPropFile",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlReadPropFile");
		return prop;
	}

	/**
	 * cfGlSelElementClickWait method waits for an element to be clickable
	 * 
	 * @param driver
	 * @param locator
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelElementClickWait(WebDriver driver, String locator) throws Exception {
		UTAFLog.info("In cfGlSelElementClickWait");
		UTAFLog.info("Locator : " + locator);
		try {
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
			UTAFLog.info("Clicking on :  " + locator);
			eleSearched.click();
			UTAFLog.info("Clicked on : " + locator);

		} catch (NoClassDefFoundError ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementClickWait",
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementClickWait",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelElementClickWait");
	}

	/**
	 * cfGlSelSetBrowser method is used for setting the browser it takes driver
	 * path from utaf properties file
	 * 
	 * @param browser
	 *            name
	 * @return webdriver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static WebDriver cfGlSelSetBrowser(String strBrowser) throws Exception {
		UTAFLog.info("In cfGlSelSetBrowser");
		WebDriver wb = null;
		
		if(!UTAFFwVars.utafFPlatform.equalsIgnoreCase("EMPTY") && UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB"))
			strBrowser = UTAFFwVars.utafFPlatform;
		else
			UTAFFwVars.utafFPlatform = strBrowser;
		try {

			if (strBrowser.equalsIgnoreCase("IE") || strBrowser.equalsIgnoreCase("Internet Explorer")) {
				wb = cfGlSelSetIEDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.IE"));
				UTAFFwVars.utafFWTCStatus = "PASS";
			} else if (strBrowser.equalsIgnoreCase("Chrome") || strBrowser.equalsIgnoreCase("GoogleChrome")
					|| strBrowser.equalsIgnoreCase("Google Chrome")) {
				wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"));
				UTAFFwVars.utafFWTCStatus = "PASS";
			} 
			else if (strBrowser.equalsIgnoreCase("Edge") ) {
				//wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"));
				wb = cfGlSelSetEdgeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.EDGE"));
				UTAFFwVars.utafFWTCStatus = "PASS";
			}else {
				cfGLGenericExceptionHandlingThrow("cfGlSelSetBrowser",
						"Incorrect Browser");
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSetBrowser",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlSelSetBrowser");
		return wb;
	}
	
	public static WebDriver cfGlSelSetBrowser(String[] inputParams) throws Exception {
		UTAFLog.info("In cfGlSelSetBrowser");
		WebDriver wb = null;
		String strBrowser = inputParams[0];
		String isHeadless = inputParams[1];
		if(!UTAFFwVars.utafFPlatform.equalsIgnoreCase("EMPTY") && UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB"))
			strBrowser = UTAFFwVars.utafFPlatform;
		else
			UTAFFwVars.utafFPlatform = strBrowser;
		try {

			if (strBrowser.equalsIgnoreCase("IE") || strBrowser.equalsIgnoreCase("Internet Explorer")) {
				wb = cfGlSelSetIEDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.IE"));
				UTAFFwVars.utafFWTCStatus = "PASS";
			} else if (strBrowser.equalsIgnoreCase("Chrome") || strBrowser.equalsIgnoreCase("GoogleChrome")
					|| strBrowser.equalsIgnoreCase("Google Chrome")) {
				//wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"));
				wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"),isHeadless, inputParams[2]);
				UTAFFwVars.utafFWTCStatus = "PASS";
			} 
			else if (strBrowser.equalsIgnoreCase("Edge") ) {
				//wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"));
				wb = cfGlSelSetEdgeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.EDGE"),isHeadless);
				UTAFFwVars.utafFWTCStatus = "PASS";
			}
			else if (strBrowser.equalsIgnoreCase("Firefox") ) {
				//wb = cfGlSelSetChromeDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.CHROME"));
				wb = cfGlSelSetFirefoxDriver(UTAFFwVars.utafFWProps.getProperty("WEBDRIVER.PATH.FIREFOX"),isHeadless);
				UTAFFwVars.utafFWTCStatus = "PASS";
			}else {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"Incorrect Browser");
			}
			cfGLSelMaximize(wb);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage());

		}
		UTAFLog.info("Out cfGlSelSetBrowser");
		return wb; // driverobj.get();
	}



	/**
	 * cfGlSelLaunchURL method is used to launch the URL
	 * 
	 * @param driver
	 * @param url
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelLaunchURL(WebDriver driver, String url) throws Exception {
		UTAFLog.info("In cfGlSelLaunchURL");
		try {
			driver.get(url);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelLaunchURL",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlSelLaunchURL");
	}

	/**
	 * cfGlSelGetTitle method is used to capture the title
	 * 
	 * @param driver
	 * @param varianle
	 *            to store the title
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static String cfGlSelGetTitle(WebDriver driver, String vTitle) throws Exception {
		UTAFLog.info("In cfGlSelGetTitle");
		String actualTitle = null;
		try {
			actualTitle = driver.getTitle();
			cfGlADDRunTimeVar(vTitle, actualTitle);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetTitle",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlSelGetTitle");
		return actualTitle;
	}

	/**
	 * cfGlSelSetIEDriver method is to set IE driver
	 * 
	 * @param driver
	 *            path
	 * @return IE driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static InternetExplorerDriver cfGlSelSetIEDriver(String driverPath) throws Exception {
		UTAFLog.info("In cfGlSelSetIEDriver");
		WebDriver wb = null;
		try {
			Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
			Thread.sleep(2000);
			System.setProperty("webdriver.ie.driver", driverPath);
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			// caps.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
			// "https://intrauat.web.bc/WWS/WebSealServlet/PGPHEhFfGOmgyaVVojR1QqY3wycqXmkQ*/!STANDARD?pyActivity=Embed-PortalLayout.RedirectAndRun&ThreadName=OpenPortal&Location=pyActivity%3DData-Portal.ShowSelectedPortal%26portal%3DOLOUser%26Name%3D%20OLOUser%26pzSkinName%3D%26developer%3Dfalse%26ThreadName%3DOpenPortal%26launchPortal%3Dtrue&bPurgeTargetThread=true&target=popup&pzHarnessID=HIDC543CD4DFAE5C1917608234F970E560F");
			//
			UTAFLog.info("Out cfGlSelSetBrowser");
			wb = new InternetExplorerDriver(caps);
			return (InternetExplorerDriver) wb;
		} catch (Exception ex) {
			UTAFLog.info("Out cfGlSelSetIEDriver");
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
		return null;
	}

	/**
	 * cfGlSelSetChromeDriver method is to set IE driver
	 * 
	 * @param driver path
	 * @return Chrome driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	@SuppressWarnings("finally")
	public static WebDriver cfGlSelSetChromeDriver(String...inputPramams) throws Exception {
		String driverPath = inputPramams[0];
		String isHeadless = inputPramams[1];
		String isIncognito = inputPramams[2];
		UTAFLog.info("In cfGlSelSetChromeDriver");
		DesiredCapabilities dcChrome = null;
		WebDriver wd = null;
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Thread.sleep(3000);
			System.setProperty("webdriver.chrome.driver", driverPath);
			ChromeOptions options = new ChromeOptions();
			
			if (!(isIncognito == null)) {
				if ((isIncognito.trim().equalsIgnoreCase("N")))
					UTAFLog.info("set incognito parameter "+ isIncognito);
				else{
					UTAFLog.info("set incognito parameter "+ isIncognito);
					options.addArguments("--incognito");
				}
			}else{
				options.addArguments("--incognito");
			}
			/*if(!(inputPramams[2] == null))
			{if(!(inputPramams[2].trim().equalsIgnoreCase("N")))
				
			 }*/
			//options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"); 
			
			Map<String, String> mobileEmulation = new HashMap<>();

			mobileEmulation.put("deviceName", "iPad Pro");

			
			if(!(isHeadless == null))
				{if(isHeadless.trim().equalsIgnoreCase("Y"))
					options.addArguments("--headless");}
			
			options.addArguments("start-maximized");
			options.addArguments("unexpectedAlertBehaviour", "ignore");
			options.addArguments("--allow-running-insecure-content");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-infobars");
			options.addArguments("enable-automation");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--ignore-ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");
			options.setExperimentalOption("useAutomationExtension", false);
			options.addArguments("remote-debugging-port=10000");
			options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
			//options.setExperimentalOption("mobileEmulation", mobileEmulation);
			dcChrome = DesiredCapabilities.chrome();
			dcChrome.setCapability(ChromeOptions.CAPABILITY, options);
			dcChrome.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			UTAFFwVars.utafFWTCStatus = "PASS";
			wd = new ChromeDriver(dcChrome);
			try{
			if(UTAFFwVars.utafFWSelfHealFlag.equalsIgnoreCase("Y")){
				//UTAFLog.info("In Self Healing Block");
				//wd = new EnrichedRemoteWebDriver((RemoteWebDriver)wd,UTAFFwVars.utafFWFolderPath + "SelfHeal/isdc.properties");
			}
			}
			catch(Exception ex){
				UTAFLog.warn(ex.getMessage());
			}catch(Error er){
				UTAFLog.warn(er.getMessage());
			}
			return wd;
		} catch (Exception ex) {
			if(ex.getMessage().contains("Timeout") && !(inputPramams[10].equalsIgnoreCase("RETRY")) ){
				inputPramams[9]= "RETRY";
				cfGlSelSetChromeDriver(inputPramams);
			}else
			cfGLGenericExceptionHandlingThrow("cfGlSelSetChromeDriver",
					ex.getMessage() );

		}
		return null;
	}
	
	
	public static void selfhealreports(WebDriver driver) {
		/*
		try {
			if(UTAFFwVars.utafFWSelfHealFlag.equalsIgnoreCase("Y") && UTAFFwVars.utafFWSelfHealON){
				if (!(driver == null)) {
					if (EnrichedRemoteWebDriver.getreturnNewValues() != null) {
						// if(EnrichedRemoteWebDriver.getreturnNewValues() != null)
						// {
						ArrayList<String> var = EnrichedRemoteWebDriver.getreturnNewValues();
	
						cfGlReport(driver, "WARNING", "Expected Xpaths : " + var.get(0), false, true);
						cfGlReport(driver, "WARNING", "Abs Path : " + var.get(1), false, false);
						System.out.println(var);
						EnrichedRemoteWebDriver.setreturnNewValues(null);
					}
				}
			}
		} catch (Exception e) {
			UTAFLog.warn("in selfhealreports" + e.getMessage());
			EnrichedRemoteWebDriver.setreturnNewValues(null);
		} catch (Error er) {
			UTAFLog.warn("in selfhealcreports" + er.getMessage());
			EnrichedRemoteWebDriver.setreturnNewValues(null);
		}
		*/
	}
	
	public static void cfGlSelfHealingOn() {
		/*
		try {
			if(!UTAFFwVars.utafFWSelfHealON){
			EnrichedRemoteWebDriver.SelfHealingOn();
			UTAFFwVars.utafFWSelfHealON=true;}
		} catch (Exception e) {
		}catch (Error e) {
		}
		*/
	}
	
	public static void cfGlSelfHealingOff() {
		/*
		try {
			if(UTAFFwVars.utafFWSelfHealON){
			EnrichedRemoteWebDriver.SelfHealingOf(); 
			UTAFFwVars.utafFWSelfHealON=false;}
		} catch (Exception e) {
		}catch (Error e) {
		}
		*/
	}

	/**
	 * cfGlSelSetChromeDriver method is to set IE driver
	 * 
	 * @param driver
	 *            path
	 * @return Chro;e driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	@SuppressWarnings("finally")
	public static WebDriver cfGlSelSetEdgeDrivero(String driverPath) throws Exception {
		UTAFLog.debug("In cfGlSelSetEdgeDriver");
		
		DesiredCapabilities dcChrome = null;
		try {
			//Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			//Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Thread.sleep(500);
			System.setProperty("webdriver.edge.driver", driverPath);
			//EdgeOptions options = new EdgeOptions();
			ChromeOptions options = new ChromeOptions();
			//options.se
			//options.addArguments("--disable-extensions"); 
			//options.addArguments("start-maximized");
			//options.addArguments("--incognito");
			//options.addArguments("unexpectedAlertBehaviour", "ignore");
			options.setCapability("excludeSwitches", Arrays.asList("disable-popup-blocking"));
			options.setCapability("unexpectedAlertBehaviour", "ignore");
			options.setCapability("useAutomationExtension", false);
			//options.setExperimentalOption("useAutomationExtension", false);
			//options.addArguments("remote-debugging-port=10000");
			//options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
			dcChrome = DesiredCapabilities.edge();
			//dcChrome.setCapability(ChromeOptions.CAPABILITY, options);
			//dcChrome.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			UTAFFwVars.utafFWTCStatus = "PASS";
			EdgeOptions edgeOptions = new EdgeOptions().merge(options);
			return  new EdgeDriver(edgeOptions);//  new ChromeDriver(dcChrome);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );

		}
		return null;
	}
	
	
	/**
	 * cfGlSelSetFirefoxDriver method is to set IE driver
	 * 
	 * @param driver
	 *            path
	 * @return firefox driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	@SuppressWarnings("finally")
	public static WebDriver cfGlSelSetFirefoxDriver(String... inputPramams) throws Exception {
		UTAFLog.debug("In cfGlSelSetFirefoxDriver");
		String driverPath = inputPramams[0];
		String isHeadless = inputPramams[1];
		try {
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
			Thread.sleep(2000);
			System.setProperty("webdriver.gecko.driver", driverPath);
			FirefoxOptions options = new FirefoxOptions();
			if (!(isHeadless == null)) {
				if (isHeadless.trim().equalsIgnoreCase("Y"))
					options.addArguments("-private");
			}

			UTAFFwVars.utafFWTCStatus = "PASS";
			return new FirefoxDriver(options);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSetFirefoxDriver", ex.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("finally")
	public static WebDriver cfGlSelSetEdgeDriver(String...inputPramams) throws Exception {
		UTAFLog.debug("In cfGlSelSetEdgeDriver");
		String driverPath = inputPramams[0];
		//String isHeadless = inputPramams[1];
		DesiredCapabilities dcChrome = null;
		try {
			Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
			Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
			Thread.sleep(3000);
			System.setProperty("webdriver.edge.driver", driverPath);
			EdgeOptions edgeOptions = new EdgeOptions();
			//ChromeOptions edgeOptions2 = new ChromeOptions();
			Map<String, String> mobileEmulation = new HashMap<>();
			//edgeOptions.

			mobileEmulation.put("deviceName", "iPad Pro");
			//edgeOptions.addAdditionalCapability("mobileEmulation", mobileEmulation);
			//edgeOptions.addAdditionalCapability("InPrivate", true);
			//edgeOptions.setCapability("InPrivate", true);
			//WebDriverManager manager = WebDriverManager.edgedriver();
			//manager.config().setEdgeDriverVersion("84.0.522.49");
			//manager.setup();
			/*EdgeOptions options = new EdgeOptions();
			driver = new EdgeDriver(options);
			//EdgeOptions options = new EdgeOptions();
			ChromeOptions options = new ChromeOptions();
			//options.se
			//options.addArguments("--disable-extensions"); 
			//options.addArguments("start-maximized");
			//options.addArguments("--incognito");
			//options.addArguments("unexpectedAlertBehaviour", "ignore");
			options.setCapability("excludeSwitches", Arrays.asList("disable-popup-blocking"));
			options.setCapability("unexpectedAlertBehaviour", "ignore");
			options.setCapability("useAutomationExtension", false);
			//options.setExperimentalOption("useAutomationExtension", false);
			//options.addArguments("remote-debugging-port=10000");
			//options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
			dcChrome = DesiredCapabilities.edge();
			//dcChrome.setCapability(ChromeOptions.CAPABILITY, options);
			//dcChrome.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			UTAFFwVars.utafFWTCStatus = "PASS";
			EdgeOptions edgeOptions = new EdgeOptions().merge(options);*/
			//EdgeOptions edgeOptions = new EdgeOptions().merge(options);
			 UTAFFwVars.utafFWTCStatus = "PASS";
			return  new EdgeDriver(edgeOptions);//  new ChromeDriver(dcChrome);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );

		}
		return null;
	}
	
	
	public static void setTimeOuts(int pageLoadTimeOutInSec, int implicitWaitInSec, WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(implicitWaitInSec, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeOutInSec, TimeUnit.SECONDS);
	}

	/**
	 * cfGlSelSwitchToDefaultContent method is to switch to default content
	 * 
	 * @param driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSwitchToWindowTitle(WebDriver driver, String windowTitle) throws Exception {
		UTAFLog.info("In cfGlSelSwitchToWindowTitle");

		try {
			Set<String> driverHandles = driver.getWindowHandles();
			boolean var = false;
			for (String handleName : driverHandles) {
				driver.switchTo().window(handleName);
				if (windowTitle.equalsIgnoreCase(driver.getTitle())) {
					var = true;
					UTAFFwVars.utafFWTCStatus = "PASS";
					UTAFLog.info("Successfully switched to window : " + windowTitle);
					break;
				}
			}
			if (!var) {
				cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToWindowTitle",
						windowTitle + " is not available");
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToWindowTitle",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelSwitchToWindowTitle");
	}

	/**
	 * cfGlSelSwitchToDefaultContent method is to switch to default content
	 * 
	 * @param driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSwitchToDefaultContent(WebDriver driver) throws Exception {
		UTAFLog.info("In cfGlSelSwitchToDefaultContent");
		try {
			driver.switchTo().defaultContent();
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToDefaultContent",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelSwitchToDefaultContent");
	}

	/**
	 * cfGlSelQuitBrowser method is to closing the web driver
	 * 
	 * @param driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelQuitBrowser(WebDriver driver) throws Exception {
		UTAFLog.info("In cfGlSelQuitBrowser");
		try {
			driver.quit();
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelQuitBrowser",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelQuitBrowser");
	}

	/**
	 * cfGlSelSwitchToFrame method is to switch to frame
	 * 
	 * @param driver
	 * @param frame
	 *            index
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSwitchToFrame(WebDriver driver, String frame) throws Exception {
		UTAFLog.info("In cfGlSelSwitchToFrame");
		int intFrame = 0;
		try {
			intFrame = Integer.parseInt(frame);
			driver.switchTo().frame(intFrame);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (NoSuchFrameException | NoClassDefFoundError ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToFrame",
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToFrame",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelSwitchToFrame");
	}

	public static void cfGlSelSwitchToFrameName(WebDriver driver,String... inputParams ) throws Exception {
		UTAFLog.info("In cfGlSelSwitchToFrameName");
		String frame = inputParams[0];
		try {
			driver.switchTo().frame(frame);
		} catch (NoSuchFrameException | NoClassDefFoundError ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToFrame",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSwitchToFrame",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlSelSwitchToFrameName");
	}
	
	/**
	 * cfGlSelRefresh method is to refresh the page
	 * 
	 * @param driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelRefresh(WebDriver driver) throws Exception {
		UTAFLog.info("In cfGlSelRefresh");
		try {
			driver.navigate().refresh();
			cfGLGenericPassHandling("cfGlSelRefresh", "Refreshed successfully");
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelRefresh",
					ex.getMessage() );
		}
		UTAFLog.info("Out cfGlSelRefresh");
	}

	/**
	 * cfGlSelSelectDropDown method is to select from dropdown based on visible
	 * text, index, or value
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 * value can be 
	 * yourtextvalue;text
	 * yourvalue;value
	 * yourindex;index
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSelectDropDown(WebDriver driver, String elename, String value) throws Exception {
		UTAFLog.info("In cfGlSelSelectDropDown");
		try {
			int i;
			String rfElementSearch = elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			String[] temp = value.split(";");
			Select select = new Select(eleSearched);
			if (rfElementSearch.equalsIgnoreCase("Pass")) {
				UTAFLog.info("Trying to select from Dropdown-----------");
				if (temp[1].equalsIgnoreCase("text")) {
					select.selectByVisibleText(temp[0]);
				} else if (temp[1].equalsIgnoreCase("index")) {
					i = Integer.parseInt(temp[0]);
					select.selectByIndex(i);
				} else if (temp[1].equalsIgnoreCase("value")) {
					select.selectByValue(temp[0]);
				}
				UTAFFwVars.utafFWTCStatus = "PASS";
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlSelSelectDropDown",
						"Element not present");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSelectDropDown",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelSelectDropDown");
	}

	public static List<WebElement>  cfGlSelGettDropDownOptions(WebDriver driver, String elename, String value) throws Exception {
		UTAFLog.info("In cfGlSelGettDropDownOptions");
		List<WebElement> allOptions = null;
		try {
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			Select select = new Select(eleSearched);
			allOptions = select.getOptions();
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGettDropDownOptions",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelGettDropDownOptions");
		return allOptions;
	}
	
	public static List<WebElement>  cfGlSelGettDropDownOptions(WebDriver driver, String elename) throws Exception {
		UTAFLog.info("In cfGlSelGettDropDownOptions");
		List<WebElement> allOptions = null;
		try {
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			Select select = new Select(eleSearched);
			allOptions = select.getOptions();
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGettDropDownOptions",
					ex.getMessage() );

		}
		UTAFLog.info("Out cfGlSelGettDropDownOptions");
		return allOptions;
	}


	public static boolean cfGlSelElementCountAndCompare(WebDriver driver, String vLocator, String value2Compare) throws Exception {
        UTAFLog.debug("In cfGlSelElementCountAndCompare");
        int eleSize = 0;
        boolean vVal=false;
        try {
              cfGlSelCheckDocReady(driver);
              List<WebElement> eleList = cfGlSelElementsSearch(driver, vLocator);
              eleSize = eleList.size();
               cfGLGenericPassHandling("cfGlSelElementCountAndCompare",
                            "Count of  " + vLocator + " on page is " + eleSize);
              if (value2Compare.equalsIgnoreCase(String.valueOf(eleSize))) {
                     vVal = true;
                     UTAFFwVars.utafFWTCStatus = "PASS";
                     UTAFLog.info("Value matching with given value : " + value2Compare + " and tag value :  " + String.valueOf(eleSize));
              } else {
                   cfGLGenericExceptionHandlingThrow("cfGlSelElementCountAndCompare",
                                   "Value not matching with given value : " + value2Compare + " and tag value " + String.valueOf(eleSize));
              }

        } catch (UnhandledAlertException ex) {
             cfGLGenericExceptionHandlingThrow("cfGlSelElementCountAndCompare",
                            ex.getMessage());
        } catch (Exception ex) {
             cfGLGenericExceptionHandlingThrow("cfGlSelElementCountAndCompare",
                            ex.getMessage());

        }
        UTAFLog.debug("Out cfGlSelElementCountAndCompare");
        return vVal;
 }

	/**
	 * cfGlSelSelectDropDownIndex method is to select from dropdown based on
	 * index
	 * 
	 * @param driver
	 * @param vLocator
	 * @param vIndex
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSelectDropDownIndex(WebDriver driver, String vLocator, String vIndex) throws Exception {
		UTAFLog.debug("In cfGlSelSelectDropDownIndex");
		try {
			int i;
			elementSearch(driver, vLocator, UTAFFwVars.utafFWExplicitWait);
			Select select = new Select(eleSearched);
			i = Integer.parseInt(vIndex);
			select.selectByIndex(i);
			cfGLGenericPassHandling("cfGlSelSelectDropDownIndex",
					"Index " + vIndex + " selected from dropdown " + vLocator);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSelectDropDownIndex",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelSelectDropDownIndex");
	}

	/**
	 * cfGlSelSelectDropDownValue method is to select from drop down based on
	 * value
	 * 
	 * @param driver
	 * @param vLocator
	 * @param vValue
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSelectDropDownValue(WebDriver driver, String vLocator, String vValue) throws Exception {
		UTAFLog.debug("In cfGlSelSelectDropDownValue");
		try {
			elementSearch(driver, vLocator, UTAFFwVars.utafFWExplicitWait);
			Select select = new Select(eleSearched);
			select.selectByValue(vValue);
			cfGLGenericPassHandling("cfGlSelSelectDropDownValue",
					"Value " + vValue + " selected from dropdown " + vLocator);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSelectDropDownValue",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelSelectDropDownValue");
	}

	/**
	 * cfGlSelSelectDropDownText method is to select from drop down based on
	 * text
	 * 
	 * @param driver
	 * @param vLocator
	 * @param vText
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelSelectDropDownText(WebDriver driver, String vLocator, String vText) throws Exception {
		UTAFLog.debug("In cfGlSelSelectDropDownText");
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, vLocator, UTAFFwVars.utafFWExplicitWait);
			Select select = new Select(eleSearched);
			select.selectByVisibleText(vText);
			cfGLGenericPassHandling("cfGlSelSelectDropDownText",
					"Text " + vText + " selected from dropdown " + vLocator);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSelectDropDownText",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelSelectDropDownText");
	}

	/*
	 * Function : Get Text from an element and compare with element provided in
	 * Parameter Input Parameters : ElementName present in properties file , Tag
	 * name you need to fetch value , WebDriver
	 * 
	 * Excel input as : Elename , TagName , ValueTo Compare
	 * 
	 */
	public static boolean cfGlSelGetAttrAndCompare(WebDriver driver1, String elename, String tagName,
			String value2Compare) throws Exception {

		String text = "";
		boolean vVal = false;

		try {
			elementSearch(driver1, elename, 50);

			text = eleSearched.getAttribute(tagName);
			if (text.equalsIgnoreCase("") || text == null) {
				cfGLGenericExceptionHandlingThrow("cfGlSelGetAttrAndCompare",
						"No value in the element : " + elename);
			} else {
				if (value2Compare.equalsIgnoreCase(text)) {
					vVal = true;
					UTAFFwVars.utafFWTCStatus = "pass";
					UTAFLog.info("Value matching with given value : " + value2Compare + " and tag value :  " + text);
				} else {
					cfGLGenericExceptionHandlingThrow("cfGlSelGetAttrAndCompare",
							"Value not matching with given value : " + value2Compare + " and tag value " + text);
				}
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetAttrAndCompare",
					ex.getMessage() );
		}
		return vVal;
	}

	public static boolean cfGlSelGetCSSAttrAndCompare(WebDriver driver1, String elename, String cssField,
			String value2Compare) throws Exception {
		String text = "";
		boolean vVal = false;
		try {
			elementSearch(driver1, elename, UTAFFwVars.utafFWExplicitWait);
			text = eleSearched.getCssValue(cssField);
			if (text.equalsIgnoreCase("") || text == null) {
				cfGLGenericExceptionHandlingThrow("cfGlSelGetCSSAttrAndCompare",
						"No value in the element : " + elename);
			} else {
				if (value2Compare.equalsIgnoreCase(text)) {
					vVal = true;
					UTAFFwVars.utafFWTCStatus = "pass";
					UTAFLog.info("Value matching with given value : " + value2Compare + " and tag value :  " + text);
				} else {
					cfGLGenericExceptionHandlingThrow("cfGlSelGetCSSAttrAndCompare",
							"Value not matching with given value : " + value2Compare + " and tag value " + text);
				}
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetCSSAttrAndCompare",
					ex.getMessage() );
		}
		return vVal;
	}

	public static String cfGlSelGetCSSAttr(WebDriver vDriver, String vLocator, String cssField, String vVar)
			throws Exception {
		String text = "";
		try {
			elementSearch(vDriver, vLocator, UTAFFwVars.utafFWExplicitWait);
			text = eleSearched.getCssValue(cssField);
			if (text.equalsIgnoreCase("") || text == null) {
				cfGLGenericExceptionHandlingThrow("cfGlSelGetCSSAttr",
						"No value in the element : " + vLocator);
			} else {
				UTAFRead2.runTimeVar.put(vVar, text);
				UTAFFwVars.utafFWTCStatus = "pass";
				UTAFLog.info("Value stored in : " + vVar + " and tag value :  " + text);

			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetCSSAttr",
					ex.getMessage() );
		}
		return text;
	}

	public static void setPropFile(String configpath) {
		objectMapProps = new Properties();
		InputStream fis;
		try {
			fis = new FileInputStream(configpath);
			objectMapProps.load(fis);
		} catch (IOException ex) {
			UTAFFwVars.utafFWTCError = ex.getMessage() ;
			UTAFFwVars.utafFWTCStatus = "FAIL";
		}
	}

	

	public static String[] getObjectValue2(String objectName) throws Exception {
		UTAFLog.debug("In getObjectValue2");
		String arr[] = null;
		String[] arrlocator = { "id", "xpath", "name", "linkText", "cssSelector", "partialLinkText", "tagName" };
		List<String> arrLocatorList = Arrays.asList(arrlocator);
		try {
			String vaueOfProp = objectMapProps.getProperty(objectName);
			arr = vaueOfProp.split(":");

			int i = arr.length;
			if (arrLocatorList.contains(arr[i - 1])) {
				arr[0] = vaueOfProp.substring(0, vaueOfProp.length() - (arr[i - 1].length() + 1));
				UTAFLog.debug("Locatore value : " + arr[0]);
				arr[1] = arr[i - 1];
			}
			UTAFLog.debug("Element name is : " + arr[0] + " Element type is : " + arr[1]);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + ex.fillInStackTrace() + "\n Error in fetching the property : " +objectName );
		}
		return arr;
	}

	public static void captureScreenshot(String screenshotFileName, WebDriver driver) {
		Calendar c = Calendar.getInstance();
		String todayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
		double vRandom = Math.random();
		String vRandomS= vRandom +"";
		vRandomS= vRandomS.substring(2,vRandomS.length());
		todayDate = todayDate + vRandomS;
		if(UTAFFwVars.utafFWProps.containsKey("SCREENSHOT.PATH")){
			String tempPath = UTAFFwVars.utafFWProps.getProperty("SCREENSHOT.PATH");
			if(!(tempPath == null)){
				UTAFFwVars.utafFWSSPath = tempPath + UTAFFwVars.utafFWTSEID  + "/ScreenShots/" + todayDate + ".jpg";
			}else 
				UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath + "Reports\\" + UTAFFwVars.utafFWTSEID  + "/ScreenShots/" + todayDate + ".jpg";
		}else{
			UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath +"Reports\\" + UTAFFwVars.utafFWTSEID  + "/ScreenShots/" + todayDate + ".jpg";
		}
		
		
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
					 + UTAFFwVars.utafFWTSEID  + "/ScreenShots/" + todayDate + ".jpg";
		}
		
		try {
			try {
				JavascriptExecutor js;
				String pageLoadStatus = "";
				for (int i = 0; i <= 30; i++) {
					js = (JavascriptExecutor) driver;
					pageLoadStatus = (String) js.executeScript("return document.readyState");
					if (pageLoadStatus.equalsIgnoreCase("complete")) {
						UTAFLog.info("Ready State complete before taking screenshot ");
						break;
					}
				}
			} catch (Exception ex) {

			}
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(UTAFFwVars.utafFWSSPath));
			UTAFFwVars.utafFWSSPath = "./ScreenShots/" + todayDate + ".jpg";
		} catch (IOException | NullPointerException ex) {
			UTAFLog.error ("Error in captureScreenshot" + ex.getMessage() );
		} catch (Exception ex) {
			UTAFLog.error ("Error in captureScreenshot" + ex.getMessage() );
		}

	}

	public static void captureScreenshotPrint(String screenshotFileName) {
		Calendar c = Calendar.getInstance();
		String todayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
		double vRandom = Math.random();
		String vRandomS = vRandom + "";
		vRandomS = vRandomS.substring(2, vRandomS.length());
		todayDate = todayDate + vRandomS;

		if (UTAFFwVars.utafFWProps.containsKey("SCREENSHOT.PATH")) {
			String tempPath = UTAFFwVars.utafFWProps.getProperty("SCREENSHOT.PATH");
			if (!(tempPath == null)) {
				UTAFFwVars.utafFWSSPath = tempPath + UTAFFwVars.utafFWTSEID + "/ScreenShots/" + todayDate + ".jpg";
			} else
				UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath + "Reports\\" + UTAFFwVars.utafFWTSEID
						+ "/ScreenShots/" + todayDate + ".jpg";
		} else {
			UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath + "Reports\\" + UTAFFwVars.utafFWTSEID
					+ "/ScreenShots/" + todayDate + ".jpg";
		}

		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH") + UTAFFwVars.utafFWTSEID
					+ "/ScreenShots/" + todayDate + ".jpg";
		}

		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "bmp", new File(UTAFFwVars.utafFWSSPath));
			UTAFFwVars.utafFWSSPath = "./ScreenShots/" + todayDate + ".jpg";
		} catch (IOException | NullPointerException ex) {
			UTAFLog.error("Error in captureScreenshot" + ex.getMessage());
		} catch (Exception ex) {
			UTAFLog.error("Error in captureScreenshot" + ex.getMessage());
		}

	}
	
	
	public static String cfSelWaitEleClickable(WebDriver driver, String locator) throws Exception {
		String retValue = "FAIL";
		try {
			UTAFLog.info("Wait for element to be clickable");
			String element[] = null;
			element = getObjectValue2(locator);
			element[0] = updateProperty(element[0]);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.id(element[0])));
				eleSearched = driver.findElement(By.id(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element[0])));
				eleSearched = driver.findElement(By.xpath(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.name(element[0])));
				eleSearched = driver.findElement(By.name(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.linkText(element[0])));
				eleSearched = driver.findElement(By.linkText(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(element[0])));
				eleSearched = driver.findElement(By.cssSelector(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				retValue = "Pass";
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				retValue = "Pass";
			} else {
				retValue = "Fail@" + "please select valid locator type";
			}
		} catch (NoClassDefFoundError | InterruptedException | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfSelWaitEleClickable",
					ex.getMessage());
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfSelWaitEleClickable",
					ex.getMessage());
		}
		return retValue;
	}

	public static String replaceString(String inputParam) {
		try {
			System.out.println("Inside : returnTimeVar " + inputParam);
			while (inputParam.contains("${")) {
				int indexOF = inputParam.indexOf("${");
				int spaceIndex = indexOF + inputParam.substring(indexOF).indexOf("}");
				String runTimeVariableKey = inputParam.substring(indexOF + 2, spaceIndex);
				String newrunTimeVariableKey = runTimeVariableKey.replaceAll("\\s+", "");
				if (!UTAFRead2.runTimeVar.containsKey(newrunTimeVariableKey)) {
					UTAFRead2.runTimeVar.put(newrunTimeVariableKey, newrunTimeVariableKey);
				}
				String runTimeVariableValue = UTAFRead2.runTimeVar.get(newrunTimeVariableKey);
				inputParam = inputParam.substring(0, indexOF).concat(runTimeVariableValue)
						.concat(inputParam.substring(spaceIndex + 1));
				System.out.println("Variable key is : " + runTimeVariableKey);
				System.out.println("Variable value is : " + runTimeVariableValue);
			}
		} catch (Exception ex) {
		}
		return inputParam;
	}
	
	public static void cfGLSelMaximize(WebDriver driver) {
		try {
			driver.manage().window().maximize();
		} catch (Exception ex) {
			
		}
	}
	
	public static String updateProperty(String vProp) {
		if (!(vProp == null)) {
			for (int i = 1; i <= 5; i++) {
				if (vProp.contains("UTAFPropValue_" + i)) {
					UTAFLog.debug(UTAFRead2.runTimeVar.get("UTAFPropValue_" + i));
					vProp = vProp.replace("UTAFPropValue_" + i, UTAFRead2.runTimeVar.get("UTAFPropValue_" + i));
					UTAFLog.debug(vProp);

				}
			}
			if (vProp.contains("${")) {
				vProp = replaceString(vProp);
			}
			if (vProp.contains("UTAFPropValue")) {
				UTAFLog.debug(UTAFRead2.runTimeVar.get("UTAFPropValue"));
				vProp = vProp.replace("UTAFPropValue", UTAFRead2.runTimeVar.get("UTAFPropValue"));
				UTAFLog.debug(vProp);

			}
		}
		return vProp;
	}
	
	public static String elementSearch(WebDriver driver, String locator, int waitTimeout) throws Exception {
		try {
			eleSearched = null;
			cfGlSelCheckDocReady(driver);
			String element[] = null;
			element = getObjectValue2(locator);
			element[0] = updateProperty(element[0]);
			WebDriverWait wait = new WebDriverWait(driver,waitTimeout);

			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[0])));
				eleSearched = driver.findElement(By.id(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				UTAFLog.info("in xpath");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[0])));
				
				eleSearched = driver.findElement(By.xpath(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[0])));
				eleSearched = driver.findElement(By.name(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[0])));
				eleSearched = driver.findElement(By.linkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[0])));
				eleSearched = driver.findElement(By.cssSelector(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else {
				return "Fail@" + "please select valid locator type";
			}
		} catch (NoClassDefFoundError  | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("elementSearch",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("elementSearch",
					ex.getMessage() );
		}
		return "Fail@" + "please select valid locator type";

	}

	public static String cfGlSelSimpleElementSearch(WebDriver driver, String locator) throws Exception {
		try {
			eleSearched = null;
			cfGlSelCheckDocReady(driver);
			String element[] = null;
			element = getObjectValue2(locator);
			element[0] = updateProperty(element[0]);
			
			if (element[1].trim().equalsIgnoreCase("id")) {
				eleSearched = driver.findElement(By.id(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				eleSearched = driver.findElement(By.xpath(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				eleSearched = driver.findElement(By.name(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				eleSearched = driver.findElement(By.linkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				eleSearched = driver.findElement(By.cssSelector(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else {
				return "Fail@" + "please select valid locator type";
			}
		} catch (NoClassDefFoundError  | NoSuchElementException | UnhandledAlertException ex) {

			cfGLGenericExceptionHandlingThrow("cfGlSelSimpleElementSearch",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSimpleElementSearch",
					ex.getMessage() );
		}
		return "Fail@" + "please select valid locator type";

	}

	public static void cfGlSelectAutoComplete(WebDriver driver, String elename, String value) throws Exception {
		try {
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);

			eleSearched.clear();
			UTAFLog.info("Sending this :  " + value + " to " + elename);
			eleSearched.sendKeys(value);
			UTAFLog.info("Sent this : " + value + " to " + elename);
			Thread.sleep(5000);
			eleSearched.sendKeys(Keys.DOWN);
			Thread.sleep(5000);
			eleSearched.sendKeys(Keys.TAB);
			UTAFLog.info("Selected the value from auto complete checkbox");
			UTAFFwVars.utafFWTCStatus = "Pass";
		} catch (NoClassDefFoundError ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelectAutoComplete",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelectAutoComplete",
					ex.getMessage() );
		}
	}

	/**
	 * cfGlSelCheckDocReady method is used to wait for ready state
	 * 
	 * @param driver
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @since 01-Jul-2018
	 */
	public static void cfGlSelCheckDocReady(WebDriver driver) {
		/*
		 * UTAFLog.info("In cfGlSelCheckDocReady"); try { int i = 0; for (i = 0;
		 * i < UTAFFwVars.utafFWDocReadyTimeout; i++) { if (((String)
		 * ((JavascriptExecutor)
		 * driver).executeScript("return document.readyState"))
		 * .equalsIgnoreCase("complete")) { // if ((Boolean)
		 * ((JavascriptExecutor)driver).
		 * executeScript("return // jQuery.active == 0")){ //
		 * UTAFFwVars.utafFWTCStatus = "Pass"; // break; // }else{
		 * UTAFLog.info("Page load complete"); //
		 * UTAFLog.info("Waiting for jquery function to complete");
		 * Thread.sleep(1000); break; // } } else {
		 * UTAFLog.info("Waiting  for page load to complete");
		 * Thread.sleep(1000); } } if (i == 150) {
		 * cfGLGenericExceptionHandlingThrow(Thread.currentThread().
		 * getStackTrace()[1 ]. getMethodName(),
		 * "document is not ready for the click"); } } catch
		 * (NoClassDefFoundError ex) {
		 * UTAFLog.info("Error in cfGlSelCheckDocReady" + ex.getMessage() + "\n"
		 * + ex.getStackTrace()); //
		 * cfGLGenericExceptionHandlingThrow(Thread.currentThread().
		 * getStackTrace()[1 ]. getMethodName(),ex.getMessage() + "\n" +
		 * ex.getStackTrace() // + "::" + ex.getStackTrace()); } catch
		 * (Exception ex) { UTAFLog.info("Error in cfGlSelCheckDocReady" +
		 * ex.getMessage() ); //
		 * cfGLGenericExceptionHandlingThrow(Thread.currentThread().
		 * getStackTrace()[1 ]. getMethodName(),ex.getMessage() + "\n" +
		 * ex.getStackTrace() // + "::" + ex.getStackTrace()); }
		 * UTAFLog.info("Out cfGlSelCheckDocReady");
		 */
	}

	public static boolean cfGlSelIsElementDisplayed(WebDriver driver, String locator) throws Exception {
		UTAFLog.debug("In cfGlSelIsElementDisplayed ");
		boolean isElementDisplayed = false;
		try {
			try {
				cfGlSelfHealingOff();
				cfGlSelSimpleElementSearch(driver, locator);
				isElementDisplayed = eleSearched.isDisplayed();// wait.until(ExpectedConditions.visibilityOf(eleSearched)).isDisplayed();
			} catch (Exception ex) {
				UTAFFwVars.utafFWTCStatus = "PASS";
				isElementDisplayed = false;
			}
			cfGlSelfHealingOn();
		} catch (Exception ex) {
			cfGLGenericExceptionHandling("cfGlSelIsElementDisplayed", ex.getMessage());
		}
		UTAFLog.debug("Out cfGlSelIsElementDisplayed ");
		
		return isElementDisplayed;
	}
	
	//to remove
	public static boolean waitUntilElementDisplayed(final WebElement webElement, WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver arg0) {
				try {
					webElement.isDisplayed();
					return true;
				} catch (NoSuchElementException e) {
					return false;
				} catch (StaleElementReferenceException f) {
					return false;
				}
			}
		};
		return wait.until(elementIsDisplayed);
	}

	public static void cfGlSelElementClick(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFLog.debug("Clicking on :  " + locator);
			if (eleSearched.isEnabled()) {
				eleSearched.click();
				UTAFLog.debug("Clicked on : " + locator);
				cfGLGenericPassHandling("cfGlSelElementClick",
						"Successfully clicked on : " + locator);
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlSelElementClick",
						locator + " - Element is not enabled : ");
			}

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementClick",
					ex.getMessage());
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementClick",
					ex.getMessage());
		}
	}

	public static void cfGlSelSimpleElementClick(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			cfGlSelSimpleElementSearch(driver, locator);
			UTAFLog.debug("Clicking on :  " + locator);
			if (eleSearched.isEnabled()) {
				eleSearched.click();
				UTAFLog.debug("Clicked on : " + locator);
				cfGLGenericPassHandling("cfGlSelSimpleElementClick", "Successfully clicked on : " + locator);
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlSelSimpleElementClick",
						locator + " - Element is not enabled : ");
			}

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSimpleElementClick", ex.getMessage());
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSimpleElementClick", ex.getMessage());
		}
	}
	
	public static void cfGlSelDirectElementClick(WebDriver driver, WebElement vEle) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);

			UTAFLog.info("Clicking on :  " + vEle );
			vEle.click();
			UTAFLog.info("Clicked on : " + vEle);
			cfGLGenericPassHandling("cfGlSelDirectElementClick", "Successfully clicked on : " + vEle);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelDirectElementClick", ex.getMessage());
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelDirectElementClick", ex.getMessage());
		}
	}

	public static void cfGlJsElementClick(WebDriver driver, String locator) throws Exception {
		try {

			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFFwVars.utafFWTCStatus = "PASS";
			UTAFLog.info("Clicking on :  " + locator);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", eleSearched);
			UTAFLog.info("Clicked on : " + locator);
			cfGLGenericPassHandling("cfGlJsElementClick", "Successfully clicked on : " + locator);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementClick", ex.getMessage());
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementClick", ex.getMessage());
		}
	}

	public static void cfGlJsElementDirectClick(WebDriver driver, WebElement vEle) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", vEle);
			cfGLGenericPassHandling("cfGlJsElementDirectClick",
					"Successfully clicked on : " + vEle);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementDirectClick",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementDirectClick",
					ex.getMessage() );
		}
	}

	public static void cfGlJsElementSimpleClick(WebDriver driver, String vLocator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			cfGlSelSimpleElementSearch(driver, vLocator);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", eleSearched);
			cfGLGenericPassHandling("cfGlJsElementSimpleClick",
					"Successfully clicked on : " + vLocator);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementSimpleClick",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlJsElementSimpleClick",
					ex.getMessage() );
		}
	}

	public static void cfGlSelAlertAccept(WebDriver driver) throws Exception {
		try {
			
			cfGlSelCheckDocReady(driver);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			cfGLGenericPassHandling("cfGlSelAlertAccept",
					"Successfully clicked on alert");
		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertAccept",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertAccept",
					ex.getMessage() );
		}
	}
	
	public static void cfGlSelAlertAcceptIfExist(WebDriver driver) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			cfGLGenericPassHandling("cfGlSelAlertAcceptIfExist",
					"Successfully clicked on alert");
		} catch (UnhandledAlertException ex) {
		} catch (Exception ex) {
		}
	}

	public static String cfGlSelAlertGetText(WebDriver driver, String vVar) throws Exception {
		try {
			
			cfGlSelCheckDocReady(driver);
			Alert alert = driver.switchTo().alert();
			cfGlADDRunTimeVar(vVar, alert.getText());
			cfGLGenericPassHandling("cfGlSelAlertGetText",
					"Successfully stored text of alert in  " + vVar);
		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertGetText",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertGetText",
					ex.getMessage() );
		}
		return cfGlGetRunTimeVar(vVar);
	}

	public static void cfGlSelAlertGetTextAndCompare(WebDriver driver, String valueToCompare) throws Exception {
		try {
			
			cfGlSelCheckDocReady(driver);
			Alert alert = driver.switchTo().alert();
			if (valueToCompare.equalsIgnoreCase(alert.getText()))
				cfGLGenericPassHandling("cfGlSelAlertGetTextAndCompare",
						"Successfully compared text of alert in  " + valueToCompare);
			else
				cfGLGenericExceptionHandlingThrow("cfGlSelAlertGetTextAndCompare",
						"Alert text is " + alert.getText() + "while expected is " + valueToCompare);
		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertGetTextAndCompare",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelAlertGetTextAndCompare",
					ex.getMessage() );
		}
	}
	
	/*
	 * Check whether checkbox is selected and then click on Check
	 */
	public static void cfGLSelSelectCheckBox(WebDriver driver, String locator) throws Exception {
		try {
			String rfElementSearch = null;
			cfGlSelCheckDocReady(driver);
			rfElementSearch = elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			if (rfElementSearch.equalsIgnoreCase("Pass")) {
				UTAFFwVars.utafFWTCStatus = "pass";
				boolean b = eleSearched.isSelected();
				if (b == false) {
					UTAFLog.info("Clicking on :  " + locator);
					eleSearched.click();
					UTAFLog.info("Clicked on : " + locator);
				} else {
					UTAFLog.info("Element already checked :" + locator);
				}
				cfGLGenericPassHandling("cfGLSelSelectCheckBox",
						"Successfully clicked on : " + locator);
			} else {
				cfGLGenericExceptionHandlingThrow("cfGLSelSelectCheckBox",
						"Element not found ");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGLSelSelectCheckBox",
					ex.getMessage() );
		}
	}

	public static boolean cfGLSelCheckBoxCheck(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFFwVars.utafFWTCStatus = "pass";
			boolean b = eleSearched.isSelected();
			cfGLGenericPassHandling("cfGLSelCheckBoxCheck",
					"Successfully checked checkedbow on : " + locator);
			return b;

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGLSelCheckBoxCheck",
					ex.getMessage() );
		}
		return false;
	}

	public static void cfGLSelDoubleClick(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFLog.info("Double Clicking on :  " + locator);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
			Actions action = new Actions(driver);
			action.moveToElement(eleSearched).doubleClick().perform();
			UTAFLog.info("Double Clicked on : " + locator);
			cfGLGenericPassHandling("cfGLSelDoubleClick",
					"Successfully clicked on : " + locator);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGLSelDoubleClick",
					ex.getMessage() );
		}
	}
	
	public static void 	cfGLJsMoveToElement(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			Actions action = new Actions(driver);
			action.moveToElement(eleSearched).perform();
			cfGLGenericPassHandling("cfGLJsMoveToElement",
					"Successfully Moved to  " + locator);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGLJsMoveToElement",
					ex.getMessage() );
		}
	}
	
	public static void 	cfGlActionClick(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			Actions action = new Actions(driver);
			action.click(eleSearched);
			cfGLGenericPassHandling("cfGlActionClick",
					"Successfully clicked to  " + locator);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlActionClick",
					ex.getMessage() );
		}
	}

	public static void cfGlJsMultipleElementClick(WebDriver driver1, String locator, String locator1) throws Exception {
		try {
			elementSearch(driver1, locator, UTAFFwVars.utafFWExplicitWait);
			Actions action = new Actions(driver1);
			WebElement we = eleSearched;
			action.moveToElement(we);
			String element[] = null;
			locator1 = locator1.replaceAll("\\s+", "");
			element = getObjectValue2(locator1);
			if (element[0].contains("UTAFPropValue")) {
				UTAFLog.info(UTAFRead2.runTimeVar.get("UTAFPropValue"));
				element[0] = element[0].replace("UTAFPropValue", UTAFRead2.runTimeVar.get("UTAFPropValue"));
				UTAFLog.info(element[0]);

			}
			UTAFLog.info("Clicking on :  " + locator);
			UTAFLog.info(eleSearched.getText());
			Thread.sleep(200);

			action.moveToElement(we).moveToElement(driver1.findElement(By.xpath(element[0]))).click().build().perform();

			UTAFFwVars.utafFWTCStatus = "pass";

			UTAFLog.info("Clicked on : " + locator1);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}

	/*
	 * Method : multipleClicks Input : Elements names separated by comma It will
	 * click the non of elements passed to the method Editor : PranayKumar
	 * Infosys ltd
	 */
	public static void cfGlSelMultipleClicksArr(WebDriver driver, String elements) throws Exception {
		try {

			String[] elementList = new String[20];
			elementList = elements.split(",");
			int size = elementList.length;
			for (int i = 0; i < size; i++) {
				elementSearch(driver, elementList[i], UTAFFwVars.utafFWExplicitWait);
				UTAFRead2.testCaseStatus = "pass";
				UTAFLog.info("Clicking on :  " + elementList[i]);
				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", eleSearched);
				UTAFLog.info("Clicked on : " + elementList[i]);

			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelMultipleClicksArr",
					ex.getMessage() );
		}
	}

	/*
	 * Method : highlightElement Inputs : Locator and WebDriver It will locate
	 * the element and set the property of the elements to background yellow and
	 * give border using JavascriptExecutor
	 */
	public static void cfGlSelHighlightElement(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 4px solid red;');",
					eleSearched);
			cfGLGenericPassHandling("cfGlSelHighlightElement",
					"Successfully highlighted on : " + locator);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelHighlightElement",
					ex.getMessage() );
		}
	}

	/**
	 * cfGlSelRightClick method is to right click on the element
	 * 
	 * @param driver as WebDriver
	 * @param locator as String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2019
	 **/
	public static void cfGlSelRightClick(WebDriver driver, String locator) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			Actions actions = new Actions(driver);
			cfGlSelSimpleElementSearch(driver, locator);
			actions.contextClick(eleSearched).perform();
			cfGLGenericPassHandling("cfGlSelRightClick",
					"Right click on : " + locator);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelRightClick",
					ex.getMessage() );
		}
	}
	/*
	 * Check whether checkbox is selected and then click on Check
	 */

	public static void checkboxClick(String locator, WebDriver driver1) throws Exception {
		try {
			elementSearch(driver1, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFRead2.testCaseStatus = "pass";
			boolean b = eleSearched.isSelected();
			if (b == false) {
				UTAFLog.info("Clicking on :  " + locator);
				eleSearched.click();
				UTAFLog.info("Clicked on : " + locator);
			} else {
				UTAFLog.info("Element already checked :" + locator);
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("checkboxClick",
					ex.getMessage() );
		}
	}

	public static void rfWriteToExcel(String pKey, String usrComment) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			UTAFLog.info(dateFormat.format(date)); // 2016/11/16 12:08:43
			String vDataWB = UTAFFwVars.utafFWFolderPath + "Reports\\Data.xls";
			UTAFRead2.excelReportPath = vDataWB;
			File file = new File(vDataWB);
			WritableWorkbook workbook;
			WritableSheet sheet;
			Workbook workbookRead = Workbook.getWorkbook(new File(vDataWB));
			workbook = Workbook.createWorkbook(file, workbookRead);
			sheet = workbook.getSheet("Data");
			int vRowCount = sheet.getRows();
			UTAFLog.info("Ths is rows : " + vRowCount);
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setWrap(true);
			Label label0 = new Label(0, (vRowCount), Integer.toString(vRowCount), cellFormat);
			sheet.addCell(label0);
			// UTAFLog.info("Ths is rows : 1");
			Label label1 = new Label(1, (vRowCount), pKey, cellFormat);
			sheet.addCell(label1);
			// UTAFLog.info("Ths is rows : 2");
			Label label2 = new Label(2, (vRowCount), usrComment, cellFormat);
			sheet.addCell(label2);
			Label label3 = new Label(3, (vRowCount), UTAFRead2.currTestCaseName, cellFormat);
			sheet.addCell(label3);
			Label label4 = new Label(4, (vRowCount), dateFormat.format(date), cellFormat);
			sheet.addCell(label4);
			workbook.write();
			workbook.close();
			UTAFLog.info("Data added : " + usrComment);
		} catch (Exception ex) {
			UTAFRead2.testCaseStatus = "fail";
			UTAFRead2.testCaseError = ex.getMessage() ;
			UTAFLog.info(ex.getStackTrace() + "");
		}
	}

	public static void cfGlSelSendValue(WebDriver driver, String elename, String value) throws Exception {
		try {

			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			UTAFFwVars.utafFWTCStatus = "pass";
			UTAFLog.info("Sending this :  " + value + " to " + elename);
			eleSearched.clear();
			Thread.sleep(300);
			eleSearched.sendKeys(value);
			eleSearched.sendKeys(Keys.TAB);
			UTAFLog.info("Sent this : " + value + " to " + elename);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Successfully sent value on : " + elename);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}

	public static void cfGlSelClearAndSendValue(WebDriver driver, String elename, String value) throws Exception {
		try {

			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			UTAFFwVars.utafFWTCStatus = "pass";
			UTAFLog.info("Sending this :  " + value + " to " + elename);
			eleSearched.sendKeys(Keys.chord(Keys.CONTROL, "a"),Keys.BACK_SPACE,value);
			eleSearched.sendKeys(Keys.TAB);
			UTAFLog.info("Sent this : " + value + " to " + elename);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Successfully sent value on : " + elename);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}
	public static void cfGlSelClearTextBox(WebDriver driver, String elename) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			UTAFLog.info("Clearing the field " + elename);
			eleSearched.clear();
			UTAFLog.info("Cleared the field " + elename);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Successfully cleared value on : " + elename);

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}

	public static void selectDate(String elename, String dateValue, WebDriver driver1) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyddMM");
			Date date = formatter.parse(dateValue);
			// Get all the data of the date
			String strYear = (new SimpleDateFormat("yyyy")).format(date);
			UTAFLog.info(strYear);
			String strMonth = (new SimpleDateFormat("MMM")).format(date);
			UTAFLog.info(strMonth);
			String strDate = (new SimpleDateFormat("d")).format(date);
			UTAFLog.info(strDate);

			String rfElementSearch = null;
			rfElementSearch = elementSearch(driver1, elename, UTAFFwVars.utafFWExplicitWait);
			if (rfElementSearch.equalsIgnoreCase("Pass")) {
				UTAFLog.info("Pass");

				// click the calender icon of the calender field
				WebDriverWait wait = new WebDriverWait(driver1, 10);
				wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
				((JavascriptExecutor) driver1).executeScript("arguments[0].click();", eleSearched);
				Thread.sleep(200);
				UTAFLog.info("Clicked");
				Thread.sleep(2000);

				// select the Month
				// month_input is an xpath
				rfElementSearch = elementSearch(driver1, "month_input", UTAFFwVars.utafFWExplicitWait);
				if (rfElementSearch.equalsIgnoreCase("Pass")) {
					eleSearched.sendKeys(strMonth);
					String month = eleSearched.getAttribute("value");
					UTAFLog.info("Month after selection : " + month);
				} else {
					UTAFLog.info("Month field not found");
				}

				// select the year
				// year_input is an xpath
				rfElementSearch = elementSearch(driver1, "year_input", UTAFFwVars.utafFWExplicitWait);
				if (rfElementSearch.equalsIgnoreCase("Pass")) {
					eleSearched.sendKeys(strYear);
					String year = eleSearched.getAttribute("value");
					UTAFLog.info("Year after slection : " + year);
				} else {
					UTAFLog.info("Year field not found");
				}

				Thread.sleep(2000);

				// select the date from the table using anchor tag and click on
				// the anchor tag to click the date
				// date_table is an xpath
				rfElementSearch = elementSearch(driver1, "date_table", UTAFFwVars.utafFWExplicitWait);
				if (rfElementSearch.equalsIgnoreCase("Pass")) {
					List<WebElement> td = eleSearched.findElements(By.tagName("a"));
					{
						for (WebElement data : td) {
							String value = data.getText();
							UTAFLog.info("Td value is " + value);
							if (value.equalsIgnoreCase(strDate) && data.isEnabled()) {
								wait = new WebDriverWait(driver1, 10);
								wait.until(ExpectedConditions.elementToBeClickable(data));
								((JavascriptExecutor) driver1).executeScript("arguments[0].click();", data);
								UTAFLog.info("date selected");
							}
						}
					}

				} else {
					UTAFLog.info("Date table not found");
				}
			} else {
				UTAFLog.info("Calender icon not found");
			}
		} catch (Exception e) {
			UTAFLog.info(e.getMessage());
			UTAFFwVars.utafFWTCStatus = "fail";
			UTAFFwVars.utafFWTCError = e.getMessage();
		}

	}

	public static void cfGlSelGetText(WebDriver driver, String elename, String variableName) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);

			UTAFLog.info("Value found is " + eleSearched.getText());
			UTAFRead2.runTimeVar.put(variableName, eleSearched.getText());
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Text for " + elename + " is " + UTAFRead2.runTimeVar.get(variableName));

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}
	
	public static void cfGlSelSimpleGetText(WebDriver driver, String elename, String variableName) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			cfGlSelSimpleElementSearch(driver, elename);

			UTAFLog.info("Value found is " + eleSearched.getText());
			UTAFRead2.runTimeVar.put(variableName, eleSearched.getText());
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Text for " + elename + " is " + UTAFRead2.runTimeVar.get(variableName));

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}

	/*
	 * Function : Get Text from an element and compare with element provided in
	 * Parameter Input Parameters : ElementName present in properties file name
	 * you need to fetch value , WebDriver
	 * 
	 * Excel input as : Elename , TagName , ValueTo Compare
	 * 
	 */
	public static String cfGlSelGetTextAndCompare(WebDriver driver, String elename, String value2Compare)
			throws Exception {
		String vText = null;
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			vText = eleSearched.getText();
			if (vText.equalsIgnoreCase("")) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"Text is not present for given element : " + elename);
			} else {
				if (value2Compare.equalsIgnoreCase(vText)) {
					cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Given element text " + value2Compare + "is equal to actual value " + vText);
				} else {
					cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Given element text " + value2Compare + " is not equal to actual value " + vText);
				}
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
		return vText;
	}

	/*
	 * Function : Get Text from an element and compare with element provided in
	 * Parameter Input Parameters : ElementName present in properties file , Tag
	 * name you need to fetch value , WebDriver
	 * 
	 * Excel input as : Elename , TagName , ValueTo Compare
	 * 
	 */
	public static void cfGlSelGetAttrValueAndCompare(WebDriver driver, String elename, String tagName,
			String value2Compare) throws Exception {
		String vText = "";
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			vText = eleSearched.getAttribute(tagName);
			if (vText.equalsIgnoreCase("")) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"Text is not present for given attribute : " + tagName);
			} else {
				if (value2Compare.equalsIgnoreCase(vText)) {
					cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Given attribute value " + value2Compare + " is equal to actual value " + vText);
				} else {
					cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Given attribute value " + value2Compare + " is not equal to actual value " + vText);
				}
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		}
	}

	public static void cfGlWriteToExcel(String pKey, String usrComment) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			UTAFLog.info(dateFormat.format(date)); // 2016/11/16 12:08:43
			String vDataWB = UTAFFwVars.utafFXLReportPath;

			File file = new File(vDataWB);
			WritableWorkbook workbook;
			WritableSheet sheet;
			Workbook workbookRead = Workbook.getWorkbook(new File(vDataWB));
			workbook = Workbook.createWorkbook(file, workbookRead);
			sheet = workbook.getSheet("UTAFReport");
			int vRowCount = sheet.getRows();
			UTAFLog.info("Ths is rows : " + vRowCount);
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setWrap(true);
			Label label0 = new Label(0, (vRowCount), Integer.toString(vRowCount), cellFormat);
			sheet.addCell(label0);
			Label label1 = new Label(1, (vRowCount), pKey, cellFormat);
			sheet.addCell(label1);
			Label label2 = new Label(2, (vRowCount), usrComment, cellFormat);
			sheet.addCell(label2);
			Label label3 = new Label(3, (vRowCount), UTAFRead2.currTestCaseId, cellFormat);
			sheet.addCell(label3);
			Label label4 = new Label(4, (vRowCount), dateFormat.format(date), cellFormat);
			sheet.addCell(label4);
			workbook.write();
			workbook.close();
			UTAFLog.info("Data added : " + usrComment);
		} catch (Exception ex) {
			UTAFFwVars.utafFWTCStatus = "FAIL";
			UTAFFwVars.utafFWTCError = ex.getMessage() ;
			UTAFLog.info(ex.getStackTrace() + "");
		}
	}

	public static void printtoExcel(String pKey, String MoID, String CID) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			UTAFLog.info(dateFormat.format(date)); // 2016/11/16 12:08:43
			String vDataWB = System.getProperty("user.dir") + "\\Reports\\Data.xls";
			File file = new File(vDataWB);
			WritableWorkbook workbook;
			WritableSheet sheet;
			Workbook workbookRead = Workbook.getWorkbook(new File(vDataWB));
			workbook = Workbook.createWorkbook(file, workbookRead);
			sheet = workbook.getSheet("Data");
			int vRowCount = sheet.getRows();
			UTAFLog.info("Ths is rows : " + vRowCount);
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setWrap(true);
			Label label0 = new Label(0, (vRowCount), Integer.toString(vRowCount), cellFormat);
			sheet.addCell(label0);
			Label label1 = new Label(1, (vRowCount), pKey, cellFormat);
			sheet.addCell(label1);
			Label label2 = new Label(2, (vRowCount), MoID, cellFormat);
			sheet.addCell(label2);
			Label label3 = new Label(3, (vRowCount), UTAFRead2.currTestCaseName, cellFormat);
			sheet.addCell(label3);
			Label label4 = new Label(4, (vRowCount), CID, cellFormat);
			sheet.addCell(label4);
			Label label5 = new Label(5, (vRowCount), dateFormat.format(date), cellFormat);
			sheet.addCell(label5);
			workbook.write();
			workbook.close();
			UTAFLog.info("Data added in double values : " + MoID + ":" + CID);
		} catch (Exception ex) {
			UTAFFwVars.utafFWTCStatus = "fail";
			UTAFFwVars.utafFWTCError = ex.getMessage() ;
			UTAFLog.info(ex.getStackTrace() + "");
		}
	}

	public static boolean cfGlSelIsElementEnabled(WebDriver driver, String elename) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			elementSearch(driver, elename, UTAFFwVars.utafFWExplicitWait);
			UTAFLog.info("Checking element is enabled or not at  " + elename);
			if (eleSearched.isEnabled()) {
				cfGLGenericPassHandling("cfGlSelIsElementEnabled",
						"Element is enabled " + elename);
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelIsElementEnabled",
					ex.getMessage() );

		}catch (Error ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelIsElementEnabled",
					ex.getMessage() );

		}
		return false;
	}

	public static void cfGlSelValidateTitle(WebDriver driver, String expectedTitle) throws Exception {
		try {
			cfGlSelCheckDocReady(driver);
			String actualTitle = driver.getTitle();
			if (actualTitle.equalsIgnoreCase(expectedTitle)) {
				cfGLGenericPassHandling("cfGlSelValidateTitle",
						"Given title text " + expectedTitle + " is equal to actual value " + actualTitle);
			} else {
				cfGLGenericPassHandling("cfGlSelValidateTitle",
						"Given title text " + expectedTitle + " is not equal to actual value " + actualTitle);
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelValidateTitle",
					ex.getMessage() );
		}
	}

	public static void cfGlSelDrawLine(WebDriver driver, String locator) throws Exception {
		try {
			elementSearch(driver, locator, UTAFFwVars.utafFWExplicitWait);
			UTAFFwVars.utafFWTCStatus = "Pass";
			UTAFLog.info("Clicking on :  " + locator);
			UTAFLog.info(eleSearched.getText());
			Actions builder = new Actions(driver);
			Action drawAction = builder.moveToElement(eleSearched, 135, 15).click().moveByOffset(200, 60).click()
					.moveByOffset(100, 70).doubleClick().build();
			drawAction.perform();
			Thread.sleep(200);
			cfGLGenericPassHandling("cfGlSelDrawLine", "");

		} catch (NoClassDefFoundError ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelDrawLine",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelDrawLine",
					ex.getMessage() );
		}
	}



	public static List<WebElement> cfGlSelElementsSearch(WebDriver driver, String locator) throws Exception {
		List<WebElement> eleSearched = new ArrayList<>();
		try {
			cfGlSelfHealingOff();
			String element[] = null;
			element = getObjectValue2(locator);
			element[0]=updateProperty(element[0]);
			cfGlSelCheckDocReady(driver);
			if (element[1].trim().equalsIgnoreCase("id")) {
				eleSearched = driver.findElements(By.id(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				eleSearched = driver.findElements(By.xpath(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				eleSearched = driver.findElements(By.name(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				eleSearched = driver.findElements(By.linkText(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				eleSearched = driver.findElements(By.cssSelector(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				eleSearched = driver.findElements(By.partialLinkText(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				eleSearched = driver.findElements(By.partialLinkText(element[0]));
			}
			cfGLGenericPassHandling("cfGlSelElementsSearch", "");
			cfGlSelfHealingOn();
			return eleSearched;
		} catch (NoClassDefFoundError | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementsSearch",
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelElementsSearch",
					ex.getMessage() );

		}
		return eleSearched;
	}

	public static void cfGlElementScrollTillVisible(WebDriver driver, String ele) {
		try {
			String element = cfGlSelSimpleElementSearch(driver, ele);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		} catch (Exception ex) {
			UTAFLog.error("Error in cfGlSelPageScroll " +  ex.getMessage());
		}
	}

	public static void cfGlSelPageScroll(WebDriver driver, String scroll){
		try {
			cfGlSelCheckDocReady(driver);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			if (scroll.equalsIgnoreCase("down")) {
				UTAFLog.info("down");
				jse.executeScript("scroll(0, 500);");
			} else if (scroll.equalsIgnoreCase("up")) {
				UTAFLog.info("up");
				jse.executeScript("scroll(0, -500);");
			}
		} catch (Exception ex) {
			UTAFLog.error("Error in cfGlSelPageScroll " +  ex.getMessage());
		}
	}

	public static String generateRandomNumber() throws NoSuchAlgorithmException {
		String strRandom = "";
		String strNumbers = "123456789";
		Random rnd = SecureRandom.getInstanceStrong();
		StringBuilder strRandomNumber = new StringBuilder(9);
		for (int i = 0; i < 9; i++) {
			strRandomNumber.append(strNumbers.charAt(rnd.nextInt(strNumbers.length())));
		}
		strRandom = strRandomNumber.toString();
		UTAFLog.info(strRandom);
		return strRandom;
	}
	
	public static String cfGLRandomName(String vTemp) throws NoSuchAlgorithmException {
		
		String todayDate = "A12448485";//new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
		String strRandom = "A";
		try{
			Calendar c = Calendar.getInstance();
			todayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
		double vRandom = Math.random();
		String vRandomS= vRandom +"";
		vRandomS= vRandomS.substring(2,vRandomS.length());
		todayDate = todayDate + vRandomS;
		
		// Creating array and Storing the array 
        // returned by toCharArray() 
        char[] ch = todayDate.toCharArray();
        String[] vChracters = {"A","B","C","D","E","F","G","H","I","J"};
        String[] vInts = {"0","1","2","3","4","5","6","7","8","9"};
        
        // Printing array 
        for (char vChar : ch) {
        	for (int i = 0; i < vInts.length;i++) {
				if((vChar+"").equalsIgnoreCase(vInts[i]))
					strRandom = strRandom.concat(vChracters[i]);
			}
        } 
        UTAFRead2.runTimeVar.put(vTemp, strRandom);
		UTAFLog.info(strRandom);}
		catch(Exception ex){
			UTAFRead2.runTimeVar.put(vTemp, todayDate);
		}
		return strRandom;
	}

	public static void cfGlSelWaitForElementToBeClickable(WebDriver driver, String locator) throws Exception {
		UTAFLog.debug("In cfGlSelWaitForElementToBeClickable");
		try {
			cfGlSelfHealingOff();
			String element[] = null;
			element = getObjectValue2(locator);
			element[0]=updateProperty(element[0]);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			cfGlSelCheckDocReady(driver);
			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.name(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.tagName(element[0] ) ) ) );
			}
			cfGlSelfHealingOn();
			cfGLGenericPassHandling("cfGlSelWaitForElementToBeClickable", "");
		} catch (NoClassDefFoundError | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeClickable",
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeClickable",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelWaitForElementToBeClickable");
	}
	
	public static void cfGlSelWaitForElementToBeDisappear(WebDriver driver, String locator) throws Exception {
		UTAFLog.debug("In cfGlSelWaitForElementToBeClickable");
		try {
			cfGlSelfHealingOff();
			String element[] = null;
			element = getObjectValue2(locator);
			element[0]=updateProperty(element[0]);
			WebDriverWait wait = new WebDriverWait(driver, UTAFFwVars.utafFWExplicitWait);
			cfGlSelCheckDocReady(driver);
			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id(element[0])))) ;
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.xpath(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.name(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.linkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.cssSelector(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.partialLinkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated((By.tagName(element[0] ) ) ) );
			}
			cfGlSelfHealingOn();
			cfGLGenericPassHandling("cfGlSelWaitForElementToBeDisappear", "");
		} catch (NoClassDefFoundError | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeDisappear", ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeDisappear", ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelWaitForElementToBeDisappear");
	}
	

	public static void cfGlSelWaitForElementToBeClickSeconds(WebDriver driver, String locator, String vSeconds) throws Exception {
		UTAFLog.debug("In cfGlSelWaitForElementToBeClickable");
		int vSecondsI = 0;
		try {
			cfGlSelfHealingOff();
			if (! (vSeconds.isEmpty() || vSeconds == null)) 
				vSecondsI = Integer.parseInt(vSeconds);
			
			String element[] = null;
			element = getObjectValue2(locator);
			element[0]=updateProperty(element[0]);
			WebDriverWait wait = new WebDriverWait(driver, vSecondsI);
			cfGlSelCheckDocReady(driver);
			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.name(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(element[0] ) ) ) );
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.tagName(element[0] ) ) ) );
			}
			cfGlSelfHealingOn();
			cfGLGenericPassHandling("cfGlSelWaitForElementToBeClickSeconds", "");
		} catch (NoClassDefFoundError | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeClickSeconds",
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelWaitForElementToBeClickSeconds",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlSelWaitForElementToBeClickSeconds");
	}




	public static String cfGlSelSimpleGetAttribute(WebDriver driver, String eleName, String tagName) throws Exception {
		String text = "";
		UTAFLog.debug("In cfGlSelSimpleGetAttribute");
		try {
			cfGlSelSimpleElementSearch(driver, eleName);
			text = eleSearched.getAttribute(tagName);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelSimpleGetAttribute",
					ex.getMessage());
		}
		UTAFLog.debug("Out cfGlSelSimpleGetAttribute");
		return text;
	}
	
	public static String cfGlSelGetAttribute(WebDriver driver, String eleName, String tagName) throws Exception {
		String text = "";
		UTAFLog.debug("In cfGlSelGetAttribute");
		try {
			elementSearch(driver, eleName, UTAFFwVars.utafFWExplicitWait);
			text = eleSearched.getAttribute(tagName);
			cfGLGenericPassHandling("cfGlSelGetAttribute", tagName + " = " + text);
			
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetAttribute",
					ex.getMessage());
		}
		UTAFLog.debug("Out cfGlSelGetAttribute");
		return text;
	}

	public static void cfGlSelGetAttributeStore(WebDriver driver, String eleName, String tagName, String vVar)
			throws Exception {
		UTAFLog.debug("In cfGlSelGetAttributeStore");
		try {
			String text = "";
			String rfElementSearch = elementSearch(driver, eleName, UTAFFwVars.utafFWExplicitWait);
			if (rfElementSearch.equalsIgnoreCase("Pass")) {
				text = eleSearched.getAttribute(tagName);
				if (text.equalsIgnoreCase("")) {
					cfGLGenericExceptionHandlingThrow("cfGlSelGetAttributeStore",
							"No attribute value in the element : " + eleName + " - Tag :" + tagName);
				} else {
					cfGlADDRunTimeVar(vVar, text);
					UTAFFwVars.utafFWTCStatus = "PASS";
					UTAFLog.info("Value stored in : " + vVar + " and tag value :  " + text);
				}
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSelGetAttributeStore",
					ex.getMessage() );

		}
		UTAFLog.debug("Out cfGlSelGetAttributeStore");
	}



	

	public static String cfGlXTempGetUnixTime(String vVar) throws Exception {
		UTAFLog.debug("In cfGlXTempGetUnixTime");
		try {
			long unixTime = System.currentTimeMillis() / 1000L;
			cfGlADDRunTimeVar(vVar, unixTime + "");
			cfGLGenericPassHandling("cfGlXTempGetUnixTime",
					"Successfully generated unix time  : " + unixTime);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlXTempGetUnixTime",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlXTempGetUnixTime");
		return cfGlGetRunTimeVar(vVar);
		
	}

	/*
	 * public static void cfGlXTempGetEpochTime(String vVar) { try { long now =
	 * Instant.now().getEpochSecond(); UTAFRead2.runTimeVar.put(vVar,now + "");
	 * cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].
	 * getMethodName(), "Successfully generated unix time  : " + now);
	 * }catch(Exception ex){
	 * cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[
	 * 1]. getMethodName(), ex.getMessage() ); } }
	 */

	public static void cfGlCopyFile(String vPathDestFileName, String vPathSourceFileName) throws Exception {
		try {
			String destFileName = UTAFFwVars.utafFWFolderPath + vPathDestFileName;
			String srcFileName = UTAFFwVars.utafFWFolderPath + vPathSourceFileName;
			File srcfile = new File(srcFileName);
			if (srcfile.exists()) {
				File destfile = new File(destFileName);
				FileUtils.copyFile(srcfile, destfile);
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlCopyFile",
						"copy of this file is not possible due to " + srcFileName + " does not exist!!!");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlCopyFile",
					ex.getMessage() );
		}
	}

	public static void cfGlModifyTextFile(String vPath, String vVarToReplace, String vVarValue) throws Exception {
		UTAFLog.debug("In cfGlModifyTextFile");
		try {
			String newFileName = UTAFFwVars.utafFWFolderPath + vPath;
			File file2 = new File(newFileName);
			String pathContent = "";
			pathContent = FileUtils.readFileToString(file2);
			UTAFLog.info(pathContent);
			if (vVarValue.equalsIgnoreCase("NA")) {
				String[] varArr = vVarToReplace.split(",");
				for (int i = 0; i < varArr.length; i++) {
					pathContent = pathContent.replace(varArr[i], cfGlGetRunTimeVar(varArr[i]));
				}
			} else {
				String[] varArr = vVarToReplace.split(",");
				String[] varArrValue = vVarValue.split(",");
				for (int i = 0; i < varArr.length; i++) {
					pathContent = pathContent.replace(varArr[i], varArrValue[i]);
				}
			}
			FileUtils.write(file2, pathContent);
			cfGLGenericPassHandling("cfGlModifyTextFile",
					"Successfully Modified File");
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlModifyTextFile",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlModifyTextFile");
	}

	/*
	 * public static void cfGlSFTPPutLocation(String uFileName, String flowType)
	 * throws JSchException, SftpException { try{ String uUserName =
	 * UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	 * ".APP.FILEZILA.USER"); String uPassword =
	 * UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	 * ".APP.FILEZILA.PASS"); String uServerName =
	 * UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	 * ".APP.FILEZILA.SERVER"); String locationName =
	 * UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	 * ".APP.FILEZILA.PATH."+flowType);
	 * 
	 * uFileName = UTAFFwVars.utafFWFolderPath+uFileName; int uPort =
	 * Integer.parseInt(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV
	 * + ".APP.FILEZILA.PORT")); UTAFLog.info(uUserName + uPassword+ uPort
	 * +uFileName +locationName +uServerName); JSch jsch = new JSch();
	 * com.jcraft.jsch.Session session = jsch.getSession(uUserName, uServerName,
	 * uPort); session.setConfig("StrictHostKeyChecking", "no");
	 * session.setPassword(uPassword); session.connect();
	 * UTAFLog.info("In cfGlSFTPPutLocation connection successful!!!" ); Channel
	 * channel = session.openChannel("sftp"); channel.connect();
	 * UTAFLog.info("In cfGlSFTPPutLocation SFTP successful!!!" ); ChannelSftp
	 * sftpChannel = (ChannelSftp) channel;
	 * sftpChannel.put(uFileName,locationName );
	 * UTAFLog.info("In cfGlSFTPPutLocation file transfer successful!!!" );
	 * sftpChannel.exit(); channel.disconnect(); session.disconnect();
	 * UTAFLog.info("In cfGlSFTPPutLocation disconnect session successful!!!"
	 * );} catch(Exception ex){
	 * cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[
	 * 1]. getMethodName(), ex.getMessage() ); } //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 */

	public static String cfGlXTempGetDate(String vDateFormat, String vVar) throws Exception {
		UTAFLog.debug("In cfGlXTempGetDate");
		String today = "";
		try {
			Date date = new Date();
			SimpleDateFormat dt = new SimpleDateFormat(vDateFormat);
			today = dt.format(date);
			UTAFLog.info(today);
			cfGlADDRunTimeVar(vVar, today + "");
			cfGLGenericPassHandling("cfGlXTempGetDate",
					"Successfully generated Date time  : " + today);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlXTempGetDate",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlXTempGetDate");
		return today;
	}

	public static void cfGlAlertValidation(WebDriver driver, String alertVal) {
		try {
			Alert alert = driver.switchTo().alert();
			if (alertVal.equalsIgnoreCase("Alert_Y")) {
				alert.accept();
			} else if (alertVal.equalsIgnoreCase("Alert_N")) {
				alert.dismiss();
			}
		} catch (Exception e) {
		}
	}

	public static void UpdateExcel(String customerID, String address, String QuoteNum, String OrderID) {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date dateobj = new Date();
			String currentDate = df.format(dateobj);
			File excelFile;
			FileInputStream fis;
			XSSFWorkbook excelSheet;
			XSSFSheet spreadsheet;
			excelFile = new File(UTAFFwVars.utafFWFolderPath + "OrderFolder//OrdersList.xlsx");
			fis = new FileInputStream(excelFile);
			excelSheet = new XSSFWorkbook(fis);
			spreadsheet = excelSheet.getSheet("Sheet1");
			int rowCount = spreadsheet.getLastRowNum();
			Row row = spreadsheet.createRow(rowCount + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(currentDate);
			cell = row.createCell(1);
			cell.setCellValue(customerID);
			cell = row.createCell(2);
			cell.setCellValue(address);
			cell = row.createCell(3);
			cell.setCellValue(QuoteNum);
			cell = row.createCell(4);
			cell.setCellValue(OrderID);
			fis.close();
			FileOutputStream fos = new FileOutputStream(
					new File(UTAFFwVars.utafFWFolderPath + "OrderFolder//OrdersList.xlsx"));
			excelSheet.write(fos);
			fos.close();
			
		} catch (Exception ex) {
			UTAFFwVars.utafFWTCStatus = "fail";
			UTAFFwVars.utafFWTCError = ex.getMessage() ;
			UTAFLog.info(ex.getStackTrace() + "");
		}
	}

	public static void cfGlSwitchIframeElement(WebDriver driver, String element) throws Exception {
		UTAFLog.debug("In cfGlSwitchIframeElement");
		try {
			driver.switchTo().defaultContent();
			elementSearch(driver, element, UTAFFwVars.utafFWExplicitWait);
			driver.switchTo().frame(eleSearched);
			UTAFFwVars.utafFWTCStatus = "PASS";
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSwitchIframeElement",
					ex.getMessage() );
		}
		UTAFLog.debug("In cfGlSwitchIframeElement");
	}

	public static void cfGlSwitchIframeIterate(WebDriver driver, String element, String flag) throws Exception {
		UTAFLog.debug("IN cfGlSwitchIframeIterate");
		try {
			cfGlSelfHealingOff();
			driver.switchTo().defaultContent();
			if (flag.equalsIgnoreCase("Y")) {
				try {
					int size = driver.findElements(By.tagName("iframe")).size();
					UTAFLog.info("iframes : " + size);
					for (int i = 0; i <= size; i++) {
						driver.switchTo().frame(i);
						int total = cfGlReturnElementsList(driver,element).size();
						UTAFLog.info(total + "");
						if (total == 0) {
							driver.switchTo().defaultContent();
						} else if (total >= 1) {
							break;
						}
					}
					
				} catch (Exception ex) {
					cfGLGenericExceptionHandlingThrow("cfGlSwitchIframeIterate",
							ex.getMessage() );
				}
			} else if (flag.equalsIgnoreCase("N")) {
				driver.switchTo().defaultContent();
			}
			cfGlSelfHealingOn();
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSwitchIframeIterate",
					ex.getMessage() );
			
		}
		UTAFLog.debug("IN cfGlSwitchIframeIterate");
	}

	public static List<WebElement> cfGlReturnElementsList(WebDriver driver, String locator) throws Exception {
		UTAFLog.debug("In cfGlReturnElementsList");
		List<WebElement> webEle = new ArrayList<WebElement>();
		
		try {
			cfGlSelfHealingOff();
			String element[] = null;
			element = getObjectValue2(locator);
			element[0] = updateProperty(element[0]);
			if (element[1].trim().equalsIgnoreCase("id")) {
				webEle = driver.findElements(By.id(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				webEle = driver.findElements(By.xpath(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				webEle = driver.findElements(By.name(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				webEle = driver.findElements(By.linkText(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				webEle = driver.findElements(By.cssSelector(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				webEle = driver.findElements(By.partialLinkText(element[0]));
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				webEle = driver.findElements(By.partialLinkText(element[0]));
			} 
			cfGlSelfHealingOn();
		} catch (NoClassDefFoundError  | NoSuchElementException | UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlReturnElementsList",
					ex.getMessage() );
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlReturnElementsList",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlReturnElementsList");
		
		return webEle;

	}
	public static void cfGl_securityPopup(WebDriver driver) throws InterruptedException {

	}

	/*
	  public static void cfGlJmsCall(String xmlValue, String vQueName, String
	  corrId, String vLookUp) throws NamingException, JMSException, Exception {
	  
	  UTAFLog.info("Send below xml to WSL : " + xmlValue);
	  
	  Properties env = new Properties(); env.put(Context.SECURITY_PRINCIPAL,
	  UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	  ".SONIC.USER")); env.put(Context.SECURITY_CREDENTIALS,
	  UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	  ".SONIC.PASS")); env.put(Context.INITIAL_CONTEXT_FACTORY,
	  "com.sonicsw.jndi.mfcontext.MFContextFactory");
	  env.put(Context.PROVIDER_URL,
	  UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	  ".SONIC.CONURL")); env.put("com.sonicsw.jndi.mfcontext.domain",
	  UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	  ".SONIC.DOMAIN")); InitialContext uJNDI = new InitialContext(env);
	  UTAFLog.info("Context created successfully!!!");
	  
	  // lookup queue using queue name Queue queue = (Queue)
	  uJNDI.lookup(vQueName); UTAFLog.info("Queue lookup successful!!!");
	  
	  // create connection factory by passing the connection factory name
	  ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(vLookUp);
	  UTAFLog.info("connection factory created!!!");
	  
	  // create connection by passing the user name and password Connection
	  uConnection = (Connection)
	  conFactory.createConnection(UTAFFwVars.utafFWProps.getProperty(
	  UTAFFwVars. utafFWENV + ".SONIC.USER"),
	  UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +
	  ".SONIC.PASS")); UTAFLog.info("Connection created successfully!!");
	  
	  // created session Session session = uConnection.createSession(false,
	  QueueSession.AUTO_ACKNOWLEDGE);
	  
	  // Start the connection uConnection.start();
	  
	  // create message producer by queue name variable MessageProducer
	  producer = session.createProducer(queue);
	  
	  // create message, also here u can read you file and pass it as string
	  TextMessage msg = session.createTextMessage(xmlValue);
	  
	  // set JMSCorrealtionID msg.setJMSCorrelationID(corrId);
	  
	  // send message producer.send(msg);
	  
	  UTAFLog.info(session.getAcknowledgeMode());
	  
	  // Connection close producer.close(); session.close();
	  uConnection.close(); // System.gc();
	  UTAFLog.info("All Connections closed"); // System.exit(0);
	  Thread.sleep(3000);
	  
	  }
	 */

	/**
	 * cfGlADDRunTimeVar is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @param vVarValue
	 *            as String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static void cfGlADDRunTimeVar(String varName, String vVarValue) throws Exception {
		UTAFLog.debug("In cfGlADDRunTimeVar");
		try {
			UTAFRead2.runTimeVar.put(varName, vVarValue);
			cfGLGenericPassHandling("cfGlADDRunTimeVar",
					"Variable " + varName + " : Value " + vVarValue);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlADDRunTimeVar",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlADDRunTimeVar");
	}
	
	/**
	 * cfAddReportInVars is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @param vVarValue
	 *            as String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static void cfAddReportInVars(String varName, String vVarValue) throws Exception {
		UTAFLog.debug("In cfGlADDRunTimeVar");
		try {
			UTAFFwVars.inVar.put(varName, vVarValue);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Variable " + varName + " : Value " + vVarValue);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfAddReportInVars",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfAddReportInVars");
	}

	/**
	 * cfAddReportOutVars is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @param vVarValue
	 *            as String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static void cfAddReportOutVars(String varName, String vVarValue) throws Exception {
		UTAFLog.debug("In cfAddReportOutVars");
		try {
			UTAFFwVars.outVar.put(varName, vVarValue);
			cfGLGenericPassHandling("cfAddReportOutVars",
					"Variable " + varName + " : Value " + vVarValue);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfAddReportOutVars",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfAddReportOutVars");
	}
	
	/**
	 * cfAddReportEnvVars is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @param vVarValue
	 *            as String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Mar-2021
	 **/
	public static void cfAddReportEnvVars(String varName, String vVarValue) throws Exception {
		UTAFLog.debug("In cfAddReportEnvVars");
		try {
			UTAFFwVars.envVar.put(varName, vVarValue);
			cfGLGenericPassHandling("cfAddReportEnvVars",
					"Variable " + varName + " : Value " + vVarValue);
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfAddReportEnvVars",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfAddReportEnvVars");
	}
	
	
	public static void cfAddReportingVarMap(String vMapType,  HashMap<String, String> vMap) throws Exception {
		UTAFLog.debug("In cfAddReportingVarMap");
		try {
			if(vMapType.equalsIgnoreCase("ENV")){
				UTAFFwVars.envVar.putAll(vMap);
			}
			else if(vMapType.equalsIgnoreCase("INPUT")){
				UTAFFwVars.inVar.putAll(vMap);
			}
			else if(vMapType.equalsIgnoreCase("OUTPUT")){
				UTAFFwVars.outVar.putAll(vMap);
			}
			else{
				cfGLGenericExceptionHandlingThrow("cfAddReportingVarMap",
						"Incorrect map type passed, please pass ENV, INPUT, or OUTPUT as first paramater");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfAddReportingVarMap",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfAddReportingVarMap");
	}
	
	public static void cfAddReportingvVars(String vInVars, String vOutVars, String vEnvVars) throws Exception {
		UTAFLog.debug("In cfAddReportingvVars");
		try {
			if((vInVars != null) && !(vInVars.isEmpty())){
			String[] inVarArr = vInVars.split(";");
			for (String string : inVarArr) {
				UTAFFwVars.inVar.put(string, cfGlGetRunTimeVar(string));
			}
			}
			if((vOutVars != null) && !(vOutVars.isEmpty())){
			String[] outVarArr = vOutVars.split(";");
			for (String string : outVarArr) {
				UTAFFwVars.outVar.put(string, cfGlGetRunTimeVar(string));
			}
			}
			if((vEnvVars != null) && !(vEnvVars.isEmpty())){
			String[] envVarArr = vEnvVars.split(";");
			for (String string : envVarArr) {
				UTAFFwVars.envVar.put(string, cfGlGetRunTimeVar(string));
			}
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfAddReportingvVars",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfAddReportingvVars");
	}
	/**
	 * cfGlGetRunTimeVar is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @return String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static String cfGlGetRunTimeVar(String varName) throws Exception {
		UTAFLog.debug("In cfGlGetRunTimeVar");
		String vValue = "";
		try {
			if (UTAFRead2.runTimeVar.containsKey(varName)) {
				vValue = UTAFRead2.runTimeVar.get(varName);
				cfGLGenericPassHandling("cfGlGetRunTimeVar",
						"Value of " + varName + " is " + vValue);
				return vValue;
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlGetRunTimeVar",
						"Fail : " + varName + " is not available");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlGetRunTimeVar",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlGetRunTimeVar");
		return vValue;
	}
	
	
	/**
	 * cfGlGetFrameworkProperty is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @return String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static String cfGlGetFrameworkProperty(String varName) throws Exception {
		UTAFLog.debug("In cfGlGetFrameworkProperty");
		String vValue = "";
		try {
			if (UTAFFwVars.utafFWProps.containsKey(varName)) {
				vValue = UTAFFwVars.utafFWProps.getProperty(varName);
				cfGLGenericPassHandling("cfGlGetFrameworkProperty",
						"Value of " + varName + " is " + vValue);
				return vValue;
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlGetFrameworkProperty",
						"Fail : " + varName + " is not available");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlGetFrameworkProperty",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlGetFrameworkProperty");
		return vValue;
	}
	
	
	/**
	 * cfGlGetElementProperty is to get the run time variable
	 * 
	 * @param varName
	 *            as String
	 * @return String
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static String cfGlGetElementProperty(String varName) throws Exception {
		UTAFLog.debug("In cfGlGetElementProperty");
		String vValue = "";
		try {
			if (objectMapProps.containsKey(varName)) {
				vValue = objectMapProps.getProperty(varName);
				cfGLGenericPassHandling("cfGlGetElementProperty",
						"Value of " + varName + " is " + vValue);
				return vValue;
			} else {
				cfGLGenericExceptionHandlingThrow("cfGlGetElementProperty",
						"Fail : " + varName + " is not available");
			}
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlGetElementProperty",
					ex.getMessage() );
		}
		UTAFLog.debug("Out cfGlGetElementProperty");
		return vValue;
	}
	/*
	 * public static void cfGlReport(String vStatus, String vDesc, boolean
	 * needLog) { UTAFLog.info("cfGlReport"); try { if
	 * (vStatus.equalsIgnoreCase("PASS")) UTAFRead2.test.log(LogStatus.PASS,
	 * vDesc); else if (vStatus.equalsIgnoreCase("FAIL"))
	 * UTAFRead2.test.log(LogStatus.FAIL, vDesc); else if
	 * (vStatus.equalsIgnoreCase("INFO")) UTAFRead2.test.log(LogStatus.INFO,
	 * vDesc); else if (vStatus.equalsIgnoreCase("SKIP"))
	 * UTAFRead2.test.log(LogStatus.SKIP, vDesc); else if
	 * (vStatus.equalsIgnoreCase("FATAL")) UTAFRead2.test.log(LogStatus.FATAL,
	 * vDesc); else if (vStatus.equalsIgnoreCase("ERROR"))
	 * UTAFRead2.test.log(LogStatus.ERROR, vDesc); else if
	 * (vStatus.equalsIgnoreCase("WARNING"))
	 * UTAFRead2.test.log(LogStatus.WARNING, vDesc); else
	 * UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc); if (needLog) {
	 * UTAFLog.info("Step : " + vDesc + " is " + vStatus); }
	 * 
	 * } catch (Exception ex) { UTAFLog.info(ex.getMessage() + "\n" +
	 * ex.getStackTrace()); UTAFLog.info("Failed in cfGlReport"); } }
	 */


	
	public static void cfGlReport(WebDriver driver, String vStatus, String vDesc, boolean needLog, boolean needScreen)
			throws Exception {
		UTAFLog.info("cfGlReport");
		UTAFLog.info(vDesc);
		try {
			if (needScreen) {

				if (!(driver == null) && isAlertPresent(driver))
					captureScreenshotPrint("");
				else
					captureScreenshot("", driver);
			}

			if (needLog) {
				UTAFLog.info("Step : " + vDesc + " is " + vStatus);
			}

			if (vStatus.equalsIgnoreCase("PASS")) {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.PASS, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,
							UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "PASS";
				} else {
					UTAFRead2.test.log(LogStatus.PASS, vDesc, UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc, "");
					UTAFFwVars.utafFWTCStatus = "PASS";
				}
			} else if (vStatus.equalsIgnoreCase("FAIL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,
							UTAFFwVars.utafFWSSPath);
				} else {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc, UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc, "");

				}
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("INFO")) {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.INFO, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,
							UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "INFO";
				} else {
					UTAFRead2.test.log(LogStatus.INFO, vDesc, UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc, "");
					UTAFFwVars.utafFWTCStatus = "INFO";
				}
			} else if (vStatus.equalsIgnoreCase("SKIP")) {
				UTAFFwVars.utafFWTCSkipFlag = "Y";
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,
							UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "SKIP";
				} else {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc, UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc, "");
					UTAFFwVars.utafFWTCStatus = "SKIP";
				}
				// cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("FATAL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen)
					UTAFRead2.test.log(LogStatus.FATAL, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.FATAL, vDesc, UTAFRead2.test.addScreenCapture(""));
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("ERROR")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen)
					UTAFRead2.test.log(LogStatus.ERROR, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.ERROR, vDesc, UTAFRead2.test.addScreenCapture(""));
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("WARNING")) {
				if (needScreen)
					UTAFRead2.test.log(LogStatus.WARNING, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.WARNING, vDesc, UTAFRead2.test.addScreenCapture(""));
			} else {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,
							UTAFFwVars.utafFWSSPath);
				} else {
					UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc, "");
				}
			}

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(), vDesc);
		}
	}
	
	public static void cfGlReportDesc(WebDriver driver, String vStatus, String vDesc, boolean needLog, String vData)
			throws Exception {
		UTAFLog.debug("cfGlReport");
		UTAFLog.info(vDesc);
		try {
			if (needLog) {
				UTAFLog.info("Step : " + vDesc + " is " + vStatus);
			}
			
			
			if (vStatus.equalsIgnoreCase("PASS")) {
				if (!(vData == null)) {
					UTAFRead2.test.log(LogStatus.PASS, vDesc, vData);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,vData);
					UTAFFwVars.utafFWTCStatus = "PASS";
				} else {
					UTAFRead2.test.log(LogStatus.PASS, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "PASS";
				}
			} else if (vStatus.equalsIgnoreCase("FAIL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (!(vData == null)) {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc, vData);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,vData);
				} else {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					
				}
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("INFO")) {
				if (!(vData == null)) {
					UTAFRead2.test.log(LogStatus.INFO, vDesc,vData);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,vData);
					UTAFFwVars.utafFWTCStatus = "INFO";
				} else {
					UTAFRead2.test.log(LogStatus.INFO, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "INFO";
				}
			} else if (vStatus.equalsIgnoreCase("SKIP")) {
				if (!(vData == null)) {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc,vData);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,vData);
					UTAFFwVars.utafFWTCStatus = "SKIP";
				} else {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "SKIP";
				}
			} else if (vStatus.equalsIgnoreCase("FATAL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (!(vData == null)) 
					UTAFRead2.test.log(LogStatus.FATAL, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.FATAL, vDesc);
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("ERROR")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (!(vData == null)) 
					UTAFRead2.test.log(LogStatus.ERROR, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.ERROR, vDesc);
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("WARNING")) {
				if (!(vData == null)) 
					UTAFRead2.test.log(LogStatus.WARNING, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.WARNING, vDesc);
			} else {
				if (!(vData == null)) 
					{UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc,
							vData);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,vData);
					}
				else
					{UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc);
				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");}
			}

			

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					vDesc);
		}
	}

	
	public static void cfGlReport(WebDriver driver, UTAFStatus vEnumStatus, String vDesc, boolean needLog, boolean needScreen)
			throws Exception {
		UTAFLog.debug("cfGlReport");
		String vStatus = vEnumStatus.toString();
		try {
			if (needScreen) {
				if (isAlertPresent(driver))
					captureScreenshotPrint("");
				else
					captureScreenshot("", driver);
			}
			if (needLog) {
				UTAFLog.info("Step : " + vDesc + " is " + vStatus);
		}
//			if (needScreen) {
//				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);
//			}else
//				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
			
			if (vStatus.equalsIgnoreCase("PASS")) {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.PASS, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "PASS";
				} else {
					UTAFRead2.test.log(LogStatus.PASS, vDesc,"");
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "PASS";
				}
			} else if (vStatus.equalsIgnoreCase("FAIL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);

				} else {
					UTAFRead2.test.log(LogStatus.FAIL, vDesc,"");
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
				}
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("INFO")) {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.INFO, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "INFO";
				} else {
					UTAFRead2.test.log(LogStatus.INFO, vDesc,UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "INFO";
				}
			} else if (vStatus.equalsIgnoreCase("SKIP")) {
				if (needScreen) {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc, UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);
					UTAFFwVars.utafFWTCStatus = "SKIP";
				} else {
					UTAFRead2.test.log(LogStatus.SKIP, vDesc,UTAFRead2.test.addScreenCapture(""));
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");
					UTAFFwVars.utafFWTCStatus = "SKIP";
				}
			} else if (vStatus.equalsIgnoreCase("FATAL")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen)
					UTAFRead2.test.log(LogStatus.FATAL, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.FATAL, vDesc);
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("ERROR")) {
				UTAFFwVars.utafReportFlag = "Y";
				if (needScreen)
					UTAFRead2.test.log(LogStatus.ERROR, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.ERROR, vDesc);
				cfGLThrowException(vDesc);
			} else if (vStatus.equalsIgnoreCase("WARNING")) {
				if (needScreen)
					UTAFRead2.test.log(LogStatus.WARNING, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				else
					UTAFRead2.test.log(LogStatus.WARNING, vDesc);
			} else {
				if (needScreen)
					{UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc,
							UTAFRead2.test.addScreenCapture(UTAFFwVars.utafFWSSPath));
				UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,UTAFFwVars.utafFWSSPath);}
				else
					{UTAFRead2.test.log(LogStatus.UNKNOWN, vDesc);
					UTAFInteractions.cfUTAFUpdateTestStep(UTAFFwVars.utafFWTCReference, vStatus, vDesc,"");}
			}

				

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					vDesc);
		}
	}
	
	

	/**
	 * cfGlSelElementCount is to send the soap request
	 * 
	 * @param driver
	 *            as Webdriver
	 * @param vLocator
	 *            as String
	 * @return int
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static int cfGlSelElementCount(WebDriver driver, String vLocator) throws Exception {
		UTAFLog.debug("In cfGlSelElementCount");
		int eleSize = 0;
		try {
			cfGlSelCheckDocReady(driver);
			List<WebElement> eleList = cfGlSelElementsSearch(driver, vLocator);
			eleSize = eleList.size();
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Count of  " + vLocator + " on page is " + eleSize);

		} catch (UnhandledAlertException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );

		}
		UTAFLog.debug("Out cfGlSelElementCount");
		return eleSize;
	}

	
	public static boolean isAlertPresent(WebDriver driver){
	    boolean foundAlert = false;
	    WebDriverWait wait = new WebDriverWait(driver, 0 /*timeout in seconds*/);
	    try {
	        wait.until(ExpectedConditions.alertIsPresent());
	        foundAlert = true;
	    } catch (Exception ex) {
	    	foundAlert = false;
	    }
	    return foundAlert;
	}
	/**
	 * cfGlSOAPSendRequest is to send the soap request
	 * 
	 * @param vSOAPURL
	 * @param vRequestFile
	 * @param vOutPutFile
	 * @return HttpResponse
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static HttpResponse cfGlSOAPSendRequest(String vSOAPURL, String vRequestFile, String vOutPutFile)
			throws Exception {
		UTAFLog.debug("In cfGlSOAPSendRequest");
		String result = null;
		HttpResponse response = null;
		try {
			String payLoad = cfGlReadFileAsString(vRequestFile);
			// CloseableHttpClient client =
			// setProxy("userproxy.glb.ebc.local",8080);
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(vSOAPURL);
			// request.addHeader("SOAPAction", soapAction);
			// request.addHeader("X-EXTERNAL-CONSUMER-ID", oloId);
			request.addHeader("Content-Type", "text/xml");
			// request.addHeader("Authorization", Auth);
			HttpEntity entity = new ByteArrayEntity(payLoad.trim().getBytes("UTF-8"));
			request.setEntity(entity);
			response = client.execute(request);
			// Get Response Code
			/*
			 * int responseCode = response.getStatusLine().getStatusCode(); if
			 * (responseCode == 200) {
			 * System.out.println("Post Successful for Provide Endpoint"); }
			 * else { System.out.println("Post Failed for Provide End point"); }
			 */
			result = EntityUtils.toString(response.getEntity());
			// System.out.println(result);*/
			cfGlSaveFile(vOutPutFile, result);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(), response + "");
			return response;

		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "::" + ex.getStackTrace());
		}
		UTAFLog.debug("Out cfGlSOAPSendRequest");
		return response;
	}

	/**
	 * cfGlReadFileAsString is to read the content to a file path provided
	 * 
	 * @param vFilePath
	 * @return vContent
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static String cfGlReadFileAsString(String vFilePath) throws Exception {
		UTAFLog.debug("In cfGlReadFileAsString");
		String content = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(vFilePath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if(content.equalsIgnoreCase(""))
					content = sCurrentLine;
				else
					content = content + "\n" + sCurrentLine;
			}
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"Returning content of the file " + vFilePath);
		} catch (IOException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "::" + ex.getStackTrace());
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() + "::" + ex.getStackTrace());
			}
		}
		UTAFLog.debug("Out cfGlReadFileAsString");
		return content;
	}

	/**
	 * cfGlSaveFile method is to write the content to a file path provided
	 * 
	 * @param vFilePath
	 * @param vContent
	 * @author Infosys UTAF Team
	 * @version 1.0
	 * @throws Exception
	 * @since 01-Jul-2018
	 **/
	public static void cfGlSaveFile(String vFilePath, String vContent) throws Exception {
		UTAFLog.debug("In cfGlSaveFile");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(vFilePath));
			writer.write(vContent);
			cfGLGenericPassHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					"file " + vFilePath + " is created with content \n" + vContent);
		} catch (IOException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlSaveFile",
					ex.getMessage() );
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException ex) {
				cfGLGenericExceptionHandlingThrow("cfGlSaveFile",
						ex.getMessage());
			}
		}
		UTAFLog.debug("Out cfGlSaveFile");
	}

	public static void cfGLTestMethod(String filePath, int divider) throws Exception {

		try {
			System.out.println(20 / divider);
		} catch (Exception e) {
			throw new Exception("My Message");
		} finally {

		}
	}

	public static void cfGLThrowException(String vMSG) throws Exception {
		throw new Exception(vMSG);
	}

	
	public static JSONObject cfGLGetJSON(String dbType, String vIdentifier, String tagName, String vStatus, String vTime)
			throws Exception {
		Connection con = null;
		Statement stmt = null;
		JSONObject obj = new JSONObject();
		try{
		UTAFLog.info("Request in cfFBPUSH");
		Calendar c = Calendar.getInstance();
		String todayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
		String appHost = UTAFFwVars.utafFWProps.getProperty("udm_HOSTNAME");
		String appPORT = UTAFFwVars.utafFWProps.getProperty("udm_PORT");
		String appDBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBL");
		String appUser = UTAFFwVars.utafFWProps.getProperty("udm_USER");
		String appPass = UTAFFwVars.utafFWProps.getProperty("udm_PASSWORD");
		/*String[] vParameters = vParameter.split(":");
		String[] vParameters2 = vParameters[0].split(",");
		String tagName = vParameters2[0];
		String vStatus = vParameters2[1];
		String vValues = vParameters2[2];
		String[] vParameters3 = vParameters[1].split(",");
		JSONObject obj = new JSONObject();
		for (String string : vParameters3) {
			obj.put(string, cfGlGetRunTimeVar(string));
		}
		*/
		
		//String JSONObject1 = "{ \"name\": \"Safari\", \"os\": \"Mac\",\"resolution\": { \"x\": 1920, \"y\": 1080 } }";
		Class.forName("com.mysql.jdbc.Driver");
		// con = (Connection)
		// DriverManager.getConnection("jdbc:mysql://10.119.134.220:3306/e2eregression?serverTimezone=CET&useUnicode=yes&characterEncoding=UTF-8",
		// "root", "" );
		con = (Connection) DriverManager.getConnection("jdbc:mysql://" + appHost + ":" + appPORT + "/"
				+ appDBNAME + "?autoReconnect=true&useSSL=false", appUser, appPass);
		
		stmt = (Statement) con.createStatement();
		String query = "select vData from utaf_db.localdatastorage where dataKey = 1005";
		
		ResultSet SQrs = stmt.executeQuery(query);
		ResultSetMetaData resultSetMetaData = SQrs.getMetaData();

		while (SQrs.next()) {
				for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
					obj = new JSONObject(SQrs.getString(1));
					
					UTAFRead2.runTimeVar.put(resultSetMetaData.getColumnName(i+1), SQrs.getString(i + 1));
					UTAFLog.info("Key - " + resultSetMetaData.getColumnName(i+1) + " : Value - " + SQrs.getString(i + 1));
				}
			break;
		}
		
		
			
		} catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\nn" + ex.getStackTrace());
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (Exception ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() + "\n1" + ex.getStackTrace());
			}
		}
		return obj;

	}
	
	public static void cfGlDBConnectSQL(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		if(dbType.trim().equalsIgnoreCase("TANDEM"))
		{int timeOut = 180;// Integer.parseInt(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".TIMEOUT").trim());
			try {
				timeOut = Integer.parseInt(getDBTimeOut(vAPP));
				UTAFLog.info("Setting Method timeout to " + timeOut + " sec");
			} catch (Exception ex) {
				UTAFLog.warn("Query time out Error for :" + vAPP + " due to " + ex.getMessage());
				UTAFLog.warn("Setting Method timeout to 180 sec");
				timeOut = 180;
			}
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
		   public Object call() throws Exception {
			   UTAFFwVars.prgExitFlag = false;
			   cfGlDBConnectSQL_Tandem(dbType, vAPP, vQuery, vPath);
			  //Thread.sleep(10000);
		      return true;
		   }
		};
		
		Future<Object> future = executor.submit(task);
		try {
		   Object result = future.get(timeOut, TimeUnit.SECONDS);
		  
		} catch (TimeoutException ex) {
			future.cancel(true); 
			UTAFFwVars.prgExitFlag= true;
			cfGLGenericExceptionHandlingThrow("cfGlDBConnectSQL",
					"There are no records for this query as method timed out : "+ timeOut + "\n" + cfGlGetStackTrace(ex));
		} catch (InterruptedException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlDBConnectSQL",
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} catch (ExecutionException ex) {
			cfGLGenericExceptionHandlingThrow("cfGlDBConnectSQL",
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		}catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow("cfGlDBConnectSQL",
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} 
			finally {
				try {
					if (dbType.trim().equalsIgnoreCase("TANDEM")) {
						UTAFLog.info("In Finally Furture Task");
						/*if (!(stmtTandem == null)){
							UTAFLog.info("In Finally stmtTandem before closing");
							stmtTandem.close();
							UTAFLog.info("Statement closed successfully");}
						*/
						if (!(conTandem == null)){
							UTAFLog.info("In Finally conTandem before closing");
							conTandem.close();
							UTAFLog.info("Connection closed successfully");}
					}
				} catch (Exception ex) {

				}
				try {
					if (!UTAFFwVars.prgExitFlag)
						future.cancel(true); // may or may not desire this
				} catch (Exception ex) {
				}
				try {
					executor.shutdown();
				} catch (Exception ex) {
				}
			}}
		else{
			 cfGlDBConnectSQL_test(dbType, vAPP, vQuery, vPath);
		}
	}
	public static ResultSet cfGlDBReturnSQLSet(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		if(dbType.trim().equalsIgnoreCase("TANDEM"))
		{int timeOut = Integer.parseInt(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".TIMEOUT").trim());
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
		   public Object call() throws Exception {
			   cfGlDBReturnSQLSet_Tandem(dbType, vAPP, vQuery, vPath);
			  //Thread.sleep(10000);
		      return true;
		   }
		};
		
		Future<Object> future = executor.submit(task);
		try {
		   Object result = future.get(timeOut, TimeUnit.SECONDS);
		  
		} catch (TimeoutException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} catch (InterruptedException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} catch (ExecutionException ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		}catch (Exception ex) {
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} 
			finally {
				try{
					
//					 if(dbType.trim().equalsIgnoreCase("TANDEM")){
//						 try{if(!(stmtTandem == null) )
//							   stmtTandem.close();}
//						 catch(Exception ex){
//								
//							}
//						 try{if(!(conTandem == null) )
//							   conTandem.close();}
//						 catch(Exception ex){
//								
//							}
//					 }
//					   
				}catch(Exception ex){
					
				}
				try {
					
					future.cancel(true); // may or may not desire this
				} catch (Exception ex) {
				}
				try {
					executor.shutdown();
				} catch (Exception ex) {
				}
			}}
		else{
			cfGlDBReturnSQLSet_Tandem(dbType, vAPP, vQuery, vPath);
		}
		return resultSetRet;
	}
	
	public static void cfGlDBConnectCouchBase(String vAPP,String vBucket, String vQuery, String vPath)
			throws Exception {
		
		UTAFLog.debug("In cfGlDBConnectCouchBase");
		if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		Cluster cs = null;
		String appConnURL = "";	
		String appUser = "";
		String appPass = "";
		String appTimeOut = "";
		int count = 0;
		try {
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			appTimeOut = getDBTimeOut(vAPP);
			
			//connURL="el9194.int.corp,el9195.int.corp,el9196.int.corp"
			cs = Cluster.connect(appConnURL, appUser, appPass);
			//Bucket bk = cs.bucket(vBucket);
			//QueryOptions qp= new QueryOptions();
			
			Duration vDur = Duration.ofSeconds(Integer.parseInt(appTimeOut));
			QueryResult result = cs.query(vQuery, queryOptions().timeout(vDur));
			
	        System.out.println(result.rowsAsObject());
			
			JSONObject jsonObj = (JSONObject)result.rowsAsObject();
			for (Object key : jsonObj.keySet()) {
						        // based on the key types
			String keyStr = (String) key;
			Object keyvalue = jsonObj.get(keyStr);
						        // Print key and value
						       UTAFLog.info("key: " + keyStr + " value: " + keyvalue);
						        UTAFRead2.runTimeVar.put(keyStr,keyvalue.toString());
						        // expand(keyvalue);
						        count++;
						        // for nested objects iteration if required
						    }
						
			if (count == 0) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"There are no records for this query : " + vQuery);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				
				cs.disconnect();
			} catch (Exception ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() );
			}
		}
		

	}
	
	public static String getDBTimeOut(String vAPP){
		String appTimeOut = "";
		try {
			appTimeOut = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".TIMEOUT").trim();
		} catch (Exception ex) {
			try {
				appTimeOut = UTAFFwVars.utafFWProps.getProperty("DB." + vAPP + ".TIMEOUT").trim();
			} catch (Exception e) {
				UTAFLog.warn("Errro in getting timeout for this :" + vAPP + " setting default to 90");
				appTimeOut = "90";
			}
		}
		return appTimeOut;
	}
	public static JsonObject cfGlDBConnectCouchBaseJSON(String vAPP,String vBucket, String vQuery, String vPath)
			throws Exception {
		JsonObject jsonObj = null;
		
		UTAFLog.debug("In cfGlDBConnectCouchBase");
		if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		Cluster cs = null;
		String appConnURL = "";	
		String appUser = "";
		String appPass = "";
		int count = 0;
		try {
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			
			//connURL="el9194.int.corp,el9195.int.corp,el9196.int.corp"
			cs = Cluster.connect(appConnURL, appUser, appPass);
			//Bucket bk = cs.bucket(vBucket);
			QueryResult result = cs.query(vQuery);
			//result.rowsAsObject()
	        System.out.println( result.rowsAsObject());
	       //return result.rowsAsObject();
			
			jsonObj = (JsonObject) result.rowsAsObject();
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				
				cs.disconnect();
			} catch (Exception ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() );
			}
		}
		return jsonObj;
		

	}
	
	
	public static List<JsonObject> cfGlDBConnectCouchBaseObjectOld(String vAPP,String vBucket, String vQuery, String vPath)
			throws Exception {
		List<JsonObject> jsonObj = null;
		
		UTAFLog.debug("In cfGlDBConnectCouchBase");
		if(vQuery.contains(".sql")){
			if((vPath == null || vPath.isEmpty() || vPath == "")) 
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		}
			
		Cluster cs = null;
		String appConnURL = "";	
		String appUser = "";
		String appPass = "";
		int count = 0;
		try {
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			
			//connURL="el9194.int.corp,el9195.int.corp,el9196.int.corp"
			cs = Cluster.connect(appConnURL, appUser, appPass);
			//Bucket bk = cs.bucket(vBucket);
			QueryResult result = cs.query(vQuery);
			//result.rowsAsObject()
	        System.out.println( result.rowsAsObject());
			//return result.rowsAsObject();
			jsonObj = result.rowsAsObject();
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				cs.disconnect();
			} catch (Exception ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() );
			}
		}
		return jsonObj;
		

	}
	
	public static void cfGlDBConnectCouchBaseObject()
			throws Exception {
		String env = "UAT";
		int maxcount = 10;
		String vQuery = UTAFCommonFunctions2.cfGlReadFileAsString("C:\\Data\\MBA_TC09_Bill_delivery_GB_MX.sql");    
		
		Cluster cluster = null;
		
		ClusterEnvironment clenv = ClusterEnvironment.builder() .timeoutConfig(TimeoutConfig.managementTimeout(Duration.ofSeconds(20000)).kvTimeout(Duration.ofSeconds(20000)) .queryTimeout(Duration.ofSeconds(20000))) .build(); 

		
		if(env.equals("UAT")) {
			
		cluster = Cluster.connect("el9194.int.corp,el9195.int.corp,el9196.int.corp",ClusterOptions.clusterOptions("ID995806", "ID995806_pw123").environment(clenv));
		}
		
		else if (env.equals("PROD")){
			
		cluster = Cluster.connect("el9279.int.corp,el9280.int.corp,el9281.int.corp",ClusterOptions.clusterOptions("id995805", "Juglans_nigra_PRD").environment(clenv));
			
		}
		
		//connectToCouchBase connToDB = new connectToCouchBase();
	
	
		QueryResult result = cluster.query(vQuery);
		List<JsonObject> listObject = result.rowsAsObject();
		

		//JsonObject billingAccount = listObject.get(0);

		cluster.disconnect();
		System.out.println("Query : "+vQuery);
		System.out.println(listObject.size());
		if (listObject.size()>0) {
			if (listObject.size()<maxcount) {
				maxcount = listObject.size();
			}
			for (int i=0;i<=maxcount-1;i++) {
				//OJDBC_DataBase.addDatatoLocalDB(tcname,listObject.get(i).toString(),env);
			}
		}else {
			System.out.println("NO data Retreived for Test Case : ");
		}

		

	}
	
	public static List<JsonObject> cfGlDBConnectCouchBaseObject(String vAPP,String vBucket, String vQuery, String vPath)
			throws Exception {
		List<JsonObject> jsonObj = null;
		
		UTAFLog.debug("In cfGlDBConnectCouchBases");
		if(vQuery.contains(".sql")){
			if((vPath == null || vPath.isEmpty() || vPath == "")) 
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		UTAFLog.info(vQuery);
		}
			
		Cluster cs = null;
		String appConnURL = "";	
		String appUser = "";
		String appPass = "";
		String appTimeOut = "";
		int count = 0;
		try {
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			appTimeOut =getDBTimeOut(vAPP);
			int intTimeout = Integer.parseInt(appTimeOut);
			ClusterEnvironment env2 = ClusterEnvironment.builder() .timeoutConfig(TimeoutConfig.managementTimeout(Duration.ofSeconds(intTimeout))
					.kvTimeout(Duration.ofSeconds(intTimeout)) .queryTimeout(Duration.ofSeconds(intTimeout))) .build(); 
			
			
            
			cs = Cluster.connect(appConnURL,ClusterOptions.clusterOptions(appUser, appPass).environment(env2));
			//Cluster.connect("el9194.int.corp,el9195.int.corp,el9196.int.corp",ClusterOptions.clusterOptions("ID995806", "ID995806_pw123").environment(clenv));



			
			//connURL="el9194.int.corp,el9195.int.corp,el9196.int.corp"
			//cs = Cluster.connect(appConnURL, appUser, appPass);
			//Bucket bk = cs.bucket("Core");
			
			QueryResult result = cs.query(vQuery);
			//result.rowsAsObject()
	        UTAFLog.info( result.rowsAsObject()+"");
			//return result.rowsAsObject();
			jsonObj = result.rowsAsObject();
			
			
		} catch (Exception ex) {
			UTAFLog.error("In coouchbase object " + ex.getMessage());
			ex.printStackTrace();
			cfGLGenericExceptionHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				cs.disconnect();
			} catch (Exception ex) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						ex.getMessage() );
			}
		}
		return jsonObj;
		

	}

	
	public static ResultSet cfGlReturnDBSet(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		
		UTAFLog.debug("In cfGlReturnDBSet");
		resultSetRet = null;
		boolean queryFlag= true;
		if(!vQuery.contains(".sql"))
		{
			queryFlag = false;
		}else if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		
		if(queryFlag){
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		}
		vQuery = replaceString(vQuery);
		
		/*
		if(vPath.contains(".sql") && (vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else if(vPath.contains(".sql") )
			vPath = vPath + "\\" + vQuery;
		if(vPath.contains(".sql"))
		{
			vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		}
		
		vQuery = replaceString(vQuery);*/
		String appConnURL = "";	
		String appClassName = "";
		String appUser = "";
		String appPass = "";
		int timeOut = 60;
		Connection con = null;
		Statement stmt = null;
		try {
			System.out.println(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL") + 
			UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME") +
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER") +
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS"));
			
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appClassName = UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			

			Class.forName(appClassName);
			con = (Connection) DriverManager.getConnection(appConnURL, appUser, appPass);
			stmt = (Statement) con.createStatement();
			try{
				timeOut = Integer.parseInt(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".TIMEOUT").trim());
				stmt.setQueryTimeout(timeOut);}
				catch(Exception ex){
					UTAFLog.warn("Query time out Error for :" + vAPP +  " due to " +  ex.getMessage() + "\n"  + ex.fillInStackTrace());
					//UTAFLog.warn("Setting defualt time out of 120 seconds");
					timeOut = 120;
			}
			
			resultSetRet = stmt.executeQuery(vQuery);
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				if(!(stmt == null) && !(stmt.isClosed()))
					stmt.close();
					if(!(con == null) && !(con.isClosed()))
						con.close();
			} catch (Exception ex) {
			}
		}
		return resultSetRet;

	}
	private static void cfGlDBConnectSQL_test(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		
		UTAFLog.info("In cfGlDBConnectSQL_test");
		boolean queryFlag= true;
		if(!vQuery.contains(".sql"))
		{
			queryFlag = false;
		}else if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		
		if(queryFlag){
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		}
		vQuery = replaceString(vQuery);
		/*
		if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		*/
		String appConnURL = "";	
		String appClassName = "";
		String appUser = "";
		String appPass = "";
		int timeOut = 60;
		Connection con = null;
		Statement stmt = null;
		int count = 0;
		try {
			UTAFLog.info(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL") + 
			UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME") +
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER") +
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS"));
			
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appClassName = UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME");
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			

			Class.forName(appClassName);
			con = (Connection) DriverManager.getConnection(appConnURL, appUser, appPass);
			stmt = (Statement) con.createStatement();
			try{
				timeOut = Integer.parseInt(getDBTimeOut(vAPP));
				stmt.setQueryTimeout(timeOut);}
				catch(Exception ex){
					UTAFLog.warn("Query time out Error for :" + vAPP +  " due to " +  ex.getMessage() + "\n"  + ex.fillInStackTrace());
					//UTAFLog.warn("Setting defualt time out of 120 seconds");
					timeOut = 120;
			}
			
			ResultSet SQrs = stmt.executeQuery(vQuery);
			ResultSetMetaData resultSetMetaData = SQrs.getMetaData();

			while (SQrs.next()) {
				count = count + 1;
					for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
						if(resultSetMetaData.getColumnTypeName(i+1).equalsIgnoreCase("JSON")){
							JSONObject jsonObj = (JSONObject)SQrs.getObject(i + 1);
							for (Object key : jsonObj.keySet()) {
						        // based on the key types
						        String keyStr = (String) key;
						        Object keyvalue = jsonObj.get(keyStr);
						        // Print key and value
						        System.out.println("key: " + keyStr + " value: " + keyvalue);
						        UTAFRead2.runTimeVar.put(keyStr,keyvalue.toString());
						        // expand(keyvalue);

						        // for nested objects iteration if required
						    }
						}
						UTAFRead2.runTimeVar.put(resultSetMetaData.getColumnName(i+1), SQrs.getString(i + 1));
						UTAFLog.info("Key - " + resultSetMetaData.getColumnName(i+1) + " : Value - " + SQrs.getString(i + 1));
					}
				break;
			}
			if (count == 0) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"There are no records for this query : " + vQuery);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				if(!(stmt == null) )
					stmt.close();
					if(!(con == null) )
						con.close();
			} catch (Exception ex) {
			}
		}
		

	}
	private static void cfGlDBConnectSQL_Tandem(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		
		UTAFLog.info("In cfGlDBConnectSQL_Tandem");
		boolean queryFlag= true;
		if(!vQuery.contains(".sql"))
		{
			queryFlag = false;
		}else if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		
		if(queryFlag){
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		}
		vQuery = replaceString(vQuery);
		/*
		if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		vQuery = replaceString(vQuery);
		*/
		String appConnURL = "";	
		String appClassName = "";
		String appUser = "";
		String appPass = "";
		int timeOut = 60;
		int count = 0;
		try {
			UTAFLog.info(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL") + 
			UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME") +
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER") +
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS"));
			
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appClassName = UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME");
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			

			Class.forName(appClassName);
			conTandem = (Connection) DriverManager.getConnection(appConnURL, appUser, appPass);
			UTAFLog.info("Connection Successful");
			stmtTandem = (Statement) conTandem.createStatement();
			try{
				timeOut = Integer.parseInt(getDBTimeOut(vAPP));
				stmtTandem.setQueryTimeout(timeOut);}
				catch(Exception ex){
					UTAFLog.warn("Query time out Error for :" + vAPP +  " due to " +  ex.getMessage() + "\n"  + ex.fillInStackTrace());
					//UTAFLog.warn("Setting defualt time out of 120 seconds");
					timeOut = 120;
			}
			UTAFLog.info("Triggering Query : " + vQuery);
			ResultSet SQrs = stmtTandem.executeQuery(vQuery);
			ResultSetMetaData resultSetMetaData = SQrs.getMetaData();
			UTAFLog.info("Executed Query Successful : " + vQuery);

			while (SQrs.next()) {
				count = count + 1;
					for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
						if(resultSetMetaData.getColumnTypeName(i+1).equalsIgnoreCase("JSON")){
							JSONObject jsonObj = (JSONObject)SQrs.getObject(i + 1);
							for (Object key : jsonObj.keySet()) {
						        // based on the key types
						        String keyStr = (String) key;
						        Object keyvalue = jsonObj.get(keyStr);
						        // Print key and value
						        System.out.println("key: " + keyStr + " value: " + keyvalue);
						        UTAFRead2.runTimeVar.put(keyStr,keyvalue.toString());
						        // expand(keyvalue);

						        // for nested objects iteration if required
						    }
						}
						UTAFRead2.runTimeVar.put(resultSetMetaData.getColumnName(i+1), SQrs.getString(i + 1));
						UTAFLog.info("Key - " + resultSetMetaData.getColumnName(i+1) + " : Value - " + SQrs.getString(i + 1));
					}
				break;
			}
			if (count == 0) {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"There are no records for this query : " + vQuery);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				if(!(stmtTandem == null) ){
					stmtTandem.close();
					UTAFLog.info("Statement Closed Successfully");}
			} catch (Exception ex) {
			}
			try {
				if(!(conTandem == null) ){
					conTandem.close();
					UTAFLog.info("Connection closed Successfully");}
			} catch (Exception ex) {
			}
		}
		

	}
	
	public static ResultSet cfGlDBReturnSQLSet_Tandem(String dbType, String vAPP, String vQuery, String vPath)
			throws Exception {
		
		UTAFLog.debug("In cfGlDBReturnSQLSet_Tandem");
		resultSetRet = null;
		boolean queryFlag= true;
		if(!vQuery.contains(".sql"))
		{
			queryFlag = false;
		}else if((vPath == null || vPath.isEmpty()))
		{
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			vPath = classloader.getResource(vQuery).getPath();
		}else 
			vPath = vPath + "\\" + vQuery;
		
		if(queryFlag){
		vQuery = UTAFCommonFunctions2.cfGlReadFileAsString(vPath);
		}
		vQuery = replaceString(vQuery);
		String appConnURL = "";	
		String appClassName = "";
		String appUser = "";
		String appPass = "";
		int timeOut = 60;
		try {
			System.out.println(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL") + 
			UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME") +
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER") +
			UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS"));
			
			appConnURL = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".CONNURL");
			appClassName = UTAFFwVars.utafFWProps.getProperty(dbType + ".CLASSNAME");
			// udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			appUser = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".USER");
			appPass = UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".DB." + vAPP + ".PASS");
			

			Class.forName(appClassName);
			conTandem = (Connection) DriverManager.getConnection(appConnURL, appUser, appPass);
			stmtTandem = (Statement) conTandem.createStatement();
			try{
				timeOut = Integer.parseInt(getDBTimeOut(vAPP));
				stmtTandem.setQueryTimeout(timeOut);}
				catch(Exception ex){
					UTAFLog.warn("Query time out Error for :" + vAPP +  " due to " +  ex.getMessage() + "\n"  + ex.fillInStackTrace());
					//UTAFLog.warn("Setting defualt time out of 120 seconds");
					timeOut = 120;
			}
			
			resultSetRet = stmtTandem.executeQuery(vQuery);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() + "\n" + cfGlGetStackTrace(ex));
		} finally {
			try {
				
			} catch (Exception ex) {
			}
		}
		return resultSetRet;
		

	}
	
	
	
	
	
	public static String cfGLRestCallDirect(String vRestURL, String jsonString, String requestMethod)
			throws Exception {
		HttpURLConnection conn = null;
		String output = "";
		try{
		URL url = new URL(vRestURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		//conn.setInstanceFollowRedirects(false);
		if(requestMethod.equalsIgnoreCase("POST")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("POST");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else if(requestMethod.equalsIgnoreCase("PUT")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("PUT");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else{
			conn.setRequestMethod("GET");
		}
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		UTAFLog.debug("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			if(output.substring(0, 1).contains("["))
				output = output.substring(1, output.length()-1);
			UTAFLog.debug(output);
			break;
		}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		} finally {
			conn.disconnect();
		}
		return output;

	}
	
	public static String cfGLRestCallDirectH(String vRestURL, String jsonString, String requestMethod)
			throws Exception {
		HttpURLConnection conn = null;
		String output = "";
		try{
		URL url = new URL(vRestURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("user_Id", UTAFFwVars.utafSYSProps.getProperty("user.name"));//cfGlGetFrameworkProperty("API.UTAF.USER"));
		conn.setRequestProperty("Authorization", UTAFFwVars.utafFWProps.getProperty("API.UTAF.PASS"));//UTAFRead2.runTimeVar.get("API.UTAF.PASS"));
		conn.setRequestProperty("user_profile", UTAFFwVars.utafFWProps.getProperty("API.UTAF.PROFILE"));
		try{
			conn.setConnectTimeout(Integer.parseInt(UTAFFwVars.utafAPIConnectionTimeOut.trim())*1000);
		}catch(Exception ex){
			conn.setConnectTimeout(60000);
			UTAFLog.warn("API Timeout Set : 60 Sec");
		}
		
		//conn.setInstanceFollowRedirects(false);
		if(requestMethod.equalsIgnoreCase("POST")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("POST");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else if(requestMethod.equalsIgnoreCase("PUT")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("PUT");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else{
			conn.setRequestMethod("GET");
		}
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		UTAFLog.debug("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			if(output.substring(0, 1).contains("["))
				output = output.substring(1, output.length()-1);
			UTAFLog.debug(output);
			break;
		}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		} finally {
			conn.disconnect();
		}
		return output;

	}
	
	public static String cfGLRestCallDirectInt(String vRestURL, String jsonString, String requestMethod)
			throws Exception {
		HttpURLConnection conn = null;
		String output = "";
		try{
		URL url = new URL(vRestURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		//conn.setInstanceFollowRedirects(false);
		if(requestMethod.equalsIgnoreCase("POST")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("POST");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else if(requestMethod.equalsIgnoreCase("PUT")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("PUT");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
			
		}else{
			conn.setRequestMethod("GET");
		}
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		UTAFLog.debug("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			if(output.substring(0, 1).contains("["))
				output = output.substring(1, output.length()-1);
			UTAFLog.debug(output);
			System.out.println(output);
			break;
		}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			UTAFLog.warn("cfGLRestCallDirectInt : "+ ex.getMessage() );
			cfGLThrowException(  " error in cfGLRestCallDirectInt : "+ex.getMessage() );
			
		} finally {
			conn.disconnect();
		}
		return output;

	}
	public static String cfGLRestCallDirectIntAuth(String vRestURL, String jsonString, String requestMethod)
			throws Exception {
		HttpURLConnection conn = null;
		String output = "";
		try{
		URL url = new URL(vRestURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("user_Id", UTAFFwVars.utafSYSProps.getProperty("user.name"));//cfGlGetFrameworkProperty("API.UTAF.USER"));
		conn.setRequestProperty("Authorization",  UTAFFwVars.utafFWProps.getProperty("API.UTAF.PASS"));//UTAFRead2.runTimeVar.get("API.UTAF.PASS"));
		conn.setRequestProperty("user_profile",  UTAFFwVars.utafFWProps.getProperty("API.UTAF.PROFILE"));
		try{
			conn.setConnectTimeout(Integer.parseInt(UTAFFwVars.utafAPIConnectionTimeOut.trim())*1000);
		}catch(Exception ex){
			conn.setConnectTimeout(60000);
			UTAFLog.warn("API Timeout Set : 60 Sec");
		}
		
		
		//conn.setInstanceFollowRedirects(false);
		if(requestMethod.equalsIgnoreCase("POST")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("POST");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
		}else if(requestMethod.equalsIgnoreCase("PUT")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestMethod("PUT");
			/*OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();*/
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			bw.write(jsonString);
			bw.flush();
			bw.close();
			
		}else{
			conn.setRequestMethod("GET");
		}
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		UTAFLog.debug("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			if(output.substring(0, 1).contains("["))
				output = output.substring(1, output.length()-1);
			UTAFLog.debug(output);
			System.out.println(output);
			break;
		}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			UTAFLog.warn("cfGLRestCallDirectInt : "+ ex.getMessage() );
			cfGLThrowException(  " error in cfGLRestCallDirectInt : "+ex.getMessage() );
			
		} finally {
			conn.disconnect();
		}
		return output;

	}
	
	public static String cfGLRestCallDirectHeader(String vRestURL, String jsonString, String requestMethod, HashMap<String, String> restHeaders)
			throws Exception {
		HttpURLConnection conn = null;
		String output = "";
		try{
		URL url = new URL(vRestURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		if(!restHeaders.isEmpty()){
			Set<String> vKeySet = restHeaders.keySet();
			for (String string : vKeySet) {
				conn.setRequestProperty(string, restHeaders.get(string));
			}
		}
		//conn.setInstanceFollowRedirects(false);
		if(requestMethod.equalsIgnoreCase("POST")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();
		}else if(requestMethod.equalsIgnoreCase("PUT")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("PUT");
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();
			os.close();
		}else{
			conn.setRequestMethod("GET");
		}
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		UTAFLog.debug("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			if(output.substring(0, 1).contains("["))
				output = output.substring(1, output.length()-1);
			UTAFLog.debug(output);
			break;
		}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
					ex.getMessage() );
		} finally {
			conn.disconnect();
		}
		return output;

	}
/* This method is developed to get stack track of an element using exception
 * 
 */
public static String cfGlGetStackTrace(Exception ex){
	//ExceptionUtils.get
		
		return ExceptionUtils.getFullStackTrace(ex);

	}


	
	
	
		
		
public static void cfGlSerSendRequest(String method, String username, String password, String url, String testDataLocal, String fileName, String header) throws Exception{
    UTAFLog.info("In cfGlSerSendRequest");
/*            if(username!=null){                              
          username = cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV +"."+username.replaceAll("\\s+", ""));
          password = cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV +"."+password.replaceAll("\\s+", ""));
    } */
// url = cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV +"."+url.replaceAll("\\s+", ""));
    url = url.replaceAll("\\s+", "");
    Map<String, String> headerMap = restUtils.inputMap(header);
    String requestBody="";
    String reqDetails="";
    try{
          if(testDataLocal!=null){
                 stepData=restUtils.stringToMap(testDataLocal);
                 testData.putAll(stepData); 
                 UTAFRead2.runTimeVar.putAll(testData);
          }
          if(url.contains("Req")){
              url=restUtils.generatePayload(testData, runTimeData, url); 
          }
          UTAFLog.info("===============================================URL===============================================");
          UTAFLog.info(url);
          if(fileName!=null){
                 if(fileName.contains(".")){
                        requestpPayloadType=fileName.split("[.]")[1];
                        requestBody=restUtils.generatePayload(testData, runTimeData, fileName);
                 UTAFLog.info("===============================================Request===========================================");
                        UTAFLog.info(requestBody);
                        reqDetails=captureRequestAndResponse(requestBody,requestpPayloadType);
                 }
                 else{
                        requestpPayloadType=fileName.trim();
                 }
          }
          if(headerMap.isEmpty()){
                 headerMap=restUtils.header(requestpPayloadType);
          }
          else{
                 headerMap.putAll(restUtils.header(requestpPayloadType));
          }
          RestAssured.urlEncodingEnabled=false;
          if(username!=null){
                 restUtils.doAuthentication(username, password);
          }
          if(method.equalsIgnoreCase("GET")){
                 if(header!=null){
                        response=RestAssured.given().headers(headerMap).get(url);
                 }
                 else{
                        response=RestAssured.given().get(url);
                 }
          }
          else if(method.equalsIgnoreCase("POST")){
                 response = RestAssured.given().headers(headerMap).body(requestBody).post(url);
          }
          else if(method.equalsIgnoreCase("PUT")){
                 if(requestBody!=""){
                        response=RestAssured.given().headers(headerMap).body(requestBody).put(url);
                 }
                 else{
                        response = RestAssured.given().put(url);
                 }
          }
          if(fileName!=null){
                 if(requestpPayloadType.equalsIgnoreCase("xml")){
                        xml=response.body().asString();
                        UTAFRead2.runTimeVar.put("utafRestResponse", xml);
//                 UTAFLog.info("===============================================Response===========================================");
//                        UTAFLog.info(xml);
                 }
                 else{
                        json=response.body().asString();
                        UTAFRead2.runTimeVar.put("utafRestResponse", json);
//                 UTAFLog.info("===============================================Response===========================================");
//                        UTAFLog.info(json);
                 }
                 if (response.body().asString().length()!=0) {
                        UTAFLog.info("Request posted successfully");
                        UTAFLog.info("===============================================Response===========================================");
                        String resp = response.prettyPrint();
                        if(requestpPayloadType.equalsIgnoreCase("xml")) {
                        resp=restUtils.xmlDocumentISO(response.body().asString());
                        }
                        String responseBody=captureRequestAndResponse(resp,requestpPayloadType);
                        String vData = restUtils.vData(url,testDataLocal, "Request="+reqDetails,"Header="+header,"Response="+responseBody);
                        cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
                 } else {
                        cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, "Received null response");
                 }
          }
          else{
                 UTAFLog.info("Request posted successfully");
                 cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, "No Response body");
          }
    
    }
    catch(Exception ex){
         cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        ex.getMessage());
    }
          
    UTAFLog.info("Out cfGlSerSendRequest");
    
}

		
		public static void cfGlUpdateXrayStatus(String status ) throws Exception{
            try{
			status = status.toUpperCase();
			if (status.equalsIgnoreCase("pass") || status.contains("PASS")) {
				status = "PASS";
			} else if (status.equalsIgnoreCase("executing")) {
				status = "EXECUTING";
			} else if (status.equalsIgnoreCase("SKIPPED") || status.contains("SKIP")) {
				status = "ABORTED";
			} else {
				status = "FAIL";
			}
            String getUrlXray = cfGlGetFrameworkProperty("JIRA.GETURL");
            //"https://jira-n2-uat.int.corp:8443/rest/raven/1.0/api/testrun?testExecIssueKey="+testExecIssueKey+"&testIssueKey="+testIssueKey;
            String putUrlXray = cfGlGetFrameworkProperty("JIRA.PUTURL");
            //"https://jira-n2-uat.int.corp:8443/rest/raven/2.0/api/testrun/$id/status?status=$status";
            getUrlXray = getUrlXray.replaceAll("varJIRATestExecIssueKey",UTAFFwVars.utafFWTSJIRAID); // cfGlGetRunTimeVar("varTSJIRAID"
            getUrlXray = getUrlXray.replaceAll("varJIRATestIssueKey",UTAFFwVars.utafFWTCJIRAID);//cfGlGetRunTimeVar("varTCJIRAID")
            System.out.println(getUrlXray);
            
            try{
                   PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme(); 
                   authScheme.setUserName(cfGlGetFrameworkProperty("JIRA.USER")); //"naveen.saxena.ext@proximus.com"
                   authScheme.setPassword(cfGlGetFrameworkProperty("JIRA.PASS"));//cfGlGetFrameworkProperty("JIRA.PASS")); //
                   RestAssured.authentication = authScheme; 
                   Response getResponse = RestAssured.given().get(getUrlXray);
                   System.out.println("Get Request Response = "+getResponse.asString());
                   
                   if(getResponse.statusCode()==200&&!(getResponse.asString().isEmpty())){
                	   //String requestBody = "{\"comment\":\"This is my comment\"}";
                         String id = getResponse.path("id").toString();
                         putUrlXray=putUrlXray.replace("varJIRAID", id).replace("varJIRAStatus", status);
                         System.out.println(putUrlXray);
                         Response putResponse = RestAssured.given().put(putUrlXray);
                         int putStatusCode = putResponse.statusCode();
                         UTAFLog.info("Put Request Status Code = "+putStatusCode);
                         if(putStatusCode!=204){
                                String exception = "Exception occured in Updating Xray status";
                                throw new RuntimeException(exception);
                         }
                         else{
                        	 UTAFLog.info("Updated Xray status as "+status+" successfully");
                         }
                   }
                   else{
                         throw new RuntimeException("Exception occured in getting ID from Xray");
                   }
            }
            catch(Exception ex){
            	 UTAFLog.warn("cfGlUpdateXrayStatusN"+"\n"+ ex.getMessage()) ;
            }}catch(Exception ex){
            	UTAFLog.warn("cfGlUpdateXrayStatus x" +"\n"+ ex.getMessage()) ;
            }
            
     }

		public  void cfGlUpdateXrayStatus(String testExecIssueKey,String testIssueKey,String status ) throws Exception{
            try{
            if(status.equalsIgnoreCase("pass")){
                   status="PASS";
            }
            else  if(status.equalsIgnoreCase("executing")){
                   status="EXECUTING";
            }else {
            	status="FAIL";
            }
            String getUrlXray = cfGlGetFrameworkProperty("JIRA.GETURL");
            //"https://jira-n2-uat.int.corp:8443/rest/raven/1.0/api/testrun?testExecIssueKey="+testExecIssueKey+"&testIssueKey="+testIssueKey;
            String putUrlXray = cfGlGetFrameworkProperty("JIRA.PUTURL");
            //"https://jira-n2-uat.int.corp:8443/rest/raven/2.0/api/testrun/$id/status?status=$status";
            getUrlXray = getUrlXray.replaceAll("varJIRATestExecIssueKey", testExecIssueKey);
            getUrlXray = getUrlXray.replaceAll("varJIRATestIssueKey", testIssueKey);
            System.out.println(getUrlXray);
            
            try{
                   PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme(); 
                   authScheme.setUserName("naveen.saxena.ext@proximus.com");//cfGlGetFrameworkProperty("JIRA.USER")); //"naveen.saxena.ext@proximus.com"
                   authScheme.setPassword("");//cfGlGetFrameworkProperty("JIRA.PASS")); //
                   RestAssured.authentication = authScheme; 
                   Response getResponse = RestAssured.given().get(getUrlXray);
                   System.out.println("Get Request Response = "+getResponse.asString());
                   
                   if(getResponse.statusCode()==200&&!(getResponse.asString().isEmpty())){
                	   String requestBody = "{\"comment\":\"This is my comment\"}";
                         String id = getResponse.path("id").toString();
                         putUrlXray=putUrlXray.replace("varJIRAID", id).replace("varJIRAStatus", status);
                         System.out.println(putUrlXray);
                         Response putResponse = RestAssured.given().put(putUrlXray);
                         int putStatusCode = putResponse.statusCode();
                         UTAFLog.info("Put Request Status Code = "+putStatusCode);
                         if(putStatusCode!=204){
                                String exception = "Exception occured in Updating Xray status";
                                throw new RuntimeException(exception);
                         }
                         else{
                        	 UTAFLog.info("Updated Xray status as "+status+" successfully");
                         }
                   }
                   else{
                         throw new RuntimeException("Exception occured in getting ID from Xray");
                   }
            }
            catch(Exception ex){
            	 UTAFLog.info(Thread.currentThread().getStackTrace()[1].getMethodName() +"\n"+ ex.getMessage()) ;
            }}catch(Exception ex){
            	UTAFLog.warn("cfGlUpdateXrayStatus " +"\n"+ ex.getMessage()) ;
            }
            
     }


		

		
		public static void cfGlSerPreCondition(String expected) throws Exception{
			UTAFLog.debug("In cfPreCondition");
			response = null;
			xml="";
			requestpPayloadType="";
			json="";
			testData.clear();
			runTimeData.clear();
			if(response ==null&&xml.equals("")&&requestpPayloadType.equals("")&&json.equals("")
					&&testData.size()==0&&runTimeData.size()==0){
				UTAFFwVars.utafFWTCStatus = "PASS";
				UTAFLog.info("Preconditions satisfied");
			} else {
				cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
						"Preconditions failed. Please check manually");
			}
			UTAFLog.debug("Out cfPreCondition");
		}
		
		public static void cfGlSerValidateStatusCode(String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateStatusCode");
			String actual = String.valueOf(response.statusCode());
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,expected);
			UTAFLog.info("Out cfGlSerValidateStatusCode");
		}
		
		public static void cfGlSerValidateStatusLine(String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateStatusLine");
			String actual = response.getStatusLine();
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,expected);
			UTAFLog.info("Out cfGlSerValidateStatusLine");
		}
		
		public static void cfGlSerValidateContentType(String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateContentType");
			String actual = response.contentType();
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,expected);
			UTAFLog.info("Out cfGlSerValidateContentType");
		}
		
		public static void cfGlSerValidateResponseHeaders(String header, String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateResponseHeaders");
			String headers[]=header.split(",");
			String expVals[]=expected.trim().split(",");
			String actualVals[]=new String[headers.length];
			for(int i=0;i<headers.length;i++){
				actualVals[i]=response.getHeader(headers[i]);
			}
			
			String actual=restUtils.actualValues(actualVals);
			String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
			UTAFLog.info("Out cfGlSerValidateResponseHeaders");
		}
		
		public static void cfGlSerValidateTagCount(String tagName, String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateTagCount");
			String tagNames[]=tagName.split(",");
			String expVals[]=expected.split(",");
			String actualVals[]=new String[tagNames.length];
			for(int i=0;i<tagNames.length;i++){
				actualVals[i]=String.valueOf(restUtils.getTagCount(tagNames[i].replace("\\s+", ""),requestpPayloadType));
			}
			String actual=restUtils.actualValues(actualVals);
			String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
			UTAFLog.info("Out cfGlSerValidateTagCount");
		}
		
		public static void cfGlSerValidateTagValueOld(String path, String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateTagValue");
			String paths[]=path.split(",");
			String expVals[]=expected.trim().split(",");
			String actualVals[]=new String[paths.length];
			for(int i=0;i<paths.length;i++){
				if(paths[i].trim().startsWith("$")){
					actualVals[i]=runTimeData.get(paths[i].trim());
				}
				else if(paths[i].trim().contains("//*")){
					actualVals[i]=restUtils.getTagValue(paths[i].trim(), requestpPayloadType);
				}
				else{
					actualVals[i]=paths[i].trim();
				}
			}
			String actual=restUtils.actualValues(actualVals);
			String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
			UTAFLog.info("Out cfGlSerValidateTagValue");
		}
		
		public static void cfGlSerValidateTagValue(String path, String expected) throws Exception{
			UTAFLog.info("In cfGlSerValidateTagValue");
			String paths[]=path.split(",");
			String expVals[]=expected.trim().split(",");
			String actualVals[]=new String[paths.length];
			for(int i=0;i<paths.length;i++){
				if(paths[i].trim().contains("Req")||paths[i].trim().contains("Rsp")){
					actualVals[i]=runTimeData.get(paths[i].trim());
					if(actualVals[i]==null){
						String xpath = cfGlGetElementProperty(paths[i]);
						if(xpath.trim().contains("Req_")){
							for(String prop : restUtils.patternMatch("Req_", xpath)){
								xpath = xpath.replace("${"+prop+"}", testData.get(prop));
							}
						}
						if(xpath.trim().contains("Rsp_")){
							for(String prop : restUtils.patternMatch("Rsp_", xpath)){
								xpath = xpath.replace("${"+prop+"}", runTimeData.get(prop));
							}
						}
						actualVals[i]=restUtils.getTagValue(xpath.trim(), requestpPayloadType);
						runTimeData.put(paths[i].trim(), actualVals[i]);
						UTAFRead2.runTimeVar.putAll(runTimeData);
					}
				}
				else{
					actualVals[i]=paths[i].trim();
				}
			}
			String actual=restUtils.actualValues(actualVals);
			String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
			restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);	
			UTAFLog.info("Out cfGlSerValidateTagValue");
		}
		
		public static void cfGlSerValidateValueInResponse(String expected) throws Exception{
            UTAFLog.info("In cfGlSerValidateValueInResponse");
            String expVals[]=expected.trim().split(",");
            String actualVals[]=new String[expVals.length];
            for(int i=0;i<actualVals.length;i++){
                  String expVal=restUtils.expectedValues(expVals[i],requestpPayloadType);
                  if(response.body().asString().contains(expVal)){
                         actualVals[i]=expVal;
                  }
                  else{
                         actualVals[i]="Not Available";
                  }
            }
            String actual=restUtils.actualValues(actualVals);
            String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
            restUtils.assertion("cfGlSerValidateValueInResponse",actual,finalExp);       
            UTAFLog.info("Out cfGlSerValidateValueInResponse");
     }

		
		public static void cfGlSerValidateDB(String query,String colName,String variable,String expected) throws Exception{
            UTAFLog.info("In cfGlSerValidateDB");
            UTAFRead2.runTimeVar.put("TSIP_1",query);
            query = cfGlGetElementProperty(query.trim());
//         String variables[]=variable.split(",");
            String colNames[]=colName.split(",");
            String expVals[]=expected.trim().split(",");
            String actualVals[]=new String[expVals.length];
            for(int i=0;i<expVals.length;i++){
                   actualVals[i]=restUtils.expectedQueryValue(query,colNames[i].trim(),variable.trim());
            }
            String actual=restUtils.actualValues(actualVals);
            String finalExp=restUtils.finalExpectedValues(expVals,requestpPayloadType);
       restUtils.assertion(Thread.currentThread().getStackTrace()[1].getMethodName(),actual,finalExp);       
            UTAFLog.info("Out cfGlSerValidateDB");
     }


		
		public static void cfGlSerStoreTagValues(String input04) throws Exception{
            UTAFLog.info("In cfGlSerStoreTagValues");
            
            String xpath ="";
            String key ="";
            int count = runTimeData.size();
            String input[] = input04.split(",");
            String color = "<font color=colors><b>val</b></font>";
            
            try{
                  UTAFLog.info("=================================Response data======================================");
                  for(int i=0;i<input.length;i++){
                         key = input[i].trim();
                         xpath = cfGlGetElementProperty(input[i].trim()).trim();
                         String value=restUtils.getTagValue(xpath,requestpPayloadType);
                         runTimeData.put(key, value);
                         UTAFLog.info(key+" = "+value);
                  }
            }
            catch(Exception e){
                  String vData = "Error occured while storing the values with property "+color.replace("colors", "\"red\"").replace("val", key+"="+xpath);
                  cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
            }
            UTAFRead2.runTimeVar.putAll(runTimeData);
            UTAFLog.info("Stored tag values successfully");
            String vData = restUtils.vData(input04);
            cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
     
            UTAFLog.info("Out StoreTagValues");
     }

				
		public static String captureRequestAndResponse(String vData) {
            Calendar c = Calendar.getInstance();
            String todayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
            double vRandom = Math.random();
            String vRandomS= vRandom +"";
            vRandomS= vRandomS.substring(2,vRandomS.length());
            todayDate = todayDate + vRandomS;
            if(UTAFFwVars.utafFWProps.containsKey("SCREENSHOT.PATH")){
                  String tempPath = UTAFFwVars.utafFWProps.getProperty("SCREENSHOT.PATH");
                  if(!(tempPath == null)){
                         UTAFFwVars.utafFWSSPath = tempPath + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + ".txt";
                  }else 
                         UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath + "Reports\\" + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + ".txt";
            }else{
                  UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath +"Reports\\" + UTAFFwVars.utafFWTSEID +"\\"  +  todayDate + ".txt";
            }
            
            
            if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
                  UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
                                + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + ".txt";
            }
            String filePath = UTAFFwVars.utafFWSSPath;
            String[] pathArray = filePath.split("\\\\");
            try {
                  FileWriter file = new FileWriter(filePath);
                  file.write(vData);
                  file.flush();
       file.close();
                  UTAFFwVars.utafFWSSPath = "./ScreenShots/" + todayDate + ".jpg";
            } catch (IOException | NullPointerException ex) {
                  UTAFLog.error ("Error in writing file" + ex.getMessage() );
            } catch (Exception ex) {
                  UTAFLog.error ("Error in writing file" + ex.getMessage() );
            }
            if(UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")){
                 // filePath="vServerPath"+UTAFFwVars.utafFWTSEID+"/"+pathArray[pathArray.length-1];
            	filePath= "./"+pathArray[pathArray.length-1];
            }
            return filePath;
     }
		public static String captureRequestAndResponse(String vData, String fileType) {
            Calendar c = Calendar.getInstance();
            String todayDate = new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
            double vRandom = Math.random();
            String vRandomS= vRandom +"";
            vRandomS= vRandomS.substring(2,vRandomS.length());
            todayDate = todayDate + vRandomS;
            if(UTAFFwVars.utafFWProps.containsKey("SCREENSHOT.PATH")){
                  String tempPath = UTAFFwVars.utafFWProps.getProperty("SCREENSHOT.PATH");
                  if(!(tempPath == null)){
                         UTAFFwVars.utafFWSSPath = tempPath + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + "."+fileType;
                  }else 
                         UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath + "Reports\\" + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + "."+fileType;
            }else{
                  UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWFolderPath +"Reports\\" + UTAFFwVars.utafFWTSEID +"\\"  +  todayDate + "."+fileType;
            }
            
            
            if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
                  UTAFFwVars.utafFWSSPath = UTAFFwVars.utafFWProps.getProperty("REPORT.SHARED.PATH")
                                + UTAFFwVars.utafFWTSEID +"\\"  + todayDate + "."+fileType;
            }
            String filePath = UTAFFwVars.utafFWSSPath;
            String[] pathArray = filePath.split("\\\\");
            
            try {
                  FileWriter file = new FileWriter(filePath);
                  file.write(vData);
                  file.flush();
       file.close();
                  UTAFFwVars.utafFWSSPath = "./ScreenShots/" + todayDate + ".jpg";
            } catch (IOException | NullPointerException ex) {
                  UTAFLog.error ("Error in writing file" + ex.getMessage() );
//                cfGLGenericExceptionHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
//                              ex.getMessage() );
            } catch (Exception ex) {
                  UTAFLog.error ("Error in writing file" + ex.getMessage() );
//                cfGLGenericExceptionHandling(Thread.currentThread().getStackTrace()[1].getMethodName(),
//                              ex.getMessage() );
            }
            if(UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")){
       filePath="vServerPath"+UTAFFwVars.utafFWTSEID+"/"+pathArray[pathArray.length-1];
            }
            filePath="./"+pathArray[pathArray.length-1];
            return filePath;
     }


	
	public static String cfGLScubeFeed(String appname, ScubeStatus overAllStatus, String remark, ScubeEnv env,
			Map<String, String> scriptStatusSet) throws Exception {
		/*//"OK", "NOK", "OK BUT"
		
 <service operation>: Updatedmcservice
< appl. name >: URL to send the callback data
< Remarks Message >: All OK
< status code>: [OK], [NOK], [OK BUT]
< Environment >: [PROD], [ACC], [INT], [PSU], [TRN]
		 */
		String mvUser = cfGlGetFrameworkProperty("SCUBE.USER");//UTAFFwVars.utafFWENV + 
		String mvPass = cfGlGetFrameworkProperty("SCUBE.PASS");//UTAFFwVars.utafFWENV + 
		String mvURL = cfGlGetFrameworkProperty("SCUBE.URL");//UTAFFwVars.utafFWENV + 
		String basicAuth= "Basic " + Base64.getEncoder().encodeToString((mvUser + ":" + mvPass).getBytes());

		String requestStatus = "fail";
		String vStatus = overAllStatus.toString();
		if (vStatus.equalsIgnoreCase("OKBUT"))
			vStatus="OK BUT";


		String jsonString = "{\"applist\":[{\"appname\":\"" + appname + "\",\"status\":\"" + vStatus
				+ "\",\"remarks\":\"" + remark + "\",\"env\":\"" + env.toString() + "\",\"components\":{";

		int i = 1;
		for (Entry<String, String> ScriptSet : scriptStatusSet.entrySet()) {

			if (i == scriptStatusSet.size()) {

				System.out.println(ScriptSet.getKey() + "  : " + ScriptSet.getValue());
				jsonString += "\"" + ScriptSet.getKey() + "\" : \"" + ScriptSet.getValue() + "\"          ";

			} else {

				System.out.println(ScriptSet.getKey() + "  :  " + ScriptSet.getValue());
				jsonString += "\"" + ScriptSet.getKey() + "\" : \"" + ScriptSet.getValue() + "\",         ";
				i++;
			}

		}

		jsonString += "}}]}";
		UTAFLog.info(jsonString);

		try {

			// data type response
			Response response = RestAssured.given().accept("application/json").header("Authorization", basicAuth)
					.header("Content-Type", "application/json;charset=UTF-8").body(jsonString).put(mvURL);

			System.out.println(response.asString());

			if (response.asString().contains("200")) {

				requestStatus = "success";

			} else {
				requestStatus = "fail";
			}

		} catch (Exception ex) {
			UTAFLog.warn(ex.getMessage());
			return requestStatus;
		}
		return requestStatus;

	}
	
	public static void cfRefreshHealthCheck(String... inputParams) throws Exception {
		if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB")) {
			UTAFInteractions.cfUTAFVDIHealthCheck(UTAFFwVars.utafFWMachineName);
		}
	}
	
	public static void cfGlSendSonicJmsMessage(String vApp,String fileName, String corrId, String testDataLocal, String queName,
			String lookUp, String brokerUrls) throws Exception {
		UTAFJMSUtil.cfGlSendJmsMessage( vApp, fileName,  corrId,  testDataLocal,  queName,
				 lookUp,  brokerUrls);
		
	}
	public static void cfGlBrowseSonicJmsMessage(String vApp,String queueName, String corrId, String lookUp)
			throws Exception {
		UTAFJMSUtil.cfGlBrowseJmsMessage( vApp, queueName,  corrId,  lookUp);
		
	}
	/*
	public static void cfGlSendJmsMessage(String fileName, String corrId, String testDataLocal, String queName,
			String lookUp, String brokerUrls) throws Exception {

		ArrayList<String> bUrls = new ArrayList<String>();
		if (testDataLocal != null) {
			stepData = restUtils.stringToMap(testDataLocal);
			testData.putAll(stepData);
			UTAFRead2.runTimeVar.putAll(testData);
		}
		if (brokerUrls != null) {
			String urls[] = brokerUrls.split(",");
			for (String bUrl : urls) {
				bUrls.add(bUrl);
			}
		}
		String xmlValue = restUtils.generatePayload(testData, runTimeData, fileName);
		UTAFLog.info(
				"===============================================Request===============================================");
		UTAFLog.info(xmlValue);
		boolean flag = true;
		progress.message.jclient.Connection uConnection = null;
		String brokerUrl = "";
		Queue queue = null;
		while (flag) {
			Properties env = new Properties();
			env.put(Context.SECURITY_PRINCIPAL,
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"));
			env.put(Context.SECURITY_CREDENTIALS,
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
			env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.CONURL"));
			env.put("com.sonicsw.jndi.mfcontext.domain",
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.DOMAIN"));
			InitialContext uJNDI = new InitialContext(env);
			UTAFLog.info("Context created successfully!!!");

			// lookup queue using queue name
			queue = (Queue) uJNDI.lookup(queName);
			UTAFLog.info("Queue lookup successful!!!");

			// create connection factory by passing the connection factory name
			ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(lookUp);
			UTAFLog.info("connection factory created!!!");

			// create connection by passing the user name and password
			uConnection = (progress.message.jclient.Connection) conFactory.createConnection(
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"),
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
			UTAFLog.info("Connection created successfully!!");
			// Start the connection
			uConnection.start();
			brokerUrl = uConnection.getBrokerURL();
			UTAFLog.info("Borker Url = " + brokerUrl);
			if (bUrls.size() > 0) {
				for (String bkrUrl : bUrls) {
					UTAFLog.info("Checking if the user given broker url is same as system broker url");
					if (bkrUrl.equals(brokerUrl)) {
						flag = false;
					}
				}
				if (flag) {
					uConnection.close();
				}
			} else {
				flag = false;
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

		UTAFLog.info("AcknowledgeMode = " + String.valueOf(session.getAcknowledgeMode()));

		String vData = restUtils.vData(brokerUrl, testDataLocal,
				"Request=" + captureRequestAndResponse(xmlValue, "xml"), "", "");
		cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		UTAFLog.info("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);
	}

	public static void cfGlBrowseJmsMessage(String queueName, String corrId, String lookUp)
			throws Exception {

		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"));
		env.put(Context.SECURITY_CREDENTIALS, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.CONURL"));
		env.put("com.sonicsw.jndi.mfcontext.domain",
				UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.DOMAIN"));
		InitialContext uJNDI = new InitialContext(env);
		UTAFLog.info("Context created successfully!!!");

		// create connection factory by passing the connection factory name
		ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(lookUp);
		UTAFLog.info("connection factory created!!!");

		// create connection by passing the user name and password
		progress.message.jclient.Connection uConnection = (progress.message.jclient.Connection) conFactory
				.createConnection(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.USER"),
						UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + ".SONIC.PASS"));
		
		
		UTAFLog.info("Connection created successfully!!");
		String brokerUrl = uConnection.getBrokerURL();
		// created session
		javax.jms.Session session = (javax.jms.Session) uConnection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

		// Start the connection
		uConnection.start();
		Queue queue1 = (Queue) session.createQueue(queueName);
		UTAFLog.info("Browse through the elements in queue");
		
		
		
		QueueBrowser browser = (QueueBrowser) session.createBrowser(queue1);
		@SuppressWarnings("unchecked")
		Enumeration<Message> e = browser.getEnumeration();
		Thread.sleep(15000);
		while (e.hasMoreElements()) {
			TextMessage message = (TextMessage) e.nextElement();
			if (message.getJMSCorrelationID().equalsIgnoreCase(corrId)) {
				xml = restUtils.xmlDocument(message.getText());
				requestpPayloadType = "xml";
				break;
			}
		}
		UTAFLog.info(
				"===============================================Response===============================================");
		UTAFLog.info(xml);
		if (xml.length() == 0) {
			UTAFLog.info("No response found for given correlationId in Queue Browser");
			cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false,
					"No response found for given correlationId in Queue Browser");
		}
		String vData = restUtils.vData(brokerUrl, "", "", "", "Response=" + captureRequestAndResponse(xml, "xml"));
		cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		UTAFLog.info("Done");
		browser.close();
		session.close();
		uConnection.close();
		// System.gc();
		UTAFLog.info("All Connections closed");
		// System.exit(0);
		Thread.sleep(3000);
	}
*/
	

}
