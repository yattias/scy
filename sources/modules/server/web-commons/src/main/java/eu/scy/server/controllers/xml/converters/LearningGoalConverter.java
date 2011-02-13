package eu.scy.server.controllers.xml.converters;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import eu.scy.core.model.transfer.LearningGoal;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2011
 * Time: 11:26:13
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoalConverter extends AbstractSingleValueConverter  {

    private static Logger log = Logger.getLogger("LearningGoalConverter.class");

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(LearningGoal.class);
    }

    @Override
    public Object fromString(String s) {

        log.info("Converting string to learning goal: " + s);

        LearningGoal learningGoal = new LearningGoal();
        learningGoal.setGoal(s);
        return learningGoal;
    }

}
