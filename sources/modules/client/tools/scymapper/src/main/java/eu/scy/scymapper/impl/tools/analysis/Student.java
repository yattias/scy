package eu.scy.scymapper.impl.tools.analysis;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig.Help;

public class Student {
	
	private String directory;
	
	private String name;
	
	private IConceptMap conceptMap;
	
	private String helpMode;

	public Student(String directory, String name, String mode, IConceptMap cMap) {
		this.directory = directory;
		this.name = name;
		this.helpMode = mode;
		this.conceptMap = cMap;
	}

	public String getDirectory() {
		return directory;
	}

	public String getName() {
		return name;
	}
	public IConceptMap getConceptMap() {
		return conceptMap;
	}
	
	public String getCsvLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(directory.replace(",", ";")).append(",");
		sb.append(name.replace(",", ";")).append(",");
		sb.append(helpMode.replace(",", ";")).append(",");
		sb.append(conceptMap.getDiagram().getNodes().size()).append(",");
		sb.append(conceptMap.getDiagram().getLinks().size()).append(",");
		for(INodeModel node : conceptMap.getDiagram().getNodes()) {
			sb.append(node.getLabel().trim().replace(",", ";").replace("\n"," ").replace("\t"," ")).append(",");
		}
//		for(ILinkModel link : conceptMap.getDiagram().getLinks()) {
//			sb.append(link.getLabel().replace(",", ";")).append(",");
//		}
		return sb.toString();		
	}

}

