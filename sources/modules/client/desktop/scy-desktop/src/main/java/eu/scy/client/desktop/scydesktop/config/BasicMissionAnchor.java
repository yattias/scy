/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.art.ColorSchemeId;
import java.net.URI;
import java.util.List;
import roolo.elo.api.IMetadata;

/**
 *
 * @author sikken
 */
public class BasicMissionAnchor// implements MissionAnchor
{

   private URI uri;
   private IMetadata metadata;
   private String id;
   private String iconType;
   private List<URI> loEloUris;
   private List<String> inputMissionAnchorIds;
   private List<String> relationNames;
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
      this.inputMissionAnchorIds = inputMissionAnchorIds;
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
      this.relationNames = relationNames;
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      this.loEloUris = loEloUris;
   }

   public void setMetadata(IMetadata metadata)
   {
      this.metadata = metadata;
   }

   public IMetadata getMetadata()
   {
      return metadata;
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

   public URI getTargetDescription()
   {
      return targetDescriptionUri;
   }

   public void setTargetDescription(URI targetDescription)
   {
      this.targetDescriptionUri = targetDescription;
   }
}
