package eu.scy.core.model;

import javax.persistence.*;

import java.util.List;

/**
 * Created by Intermedia
 * User: Jeremy / Thomas
 * Date: 11.aug.2008
 * Time: 04:00:01
 * An attempt at an ELO
 */

@Entity
@Table (name = "elo")
public class Elo extends SCYBaseObject {

    private String eloName;
    private String eloContent;
    
	
    @Column(name = "eloName", nullable = false, unique = false)
    public String getEloName() {
        return eloName;
    }

    public void setEloName(String eloName) {
        this.eloName = eloName;
    }

    
    @Column(name = "eloContent", nullable = false, unique = false)
    public String getEloContent() {
        return "oh lala!";
        //return eloContent;
    }

    public void setEloContent(String eloContent) {
        this.eloContent = eloContent;
    }

}
