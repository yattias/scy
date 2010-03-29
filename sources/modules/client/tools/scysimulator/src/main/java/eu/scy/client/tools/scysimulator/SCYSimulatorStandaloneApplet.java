package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.toolbroker.ToolBrokerImpl;

import sqv.SimQuestViewer;

public class SCYSimulatorStandaloneApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054788019795975680L;
	private SimQuestViewer sqv;
	private DataCollector dataCollector;
	private ToolBrokerImpl tbi;

	public void init() {	
		JPanel simquestPanel = new JPanel();
		createSimQuestViewer();
		sqv.setContainer(simquestPanel);
		dataCollector = null;
	        //TODO remove hardcoded username/pass
		tbi = null;
		//tbi = new ToolBrokerImpl("scysim", "scysim");

		try {
			sqv.run();

			simquestPanel.setLayout(new BorderLayout());
			sqv.getInterfacePanel().setMinimumSize(sqv.getRealSize());
			simquestPanel.add(sqv.getInterfacePanel(),
					BorderLayout.CENTER);

			dataCollector = new DataCollector(sqv, tbi, "n/a");
			simquestPanel.add(dataCollector, BorderLayout.SOUTH);
			simquestPanel.add(new StandAloneMenu(dataCollector), BorderLayout.NORTH);

		} catch (java.lang.Exception e) {
			System.out
			.println("SimQuestNode.createSimQuestNode(). exception caught:");
			e.printStackTrace();

			JTextArea info = new JTextArea(4, 42);
			info.append(e.getMessage());
			simquestPanel.add(info);
		}
		
		this.getContentPane().add(simquestPanel);		
	}
	
	public void injectSimConfig(String simConfig) {
		dataCollector.setSimConfig(simConfig);
	}
	
	public String getSimConfigPrettyFormat() {
		Element element = dataCollector.getSimConfig().toXML();
		StringWriter stringWriter = new StringWriter();
		try {
			new XMLOutputter(Format.getPrettyFormat()).output(element, stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return stringWriter.toString();
	}
	
	public String getSimConfigCompactFormat() {
		Element element = dataCollector.getSimConfig().toXML();
		StringWriter stringWriter = new StringWriter();
		try {
			new XMLOutputter(Format.getCompactFormat()).output(element, stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return stringWriter.toString();
	}

	private void createSimQuestViewer() {
		{
			 /**
			  * Run as secure (default) or unsecure applet.
			  */
			 boolean m_hasSecurity = false;
			
			 //Check the secure parameter
			 String security = getParameter("security");
			 if (security != null)
			 {
				 m_hasSecurity = Boolean.valueOf(security);
			 }
			 
			 //Create SimQuestViewer instance
			 sqv = new SimQuestViewer(m_hasSecurity);
			 
			 //Check of obligatory file parameter
			 String file = getParameter("file");
			 if (file == null)
			 {
				 System.err.println("No valid file parameter specified exiting");
				 System.exit(ERROR);
			 }

			 //Validate the file URI
			 try 
			 {
				 //Fix for spaces...
				 file = file.replace(" ", "%20");
				 
				 URI fileURI = new URI(file);
				
				if ( fileURI.isAbsolute() == false)
				{
					//Append the code base and try again
					fileURI = new URI(getCodeBase() + file);
					
					if ( fileURI.isAbsolute() == true)
					{
						file = fileURI.toString();
					}
					else
					{
					    System.err.println("No valid file URI could be created");
					    System.exit(ERROR);
					}
				}
				
				 //Set the file (URI)
				 sqv.setFile(fileURI);
				 System.out.println("File : " + file);
			 } 
			 catch (URISyntaxException e) 
			 {
				e.printStackTrace();
			    System.err.println("No valid file URI could be created");
			    System.exit(ERROR);
			 }
			 
			 //Check for optional scy-server parameter
			String  scyServer = getParameter("scy-server"); 
	      	if (scyServer != null)
	      	{
	      			sqv.setScyServer(scyServer);
	      	}

			//Check for optional user parameter
			String user = getParameter("user"); 
	      	if (user != null)
	      	{
	      		sqv.setUser(user);
	      	}

	      	sqv.createFrame(false);	      	
	    }
	}
}
