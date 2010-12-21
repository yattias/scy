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
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.IResource;
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
    private final IMetadataKey keywordsKey;
    private final IMetadataKey learningActivityKey;
    private final IMetadataKey accessKey;
    private final IMetadataKey missionRuntimeKey;
    private final IMetadataKey lasKey;
    private final IMetadataKey iconTypeKey;
    private final IMetadataKey assignmentUriKey;
    private final IMetadataKey resourcesUriKey;
    private static final String thumbnailResourceName = "thumbnail";
    private final static String thumbnailPngType = "png";
    private final static String thumbnailScyPngType = "scy/png";
    private final IMetadataKey socialTagsKey;
    private final IMetadataKey thumbnailKey;
    private final IMetadataKey obligatoryInPortfolioKey;

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
        learningActivityKey = findMetadataKey(ScyRooloMetadataKeyIds.LEARNING_ACTIVITY);
        accessKey = findMetadataKey(ScyRooloMetadataKeyIds.ACCESS);
        missionRuntimeKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNTIME);
        lasKey = findMetadataKey(ScyRooloMetadataKeyIds.LAS);
        iconTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.ICON_TYPE);
        keywordsKey = findMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS);
        socialTagsKey = findMetadataKey(CoreRooloMetadataKeyIds.SOCIAL_TAGS);
        thumbnailKey = findMetadataKey(CoreRooloMetadataKeyIds.THUMBNAIL);
        assignmentUriKey = findMetadataKey(ScyRooloMetadataKeyIds.ASSIGNMENT_URI);
        resourcesUriKey = findMetadataKey(ScyRooloMetadataKeyIds.RESOURCES_URI);
        obligatoryInPortfolioKey = findMetadataKey(ScyRooloMetadataKeyIds.OBLIGATORY_IN_PORTFOLIO);
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
        IMetadata mdata = rooloServices.getRepository().addNewELO(getUpdatedElo());
        updateMetadata(mdata);
    }

    public void updateElo() {
        IMetadata mdata = rooloServices.getRepository().updateELO(getUpdatedElo());
        updateMetadata(mdata);
    }

    public void saveAsForkedElo() {
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
        return (String) getMetadataValueContainer(titleKey).getValue();
    }

    public void setTitle(String title) {
        getMetadataValueContainer(titleKey).setValue(title);
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
            keywords.add(keyValuePair.getValue());
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
        return getSocialTags().getTagNames();
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
        List<Contribute> authors = (List<Contribute>) getMetadataValueContainer(authorKey).getValueList();
        List<String> authorIds = new ArrayList<String>();
        if (authors != null) {
            for (Contribute author : authors) {
                authorIds.add(author.getVCard());
            }
        }
        return authorIds;
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

//    public BufferedImage getThumbnail() {
//       BinaryMetadataValue binaryMetadataValue = (BinaryMetadataValue)getMetadataValueContainer(thumbnailKey).getValue();
//       if (binaryMetadataValue==null || binaryMetadataValue.getBinaryValue()==null){
//          return null;
//       }
//       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(binaryMetadataValue.getBinaryValue());
//       try {
//           return ImageIO.read(byteArrayInputStream);
//       } catch (IOException e) {
//           throw new ResourceException("problem with ", e);
//       }
//   }
//
//    public void setThumbnail(BufferedImage thumbnail) {
//       if (thumbnail == null) {
//          getMetadataValueContainer(thumbnailKey).setValue(null);
//          return;
//       }
//       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//       try {
//           ImageIO.write(thumbnail, thumbnailPngType, byteArrayOutputStream);
//           byte[] bytes = byteArrayOutputStream.toByteArray();
//           logger.info("setThumbnail(): nr of bytes: " + bytes.length);
//           BinaryMetadataValue binaryMetadataValue = new BinaryMetadataValue();
//           binaryMetadataValue.setBinaryValue(bytes);
//           binaryMetadataValue.setMimeType(thumbnailPngType);
//           getMetadataValueContainer(thumbnailKey).setValue(binaryMetadataValue);
//           logger.info("metadata xml after set thumbnail:\n" + getMetadata().getXml());
//       } catch (IOException e) {
//           throw new IllegalArgumentException("problems with thumbnail, " + e.getMessage(), e);
//       }
//   }

    public BufferedImage getThumbnail() {
       checkForCompleteElo();
       IResource thumbnailResource = elo.getResource(thumbnailResourceName);
       if (thumbnailResource == null) {
           return null;
       }
       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(thumbnailResource.getBytes());
       try {
           return ImageIO.read(byteArrayInputStream);
       } catch (IOException e) {
           throw new ResourceException("problem with ", e);
       }
   }

    public void setThumbnail(BufferedImage thumbnail) {
        if (thumbnail == null) {
            checkForCompleteElo();
            elo.deleteResource(thumbnailResourceName);
            logger.info("setThumbnail(): nr of bytes: null");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(thumbnail, thumbnailPngType, byteArrayOutputStream);
            IResource thumbnailResource = rooloServices.getELOFactory().createResource(
                    thumbnailResourceName);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            logger.info("setThumbnail(): nr of bytes: " + bytes.length);
            thumbnailResource.setBytes(bytes);
            thumbnailResource.setTechnicalFormat(thumbnailScyPngType);
            checkForCompleteElo();
            elo.addResource(thumbnailResource);
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
}
