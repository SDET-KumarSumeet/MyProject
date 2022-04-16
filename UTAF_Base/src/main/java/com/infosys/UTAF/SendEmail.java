package com.infosys.UTAF;


public class SendEmail {

	static String sSubject = "UTAF Test Report | Env - actENV | actTSName | actTSStatus | Total - actTtotal : Pass - actPass : Fail -actFail ";
	public static void sendReportByMail(String strReportFileName, String strTestCaseName)  {
		try {
			if (UTAFFwVars.utafFWExeFrom.equalsIgnoreCase("DB") && !UTAFFwVars.utafFWTSEID.equalsIgnoreCase("EMPTY") && UTAFFwVars.utafFWDBCase) {
			//UTAFInteractions.cfUTAFUpdateTestSuiteExecution(UTAFFwVars.utafFWTSEID);
				String vStatus = "";
			vStatus = UTAFInteractions.cfUTAFUpdateTestSuiteExecutionRest(UTAFFwVars.utafFWTSEID);
			if(vStatus.equalsIgnoreCase("COMPLETED"))
			{
				//Thread.sleep(70000);
				//String sMessage = SendEmailAPI.callEMAILJSON(UTAFFwVars.utafFWTSEID, sSubject);
				//sendReportByMail_back("", sSubject,sMessage);
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//UTAFLog.info("sendReportByMail Method under development");
	}

}
