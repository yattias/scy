/*
 * Created on 14.jul.2005
 *
 * 
 */
package eu.scy.colemo.server.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author Øystein
 */
public class ManageUsers {
    private Document userDoc;
    private Document agentDoc;
	private File xmlFile;
	private boolean first;
	
	public ManageUsers() {
	    try{
			xmlFile=new File("users.xml");
			xmlFile.createNewFile();
			userDoc=createXMLDocumentStructure();
			//agentDoc = readFromXMLFile(new File("agent.xml"));
			first=true;
		}
	    catch(IOException io){
			System.out.println(io.getMessage());
            io.printStackTrace();
		}
	    catch(Exception de){
	        System.out.println(de.getMessage());
            de.printStackTrace();
	    }
	}
	
	public void writeToXML(String user,String type,InetAddress ip, long time){
	    try{
	        writeXML(user,type,ip,time,xmlFile);
	    }
	    catch(IOException io){
			System.out.println(io.getMessage());
            io.printStackTrace();
		}
		catch(DocumentException docu){
			System.out.println(docu);
            docu.printStackTrace();
		}
	}
			
	public Document readFromXMLFile(File xmlFile) throws DocumentException{
		SAXReader reader;
		reader=new SAXReader();
		return reader.read(xmlFile);
	}
	
	private void writeXML(String user,String type,InetAddress ip,long time,File xmlFile)throws IOException,DocumentException{
		
	
			Element rootNode=userDoc.getRootElement();
		
			if(first){
			    Element allUsers = rootNode.addElement("allUsers");
			    Element totalPointsAll = allUsers.addElement("totalPointsAll");
			    totalPointsAll.addText("0");
			    Element totalActions = allUsers.addElement("totalActions");
			    totalActions.addText("0");
			
			    first=false;
			}
		
			Element userNode=rootNode.addElement("user");
			
				Element nameNode=userNode.addElement("name");
				nameNode.addText(user);
				Element userIpNode=userNode.addElement("ip");
				userIpNode.addText(ip.toString());
				Element timeLoggedOnNode=userNode.addElement("time_logged_on");
			    timeLoggedOnNode.addText(new Long(time).toString());
				Element chatNode=userNode.addElement("chat");
				chatNode.addText("0");
				Element addClassNode=userNode.addElement("addClass");
				addClassNode.addText("0");
				Element addFieldNode=userNode.addElement("addField");
				addFieldNode.addText("0");
				Element addMethodNode=userNode.addElement("addMethod");
				addMethodNode.addText("0");
				Element addLinkNode=userNode.addElement("addLink");
				addLinkNode.addText("0");
				Element deleteClassNode=userNode.addElement("deleteClass");
				deleteClassNode.addText("0");
				Element deleteFieldNode=userNode.addElement("deleteField");
				deleteFieldNode.addText("0");
				Element deleteMethodNode=userNode.addElement("deleteMethod");
				deleteMethodNode.addText("0");
				Element deleteLinkNode=userNode.addElement("deleteLink");
				deleteLinkNode.addText("0");
				Element moveNode=userNode.addElement("move");
				moveNode.addText("0");
				Element relevantChatNode=userNode.addElement("relevantChat");
				relevantChatNode.addText("0");
				Element totalActions =userNode.addElement("totalActions");
				totalActions.addText("0");
				Element lastActionRegistred =userNode.addElement("lastAction");
				lastActionRegistred.addText(new Long(time).toString());
				Element idleMessages =userNode.addElement("idleMessages");
				idleMessages.addText("0");
				Element lastIdleMessage = userNode.addElement("lastIdleMessage");
				lastIdleMessage.addText("0");
				Element notEnoughPrMinute = userNode.addElement("notEnoughPrMinute");
				notEnoughPrMinute.addText("0");
				Element lastNotEnoughPrMinute = userNode.addElement("lastNotEnoughPrMinute");
				lastNotEnoughPrMinute.addText("0");
				Element notEnoughComparedToRest = userNode.addElement("notEnoughComparedToRest");
				notEnoughComparedToRest.addText("0");
				Element lastNotEnoughComparedToRest = userNode.addElement("lastNotEnoughComparedToRest");
				lastNotEnoughComparedToRest.addText("0");
				Element tooMuchComparedToRest = userNode.addElement("tooMuchComparedToRest");
				tooMuchComparedToRest.addText("0");
				Element lastTooMuchComparedToRest = userNode.addElement("lastTooMuchComparedToRest");
				lastTooMuchComparedToRest.addText("0");
				Element totalPoints =userNode.addElement("totalPoints");
				totalPoints.addText("0");
				
		FileOutputStream xmlFileout=new FileOutputStream(xmlFile);
		writeXMLToFile(xmlFileout);
	}	
	
