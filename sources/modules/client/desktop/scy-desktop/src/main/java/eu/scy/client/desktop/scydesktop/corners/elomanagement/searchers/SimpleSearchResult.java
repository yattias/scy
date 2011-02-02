/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import java.net.URI;
import roolo.search.SearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public class SimpleSearchResult extends SearchResult {

    private static final long serialVersionUID = -4682993580882632670L;
    private IMetadata metadata;
    private IELO elo;

    public SimpleSearchResult() {
        super();
    }

    public SimpleSearchResult(URI uri, double relevance) {
        super();
        setUri(uri);
        setRelevance(relevance);
        metadata = null;
        elo = null;
    }

    public SimpleSearchResult(URI uri, double relevance, IMetadata metadata, IELO elo) {
        super();
        setRelevance(relevance);
        setUri(uri);
        this.metadata = metadata;
        this.elo = elo;
    }

    @Override
    public String toString() {
        return "{uri=" + getUri() + ", relevance=" + getRelevance() + "}";
    }

    public IMetadata getMetadata() {
        if (metadata != null) {
            return metadata;
        }
        throw new DataNotQueriedForException();
    }

    public IELO getELO() {
        if (elo != null) {
            return elo;
        }
        throw new DataNotQueriedForException();
    }
}
