/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import java.util.List;
import roolo.api.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class SameEloSearcher implements EloBasedSearcher {

   @Override
   public String getDisplayId()
   {
      return "sameElo";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      List<ISearchResult> searchResults = new ArrayList<ISearchResult>();
      searchResults.add(new SimpleSearchResult(scyElo.getUri(), 1.0));
      return searchResults;
   }

}
