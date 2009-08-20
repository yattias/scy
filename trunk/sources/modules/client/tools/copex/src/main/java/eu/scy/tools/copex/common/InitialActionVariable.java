/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * Variable action nommee parametree
 * @author Marjolaine
 */
public class InitialActionVariable implements Cloneable{

    // PROPERTY
    /*identifiant */
    private long dbKey ;
    /*code */
    private String code;
    /* nombre de parametres */
    private int nbParam;
    /* libelle */
    private String libelle;
    /* tableau des parametres */
    private InitialActionParam[] tabParam;

    // CONSTRUCTOR
    public InitialActionVariable(long dbKey, String code, int nbParam, String libelle, InitialActionParam[] tabParam) {
        this.dbKey = dbKey;
        this.code = code;
        this.nbParam = nbParam;
        this.libelle = libelle;
        this.tabParam = tabParam;
        sortParam();
    }

    // GETTER AND SETTER
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNbParam() {
        return nbParam;
    }

    public void setNbParam(int nbParam) {
        this.nbParam = nbParam;
    }

    public InitialActionParam[] getTabParam() {
        return tabParam;
    }

    public void setTabParam(InitialActionParam[] tabParam) {
        this.tabParam = tabParam;
        sortParam();
    }

    @Override
    public Object clone()  {
        try {
            InitialActionVariable v = (InitialActionVariable) super.clone() ;
            v.setDbKey(this.getDbKey());
            v.setCode(new String (this.getCode()));
            v.setNbParam(this.getNbParam());
            v.setLibelle(new String(this.getLibelle()));
            InitialActionParam[] tabParamC = null;
            if (tabParam != null){
                tabParamC = new InitialActionParam[this.tabParam.length];
                for (int i=0; i<tabParam.length; i++){
                    tabParamC[i] = (InitialActionParam)this.tabParam[i].clone();
                }
            }
            v.setTabParam(tabParamC);
            return v;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    /* range les parametres dans l'ordre d'apparition */
    private void sortParam(){
        InitialActionParam[] tabParamC = new InitialActionParam[nbParam];
        String s = new String (libelle);

        String po="{";
        String pf="}";

        for (int i=0; i<nbParam; i++){
            int id = s.indexOf(po);
            if (id != -1){
                int idF = s.indexOf(pf);
                if (idF != -1){
                    long dbK = -1;
                    try{
                        dbK = Long.parseLong(s.substring(id+1, idF));
                    }catch(NumberFormatException e){

                    }
                    for (int j=0; j<nbParam; j++){
                        if (tabParam[j].getDbKey() == dbK){
                            tabParamC[i] = tabParam[j];
                            break;
                        }
                    }
                    if (idF < s.length()-1)
                        s = s.substring(idF+1);
                }
            }
        }
        this.tabParam = tabParamC ;
    }

   

    /* retourne le texte du libelle avant le parametre i (et superieur au parametre i-1)
     *ex : texte {123} et surtout {124} ok ? => 0 : texte
     *                              => 1 : et surtout
     * si id -1 : texte fin :       => -1 : ok ?
     */
    public String getTextLibelle(int i){
        String pf = "}";
        if (i == -1){
            // indice du dernier {}
            int id = libelle.lastIndexOf(pf);
            if (id == -1){
                // pas trouve => renvoir la chaine complete
                return libelle;
            }else{
                if (id == libelle.length() -1)
                    return "";
                else
                    return libelle.substring(id+1);
            }
        }else{
            long idP =tabParam[i].getDbKey() ;
            String t = "{"+idP+"}";
            int id = libelle.indexOf(t) ;
            if (i == 0){
                return libelle.substring(0,id);
            }else{
                long dbPrec = tabParam[i-1].getDbKey() ;
                String tprec = "{"+dbPrec+"}";
                int idPrec = libelle.indexOf(tprec);
                return libelle.substring(idPrec+tprec.length(), id);
            }
        }
    }


    
}
