/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.*;

/**
 *
 * @author MBO
 * représente un groupe : un groupe contient plusieurs élèves
 * un groupe est géré par un ou plusieurs profs
 */
public class CopexGroup {
    //ATTRIBUTS
    /*
     * nom du groupe, également un identifiant
     */
    private String groupName;
    
    /*
     * liste des élèves composant le groupe
     */
    private ArrayList<CopexLearner> listLearners;

    // CONSTRUCTEURS 
    public CopexGroup() {
    }

    public CopexGroup(String groupName, ArrayList<CopexLearner> listLearners) {
        this.groupName = groupName;
        this.listLearners = listLearners;
    }

   // getter and setter
    /*
     * retourne le nom du groupe
     */
    public String getGroupName() {
        return groupName;
    }

    /*
     * met le nom du groupe à jour
     */ 
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

     /*
     * retourne la liste des élèves du groupe
     */
    public ArrayList<CopexLearner> getListLearners() {
        return listLearners;
    }

    /*
     * met la liste des élèves à jour
     */
    public void setListLearners(ArrayList<CopexLearner> listLearners) {
        this.listLearners = listLearners;
    }
    
    // OPERATIONS
    /*
     * ajoute un élève à la liste en fin
     */
    public void addLearner(CopexLearner copexLearner){
        if (this.listLearners == null)
                this.listLearners = new ArrayList();
        else
            this.listLearners.add(copexLearner);
    }
    
    /*
     * retourne true si l'élève est dans la liste
     */
    public boolean isLearnerInGroup(CopexLearner copexLearner){
        for (Iterator iter=listLearners.iterator();iter.hasNext();){
            if (copexLearner == iter.next())
                return true;                    
        }
        return false;
    }
    
    
}
