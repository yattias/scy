package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2009
 * Time: 06:20:00
 * To change this template use File | Settings | File Templates.
 */
public interface ScenarioService extends BaseService{

    public List getScenarios();

    public void createScenario(Scenario scenario);

    public List<Mission> getMissions();

    void save(Scenario scenario);

    Scenario getScenario(String scenarioId);

    List<LearningActivitySpace> getLearningActivitySpaces(Scenario scenario);
}
