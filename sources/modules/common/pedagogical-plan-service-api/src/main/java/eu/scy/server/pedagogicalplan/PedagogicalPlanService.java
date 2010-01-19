package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.pedagogicalplan.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2009
 * Time: 05:47:26
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanService {

    public SessionInfo login(String username, String password);

    public List <Tool> getTools();

    public List<Scenario> getScenarios();

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates();

    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template);

    public List <Scenario> getCompatibleScenarios(Mission mission);

    public List <Mission> getCompatibleMissions(Scenario scenario);
}
