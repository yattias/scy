package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mar.2010
 * Time: 12:29:31
 * To change this template use File | Settings | File Templates.
 */
public interface AssignedPedagogicalPlan extends BaseObject {
    PedagogicalPlan getPedagogicalPlan();

    void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan);

    User getUser();

    void setUser(User user);

    Boolean getUseCriteriaBasedAssessment();

    void setUseCriteriaBasedAssessment(Boolean useCriteriaBasedAssessment);
}
