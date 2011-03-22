package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

public class OntologyExportSelector implements SpaceNameSelector {

	private String buttonText;
	private String spaceNamePrefix;

	public OntologyExportSelector(String buttonText, String spaceNamePrefix) {
		this.buttonText = buttonText;
		this.spaceNamePrefix = spaceNamePrefix;
	}

	public String getButtonText() {
		return this.buttonText;
	}

	public String getSpaceNamePrefix() {
		return this.spaceNamePrefix;
	}

	public String[] getAllSpaceNames(TupleSpace ts) throws TupleSpaceException {

		return SQLSpacesConnectionDialog.readAvailableSpaces(ts, this.spaceNamePrefix);

	}
}
