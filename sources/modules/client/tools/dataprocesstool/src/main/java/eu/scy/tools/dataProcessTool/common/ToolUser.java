/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * utilisateur des outils
 * @author Marjolaine Bodin
 */
public class ToolUser implements Cloneable{
    // PROPERTY
    /* identifiant bd */
    protected long dbKey;
    
    /**
     * nom
     */
    protected String userName;
    /**
     * prenom
     */
    protected String userFirstName;

   /* isteacher */
    private boolean teacher;
    /* islearner */
    private boolean learner;
    
     // Constructeurs
    /**
     * constructeur CopexUser 
     */
     public ToolUser() {
    }
    /**
     * constructeur ToolUser : 
     * @param userName : nom de l'utilisateur
     * @param userFirstName : prenom de l'utilisateur
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

    public ToolUser(long dbKey,  String userName, String userFirstName, boolean teacher, boolean learner) {
        this.dbKey = dbKey;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.teacher = teacher;
        this.learner = learner;
    }

    // constructeur sans le prenom qui n'est pas obligatoire
    /**
     * constructeur ToolUser : 
     * @param userName : nom de l'utilisateur
     */
    public ToolUser(String userName) {
        this.userName = userName;
        this.userFirstName = null;
        this.dbKey = -1;
    }
    
    // Getter and Setter
   
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    
   

   

    /*
     * retourne le prenom de l'utilisateur
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /*
     * met a jour le prenom utilisateur
     * @param  userFirstName : prenom utilisateur
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /*
     * retourne le nom de l'utilisateur
     */
    public String getUserName() {
        return userName;
    }

    /*
     * met a jour le nom utilisateur
     * @param  userName : nom utilisateur
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLearner() {
        return learner;
    }

    public void setLearner(boolean learner) {
        this.learner = learner;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
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
            user.setLearner(this.isLearner());
            user.setTeacher(this.isTeacher());
            return user;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
