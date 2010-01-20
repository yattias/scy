package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Tool;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:29:14
 * To change this template use File | Settings | File Templates.
 */
public interface LASDAO extends SCYBaseDAO{

    public void addToolToLAS(Tool tool, LearningActivitySpace las);


}
