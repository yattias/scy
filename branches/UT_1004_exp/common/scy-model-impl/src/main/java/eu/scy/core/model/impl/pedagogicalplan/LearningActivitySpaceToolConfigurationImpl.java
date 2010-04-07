package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.nov.2009
 * Time: 06:22:36
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "toolconfiguration")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "toolid")
@DiscriminatorValue("general")
public class LearningActivitySpaceToolConfigurationImpl extends BaseObjectImpl implements LearningActivitySpaceToolConfiguration {

    private LearningActivitySpace learningActivitySpace;

    private Activity activity;

    private eu.scy.core.model.pedagogicalplan.Tool tool;

    @Override
    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace) {
        this.learningActivitySpace = learningActivitySpace;
    }

    @ManyToOne(targetEntity = LearningActivitySpaceImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "las_primKey")
    public LearningActivitySpace getLearningActivitySpace() {
        return learningActivitySpace;
    }


    public void setTool(eu.scy.core.model.pedagogicalplan.Tool tool) {
        this.tool = tool;
    }

    @ManyToOne(targetEntity = ToolImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tool_primKey")
    public eu.scy.core.model.pedagogicalplan.Tool getTool() {
        return tool;
    }

    @ManyToOne(targetEntity = ActivityImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_primKey")
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
