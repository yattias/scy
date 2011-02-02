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

/*
 * Created on Jan 24, 2005
 */
/**
 * @author Øystein og Roger
 */

public class XMLActionLogWriter {
	private Document doc;
	private File xmlFile;
	public XMLActionLogWriter() {
	    try{
			//xmlFile=new File("C:\\Documents and Settings\\Roger\\Skrivebord\\actions.xml");
			xmlFile=new File("actions.xml");
		
				xmlFile.createNewFile();
				doc=createXMLDocumentStructure();

		}
	    catch(IOException io){
			System.out.println(io.getMessage());
            io.printStackTrace();
		}
	/**	catch(DocumentException docu){
			System.out.println(docu);
		}*/
	}
	public void writeActionToXML(String type,String madeBy,String name,InetAddress ip,long time){
	    try{
	        writeXML(type,madeBy,name,ip,xmlFile,time);
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
	
	private void writeXML(String type,String madeBy,String name,InetAddress ip,File xmlFile,long time)throws IOException,DocumentException{
		
		Element rootNode=doc.getRootElement();
			Element actionNode=rootNode.addElement(type);
				Element madeByNode=actionNode.addElement("made_by");
				madeByNode.addText(madeBy);
				Element nameNode=actionNode.addElement("name");
				nameNode.addText(name);
				Element ipNode=actionNode.addElement("ip");
				ipNode.addText(ip.toString());
				Element timeNode=actionNode.addElement("time");
				timeNode.addText(new Long(time).toString());
		FileOutputStream xmlFileout=new FileOutputStream(xmlFile);
		writeXMLToFile(xmlFileout);
	}	
   
	public String findStuff(String newClass){
	    List list = doc.selectNodes("//contribution/chat/name");
	    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
	        Element element = (Element) iter.next();
	        String url = element.getStringValue().toLowerCase();
	        if(url.indexOf(newClass.toLowerCase())>-1){
	            return element.getParent().valueOf("made_by");            
	        }
	       
	    }
	    return null;
	}
	
	
	public void writeXMLToFile(OutputStream out) throws IOException{
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding("ISO-8859-1");
		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(this.doc);
		writer.flush();
		out.close();
		
	}
	

	/**
	 * Creates the document if it has not been created before.
	 * @return
	 */
	public Document createXMLDocumentStructure() {
		Document document = DocumentHelper.createDocument();
		document.addComment("This file stores a log of all eu.scy.colemo.server.contributions");
		Element root = document.addElement("contribution");
		return document;
	  }
	
	
}
