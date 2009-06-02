/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * represente le materiel
 * le materiel peut être 
 * - lie à une mission : materiel disponible
 * - lie à un protocole : materiel utilise
 * - lie à une action
 * les materiels sont regroupes par type de materiel
 * @author MBO
 */
public class Material implements Cloneable {
    // ATTRIBUTS
    /* cle BD */
    private long dbKey;
    /* nom du materiel */
    private String name;
    /* description */
    private String description;
    /* type de materiel */
    private ArrayList<TypeMaterial> listType;
    /* parametres */
    private ArrayList<Parameter> listParameters;
    /* conseille par l'enseignant */
    private boolean advisedLearner;

    

    // CONSTRUCTEURS
    public Material(long dbKey, String name, String description) {
        this.dbKey = dbKey;
        this.name = name;
        this.description = description;
        this.listType = new ArrayList();
        this.listParameters = new ArrayList();
    }
    
    public Material(String name, String description, ArrayList<TypeMaterial> listType, ArrayList<Parameter> listParameters, boolean advisedLearner) {
        this.name = name;
        this.description = description;
        this.listType = listType;
        this.listParameters = listParameters;
        this.advisedLearner = advisedLearner;
    }

    // GETTER AND SETTER
    public boolean isAdvisedLearner() {
        return advisedLearner;
    }

    public void setAdvisedLearner(boolean advisedLearner) {
        this.advisedLearner = advisedLearner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Parameter> getListParameters() {
        return listParameters;
    }

    public void setListParameters(ArrayList<Parameter> listParameters) {
        this.listParameters = listParameters;
    }

    public ArrayList<TypeMaterial> getListType() {
        return listType;
    }

    public void setListType(ArrayList<TypeMaterial> listType) {
        this.listType = listType;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    
    // METHODES
    /* rend la chaine à afficher sur l'ihm */
    public String toDisplay(){
        String s = getName()+" (";
        s += getTypeToDisplay();
        s += ")";
        return s;
        
    }

    /* rend la chaine à afficher sur l'ihm : liste des types */
    public String getTypeToDisplay(){
        String s = "";
        for (int i=0; i<listType.size(); i++){
            if (i>0)
                s += " - ";
            s += listType.get(i).toString();
        }
        return s;
        
    }
    /* ajout d'un type */
    public void addType(TypeMaterial type){
        this.listType.add(type);
    }
    
    
    /* ajout d'un parametre */
    public void addParam(Parameter p){
        this.listParameters.add(p);
    }
    
    /* retourn vrai si le materiel est de ce type*/
    public boolean isType(TypeMaterial t){
        int n = this.listType.size();
        for (int i=0; i<n; i++){
            if (t.getDbKey() == this.listType.get(i).getDbKey())
                return true;
        }
        return false;
    }
    
    
    
    @Override
    public Object clone()  {
        try {
            Material material = (Material) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            String descriptionC = null;
            if (this.description != null)
                descriptionC = new String(this.description);
            boolean advisedLearnerC = this.advisedLearner;
            ArrayList<TypeMaterial>  listT = null;
            if (this.listType != null){
                listT = new ArrayList();
                int nbT = this.listType.size();
                for (int i=0; i<nbT; i++){
                    listT.add((TypeMaterial)this.listType.get(i).clone());
                }
            }
            ArrayList<Parameter>  listP = null;
            if (this.listParameters != null){
                listP = new ArrayList();
                int nbP =  this.listParameters.size();
                for (int i=0; i<nbP; i++){
                    listP.add((Parameter)this.listParameters.get(i).clone());
                }
            }
            material.setDbKey(dbKeyC);
            material.setName(nameC);
            material.setDescription(descriptionC);
            material.setAdvisedLearner(advisedLearnerC);
            material.setListType(listT);
            material.setListParameters(listP);
            
            return material;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    /* renvoit true si le material a ce type */
    public boolean isType(long dbKeyType){
        int nbType = this.listType.size();
        for (int i=0; i<nbType; i++){
            if (this.listType.get(i).getDbKey() == dbKeyType){
                return true;
            }
        }
        return false;
    }
    
}
