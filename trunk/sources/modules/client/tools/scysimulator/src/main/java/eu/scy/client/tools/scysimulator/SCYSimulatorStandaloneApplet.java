package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
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
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.JDOMException;
import sqv.SimQuestViewer;

public class SCYSimulatorStandaloneApplet extends JApplet {

    private final static Logger LOGGER = Logger.getLogger(DataCollector.class.getName());
    private SimQuestViewer sqv;
    private DataCollector dataCollector;
    private ToolBrokerImpl tbi;

    @Override
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
	    LOGGER.warning("exception caught:");
	    e.printStackTrace();
	    JTextArea info = new JTextArea(4, 42);
	    info.append(e.getMessage());
	    simquestPanel.add(info);
	}

	this.getContentPane().add(simquestPanel);
    }

    public void injectSimConfig(String simConfig) {
	try {
	    SimConfig config = new SimConfig(simConfig);
	    dataCollector.setVariableValues(config.getVariables());
	} catch (JDOMException ex) {
	    JOptionPane.showMessageDialog(this, "Could not parse the SimConfig; the current simulation will not be changed.", "Parsing problem", JOptionPane.WARNING_MESSAGE);
	}
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
	    if (security != null) {
		m_hasSecurity = Boolean.valueOf(security);
	    }

	    //Create SimQuestViewer instance
	    sqv = new SimQuestViewer(m_hasSecurity);

	    //Check of obligatory file parameter
	    String file = getParameter("file");
	    if (file == null) {
		System.err.println("No valid file parameter specified exiting");
		System.exit(ERROR);
	    }

	    //Validate the file URI
	    try {
		//Fix for spaces...
		file = file.replace(" ", "%20");

		URI fileURI = new URI(file);

		if (fileURI.isAbsolute() == false) {
		    //Append the code base and try again
		    fileURI = new URI(getCodeBase() + file);

		    if (fileURI.isAbsolute() == true) {
			file = fileURI.toString();
		    } else {
			System.err.println("No valid file URI could be created");
			System.exit(ERROR);
		    }
		}

		//Set the file (URI)
		sqv.setFile(fileURI);
	    } catch (URISyntaxException e) {
		e.printStackTrace();
		System.err.println("No valid file URI could be created");
		System.exit(ERROR);
	    }

	    //Check for optional scy-server parameter
	    String scyServer = getParameter("scy-server");
	    if (scyServer != null) {
		sqv.setScyServer(scyServer);
	    }

	    //Check for optional user parameter
	    String user = getParameter("user");
	    if (user != null) {
		sqv.setUser(user);
	    }

	    sqv.createFrame(false);
	}
    }
}
