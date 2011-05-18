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
                for (int j = 0; j < toolTuples.length; j++) {
                    Tuple toolTuple = toolTuples[j];
                    String toolString = "";
                    Field[] toolFields = toolTuple.getFields();
                    for (int k = 0; k < toolFields.length; k++) {
                        Field toolField = toolFields[k];
                        toolString+=toolField.getValue() + ", ";
                    }
                }

                logger.info("---> " + toString());




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
