package eu.scy.colemo.client.ui.api.links;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:59:55
 * To change this template use File | Settings | File Templates.
 */
public interface IConceptLink extends ILink, ILinkObservable {

    public IConceptNode getFromNode();
    public void setFromNode(IConceptNode node);

    public IConceptNode getToNode();
    public void setToNode(IConceptNode node);
}
