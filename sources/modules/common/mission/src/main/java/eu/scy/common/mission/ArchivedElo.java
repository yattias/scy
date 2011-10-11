/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import eu.scy.common.scyelo.ScyElo;
import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public class ArchivedElo
{

   private URI eloUri;
   private ScyElo scyElo;
   private Object eloIcon;
   private long archievedMillis;

   @Override
   public String toString()
   {
      return "ArchivedElo{" + "eloUri=" + eloUri + "scyElo=" + scyElo + "eloIcon=" + eloIcon + "archievedMillis=" + archievedMillis + '}';
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final ArchivedElo other = (ArchivedElo) obj;
      if (this.eloUri != other.eloUri && (this.eloUri == null || !this.eloUri.equals(other.eloUri)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 37 * hash + (this.eloUri != null ? this.eloUri.hashCode() : 0);
      return hash;
   }

   public ArchivedElo(ScyElo scyElo)
   {
      this.scyElo = scyElo;
      eloUri = scyElo.getUri();
      archievedMillis = System.currentTimeMillis();
   }

   public ArchivedElo(URI eloUri, long archievedMillis)
   {
      this.eloUri = eloUri;
      this.archievedMillis = archievedMillis;
   }

   public long getArchievedMillis()
   {
      return archievedMillis;
   }

   public void setArchievedMillis(long archievedMillis)
   {
      this.archievedMillis = archievedMillis;
   }

   public Object getEloIcon()
   {
      return eloIcon;
   }

   public void setEloIcon(Object eloIcon)
   {
      this.eloIcon = eloIcon;
   }

   public URI getEloUri()
   {
      return eloUri;
   }

   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
   }

   public ScyElo getScyElo()
   {
      return scyElo;
   }

   public void setScyElo(ScyElo scyElo)
   {
      this.scyElo = scyElo;
   }
}
