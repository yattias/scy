package eu.scy.agents.conceptmap.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptMapUser {
	
	private String userName;
	
	//TODO HashMap
	private Set<ConceptMapModel> models;

	/**
	 * Constructs a new user with an empty list
	 * @param userName
	 * @param model
	 */
	public ConceptMapUser(String userName){
		this.userName = userName;
		models = new HashSet<ConceptMapModel>();
	}
	
	public String getUsername() {
		return userName;
	}

	/**
	 * Returns a users ConceptMapModel with the given EloURI.
	 * Returns null if no model is present. 
	 * @param eloUri
	 * @return
	 */
	public ConceptMapModel getModel(String eloUri) {
		for(ConceptMapModel model: getModels()){
			if(model.getEloURI().equals(eloUri)){
				return model;
			}
		}
		return null;
	}

	public Set<ConceptMapModel> getModels() {
		return models;
	}

	public void addConceptMapModel(ConceptMapModel model){
		models.add(model);
	}
}
