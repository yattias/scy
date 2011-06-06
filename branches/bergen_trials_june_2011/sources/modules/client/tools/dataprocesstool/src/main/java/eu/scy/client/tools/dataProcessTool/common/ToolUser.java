/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

/**
 * tool user in labbook
 * @author Marjolaine Bodin
 */
public class ToolUser implements Cloneable{
    /* db key identifier*/
    protected long dbKey;
    /*name*/
    protected String userName;
    /*firstname*/
    protected String userFirstName;

    public ToolUser() {
    }

    /**
     *  ToolUser : 
     * @param userName 
     * @param userFirstName 
     */
    public ToolUser(String userName, String userFirstName) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.dbKey = -1;
    }

    public ToolUser(long dbKey, String userName, String userFirstName) {
        this.dbKey = dbKey;
        this.userName = userName;
        this.userFirstName = userFirstName;
    }

   
    /**
     *  ToolUser: 
     * @param userName 
     */
    public ToolUser(String userName) {
        this.userName = userName;
        this.userFirstName = null;
        this.dbKey = -1;
    }
    
   
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    
   

   

    /*
     * gets the fristName of the user
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /*
     * set the firstName
     * @param  userFirstName 
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /*
     * gets the user name
     */
    public String getUserName() {
        return userName;
    }

    /*
     * set the user name
     * @param  userName 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    

    @Override
    public Object clone()  {
        try {
            ToolUser user = (ToolUser) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.userName);
            String firstNameC = new String(this.userFirstName);
            user.setDbKey(dbKeyC);
            user.setUserName(nameC);
            user.setUserFirstName(firstNameC);
            return user;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
