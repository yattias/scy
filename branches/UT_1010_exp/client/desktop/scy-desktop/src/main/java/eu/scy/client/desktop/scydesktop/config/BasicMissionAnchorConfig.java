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
public class BasicMissionAnchorConfig {
   private URI uri;
   private String id;
   private List<URI> loEloUris;
   private List<String> inputMissionAnchorNames;
   private boolean mainAnchorElo;
   private List<String> relationNames;

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> resourceEloUris)
   {
      this.loEloUris = resourceEloUris;
   }

   public List<String> getInputMissionAnchorNames()
   {
      return inputMissionAnchorNames;
   }

   public void setInputMissionAnchorNames(List<String> inputMissionAnchorNames)
   {
      this.inputMissionAnchorNames = inputMissionAnchorNames;
   }

   public boolean isMainAnchorElo()
   {
      return mainAnchorElo;
   }

   public void setMainAnchorElo(boolean mainAnchorElo)
   {
      this.mainAnchorElo = mainAnchorElo;
   }

   public URI getUri()
   {
      return uri;
   }

   public void setUri(URI uri)
   {
      this.uri = uri;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      this.relationNames = relationNames;
   }
   
}
