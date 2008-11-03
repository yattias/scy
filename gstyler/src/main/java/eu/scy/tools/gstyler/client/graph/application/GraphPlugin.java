package eu.scy.tools.gstyler.client.graph.application;

import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;

/**
 * Plugins for a {@link GraphApplication} have to implement this interface
 */
public interface GraphPlugin {

    public String getName();

    public Widget getUI();

    public void addExampleDocument(GWTGraph graph);

    public GraphApplication getGraphApplication();
}