package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

public class AvailableOntologySelector implements SpaceNameSelector {

	private String buttonText;
	
	private String spaceNamePrefix;
	
	public AvailableOntologySelector(String buttonText, String spaceNamePrefix) {
		this.buttonText = buttonText;
		this.spaceNamePrefix  = spaceNamePrefix;
	}

	public String[] getAllSpaceNames(TupleSpace ts) throws TupleSpaceException {

		return SQLSpacesConnectionDialog.readAvailableSpaces(ts, this.spaceNamePrefix);
	}

	public String getButtonText() {
		return this.buttonText;
	}

	public String getSpaceNamePrefix() {
		return this.spaceNamePrefix;
	}
}
