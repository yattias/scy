/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 * teacher
 * @author Marjolaine
 */
public class CopexTeacher extends CopexUser implements Cloneable{

    public CopexTeacher(String userName) {
        super(userName);
    }

    public CopexTeacher(long dbKey, String userName, String userFirstName) {
        super(dbKey, userName, userFirstName);
    }

    public CopexTeacher(String userName, String userFirstName) {
        super(userName, userFirstName);
    }

}
