package com.infosys.UTAF;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import nu.xom.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.infosys.UTAF.UTAFCommonFunctions2;
import com.infosys.UTAF.UTAFFwVars;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;



public class UTAFRestUtils {
	
	public String[] keystoreValues() throws Exception{
		String[] values = new String[2];
		values[0]=UTAFFwVars.utafFWProps.getProperty(UTAFRead2.runTimeVar.get("TCIP_1"));
		if(values[0]!=null){
			values[1]=UTAFFwVars.utafFWProps.getProperty(UTAFRead2.runTimeVar.get("TCIP_1")+"_password");
		}
		else{
			values[1]=null;
		}
		return values;
	}
	
	public String getBody(String fileName) throws IOException{
		String bodyPath ="";
        if(fileName.contains("//")||fileName.contains("\\")){
               bodyPath=fileName;
        }
        else{
               bodyPath = UTAFFwVars.utafFWFolderPath + "//requestFiles//"+fileName;
        }
        return new String(Files.readAllBytes(Paths.get(bodyPath)));
	}
	
	
	public String generatePayload(Map<String,String> map, String fileName) throws IOException{
		String payLoad = getBody(fileName);
		Set<String> keys = map.keySet();
		for(String key:keys){
			if(payLoad.contains(key)){
				payLoad=payLoad.replace(key, map.get(key));
			}
		}
		return payLoad;
	}
	
	public String generatePayload(Map<String,String> testData, Map<String,String>runTimeData, String fileName) throws Exception{
		String payLoad = fileName;
		try {
			if(fileName.endsWith(".xml")||fileName.endsWith(".json")){
				payLoad = getBody(fileName);
			}
			Set<String> testDataKeys = testData.keySet();
			Set<String> runTimeDataKeys = runTimeData.keySet();
			if(testData.size()!=0){
				for(String key:testDataKeys){
					if(fileName.endsWith(".xml")||fileName.endsWith(".json")){
					if(payLoad.contains(key)){
						payLoad=payLoad.replace("${"+key+"}", testData.get(key));
					}
					}
					else {
						if(payLoad.contains(key)){
						payLoad=payLoad.replace("{"+key+"}", testData.get(key));
					}
					}
				}
			}
			
			if(runTimeData.size()!=0){
				for(String key:runTimeDataKeys){
					if(fileName.endsWith(".xml")||fileName.endsWith(".json")){
					if(payLoad.contains(key)){
						payLoad=payLoad.replace("${"+key+"}", runTimeData.get(key));
					}
					}
					else {
						if(payLoad.contains(key)){
						payLoad=payLoad.replace("{"+key+"}", runTimeData.get(key));
						}
					}
					
				}
			}
			
			if(fileName.endsWith(".xml")){
				for(String prop:patternMatch("Req_",payLoad)){
					payLoad = payLoad.replace("${"+prop+"}", "");
				}
				payLoad=payLoad.trim();
				if(!(payLoad.startsWith("<?xml"))) {
				payLoad="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +payLoad;
				}
				payLoad=xmlDocument(removeEmptyTags(payLoad));
				payLoad=payLoad.replace("<!--Optional: -->", "");
				payLoad=payLoad.replace("EMPTYTAG", "");				
			} 
			
		}
		catch (Exception ex){
			UTAFCommonFunctions2.cfGLGenericExceptionHandlingThrow(Thread.currentThread().getStackTrace()[1].getMethodName(),
                    ex.getMessage());
		}
		return payLoad;
	}
	
	public String removeEmptyTags(String xml) throws Exception{
		InputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		nu.xom.Document doc = new Builder().build(input);
		removeEmptyNode(doc.getRootElement());
		return doc.toXML();
	}
	
	public void removeEmptyNode(Node node) {
	    if(node.getChildCount()!=0){
	        int count = node.getChildCount();
	        for (int i = count-1; i >= 0 ; i--) { 
	            removeEmptyNode(node.getChild(i));
	        }
	    }

	    doCheck(node);
	}

	public void doCheck(Node node){
	    if(node.getChildCount() == 0 && "".equals(node.getValue().trim())) {
	        try{node.getParent().removeChild(node);}catch(Exception e){}
	    }   
	}
	
