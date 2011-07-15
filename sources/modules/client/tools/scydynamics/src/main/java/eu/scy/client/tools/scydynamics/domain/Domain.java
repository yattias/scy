package eu.scy.client.tools.scydynamics.domain;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class Domain {

	private final static Logger LOGGER = Logger.getLogger(Domain.class.getName());
	
	private ReferenceModel referenceModel;
	private ConceptSet conceptSet;
	private SimulationSettings simulationSettings;
	private HashMap<String, String> termConceptMap;
	
	public Domain(String referenceModelFilename, String concepSetFilename, String simulationSettingsFilename) throws Exception {
		this.referenceModel = (ReferenceModel) unmarshal(referenceModelFilename);
		this.conceptSet = (ConceptSet) unmarshal(concepSetFilename);
		this.simulationSettings = (SimulationSettings) unmarshal(simulationSettingsFilename);
		getTermConceptMap();
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
	
	private HashMap<String, String> getTermConceptMap() {
		if (termConceptMap == null) {
			termConceptMap = new HashMap<String, String>();
			Concept concept;
			for (Node node: referenceModel.getNodes()) {
				concept = conceptSet.getConcept(node.getConcept());
				if (concept != null) {
					for (Term term: concept.getTerms()) {
						termConceptMap.put(term.getTerm(), concept.getName());
					}
				}
			}
		}
		return termConceptMap;
	}
	
	private Object unmarshal(String fileName) throws Exception {
		Object returnObject = null;
		JAXBContext context = JAXBContext.newInstance(ReferenceModel.class, ConceptSet.class, SimulationSettings.class); 
		Unmarshaller um = context.createUnmarshaller();
		File f = new File(fileName);
		LOGGER.info("loading from: "+f.getAbsolutePath());
		LOGGER.info("file exists: "+f.exists());
		returnObject = um.unmarshal(new FileReader(fileName));
		return returnObject;
	}
	
	
	/**
	 * @param name The name that will be used to look for proposals.
	 * @return Returns a list of similar names from the current Domain (with Levenshtein-Distance
	 * < 5). If the given name can be found in the domain as is, null is returned.
	 */
	public List<String> proposeNames(String name) {
		ArrayList<String> proposed = new ArrayList<String>();
		if (getTermConceptMap().keySet().contains(name)) {
			return null;
		}
		for (Entry<String, String> entry: getTermConceptMap().entrySet()) {
			if (DomainUtils.getLevenshteinDistance(entry.getKey(), name)<5) {
				proposed.add(entry.getKey());
			}
		}
		return proposed;
	}
	
	public String getConceptByTerm(String term) {
		return getTermConceptMap().get(term);
	}
	
	
	public Node getNodeByConcept(Concept concept) {
		return getNodeByConcept(concept.getName());
	}
	
	public Node getNodeByConcept(String concept) {
		LOGGER.info("concept: "+concept);
		if (concept == null) return null;
		for (Node node: referenceModel.getNodes()) {
			if (node.getConcept().equals(concept)) {
				return node;
			}
		}
		return null;
	}
	
}
