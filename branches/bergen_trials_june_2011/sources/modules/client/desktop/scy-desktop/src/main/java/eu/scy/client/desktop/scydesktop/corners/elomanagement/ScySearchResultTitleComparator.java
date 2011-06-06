/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import java.util.Comparator;

/**
 *
 * @author SikkenJ
 */
public class ScySearchResultTitleComparator implements Comparator<ScySearchResult>
{

   @Override
   public int compare(ScySearchResult scySearchResult1, ScySearchResult scySearchResult2)
   {
      if (scySearchResult1 == null)
      {
         if (scySearchResult2 == null)
         {
            return 0;
         }
         return 1;
      }
      if (scySearchResult1 == null)
      {
         return -1;
      }
      return scySearchResult1.getScyElo().getTitle().compareToIgnoreCase(scySearchResult2.getScyElo().getTitle());
   }
}
