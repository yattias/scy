package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:00:58
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlan extends PedagogicalPlanBase, BasedOnTemplate{


    /**
     * Sets the template this pedagogical plan is based on. The plan will inherit all default values from the template
     * @return
     */
    public PedagogicalPlanTemplate getTemplate();

    public void setTemplate(PedagogicalPlanTemplate template);


    Boolean getPublished();

    void setPublished(Boolean published);

    Boolean getMakeAllAssignedStudentsBuddies();

    void setMakeAllAssignedStudentsBuddies(Boolean makeAllAssignedStudentsBuddies);

    public Integer getMaximumNumberOfAnchorELOsInPortfolio();

    public void setMaximumNumberOfAnchorELOsInPortfolio(Integer maximumNumberOfAnchorELOsInPortfolio);

    public Integer getMinimumNumberOfAnchorELOsInPortfolio();

    public void setMinimumNumberOfAnchorELOsInPortfolio(Integer minimumNumberOfAnchorELOsInPortfolio);
}
