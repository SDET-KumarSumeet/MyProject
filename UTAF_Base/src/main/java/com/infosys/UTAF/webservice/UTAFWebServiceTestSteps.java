package com.infosys.UTAF.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;

import com.infosys.UTAF.UTAFCommonFunctions2;
import com.infosys.UTAF.UTAFRead2;

import io.restassured.response.Response;


public class UTAFWebServiceTestSteps {
	
	public UTAFRestUtils restUtils;
	public Response response =UTAFCommonFunctions2.response ;
	public UTAFCommonUtils comUtils;
	public String xml=UTAFCommonFunctions2.xml;
	public String requestpPayloadType;
	public String json=UTAFCommonFunctions2.json;
	public boolean flag;
	public Map<String, Object> headers = new HashMap<String,Object>();
	public Map<String, String> stepData = new HashMap<String,String>();
	public static Map<String,String> testData = new LinkedHashMap<String,String>();
	public static HashMap<String,String> runTimeData = new LinkedHashMap<String,String>();
	
	public Response postRequest(String input01, String input02, String input03, String input04) throws Exception{
		comUtils = new UTAFCommonUtils();
		
		restUtils = new UTAFRestUtils();
		if(!input01.equals("")){
			stepData=comUtils.stringToMap(input01);
			testData.putAll(stepData);
		}
		String url=input04;
		String fileName=input02;
		String payloadType=input02.split("[.]")[1];
		String header=input03;
		String requestBody=comUtils.generatePayload(testData, runTimeData, fileName);
		System.out.println("===============================================Request===========================================");
		System.out.println(requestBody);
		requestpPayloadType=payloadType;
		response = restUtils.postMethod(url, requestBody, payloadType, header);
		if(payloadType.equalsIgnoreCase("xml")){
			xml=response.body().asString();
			System.out.println("===============================================Response===========================================");
			response.body().prettyPrint();
		}
		else{
			json=response.body().asString();
			System.out.println("===============================================Response===========================================");
			response.body().prettyPrint();
		}
		
		if(response.body().asString().length()==0){
			flag=false;
			String step = "posting the Request"+" --> \n"+"Received null response";
//			UTAFRead2.test.fail(step);
		}
		else{
			flag=true;
//			UTAFRead2.test.pass("posting the Request");
		}
		System.out.println(testData);
		return response;
	}
	
	public Response getRequest(String input01, String input02, String input03,String input04){
		comUtils = new UTAFCommonUtils();
		restUtils = new UTAFRestUtils();
		String url=input04;
		String param=input02;
		String header=input03;
		String responseType=input01;
		if(param.contains(":")){
			if(param.contains("$")){
				String params[]=param.split(",");
				for(int i=0;i<params.length;i++){
					param=param.replace(params[i].split(":")[1].trim(), testData.get(params[i].split(":")[1].trim()));
				}
			}
			response = restUtils.getMethodWithQueryParams(url, param, header);
		}
		else{
			if(param.contains("$")){
				String params[]=param.split(",");
				for(int i=0;i<params.length;i++){
					param=param.replace(params[i].trim(), testData.get(params[i].trim()));
				}
			}
			response = restUtils.getMethodWithPathParams(url, param, header);
		}
		if(responseType.equalsIgnoreCase("xml")){
			xml=response.body().asString();
		}
		else{
			json=response.body().asString();
		}String actual="";
		if(response.body().asString().length()>0){
			actual = actual+"No empty response";
		}
		if(response.body().asString().length()==0){
			flag=false;
			String step = "Getting the Request"+" --> \n"+"Received null response";
//			UTAFRead2.test.fail(step);
		}
		else{
			flag=true;
//			UTAFRead2.test.pass("Getting the Request");
		}
		return response;
	}
	
