package eu.scy.core.roolo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

import eu.scy.common.mission.impl.BasicMissionAnchor;
import eu.scy.core.model.transfer.*;
import eu.scy.core.roolo.filter.FeedbackEloSearchFilterImpl;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.AndQuery;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.SearchOperation;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsHelper;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;

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

    private SQLSpacesActionLogger sqlSpacesActionLogger;

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
        //setGlobalMissionScaffoldingLevel(missionSpecificationElo, 2);

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
        return getUsersFromRuntimeElos(missionSpecificationElo);
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
        if (missionRuntimeElo != null) {
            URI uri = missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri();
            return MissionSpecificationElo.loadLastVersionElo(uri, this);
        }

        return null;

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

        IAction action = new Action();
        action.setType("GlobalScaffoldingLevelAdjustment");
        action.setUser("tea");
        action.addContext(ContextConstants.tool, "Authoring");
        action.addAttribute("mission_uri", String.valueOf(missionSpecificationElo.getUri()));
        action.addAttribute("mission_title", missionSpecificationElo.getTitle());
        action.addAttribute("globalScaffoldingLevel", String.valueOf(scaffoldingLevel));
        action.addAttribute("assKicked", "true");


        getSqlSpacesActionLogger().log(action);


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
        MissionModelElo missionModel = MissionModelElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), this);
        missionModel.getMissionModel().loadMetadata(this);
        return missionModel.getTypedContent().getMissionAnchors();
    }


    @Override
    public List<TransferElo> getObligatoryAnchorELOs(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlan) {
        List anchorElos = getAnchorELOs(missionSpecificationElo);
        List returnList = new LinkedList();
        for (int i = 0; i < anchorElos.size(); i++) {
            BasicMissionAnchor basicMissionAnchor = (BasicMissionAnchor) anchorElos.get(i);
            ScyElo elo = ScyElo.loadElo(basicMissionAnchor.getEloUri(), this);
            if (elo.getObligatoryInPortfolio() != null && elo.getObligatoryInPortfolio() == true) {
                returnList.add(new TransferElo(elo));
            }
        }

        log.info("FETCHED " + returnList.size() + " OBLIGATORY ELOS IN MISSION!");

        return returnList;

    }

    private boolean getIsDefinedAsObligatoryInPedagogicalPlan(PedagogicalPlanTransfer pedagogicalPlan, ScyElo scyElo) {
        if (scyElo.getObligatoryInPortfolio() == null) {
            scyElo.setObligatoryInPortfolio(false);
            scyElo.updateElo();
        }
        return scyElo.getObligatoryInPortfolio();
    }

    private RuntimeSettingsElo getRuntimeSettingsElo(MissionSpecificationElo missionSpecificationElo) {
        return RuntimeSettingsElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
    }

    @Override
    public List<ISearchResult> getMissionSpecifications() {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionSpecificationQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType());
        IQuery missionSpecificationQuery = new Query(missionSpecificationQueryComponent);
        missionSpecificationQuery.setMaxResults(99999999);

        return getRepository().search(missionSpecificationQuery);
    }

    public List<ISearchResult> getMissionSpecificationsByAuthor(String author) {
        return getMissionSpecifications();
    }

    @Override
    public List<Portfolio> getPortfoliosThatAreReadyForAssessment(MissionSpecificationElo missionSpecificationElo) {
        List<ISearchResult> runtimeElos = getRuntimeElos(missionSpecificationElo);
        List<Portfolio> returnList = new LinkedList<Portfolio>();
        for (int i = 0; i < runtimeElos.size(); i++) {
            ISearchResult shittyElo = runtimeElos.get(i);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(shittyElo.getUri(), this);
            URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
            if (portfolioURI != null) {
                ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
                String xml = scyElo.getContent().getXmlString();
                if (xml != null) {
                    Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(xml);
                    portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
                    boolean portfolioSubmitted = portfolio.getIsPortfolioSubmitted();
                    boolean portfolioAssessed = portfolio.getIsPortfolioAssessed();
                    if (portfolioSubmitted && !portfolioAssessed) {
                        returnList.add(portfolio);
                    } else {
                        log.info("PORTFOLIO " + portfolio.getOwner() + " STATUS: " + portfolio.getPortfolioStatus() + " :: " + portfolio.getIsPortfolioSubmitted());
                    }

                }
            }
        }
        return returnList;
    }

    public Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo, String username) {
        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
        if (scyElo != null) {
            String xml = scyElo.getContent().getXmlString();
            if (xml != null && xml.length() > 0) {
                return (Portfolio) getXmlTransferObjectService().getObject(xml);
            }
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setMissionName(missionRuntimeElo.getTitle());
        portfolio.setOwner(username);
        portfolio.setPortfolioStatus(Portfolio.PORTFOLIO_STATUS_NOT_SUBMITTED);
        portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
        return portfolio;
    }

    @Override
    public FeedbackEloSearchFilter createFeedbackEloSearchFilter() {
        return new FeedbackEloSearchFilterImpl();
    }

    @Override
    public List<TransferElo> getElosForFeedback(MissionRuntimeElo missionRuntimeElo, String username, FeedbackEloSearchFilter feedbackEloSearchFilter) {
        List<TransferElo> returnList = new LinkedList<TransferElo>();
        List<ISearchResult> feedbackList = getFeedback();
        for (int i = 0; i < feedbackList.size(); i++) {
            ISearchResult searchResult = feedbackList.get(i);
            ScyElo feedbackElo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
            URI uri = feedbackElo.getFeedbackOnEloUri();
            ScyElo commentedOn = ScyElo.loadLastVersionElo(uri, this);
            if (commentedOn.getMissionSpecificationEloUri().equals(missionRuntimeElo.getMissionSpecificationElo())) {
                FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackElo.getContent().getXmlString());

                TransferElo transferElo = new TransferElo(commentedOn);
                transferElo.setFeedbackEloTransfer(feedbackEloTransfer);
                if (!transferElo.getCreatedBy().trim().equals(username)) {
                    transferElo.setFeedbackELO(feedbackElo);
                }
                returnList.add(transferElo);
            }


        }

        FeedbackEloSearchResultFilter filter = new FeedbackEloSearchResultFilter();
        filter.setFeedbackEloSearchFilter(feedbackEloSearchFilter);

        returnList = filter.filter(returnList);
        returnList = filter.sort(returnList);


        return returnList;
    }

    @Override
    public List<TransferElo> getElosForFeedback(MissionSpecificationElo missionSpecificationElo, FeedbackEloSearchFilter feedbackEloSearchFilter) {
        List<TransferElo> returnList = new LinkedList<TransferElo>();
        List<ISearchResult> feedbackList = getFeedback();
        for (int i = 0; i < feedbackList.size(); i++) {
            ISearchResult searchResult = feedbackList.get(i);
            ScyElo feedbackElo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
            URI uri = feedbackElo.getFeedbackOnEloUri();
            ScyElo commentedOn = ScyElo.loadLastVersionElo(uri, this);
            if (commentedOn.getMissionSpecificationEloUri().equals(missionSpecificationElo.getUri())) {
                FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackElo.getContent().getXmlString());

                TransferElo transferElo = new TransferElo(commentedOn);
                transferElo.setFeedbackEloTransfer(feedbackEloTransfer);
                /*if (!transferElo.getCreatedBy().trim().equals(username)) {
                    transferElo.setFeedbackELO(feedbackElo);
                } */
                returnList.add(transferElo);
            }


        }

        FeedbackEloSearchResultFilter filter = new FeedbackEloSearchResultFilter();
        filter.setFeedbackEloSearchFilter(feedbackEloSearchFilter);

        returnList = filter.filter(returnList);
        returnList = filter.sort(returnList);


        return returnList;
    }

    @Override
    public void deleteAllFeedbackFeedback() {
        List<ISearchResult> results = getFeedback();

        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = (ISearchResult) results.get(i);
            ScyElo scyELO = getElo(searchResult.getUri());
            scyELO.getContent().setXmlString("");
            scyELO.updateElo();
        }
    }

    @Override
    public List<ISearchResult> getFeedback() {

        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent feedbackComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, "scy/feedback");
        IQuery feedbackQuery = new Query(feedbackComponent);
        feedbackQuery.setMaxResults(9999999);

        List<ISearchResult> results = getRepository().search(feedbackQuery);
        return results;
    }

    @Override
    public NewestElos getMyElosWithFeedback(MissionRuntimeElo missionRuntimeElo, String currentUserName) {
        NewestElos newestElos = new NewestElos();

        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent feedbackComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, "scy/feedback");
        final IMetadataKey auhtorKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
        IQueryComponent authorComponent = new MetadataQueryComponent(auhtorKey, SearchOperation.EQUALS, currentUserName);
        AndQuery andQuery = new AndQuery(feedbackComponent, authorComponent);
        IQuery feedbackQuery = new Query(feedbackComponent);
        feedbackQuery.setMaxResults(99999);

        List<ISearchResult> results = getRepository().search(feedbackQuery);

        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = (ISearchResult) results.get(i);
            ScyElo scyELO = getElo(searchResult.getUri());
            String xmlString = scyELO.getElo().getContent().getXmlString();
            if (xmlString != null && xmlString.length() > 0) {
                if (xmlString.startsWith("<feedback>")) {
                    xmlString = fixXml(xmlString, scyELO);
                }
                FeedbackEloTransfer feedbackTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(xmlString);
                URI parent = scyELO.getFeedbackOnEloUri();
                ScyElo parentElo = ScyElo.loadLastVersionElo(parent, this);
                if(parentElo.getAuthors().contains(currentUserName)) {
                    newestElos.addElo(new TransferElo(parentElo));
                }

            }

        }

        return newestElos;
    }

    @Override
    public NewestElos getFeedbackElosWhereIHaveContributed(MissionRuntimeElo missionRuntimeElo, String currentUserName) {
        log.info("LOADING ELOS WHERE I (" + currentUserName + ") HAVE CONTRIBUTED!");
        List feedbackElos = getFeedback();
        NewestElos newestElos = new NewestElos();

        for (int i = 0; i < feedbackElos.size(); i++) {
            ISearchResult searchResult = (ISearchResult) feedbackElos.get(i);
            ScyElo feedbackElo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
            FeedbackEloTransfer feedbackTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackElo.getContent().getXmlString());

            if (getHasUserContributedWithFeedbackOnElo(feedbackTransfer, currentUserName)) {
                URI parent = feedbackElo.getFeedbackOnEloUri();
                ScyElo parentElo = ScyElo.loadLastVersionElo(parent, this);
                newestElos.addElo(new TransferElo(parentElo));
            }
        }
        return newestElos;


    }

    private boolean getHasUserContributedWithFeedbackOnElo(FeedbackEloTransfer feedbackEloTransfer, String currentUserName) {
        if (feedbackEloTransfer == null) return false;
        List<FeedbackTransfer> feedbackTransfers = feedbackEloTransfer.getFeedbacks();
        for (int i = 0; i < feedbackTransfers.size(); i++) {
            FeedbackTransfer feedbackTransfer = feedbackTransfers.get(i);
            List<FeedbackReplyTransfer> replies = feedbackTransfer.getReplies();
            if (replies != null) {
                for (int j = 0; j < replies.size(); j++) {
                    FeedbackReplyTransfer transfer = replies.get(j);
                    if (transfer.getCreatedBy().equals(currentUserName)) return true;
                }

            }
            if (feedbackTransfer.getCreatedBy().equals(currentUserName)) return true;
        }

        return false;
    }

    @Override
    public void clearAllPortfolios() {
        List<ISearchResult> runtimeElos = getRuntimeElos(null);
        for (int i = 0; i < runtimeElos.size(); i++) {
            ISearchResult shittyElo = runtimeElos.get(i);
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

    @Override
    public TransferElo getTransferElo(ScyElo scyElo) {

        List feedback = getFeedback();
        for (int i = 0; i < feedback.size(); i++) {
            ISearchResult searchResult = (ISearchResult) feedback.get(i);
            ScyElo feedbackElo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
            URI parentEloURI = feedbackElo.getFeedbackOnEloUri();

            ScyElo parentCandidate = ScyElo.loadLastVersionElo(parentEloURI, this);

            if (parentCandidate.getUri().equals(scyElo.getUri())) {
                TransferElo transferElo = new TransferElo(scyElo);
                transferElo.setFeedbackELO(feedbackElo);
                return transferElo;
            }
        }

        log.warning("DID NOT FIND FEEDBACK FOR ELO " + scyElo.getTitle());
        return null;

        /*String title = scyElo.getTitle();
        String createdBy = scyElo.getCreator();
        //TransferElo scyEloTransf = new TransferElo(scyElo);
        List feedbackElos = getFeedback();
        for (int i = 0; i < feedbackElos.size(); i++) {
            ScyElo feedbackElo = (ScyElo) feedbackElos.get(i);
            TransferElo feedbackEloTransf = new TransferElo(feedbackElo);
            //URI scyEloURI = scyElo.getUri();
            URI feedbackEloParentURI = feedbackElo.getFeedbackOnEloUri();
            //URI feedbackEloURI = feedbackElo.getUri();
            ScyElo parentElo = ScyElo.loadLastVersionElo(feedbackEloParentURI, this);
            String parentEloTitle = parentElo.getTitle();
            String parentEloCreator = parentElo.getCreator();
            //if (createdBy != null) {
            //    if (title.equals(parentEloTitle) && createdBy.equals(parentEloCreator)) {
                    TransferElo returnElo = new TransferElo(scyElo);
                    returnElo.setFeedbackELO(feedbackElo);
                    return returnElo;
            //    }
            //}


        }
        return null;
        */
    }

    @Override
    public List<ISearchResult> getElosWithTechnicalType(String technicalFormat, String username) {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent feedbackComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, technicalFormat);
        final IMetadataKey auhtorKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
        IQueryComponent authorComponent = new MetadataQueryComponent(auhtorKey, SearchOperation.EQUALS, username);
        AndQuery andQuery = new AndQuery(feedbackComponent, authorComponent);
        IQuery query = new Query(feedbackComponent);

        Set userNames = new HashSet();
        userNames.add(username);
        query.setMaxResults(99999999);

        query.setIncludedUsers(userNames);

        return getRepository().search(query);

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

    public SQLSpacesActionLogger getSqlSpacesActionLogger() {
        return sqlSpacesActionLogger;
    }

    public void setSqlSpacesActionLogger(SQLSpacesActionLogger sqlSpacesActionLogger) {
        this.sqlSpacesActionLogger = sqlSpacesActionLogger;
    }
}
