package eu.scy.scymapper.api;

import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;

import java.io.Serializable;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:15:27
 */
public interface IConceptMap extends Serializable, Cloneable {
	String getName();

	void setName(String name);

	IDiagramModel getDiagram();

	IDiagramSelectionModel getDiagramSelectionModel();

	void setSelectionModel(IDiagramSelectionModel selectionModel);

	void setDiagram(IDiagramModel diagram);

	void addConceptMapListener(IConceptMapListener l);

	void removeConceptMapListener(IConceptMapListener l);

	void notifyListeners();
}
