/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.net.URI;
import java.util.List;

import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 * 
 * @author SikkenJ
 */
public class ScyElo
{

   private IELO elo;
   private IMetadata metadata;
   private final ToolBrokerAPI tbi;
   private final IMetadataTypeManager metadataTypemanager;
   private final IMetadataKey identifierKey;
   private final IMetadataKey titleKey;
   private final IMetadataKey technicalFormatKey;
   private final IMetadataKey descriptionKey;
   private final IMetadataKey isForkOfKey;
   private final IMetadataKey isForkedByKey;

   private ScyElo(IELO elo, IMetadata metadata, ToolBrokerAPI tbi)
   {
      assert metadata != null;
      assert tbi != null;
      assert tbi.getMetaDataTypeManager() != null;
      this.elo = elo;
      this.metadata = metadata;
      this.tbi = tbi;
      this.metadataTypemanager = tbi.getMetaDataTypeManager();
      identifierKey = findMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      descriptionKey = findMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION);
      isForkOfKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF);
      isForkedByKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY);
   }

   public ScyElo(IELO elo, ToolBrokerAPI tbi)
   {
      this(elo, elo.getMetadata(), tbi);
   }

   public ScyElo(IMetadata metadata, ToolBrokerAPI tbi)
   {
      this(null, metadata, tbi);
   }

   public static ScyElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new ScyElo(elo, tbi);
   }

   public static ScyElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new ScyElo(elo, tbi);
   }

   public static ScyElo loadMetadata(URI uri, ToolBrokerAPI tbi)
   {
      IMetadata metadata = tbi.getRepository().retrieveMetadata(uri);
      if (metadata == null)
      {
         return null;
      }
      return new ScyElo(metadata, tbi);
   }

   public static ScyElo loadLastVersionMetadata(URI uri, ToolBrokerAPI tbi)
   {
      IMetadata metadata = tbi.getRepository().retrieveMetadataLastVersion(uri);
      if (metadata == null)
      {
         return null;
      }
      return new ScyElo(metadata, tbi);
   }

   public static ScyElo createElo(String technicalFormat, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      ScyElo scyElo = new ScyElo(elo, tbi);
      scyElo.getMetadata().getMetadataValueContainer(scyElo.technicalFormatKey).setValue(
               technicalFormat);
      return scyElo;
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

   protected static IMetadataKey getTechnicalFormatKey(ToolBrokerAPI tbi)
   {
      return tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "{uri=" + getUri() + ", title=" + getTitle()
               + ", format=" + getTechnicalFormat() + '}';
   }

   public void saveAsNewElo()
   {
      IMetadata metdata = tbi.getRepository().addNewELO(getUpdatedElo());
      tbi.getELOFactory().updateELOWithResult(elo, metdata);
   }

   public void updateElo()
   {
      IMetadata metdata = tbi.getRepository().updateELO(getUpdatedElo());
      tbi.getELOFactory().updateELOWithResult(elo, metdata);
   }

   public void saveAsForkedElo()
   {
      IMetadata metdata = tbi.getRepository().addForkedELO(getUpdatedElo());
      tbi.getELOFactory().updateELOWithResult(elo, metdata);
   }

   public IELO getElo()
   {
      return elo;
   }

   public IELO getUpdatedElo()
   {
      checkForCompleteElo();
      return this.getElo();
   }

   public IMetadata getMetadata()
   {
      return metadata;
   }

   public boolean hasOnlyMetadata()
   {
      return elo == null;
   }

   public void retrieveElo()
   {
      if (getUri() == null)
      {
         throw new IllegalStateException("ScyElo does not represent a stored elo");
      }
      if (hasOnlyMetadata())
      {
         elo = tbi.getRepository().retrieveELO(getUri());
         metadata = elo.getMetadata();
      }
   }

   protected void checkForCompleteElo()
   {
      if (hasOnlyMetadata())
      {
         retrieveElo();
      }
   }

   protected IMetadataValueContainer getMetadataValueContainer(IMetadataKey key)
   {
      return getMetadata().getMetadataValueContainer(key);
   }

   public IContent getContent()
   {
      checkForCompleteElo();
      return elo.getContent();
   }

   public URI getUri()
   {
      return (URI) getMetadataValueContainer(identifierKey).getValue();
   }

   public String getTechnicalFormat()
   {
      return (String) getMetadataValueContainer(technicalFormatKey).getValue();
   }

   protected void verifyTechnicalFormat(String technicalFormat)
   {
      if (!technicalFormat.equals(getTechnicalFormat()))
      {
         throw new IllegalStateException("elo is should have a technical format '"
                  + technicalFormat + "', but it is '" + getTechnicalFormat() + ", uri=" + getUri());
      }
   }

   public String getTitle()
   {
      return (String) getMetadataValueContainer(titleKey).getValue();
   }

   public void setTitle(String title)
   {
      getMetadataValueContainer(titleKey).setValue(title);
   }

   public String getDescription()
   {
      return (String) getMetadataValueContainer(descriptionKey).getValue();
   }

   public void setDescription(String description)
   {
      getMetadataValueContainer(descriptionKey).setValue(description);
   }

   public URI getIsForkedOfEloUri()
   {
      return (URI) getMetadataValueContainer(isForkOfKey).getValue();
   }

   @SuppressWarnings("unchecked")
   public List<URI> getIsForkedByEloUris()
   {
      return (List<URI>) getMetadataValueContainer(isForkedByKey).getValueList();
   }
}
