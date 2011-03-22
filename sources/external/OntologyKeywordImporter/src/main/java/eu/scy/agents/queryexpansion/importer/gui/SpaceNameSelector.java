package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

public interface SpaceNameSelector {

	public String[] getAllSpaceNames(TupleSpace ts) throws TupleSpaceException;
	
	public String getButtonText();
	
	public String getSpaceNamePrefix();
}
