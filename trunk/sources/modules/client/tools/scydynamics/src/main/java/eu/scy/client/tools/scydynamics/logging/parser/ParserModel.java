package eu.scy.client.tools.scydynamics.logging.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;

public class ParserModel {

	private File logDirectory;
	// users are organized by username in this hashmap
	private HashMap<String, UserModel> userModels;
	private HashMap<String, UserModel> backupUserModels;
	private Domain domain;
	
	public ParserModel(Domain domain) {
		this.domain = domain;
		userModels = new HashMap<String, UserModel>();
		backupUserModels = new HashMap<String, UserModel>();
	}
	
	public void releaseFilters() {
		userModels = (HashMap<String, UserModel>) backupUserModels.clone();
		for (UserModel userModel: userModels.values()) {
			userModel.releaseFilters();
		}
	}
	
	public void setBackupPoint() {
		this.backupUserModels = (HashMap<String, UserModel>) userModels.clone();	
		for (UserModel userModel: userModels.values()) {
			userModel.setBackupPoint();
		}
	}
	
	public void filterForTime(long start, long end) {
		for (UserModel userModel: userModels.values()) {
			userModel.filterForTime(start, end);
		}
		cleanModel();
	}

	private void cleanModel() {
		HashMap<String, UserModel> userModelsClone = (HashMap<String, UserModel>) userModels.clone();
		for (UserModel userModel: userModelsClone.values()) {
			if (userModel.getActions().isEmpty()) {
				userModels.remove(userModel.getUserName());
			}
		}
	}

	public void setDirectory(File logDirectory) {
		this.logDirectory = logDirectory;
	}

	public String getDirectory() {
		return logDirectory.toString();
	}
	
	public HashMap<String, UserModel> getUserModels() {
		return this.userModels;
	}
	
	public void addAction(IAction newAction) {
		// add action to user-specific action list
		if (userModels.get(newAction.getUser()) == null) {
			// user does not exist yet -> create
			userModels.put(newAction.getUser(), new UserModel(newAction.getUser(), domain));
		}
		userModels.get(newAction.getUser()).addAction(newAction);
	}
	
	public ArrayList<IAction> getAction(String type) {
		ArrayList<IAction> returnActions = new ArrayList<IAction>();
		for (UserModel userModel: userModels.values()) {
			for (IAction action: userModel.getActions()) {
				if (action.getType().equalsIgnoreCase(type)) {
					returnActions.add(action);
				}
			}
		}
		return returnActions;
	}
	
	public ArrayList<IAction> getActions() {
		ArrayList<IAction> returnActions = new ArrayList<IAction>();
		for (UserModel userModel: userModels.values()) {
			returnActions.addAll(userModel.getActions());
		}
		return returnActions;
	}

	public UserModel getUserModel(String userName) {
		return userModels.get(userName);
	}

	public IAction getEarliestAction() {
		long timestamp = Long.MAX_VALUE;
		IAction action = null;
		for (UserModel userModel: userModels.values()) {
			if (userModel.getFirstAction().getTimeInMillis() < timestamp) {
				action = userModel.getFirstAction();
			}
		}
		return action;
	}
	
	public IAction getLatestAction() {
		long timestamp = Long.MIN_VALUE;
		IAction action = null;
		for (UserModel userModel: userModels.values()) {			
			if (userModel.getLastAction().getTimeInMillis() > timestamp) {
				action = userModel.getLastAction();
			}
		}
		return action;
	}
	
}
