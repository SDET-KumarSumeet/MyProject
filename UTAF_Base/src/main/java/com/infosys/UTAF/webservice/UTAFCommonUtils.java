package com.infosys.UTAF.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.infosys.UTAF.UTAFCommonFunctions2;
import com.infosys.UTAF.UTAFFwVars;

public class UTAFCommonUtils extends UTAFCommonFunctions2 {
	
	public String getBody(String fileName) throws IOException{
		String bodyPath =UTAFFwVars.utafFWFolderPath + "request_xmls//"+fileName;
		return new String(Files.readAllBytes(Paths.get(bodyPath)));
	}
	
	public List<String> xmlFilters(String body){
		String bd[] = body.split(">");
		List<String> list = new ArrayList<String>();
		for(String filter:bd){
			if(filter.startsWith("$")){
				list.add(filter.substring(0, filter.lastIndexOf("<")));
			}
		}
		return list;
	}
	
	public List<String> jsonFilters(String body){
		String bd[] = body.split(":");
		List<String> list = new ArrayList<String>();
		for(String filter:bd){
			if(filter.startsWith("usr")){
				list.add(filter.substring(0, filter.lastIndexOf(",")));
			}
		}
		return list;
	}
	
	public String replaceFilter(List<String> list, Map<String,String> map, String body){
		list = xmlFilters(body);
		for(String filter:list){
			body=body.replace(filter, map.get(filter));	
		}
		return body;
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
	
	public String generatePayload(Map<String,String> testData, Map<String,String>runTimeData, String fileName) throws IOException{
		String payLoad = getBody(fileName);
		Set<String> testDataKeys = testData.keySet();
		Set<String> runTimeDataKeys = runTimeData.keySet();
		if(testData.size()!=0){
			for(String key:testDataKeys){
				if(payLoad.contains(key)){
					payLoad=payLoad.replace(key, testData.get(key));
				}
			}
		}
		
		if(runTimeData.size()!=0){
			for(String key:runTimeDataKeys){
				if(payLoad.contains(key)){
					payLoad=payLoad.replace(key, runTimeData.get(key));
				}
			}
		}
		
		return payLoad;
	}
	
	public Map<String,String> stringToMap(String str) throws Exception{
		Map<String, String> map = new HashMap<String,String>();
		String input[] = str.split(",");
		for(int i=0;i<input.length;i++){
			map.put(input[i].trim(), cfGlGetElementProperty(input[i]).trim());
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
	
	public String requestBody(String fileName, Map<String,String> map) throws IOException{
		String body=getBody(fileName);
		List<String> list = xmlFilters(body);
		body=replaceFilter(list,map,body);
		return body;
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
	
	public Document stringToDoc(String xmlString) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        if(xmlString.contains("&")){
        	xmlString=xmlString.replace("&", "&amp");
        }
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
		return doc;
	}
	
	public String JsonToXml(String JSON){
		JSONObject json = new JSONObject(JSON);
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<root>" + XML.toString(json)+ "</root>";
		return xml;
	}
	
	public JSONObject stringToJson(String str){
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
	
	public Connection getConnection(){
		String driver="";
		String url="";
		String userName="";
		String password="";
		
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
	
	public String getValueFromDb(String query,String columnName){
		String value="";
		Connection connection = getConnection();
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
				value=result.getString(columnName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;	
	}
	
	public static Properties configProperties() throws IOException{
		Properties prop = new Properties();
		FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir") + "//data//config.properties"));
		prop.load(file);
		return prop;
	}
	
	

}
