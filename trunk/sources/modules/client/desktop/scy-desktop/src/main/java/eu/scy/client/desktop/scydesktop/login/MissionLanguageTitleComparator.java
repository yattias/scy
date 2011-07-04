/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.login;

import eu.scy.common.scyelo.ScyElo;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author SikkenJ
 */
public class MissionLanguageTitleComparator implements Comparator<ScyElo>
{

   private final Locale myLocale;

   public MissionLanguageTitleComparator(Locale myLocale)
   {
      this.myLocale = myLocale;
   }

   public MissionLanguageTitleComparator()
   {
      this(Locale.getDefault());
   }

   @Override
   public int compare(ScyElo scyElo1, ScyElo scyElo2)
   {
      if (scyElo1 == null && scyElo2 == null)
      {
         return 0;
      }
      if (scyElo1 == null)
      {
         return -1;
      }
      if (scyElo2 == null)
      {
         return 1;
      }
      int compareResult = compareByLanguage(scyElo1, scyElo2);
      if (compareResult != 0)
      {
         return compareResult;
      }
      return compareByTitle(scyElo1, scyElo2);
   }

   private int compareByLanguage(ScyElo scyElo1, ScyElo scyElo2)
   {
      final boolean scyElo1SupportsMyLocale = scyElo1.getElo().supportsLanguage(myLocale);
      final boolean scyElo2SupportsMyLocale = scyElo2.getElo().supportsLanguage(myLocale);
      if (scyElo1SupportsMyLocale && scyElo2SupportsMyLocale)
      {
         return 0;
      }
      if (scyElo1SupportsMyLocale)
      {
         return -1;
      }
      if (scyElo2SupportsMyLocale)
      {
         return 1;
      }
      return 0;
   }

   private int compareByTitle(ScyElo scyElo1, ScyElo scyElo2)
   {
      return scyElo1.getTitle().compareToIgnoreCase(scyElo2.getTitle());
   }
}
