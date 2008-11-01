package eu.scy.tools.gstyler.client.common;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for the visible part of a GStyler plugin.  
 */
public interface Palette {

    public abstract String getName();

    public abstract Widget getUI();

}