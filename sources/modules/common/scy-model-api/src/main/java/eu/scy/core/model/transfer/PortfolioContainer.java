package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 20:07:49
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioContainer {

    private List portfolios;

    public List getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List portfolios) {
        this.portfolios = portfolios;
    }

    public void addPortfolio(Portfolio portfolio) {
        if(getPortfolios() == null) portfolios = new LinkedList();
        getPortfolios().add(portfolio);
    }
}
