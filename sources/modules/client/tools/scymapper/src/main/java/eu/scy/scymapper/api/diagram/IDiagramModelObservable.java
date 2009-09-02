package eu.scy.scymapper.api.diagram;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:27:20
  */
public interface IDiagramModelObservable {
    public void addObserver(IDiagramModelObserver o);

    public void removeObserver(IDiagramModelObserver o);

    public void notifyUpdated();

    public void notifyNodeAdded(INodeModel node);

    public void notifyNodeRemoved(INodeModel node);

    public void notifyLinkAdded(ILinkModel link);

    public void notifyLinkRemoved(ILinkModel link);

    public void notifyNodeSelected(INodeModel node);
}
