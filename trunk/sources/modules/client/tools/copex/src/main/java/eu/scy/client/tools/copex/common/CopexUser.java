/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author marjolaine
 * user in the labbook, it can be a learner or a teacher
 */
public abstract class CopexUser implements Cloneable{
    /** db id */
    protected long dbKey;
    /**name */
    protected String userName;
    /**firstname */
    protected String userFirstName;

     public CopexUser() {
    }
    /**
     * CopexUser
     * @param userName 
     * @param userFirstName 
     */
    public CopexUser(String userName, String userFirstName) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.dbKey = -1;
    }

    public CopexUser(long dbKey, String userName, String userFirstName) {
        this.dbKey = dbKey;
        this.userName = userName;
        this.userFirstName = userFirstName;
    }


    /**
     * CopexUser
     * @param userName 
     */
    public CopexUser(String userName) {
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

    /** gets the user firstname*/
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * set the user firstname
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
            CopexUser user = (CopexUser) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.userName);
            String firstNameC = null;
            if(userFirstName != null)
                firstNameC = new String(this.userFirstName);
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
