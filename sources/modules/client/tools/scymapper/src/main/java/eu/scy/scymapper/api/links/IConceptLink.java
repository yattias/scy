package eu.scy.scymapper.api.links;

import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:59:55
 * To change this template use File | Settings | File Templates.
 */
public interface IConceptLink extends ILink, ILinkObservable {

    public INode getFromNode();
    public void setFromNode(INode node);

    public INode getToNode();
    public void setToNode(INode node);
}
