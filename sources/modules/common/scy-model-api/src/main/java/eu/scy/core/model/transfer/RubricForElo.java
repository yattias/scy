package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 22:04:26
 * To change this template use File | Settings | File Templates.
 */
public class RubricForElo extends Rubric{

    private String anchorElo;

    public String getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(String anchorElo) {
        this.anchorElo = anchorElo;
    }
}
