package eu.scy.tools.gstyler.client.common;

import eu.scy.tools.gstyler.client.graph.GWTGraph;

/**
 * Public API of GStyler and potentially other applications.
 * Palettes get a reference to a class implementing this interface.
 */
public interface GraphApplication {

    public abstract GWTGraph getGraph();

}