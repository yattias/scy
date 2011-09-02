/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.exceptions.ResourceException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;
import roolo.elo.metadata.keys.BinaryMetadataValue;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.metadata.keys.KeyValuePair;
import roolo.elo.metadata.keys.SocialTags;

/**
 *
 * @author SikkenJ
 */
public class ScyElo {

    private static final Logger logger = Logger.getLogger(ScyElo.class);
    private IELO elo;
    private IMetadata metadata;
    private URI uriFirstVersion;
    private boolean template = false;
    private final RooloServices rooloServices;
    private final IMetadataTypeManager metadataTypemanager;
    private final IMetadataKey identifierKey;
    private final IMetadataKey titleKey;
    private final IMetadataKey technicalFormatKey;
    private final IMetadataKey descriptionKey;
    private final IMetadataKey dateCreatedKey;
    private final IMetadataKey dateLastModifiedKey;
    private final IMetadataKey isForkOfKey;
    private final IMetadataKey isForkedByKey;
    private final IMetadataKey logicalRoleKey;
    private final IMetadataKey functionalRoleKey;
    private final IMetadataKey authorKey;
    private final IMetadataKey creatorKey;
    private final IMetadataKey keywordsKey;
    private final IMetadataKey learningActivityKey;
    private final IMetadataKey accessKey;
    private final IMetadataKey missionRuntimeKey;
    private final IMetadataKey missionRunningKey;
    private final IMetadataKey lasKey;
    private final IMetadataKey iconTypeKey;
    private final IMetadataKey colorSchemeIdKey;
    private final IMetadataKey assignmentUriKey;
    private final IMetadataKey resourcesUriKey;
    private final IMetadataKey dateFirstUserSaveKey;
    private static final String thumbnailResourceName = "thumbnail";
    private final static String thumbnailPngType = "png";
    private final static String thumbnailScyPngType = "scy/png";
    private final IMetadataKey socialTagsKey;
    private final IMetadataKey thumbnailKey;
    private final IMetadataKey obligatoryInPortfolioKey;
    private final IMetadataKey feedbackOnKey;
    private final IMetadataKey mucIdKey;
    private final IMetadataKey templateKey;
    private final IMetadataKey missionIdKey;
    private final IMetadataKey finishedKey;

