package eu.scy.core.runtime;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.core.roolo.BaseELOServiceImpl;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 15:13:38
 * To change this template use File | Settings | File Templates.
 */
public class SessionServiceImpl extends BaseELOServiceImpl implements SessionService {

    private TupleSpace tupleSpace;
    private final static String LANGUAGE = "language";
    private final static String TOOL = "tool";
    private final static String MISSION = "mission";
    private final static String LAS = "las";
    private final static String SEND_NOTIFICATION = "send_notification";

    private static final Logger logger = Logger.getLogger(SessionServiceImpl.class);


    @Override
    public List getActiveStudentsOnMission(MissionSpecificationElo missionSpecificationElo) {
        return getCurrentStudentActivity(missionSpecificationElo);
    }



    public List getCurrentStudentActivity(MissionSpecificationElo missionSpecificationElo) {
        List<UserActivityInfo> userActivityInfoList = new LinkedList<UserActivityInfo>();

        try {
            Tuple missionTemplate = new Tuple(MISSION ,String.class, String.valueOf(missionSpecificationElo.getUri()), String.class);

            Tuple[] missionTuples = getTupleSpace().readAll(missionTemplate);
            String missionString = "";
            missionString += missionTuples.length + "..";
            for (int j = 0; j < missionTuples.length; j++) {
                UserActivityInfo userActivityInfo = new UserActivityInfo();
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
            getTupleSpace().write(messageTuple);
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
}
