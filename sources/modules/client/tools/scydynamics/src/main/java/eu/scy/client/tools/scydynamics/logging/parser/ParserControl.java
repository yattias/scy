package eu.scy.client.tools.scydynamics.logging.parser;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.jfree.ui.RefineryUtilities;

import eu.scy.actionlogging.ActionXMLTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.logging.parser.actions.ActionMatrixAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.FeedbackTimelineAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.FilterMissionAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.FilterTimeAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.ModelScoreAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.ReleaseFilterAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.ShowStatisticsAction;
import eu.scy.client.tools.scydynamics.logging.parser.actions.ShowTermsAction;

public class ParserControl implements ActionListener, Runnable {

	private ParserView view;
	private ParserModel model;
	private ArrayList<File> files;
	private int i;
	private Domain domain;
	
	public ParserControl(ParserModel model, ParserView view, Domain domain) {
		this.view = view;
		this.model = model;
		this.domain = domain;
		setActions();
	}
	
	private void setActions() {
		view.setActionListener(this);
		view.termsButton.setAction(new ShowTermsAction(view, model, domain));
		view.filterTimeButton.setAction(new FilterTimeAction(view, model, domain));
		view.filterMissionButton.setAction(new FilterMissionAction(view, model, domain));
		view.statisticsButton.setAction(new ShowStatisticsAction(view, model, domain));
		view.actionMatrixButton.setAction(new ActionMatrixAction(view, model, domain));
		view.releaseFilterButton.setAction(new ReleaseFilterAction(view, model, domain));
		view.feedbackButton.setAction(new FeedbackTimelineAction(view, model, domain));
		view.scoreButton.setAction(new ModelScoreAction(view, model, domain));
	}
	
	private void chooseFolder() {
		JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("choose folder with action log files");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
	    	model.setDirectory(chooser.getCurrentDirectory());
	    	view.updateView();
	    } else {
	    	// no selection, doing nothing
	    }
	}
	
	private void collectFiles(File directory, ArrayList<File> files) {
		String[] list = directory.list();
		File file;
		for (String fileName: list) {
			file = new File(directory+System.getProperty("file.separator")+fileName);
			//file = new File(directory+"\\"+fileName);
			System.out.println("parsing "+fileName);
			if (file.isDirectory()) {
				// recursively go deeper...
				collectFiles(file, files);
			} else if (fileName.endsWith("txt")) {
				// add file to list
				//files.add(new File(directory+"\\"+fileName));
				files.add(new File(directory+System.getProperty("file.separator")+fileName));
			}
		}
	}
	
	private void parseFiles(ArrayList<File> files) {
		for (i=0; i<files.size(); i++) {
			try {
				XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
				xmlInputFactory.setProperty("javax.xml.stream.isValidating", false);
			    xmlInputFactory.setProperty("javax.xml.stream.supportDTD", false);
				XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new InputStreamReader(new FileInputStream(files.get(i))));				
			    parseFile(xmlStreamReader);
			    SwingUtilities.invokeLater(new Runnable() {
				    public void run() {
				    	view.progressBar.setValue(i);
				    }
				  });	
			} catch (Exception e) {
				System.out.println("exception caught for file: "+files.get(i).getAbsolutePath());
				System.out.println(e.getMessage());
			}
		}
		model.setBackupPoint();
		view.updateView();
	}
	
	private static IAction parseActionElement(XMLStreamReader reader) throws XMLStreamException, TransformerException, DocumentException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer t = tf.newTransformer();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    StAXSource source = new StAXSource(reader);
	    t.transform(source, new StreamResult(outputStream));
		Document actionDocument = DocumentHelper.parseText(outputStream.toString());
		ActionXMLTransformer actionTransformer = new ActionXMLTransformer(actionDocument.getRootElement());
		return actionTransformer.getActionAsPojo();
	}
	
	private void parseFile(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		boolean first = true;
		IAction parsedAction;
		while (xmlStreamReader.hasNext()) {
	        int eventType = xmlStreamReader.getEventType();
	        if (eventType == XMLStreamConstants.START_ELEMENT) {
	            String elementName = xmlStreamReader.getName().getLocalPart();
	            if (!elementName.toLowerCase().equals("action")) {
	                xmlStreamReader.next();
	                continue;
	            }
	            try {
	            	parsedAction = parseActionElement(xmlStreamReader);
	            	if (first) {
	            		// workaround to identify the beginning of a file
	            		first = false;
	            		IAction firstAction = new Action();
	            		firstAction.setType(ModellingLogger.START_APPLICATION);
	            		firstAction.setTimeInMillis(parsedAction.getTimeInMillis()-1);
	            		firstAction.setUser(parsedAction.getUser());
	            		firstAction.addContext(ContextConstants.tool, parsedAction.getContext(ContextConstants.tool));
	            		firstAction.addContext(ContextConstants.eloURI, parsedAction.getContext(ContextConstants.eloURI));
	            		firstAction.addContext(ContextConstants.mission, parsedAction.getContext(ContextConstants.mission));
	            		firstAction.addContext(ContextConstants.session, parsedAction.getContext(ContextConstants.session));
	            		model.addAction(firstAction);
	            	}
					model.addAction(parsedAction);
				} catch (Exception e) {
					System.out.println("exception caught while parsing action-element");
					System.out.println(e.getMessage());
				}
	        } else {
	            xmlStreamReader.next();
	        }
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("folder")) {
			chooseFolder();
		} else if (e.getActionCommand().equals("go")) {
			Thread t = new Thread(this);
			t.start();		
		} else if (e.getActionCommand().equals(ModellingLoggerFES.TERM_NOT_RECOGNIZED)) {
			findActions(ModellingLoggerFES.TERM_NOT_RECOGNIZED);
		} else if (e.getActionCommand().equals(ModellingLoggerFES.TERM_NOT_RECOGNIZED_PROPOSALS)) {
			findActions(ModellingLoggerFES.TERM_NOT_RECOGNIZED_PROPOSALS);
		}
	}



	private void findActions(String type) {
		ArrayList<IAction> actions = model.getAction(type);
		view.addInfo("");
		view.addInfo("found "+actions.size()+" action of type "+type+":");
		for(IAction action: actions) {
			String info = "user: "+action.getUser()+", condition: "+action.getContext(ContextConstants.mission)+", not recognized term: "+action.getAttribute("term");
			view.addInfo(info);
		}
		
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	view.startProgressBar();
		    }
		  });
		
		files = new ArrayList<File>();
		collectFiles(new File(model.getDirectory()), files);
		System.out.println("collected "+files.size()+" logfiles.");
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	view.progressBar.setMinimum(0);
				view.progressBar.setMaximum(files.size());
				view.progressBar.setIndeterminate(false);
		    }
		  });
		
		parseFiles(files);
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	view.stopProgressBar();
		    	view.addInfo("found "+model.getUserModels().size()+" users with "+model.getActions().size()+" actions in "+files.size()+" files.");
		    }
		  });
	}


}
