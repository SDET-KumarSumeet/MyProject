package com.infosys.UTAF;

import java.io.IOException;
import java.sql.SQLException;

public class UTAFUdmBridge {
	static String UTAF_DataKey[] = new String[5];
	static String UTAF_UpdatedVal;

	public static void udm_getValue(String DataKey){
		UTAFLog.info("udm_getValue Method under development");
	}
	public static void udm_getValue_back(String DataKey) throws ClassNotFoundException, IOException, SQLException {
		UTAF_DataKey = DataKey.split("_");

		switch (UTAF_DataKey[2]) {
		case "partyID":
			UTAFUdm.udmBridge(UTAFFwVars.utafFWProps.getProperty("partyID"),
					UTAFFwVars.utafFWProps.getProperty("update_partyID"), DataKey);
		case "saltoAddress":
			UTAFUdm.udmSALTOAddress(UTAFFwVars.utafFWProps.getProperty("selectAddress"),
					UTAFFwVars.utafFWProps.getProperty("updateAddress"), DataKey); 
		}
		
		
	}

}
