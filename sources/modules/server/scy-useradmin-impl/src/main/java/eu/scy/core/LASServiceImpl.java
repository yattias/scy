package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.LASDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:32:48
 * To change this template use File | Settings | File Templates.
 */
public class LASServiceImpl extends BaseServiceImpl implements LASService{

    private LASDAO lasDAO;

    public LASDAO getLasDAO() {
        return lasDAO;
    }

    public void setLasDAO(LASDAO lasDAO) {
        this.lasDAO = lasDAO;
    }



    @Override
    public void addToolToLAS(Tool tool, LearningActivitySpace las) {
        lasDAO.addToolToLAS(tool, las);
    }

    @Override
    public void addToolToActivity(Tool tool, Activity activity) {
        lasDAO.addToolToActivity(tool, activity);
    }

    @Override
    public List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace) {
        return lasDAO.getToolConfigurations(learningActivitySpace);
    }
}
