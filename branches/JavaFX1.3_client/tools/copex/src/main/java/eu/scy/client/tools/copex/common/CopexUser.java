/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author MBO
 * represente un utilisateur COPEX : c'est soit un eleve (CopexLearner) soit un enseignant (CopexTeacher)
 * un utilisateur est represente par : 
 * - son login (identifiant unique)
 * - son mot de passe
 * - son nom
 * - son prenom
 */
public class CopexUser implements Cloneable{
    // Attributs
    /* identifiant bd */
    protected long dbKey;
    /**
     * login
     */
    protected String copexLogin;
    /**
     * mot de passe
     */
    protected String copexPassWord;
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
     public CopexUser() {
    }
    /**
     * constructeur CopexUser : 
     * @param copexLogin : login de l'utilisateur
     * @param copexPassWord : mot de passe de l'utilisateur
     * @param userName : nom de l'utilisateur
     * @param userFirstName : prenom de l'utilisateur
     */
    public CopexUser(String copexLogin, String copexPassWord, String userName, String userFirstName) {
        this.copexLogin = copexLogin;
        this.copexPassWord = copexPassWord;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.dbKey = -1;
    }

    public CopexUser(long dbKey, String copexLogin, String copexPassWord, String userName, String userFirstName) {
        this.dbKey = dbKey;
        this.copexLogin = copexLogin;
        this.copexPassWord = copexPassWord;
        this.userName = userName;
        this.userFirstName = userFirstName;
    }

    public CopexUser(long dbKey, String copexLogin, String copexPassWord, String userName, String userFirstName, boolean teacher, boolean learner) {
        this.dbKey = dbKey;
        this.copexLogin = copexLogin;
        this.copexPassWord = copexPassWord;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.teacher = teacher;
        this.learner = learner;
    }

    // constructeur sans le prenom qui n'est pas obligatoire
    /**
     * constructeur CopexUser : 
     * @param copexLogin : login de l'utilisateur
     * @param copexPassWord : mot de passe de l'utilisateur
     * @param userName : nom de l'utilisateur
     */
    public CopexUser(String copexLogin, String copexPassWord, String userName) {
        this.copexLogin = copexLogin;
        this.copexPassWord = copexPassWord;
        this.userName = userName;
        this.userFirstName = null;
        this.dbKey = -1;
    }
    
    // Getter and Setter
    /*
     * retourne le login de l'utilisateur
     */
    public String getCopexLogin() {
        return copexLogin;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    
    /*
     * met a jour le login utilisateur 
     * @param  copexLogin : login utilisateur
     */
    public void setCopexLogin(String copexLogin) {
        this.copexLogin = copexLogin;
    }

     /*
     * retourne le mot de passe de l'utilisateur
     */
    public String getCopexPassWord() {
        return copexPassWord;
    }

    /*
     * met a jour le mot de passe utilisateur 
     * @param  copexPassWord : mot de passe utilisateur
     */
    public void setCopexPassWord(String copexPassWord) {
        this.copexPassWord = copexPassWord;
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
            CopexUser user = (CopexUser) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.userName);
            String firstNameC = new String(this.userFirstName);
            String loginC = new String(this.copexLogin);
            String passWordC = new String(this.copexPassWord);
            user.setDbKey(dbKeyC);
            user.setUserName(nameC);
            user.setUserFirstName(firstNameC);
            user.setCopexLogin(loginC);
            user.setCopexPassWord(passWordC);
            user.setLearner(this.isLearner());
            user.setTeacher(this.isTeacher());
            return user;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    
    
}
