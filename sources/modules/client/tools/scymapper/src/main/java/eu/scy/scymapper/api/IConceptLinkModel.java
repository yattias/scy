package eu.scy.scymapper.api;

import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.ILinkModelObservable;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:59:55
 * To change this template use File | Settings | File Templates.
 */
public interface IConceptLinkModel extends ILinkModel, ILinkModelObservable {

    public INodeModel getFromNode();
    public void setFromNode(INodeModel node);

    public INodeModel getToNode();
    public void setToNode(INodeModel node);
}
