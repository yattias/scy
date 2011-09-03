package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2011
 * Time: 08:44:55
 * To change this template use File | Settings | File Templates.
 */
public class EloAnchorEloPair {

    private TransferElo elo;
    private TransferElo anchorElo;

    public TransferElo getElo() {
        return elo;
    }

    public void setElo(TransferElo elo) {
        this.elo = elo;
    }

    public TransferElo getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(TransferElo anchorElo) {
        this.anchorElo = anchorElo;
    }
}
