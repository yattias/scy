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
      IMetadata mdata = tbi.getRepository().addNewELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   public void updateElo()
   {
      IMetadata mdata = tbi.getRepository().updateELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   public void saveAsForkedElo()
   {
      IMetadata mdata = tbi.getRepository().addForkedELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   private void updateMetadata(IMetadata mdata)
   {
      tbi.getELOFactory().updateELOWithResult(elo, mdata);
      metadata = elo.getMetadata();
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

   public URI getMission() {
       // TODO
       return null;
   }

   public void setMission() {
       // TODO
   }

   public void setLearningActivity(String activity) {
       // this is the method to set the LAS in which the elo has been created
       // the type String may be replaced by an ENUM
       // examples would be "experimentation", "design activity", "report"
       // (have to check with WPI documents
       // to be stored in /elo/metadata/lom/educational/context
       // TODO
   }

   public String getLearningActivity() {
       // TODO
       return null;
   }

   public void setFunctionalRole(String role) {
       // sets the functional role of an elo
       // may be replaced with an ENUM
       // examples are resource, report, hypothesis
       // to be stored in /elo/metadata/lom/educational/learningResourceType
       // TODO
   }

   public String getFunctionalRole() {
       // TODO
       return null;
   }

   public void addKeyword(String keyword) {
       // store a keyword to the list of keywords
       // at elo/metadata/lom/general/keyword
       // TODO
   }

   public void addKeywords(List<String> keywords) {
       // TODO
   }

   public List<String> getKeywords() {
       // TODO
       return null;
   }

   public void addAuthor(String authorID) {
       // adding an author requires three values to be added
       // /elo/metadata/lom/lifecycle/contribution/role = "author"
       // /elo/metadata/lom/lifecycle/contribution/entity = authorID
       // /elo/metadata/lom/lifecycle/contribution/data = dataTime (see lom specification)
       // TODO
   }

   public List<String> getAuthors() {
       // TODO
       return null;
   }

   public void setAccess(String accessTag) {
       // goes to /elol/metadata/lom/rights/copyrightAndOtherRestrictions
       // defines access limitations of an elo
       // could also define an elo as "deleted" (without actually deleting it from roolo
       // different use cases have to be decided soon
       // TODO
   }

   public String getAccess() {
       // TODO
       return null;
   }

}
