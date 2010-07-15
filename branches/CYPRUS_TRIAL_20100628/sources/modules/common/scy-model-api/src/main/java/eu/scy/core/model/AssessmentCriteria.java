package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jul.2010
 * Time: 05:49:36
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentCriteria extends BaseObject {

    public Assessment getAssessment();

    public void setAssessment(Assessment assessment);

    String getCriteria();

    void setCriteria(String criteria);
}
