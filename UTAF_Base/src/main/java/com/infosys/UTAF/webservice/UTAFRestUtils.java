package com.infosys.UTAF.webservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.gson.Gson;
import com.infosys.UTAF.UTAFCommonFunctions2;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;



public class UTAFRestUtils {
	
	public String[] keystoreValues() throws Exception{
		String[] values = new String[2];
		values[0]=UTAFCommonFunctions2.cfGlGetElementProperty("keystore");
		values[1]=UTAFCommonFunctions2.cfGlGetElementProperty("keystorePassword");
		return values;
	}
	
	
	public Map<String,String> inputMap(String input){
		Map<String,String> headers = new HashMap<String,String>();
		if(input!=null){
			String strArray[]=input.split(",");
			for(int i=0;i<strArray.length;i++){
				String key = strArray[i].split(":")[0].trim();
				String value = strArray[i].split(":")[1].trim();
				headers.put(key, value);
			}
		}
		return headers;
	}
	
	public Map<String,String> header(String payloadType ){
		Map<String,String> header = new HashMap<String,String>();
		if(payloadType.equalsIgnoreCase("json")){
			header.put("Content-Type", "application/json");
		}
		else{
			header.put("Content-Type", "text/xml;charset=UTF-8");
		}
		return header;
	}
	
	public Response postMethod(String url, String requestBody, String payloadType, String header) throws Exception{
		if(!keystoreValues()[0].equals("NA")){
			RestAssured.config = RestAssured.config().sslConfig(getSslConfig());
		} 
		
		Map<String, String> headerMap = new HashMap<String,String>();
		headerMap = inputMap(header);
		if(headerMap.isEmpty()){
			headerMap=header(payloadType);
		}
		else{
			headerMap.putAll(header(payloadType));
		}
		Response response = RestAssured.given()
							.headers(headerMap)
							.body(requestBody)
							.post(url);
		return response;
	}
	
	public Response getMethodWithQueryParams(String url, String param, String header){
		
		Map<String,String> headerMap = new HashMap<String,String>();
		Map<String,String> paramMap = new HashMap<String,String>();
		headerMap = inputMap(header);
		paramMap =  inputMap(param);
		Response response = null;
		String userName="";
		String password="";
		
		if(!paramMap.isEmpty()&&!headerMap.isEmpty()){
			response=RestAssured.given().queryParams(paramMap).headers(headerMap).get(url);
		}
		else if(!paramMap.isEmpty()&&headerMap.isEmpty()){
			response=RestAssured.given().auth().basic(userName, password).queryParams(paramMap).get(url);
		}
		else if(paramMap.isEmpty()&&!headerMap.isEmpty()){
			response=RestAssured.given().headers(headerMap).get(url);
		}
		else if(paramMap.isEmpty()&&headerMap.isEmpty()){
			response=RestAssured.given().get(url);
		}
		return response;
	}
	
	public Response getMethodWithPathParams(String url, String param, String header){
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap = inputMap(header);
		Response response = null;
		String params[] = param.split(",");
		for(int i=0;i<params.length;i++){
			int val = i+1;
			url=url.replace("param"+val, params[i].trim());	
		}
		if(!headerMap.isEmpty()){
			response=RestAssured.given().headers(headerMap).get(url);
		}
		else{
			response=RestAssured.given().get(url);
		}
		return response;	
	}
	
	public Response putMethod(String url, String requestBody, String payloadType, String header){
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap = inputMap(header);
		if(headerMap.isEmpty()){
			headerMap=header(payloadType);
		}
		else{
			headerMap.putAll(header(payloadType));
		}
		Response response = RestAssured.given()
							.headers(headerMap)
							.body(requestBody)
							.put(url);
		return response;
	}
	
	public SSLConfig getSslConfig() throws Exception {
		String fileName = keystoreValues()[0];
		String password = keystoreValues()[1];
	    KeyStore keyStore = null;
	    try {
	      FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//key store//"+fileName);
	      keyStore = KeyStore.getInstance("JKS");
	      keyStore.load(
	          fis,
	          password.toCharArray());

	    } catch (Exception ex) {
	      System.out.println("Error while loading keystore >>>>>>>>>");
	      ex.printStackTrace();
	    }
	    if (keyStore != null) {
	      org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = null;
	      try {
	        clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, password);
	      } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	      } catch (KeyManagementException e) {
	        e.printStackTrace();
	      } catch (KeyStoreException e) {
	        e.printStackTrace();
	      } catch (UnrecoverableKeyException e) {
	        e.printStackTrace();
	      }
	      return new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
	    }
	    else return  null;
	  }
	
	}