	public Response putRequest(String input01, String input02, String input03, String input04) throws Exception{
		comUtils = new UTAFCommonUtils();
		restUtils = new UTAFRestUtils();
		if(!input04.equals("")){
			stepData=comUtils.stringToMap(input04);
			testData.putAll(stepData);
		}
		String url=input01;
		String fileName=input02;
		String payloadType=input02.split("[.]")[1];
		String header=input03;
		String requestBody=comUtils.generatePayload(testData, runTimeData, fileName);
		System.out.println(requestBody);
		requestpPayloadType=payloadType;
		response = restUtils.putMethod(url, requestBody, payloadType, header);
		if(payloadType.equalsIgnoreCase("xml")){
			xml=response.body().asString();
		}
		else{
			json=response.body().asString();
		}
		String actual="";
		if(response.body().asString().length()>0){
			actual = actual+"No empty response";
		}
		if(response.body().asString().length()==0){
			flag=false;
			String step = "Updating the Request"+" --> \n"+"Received null response";
//			UTAFRead2.test.fail(step);
		}
		else{
			flag=true;
//			UTAFRead2.test.pass("Updating the Request");
		}
		return response;
	}
	
	public boolean validateStatusCode(String stepName, String expected){
		String actual = String.valueOf(response.statusCode());
		return assertion(stepName, expected, actual);
	}
	
	public boolean validateStatusLine(String stepName, String expected){
		String actual = response.getStatusLine();
		return assertion(stepName, expected, actual);
	}
	
	public boolean validateContentType(String stepName, String expected){
		String actual = response.contentType();
		return assertion(stepName, expected, actual);
	}
	
	public boolean validateResponseHeaders(String stepName, String header, String expected) throws Exception{
		String headers[]=header.split(",");
		String expVals[]=expected.trim().split(",");
		String actualVals[]=new String[headers.length];
		for(int i=0;i<headers.length;i++){
			actualVals[i]=response.getHeader(headers[i]);
		}
		
		String actual=actualValues(actualVals);
		String finalExp=finalExpectedValues(expVals,requestpPayloadType);
		if(finalExp.equals(actual)){
			flag = true;
//			Report.pass(stepName, tagName, finalExp, actual);
//			UTAFRead2.test.pass(stepName);
		}
		else{
			flag=false;
			String step = stepName+" --> \n"+"expected value = "+finalExp+".But actual value = "+actual;
//			UTAFRead2.test.fail(step);
//			Report.fail(stepName, tagName, finalExp, actual);
		}
		
		return flag;
		
	}
	
	public boolean validateTagCount(String stepName,String tagName, String expected) throws Exception{
		String tagNames[]=tagName.split(",");
		String expVals[]=expected.split(",");
		String actualVals[]=new String[tagNames.length];
		for(int i=0;i<tagNames.length;i++){
			actualVals[i]=String.valueOf(getTagCount(tagNames[i].replace("\\s+", ""),requestpPayloadType));
		}
		String actual=actualValues(actualVals);
		String finalExp=finalExpectedValues(expVals,requestpPayloadType);
		if(finalExp.equals(actual)){
			flag = true;
//			Report.pass(stepName, tagName, finalExp, actual);
//			UTAFRead2.test.pass(stepName);
		}
		else{
			flag=false;
			String step = stepName+" --> \n"+"expected value = "+finalExp+".But actual value = "+actual;
//			UTAFRead2.test.fail(step);
//			Report.fail(stepName, tagName, finalExp, actual);
		}
		return flag;
	}
	
	public boolean validateTagValue(String stepName, String path, String expected) throws Exception{
		
		String paths[]=path.split(",");
		String expVals[]=expected.trim().split(",");
		String actualVals[]=new String[paths.length];
		for(int i=0;i<paths.length;i++){
			if(paths[i].trim().startsWith("$")){
				actualVals[i]=runTimeData.get(paths[i].trim());
			}
			else if(paths[i].trim().contains("//*")){
				actualVals[i]=getTagValue(paths[i].trim(), requestpPayloadType);
			}
			else{
				actualVals[i]=paths[i].trim();
			}
		}
		String actual=actualValues(actualVals);
		String finalExp=finalExpectedValues(expVals,requestpPayloadType);
		if(finalExp.equals(actual)){
			flag = true;
//			UTAFRead2.test.pass(stepName);
		}
		else{
			flag=false;
			String step = stepName+" --> \n"+"expected value = "+finalExp+".But actual value = "+actual;
//			UTAFRead2.test.fail(step);
		}
				
		return flag;
	}
	
