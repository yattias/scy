/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.net.URI;

import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 *
 * @author SikkenJ
 */
public class ScyElo
{

   private IELO elo;
   private final ToolBrokerAPI tbi;
   private final IMetadataTypeManager metadataTypemanager;
   private final IMetadataKey titleKey;
   private final IMetadataKey technicalFormatKey;
   private final IMetadataKey descriptionKey;

   public ScyElo(IELO elo, ToolBrokerAPI tbi)
   {
      assert elo != null;
      assert tbi != null;
      assert tbi.getMetaDataTypeManager() != null;
      this.elo = elo;
      this.tbi = tbi;
      this.metadataTypemanager = tbi.getMetaDataTypeManager();
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      descriptionKey = findMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION);
   }

   protected final IMetadataKey findMetadataKey(IMetadataKeyIdDefinition id)
   {
      IMetadataKey key = metadataTypemanager.getMetadataKey(id);
      if (key == null)
      {
         throw new IllegalStateException("the metadata key cannot be found, id: " + id);
      }
      return key;
   }

   @Override
   public String toString()
   {
      return  getClass().getSimpleName() + "{uri=" + getUri() + ", title=" + getTitle() + ", format=" + getTechnicalFormat() + '}';
   }

   public IELO getElo()
   {
      return elo;
   }

   public IMetadata getMetatadata()
   {
      return elo.getMetadata();
   }

   public URI getUri()
   {
      return elo.getUri();
   }

   public String getTechnicalFormat()
   {
      return (String) elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue();
   }

   public void setTechnicalFormat(String technicalFormat)
   {
      elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(technicalFormat);
   }

   public String getTitle()
   {
      return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue();
   }

   public void setTitle(String title)
   {
      elo.getMetadata().getMetadataValueContainer(titleKey).setValue(title);
   }

   public String getDescription()
   {
      return (String) elo.getMetadata().getMetadataValueContainer(descriptionKey).getValue();
   }

   public void setDescription(String description)
   {
      elo.getMetadata().getMetadataValueContainer(descriptionKey).setValue(description);
   }
}
