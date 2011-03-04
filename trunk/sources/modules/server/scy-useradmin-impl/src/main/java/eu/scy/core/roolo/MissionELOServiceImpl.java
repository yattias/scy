package eu.scy.core.roolo;

import eu.scy.common.mission.*;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.*;
import roolo.elo.api.IELO;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.okt.2010
 * Time: 06:16:46
 * To change this template use File | Settings | File Templates.
 */
public class MissionELOServiceImpl extends BaseELOServiceImpl implements MissionELOService {

    private static Logger log = Logger.getLogger("MissionELOServiceImpl.class");

    private static final String GLOBAL_MISSION_SCAFFOLDING_LEVEL = "globalMissionScaffoldingLevel";

    private static final RuntimeSettingKey globalMissionScaffoldingLevelKey = new RuntimeSettingKey(GLOBAL_MISSION_SCAFFOLDING_LEVEL, null, null);

    private XMLTransferObjectService xmlTransferObjectService;

    @Override
    public MissionSpecificationElo createMissionSpecification(String title, String description, String author) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.createElo(this);
        missionSpecificationElo.setDescription(description);
        missionSpecificationElo.setTitle(title);

        missionSpecificationElo.addAuthor(author);

        ScyElo missionRuntimeElo = getElo(missionSpecificationElo.getMissionRuntimeEloUri());

        IMetadata newMissionRuntimeEloMetadata = getRepository().addForkedELO(missionRuntimeElo.getElo());


        IMetadata metaData = getRepository().addForkedELO(missionSpecificationElo.getElo());


        missionSpecificationElo.saveAsNewElo();


