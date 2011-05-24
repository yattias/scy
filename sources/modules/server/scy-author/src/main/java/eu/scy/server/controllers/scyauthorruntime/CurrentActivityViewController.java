package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.runtime.SessionService;
import eu.scy.server.controllers.BaseController;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mai.2011
 * Time: 13:00:33
 * To change this template use File | Settings | File Templates.
 */
public class CurrentActivityViewController extends BaseController {

    private MissionELOService missionELOService;

    private TupleSpace tupleSpace;
    private TupleSpace commandSpace;
    private SessionService sessionService;

    private final static String LANGUAGE = "language";
    private final static String TOOL = "tool";
    private final static String MISSION = "mission";
    private final static String LAS = "las";
    private final static String SEND_NOTIFICATION = "send_notification";


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        MissionSpecificationElo missionRuntimeElo = null;
        try {
            URI uri = new URI(request.getParameter("eloURI"));
            missionRuntimeElo = MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        List<UserActivityInfo> userActivityInfo = getCurrentStudentActivity(missionRuntimeElo, request);
        modelAndView.addObject("userActivityList", userActivityInfo);

        //sendMessage(String.valueOf(missionRuntimeElo.getUri()));

    }


    public List getCurrentStudentActivity(MissionSpecificationElo missionSpecificationElo, HttpServletRequest request) {
        List<UserActivityInfo> userActivityInfoList = new LinkedList<UserActivityInfo>();

        try {
            Tuple missionTemplate = new Tuple(MISSION ,String.class, String.valueOf(missionSpecificationElo.getUri()), String.class);

            Tuple[] missionTuples = getTupleSpace().readAll(missionTemplate);
            String missionString = "";
            missionString += missionTuples.length + "..";
            for (int j = 0; j < missionTuples.length; j++) {
                UserActivityInfo userActivityInfo = new UserActivityInfo();
                userActivityInfo.setNumberOfElosProduced("" + getSessionService().findElosFor(missionSpecificationElo.getUri(), getCurrentUserName(request)).size());
                Tuple missionTuple = missionTuples[j];
                Field[] missionFields = missionTuple.getFields();
                for (int k = 0; k < missionFields.length; k++) {
                    Field missionField = missionFields[k];
                    if(k == 1) userActivityInfo.setUserName((String) missionField.getValue());
                    if(k == 2) userActivityInfo.setMissionSpecification((String) missionField.getValue());
                    if(k == 3) userActivityInfo.setMissionName((String) missionField.getValue());
                }

                addTool(userActivityInfo);
                addLas(userActivityInfo);

                userActivityInfoList.add(userActivityInfo);

            }

        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }


        return userActivityInfoList;
    }

    private void sendMessage(String missionURI) {
        Tuple messageTuple = new Tuple(SEND_NOTIFICATION, "stefan@scy.collide.info/Smack", "digital@scy.collide.info/Smack", missionURI, "Norway rocks!");
        try {
            getCommandSpace().write(messageTuple);
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void addTool(UserActivityInfo userActivityInfo) {
        try {
            Tuple toolTemplate = new Tuple(TOOL, userActivityInfo.getUserName(), String.class, String.class);
            Tuple [] toolTuples = getTupleSpace().readAll(toolTemplate);
            for (int j = 0; j < toolTuples.length; j++) {
                Tuple toolTuple = toolTuples[j];
                Field[] toolFields = toolTuple.getFields();
                for (int k = 0; k < toolFields.length; k++) {
                    Field toolField = toolFields[k];
                    if(k == 2) userActivityInfo.setToolName((String) toolField.getValue());
                }
            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void addLas(UserActivityInfo userActivityInfo) {
        try {
            Tuple lasTemplate = new Tuple(LAS,userActivityInfo.getUserName(), String.class, String.class);

            Tuple [] lasTuples = getTupleSpace().readAll(lasTemplate);
            String lasString = "";
            lasString +=lasTuples.length + "..";
            for (int j = 0; j < lasTuples.length; j++) {
                Tuple lasTuple = lasTuples[j];
                Field[] lasFields =lasTuple.getFields();
                for (int k = 0; k < lasFields.length; k++) {
                    Field lasField = lasFields[k];
                    if(k == 3) userActivityInfo.setLasName((String) lasField.getValue());
                }
            }
        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }

    public void setTupleSpace(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public TupleSpace getCommandSpace() {
        return commandSpace;
    }

    public void setCommandSpace(TupleSpace commandSpace) {
        this.commandSpace = commandSpace;
    }
}
