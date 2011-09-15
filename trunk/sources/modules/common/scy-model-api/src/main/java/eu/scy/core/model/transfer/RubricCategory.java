package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 21:51:29
 * To change this template use File | Settings | File Templates.
 */
public class RubricCategory extends BaseXMLTransfer {

    private String name;
    private List<RubricAssessmentCriteria> rubricAssessmentCriterias = new LinkedList<RubricAssessmentCriteria>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RubricAssessmentCriteria> getRubricAssessmentCriterias() {
        if(rubricAssessmentCriterias == null) rubricAssessmentCriterias = new LinkedList<RubricAssessmentCriteria>();
        return rubricAssessmentCriterias;
    }

    public void setRubricAssessmentCriterias(List<RubricAssessmentCriteria> rubricAssessmentCriterias) {
        this.rubricAssessmentCriterias = rubricAssessmentCriterias;
    }

    public void addRubricAssessmentCriteria (RubricAssessmentCriteria rubricAssessmentCriteria) {
        getRubricAssessmentCriterias().add(rubricAssessmentCriteria);
    }
}
