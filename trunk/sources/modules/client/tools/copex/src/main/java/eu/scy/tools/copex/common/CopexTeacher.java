/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.*;

/**
 *
 * @author MBO
 * Classe representant un enseignant
 * un enseignant est un utilisateur COPEX
 */
public class CopexTeacher extends CopexUser {

    // ATTRIBUTS
    /*
     *  liste des groupes geres par l'enseignant
     */
    private ArrayList<CopexGroup> listGroup;
    
    // CONSTRUCTEURS
    public CopexTeacher(String copexLogin, String copexPassWord, String userName) {
        super(copexLogin, copexPassWord, userName);
        this.listGroup = new ArrayList();
    }

    public CopexTeacher(String copexLogin, String copexPassWord, String userName, String userFirstName) {
        super(copexLogin, copexPassWord, userName, userFirstName);
        this.listGroup = new ArrayList();
        
    }
    public CopexTeacher(String copexLogin, String copexPassWord, String userName, ArrayList<CopexGroup> group) {
        super(copexLogin, copexPassWord, userName);
        this.listGroup = group;
    }
     public CopexTeacher(String copexLogin, String copexPassWord, String userName, String userFirstName, ArrayList<CopexGroup> group) {
        super(copexLogin, copexPassWord, userName, userFirstName);
        this.listGroup = group;
        
    }

    
    // GETTER AND SETTER
    public ArrayList<CopexGroup> getListGroup() {
        return listGroup;
    }

    public void setListGroup(ArrayList<CopexGroup> listGroup) {
        this.listGroup = listGroup;
    }
    
    // OPERATIONS
    /*
     * ajoute un groupe
     */
    private void addGroup(CopexGroup group){
        this.listGroup.add(group);
    }
    /*
     * retourne true si le groupe est gere par l'enseignant
     */
    private boolean isTeacherGroup(CopexGroup group){
        for (Iterator iter=listGroup.iterator();iter.hasNext();){
            if (group == iter.next())
                return true;                    
        }
        return false;
    }

}