	public Map<String,String> stringToMap(String str) throws Exception{
		Map<String, String> map = new HashMap<String,String>();
		str=str.replaceAll("\\s+", "");
		String input[] = str.split(",");
		for(int i=0;i<input.length;i++){
			String key = input[i].split("=")[0];
			String value = input[i].split("=")[1];
			map.put(key.replaceAll("\\s+", ""), value.replaceAll("\\s+", ""));
		}
		return map;
	}
	
	public List<String> stringToList(String str, int index){
		List<String> list = new ArrayList<String>();
		String input[] = str.split(",");
		for(int i=0;i<input.length;i++){
			list.add(input[i].split("--")[index].trim());
		}
		return list;
	}
	
	
	public Document document(String xmlString) throws Exception{
        Document doc = stringToDoc(xmlString);
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT,"yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(doc), new StreamResult(out));
        doc=stringToDoc(out.toString());
	    return doc;
	}
	
	public String xmlDocument(String xmlString) throws Exception{
        Document doc = stringToDoc(xmlString);
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");	
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT,"yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(doc), new StreamResult(out));
	    return out.toString();
	}
	
	public String xmlDocumentISO(String xmlString) throws Exception{
		try{
        Document doc = stringToDoc(xmlString);
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");	
        tf.setOutputProperty(OutputKeys.INDENT,"yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(doc), new StreamResult(out));
	    return out.toString();}
		catch(Exception ex){
	    	return xmlString;
	    }
	}
	
	public Document stringToDoc(String xmlString) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        if(xmlString.contains("&")&&!(xmlString.contains("&amp"))){
        	xmlString=xmlString.replace("&", "&amp");
        }
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
		return doc;
	}
	
	public String JsonToXml(String JSON){
//		JSONObject json = new JSONObject(JSON);
		JSONObject jsonObject = null;
		if(JSON.startsWith("[")) {
            JSONArray array = new JSONArray(JSON);
            for(int i=0; i < array.length(); i++) {
                jsonObject=array.getJSONObject(i);
            }
        }
        else {
            jsonObject = new JSONObject(JSON);
        } 
		
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<root>" + XML.toString(jsonObject)+ "</root>"; 
		return xml;
	}
	
	public JSONObject jsonObject(String str){
		JSONObject json = new JSONObject(str);
		return json;
	}
	
	public Map<String,Object> JsonToMap(String a){
		String json="";
		String b[] = a.split(",");
		for(int j=0;j<b.length;j++){
			String c=b[j].split(":")[0];
			String d=b[j].split(":")[1];
			json=json+"\""+c+"\""+":"+"\""+d+"\""+",\n";
			
		}
		json="{"+json.substring(0, json.length()-1)+"}";
		JSONObject newJosn = new JSONObject(json);
		return newJosn.toMap();
	}
	
