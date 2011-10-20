/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import eu.scy.common.scyelo.ScyElo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public class UriScyElo
{

   private URI uri;
   private ScyElo scyElo;

   public static List<URI> getUris(List<UriScyElo> uriScyElos)
   {
      List<URI> uris = new ArrayList<URI>();
      if (uriScyElos != null)
      {
         for (UriScyElo uriScyElo : uriScyElos)
         {
            uris.add(uriScyElo.getUri());
         }
      }
      return uris;
   }

   public static List<UriScyElo> createUriScyElos(List<URI> uris)
   {
      List<UriScyElo> uriScyElos = new ArrayList<UriScyElo>();
      if (uriScyElos != null)
      {
         for (URI uri : uris)
         {
            uriScyElos.add(new UriScyElo(uri));
         }
      }
      return uriScyElos;
   }

   public UriScyElo(URI uri)
   {
      this.uri = uri;
   }

   public UriScyElo(ScyElo ScyElo)
   {
      this.scyElo = ScyElo;
      uri = scyElo.getUri();
   }

   @Override
   public String toString()
   {
      return "UriScyElo{" + "uri=" + uri + ",scyElo set=" + (scyElo!=null) + '}';
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
      final UriScyElo other = (UriScyElo) obj;
      if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 83 * hash + (this.uri != null ? this.uri.hashCode() : 0);
      return hash;
   }

   public ScyElo getScyElo()
   {
      return scyElo;
   }

   public URI getUri()
   {
      return uri;
   }

   public void setScyElo(ScyElo ScyElo)
   {
      this.scyElo = ScyElo;
      uri = scyElo.getUri();
   }
}
