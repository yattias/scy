/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.scyelo;

import java.util.Comparator;

/**
 *
 * @author SikkenJ
 */
public class ScyEloTitleComparator implements Comparator<ScyElo>
{

   @Override
   public int compare(ScyElo scyElo1, ScyElo scyElo2)
   {
      if (scyElo1 == null)
      {
         if (scyElo2 == null)
         {
            return 0;
         }
         return 1;
      }
      if (scyElo1 == null)
      {
         return -1;
      }
      return scyElo1.getTitle().compareToIgnoreCase(scyElo2.getTitle());
   }
}