package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:27:10
 */
public interface IDiagramObserver {
    void linkAdded(IConceptLink link);

    void linkRemoved(IConceptLink link);

    void nodeAdded(INode n);

    void nodeRemoved(INode n);

    void updated(IDiagram diagram);

    void nodeSelected(INode n);
}
