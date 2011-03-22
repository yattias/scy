/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;
import roolo.elo.metadata.keys.Contribute;

/**
 * 
 * @author SikkenJ
 */
public class ScyElo
{

   private IELO elo;
   private IMetadata metadata;
   private URI uriFirstVersion;
   private final RooloServices rooloServices;
   private final IMetadataTypeManager metadataTypemanager;
   private final IMetadataKey identifierKey;
   private final IMetadataKey titleKey;
   private final IMetadataKey technicalFormatKey;
   private final IMetadataKey descriptionKey;
   private final IMetadataKey isForkOfKey;
   private final IMetadataKey isForkedByKey;
   private final IMetadataKey logicalRoleKey;
   private final IMetadataKey functionalRoleKey;
   private final IMetadataKey authorKey;
   private final IMetadataKey keywordsKey;
   private final IMetadataKey learningActivityKey;
   private final IMetadataKey accessKey;
   private final IMetadataKey missionRuntimeKey;

   private ScyElo(IELO elo, IMetadata metadata, RooloServices rooloServices)
   {
      assert metadata != null;
      assert rooloServices != null;
      assert rooloServices.getMetaDataTypeManager() != null;
      this.elo = elo;
      this.metadata = metadata;
      this.rooloServices = rooloServices;
      this.metadataTypemanager = rooloServices.getMetaDataTypeManager();
      identifierKey = findMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      descriptionKey = findMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION);
      isForkOfKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF);
      isForkedByKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY);
      logicalRoleKey = findMetadataKey(ScyRooloMetadataKeyIds.LOGICAL_TYPE);
      functionalRoleKey = findMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);
      authorKey = findMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
      keywordsKey = findMetadataKey(ScyRooloMetadataKeyIds.KEYWORDS);
      learningActivityKey = findMetadataKey(ScyRooloMetadataKeyIds.LEARNING_ACTIVITY);
      accessKey = findMetadataKey(ScyRooloMetadataKeyIds.ACCESS);
      missionRuntimeKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNTIME);
   }

   public ScyElo(IELO elo, RooloServices rooloServices)
   {
      this(elo, elo.getMetadata(), rooloServices);
   }

   public ScyElo(IMetadata metadata, RooloServices rooloServices)
   {
      this(null, metadata, rooloServices);
   }

   public static ScyElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new ScyElo(elo, rooloServices);
   }

   public static ScyElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new ScyElo(elo, rooloServices);
   }

   public static ScyElo loadMetadata(URI uri, RooloServices rooloServices)
   {
      IMetadata metadata = rooloServices.getRepository().retrieveMetadata(uri);
      if (metadata == null)
      {
         return null;
      }
      return new ScyElo(metadata, rooloServices);
   }

   public static ScyElo loadLastVersionMetadata(URI uri, RooloServices rooloServices)
   {
      IMetadata metadata = rooloServices.getRepository().retrieveMetadataLastVersion(uri);
      if (metadata == null)
      {
         return null;
      }
      return new ScyElo(metadata, rooloServices);
   }

   public static ScyElo createElo(String technicalFormat, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      ScyElo scyElo = new ScyElo(elo, rooloServices);
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

   protected static IMetadataKey getTechnicalFormatKey(RooloServices rooloServices)
   {
      return rooloServices.getMetaDataTypeManager().getMetadataKey(
               CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "{uri=" + getUri() + ", title=" + getTitle()
               + ", format=" + getTechnicalFormat() + '}';
   }

   public void saveAsNewElo()
   {
      IMetadata mdata = rooloServices.getRepository().addNewELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   public void updateElo()
   {
      IMetadata mdata = rooloServices.getRepository().updateELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   public void saveAsForkedElo()
   {
      IMetadata mdata = rooloServices.getRepository().addForkedELO(getUpdatedElo());
      updateMetadata(mdata);
   }

   private void updateMetadata(IMetadata mdata)
   {
      rooloServices.getELOFactory().updateELOWithResult(elo, mdata);
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
         elo = rooloServices.getRepository().retrieveELO(getUri());
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

   public URI getUriFirstVersion()
   {
      if (uriFirstVersion == null)
      {
         if (getUri() == null)
         {
            return null;
         }
         IMetadata metadataFirstVersion = rooloServices.getRepository()
                  .retrieveMetadataFirstVersion(getUri());
         if (metadataFirstVersion != null)
         {
            uriFirstVersion = (URI) metadataFirstVersion.getMetadataValueContainer(identifierKey)
                     .getValue();
         }
      }
      return uriFirstVersion;
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

   public URI getMissionRuntimeEloUri()
   {
      return (URI) getMetadataValueContainer(missionRuntimeKey).getValue();
   }

   public void setMissionRuntimeEloUri(URI uri)
   {
      getMetadataValueContainer(missionRuntimeKey).setValue(uri);
   }

   public void setLearningActivity(LearningActivity activity)
   {
      // this is the method to set the LAS in which the elo has been created
      // the type String may be replaced by an ENUM
      // examples would be "experimentation", "design activity", "report"
      // (have to check with WPI documents
      // to be stored in /elo/metadata/lom/educational/context
      String activityString = "";
      if (activity != null)
      {
         activityString = activity.toString();
      }
      getMetadataValueContainer(learningActivityKey).setValue(activityString);
   }

   public LearningActivity getLearningActivity()
   {
      String value = (String) getMetadataValueContainer(learningActivityKey).getValue();
      if (value != null)
      {
         return LearningActivity.valueOf(value);
      }
      return null;
   }

   public void setFunctionalRole(EloFunctionalRole role)
   {
      // sets the functional role of an elo
      // may be replaced with an ENUM
      // examples are resource, report, hypothesis
      // to be stored in /elo/metadata/lom/educational/learningResourceType
      String roleString = "";
      if (role != null)
      {
         roleString = role.toString();
      }
      getMetadataValueContainer(functionalRoleKey).setValue(roleString);
   }

   public EloFunctionalRole getFunctionalRole()
   {
      String value = (String) getMetadataValueContainer(functionalRoleKey).getValue();
      if (value != null)
      {
         return EloFunctionalRole.valueOf(value);
      }
      return null;
   }

   public void setLogicalRole(EloLogicalRole role)
   {
      // sets the logical role of an elo
      // may be replaced with an ENUM
      // examples are resource, report, hypothesis
      // to be stored in /elo/metadata/lom/educational/learningResourceType?????????????????
      String roleString = "";
      if (role != null)
      {
         roleString = role.toString();
      }
      getMetadataValueContainer(logicalRoleKey).setValue(roleString);
   }

   public EloLogicalRole getLogicalRole()
   {
      String value = (String) getMetadataValueContainer(logicalRoleKey).getValue();
      if (value != null)
      {
         return EloLogicalRole.valueOf(value);
      }
      return null;
   }

   public void addKeyword(String keyword)
   {
      // store a keyword to the list of keywords
      // at elo/metadata/lom/general/keyword
      getMetadataValueContainer(keywordsKey).addValue(keyword);
   }

   public void addKeywords(List<String> keywords)
   {
      for (String keyword : keywords)
      {
         getMetadataValueContainer(keywordsKey).addValue(keyword);
      }
   }

   @SuppressWarnings("unchecked")
   public List<String> getKeywords()
   {
      return (List<String>) getMetadataValueContainer(keywordsKey).getValueList();
   }

   public void addAuthor(String authorID)
   {
      // adding an author requires three values to be added
      // /elo/metadata/lom/lifecycle/contribution/role = "author"
      // /elo/metadata/lom/lifecycle/contribution/entity = authorID
      // /elo/metadata/lom/lifecycle/contribution/data = dataTime (see lom specification)
      getMetadataValueContainer(authorKey).addValue(new Contribute(authorID, System.currentTimeMillis()));
   }

   @SuppressWarnings("unchecked")
   public List<String> getAuthors()
   {
      List<Contribute> authors = (List<Contribute>) getMetadataValueContainer(authorKey).getValueList();
      List<String> authorIds = new ArrayList<String>();
      if (authors!=null){
         for (Contribute author: authors){
            authorIds.add(author.getVCard());
         }
      }
      return authorIds;
   }

   public void setAccess(Access access)
   {
      // goes to /elol/metadata/lom/rights/copyrightAndOtherRestrictions
      // defines access limitations of an elo
      // could also define an elo as "deleted" (without actually deleting it from roolo
      // different use cases have to be decided soon
      String accessString = "";
      if (access != null)
      {
         accessString = access.toString();
      }
      getMetadataValueContainer(accessKey).setValue(accessString);
   }

   public Access getAccess()
   {
      String value = (String) getMetadataValueContainer(accessKey).getValue();
      if (value != null)
      {
         return Access.valueOf(value);
      }
      return null;
   }

}