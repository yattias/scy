/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

/**
 * labbook mission
 * @author Marjolaine Bodin
 */
public class Mission implements Cloneable {
    /* db key  */
    private long dbKey;
    /*code */
    private String code;
    /* name */
    private String name;
    /* description */
    private String description;

    public Mission(long dbKey, String code, String name, String description) {
        this.dbKey = dbKey;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
     @Override
    public Object clone()  {
        try {
            Mission mission = (Mission) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            String codeC = "";
            if (this.code != null)
                codeC = new String(this.code);
            String descC = "";
            if (this.description != null)
                descC = new String(description);
            mission.setDbKey(dbKeyC);
            mission.setName(nameC);
            mission.setCode(codeC);
            mission.setDescription(descC);
            return mission;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
