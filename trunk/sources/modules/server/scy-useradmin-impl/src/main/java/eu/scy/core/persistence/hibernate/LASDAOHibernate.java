package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceToolConfigurationImpl;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.LASDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:31:44
 * To change this template use File | Settings | File Templates.
 */
public class LASDAOHibernate extends ScyBaseDAOHibernate implements LASDAO {

    @Override
    public void addToolToLAS(Tool tool, LearningActivitySpace las) {
        LearningActivitySpaceToolConfigurationImpl configuration = new LearningActivitySpaceToolConfigurationImpl();
        configuration.setTool(tool);
        configuration.setLearningActivitySpace(las);
        save(configuration);
    }
}
