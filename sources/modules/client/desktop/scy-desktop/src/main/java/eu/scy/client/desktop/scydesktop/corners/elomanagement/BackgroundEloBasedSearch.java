/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SimpleSearchResult;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import roolo.search.ISearchResult;

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

   public BackgroundEloBasedSearch(ToolBrokerAPI tbi, NewEloCreationRegistry newEloCreationRegistry, EloBasedSearcher[] eloBasedSearchers, ScyElo scyElo, EloBasedSearchFinished eloBasedSearchFinished)
   {
      super(tbi, newEloCreationRegistry);
      this.eloBasedSearchers = eloBasedSearchers;
      this.scyElo = scyElo;
      this.eloBasedSearchFinished = eloBasedSearchFinished;
   }

   @Override
   public void doSearch()
   {
      if (isAbort())
      {
         return;
      }
      final List<ISearchResult> searchResults = doTheSearch();
      if (isAbort())
      {
         return;
      }
      final List<ScySearchResult> scySearchResults = convertToScySearchResults(searchResults);
      sendScySearchResuls(scySearchResults);
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
         if (querySearchResults != null)
         {
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
                        searchResult.getUri(), searchResult.getRelevance()));
                  }
               }
               queryResults = newQueryResults;
            }
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
