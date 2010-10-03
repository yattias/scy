/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.net.URI;
import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicMissionMap {
   private String id;
   private String name;
   private List<URI> loEloUris;
   private List<BasicLas> lasses;
   private String initialLasId;

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public List<BasicLas> getLasses()
   {
      return lasses;
   }

   public void setLasses(List<BasicLas> lasses)
   {
      this.lasses = lasses;
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      this.loEloUris = loEloUris;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getInitialLasId()
   {
      return initialLasId;
   }

   public void setInitialLasId(String initialLasId)
   {
      this.initialLasId = initialLasId;
   }
}
