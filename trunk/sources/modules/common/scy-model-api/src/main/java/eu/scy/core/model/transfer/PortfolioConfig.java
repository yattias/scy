package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.jan.2011
 * Time: 11:11:42
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioConfig extends BaseXMLTransfer{

    private List portfolioReflectionTabs = new LinkedList();
    private EloReflection eloReflection;

    public List getPortfolioReflectionTabs() {
        return portfolioReflectionTabs;
    }

    public void setPortfolioReflectionTabs(List portfolioReflectionTabs) {
        this.portfolioReflectionTabs = portfolioReflectionTabs;
    }

    public void addPortfolioReflectionTab(Tab tab) {
        getPortfolioReflectionTabs().add(tab);
    }

    public EloReflection getEloReflection() {
        return eloReflection;
    }

    public void setEloReflection(EloReflection eloReflection) {
        this.eloReflection = eloReflection;
    }
}
