package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:24:58
 *
 * The AnchorELO represents a planned ELO that should be the outcome of for instance a LAS, one Activity, etc, and that the teacher can decide should be included in the portfolio.
 */
public interface AnchorELO extends PlannedELO, Assessable, PositionedObject {



    /**
     * Returns the Activity that the AnchorELo should be produced by
     *
     * @return the Activity that produced the AnchorELO
     */
    public Activity getProducedBy();

    /**
     * Sets the LAS  that produced the AnchorELO
     *
     * @param producedBy the LAS that produces the AnchorELO
     */
    public void setProducedBy(Activity producedBy);

    public LearningActivitySpace getInputTo();
    public void setInputTo(LearningActivitySpace inputTo);

    public Boolean getIncludedInPortfolio();
    public void setIncludedInPortfolio(Boolean includedInPortfolio);

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio);
    public Boolean getObligatoryInPortfolio();

    String getMissionMapId();
    void setMissionMapId(String missionMapId);

    String getHumanReadableName();

    void setHumanReadableName(String humanReadableName);
}
