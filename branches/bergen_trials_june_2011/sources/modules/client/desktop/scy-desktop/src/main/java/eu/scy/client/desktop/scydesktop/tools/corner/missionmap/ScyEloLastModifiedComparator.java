/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.common.scyelo.ScyElo;
import java.util.Comparator;

/**
 *
 * @author SikkenJ
 */
public class ScyEloLastModifiedComparator implements Comparator<ScyElo>
{

   @Override
   public int compare(ScyElo o1, ScyElo o2)
   {
      if (o1 == null || o2 == null)
      {
         return compareOnNulls(o1, o2);
      }
      // both elos are not null
      Long lastModified1 = getLastModified(o1);
      Long lastModified2 = getLastModified(o2);
      if (lastModified1 == null || lastModified2 == null)
      {
         return compareOnNulls(lastModified1, lastModified2);
      }
      // both dates are not null
      return lastModified1.compareTo(lastModified2);
   }

   private Long getLastModified(ScyElo scyElo)
   {
      Long lastModified = scyElo.getDateLastModified();
      if (lastModified == null)
      {
         lastModified = scyElo.getDateCreated();
      }
      return lastModified;
   }

   private int compareOnNulls(Object o1, Object o2)
   {
      if (o1 == null && o2 == null)
      {
         return 0;
      }
      else if (o1 == null)
      {
         return -1;
      }
      else if (o2 == null)
      {
         return 1;
      }
      return 0;
   }
}
