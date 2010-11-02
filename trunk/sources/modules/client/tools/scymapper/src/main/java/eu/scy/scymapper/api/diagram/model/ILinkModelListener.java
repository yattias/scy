package eu.scy.scymapper.api.diagram.model;

/**
 * @author bjoerge
 * @created 22.jun.2009 Time: 20:46:43 To change this template use File | Settings | File Templates.
 */
public interface ILinkModelListener {

    void updated(ILinkModel subject);

    void selectionChanged(ILinkModel node);

    void labelChanged(ILinkModel link);

    void linkFlipped(ILinkModel simpleLink);
}
