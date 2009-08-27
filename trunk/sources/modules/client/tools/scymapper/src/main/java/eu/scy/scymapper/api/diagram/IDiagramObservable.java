package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:27:20
  */
public interface IDiagramObservable {
    public void addObserver(IDiagramObserver o);

    public void removeObserver(IDiagramObserver o);

    public void notifyUpdated();

    public void notifyNodeAdded(INode node);

    public void notifyNodeRemoved(INode node);

    public void notifyLinkAdded(IConceptLink link);

    public void notifyLinkRemoved(IConceptLink link);

    public void notifyNodeSelected(INode node);
}
