package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.impl.model.NodeModel;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:29:28
 * To change this template use File | Settings | File Templates.
 */
public interface INodeModelObserver {
    public void moved(INodeModel node);
    public void resized(INodeModel node);

    public void labelChanged(INodeModel node);

    public void styleChanged(INodeModel node);

    public void shapeChanged(INodeModel node);

    public void nodeSelected(NodeModel conceptNode);
}
