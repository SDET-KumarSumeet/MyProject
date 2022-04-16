package com.infosys.UTAF;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import progress.message.jclient.ConnectionFactory;
import progress.message.jclient.Queue;
import progress.message.jclient.QueueBrowser;

public class UTAFJMSUtil {
	public static void cfGlSendJmsMessage(String vApp,String fileName, String corrId, String testDataLocal, String queName,
			String lookUp, String brokerUrls) throws Exception {
		vApp = "." + vApp;

		ArrayList<String> bUrls = new ArrayList<String>();
		if (testDataLocal != null) {
			UTAFCommonFunctions2.stepData = UTAFCommonFunctions2.restUtils.stringToMap(testDataLocal);
			UTAFCommonFunctions2.testData.putAll(UTAFCommonFunctions2.stepData);
			UTAFRead2.runTimeVar.putAll(UTAFCommonFunctions2.testData);
		}
		if (brokerUrls != null) {
			String urls[] = brokerUrls.split(",");
			for (String bUrl : urls) {
				bUrls.add(bUrl);
			}
		}
		String xmlValue = UTAFCommonFunctions2.restUtils.generatePayload(UTAFCommonFunctions2.testData, UTAFCommonFunctions2.runTimeData, fileName);
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
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+ ".SONIC.USER"));
			env.put(Context.SECURITY_CREDENTIALS,
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +vApp+ ".SONIC.PASS"));
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
			env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.CONURL"));
			env.put("com.sonicsw.jndi.mfcontext.domain",
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.DOMAIN"));
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
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.USER"),
					UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.PASS"));
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

		String vData = UTAFCommonFunctions2.restUtils.vData(brokerUrl, testDataLocal,
				"Request=" + UTAFCommonFunctions2.captureRequestAndResponse(xmlValue, "xml"), "", "");
		UTAFCommonFunctions2.cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		// Connection close
		producer.close();
		session.close();
		uConnection.close();
		// System.gc();
		UTAFLog.info("All Connections closed");
		// System.exit(0);
		Thread.sleep(500);
	}

	public static void cfGlBrowseJmsMessage(String vApp, String queueName, String corrId, String lookUp)
			throws Exception {
		vApp = "."+vApp;
		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+ ".SONIC.USER"));
		env.put(Context.SECURITY_CREDENTIALS, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +vApp+ ".SONIC.PASS"));
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
		env.put(Context.PROVIDER_URL, UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.CONURL"));
		env.put("com.sonicsw.jndi.mfcontext.domain",
				UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV + vApp+".SONIC.DOMAIN"));
		InitialContext uJNDI = new InitialContext(env);
		UTAFLog.info("Context created successfully!!!");

		// create connection factory by passing the connection factory name
		ConnectionFactory conFactory = (ConnectionFactory) uJNDI.lookup(lookUp);
		UTAFLog.info("connection factory created!!!");

		// create connection by passing the user name and password
		progress.message.jclient.Connection uConnection = (progress.message.jclient.Connection) conFactory
				.createConnection(UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +vApp+ ".SONIC.USER"),
						UTAFFwVars.utafFWProps.getProperty(UTAFFwVars.utafFWENV +vApp+ ".SONIC.PASS"));
		
		
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
		while (e.hasMoreElements()) {
			TextMessage message = (TextMessage) e.nextElement();
			if (message.getJMSCorrelationID().equalsIgnoreCase(corrId)) {
				UTAFCommonFunctions2.xml = UTAFCommonFunctions2.restUtils.xmlDocument(message.getText());
				UTAFCommonFunctions2.requestpPayloadType = "xml";
				break;
			}
		}
		UTAFLog.info(
				"===============================================Response===============================================");
		UTAFLog.info(UTAFCommonFunctions2.xml);
		if (UTAFCommonFunctions2.xml.length() == 0) {
			UTAFLog.info("No response found for given correlationId in Queue Browser");
			UTAFCommonFunctions2.cfGlReportDesc(null, "FAIL", UTAFRead2.runTimeVar.get("utafFWTCStep"), false,
					"No response found for given correlationId in Queue Browser");
		}
		String vData = UTAFCommonFunctions2.restUtils.vData(brokerUrl, "", "", "", "Response=" + UTAFCommonFunctions2.captureRequestAndResponse(UTAFCommonFunctions2.xml, "xml"));
		UTAFCommonFunctions2.cfGlReportDesc(null, "PASS", UTAFRead2.runTimeVar.get("utafFWTCStep"), false, vData);
		UTAFLog.info("Done");
		browser.close();
		session.close();
		uConnection.close();
		// System.gc();
		UTAFLog.info("All Connections closed");
		// System.exit(0);
		Thread.sleep(500);
	}
	}
