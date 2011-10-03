package eu.scy.core.runtime;

import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.LasActivityInfo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.core.roolo.BaseELOServiceImpl;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.*;

import java.net.URI;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 15:13:38
 * To change this template use File | Settings | File Templates.
 */
public class SessionServiceImpl extends BaseELOServiceImpl implements SessionService {

    private UserService userService;

    private TupleSpace tupleSpace;
    private final static String LANGUAGE = "language";
    private final static String TOOL = "tool";
    private final static String MISSION = "mission";
    private final static String LAS = "las";
    private final static String SEND_NOTIFICATION = "send_notification";

    private XMLTransferObjectService xmlTransferObjectService;

    private static final Logger logger = Logger.getLogger(SessionServiceImpl.class);


    @Override
    public List getActiveStudentsOnMission(MissionSpecificationElo missionSpecificationElo) {
        return getCurrentStudentActivity(missionSpecificationElo);
    }


    @Override
    public List getCurrentStudentActivity(MissionSpecificationElo missionSpecificationElo) {
        List<UserActivityInfo> userActivityInfoList = new LinkedList<UserActivityInfo>();

        try {
            Tuple missionTemplate = new Tuple(MISSION, String.class, String.valueOf(missionSpecificationElo.getUri()), String.class, String.class, String.class);

            Tuple[] missionTuples = getTupleSpace().readAll(missionTemplate);
            String missionString = "";
            missionString += missionTuples.length + "..";
            for (int j = 0; j < missionTuples.length; j++) {
                UserActivityInfo userActivityInfo = new UserActivityInfo();
                Tuple missionTuple = missionTuples[j];
                Field[] missionFields = missionTuple.getFields();
                for (int k = 0; k < missionFields.length; k++) {
                    Field missionField = missionFields[k];
                    if (k == 1) userActivityInfo.setUserName((String) missionField.getValue());
                    if (k == 2) userActivityInfo.setMissionSpecification((String) missionField.getValue());
                    if (k == 3) userActivityInfo.setMissionName((String) missionField.getValue());
                }

                addTool(userActivityInfo);
                addLas(userActivityInfo, getPedagogicalPlanForMission(missionSpecificationElo));

                List elos = findElosFor(userActivityInfo.getParsedUserName());
                String size = String.valueOf(elos.size());

                addPortfolio(userActivityInfo, missionSpecificationElo);
                userActivityInfo.setNumberOfElosProduced(size);

                userActivityInfoList.add(userActivityInfo);


            }

        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }


        return userActivityInfoList;
    }

    private void addPortfolio(UserActivityInfo userActivityInfo, MissionSpecificationElo missionSpecificationElo) {
        MissionRuntimeElo runtime = getMissionRuntime(userActivityInfo.getParsedUserName(), missionSpecificationElo);
        Portfolio portfolio = getPortfolio(runtime);
        userActivityInfo.setPortfolio(portfolio);
    }

