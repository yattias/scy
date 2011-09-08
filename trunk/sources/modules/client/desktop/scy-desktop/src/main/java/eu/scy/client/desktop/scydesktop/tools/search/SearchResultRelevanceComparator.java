/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import java.util.Comparator;
import roolo.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class SearchResultRelevanceComparator implements Comparator<ISearchResult>
{

   @Override
   public int compare(ISearchResult searchResult1, ISearchResult searchResult2)
   {
      if (searchResult1 == null)
      {
         if (searchResult2 == null)
         {
            return 0;
         }
         return 1;
      }
      if (searchResult1 == null)
      {
         return -1;
      }
      double dif = searchResult2.getRelevance() - searchResult1.getRelevance();
      if (dif < 0.0)
      {
         return -1;
      }
      else if (dif > 0.0)
      {
         return 1;
      }
      return 0;
   }
}
