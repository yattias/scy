package eu.scy.colemo.client;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.apr.2009
 * Time: 06:33:02
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapLoader {

    private static ConceptMapLoader defaultInstance;

    private ConceptMapLoader() {
    }

    public static ConceptMapLoader getDefaultInstance() {
        if(defaultInstance == null) defaultInstance = new ConceptMapLoader();
        return defaultInstance;
    }

    public List parseConcepts(String xml) {
        return null;    
    }
}
