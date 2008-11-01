package eu.scy.tools.gstyler.client.graph.application;

import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;

/**
 * Plugins for a {@link GraphApplication} have to implement this interface
 */
public interface GraphPlugin {

    public abstract String getName();

    public abstract Widget getUI();

    public abstract void addExampleDocument(GWTGraph graph);
}