/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.scyelo.ScyElo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicMissionAnchor// implements MissionAnchor
{

   private URI uri;
   private ScyElo scyElo;
   private String id;
   private String iconType;
   private List<URI> loEloUris = new ArrayList<URI>();
   private List<String> inputMissionAnchorIds = new ArrayList<String>();
   private List<String> relationNames = new ArrayList<String>();
   private URI targetDescriptionUri;
   private URI assignmentUri;
   private URI resourcesUri;
   private ColorSchemeId colorScheme;

   public String getId()
   {
      return id;
   }

   public List<String> getInputMissionAnchorIds()
   {
      return inputMissionAnchorIds;
   }

   public void setInputMissionAnchorIds(List<String> inputMissionAnchorIds)
   {
      if (inputMissionAnchorIds != null)
      {
         this.inputMissionAnchorIds = inputMissionAnchorIds;
      }
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public URI getUri()
   {
      return uri;
   }

   public void setUri(URI eloUri)
   {
      this.uri = eloUri;
   }

   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      if (relationNames != null)
      {
         this.relationNames = relationNames;
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

   public ScyElo getScyElo()
   {
      return scyElo;
   }

   public void setScyElo(ScyElo scyElo)
   {
      this.scyElo = scyElo;
   }

   public String getIconType()
   {
      return iconType;
   }

   public void setIconType(String iconType)
   {
      this.iconType = iconType;
   }

   public URI getAssignmentUri()
   {
      return assignmentUri;
   }

   public void setAssignmentUri(URI assignmentUri)
   {
      this.assignmentUri = assignmentUri;
   }

   public ColorSchemeId getColorScheme()
   {
      return colorScheme;
   }

   public void setColorScheme(ColorSchemeId colorScheme)
   {
      this.colorScheme = colorScheme;
   }

   public URI getResourcesUri()
   {
      return resourcesUri;
   }

   public void setResourcesUri(URI resourcesUri)
   {
      this.resourcesUri = resourcesUri;
   }

   public URI getTargetDescriptionUri()
   {
      return targetDescriptionUri;
   }

   public void setTargetDescriptionUri(URI targetDescription)
   {
      this.targetDescriptionUri = targetDescription;
   }
}
