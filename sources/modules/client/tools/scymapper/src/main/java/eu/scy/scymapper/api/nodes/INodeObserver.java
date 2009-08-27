package eu.scy.scymapper.api.nodes;

import eu.scy.scymapper.impl.model.Node;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:29:28
 * To change this template use File | Settings | File Templates.
 */
public interface INodeObserver {
    public void moved(INode node);
    public void resized(INode node);

    public void labelChanged(INode node);

    public void styleChanged(INode node);

    public void shapeChanged(INode node);

    public void nodeSelected(Node conceptNode);
}