    private Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo) {
        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, this);
        if (scyElo != null) {
            String xml = scyElo.getContent().getXmlString();
            if (xml != null && xml.length() > 0) {
                return (Portfolio) getXmlTransferObjectService().getObject(xml);
            }
        }

        return null;
    }


    private MissionRuntimeElo getMissionRuntime(String userNname, MissionSpecificationElo missionSpecificationElo) {
        List<ISearchResult> results = getRuntimeElosForUser(userNname);
        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = results.get(i);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(searchResult.getUri(), this);
            if (missionRuntimeElo.getMissionSpecificationElo().equals(missionSpecificationElo.getUri())) {
                logger.info("FOUND RUNTIME ELO " + missionRuntimeElo.getTitle());
                return missionRuntimeElo;

            }
        }
        return null;

    }

    private List<ISearchResult> getRuntimeElosForUser(String userName) {

        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        IQuery missionRuntimeQuery = new Query(missionRuntimeQueryComponent);
        missionRuntimeQuery.setMaxResults(500);
        Set users = new HashSet();
        users.add(userName);
        missionRuntimeQuery.setIncludedUsers(users);
        return getRepository().search(missionRuntimeQuery);

        /*

        List runtimeElos = new LinkedList();
        List<ISearchResult> runtimeModels = getRuntimeElos(null);
        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            if (missionRuntimeElo != null) {
                String missionRunningHAHAHA = missionRuntimeElo.getUserRunningMission();
                if (missionRunningHAHAHA != null && missionRunningHAHAHA.equals(userName)) {
                    runtimeElos.add(missionRuntimeElo);
                }
            }
        }
        return runtimeElos;
        */
    }


    public UserActivityInfo getUserActivityInfo(MissionSpecificationElo missionSpecificationElo, String userName) {

        try {
            Tuple missionTemplate = new Tuple(MISSION, userName, String.valueOf(missionSpecificationElo.getUri()), String.class);

            Tuple[] missionTuples = getTupleSpace().readAll(missionTemplate);
            String missionString = "";
            missionString += missionTuples.length + "..";
            for (int j = 0; j < missionTuples.length; j++) {
                UserActivityInfo userActivityInfo = new UserActivityInfo();
                Tuple missionTuple = missionTuples[j];
                Field[] missionFields = missionTuple.getFields();
                for (int k = 0; k < missionFields.length; k++) {
                    Field missionField = missionFields[k];
                    if (k == 1) userActivityInfo.setUserName((String) missionField.getValue());
                    if (k == 2) userActivityInfo.setMissionSpecification((String) missionField.getValue());
                    if (k == 3) userActivityInfo.setMissionName((String) missionField.getValue());
                }

                addTool(userActivityInfo);
                addLas(userActivityInfo, getPedagogicalPlanForMission(missionSpecificationElo));

                return userActivityInfo;

            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

        return null;

    }


    public PedagogicalPlanTransfer getPedagogicalPlanForMission(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer transfer = null;
        URI uri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, this);
        if (scyElo != null) {
            String content = scyElo.getContent().getXmlString();
            if (content != null && content.length() > 0) {
                transfer = (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(content);
            }


        }

        return transfer;
    }


    private void sendMessage(String missionURI) {
        Tuple messageTuple = new Tuple(SEND_NOTIFICATION, "stefan@scy.collide.info/Smack", "digital@scy.collide.info/Smack", missionURI, "Norway rocks!");
        try {
            getTupleSpace().write(messageTuple);
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void addTool(UserActivityInfo userActivityInfo) {
        try {
            Tuple toolTemplate = new Tuple(TOOL, userActivityInfo.getUserName(), String.class, String.class);
            Tuple[] toolTuples = getTupleSpace().readAll(toolTemplate);
            for (int j = 0; j < toolTuples.length; j++) {
                Tuple toolTuple = toolTuples[j];
                Field[] toolFields = toolTuple.getFields();
                for (int k = 0; k < toolFields.length; k++) {
                    Field toolField = toolFields[k];
                    if (k == 2) userActivityInfo.setToolName((String) toolField.getValue());
                }
            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void addLas(UserActivityInfo userActivityInfo, PedagogicalPlanTransfer pedagogicalPlanForMission) {
        try {
            Tuple lasTemplate = new Tuple(LAS, userActivityInfo.getUserName(), String.class, String.class);

            Tuple[] lasTuples = getTupleSpace().readAll(lasTemplate);
            String lasString = "";
            lasString += lasTuples.length + "..";
            for (int j = 0; j < lasTuples.length; j++) {
                Tuple lasTuple = lasTuples[j];
                Field[] lasFields = lasTuple.getFields();
                String lasId = "";
                for (int k = 0; k < lasFields.length; k++) {
                    Field lasField = lasFields[k];
                    if (k == 3) lasId = (String) lasField.getValue();
                }
                userActivityInfo.setLasName(pedagogicalPlanForMission.obtainLasName(lasId));
            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public List<LasActivityInfo> getActiveLasses(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer pedagogicalPlan = getPedagogicalPlanForMission(missionSpecificationElo);
        Tuple lasTemplate = new Tuple(LAS, String.class, missionSpecificationElo.getUri().toString(), String.class);
        List<LasActivityInfo> returnList = new LinkedList<LasActivityInfo>();
        try {
            Tuple[] lasTuples = getTupleSpace().readAll(lasTemplate);
            for (int i = 0; i < lasTuples.length; i++) {
                String lasName = null;


                Tuple lasTuple = lasTuples[i];
                Field[] lasFields = lasTuple.getFields();
                for (int k = 0; k < lasFields.length; k++) {
                    Field lasField = lasFields[k];
                    if (k == 3) lasName = ((String) lasField.getValue());
                }

                LasActivityInfo lasActivityInfo = null;

                lasActivityInfo = getLasActivityInfo(lasName, returnList, missionSpecificationElo);
                if (lasActivityInfo == null) {
                    lasActivityInfo = new LasActivityInfo();
                    lasActivityInfo.setLasName(lasName);
                    lasActivityInfo.setHumanReadableName(pedagogicalPlan.obtainLasName(lasName));
                    addActiveUsers(lasActivityInfo, missionSpecificationElo);
                    returnList.add(lasActivityInfo);
                }


            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

        return returnList;
    }

    private LasActivityInfo getLasActivityInfo(String lasName, List<LasActivityInfo> returnList, MissionSpecificationElo missionSpecificationElo) {
        for (int j = 0; j < returnList.size(); j++) {
            LasActivityInfo alreadyAddedLasActivityInfo = returnList.get(j);
            if (alreadyAddedLasActivityInfo.getLasName().equals(lasName)) {
                addActiveUsers(alreadyAddedLasActivityInfo, missionSpecificationElo);
                return alreadyAddedLasActivityInfo;
            }

        }

        return null;
    }

    private void addActiveUsers(LasActivityInfo lasActivityInfo, MissionSpecificationElo missionSpecificationElo) {
        Tuple lasTemplate = new Tuple(LAS, String.class, missionSpecificationElo.getUri().toString(), lasActivityInfo.getLasName());
        try {
            Tuple[] lasTuples = getTupleSpace().readAll(lasTemplate);
            for (int i = 0; i < lasTuples.length; i++) {
                Tuple lasTuple = lasTuples[i];
                Field[] lasFields = lasTuple.getFields();
                for (int k = 0; k < lasFields.length; k++) {
                    Field lasField = lasFields[k];
                    if (k == 1) {
                        String userName = (String) lasField.getValue();
                        lasActivityInfo.addActiveUser(getUserService().getUser(getParsedUserName(userName)));
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String getParsedUserName(String smackUserName) {
        String userName = smackUserName;
        String returnValue = userName.substring(0, userName.indexOf("@"));
        return returnValue;
    }


    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }

    public void setTupleSpace(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
