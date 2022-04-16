package com.infosys.UTAF;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigValues {

	public static String strConfigFilePath = "C:\\Data\\Automation\\UTAF_Automation\\utaf_config.properties";

	
	public static String getConfigValue(String strProName) {
		String strValue = "";
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(strConfigFilePath);
			prop.load(input);

			strValue = prop.getProperty(strProName);
			// System.out.println(strURL);

			// Frame.CreateHTMLReport(strEnvironment);
			// Frame.CreateHTMLReport(strEnvironment);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return strValue;

	}

	public String getConfigValueonly(String strProName) {
		String strValue = "";
		String[] strOption;

		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(strConfigFilePath);
			prop.load(input);

			strValue = prop.getProperty(strProName);
			strOption = strValue.split("\\|");
			strValue = strOption[1];
			// System.out.println(strURL);

			// Frame.CreateHTMLReport(strEnvironment);
			// Frame.CreateHTMLReport(strEnvironment);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return strValue;

	}

}

