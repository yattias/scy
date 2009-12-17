package eu.scy.scymapper.api.diagram.model;

/**
 * @author bjoerge
 * @created 24.jun.2009 11:27:10
 */
public interface IDiagramListener {
    void linkAdded(ILinkModel link);

    void linkRemoved(ILinkModel link);

    void nodeAdded(INodeModel n);

    void nodeRemoved(INodeModel n);

    void updated(IDiagramModel diagramModel);

    void nodeSelected(INodeModel n);
}
