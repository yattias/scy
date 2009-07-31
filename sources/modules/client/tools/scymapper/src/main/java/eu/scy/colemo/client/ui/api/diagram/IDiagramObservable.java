package eu.scy.colemo.client.ui.api.diagram;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;

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

    public void notifyNodeAdded(IConceptNode node);

    public void notifyNodeRemoved(IConceptNode node);

    public void notifyLinkAdded(IConceptLink link);

    public void notifyLinkRemoved(IConceptLink link);

    public void notifyNodeSelected(IConceptNode node);
}
