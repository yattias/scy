package eu.scy.scymapper.api;

import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;

/**
 * A concept type is a pre configured concept used to make certain concepts readily available in scymapper
 * It creates a easy and quick way to configure a list of available concept nodes e.g. using spring
 * A concept type is used as a template when creating new concepts. When the user adds a new concept based
 * on a concept type, a new INodeModel instance is created with the properties of the ConceptType copied to the
 * INodeModel instance as initial values.
 * @see eu.scy.scymapper.api.diagram.INodeModel
 */
public interface IConceptType {
    /**
     * Returns the configured node shape
     * @return a node shape
     */
    INodeShape getNodeShape();

    /**
     * Set the node shape of this concept prototype
     * @param shape the node shape
     */
    void setNodeShape(INodeShape shape);

    /**
     * Set the node style of this object
     * @param nodeStyle the node style object
     */
    void setNodeStyle(INodeStyle nodeStyle);

    /**
     * @return the node style of this concept prototype
     */
    INodeStyle getNodeStyle();

    /**
     *
     * @return Name (label) of the concept prototype
     */
    String getName();

    /**
     * Sets the name (label) of this concept prototype
     * @param name
     */
    void setName(String name);

    /**
     * Returns the description of this concept prototype
     * @return the description of this concept prototype
     */
    String getDescription();

    /**
     * Sets the description of this concept prototype
     * @param description the description of this concept prototype
     */
    void setDescription(String description);

    /**
     * Returns the configured height
     * @return the height
     */
    int getHeight();

    /**
     * Sets the height of the concept prototype
     * @param height the height
     */
    void setHeight(int height);

    /**
     * Returns the configured width
     * @return the width
     */
    int getWidth();

    /**
     * Sets the configured width
     * @param width the width
     */
    void setWidth(int width);

}
