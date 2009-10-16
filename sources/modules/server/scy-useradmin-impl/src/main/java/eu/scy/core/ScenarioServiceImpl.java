package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2009
 * Time: 06:20:59
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioServiceImpl implements ScenarioService{

    private ScenarioDAO scenarioDAO;



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
