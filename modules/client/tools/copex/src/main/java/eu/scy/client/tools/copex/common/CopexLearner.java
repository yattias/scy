/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author MBO
 * represente un eleve
 * un eleve est un utilisateur de copex
 */
public class CopexLearner extends CopexUser {

    public CopexLearner(String copexLogin, String copexPassWord, String userName, String userFirstName) {
        super(copexLogin, copexPassWord, userName, userFirstName);
    }

    public CopexLearner(String copexLogin, String copexPassWord, String userName) {
        super(copexLogin, copexPassWord, userName);
    }

    @Override
    public String toString() {
        String s = "Learner : \n";
        s += "login : "+this.copexLogin+"\n";
        s += "password : "+this.copexPassWord+"\n";
        s += "name : "+this.userName+"\n";
        s += "firstName : "+this.userFirstName==null?"":this.userFirstName;
        return s;
    }
    

}
