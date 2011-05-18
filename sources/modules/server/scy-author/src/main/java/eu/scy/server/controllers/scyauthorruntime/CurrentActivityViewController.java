package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.server.controllers.BaseController;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private TupleSpace tupleSpace;

    private final static String LANGUAGE = "language";
    private final static String TOOL = "tool";
    private final static String MISSION = "mission";
    private final static String LAS = "las";


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        List <UserActivityInfo> userActivityInfo = getCurrentStudentActivity();

        modelAndView.addObject("userActivityList", userActivityInfo);







    }


    public List getCurrentStudentActivity() {
        logger.info("TUPLE SPACE CONNECTED: " + getTupleSpace().isConnected() + " " + System.currentTimeMillis());
        List <UserActivityInfo> userActivityInfoList = new LinkedList<UserActivityInfo>();

        try {
            Tuple languageTemplate = new Tuple(LANGUAGE ,String.class, String.class);
            Tuple toolTemplate = new Tuple(TOOL ,String.class, String.class, String.class);
            Tuple missionTemplate = new Tuple(MISSION ,String.class, String.class, String.class);
            Tuple lasTemplate = new Tuple(LAS,String.class, String.class, String.class);
            Tuple [] tuples = getTupleSpace().readAll(languageTemplate);
            for (int i = 0; i < tuples.length; i++) {
                UserActivityInfo userActivityInfo = new UserActivityInfo();
                Tuple tuple = tuples[i];
                Field[] fields = tuple.getFields();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    if(j == 1) userActivityInfo.setUserName((String) field.getValue());
                    if(j == 2) userActivityInfo.setLanguage((String) field.getValue());
                }


                Tuple [] toolTuples = getTupleSpace().readAll(toolTemplate);
                String toolString = "";
                toolString +=toolTuples.length + "..";
                for (int j = 0; j < toolTuples.length; j++) {
                    Tuple toolTuple = toolTuples[j];
                    Field[] toolFields = toolTuple.getFields();
                    for (int k = 0; k < toolFields.length; k++) {
                        Field toolField = toolFields[k];
                        toolString+=toolField.getValue() + ", ";
                    }
                }

                logger.info("T---> " + toolString);

                Tuple [] missionTuples = getTupleSpace().readAll(missionTemplate);
                String missionString = "";
                missionString +=missionTuples.length + "..";
                for (int j = 0; j < missionTuples.length; j++) {
                    Tuple missionTuple = missionTuples[j];
                    Field[] missionFields =missionTuple.getFields();
                    for (int k = 0; k < missionFields.length; k++) {
                        Field missionField = missionFields[k];
                        missionString+=missionField.getValue() + ", ";
                    }
                }

                logger.info("M---> " + missionString);

                Tuple [] lasTuples = getTupleSpace().readAll(lasTemplate);
                String lasString = "";
                lasString +=lasTuples.length + "..";
                for (int j = 0; j < lasTuples.length; j++) {
                    Tuple lasTuple = lasTuples[j];
                    Field[] lasFields =lasTuple.getFields();
                    for (int k = 0; k < lasFields.length; k++) {
                        Field lasField = lasFields[k];
                        lasString+=lasField.getValue() + ", ";
                    }
                }

                logger.info("LAS---> " + lasString);




                userActivityInfoList.add(userActivityInfo);
            }

        } catch (TupleSpaceException e) {
            logger.error(e.getMessage(), e);
        }


        return userActivityInfoList;
    }

    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }

    public void setTupleSpace(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
    }
}
