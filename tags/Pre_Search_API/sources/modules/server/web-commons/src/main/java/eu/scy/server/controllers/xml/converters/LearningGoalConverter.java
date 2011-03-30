package eu.scy.server.controllers.xml.converters;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import eu.scy.server.controllers.xml.transfer.LearningGoal;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2011
 * Time: 11:26:13
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoalConverter extends AbstractSingleValueConverter  {
    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(LearningGoal.class);
    }

    @Override
    public Object fromString(String s) {
        // System.out.println("CONVERTING LEARNING GOAL FROM STRING : " +s );
        LearningGoal learningGoal = new LearningGoal();
        learningGoal.setGoal(s);
        return learningGoal;
    }

/*
    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        LearningGoal leaeLearningGoal = (LearningGoal) value;
        writer.startNode("goal");
        writer.setValue(leaeLearningGoal.getGoal());
        writer.endNode();
        
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        LearningGoal learningGoal = new LearningGoal();
        reader.moveDown();
        learningGoal.setGoal(reader.getValue());
        reader.moveDown();
        return learningGoal;
    }
    */
}