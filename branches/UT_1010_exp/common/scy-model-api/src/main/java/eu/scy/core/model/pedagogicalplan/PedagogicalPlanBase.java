package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 06:47:02
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanBase extends BaseObject {

    void setMission(Mission mission);
    Mission getMission();

    void setScenario(Scenario scenario);
    Scenario getScenario();

    void setPortfolioTemplate(PortfolioTemplate portfolioTemplate);
    PortfolioTemplate getPortfolioTemplate();
}
