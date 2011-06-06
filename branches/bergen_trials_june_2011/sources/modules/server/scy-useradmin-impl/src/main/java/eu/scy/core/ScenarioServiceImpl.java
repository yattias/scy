package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2009
 * Time: 06:20:59
 */
public class ScenarioServiceImpl extends BaseServiceImpl implements ScenarioService{

    private static Logger log = Logger.getLogger("ScenarioServiceImpl.class");

    private ScenarioDAO scenarioDAO;

    public List getScenarios() {
        return getScenarioDAO().getScenarios();
    }

    @Transactional
    public void createScenario(Scenario scenario) {

        getScenarioDAO().createScenario(scenario);
    }

    @Override
    public List<Mission> getMissions() {
        return getScenarioDAO().getMissions();
    }

    @Override
    public void save(Scenario scenario) {
        getScenarioDAO().save(scenario);
    }

    @Override
    public Scenario getScenario(String scenarioId) {
        return getScenarioDAO().getScenario(scenarioId);
    }

    @Override
    public List<LearningActivitySpace> getLearningActivitySpaces(Scenario scenario) {
        return getScenarioDAO().getLearningActivitySpaces(scenario);
    }

    public ScenarioDAO getScenarioDAO() {
        return (ScenarioDAO) getScyBaseDAO();
    }

    public void setScenarioDAO(ScenarioDAO scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }
}
