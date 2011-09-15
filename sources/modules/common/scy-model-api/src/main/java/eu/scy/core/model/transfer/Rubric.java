package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 21:51:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class Rubric extends BaseXMLTransfer {

    private List<RubricCategory> rubricCategories = new LinkedList<RubricCategory>();

    public List<RubricCategory> getRubricCategories() {
        return rubricCategories;
    }

    public void setRubricCategories(List<RubricCategory> rubricCategories) {
        this.rubricCategories = rubricCategories;
    }
    
    public void addRubricCategory(RubricCategory rubricCategory) {
        if(getRubricCategories() == null) rubricCategories = new LinkedList<RubricCategory>();
        getRubricCategories().add(rubricCategory);
    }

}
