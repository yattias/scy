/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission;

import java.util.Comparator;
import eu.scy.common.scyelo.ScyEloTitleComparator;

/**
 *
 * @author SikkenJ
 */
public class ArchivedEloTitleComparator implements Comparator<ArchivedElo>
{
   private final ScyEloTitleComparator scyEloTitleComparator = new ScyEloTitleComparator();

   @Override
   public int compare(ArchivedElo archivedElo1, ArchivedElo archivedElo2)
   {
      if (archivedElo1 == null)
      {
         if (archivedElo2 == null)
         {
            return 0;
         }
         return 1;
      }
      if (archivedElo1 == null)
      {
         return -1;
      }
      return scyEloTitleComparator.compare(archivedElo1.getScyElo(),archivedElo2.getScyElo());
   }
}
