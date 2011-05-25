package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 20:34:29
 * To change this template use File | Settings | File Templates.
 */
public class LasTransfer extends BaseXMLTransfer{

    private String originalLasId;
    private String name;
    private AnchorEloTransfer anchorElo;
    private Integer minutesPlannedUsedInLas = 0;

    public String getOriginalLasId() {
        return originalLasId;
    }

    public void setOriginalLasId(String originalLasId) {
        this.originalLasId = originalLasId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnchorEloTransfer getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(AnchorEloTransfer anchorElo) {
        this.anchorElo = anchorElo;
    }

    public Integer getMinutesPlannedUsedInLas() {
        return minutesPlannedUsedInLas;
    }

    public void setMinutesPlannedUsedInLas(Integer minutesPlannedUsedInLas) {
        this.minutesPlannedUsedInLas = minutesPlannedUsedInLas;
    }
}
