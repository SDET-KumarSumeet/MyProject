package com.infosys.UTAF;

import java.util.Properties;
import java.util.Set;

public class PropertyMerge {

	public static void main(String[] args) throws Exception {
		UTAFFwVars.utafFWProps = UTAFCommonFunctions2.cfGlReadPropFile(UTAFFwVars.utafFWProps,
				"C:\\Data\\Automation\\UTAF_Automation\\UTAF_DEMO2.properties");
	//	UTAFCommonFunctions2.setPropFile("C:\\Data\\Automation\\UTAF_Automation\\Base.properties");
		Properties utafFWPropss = new Properties();
		
		utafFWPropss = UTAFCommonFunctions2.cfGlReadPropFile(utafFWPropss,
				"C:\\Data\\Automation\\UTAF_Automation\\Base.properties");
		
		
		UTAFFwVars.utafFWProps.putAll(utafFWPropss);

		Set<String> keys = UTAFFwVars.utafFWProps.stringPropertyNames();
		for (String key : keys) {
			if (key.equalsIgnoreCase("UTAF.RestAPI.getTestCaseForExecution")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.updateTestcaseParam")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.updateTestSuiteExecution")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.checkFailedTestcase")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.getShadowTestCase")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.updateVDI")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.vdiHealthCheck")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
			if (key.equalsIgnoreCase("UTAF.RestAPI.updateVDIPool")) {
				System.out.println("From Base Properties : " + UTAFFwVars.utafFWProps.getProperty(key));
			}
		}

	}

}
