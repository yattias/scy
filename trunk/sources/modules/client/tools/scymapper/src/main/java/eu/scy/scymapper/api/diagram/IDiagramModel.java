package eu.scy.scymapper.api.diagram;

import java.awt.*;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagramModel extends Serializable {

    void setName(String name);

    String getName();

    void addNode(INodeModel n);

	void addNode(INodeModel n, boolean preventOverlap);

    void removeNode(INodeModel n);

    void addLink(ILinkModel n);

	void removeLink(ILinkModel l);

    Set<ILinkModel> getLinks();
	
    Set<INodeModel> getNodes();

	void addDiagramListener(IDiagramListener o);

    void removeDiagramListener(IDiagramListener o);

    void notifyUpdated();

    void notifyNodeAdded(INodeModel node);

    void notifyNodeRemoved(INodeModel node);

    void notifyLinkAdded(ILinkModel link);

    void notifyLinkRemoved(ILinkModel link);

    void removeAll();

    INodeModel getNodeAt(Point point);
}
