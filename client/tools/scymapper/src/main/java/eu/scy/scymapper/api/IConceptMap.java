package eu.scy.scymapper.api;

import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;

import java.io.Serializable;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:15:27
 * This interface represents a concept map.
 */
public interface IConceptMap extends Serializable, Cloneable {

    // Default concept map nam
    final String DEFAULT_CMAP_NAME = "New Concept Map";

    /**
     * @return Name of concept map
     */
    String getName();

    /**
     * @param name Name of concept map
     */
	void setName(String name);

    /**
     * @return The diagram model
     */
	IDiagramModel getDiagram();

    /**
     * @return The selection model used in this concept map
     */
	IDiagramSelectionModel getDiagramSelectionModel();

    /**
     * @param selectionModel Selectionmodel to use in this concept map
     */
	void setSelectionModel(IDiagramSelectionModel selectionModel);

    /**
     *
     * @param diagram Set the diagram
     */
	void setDiagram(IDiagramModel diagram);

    /**
     * Method for adding listeners to the concept map
     * @param l The listener to add
     */
	void addConceptMapListener(IConceptMapListener l);

    /**
     * Method for removing a concept map listener
     * @param l The listener to add
     */
	void removeConceptMapListener(IConceptMapListener l);

    /**
     * Require the implementaion of this. Notifies listeners.
     */
	void notifyListeners();
}
