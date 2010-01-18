package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
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
public class ScenarioServiceImpl implements ScenarioService{

    private static Logger log = Logger.getLogger("ScenarioServiceImpl.class");

    private ScenarioDAO scenarioDAO;

    public List getScenarios() {
        return scenarioDAO.getScenarios();
    }

    @Transactional
    public void createScenario(Scenario scenario) {
        scenarioDAO.createScenario(scenario);
    }

    public ScenarioDAO getScenarioDAO() {
        return scenarioDAO;
    }

    public void setScenarioDAO(ScenarioDAO scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }
}
