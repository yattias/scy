package eu.scy.scymapper.api.configuration;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import java.util.List;

/**
 * @author bjoerge
 * @created 30.okt.2009 12:06:18
 * <p/>
 * A tool configuration for SCYMapper
 */
public interface ISCYMapperToolConfiguration extends LearningActivitySpaceToolConfiguration {

	/**
	 * Returns a list of concept types that should be made available for the learner
	 *
	 * @return the list of concept types that should be made available for the learner
	 * @see eu.scy.scymapper.api.INodeFactory
	 */
	List<INodeFactory> getNodeFactories();

	/**
	 * Returns a list of link types that should be made available for the learner
	 *
	 * @return the list of link types that should be made available for the learner
	 * @see eu.scy.scymapper.api.ILinkFactory
	 */
	List<ILinkFactory> getLinkFactories();

	/**
	 * Sets the list of concept types that should be made available for the learner
	 *
	 * @param availableNodeShapes list of concept types that should be made available for the learner
	 */
	void setNodeFactories(List<INodeFactory> availableNodeShapes);

	/**
	 * Sets the list of link types that should be made available for the learner
	 *
	 * @param availableLinkShapes list of link types that should be made available for the learner
	 */
	void setLinkFactories(List<ILinkFactory> availableLinkShapes);

	/**
	 * Sets a list of predefined nodes that should be added to the concept map at startup time
	 * This can be used to provide a set of predefined and/or locked nodes to the learner
	 * I.e. by setting a constraint to the node model, one could add a concept that can not be moved, resized or deleted
	 *
	 * @param predefinedNodes list of predefined nodes that should be added to the concept map at startup time
	 */
	void setPredefinedNodes(List<INodeModel> predefinedNodes);

	/**
	 * Returns a list of predefined nodes that should be added to the concept map at startup time
	 *
	 * @return list of predefined nodes that should be added to the concept map at startup time
	 */
	List<INodeModel> getPredefinedNodes();


	void setConnectorFactories(List<INodeFactory> connectorFactories);

	List<INodeFactory> getConnectorFactories();

	/**
	 * Returns true if SCYMapper is running in debug mode
	 *
	 * @return true if SCYMapper is running in debug mode
	 */
	boolean isDebug();

	/**
	 * Sets debug mode
	 *
	 * @param b true if debug mode should be on, false otherwise
	 */
	void setDebug(boolean b);

	void setViewShadow(boolean viewShadow);

	boolean getViewShadow();
}
