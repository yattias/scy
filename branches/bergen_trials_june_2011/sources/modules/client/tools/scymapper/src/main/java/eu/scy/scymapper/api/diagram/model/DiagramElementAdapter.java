package eu.scy.scymapper.api.diagram.model;

/**
 * @author bjoerge
 * @created 05.feb.2010 18:21:18
 */
public abstract class DiagramElementAdapter implements IDiagramListener, ILinkModelListener, INodeModelListener {

	@Override
	public void linkAdded(ILinkModel link, boolean focused) {
	}

	@Override
	public void linkRemoved(ILinkModel link) {
	}

	@Override
	public void linkFlipped(ILinkModel link) {
	}

	@Override
	public void nodeAdded(INodeModel n, boolean focused) {
	}

	@Override
	public void nodeRemoved(INodeModel node) {
	}

	@Override
	public void updated(IDiagramModel diagramModel) {
	}

	@Override
	public void nodeSelected(INodeModel n) {
	}

	@Override
	public void moved(INodeModel node) {
	}

	@Override
	public void resized(INodeModel node) {
	}

	@Override
	public void labelChanged(INodeModel node) {
	}

	@Override
	public void shapeChanged(INodeModel node) {
	}

	@Override
	public void styleChanged(INodeModel node) {
	}

	@Override
	public void selectionChanged(INodeModel node) {
	}

	@Override
	public void deleted(INodeModel nodeModel) {
	}

	@Override
	public void updated(ILinkModel subject) {
	}

	@Override
	public void selectionChanged(ILinkModel node) {
	}

	@Override
	public void labelChanged(ILinkModel link) {
	}
}
