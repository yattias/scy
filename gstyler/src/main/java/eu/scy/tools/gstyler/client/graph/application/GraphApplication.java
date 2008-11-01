package eu.scy.tools.gstyler.client.graph.application;

import eu.scy.tools.gstyler.client.graph.GWTGraph;

/**
 * Public API of a GWTGraph based application (such as GStyler)
 * Plugins get a reference to a class implementing this interface.
 */
public interface GraphApplication {

    public abstract GWTGraph getGraph();

}