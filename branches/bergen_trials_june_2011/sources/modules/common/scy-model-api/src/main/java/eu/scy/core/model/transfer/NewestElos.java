package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2011
 * Time: 14:24:18
 * To change this template use File | Settings | File Templates.
 */
public class NewestElos extends BaseXMLTransfer{

    // bla bla
    private List elos = new LinkedList();

    public List getElos() {
        return elos;
    }

    public void setElos(List elos) {
        this.elos = elos;
    }

    public void addElo(TransferElo elo) {
        getElos().add(elo);
    }
}
