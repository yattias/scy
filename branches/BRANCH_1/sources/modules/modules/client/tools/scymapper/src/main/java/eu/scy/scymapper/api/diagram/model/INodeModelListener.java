package eu.scy.scymapper.api.diagram.model;

/**
 * @author bjoerge
 * @created 22.jun.2009 18:29:28
 */
public interface INodeModelListener {
	void moved(INodeModel node);

	void resized(INodeModel node);

	void labelChanged(INodeModel node);

	void shapeChanged(INodeModel node);

	void styleChanged(INodeModel node);

	void selectionChanged(INodeModel node);

	void deleted(INodeModel nodeModel);
}
