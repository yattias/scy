package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.AssessmentStrategy;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 28.sep.2009
 * Time: 16:08:57
 */

@Entity
@Table(name="assessmentstrategy")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "strategytype")
public abstract class AssessmentStrategyImpl extends BaseObjectImpl implements AssessmentStrategy  {
}
