package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Assessment;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:41:18
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentService extends BaseService{

    public Assessment findAssessmentByName(String name);

}