    private ScyElo(IELO elo, IMetadata metadata, RooloServices rooloServices) {
        assert metadata != null;
        assert rooloServices != null;
        assert rooloServices.getELOFactory() != null;
        assert rooloServices.getMetaDataTypeManager() != null;
        assert rooloServices.getRepository() != null;
        this.elo = elo;
        this.metadata = metadata;
        this.rooloServices = rooloServices;
        this.metadataTypemanager = rooloServices.getMetaDataTypeManager();
        identifierKey = findMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
        titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE);
        technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        descriptionKey = findMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION);
        dateCreatedKey = findMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED);
        dateLastModifiedKey = findMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
        isForkOfKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF);
        isForkedByKey = findMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY);
        logicalRoleKey = findMetadataKey(ScyRooloMetadataKeyIds.LOGICAL_TYPE);
        functionalRoleKey = findMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);
        authorKey = findMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
        creatorKey = findMetadataKey(ScyRooloMetadataKeyIds.CREATOR);
        learningActivityKey = findMetadataKey(ScyRooloMetadataKeyIds.LEARNING_ACTIVITY);
        accessKey = findMetadataKey(ScyRooloMetadataKeyIds.ACCESS);
        missionRuntimeKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNTIME);
        missionRunningKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
        lasKey = findMetadataKey(ScyRooloMetadataKeyIds.LAS);
        iconTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.ICON_TYPE);
        colorSchemeIdKey = findMetadataKey(ScyRooloMetadataKeyIds.COLOR_SCHEME_ID);
        keywordsKey = findMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS);
        socialTagsKey = findMetadataKey(CoreRooloMetadataKeyIds.SOCIAL_TAGS);
        thumbnailKey = findMetadataKey(CoreRooloMetadataKeyIds.THUMBNAIL);
        assignmentUriKey = findMetadataKey(ScyRooloMetadataKeyIds.ASSIGNMENT_URI);
        resourcesUriKey = findMetadataKey(ScyRooloMetadataKeyIds.RESOURCES_URI);
        obligatoryInPortfolioKey = findMetadataKey(ScyRooloMetadataKeyIds.OBLIGATORY_IN_PORTFOLIO);
        feedbackOnKey = findMetadataKey(ScyRooloMetadataKeyIds.FEEDBACK_ON);
        dateFirstUserSaveKey = findMetadataKey(ScyRooloMetadataKeyIds.DATE_FIRST_USER_SAVE);
        mucIdKey = findMetadataKey(ScyRooloMetadataKeyIds.MUC_ID);
        templateKey = findMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE);
        missionIdKey = findMetadataKey(CoreRooloMetadataKeyIds.MISSION_ID);
        finishedKey = findMetadataKey(CoreRooloMetadataKeyIds.FINISHED);
    }

    public ScyElo(IELO elo, RooloServices rooloServices) {
        this(elo, elo.getMetadata(), rooloServices);
    }

    public ScyElo(IMetadata metadata, RooloServices rooloServices) {
        this(null, metadata, rooloServices);
    }

    public static ScyElo loadElo(URI uri, RooloServices rooloServices) {
        IELO elo = rooloServices.getRepository().retrieveELO(uri);
        if (elo == null) {
            return null;
        }
        return new ScyElo(elo, rooloServices);
    }

    public static ScyElo loadLastVersionElo(URI uri, RooloServices rooloServices) {
        IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
        if (elo == null) {
            return null;
        }
        return new ScyElo(elo, rooloServices);
    }

    public static ScyElo loadMetadata(URI uri, RooloServices rooloServices) {
        IMetadata metadata = rooloServices.getRepository().retrieveMetadata(uri);
        if (metadata == null) {
            return null;
        }
        return new ScyElo(metadata, rooloServices);
    }

    public static ScyElo loadLastVersionMetadata(URI uri, RooloServices rooloServices) {
        IMetadata metadata = rooloServices.getRepository().retrieveMetadataLastVersion(uri);
        if (metadata == null) {
            return null;
        }
        return new ScyElo(metadata, rooloServices);
    }

    public static ScyElo createElo(String technicalFormat, RooloServices rooloServices) {
        IELO elo = rooloServices.getELOFactory().createELO();
        ScyElo scyElo = new ScyElo(elo, rooloServices);
        scyElo.getMetadata().getMetadataValueContainer(scyElo.technicalFormatKey).setValue(
                technicalFormat);
        return scyElo;
    }

    protected final IMetadataKey findMetadataKey(IMetadataKeyIdDefinition id) {
        IMetadataKey key = metadataTypemanager.getMetadataKey(id);
        if (key == null) {
            throw new IllegalStateException("the metadata key cannot be found, id: " + id);
        }
        return key;
    }

    protected static IMetadataKey getTechnicalFormatKey(RooloServices rooloServices) {
        return rooloServices.getMetaDataTypeManager().getMetadataKey(
                CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    protected RooloServices getRooloServices() {
        return rooloServices;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{uri=" + getUri() + ", title=" + getTitle()
                + ", format=" + getTechnicalFormat() + '}';
    }

    public void saveAsNewElo() {
       if (template){
          setTemplate(template);
       }
        IMetadata mdata = rooloServices.getRepository().addNewELO(getUpdatedElo());
        updateMetadata(mdata);
    }

    public void updateElo() {
       if (template){
          setTemplate(template);
       }
        IMetadata mdata = rooloServices.getRepository().updateELO(getUpdatedElo());
        updateMetadata(mdata);
    }

    public void saveAsForkedElo() {
       if (template){
          setTemplate(template);
       }
        IMetadata mdata = rooloServices.getRepository().addForkedELO(getUpdatedElo());
        updateMetadata(mdata);
    }

    private void updateMetadata(IMetadata mdata) {
        rooloServices.getELOFactory().updateELOWithResult(elo, mdata);
        metadata = elo.getMetadata();
    }

    public IELO getElo() {
        return elo;
    }

    public IELO getUpdatedElo() {
        checkForCompleteElo();
        return this.getElo();
    }

    public boolean reloadFrom(URI eloUri) {
        final IELO newElo = rooloServices.getRepository().retrieveELO(eloUri);
        if (newElo == null) {
            return false;
        }
        uriFirstVersion = null;
        metadata = newElo.getMetadata();
        if (!hasOnlyMetadata()) {
            elo = newElo;
        }
        verifyTechnicalFormat();
        return true;
    }

    public IMetadata getMetadata() {
        return metadata;
    }

    public boolean hasOnlyMetadata() {
        return elo == null;
    }

    public void retrieveElo() {
        if (getUri() == null) {
            throw new IllegalStateException("ScyElo does not represent a stored elo");
        }
        if (hasOnlyMetadata()) {
            elo = rooloServices.getRepository().retrieveELO(getUri());
            elo.setMetadata(metadata);
        }
    }

    protected void checkForCompleteElo() {
        if (hasOnlyMetadata()) {
            retrieveElo();
        }
    }

    protected IMetadataValueContainer getMetadataValueContainer(IMetadataKey key) {
        return getMetadata().getMetadataValueContainer(key);
    }

    public IContent getContent() {
        checkForCompleteElo();
        return elo.getContent();
    }

    public URI getUri() {
        return (URI) getMetadataValueContainer(identifierKey).getValue();
    }

    public URI getUriFirstVersion() {
        if (uriFirstVersion == null) {
            if (getUri() == null) {
                return null;
            }
            IMetadata metadataFirstVersion = rooloServices.getRepository().retrieveMetadataFirstVersion(getUri());
            if (metadataFirstVersion != null) {
                uriFirstVersion = (URI) metadataFirstVersion.getMetadataValueContainer(identifierKey).getValue();
            }
        }
        return uriFirstVersion;
    }

    public String getTechnicalFormat() {
        return (String) getMetadataValueContainer(technicalFormatKey).getValue();
    }

    protected void verifyTechnicalFormat() {
    }

    public String getTitle() {
       String title = (String) getMetadataValueContainer(titleKey).getValue(Locale.getDefault());
       if (title!=null){
          return title;
       }
        return (String) getMetadataValueContainer(titleKey).getValue();
    }

    public String getTitle(Locale language) {
       return (String) getMetadataValueContainer(titleKey).getValue(language);
    }

    public void setTitle(String title) {
//        getMetadataValueContainer(titleKey).setValue(title);
        getMetadataValueContainer(titleKey).setValue(title,Locale.getDefault());
    }

    public void setTitle(String title, Locale langauge) {
        getMetadataValueContainer(titleKey).setValue(title,langauge);
    }

    public String getDescription() {
        return (String) getMetadataValueContainer(descriptionKey).getValue();
    }

    public void setDescription(String description) {
        getMetadataValueContainer(descriptionKey).setValue(description);
    }

    public Long getDateCreated() {
        return (Long) getMetadataValueContainer(dateCreatedKey).getValue();
    }

    public Long getDateLastModified() {
       return (Long) getMetadataValueContainer(dateLastModifiedKey).getValue();
   }

    public Long getDateFirstUserSave() {
       return (Long) getMetadataValueContainer(dateFirstUserSaveKey).getValue();
   }

    public void setDateFirstUserSave(Long date) {
       getMetadataValueContainer(dateFirstUserSaveKey).setValue(date);
   }

    public URI getIsForkedOfEloUri() {
        return (URI) getMetadataValueContainer(isForkOfKey).getValue();
    }

    @SuppressWarnings("unchecked")
    public List<URI> getIsForkedByEloUris() {
        return (List<URI>) getMetadataValueContainer(isForkedByKey).getValueList();
    }

    public URI getMissionRuntimeEloUri() {
       return (URI) getMetadataValueContainer(missionRuntimeKey).getValue();
   }

   public void setMissionRuntimeEloUri(URI uri) {
       getMetadataValueContainer(missionRuntimeKey).setValue(uri);
   }

   public URI getMissionSpecificationEloUri() {
      return (URI) getMetadataValueContainer(missionRunningKey).getValue();
   }

   public void setMissionSpecificationEloUri(URI uri) {
      getMetadataValueContainer(missionRunningKey).setValue(uri);
   }

   public String getMissionId() {
      return (String) getMetadataValueContainer(missionIdKey).getValue();
   }

   public void setMissionId(String missionId) {
      getMetadataValueContainer(missionIdKey).setValue(missionId);
   }

   public void setFinished(boolean finished){
      getMetadataValueContainer(finishedKey).setValue(Boolean.toString(finished));
   }

   public boolean isFinished() {
      return Boolean.parseBoolean((String) getMetadataValueContainer(finishedKey).getValue());
   }

    public void setLearningActivity(LearningActivity activity) {
        // this is the method to set the LAS in which the elo has been created
        // the type String may be replaced by an ENUM
        // examples would be "experimentation", "design activity", "report"
        // (have to check with WPI documents
        // to be stored in /elo/metadata/lom/educational/context
        String activityString = "";
        if (activity != null) {
            activityString = activity.toString();
        }
        getMetadataValueContainer(learningActivityKey).setValue(activityString);
    }

    public LearningActivity getLearningActivity() {
        String value = (String) getMetadataValueContainer(learningActivityKey).getValue();
        if (value != null) {
            return LearningActivity.myValueOf(value);
        }
        return null;
    }

    public void setFunctionalRole(EloFunctionalRole role) {
        // sets the functional role of an elo
        // may be replaced with an ENUM
        // examples are resource, report, hypothesis
        // to be stored in /elo/metadata/lom/educational/learningResourceType
        String roleString = "";
        if (role != null) {
            roleString = role.toString();
        }
        getMetadataValueContainer(functionalRoleKey).setValue(roleString);
    }

    public EloFunctionalRole getFunctionalRole() {
        String value = (String) getMetadataValueContainer(functionalRoleKey).getValue();
        if (value != null) {
            try {
                return EloFunctionalRole.myValueOf(value);
            } catch (IllegalArgumentException e) {
                logger.info("unkown functional role value (" + value + ") in elo: " + getUri());
            }
        }
        return null;
    }

    public void setLogicalRole(EloLogicalRole role) {
        // sets the logical role of an elo
        // may be replaced with an ENUM
        // examples are resource, report, hypothesis
        // to be stored in /elo/metadata/lom/educational/learningResourceType?????????????????
        String roleString = "";
        if (role != null) {
            roleString = role.toString();
        }
        getMetadataValueContainer(logicalRoleKey).setValue(roleString);
    }

    public EloLogicalRole getLogicalRole() {
        String value = (String) getMetadataValueContainer(logicalRoleKey).getValue();
        if (value != null) {
            try {
                return EloLogicalRole.myValueOf(value);
            } catch (Exception e) {
                logger.info("unkown logical role value (" + value + ") in elo: " + getUri());
            }
        }
        return null;
    }

    public void addKeyword(String keyword) {
        // store a keyword to the list of keywords
        // at elo/metadata/lom/general/keyword
        addKeyword(keyword, new Float(1.0f));
    }

    /**
     *
     * @param keyword
     * @param boost dont use it if you are not sure what it means ;-)
     */
    public void addKeyword(String keyword, Float boost) {
        // store a keyword to the list of keywords
        // at elo/metadata/lom/general/keyword
        if (boost == null) {
            boost = new Float(1.0f);
        }
        KeyValuePair value = new KeyValuePair(keyword, boost.toString());
        getMetadataValueContainer(keywordsKey).addValue(value);
    }

    public void addKeywords(List<String> keywords) {
        for (String keyword : keywords) {
            addKeyword(keyword);
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<String>();
        final List<KeyValuePair> keywordPairs = (List<KeyValuePair>) getMetadataValueContainer(keywordsKey).getValueList();
        for (KeyValuePair keyValuePair : keywordPairs) {
            keywords.add(keyValuePair.getKey());
        }
        return keywords;
    }

    public SocialTags getSocialTags(){
        return (SocialTags) getMetadataValueContainer(socialTagsKey).getValue();
    }

    public void addSocialTag(String tagName, String username){
        getSocialTags().addSocialTag(tagName, username);
    }

    public boolean containsSocialTag(String tagName){
        return getSocialTags().containsTag(tagName);
    }

    public boolean containsSocialTag(String tagName, String username){
        return getSocialTags().containsTag(tagName);
    }

    public Set<String> getTagNames(){
        SocialTags st = getSocialTags();
        if (st != null) {
            return st.getTagNames();
        } else {
            return new HashSet<String>();
        }
    }

    public void removeSocialTag(String tagName, String username){
        getSocialTags().removeContributeFromTag(tagName, username);
    }

    public BinaryMetadataValue getMetadataThumbnail(){
        return (BinaryMetadataValue) getMetadataValueContainer(thumbnailKey).getValue();
    }

    public void setMetadataThumbnail(BinaryMetadataValue binaryMetadataValue){
        getMetadataValueContainer(thumbnailKey).setValue(binaryMetadataValue);
    }

    public void addAuthor(String authorID) {
        // adding an author requires three values to be added
        // /elo/metadata/lom/lifecycle/contribution/role = "author"
        // /elo/metadata/lom/lifecycle/contribution/entity = authorID
        // /elo/metadata/lom/lifecycle/contribution/data = dataTime (see lom specification)
        getMetadataValueContainer(authorKey).addValue(
                new Contribute(authorID, System.currentTimeMillis()));
    }

    public void removeAuthor(String authorID) {
        List values = getMetadataValueContainer(authorKey).getValueList();
        int indexToDelete = -1;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) instanceof Contribute) {
                Contribute c = (Contribute) values.get(i);
                if (c.getVCard().equals(authorID)) {
                    indexToDelete = i;
                    break;
                }
            }
        }
        if (indexToDelete != -1) {
            values.remove(indexToDelete);
        }
    }

    public void setAuthors(List<String> authorIds) {
        List<Contribute> authors = new ArrayList<Contribute>();
        for (String authorId : authorIds) {
            authors.add(new Contribute(authorId, System.currentTimeMillis()));
        }
        getMetadataValueContainer(authorKey).setValueList(authors);
    }

    public void setAuthor(String authorId) {
        List<String> authorIds = new ArrayList<String>();
        authorIds.add(authorId);
        setAuthors(authorIds);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthors() {
        List<Object> authors = (List<Object>) getMetadataValueContainer(authorKey).getValueList();
        List<String> authorIds = new ArrayList<String>();
        if (authors != null) {
            for (Object author : authors) {
                if (author instanceof String) {
                    authorIds.add((String)author);
                } else if (author instanceof Contribute) {
                    authorIds.add(((Contribute) author).getVCard());
                }
            }
        }
        return authorIds;
    }

    public String getCreator(){
       Contribute creator = (Contribute) getMetadataValueContainer(creatorKey).getValue();
       if (creator!=null){
      	 return creator.getVCard();
       }
       return null;
    }

    public void setCreator(String creatorId){
   	 if (creatorId!=null && creatorId.length()>0){
   		 getMetadataValueContainer(creatorKey).setValue(new Contribute(creatorId,System.currentTimeMillis()));
   	 }
   	 else{
   		 getMetadata().deleteMetatadata(creatorKey);
   	 }
    }

    public void setAccess(Access access) {
        // goes to /elol/metadata/lom/rights/copyrightAndOtherRestrictions
        // defines access limitations of an elo
        // could also define an elo as "deleted" (without actually deleting it from roolo
        // different use cases have to be decided soon
        String accessString = "";
        if (access != null) {
            accessString = access.toString();
        }
        getMetadataValueContainer(accessKey).setValue(accessString);
    }

    public Access getAccess() {
        String value = (String) getMetadataValueContainer(accessKey).getValue();
        if (value != null) {
            return Access.valueOf(value);
        }
        return null;
    }

    public BufferedImage getThumbnail() {
       BinaryMetadataValue binaryMetadataValue = (BinaryMetadataValue)getMetadataValueContainer(thumbnailKey).getValue();
       if (binaryMetadataValue==null || binaryMetadataValue.getBytes()==null){
          return null;
       }
       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(binaryMetadataValue.getBytes());
       try {
           return ImageIO.read(byteArrayInputStream);
       } catch (IOException e) {
           throw new ResourceException("problem with ", e);
       }
   }

    public void setThumbnail(BufferedImage thumbnail) {
       if (thumbnail == null) {
          getMetadataValueContainer(thumbnailKey).setValue(null);
          return;
       }
       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       try {
           ImageIO.write(thumbnail, thumbnailPngType, byteArrayOutputStream);
           byte[] bytes = byteArrayOutputStream.toByteArray();
           logger.info("setThumbnail(): nr of bytes: " + bytes.length);
           BinaryMetadataValue binaryMetadataValue = new BinaryMetadataValue();
           binaryMetadataValue.setBytes(bytes);
           binaryMetadataValue.setMimeType(thumbnailPngType);
           getMetadataValueContainer(thumbnailKey).setValue(binaryMetadataValue);
//           logger.info("metadata xml after set thumbnail:\n" + getMetadata().getXml());
       } catch (IOException e) {
           throw new IllegalArgumentException("problems with thumbnail, " + e.getMessage(), e);
       }
   }

    public String getLasId() {
        return (String) getMetadataValueContainer(lasKey).getValue();
    }

    public void setLasId(String lasId) {
        getMetadataValueContainer(lasKey).setValue(lasId);
    }

    public String getIconType() {
       return (String) getMetadataValueContainer(iconTypeKey).getValue();
   }

   public void setIconType(String iconType) {
       getMetadataValueContainer(iconTypeKey).setValue(iconType);
   }

   public ColorSchemeId getColorSchemeId() {
      String colorSchemeIdString = (String) getMetadataValueContainer(colorSchemeIdKey).getValue();
      if (colorSchemeIdString!=null && colorSchemeIdString.length()>00){
      	try
			{
				return ColorSchemeId.valueOf(colorSchemeIdString.toUpperCase());
			}
			catch (Exception e)
			{
				logger.warn("unknown value for colorSchemeId: " + colorSchemeIdString + " in elo " + getUri() + ", " + e.getMessage());
			}
      }
      return null;
  }

  public void setColorSchemeId(ColorSchemeId colorSchemeId) {
      getMetadataValueContainer(colorSchemeIdKey).setValue(colorSchemeId==null?null:colorSchemeId.toString());
  }

    public URI getAssignmentUri() {
       return (URI) getMetadataValueContainer(assignmentUriKey).getValue();
   }

   public void setAssignmentUri(URI assignmentUri) {
       getMetadataValueContainer(assignmentUriKey).setValue(assignmentUri);
   }

   public URI getResourcesUri() {
      return (URI) getMetadataValueContainer(resourcesUriKey).getValue();
   }

   public void setResourcesUri(URI resourcesUri) {
      getMetadataValueContainer(resourcesUriKey).setValue(resourcesUri);
   }

   public Boolean getObligatoryInPortfolio() {
      // needed to do a little trickery to convert a string to boolesn, because of lack of a boolean metadata key
      String value = (String) getMetadataValueContainer(obligatoryInPortfolioKey).getValue();
      if (value==null){
         return null;
      }
      return Boolean.valueOf(value);
   }

   public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio) {
     // needed to do a little trickery to convert a boolean to string, because of lack of a boolean metadata key
     String value = null;
     if (obligatoryInPortfolio!=null){
        value = obligatoryInPortfolio.toString();
     }
     getMetadataValueContainer(obligatoryInPortfolioKey).setValue(value);
  }

   public URI getFeedbackOnEloUri() {
      return (URI) getMetadataValueContainer(feedbackOnKey).getValue();
  }

  public void setFeedbackOnEloUri(URI uri) {
      getMetadataValueContainer(feedbackOnKey).setValue(uri);
  }

  public String getMucId() {
      return (String) getMetadataValueContainer(mucIdKey).getValue();
  }

  public void setMucId(String mucId) {
      getMetadataValueContainer(mucIdKey).setValue(mucId);
  }

   public Boolean getTemplate() {
      // needed to do a little trickery to convert a string to boolesn, because of lack of a boolean metadata key
      String value = (String) getMetadataValueContainer(templateKey).getValue();
      if (value==null){
         return null;
      }
      return Boolean.valueOf(value);
   }

   public void setTemplate(Boolean template) {
     // needed to do a little trickery to convert a boolean to string, because of lack of a boolean metadata key
     String value = null;
     if (template!=null){
        value = template.toString();
        this.template = Boolean.valueOf(template);
     }
     getMetadataValueContainer(templateKey).setValue(value);
  }

}
