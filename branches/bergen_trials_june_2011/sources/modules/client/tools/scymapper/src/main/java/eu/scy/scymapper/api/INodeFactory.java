package eu.scy.scymapper.api;

import eu.scy.scymapper.api.diagram.model.INodeModel;

import javax.swing.*;

/**
 * A concept type is a pre configured concept used to make certain concepts readily available in scymapper
 * It creates a easy and quick way to configure a list of available concept nodes e.g. using spring
 * A concept type is used as a template when creating new concepts. When the user adds a new concept based
 * on a concept type, a new INodeModel instance is created with the properties of the DefaultNodeFactory copied to the
 * INodeModel instance as initial values.
 *
 * @see eu.scy.scymapper.api.diagram.model.INodeModel
 */
public interface INodeFactory {
	Icon getIcon();

	String getName();

	INodeModel create();
}
