/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SimpleSearchResult;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.search.SearchResultRelevanceComparator;

/**
 *
 * @author SikkenJ
 */
public class BackgroundEloBasedSearch extends BackgroundSearch
{

   private final static Logger logger = Logger.getLogger(BackgroundEloBasedSearch.class);
   private final EloBasedSearcher[] eloBasedSearchers;
   private final ScyElo scyElo;
   private final EloBasedSearchFinished eloBasedSearchFinished;

   public BackgroundEloBasedSearch(ToolBrokerAPI tbi, EloInfoControl eloInfoControl, NewEloCreationRegistry newEloCreationRegistry, EloBasedSearcher[] eloBasedSearchers, ScyElo scyElo, EloBasedSearchFinished eloBasedSearchFinished)
   {
      super(tbi, eloInfoControl, newEloCreationRegistry);
      this.eloBasedSearchers = eloBasedSearchers;
      this.scyElo = scyElo;
      this.eloBasedSearchFinished = eloBasedSearchFinished;
   }

   @Override
   public void run()
   {
      long startNanos = System.nanoTime();
      if (isAbort())
      {
         return;
      }
      final long startSearchNanos = System.nanoTime();
      final List<ISearchResult> searchResults = doTheSearch();
      final long realSEarchNanos = System.nanoTime() - startSearchNanos;
      if (isAbort())
      {
         return;
      }
      final List<ScySearchResult> scySearchResults = convertToScySearchResults(searchResults);
      sendScySearchResuls(scySearchResults);
      long nanosUsed = System.nanoTime() - startNanos;
      logger.info("found " + scySearchResults.size() + " elos in " + nanosUsed / 100000 / 10.0 + " ms, search it self took " + realSEarchNanos / 100000 / 10.0 + " ms");
   }

   private List<ISearchResult> doTheSearch()
   {
      Map<URI, ISearchResult> queryResults = new HashMap<URI, ISearchResult>();
      boolean firstQuery = true;
      for (EloBasedSearcher eloBasedSearcher : eloBasedSearchers)
      {
         List<ISearchResult> querySearchResults = eloBasedSearcher.findElos(scyElo);
         if (isAbort())
         {
            return null;
         }
         if (firstQuery)
         {
            for (ISearchResult searchResult : querySearchResults)
            {
               queryResults.put(searchResult.getUri(), searchResult);
            }
            firstQuery = false;
         }
         else
         {
            Map<URI, ISearchResult> newQueryResults = new HashMap<URI, ISearchResult>();
            for (ISearchResult searchResult : querySearchResults)
            {
               ISearchResult previousQueryResult = queryResults.get(searchResult.getUri());
               if (previousQueryResult != null)
               {
                  newQueryResults.put(searchResult.getUri(), new SimpleSearchResult(
                     searchResult.getUri(), searchResult.getRelevance()
                     * previousQueryResult.getRelevance()));
               }
            }
            queryResults = newQueryResults;
         }
      }
      List<ISearchResult> searchResults = new ArrayList<ISearchResult>(queryResults.values());
      Collections.sort(searchResults, new SearchResultRelevanceComparator());
      return searchResults;

   }

   @Override
   protected void setSearchResults(List<ScySearchResult> scySearchResults)
   {
      eloBasedSearchFinished.eloBasedSearchFinished(scySearchResults);
   }
}
