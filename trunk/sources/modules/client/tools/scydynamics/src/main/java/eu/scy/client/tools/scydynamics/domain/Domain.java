package eu.scy.client.tools.scydynamics.domain;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class Domain {

	private final static Logger debugLogger = Logger.getLogger(Domain.class.getName());
	
	private final ReferenceModel referenceModel;
	private final ConceptSet conceptSet;
	private final SimulationSettings simulationSettings;
	private final HashMap<String, String> termConceptMap;
	
	public Domain(String referenceModelFilename, String concepSetFilename, String simulationSettingsFilename) throws Exception {
		this.referenceModel = (ReferenceModel) unmarshal(referenceModelFilename);
		this.conceptSet = (ConceptSet) unmarshal(concepSetFilename);
		this.simulationSettings = (SimulationSettings) unmarshal(simulationSettingsFilename);
		termConceptMap = createTermConceptMap();
		debugLogger.info("domain loaded.");
		debugLogger.info("concepts in concept set: "+conceptSet.getConcepts());
		debugLogger.info("nodes in reference model: "+referenceModel.getNodes());
		debugLogger.info("edges in reference model: "+referenceModel.getEdges().size());
	}
	
	public ReferenceModel getReferenceModel() {
		return referenceModel;
	}
	
	public ConceptSet getConceptSet() {
		return conceptSet;
	}
	
	public SimulationSettings getSimulationSettings() {
		return simulationSettings;
	}
	
	private HashMap<String, String> createTermConceptMap() {
		HashMap<String, String> returnMap = new HashMap<String, String>();
			for (Concept concept: conceptSet.getConcepts()) {			
				if (concept != null) {
					for (Term term: concept.getTerms()) {
						if (returnMap.containsKey(term.getTerm())) {
							debugLogger.warning("duplicate term: "+term+" present in concepts "+concept+" + "+returnMap.get(term.getTerm()));
						}
						returnMap.put(term.getTerm(), concept.getName());
					}
				}
			}
		return returnMap;
	}
	
	private Object unmarshal(String fileName) throws Exception {
		Object returnObject = null;
		JAXBContext context = JAXBContext.newInstance(ReferenceModel.class, ConceptSet.class, SimulationSettings.class); 
		Unmarshaller um = context.createUnmarshaller();
		InputStream stream = getClass().getResourceAsStream(fileName);
		if (stream != null) {
			debugLogger.info("loading from stream: "+fileName);
			returnObject = um.unmarshal(stream);
		} else {
			File f = new File(fileName);
			debugLogger.info("loading from: "+f.getAbsolutePath());
			debugLogger.info("file exists: "+f.exists());
			returnObject = um.unmarshal(new FileReader(fileName));
		}
		return returnObject;
	}
	
	/**
	 * @param name The name that will be used to look for proposals.
	 * @return Returns a list of similar names from the current Domain (with Levenshtein-Distance
	 * < 5). If the given name can be found in the domain as is, null is returned.
	 */
	public List<String> proposeNames(String name) {
		if (termConceptMap.keySet().contains(name)) {
			return null;
		}
		ArrayList<String> proposed = new ArrayList<String>();
		for (Entry<String, String> entry: termConceptMap.entrySet()) {
			if (DomainUtils.getLevenshteinDistance(entry.getKey(), name)<5) {
				proposed.add(entry.getKey());
			}
		}
		return proposed;
	}
	
	public String getConceptNameByTerm(String term) {
		return termConceptMap.get(term);
	}
	
	public Concept getConceptByTerm(String term) {
		if (term == null) {
			return null;
		} else {
			String conceptName = getConceptNameByTerm(term);
			if (conceptName == null) {
				return null;
			}
			for (Concept concept: this.getConceptSet().getConcepts()) {
				if (concept.getName().toLowerCase().equals(conceptName.toLowerCase())) {
					return concept;
				}
			}
			return null;
		}
	}
	
	public Node getNodeByConcept(Concept concept) {
		return getNodeByConcept(concept.getName());
	}
	
	public Node getNodeByConcept(String concept) {
		if (concept == null) return null;
		for (Node node: referenceModel.getNodes()) {
			if (node.getConcept().equals(concept)) {
				return node;
			}
		}
		return null;
	}
	
	public Concept getConceptByName(String conceptName) {
		Iterator<Concept> itr = conceptSet.getConcepts().iterator();
		while (itr.hasNext()) {
			Concept concept = itr.next();
			if (concept.getName().toLowerCase().equals(conceptName.toLowerCase())) {
				return concept;
			}
		}
		return null;
	}

	public Edge getEdgeBetweenNodes(Node fromNode, Node toNode) {
		Edge returnEdge = null;
		try {
			for (Edge edge: referenceModel.getEdges()) {
				if (edge.getFrom().equals(fromNode.getId()) && edge.getTo().equals(toNode.getId())) {
					returnEdge = edge;
				}
			}
		} catch (Exception ex) {
			//debugLogger.warning(ex.getMessage());
		}
		return returnEdge;		
	}
	
	public Edge getEdgeBetweenNodeIds(String fromNodeId, String toNodeId) {
		Edge returnEdge = null;
		try {
			for (Edge edge: referenceModel.getEdges()) {
				if (edge.getFrom().equals(fromNodeId) && edge.getTo().equals(toNodeId)) {
					returnEdge = edge;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnEdge;		
	}
	
	public Edge getEdgeBetweenNodeTerms(String fromNodeTerm, String toNodeTerm) {
		String fromConcept = getConceptNameByTerm(fromNodeTerm);
		String toConcept = getConceptNameByTerm(toNodeTerm);
		Node fromNode = getNodeByConcept(fromConcept);
		Node toNode = getNodeByConcept(toConcept);	
		return getEdgeBetweenNodes(fromNode, toNode);
	}
	
}