	public int getTagCount(String tagName, String responseType) throws Exception{
		comUtils = new UTAFCommonUtils();
		Document doc = null;
		if(responseType.equalsIgnoreCase("xml")){
			doc = comUtils.stringToDoc(UTAFCommonFunctions2.xml);
		}
		else{
			doc = comUtils.stringToDoc(comUtils.JsonToXml(UTAFCommonFunctions2.json));
		}
		NodeList nodes = doc.getElementsByTagName(tagName);
		return nodes.getLength();
	}
	
	public String getTagValue(String path, String responseType) throws Exception{
		comUtils = new UTAFCommonUtils();
		String value ="";
		Document doc = null;
		if(responseType.equalsIgnoreCase("xml")){
			doc = comUtils.document(UTAFCommonFunctions2.xml);
		}
		else{
			doc = comUtils.document(comUtils.JsonToXml(UTAFCommonFunctions2.json));
		}
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expression = xpath.compile(path);
		Object result = expression.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;
		value = nodes.item(0).getNodeValue();
		return value;
	}
	
	public void storeTagValues(String stepName,String input04) throws Exception{
		comUtils = new UTAFCommonUtils();
		int count = runTimeData.size();
		List<String> tagName = comUtils.stringToList(input04, 0);
		List<String> path = comUtils.stringToList(input04, 1);
		for(int i=0;i<path.size();i++){
			String key = tagName.get(i);
			String value=getTagValue(path.get(i),requestpPayloadType);
			runTimeData.put(key, value);
		}
		int currentCount = runTimeData.size();
		if(currentCount==(count+path.size())){
			flag=true;
//			UTAFRead2.test.pass(stepName);
		}
		else{
			flag=false;
			String step = stepName+" --> \n Error occured while storing the values. Please check manually";
//			UTAFRead2.test.fail(step);
		}
		System.out.println(runTimeData);
	}
	
/*	public String generateRequestPayload(String stepName,String fileName,String expected) throws IOException{
		comUtils = new CommonUtils();
		String body = comUtils.generatePayload(data, fileName);
		return body;
	}*/
	
	public String expectedQueryValue(String expected){
		comUtils = new UTAFCommonUtils();
		String query = expected.split("--")[0].trim();
		String colName=expected.split("--")[1].trim();
		List<String> list = new ArrayList<String>();
		
		if(colName.startsWith("$")){
			colName=testData.get(colName);
		}
		if(query.contains("$")){
			String queryStr[]=query.split(" ");
			for(int i=0;i<queryStr.length;i++){
				if(queryStr[i].startsWith("$")){
					list.add(queryStr[i].trim().replace("\\s+", ""));
				}
			}
			for(String data:list){
				query=query.replace(data, testData.get(data));
			}
		}
		return comUtils.getValueFromDb(query,colName);
	}
	
	public String expectedTagData(String expected){
		return testData.get(expected.trim());
	}
	
	public String expectedTagValue(String expected,String responseType) throws Exception{
		return getTagValue(expected.trim(),responseType);
	}
	
	public String expectedValues(String expected,String responseType) throws Exception{
		String expectedValue="";
		if(expected.trim().startsWith("query")||expected.trim().startsWith("Query")){
			expectedValue=expectedQueryValue(expected.trim());
		}
		else if(expected.trim().startsWith("$")){
			expectedValue=expectedTagData(expected.trim());
		}
		else if(expected.trim().contains("//*")){
			expectedValue=expectedTagValue(expected.trim(),responseType);
		}
		else{
			expectedValue=expected.trim();
		}
		return expectedValue;
	}
	
	public String actualValues(String[] actualVals){
		String actual="";
		for(String val:actualVals){
			actual=actual+val+",";
		}
		actual=actual.substring(0, actual.length()-1);
		return actual;
	}
	
