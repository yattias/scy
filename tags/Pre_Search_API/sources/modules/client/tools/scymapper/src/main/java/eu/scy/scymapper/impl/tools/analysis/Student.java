package eu.scy.scymapper.impl.tools.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import eu.scy.actionlogging.ActionXMLTransformer;
import eu.scy.actionlogging.api.IAction;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.logging.*;

public class Student {
	
	private File directory;
	
	private String name;
	
	private IConceptMap conceptMap;
	
	private String helpMode;
	
	private IAction[] actions;

	public Student(File directory, String name, String mode, IConceptMap cMap) {
		this.directory = directory;
		this.name = name;
		this.helpMode = mode;
		this.conceptMap = cMap;
		this.actions = null;
	}

	public File getDirectory() {
		return directory;
	}

	public String getName() {
		return name;
	}
	
	public String getHelpMode() {
		return helpMode;
	}
	
	public IConceptMap getConceptMap() {
		return conceptMap;
	}
	
	public String getActionLogFile(String actionLogPath) {
		return actionLogPath + "actions_" + this.helpMode.toLowerCase() + ".xml";
	}

	public String getCsvLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(directory.toString().replace(",", ";")).append(",");
		sb.append(name.replace(",", ";")).append(",");
		sb.append(helpMode.replace(",", ";")).append(",");
		sb.append(conceptMap.getDiagram().getNodes().size()).append(",");
		sb.append(conceptMap.getDiagram().getLinks().size()).append(",");
		sb.append(getLexiconCalledCount()).append(",");
		sb.append(getConceptHelpRequestCount()).append(",");
		sb.append(getRelationHelpRequestCount()).append(",");
		sb.append(getSynonymAddedCount()).append(",");
		for(INodeModel node : conceptMap.getDiagram().getNodes()) {
			sb.append(node.getLabel().trim().replace(",", ";").replace("\n"," ").replace("\t"," ")).append(",");
		}
//		for(ILinkModel link : conceptMap.getDiagram().getLinks()) {
//			sb.append(link.getLabel().replace(",", ";")).append(",");
//		}
		return sb.toString();		
	}
	
	public int getLexiconCalledCount() {
		return getTypeCount(ConceptMapActionLoggerCollide.LEXICON_SWITCHED);
	}
	
	public int getSynonymAddedCount() {
		if(this.helpMode.equals(SCYMapperStandaloneConfig.Help.NOHELP.toString())) {
			return -1;
		}
		return getTypeCount(ConceptMapActionLoggerCollide.SYNONYM_ADDED);
	}
	
	public int getConceptHelpRequestCount() {
		if(!this.helpMode.equals(SCYMapperStandaloneConfig.Help.VOLUNTARY.toString())) {
			return -1;
		}
		return getTypeCount(ConceptMapActionLoggerCollide.REQUEST_CONCEPT);
	}
	
	public int getRelationHelpRequestCount() {
		if(!this.helpMode.equals(SCYMapperStandaloneConfig.Help.VOLUNTARY.toString())) {
			return -1;
		}
		return getTypeCount(ConceptMapActionLoggerCollide.REQUEST_RELATION);
	}
	
	public int getTypeCount(String type) {
		int typeCount = 0;
		for(IAction action : actions) {
			if(action.getType().equals(type)) {
				typeCount++;
			}
		}
		return typeCount;
	}
	
	
	public int readActionLogs(String actionLogDir) throws DocumentException, IOException {
	    ArrayList<IAction> result = new ArrayList<IAction>();
	    BufferedReader br = new BufferedReader(new FileReader(getActionLogFile(actionLogDir)));
	    String buffer = null;
	    while ((buffer = br.readLine()) != null) {
	        Document doc = DocumentHelper.parseText(buffer);
	        ActionXMLTransformer axt = new ActionXMLTransformer(doc.getRootElement());
	        String userName = axt.getActionAsElement().attributeValue("user");
	        if(userName != null && userName.equalsIgnoreCase(this.name))
	        result.add(axt.getActionAsPojo());
	    }
	    br.close();
	    this.actions = (IAction[]) result.toArray(new IAction[result.size()]);
	    return this.actions.length;
	}	

}

