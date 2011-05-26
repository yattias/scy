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
    private String comments;
    private String lasType;
    private String instructions;

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLasType() {
        return lasType;
    }

    public void setLasType(String lasType) {
        this.lasType = lasType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