        return missionSpecificationElo;
    }

    @Override
    public List getMissionSpecifications(String author) {
        return getMissionSpecificationsByAuthor(author);
    }

    @Override
    public void createMissionSpecification(MissionSpecificationElo missionSpecificationElo, String authorUserName) {

        missionSpecificationElo.saveAsForkedElo();
        missionSpecificationElo.setTitle("New plan");
        missionSpecificationElo.addAuthor(authorUserName);

        RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo.loadElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
        runtimeSettingsElo.setTitle("runtime for " + missionSpecificationElo.getTitle());
        runtimeSettingsElo.saveAsForkedElo(); //new copy of runtime settings has been created

        missionSpecificationElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
        missionSpecificationElo.updateElo();
        setGlobalMissionScaffoldingLevel(missionSpecificationElo, 2);

    }

    @Override
    public void setTitle(ScyElo scyElo, Object value) {
        scyElo.setTitle((String) value);
        scyElo.updateElo();
    }

    @Override
    public String getTitle(ScyElo scyElo) {
        return scyElo.getTitle();
    }

    @Override
    public List getAssignedUserNamesFor(MissionSpecificationElo missionSpecificationElo) {
        List<ScyElo> runtimeModels = getRuntimeElos(missionSpecificationElo);
        List userNames = new LinkedList();

        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            log.info(missionRuntimeElo.getContent().getXmlString());
            if (missionRuntimeElo != null) {
                if (missionRuntimeElo.getTitle().equals(missionSpecificationElo.getTitle())) {
                    String userName = missionRuntimeElo.getUserRunningMission();
                    userNames.add(userName);
                } else {
                    log.info("TITLE " + missionRuntimeElo.getTitle() + " DOES NOT EQUAL: " + missionSpecificationElo.getTitle());
                    //HE HE : NOT MY PROUDEST MOMENT
                }
            }


        }

        return userNames;

    }


    @Override
    public MissionSpecificationElo getMissionSpecificationELO(URI missionSpecificationURI) {
        if (missionSpecificationURI != null) {
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionSpecificationURI, this);
            ScyElo elo = MissionSpecificationElo.loadElo(missionSpecificationElo.getUri(), this);
            ScyElo.loadMetadata(elo.getUri(), this);
        } else {
            log.info("THE MISSION SPECIFICATION URI IS NULL!");
        }

        return null;
    }

    @Override
    public MissionSpecificationElo getMissionSpecificationELOForRuntume(MissionRuntimeElo missionRuntimeElo) {


        URI uri =  missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri();
        return MissionSpecificationElo.loadLastVersionElo(uri, this);
/*
        List missionSpecifications = getMissionSpecifications();
        ScyElo returnElo = null;
        for (int i = 0; i < missionSpecifications.size(); i++) {
            ScyElo missionSpeicification = (ScyElo) missionSpecifications.get(i);
            log.info("SEARCHING FOR MISSION WITH THE NAME: " + missionRuntimeElo.getTitle() + " COMPARING TO " + missionSpeicification.getTitle());
            if (missionRuntimeElo.getTitle().equals(missionRuntimeElo.getTitle())) {
                log.info("FOUND IT: " + missionSpeicification.getUri());
                returnElo = missionSpeicification;

            }

        }
        */

       // if(returnElo != null) return MissionSpecificationElo.loadElo(returnElo.getUri(), this);

        //return null;
    }


    @Override
    public void setGlobalMissionScaffoldingLevel(ScyElo scyElo, Object value) {
        if (value instanceof String) value = Integer.valueOf((String) value);
        Integer scaffoldingLevel = (Integer) value;

        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo runtimeSettingsElo = getRuntimeSettingsElo(missionSpecificationElo);
        ((RuntimeSettingsHelper) runtimeSettingsElo.getPropertyAccessor()).setScaffoldingLevel(scaffoldingLevel);
    }

    @Override
    public Integer getGlobalMissionScaffoldingLevel(ScyElo scyElo) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo elo = getRuntimeSettingsElo(missionSpecificationElo);
        return ((RuntimeSettingsHelper) elo.getPropertyAccessor()).getScaffoldingLevel();
    }

    @Override
    public List<Las> getLasses(MissionSpecificationElo missionSpecificationElo) {
        MissionModelElo missionModel = MissionModelElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), this);
        return missionModel.getTypedContent().getLasses();//what is  this? A getter??
    }

    @Override
    public List getAnchorELOs(MissionSpecificationElo missionSpecificationElo) {

        log.info("LOADING ANCHOR ELOS FOR MISSION SPEC: " + missionSpecificationElo.getUri());

        MissionModelElo missionModel = MissionModelElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), this);
        missionModel.getMissionModel().loadMetadata(this);
        List lasses = missionModel.getTypedContent().getLasses();//what is  this? A getter??

        log.info("*** *** MISSION URI: " + missionModel.getUri());

        List missionAnchors = new LinkedList();
        for (int i = 0; i < lasses.size(); i++) {
            Las las = (Las) lasses.get(i);
            MissionAnchor missionAnchor = las.getMissionAnchor();
            if (missionAnchor != null) {
                ScyElo missionAnchorElo = ScyElo.loadLastVersionElo(missionAnchor.getEloUri(), this);
                missionAnchors.add(missionAnchorElo);
                if (missionAnchorElo != null) {
                    log.info("MISSION ANCHOR: " + missionAnchorElo.getTitle() + " OBLIGATORY: " + missionAnchorElo.getObligatoryInPortfolio());
                } else {
                    log.info("MISSION SCY ELO IS NULL:" + missionAnchor.getIconType());
                }

            }

        }

        log.info("Returning : " + missionAnchors.size() + " MISSION ANCHORS");

        return missionAnchors;
    }

    private RuntimeSettingsElo getRuntimeSettingsElo(MissionSpecificationElo missionSpecificationElo) {
        return RuntimeSettingsElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
    }

    @Override
    public List getMissionSpecifications() {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionSpecificationQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType());
        IQuery missionSpecificationQuery = new Query(missionSpecificationQueryComponent);
        return getELOs(missionSpecificationQuery);
    }

    public List getMissionSpecificationsByAuthor(String author) {
        return getMissionSpecifications();
        /*final IMetadataKey authorKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        IQuery missionSpecificationQuery = new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, author);
        return getELOs(missionSpecificationQuery);
        */
    }

    @Override
    public List findElosFor(URI missionURI, String username) {

        //IQueryComponent bmq1 = new MetadataQueryComponent(getMetaDataTypeManager().getMetadataKey("mission"), SearchOperation.EQUALS, "ecomission"); //e.g. mission = "ecoMission"
        IQueryComponent bmq2 = new MetadataQueryComponent(getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR), SearchOperation.EQUALS, username);   //e.g. author = "jan"

        log.info("Loading elos for mission with uri: " + missionURI);




        //AndQuery aq = new AndQuery(bmq1, bmq2);
        IQuery q = new Query(bmq2);
        List<ISearchResult> results = getRepository().search(q);
        List elos = new LinkedList();
        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = results.get(i);
            ScyElo scyElo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
            //if(scyElo.getMissionSpecificationEloUri().equals(missionURI)) {
                elos.add(getRepository().retrieveELO(searchResult.getUri()));
            //}

        }

        return elos;
    }

    @Override
    public List getPortfoliosThatAreReadyForAssessment(MissionSpecificationElo missionSpecificationElo) {
        List runtimeElos = getRuntimeElos(missionSpecificationElo);
        List returnList = new LinkedList();
        for (int i = 0; i < runtimeElos.size(); i++) {
            ScyElo shittyElo = (ScyElo) runtimeElos.get(i);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(shittyElo.getUri(), this);
            URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
            if (portfolioURI != null) {
                ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
                String xml = scyElo.getContent().getXmlString();
                if (xml != null) {
                    Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(xml);
                    portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
                    if(portfolio.getIsPortfolioSubmitted() && portfolio.getIsPortfolioAssessed() == false) {
                        returnList.add(portfolio);
                    } else {
                        log.info("PORTFOLIO " + portfolio.getOwner() + " STATUS: " + portfolio.getPortfolioStatus() + " :: " + portfolio.getIsPortfolioSubmitted());
                    }

                }
            }
        }
        return returnList;
    }

    public Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo) {
        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
        if(scyElo != null) {
            String xml = scyElo.getContent().getXmlString();
            if(xml != null && xml.length() > 0) {
                return (Portfolio) getXmlTransferObjectService().getObject(xml);
            }
        }

        return null;
    }

    @Override
    public NewestElos getNewestElosForFeedback(MissionRuntimeElo missionRuntimeElo, String username) {

        NewestElos newestElos = new NewestElos();

        List feedbackList = getFeedback();
        for (int i = 0; i < feedbackList.size(); i++) {
            ScyElo feedbackElo = (ScyElo) feedbackList.get(i);
            URI uri = feedbackElo.getFeedbackOnEloUri();
            ScyElo commentedOn = ScyElo.loadLastVersionElo(uri, this);

            TransferElo transferElo = new TransferElo(commentedOn);
            transferElo.setFeedbackELO(feedbackElo);
            newestElos.addElo(transferElo);

        }

        return newestElos;
    }

    @Override
    public List getFeedback() {

        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent feedbackComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, "scy/feedback");
        IQuery feedbackQuery = new Query(feedbackComponent);

        List<ISearchResult> results = getRepository().search(feedbackQuery);

        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = (ISearchResult) results.get(i);
            ScyElo scyELO = getElo(searchResult.getUri());

            String xmlString = scyELO.getElo().getContent().getXmlString();

            //log.info("***************** FEEDBACK: " + xmlString);

            if(xmlString.startsWith("<feedback>")) xmlString = fixXml(xmlString, scyELO);
        }

        return getELOs(feedbackQuery);
    }

    @Override
    public List getMyElosWithFeedback(MissionRuntimeElo missionRuntimeElo, String currentUserName) {
        List feedback = getFeedback();
        List returnList = new LinkedList();

        for (int i = 0; i < feedback.size(); i++) {
            ScyElo scyElo = (ScyElo) feedback.get(i);
            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(scyElo.getContent().getXml());
            if(feedbackEloTransfer.getCreatedBy() != null && feedbackEloTransfer.getCreatedBy().equals(currentUserName)) returnList.add(feedbackEloTransfer);
        }

        return returnList;

    }

    @Override
    public List getFeedbackElosWhereIHaveContributed(MissionRuntimeElo missionRuntimeElo, String currentUserName) {
        List feedbackElos = getFeedback();
        List returnList = new LinkedList();
        for (int i = 0; i < feedbackElos.size(); i++) {
            ScyElo scyElo = (ScyElo) feedbackElos.get(i);
            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(scyElo.getContent().getXml());
            if(feedbackEloTransfer != null) {
                List givenFeedback = feedbackEloTransfer.getFeedbacks();
                for (int j = 0; j < givenFeedback.size(); j++) {
                    FeedbackTransfer feedbackTransfer = (FeedbackTransfer) givenFeedback.get(j);
                    if(feedbackTransfer.getCreatedBy().equals(currentUserName)) {
                        if(!returnList.contains(feedbackEloTransfer)) returnList.add(feedbackEloTransfer);
                    } else {
                        List replies = feedbackTransfer.getReplies();
                        for (int k = 0; k < replies.size(); k++) {
                            FeedbackReplyTransfer replyTransfer = (FeedbackReplyTransfer) replies.get(k);
                            if(replyTransfer.getCreatedBy().equals(currentUserName)) {
                                if(!returnList.contains(feedbackEloTransfer)) returnList.add(feedbackEloTransfer);
                            }
                        }
                    }
                }

            }
        }

        return returnList;
    }

    @Override
    public void clearAllPortfolios() {
        List runtimeElos = getRuntimeElos(null);
        for (int i = 0; i < runtimeElos.size(); i++) {
            ScyElo shittyElo = (ScyElo) runtimeElos.get(i);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(shittyElo.getUri(), this);
            URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
            if (portfolioURI != null) {
                ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
                scyElo.getContent().setXmlString(null);
                scyElo.updateElo();
            }
        }
    }

    @Override
    public MissionSpecificationElo getMissionSpecificationElo(String missionURI) {
        try {
            URI uri = new URI(missionURI);
            return MissionSpecificationElo.loadLastVersionElo(uri, this);
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("Problems (yeah, real problems) loading mission specification with uri: " + missionURI);
    }

    private String fixXml(String xmlString, ScyElo scyElo) {

        String feedback = "<feedback>";
        String feedbackEnd = "</feedback>";
        xmlString = xmlString.substring(feedback.length(), xmlString.length());
        xmlString = xmlString.substring(0, xmlString.length() - feedbackEnd.length());

        xmlString = "<feedbackelo>" + xmlString + "</feedbackelo>";

        scyElo.getElo().getContent().setXmlString(xmlString);
        scyElo.updateElo();
        log.info("FIXED ELO!");

        return xmlString;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }
}
