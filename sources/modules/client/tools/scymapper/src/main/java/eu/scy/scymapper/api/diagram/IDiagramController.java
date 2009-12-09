package eu.scy.scymapper.api.diagram;

/**
 * @author bjoerge
 * @created 24.jun.2009 11:43:03
 */
public interface IDiagramController {
    void setName(String name);
    void addNode(INodeModel n, boolean preventOverlap);
    void addNode(INodeModel n);
    void addLink(ILinkModel l);

    void removeAll();

    void removeNode(INodeModel n);
}
