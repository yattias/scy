/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import java.net.URI;
import roolo.api.search.ISearchResult;
import roolo.api.search.exceptions.DataNotQueriedForException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public class SimpleSearchResult implements ISearchResult
{
   private static final long serialVersionUID = -4682993580882632670L;

   private final URI uri;
   private final double relevance;
   private final IMetadata metadata;
   private final IELO elo;

   public SimpleSearchResult(URI uri, double relevance)
   {
      super();
      this.uri = uri;
      this.relevance = relevance;
      metadata = null;
      elo = null;
   }

   public SimpleSearchResult(URI uri, double relevance, IMetadata metadata, IELO elo)
   {
      super();
      this.uri = uri;
      this.relevance = relevance;
      this.metadata = metadata;
      this.elo = elo;
   }

   @Override
   public String toString()
   {
      return "{uri=" + getUri() + ", relevance=" + getRelevance() + "}";
   }

   @Override
   public URI getUri()
   {
      return uri;
   }

   @Override
   public double getRelevance()
   {
      return relevance;
   }

   @Override
   public IMetadata getMetadata()
   {
      if (metadata != null)
         return metadata;
      throw new DataNotQueriedForException();
   }

   @Override
   public IELO getELO()
   {
      if (elo != null)
         return elo;
      throw new DataNotQueriedForException();
   }

}
