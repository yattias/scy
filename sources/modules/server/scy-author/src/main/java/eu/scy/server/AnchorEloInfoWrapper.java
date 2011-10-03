package eu.scy.server;

import eu.scy.common.scyelo.ScyElo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.okt.2011
 * Time: 19:26:01
 * To change this template use File | Settings | File Templates.
 */
public class AnchorEloInfoWrapper {

    private ScyElo anchorElo;
    private String name;
    private Boolean obligatoryInPortfolio;
    private String lasName;
    private String anchorEloInstructions;


    public ScyElo getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(ScyElo anchorElo) {
        this.anchorElo = anchorElo;
    }

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

    public String getEncodedUri() {
        try {
            return URLEncoder.encode(anchorElo.getUri().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getLasName() {
        return lasName;
    }

    public void setLasName(String lasName) {
        this.lasName = lasName;
    }

    public String getAnchorEloInstructions() {
        return anchorEloInstructions;
    }

    public void setAnchorEloInstructions(String anchorEloInstructions) {
        this.anchorEloInstructions = anchorEloInstructions;
    }
}