	public Connection dbConnection() throws Exception{
		String app = UTAFRead2.runTimeVar.get("TSIP_1").split("[_]")[0];
        String driver="";
        String url=UTAFCommonFunctions2.cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV + ".DB." + app + ".CONNURL");
        String userName=UTAFCommonFunctions2.cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV + ".DB." + app + ".USER");
        String password=UTAFCommonFunctions2.cfGlGetFrameworkProperty(UTAFFwVars.utafFWENV + ".DB." + app + ".PASS");
        if(url.contains("oracle")){
               driver = "oracle.jdbc.driver.OracleDriver";
        }
        else{
               driver = "com.mysql.jdbc.Driver";
        }
        try {
               Class.forName(driver);
        } catch (ClassNotFoundException e) {
               e.printStackTrace();
        }
        Connection connection= null;
        try {
               connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
               e.printStackTrace();
        }
        return connection;
 }
 

	
	public String getValueFromDb(String query,String columnName) throws Exception{
		String value="";
		Connection connection = null;
		try{
			connection= dbConnection();
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
					value=result.getString(columnName);
				}
				catch(Exception e){
					value=String.valueOf(result.getInt(columnName));
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
		
		return value;	
	}
	
	
	public Set<String> patternMatch(String pattern, String body){
		Pattern pat = Pattern.compile(pattern+"\\w+");
		Matcher matcher = pat.matcher(body);
		Set<String> propSet = new HashSet<String>();
		while (matcher.find()){propSet.add(matcher.group());}
		return propSet;
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
	
	public void doAuthentication(String username,String password) throws Exception{
		if(username.endsWith(".jks")||username.endsWith(".p12")){
			RestAssured.config = RestAssured.config().sslConfig(getSslConfig(username, password));
		}
		else{
			PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme(); 
			authScheme.setUserName(username); 
			authScheme.setPassword(password); 
			RestAssured.authentication = authScheme;
		}
	}
	
	
	
	public Response postMethod(String url, String requestBody, String payloadType, String header) throws Exception{
		String username = UTAFFwVars.utafFWProps.getProperty(UTAFRead2.runTimeVar.get("TCIP_1"));
		String password = UTAFFwVars.utafFWProps.getProperty(UTAFRead2.runTimeVar.get("TCIP_2"));
		if(username!=null){
			doAuthentication(username,password);
//			RestAssured.config = RestAssured.config().sslConfig(getSslConfig());
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
	
	public SSLConfig getSslConfig(String fileName, String password) throws Exception {
	    KeyStore keyStore = null;
	    String instance ="";
	    try {
	      FileInputStream fis = new FileInputStream(UTAFFwVars.utafFWFolderPath + "//keyStore//"+fileName);
	      if(fileName.endsWith(".jks")){
	    	  instance = "JKS";
	      }
	      else{
	    	  instance ="PKCS12";
	      }
	      keyStore = KeyStore.getInstance(instance);
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
	
	public int getTagCount(String tagName, String responseType) throws Exception{
		
		Document doc = null;
		if(responseType.equalsIgnoreCase("xml")){
			doc = stringToDoc(UTAFCommonFunctions2.xml);
		}
		else{
			doc = stringToDoc(JsonToXml(UTAFCommonFunctions2.json));
		}
		NodeList nodes = doc.getElementsByTagName(tagName);
		return nodes.getLength();
	}
	
/*	public String getTagValue(String path, String responseType) throws Exception{
		
		String value ="";
		Document doc = null;
		if(responseType.equalsIgnoreCase("xml")){
			doc = document(UTAFCommonFunctions2.xml.toString());
		}
		else{
			doc = document(JsonToXml(UTAFCommonFunctions2.json));
			UTAFCommonFunctions2.response.jsonPath().getString("tc_user_id[2]");
		}
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expression = xpath.compile(path);
		Object result = expression.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;
		value = nodes.item(0).getTextContent();
		return value;
	} */
	
	public String getTagValue(String path, String responseType) throws Exception{
		
		String value ="";
		Document doc = null;
		if(responseType.equalsIgnoreCase("xml")){
			doc = document(UTAFCommonFunctions2.xml.toString());
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expression = xpath.compile(path);
			Object result = expression.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList)result;
			value = nodes.item(0).getTextContent();
		}
		else {
			value=UTAFCommonFunctions2.response.jsonPath().getString(path);
		}
		
		return value;
	}
	
	
	public String expectedTagData(String expected) throws Exception{
		String val="";
		if(expected.startsWith("Req")){
			val = UTAFCommonFunctions2.testData.get(expected.trim());
		}
		else{
			val = UTAFCommonFunctions2.runTimeData.get(expected.trim());
			if(val==null){
				String xpath = UTAFCommonFunctions2.cfGlGetElementProperty(expected.trim());
				if(xpath.trim().contains("Req_")){
					for(String prop : patternMatch("Req_", xpath)){
						xpath = xpath.replace("${"+prop+"}", UTAFCommonFunctions2.testData.get(prop));
					}
				}
				if(xpath.trim().contains("Rsp_")){
					for(String prop : patternMatch("Rsp_", xpath)){
						xpath = xpath.replace("${"+prop+"}", UTAFCommonFunctions2.runTimeData.get(prop));
					}
				}
				val=getTagValue(xpath.trim(), UTAFCommonFunctions2.requestpPayloadType);
				UTAFCommonFunctions2.runTimeData.put(expected.trim(), val);
				UTAFRead2.runTimeVar.putAll(UTAFCommonFunctions2.runTimeData); 
			}
		}
		return val;
	}
	
	public String expectedTagValue(String expected,String responseType) throws Exception{
		return getTagValue(expected.trim(),responseType);
	}
	
	public String expectedValues(String expected,String responseType) throws Exception{
		String expectedValue="";
		if(expected.trim().startsWith("Req")||expected.trim().startsWith("Rsp")){
			expectedValue=expectedTagData(expected.trim());
		}
		else if(expected.trim().contains("//*")){
			expectedValue=expectedTagValue(expected.trim(),responseType);
		}
		else if(expected.trim().startsWith("CDB_")){
			expectedValue="null";
		}
		else{
			expectedValue=expected.trim();
		}
		return expectedValue;
	}
	
	public String actualValues(String[] actualVals){
		String actual="";
		for(String val:actualVals){
			if(val==null) {
				val="null";
			}
			actual=actual.trim()+val.trim()+",";
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
	
	
	public String expectedQueryValue(String query, String colName, String variable) throws Exception{
        String vars[]=variable.split(",");
        for(int i=0;i<vars.length;i++){
               String tempVariable = vars[i].trim();
               if(vars[i].startsWith("Req")){
                     vars[i] = UTAFCommonFunctions2.testData.get(tempVariable);
               }
               else if(vars[i].startsWith("Rsp")){
                     vars[i] = UTAFCommonFunctions2.runTimeData.get(tempVariable);
                     if(vars[i]==null){
                            String xpath = UTAFCommonFunctions2.cfGlGetElementProperty(tempVariable);
                            vars[i]=getTagValue(xpath.trim(), UTAFCommonFunctions2.requestpPayloadType);
                            UTAFCommonFunctions2.runTimeData.put(tempVariable.trim(), vars[i]);
                            vars[i] = UTAFCommonFunctions2.runTimeData.get(tempVariable);
                     }
               }
               int j=i+1;
               query = query.replace("v"+j, vars[i].trim());
        }
        
        UTAFLog.info("Query = "+query);
        return getValueFromDb(query,colName);
 }

	public String modifiedDescription(String description,String size, String dataType, String key, String value){
		String topRow ="<tr><td rowspan=size style=\"background-color: Brown;border:1px solid #ddd !important;  color: white !important;font-size: 1.1em;font-weight: bold; width:23%;\">data</td><td style=\"border:1px solid #ddd !important; padding: 0px !important; text-align: left !important;\r\n" + 
				"    max-width:0px; word-break: break-all;\">key = value</td></tr>";
		String row = "<tr><td style=\"border:1px solid #ddd !important; padding: 0px !important;word-break: break-all; text-align: left !important;\">key = value</td></tr>";

//		String topRow ="<tr><td class=\"first\" rowspan=\"size\">data</td><td>key = value</td></tr>";
//		String row = "<tr><td>key = value</td></tr>";
//		String topRow ="<tr><td rowspan=size style=\"background-color: #5C2D91;  color: white; width:15%; border: 1px solid;\">data</td><td style=\"border: 1px solid;\">key = value</td></tr>";
//		String row = "<tr><td style=\"border: 1px solid;\">key = value</td></tr>";

		String newTopRow="";
		String newNextRow="";
		String requestBody = "<a href="+value+" target=\"_blank\"rel=\"noopener noreferrer\">Click here for request body</a>";
		String responseBody = "<a href="+value+" target=\"_blank\"rel=\"noopener noreferrer\">Click here for response body</a>";
		
		if(dataType.equals("Test Data")){
			newTopRow="topRow_testData"; newNextRow="row_testData";
			if(description.contains(newTopRow)){
				String newRow = topRow.replace("size", size).replace("data", dataType).replace("key = value", value);
				description = description.replace(newTopRow, newRow);
			}
			else{
				String newRow=row.replace("key = value", value)+"\n"+newNextRow;
				description = description.replace(newNextRow, newRow);
			}
		}
		else if(dataType.equals("Runtime Data")){
			newTopRow="topRow_runTimeData"; newNextRow="row_runTimeData";
			if(description.contains(newTopRow)){
				String newRow = topRow.replace("size", size).replace("data", dataType).replace("key", key).replace("value", value);;
				description = description.replace(newTopRow, newRow);
			}
			else{
				String newRow=row.replace("key", key).replace("value", value)+"\n"+newNextRow;
				description = description.replace(newNextRow, newRow);
			}
		}
		else if(dataType.equals("Headers")){
			newTopRow="topRow_headers"; newNextRow="row_headers";
			if(description.contains(newTopRow)){
				String newRow = topRow.replace("size", size).replace("data", dataType).replace("key = value", value);
				description = description.replace(newTopRow, newRow);
			}
			else{
				String newRow=row.replace("key = value", value)+"\n"+newNextRow;
				description = description.replace(newNextRow, newRow);
			}
		}
		else if(dataType.equals("URL")){
			String newRow = topRow.replace("size", size).replace("data", dataType).replace("key = value", value);
			description = description.replace("topRow_url", newRow);
		}
		else if(dataType.equals("Request Body")){
			String newRow = topRow.replace("size", size).replace("data", dataType).replace("key = value", requestBody);
			description = description.replace("topRow_requestBody", newRow);
		}
		else {
			String newRow = topRow.replace("size", size).replace("data", dataType).replace("key = value", responseBody);
			description = description.replace("topRow_responseBody", newRow);
		}
		
		return description;
	}

	
	public String vData(String... inputParams) throws Exception{
		String filePath="";
		filePath = UTAFRead2.vReadOnlyPath + "/templates/testDescription.html";
		//filePath=UTAFFwVars.utafFWFolderPath + "\\testDescription.html";
		
		 String description = new String(Files.readAllBytes(Paths.get(filePath)));
	        for(int i=0;i<inputParams.length;i++){
	               if(inputParams[i]!=null){
	                     if(inputParams[i].endsWith(".xml")||inputParams[i].endsWith(".json")){
	                            if(inputParams[i].startsWith("Req")){
	                                   description=modifiedDescription(description,"1", "Request Body", "", inputParams[i].split("=")[1]);
	                            }
	                            else{
	                                   description=modifiedDescription(description,"1", "Response Body", "", inputParams[i].split("=")[1]);
	                            }
	                            
	                     }
	                     else if(inputParams[i].contains("http")){
	                            description=modifiedDescription(description,"1", "URL", "", inputParams[i]);
	                     }
	                     else if(inputParams[i].contains("Req")||inputParams[i].contains("Rsp")||inputParams[i].contains("Head")){
	                            String input[] = inputParams[i].split(",");
	                            int countTestData=0;
	                            int countRunTimeData=0;
	                            int countHeader=0;
	                            for(int j=0;j<input.length;j++){
	                                   if(input[j].startsWith("Req")){countTestData=countTestData+1;}
	                                   else if(input[j].startsWith("Rsp")){countRunTimeData=countRunTimeData+1;}
	                                   else{countHeader=countHeader+1;}
	                            }
	                            for(int j=0;j<input.length;j++){
	                                   if(input[j].startsWith("Req")){
	                                          description=modifiedDescription(description, String.valueOf(countTestData), "Test Data", "", StringEscapeUtils.escapeHtml(input[j]));
	                                   }
	                                   else if(input[j].startsWith("Rsp")){
	                                	   String value =StringEscapeUtils.escapeHtml(UTAFCommonFunctions2.runTimeData.get(input[j]));
	                                          description=modifiedDescription(description, String.valueOf(countRunTimeData), "Runtime Data", input[j], value);
	                                   }
	                                   else if(input[j].contains(":")){
	                                	   		if(input[j].contains("Header")){
	                                	   			input[j]=input[j].split("=")[1];
	                                	   		}
	                                          description=modifiedDescription(description, String.valueOf(countHeader), "Headers", "", input[j]);
	                                   }
	                            }
	                     }
	               }
	        }
	        description = description.replace("topRow_testData", "").replace("row_testData", "");
	        description = description.replace("topRow_runTimeData", "").replace("row_runTimeData", "");
	        description = description.replace("topRow_headers", "").replace("row_headers", "");
	        description = description.replace("topRow_url", "").replace("topRow_requestBody", "").replace("topRow_responseBody", "");
	        return description;
 }


	
	public void assertion(String methodName, String actual, String expected) throws Exception{
		String expectdValue = "<font color=\"#32CD32\"><b>expectedVal</b></font>";
		String actualValue = "<font color=\"#32CD32\"><b>actualVal</b></font>";
		String actualValueError = "<font color=\"red\"><b>actualValError</b></font>";
		if (actual.equals(expected)) {
			UTAFLog.info("expected value = "+expected+". And actual value = "+actual);
			String vData = "expected value = "+expectdValue.replace("expectedVal", expected)+". And actual value = "+actualValue.replace("actualVal", actual);
			UTAFCommonFunctions2.cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		} else {
			UTAFLog.error("expected value = "+expected+". But actual value = "+actual);
			String vData = "expected value = "+expectdValue.replace("expectedVal", expected)+". But actual value = "+actualValueError.replace("actualValError", actual);
			UTAFCommonFunctions2.cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		}
		
	}
	}
