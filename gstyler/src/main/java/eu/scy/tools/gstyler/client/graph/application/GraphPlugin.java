package eu.scy.tools.gstyler.client.graph.application;

import com.google.gwt.user.client.ui.Widget;

/**
 * Plugins for a {@link GraphApplication} have to implement this interface
 */
public interface GraphPlugin {

    public abstract String getName();

    public abstract Widget getUI();

}