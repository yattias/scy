package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.impl.model.NodeModel;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:29:28
 * To change this template use File | Settings | File Templates.
 */
public interface INodeModelListener {
    void moved(INodeModel node);
    void resized(INodeModel node);

    void labelChanged(INodeModel node);

    void shapeChanged(INodeModel node);

    void selectionChanged(INodeModel node);

    void deleted(NodeModel nodeModel);
}
