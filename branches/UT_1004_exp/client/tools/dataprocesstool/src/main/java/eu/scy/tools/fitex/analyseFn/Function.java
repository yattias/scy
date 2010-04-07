/*
 * Function.java
 * Created on 24 mai 2007, 22:54
 */

package eu.scy.tools.fitex.analyseFn;

import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.tools.fitex.analyseFn.Analyseur.ErreurDeSyntaxe;
import eu.scy.tools.fitex.dataStruct.*;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cedric
 *
 * Cette classe diecrit une fonction, ie savoir :
 * son intitulie, 
 * son expression, 
 * son tableau des paramietres inclus dans l'expression,
 * et sa distance par rapport ie un jeu de donnees
 */
public class Function
{
    // 
    private FitexToolPanel owner;
    //L'intitulie de la fonction
    private String intitule ;
    // l'expression de la fonction
    private Expression expression=null ;
    // liste des paramietres : intitulie et objet parametre
    private HashMap<String,Parametre> mapDesParametres  = new HashMap<String,Parametre>();
    // liste tampon des paramietres pour pouvoir garder les anciennes valeurs de parametres
    private HashMap<String,Parametre> newMapDesParametres  = new HashMap<String,Parametre>() ;
    // la table par rapport ie laquelle il faut calculer la distance
    private DefaultTableModel[] data ;
    // la distance de la fonction avec les donniees
    private Double[] reliabilityFactor ;
    
    /** Creates a new instance of Function */
    public Function(FitexToolPanel owner, String intitule, DefaultTableModel[] data) {
        this.data = data ;
        this.owner = owner;
        this.reliabilityFactor = new Double[data.length];
        maJFonction(intitule) ;
    }
    
    public void maJFonction(String intitule) {
        this.intitule=intitule ;
        
        // Selon l'intitule, traiter des cas speciaux a la main
        // crieer une expression "a la main"
        // et mettre a jour newMapDesParametres 
        // if (intitule== )
            // expression = new a1b(this) ; // expression qui decrit le dosage d'un monoacide par une base forte'
        
        //else
        // analyse de l'intitule par l'analyseur syntaxique'
        try {
            /* on passe l'object fonction en parametre pour que l'analyseur ait
             * connaissance de la fonction : ainsi les parametres pourront s'enregistrer
             * dans la HashMap de la fonction prevue a cet effet */
            expression = new Analyseur(owner, intitule, this).analyser() ;
        } catch (ErreurDeSyntaxe e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("La fonction n'a pas pu etre analysee - erreur autre que ErreurDeSyntaxe.");
        }
        
        // riecupieration des anciennes valeurs des parametre dans la newMapDesParametres
        for (String key:newMapDesParametres.keySet()) {
            if (mapDesParametres.get(key) != null)
                newMapDesParametres.get(key).setValeur(mapDesParametres.get(key).valeur()) ;
        }
        mapDesParametres.clear();
        mapDesParametres.putAll(newMapDesParametres) ;
        newMapDesParametres.clear() ;
        majRF() ;
    }
    
    /** calcule la distance de la courbe par rapport ie un tableau de valeurs passie en parametre */
    public void majRF() {
 
        int nbPts = 0 ;
        double sommeYCarre = 0 ;
        Double x;
        Double y;
        Boolean ignore;
        Boolean fonctionNonDefinie = false ;

        // parcours et mesure de la distance ie tous les points diefinis dans le tableau de donniees
        // la tableModel du tableau qui contient les donnees :
        for(int d=0; d<data.length; d++){
            DefaultTableModel tableModel = data[d];

            reliabilityFactor[d] = 0.0 ;
            if (expression != null) {
                for (int i=0; i<tableModel.getRowCount(); i++) {
                    // riecupieration des valeurs de la ligne
                    x=(Double)tableModel.getValueAt(i,0);
                    y=(Double)tableModel.getValueAt(i,1);
                    ignore=(Boolean)tableModel.getValueAt(i,2);
                    if (ignore==null) ignore=false;

                    if((x!=null) && (y!=null) && (!ignore)) {
                        nbPts++;
                        // ajout de la distance au carrie
                        reliabilityFactor[d] = reliabilityFactor[d] + Math.pow((y-expression.valeur(x)),2) ;
                        sommeYCarre = sommeYCarre + Math.pow((y),2) ;
                    }
                }
            }
            // si l'expression n'est pas definie
            // si il n'y a pas de points diefinis dans la table,
            // si la fonction n'est pas definie sur certains points,
            // il ne faut pas afficher la distance
            if (expression == null || nbPts==0 || Double.isNaN(reliabilityFactor[d])) reliabilityFactor[d]=null ;
            // sinon on calcule la MOYENNE des carres
            else reliabilityFactor[d] = reliabilityFactor[d] / sommeYCarre ;
        }
    }
    
    public String getIntitule(){
        return intitule ;
    }
    
    public void setData(DefaultTableModel[] data ){
        this.data = data ;
    }
    
    public void setExpression(Expression expression){
        this.expression=expression ;
    }
    
    public Expression getExpression(){
        return expression ;
    }
   
    public HashMap<String,Parametre> getMapParametre(){
        return mapDesParametres ;
    }
     
    /** fontion appeliee lors de la creation d'un nouveau parametre par l'analyseur
     * afin de mettre a jour la map des parametres de la fonction 
     */
    public void ajouterParametre (String nomParam, Parametre param){
           newMapDesParametres.put(nomParam , param) ;
           param.setValeur(0.0) ;
    }
    
    public void setValeurParametre (String nomParam, double valeur) {
        mapDesParametres.get(nomParam).setValeur(valeur) ;
    }
    
    public Double[] getRF(){
        return reliabilityFactor ;
    }  
}
