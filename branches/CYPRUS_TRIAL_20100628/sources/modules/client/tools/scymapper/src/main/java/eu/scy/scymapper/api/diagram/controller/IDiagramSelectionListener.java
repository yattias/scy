package eu.scy.scymapper.api.diagram.controller;

import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;

/**
 * @author bjoerge
 * @created 24.sep.2009
 * Time: 16:10:05
 */
public interface IDiagramSelectionListener {
	void selectionChanged(IDiagramSelectionModel selectionModel);
}
