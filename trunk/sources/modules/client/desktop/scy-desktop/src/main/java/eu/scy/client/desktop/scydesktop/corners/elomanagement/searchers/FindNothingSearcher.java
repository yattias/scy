/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloBasedSearcher;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import java.util.List;
import roolo.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class FindNothingSearcher implements EloBasedSearcher {

   @Override
   public String toString()
   {
      return getDisplayId();
   }

   @Override
   public String getDisplayId()
   {
      return "Find nothing";
   }

   @Override
   public List<ISearchResult> findElos(ScyElo scyElo)
   {
      // just for testing
      try
      {
         Thread.sleep(2500);
      }
      catch (InterruptedException ex)
      {
      }
      List<ISearchResult> searchResults = new ArrayList<ISearchResult>();
      return searchResults;
   }

}
