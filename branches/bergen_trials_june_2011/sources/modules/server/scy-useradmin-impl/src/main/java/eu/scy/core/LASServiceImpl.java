package eu.scy.core;

import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.*;
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
        return (LASDAO) getScyBaseDAO();
    }

    public void setLasDAO(LASDAO lasDAO) {
        this.lasDAO = lasDAO;
    }



    @Override
    public void addToolToLAS(Tool tool, LearningActivitySpace las) {
        getLasDAO().addToolToLAS(tool, las);
    }

    @Override
    public void addToolToActivity(Tool tool, Activity activity) {
        getLasDAO().addToolToActivity(tool, activity);
    }

    @Override
    public List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace) {
        return getLasDAO().getToolConfigurations(learningActivitySpace);
    }

    @Override
    public List<AnchorELO> getAnchorELOsProducedByLAS(LearningActivitySpace learningActivitySpace) {
        return getLasDAO().getAnchorELOsProducedByLAS(learningActivitySpace);
    }

    @Override
    public List<LearningActivitySpace> getAllLearningActivitySpacesForScenario(Scenario scenario) {
        return getLasDAO().getAllLearningActivitySpacesForScenario(scenario);
    }

    @Override
    public LearningActivitySpace getLearningActivitySpace(String id) {
        return (LearningActivitySpace) getLasDAO().getObject(LearningActivitySpaceImpl.class, id);
    }

    @Override
    public LearningActivitySpace getLearningActivitySpaceByName(String lasName, Scenario participatesIn) {
        return getLasDAO().getLearningActivitySpaceByName(lasName, participatesIn);
    }
}
