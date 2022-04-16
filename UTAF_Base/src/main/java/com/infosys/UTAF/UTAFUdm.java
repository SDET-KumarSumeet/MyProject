package com.infosys.UTAF;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UTAFUdm {

	public static void udmBridge(String selectQuery, String updateQuery, String datakey)
			throws IOException, ClassNotFoundException, SQLException {
		
		UTAFFwVars.utafFWTCFlowType = "V2";
		UTAFFwVars.utafFWTCReference = "Test001";
		System.out.println("Request in udmBridge");
		System.out.println(selectQuery);
		System.out.println(updateQuery);
		System.out.println(datakey);
		String udm_HOSTNAME;
		String udm_PORT;
		String udm_DBNAME;
		String udm_USER;
		String udm_PASSWORD;
		String query = selectQuery;
		String uQuery = updateQuery;
		String strResult = "strResult";
		query = query.replaceAll("utafFWENV", UTAFFwVars.utafFWENV);
		query = query.replaceAll("utafFWTCFlowType", UTAFFwVars.utafFWTCFlowType);
		query = query.replaceAll("utafFWTCChannel", UTAFFwVars.utafFWTCChannel);
		System.out.println(query);
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;
		int count = 0;
		try {
			udm_HOSTNAME = UTAFFwVars.utafFWProps.getProperty("udm_HOSTNAME");
			udm_PORT = UTAFFwVars.utafFWProps.getProperty("udm_PORT");
			udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			udm_USER = UTAFFwVars.utafFWProps.getProperty("udm_USER");
			udm_PASSWORD = UTAFFwVars.utafFWProps.getProperty("udm_PASSWORD");

			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://" + udm_HOSTNAME + ":" + udm_PORT + "/"
					+ udm_DBNAME + "?autoReconnect=true&useSSL=false", udm_USER, udm_PASSWORD);
			stmt = (Statement) con.createStatement();
			stmt2 = (Statement) con.createStatement();
			ResultSet SQrs = stmt.executeQuery(query);

			while (SQrs.next()) {
				count = count + 1;
				strResult = SQrs.getString(1);
				System.out.println(strResult);
				System.out.println("Test Data available");

				uQuery = uQuery.replaceAll(datakey, strResult);
				uQuery = uQuery.replaceAll("utafFWTCReference", UTAFFwVars.utafFWTCReference);
				System.out.println(uQuery);
				stmt2.executeUpdate(uQuery);
				UTAFRead2.runTimeVar.put(datakey, strResult);
			}
			if (count == 0) {
				UTAFFwVars.utafFWTCStatus = "Fail";
				UTAFFwVars.utafFWTCError = "Data not available for :" + query;
				System.out.println("Out udmbridge");
			} else {
				UTAFFwVars.utafFWTCStatus = "Pass";
				System.out.println("Out udmbridge");
			}
		} catch (Exception ex) {
			UTAFFwVars.utafFWTCStatus = "FAIL";
			UTAFFwVars.utafFWTCError = ex.getMessage();
			System.out.println(ex.getMessage());
			System.out.println("Failed in udmBridge");
		} finally {
			stmt.close();
			stmt2.close();
			con.close();
		}

	}
	public static void udmSALTOAddress(String selectQuery, String updateQuery, String datakey)
			throws IOException, ClassNotFoundException, SQLException {
		System.out.println("Request in udmSALTOAddress");
		System.out.println(selectQuery);
		System.out.println(updateQuery);
		System.out.println(datakey);
		String udm_HOSTNAME;
		String udm_PORT;
		String udm_DBNAME;
		String udm_USER;
		String udm_PASSWORD;
		String query = selectQuery;
		String uQuery = updateQuery;
		String strStreet = "strStreet";
		String strZip = "strZip";
		String strHouse = "strHouse";
		String strFloorNum = "strFloor";
		String strApartment = "strApartment";
		String strBlock = "strBlock";
		String strBox = "strBox";
		query = query.replaceAll("utafFWTCFlowType", UTAFFwVars.utafFWENV);
		query = query.replaceAll("utafFWTCFlowType", UTAFFwVars.utafFWTCFlowType);
		query = query.replaceAll("utafFWTCChannel", UTAFFwVars.utafFWTCChannel);
		System.out.println(query);
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;
		int count = 0;
		try {
			udm_HOSTNAME = UTAFFwVars.utafFWProps.getProperty("udm_HOSTNAME");
			udm_PORT = UTAFFwVars.utafFWProps.getProperty("udm_PORT");
			udm_DBNAME = UTAFFwVars.utafFWProps.getProperty("udm_DBNAME");
			udm_USER = UTAFFwVars.utafFWProps.getProperty("udm_USER");
			udm_PASSWORD = UTAFFwVars.utafFWProps.getProperty("udm_PASSWORD");

			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://" + udm_HOSTNAME + ":" + udm_PORT + "/"
					+ udm_DBNAME + "?autoReconnect=true&useSSL=false", udm_USER, udm_PASSWORD);
			stmt = (Statement) con.createStatement();
			stmt2 = (Statement) con.createStatement();
			ResultSet SQrs = stmt.executeQuery(query);

			while (SQrs.next()) {
				String saltoAddress = "";
				count = count + 1;
				strStreet = SQrs.getString(1);
				strZip = SQrs.getString(2);
				strHouse = SQrs.getString(3);
				strFloorNum = SQrs.getString(4);

				// Street_d'Arconatistraat;ZipCode_1700;HouseNumber_14
				saltoAddress = "Street_".concat(strStreet).concat(";ZipCode_").concat(strZip).concat(";HouseNumber_")
						.concat(strHouse);
				System.out.println(saltoAddress);
				System.out.println("Test Data available");
				uQuery = uQuery.replaceAll(datakey, saltoAddress);
				uQuery = uQuery.replaceAll("utafFWTCReference", UTAFFwVars.utafFWTCReference);
				System.out.println(uQuery);
				stmt2.executeUpdate(uQuery);
				UTAFRead2.runTimeVar.put(datakey, saltoAddress);
			}
			if (count == 0) {
				UTAFFwVars.utafFWTCStatus = "Fail";
				UTAFFwVars.utafFWTCError = "Data not available for :" + query;
				System.out.println("Out udmbridge");
			} else {
				UTAFFwVars.utafFWTCStatus = "Pass";
				System.out.println("Out udmbridge");
			}
		} catch (Exception ex) {
			UTAFFwVars.utafFWTCStatus = "FAIL";
			UTAFFwVars.utafFWTCError = ex.getMessage();
			System.out.println(ex.getMessage());
			System.out.println("Failed in udmBridge");
		} finally {
			stmt.close();
			stmt2.close();
			con.close();
		}
	}


}