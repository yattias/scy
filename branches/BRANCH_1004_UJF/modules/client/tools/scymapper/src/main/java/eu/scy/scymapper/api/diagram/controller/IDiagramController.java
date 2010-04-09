package eu.scy.scymapper.api.diagram.controller;

import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * @author bjoerge
 * @created 24.jun.2009 11:43:03
 */
public interface IDiagramController {
	void setName(String name);

	void add(INodeModel n, boolean preventOverlap);

	void add(INodeModel n);

	void add(ILinkModel l);

	void remove(INodeModel n);

	void remove(ILinkModel l);

	void removeAll();
}
