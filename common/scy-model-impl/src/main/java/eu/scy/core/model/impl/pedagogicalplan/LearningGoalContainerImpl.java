package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.LearningGoalContainer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:08:34
 */
@MappedSuperclass
public abstract class LearningGoalContainerImpl extends BaseObjectImpl implements LearningGoalContainer {

    //public abstract List<LearningGoal> getLearningGoals() ;

   
}
