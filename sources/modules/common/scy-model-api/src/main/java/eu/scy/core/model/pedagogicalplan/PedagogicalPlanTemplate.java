package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:10:19
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanTemplate extends BaseObject{

    public void setMission(Mission mission);

    public Mission getMission();

    public void setScenario(Scenario scenario);

    public Scenario getScenario();

    public void setPortfolioTemplate(PortfolioTemplate portfolioTemplate);

    public PortfolioTemplate getPortfolioTemplate();

}
