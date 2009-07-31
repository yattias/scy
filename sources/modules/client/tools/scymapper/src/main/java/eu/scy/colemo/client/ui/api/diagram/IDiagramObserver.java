package eu.scy.colemo.client.ui.api.diagram;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:27:10
 */
public interface IDiagramObserver {
    public void linkAdded(IConceptLink link);

    public void linkRemoved(IConceptLink link);

    public void nodeAdded(IConceptNode n);

    public void nodeRemoved(IConceptNode n);

    public void updated(IDiagram diagram);

    public void nodeSelected(IConceptNode n);
}
