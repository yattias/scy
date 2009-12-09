package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanBase;
import eu.scy.core.model.pedagogicalplan.PortfolioTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 06:49:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="pedagogicalplan")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pedtype")
public abstract class PedagogicalPlanBaseImpl extends BaseObjectImpl implements PedagogicalPlanBase {

    private Mission mission;
    private Scenario scenario;
    private PortfolioTemplate portfolioTemplate;

    @Transient
    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @OneToOne  (targetEntity = ScenarioImpl.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="scenario_primKey")
    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Transient
    public PortfolioTemplate getPortfolioTemplate() {
        return portfolioTemplate;
    }

    public void setPortfolioTemplate(PortfolioTemplate portfolioTemplate) {
        this.portfolioTemplate = portfolioTemplate;
    }
}
