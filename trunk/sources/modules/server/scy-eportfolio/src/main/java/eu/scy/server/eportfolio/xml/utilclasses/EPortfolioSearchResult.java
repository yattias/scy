package eu.scy.server.eportfolio.xml.utilclasses;

import eu.scy.core.model.transfer.TransferElo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 11:30:23
 * To change this template use File | Settings | File Templates.
 */
public class EPortfolioSearchResult {

    private List searchResult = new LinkedList();

    public void addSearchResult(TransferElo eloModel) {
        searchResult.add(eloModel);
    }

    public List getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List searchResult) {
        this.searchResult = searchResult;
    }
}
