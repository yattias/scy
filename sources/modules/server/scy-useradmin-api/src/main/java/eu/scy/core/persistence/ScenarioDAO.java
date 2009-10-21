package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:15:31
 * To change this template use File | Settings | File Templates.
 */
public interface ScenarioDAO extends SCYBaseDAO{

    public Object createScenario(Scenario scenario);

    public List<Scenario> getScenarios();

}
