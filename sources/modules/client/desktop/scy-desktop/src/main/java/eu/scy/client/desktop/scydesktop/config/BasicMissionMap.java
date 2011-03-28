/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicMissionMap
{

   private String id;
   private String name;
   private List<URI> loEloUris = new ArrayList<URI>();
   private List<BasicLas> lasses = new ArrayList<BasicLas>();
   private String initialLasId;
   private URI missionMapBackgroundImageUri;
   private URI missionMapInstructionUri;
   private String missionMapButtonIconType;

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
      if (lasses != null)
      {
         this.lasses = lasses;
      }
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      if (loEloUris != null)
      {
         this.loEloUris = loEloUris;
      }
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

   public URI getMissionMapBackgroundImageUri()
   {
      return missionMapBackgroundImageUri;
   }

   public void setMissionMapBackgroundImageUri(URI missionMapBackgroundImageUri)
   {
      this.missionMapBackgroundImageUri = missionMapBackgroundImageUri;
   }

   public String getMissionMapButtonIconType()
   {
      return missionMapButtonIconType;
   }

   public void setMissionMapButtonIconType(String missionMapButtonIconType)
   {
      this.missionMapButtonIconType = missionMapButtonIconType;
   }

   public URI getMissionMapInstructionUri()
   {
      return missionMapInstructionUri;
   }

   public void setMissionMapInstructionUri(URI missionMapInstructionUri)
   {
      this.missionMapInstructionUri = missionMapInstructionUri;
   }
}
