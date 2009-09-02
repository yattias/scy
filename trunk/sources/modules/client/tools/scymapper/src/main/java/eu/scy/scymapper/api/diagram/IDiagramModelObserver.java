package eu.scy.scymapper.api.diagram;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:27:10
 */
public interface IDiagramModelObserver {
    void linkAdded(ILinkModel link);

    void linkRemoved(ILinkModel link);

    void nodeAdded(INodeModel n);

    void nodeRemoved(INodeModel n);

    void updated(IDiagramModel diagramModel);

    void nodeSelected(INodeModel n);
}
