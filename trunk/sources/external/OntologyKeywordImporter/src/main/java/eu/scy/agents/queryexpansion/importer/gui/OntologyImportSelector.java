package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.awt.Frame;

import javax.swing.JOptionPane;

public class OntologyImportSelector implements SpaceNameSelector {

	private Frame parent;
	private String buttonText;
	private String spaceNamePrefix;

	public OntologyImportSelector(Frame owner, String buttonText, String spaceNamePrefix) {
		this.parent = owner;
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
		try {

			String[] spaces = SQLSpacesConnectionDialog.readAvailableSpaces(ts,
					this.spaceNamePrefix);
			for (String spaceName : spaces) {
				if (spaceName != null && spaceName.equals(this.spaceNamePrefix)) {
					int retValue = JOptionPane.showConfirmDialog(this.parent,
							"The database already contains an ontology with the name: \n"
									+ this.spaceNamePrefix
									+ "\n\nDo you want to overwrite it?",
							"Ontology already exists",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (retValue == JOptionPane.NO_OPTION) {
						return new String[0];
					}
				}
			}
			return new String[] { this.spaceNamePrefix };

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this.parent,
					"The following error occurred:\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return new String[0];
		}
	}
}
