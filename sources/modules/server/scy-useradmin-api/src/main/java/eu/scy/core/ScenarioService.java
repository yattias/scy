package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2009
 * Time: 06:20:00
 * To change this template use File | Settings | File Templates.
 */
public interface ScenarioService {

    public List getScenarios();

    public void createScenario(Scenario scenario);

}
