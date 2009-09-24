package eu.scy.scymapper.api.diagram;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:43:03
 * To change this template use File | Settings | File Templates.
 */
public interface IDiagramController {
    void setName(String name);
    void addNode(INodeModel n, boolean preventOverlap);
    void addNode(INodeModel n);
    void addLink(ILinkModel l);
}