	public String finalExpectedValues(String[] expVals, String responseType) throws Exception{
		String finalExpVals[]=new String[expVals.length];
		for(int j=0;j<expVals.length;j++){
			finalExpVals[j]=expectedValues(expVals[j],responseType);	
		}
		return actualValues(finalExpVals);
	}
	
	public boolean ValidateDB(String stepName, String input01,String responseType,String expected) throws Exception{
		comUtils = new UTAFCommonUtils();
		String queries[]=input01.split(",");
		String expVals[]=expected.trim().split(",");
		String actualVals[]=new String[queries.length];
		for(int i=0;i<queries.length;i++){
			actualVals[i]=expectedQueryValue(queries[i]);
		}
		String actual=actualValues(actualVals);
		String finalExp=finalExpectedValues(expVals,responseType);
		if(finalExp.equals(actual)){
			flag = true;
		}
		else{
			flag=false;
			
		}
		return flag;
	}
	
	public boolean assertion(String stepName, String expected,String actual){
		if(actual.equals(expected)){
			flag=true;
//			Report.pass(stepName, "NA", expected, actual);
//			UTAFRead2.test.pass(stepName);
		}
		else{
			flag=false;
//			Report.fail(stepName, "NA", expected, actual);
			String step = stepName+" --> \n"+"expected value = "+expected+".But actual value = "+actual;
//			UTAFRead2.test.fail(step);
		}
		return flag;
	}
	
	public boolean assertion(int actual,int expected){
		if(actual==expected){
			flag=true;
		}
		else{
			flag=false;
		}
		return flag;
	}
	
	public void dataCleanup(String expected){
		response = null;
		xml="";
		requestpPayloadType="";
		json="";
		headers.clear();
		stepData.clear();
		testData.clear();
		runTimeData.clear();
		if(response ==null&&xml.equals("")&&requestpPayloadType.equals("")&&json.equals("")&&headers.size()==0
				&&stepData.size()==0&&testData.size()==0&&runTimeData.size()==0){
			flag=true;
//			Report.pass("preConditions", "NA", "preConditions satisfied", "preConditions satisfied");
		}
		else{
			flag=false;
			String step = "preConditions"+" --> \n"+"expected value = "+expected+".But actual value = "+"preConditions satisfied";
//			UTAFRead2.test.fail(step);
//			Report.fail("preConditions", "NA", "preConditions satisfied", "preConditions failed");
		}
	}
	
	
	
	
	public boolean runSteps(String[] stepData) throws Exception{
		String stepName=stepData[0];
		String input01=stepData[1];
		String input02=stepData[2];
		String input03=stepData[3];
		String input04=stepData[4];
		String expected=stepData[5];
		
		switch(stepName){
		case "postRequest"            : postRequest(input01, input02, input03, input04);break;
		case "getRequest"             : getRequest(input01, input02, input03, input04);break;
		case "putRequest"             : putRequest(input01, input02, input03, input04);break;
		case "validateStatusCode"     : validateStatusCode(stepName, input01);break;
		case "validateStatusLine"     : validateStatusLine(stepName, input01);break;
		case "validateContentType"    : validateContentType(stepName, input01);break;
		case "validateResponseHeaders": validateResponseHeaders(stepName, input01, input02);break;
		case "validateTagCount"       : validateTagCount(stepName, input01, input02);break;
		case "validateTagValue"       : validateTagValue(stepName, input01, input02);break;
		case "storeTagValues"         : storeTagValues(stepName,input01);break;
		case "preConditions"          : dataCleanup(input01);break;
		case "ValidateDB"             : ValidateDB(stepName,input01,input02,expected);break;
		
		}
		return flag;
	}
	
	public static void main(String[] args) throws Exception {
/*		TestSteps ts = new TestSteps();
		ts.headers("abc:def,ghi:jkl,mno:pqr");
		System.out.println(ts.headers); */
	}
	
	

}
