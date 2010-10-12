/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author marjolaine
 * a learner: a learner is a user of labbook
 */
public class CopexLearner extends CopexUser {

    public CopexLearner(long dbKey, String userName, String userFirstName) {
        super(dbKey,userName, userFirstName);
    }
    public CopexLearner(String userName, String userFirstName) {
        super(userName, userFirstName);
    }

    public CopexLearner(String userName) {
        super(userName);
    }

    @Override
    public String toString() {
        String s = "Learner : \n";
        s += "name : "+this.userName+"\n";
        s += "firstName : "+this.userFirstName==null?"":this.userFirstName;
        return s;
    }
    

}