	public void writeXMLToFile(OutputStream out) throws IOException{
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding("ISO-8859-1");

		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(this.userDoc);
		writer.flush();
		out.close();
	}

	public void updateUser(String type,String user,long time){
	    /*String a ="//user_information/user";
	   
	    List list = userDoc.selectNodes(a);
	  
	    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
	        Element element = (Element) iter.next();
	  
	        
	        if(element.valueOf("name").equals(user)){
	            Element e = element.element(type);
	            String x = e.getText();
	            
	            //Oppdaterer variabel
	            int y = Integer.parseInt(x)+1;
		        Integer z = new Integer(y);
		        e.setText(z.toString());
		        
		        if(type.equals("idleMessages")){
		            e=element.element("lastIdleMessage");
			        e.setText(new Long(time).toString());
		        }
		        else if(type.equals("notEnoughPrMinute")){
		            e=element.element("lastNotEnoughPrMinute");
			        e.setText(new Long(time).toString());
		        }
		        else if(type.equals("notEnoughComparedToRest")){
		            e=element.element("lastNotEnoughComparedToRest");
		            e.setText(new Long(time).toString());
		        }
		        else if(type.equals("tooMuchComparedToRest")){
		            e=element.element("lastTooMuchComparedToRest");
		            e.setText(new Long(time).toString());
		        }
		        else{
		            e=element.element("lastAction");
			        e.setText(new Long(time).toString());
		        }
		        
		        //****************START**************************
		        //Oppdaterer totalPoints
		       
		       int points = getPoints(type);
		       e=element.element("totalPoints");
		       int newWeight= Integer.parseInt(e.getText())+points;
		       e.setText(new Integer(newWeight).toString());
		       
		       e=element.element("totalActions");
		       if(points>0){
		           int newTotal = Integer.parseInt(e.getText())+1;
			       e.setText(new Integer(newTotal).toString());
		       }
		         
		       updateTotalPoints(points); 
		       //******************TIL HIT******************************
		        try{
		            FileOutputStream xmlFileout=new FileOutputStream(xmlFile);
		            writeXMLToFile(xmlFileout);
		        }
		        catch (IOException io){
			        System.out.println(io.getMessage());
                    io.printStackTrace();
			    }
		        break;
	        }  
	    }
	    */
	}
	
	/**
	 * Creates the document if it has not been created before.
	 * @return
	 */
	public Document createXMLDocumentStructure() {
		Document document = DocumentHelper.createDocument();
		//document.addProcessingInstruction("xml-stylesheet type=\"text/css\"","href=\"xml.css\"");	
		document.addComment("This file stores info about every user");
	
		Element root = document.addElement("user_information");
		return document;
	  }
	public Document getDocument() {
        return userDoc;
    }
	public int getPoints(String type){
	    String path="//eu.scy.colemo.server.agent/points/"+type;
        List agentList = agentDoc.selectNodes(path);
        for (Iterator it = agentList.iterator(); it.hasNext(); ) {
	        Element el = (Element) it.next();
	        String value = el.getStringValue();
	        int points =Integer.parseInt(el.getStringValue());
	        return points;
        }
        return 0;
	}
	
	public void updateTotalPoints(int points){
	    String path;
	    List list;
	    path="//user_information/allUsers/totalPointsAll";
        list = userDoc.selectNodes(path);
        for (Iterator it = list.iterator(); it.hasNext(); ) {
	        Element el = (Element) it.next();
	    
	        int totalPoints =Integer.parseInt(el.getStringValue())+points;
		    el.setText(new Integer(totalPoints).toString());
	   }
       if(points>0){
           path="//user_information/allUsers/totalActions";
           list = userDoc.selectNodes(path);
           for (Iterator it = list.iterator(); it.hasNext(); ) {
               Element el = (Element) it.next();
   	    
               int totalActions =Integer.parseInt(el.getStringValue())+1;
   		       el.setText(new Integer(totalActions).toString());	
           }
       }
	}
}
