package eu.scy.server.webeport;

import eu.scy.core.model.transfer.TransferElo;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2011
 * Time: 09:08:51
 * To change this template use File | Settings | File Templates.
 */
public class AnchoELOWithStatus {

    private TransferElo anchorElo;
    private TransferElo addedElo;

    private boolean eloHasBeenAdded;

    public TransferElo getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(TransferElo anchorElo) {
        this.anchorElo = anchorElo;
    }

    public TransferElo getAddedElo() {
        return addedElo;
    }

    public void setAddedElo(TransferElo addedElo) {
        this.addedElo = addedElo;
    }

    public boolean isEloHasBeenAdded() {
        return eloHasBeenAdded;
    }

    public void setEloHasBeenAdded(boolean eloHasBeenAdded) {
        this.eloHasBeenAdded = eloHasBeenAdded;
    }
}
