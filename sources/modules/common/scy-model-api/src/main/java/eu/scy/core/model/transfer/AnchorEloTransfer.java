package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 22:50:05
 * To change this template use File | Settings | File Templates.
 */
public class AnchorEloTransfer extends BaseXMLTransfer {

    private String name;
    private Boolean obligatoryInPortfolio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getObligatoryInPortfolio() {
        return obligatoryInPortfolio;
    }

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio) {
        this.obligatoryInPortfolio = obligatoryInPortfolio;
    }
    
}
