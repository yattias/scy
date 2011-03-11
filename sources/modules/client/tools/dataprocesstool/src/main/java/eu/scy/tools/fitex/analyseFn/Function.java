/*
 * Function.java
 * Created on 24 mai 2007, 22:54
 */

package eu.scy.tools.fitex.analyseFn;

import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.fitex.analyseFn.Analyseur.ErreurDeSyntaxe;
import eu.scy.tools.fitex.dataStruct.*;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cedric
 *
 * Cette classe decrit une fonction, a savoir :
 * son intitule,
 * son expression, 
 * son tableau des parametres inclus dans l'expression,
 * et sa distance par rapport a un jeu de donnees
 */
public class Function
{
    public static final boolean DEBUG_ANALYSEUR = false;
    private static final boolean OLD_DIST=true;
    //
    private FitexToolPanel owner;
    //L'intitulie de la fonction
    private String intitule ;
    // l'expression de la fonction
    private Expression expression=null ;
    // type y = f(x) or x = f(y) cf DataConstants.FUNCTION_TYPE_
    private char type;
    private String idPredefFunction;
    // liste des paramietres : intitulie et objet parametre
    private HashMap<String,Parametre> mapDesParametres  = new HashMap<String,Parametre>();
    // liste tampon des paramietres pour pouvoir garder les anciennes valeurs de parametres
    private HashMap<String,Parametre> newMapDesParametres  = new HashMap<String,Parametre>() ;
    // la table par rapport ie laquelle il faut calculer la distance
    private DefaultTableModel[] data ;
    // la distance de la fonction avec les donniees
    private Double[] reliabilityFactor ;
    
    /** Creates a new instance of Function */
    public Function(FitexToolPanel owner, String intitule, char type, String idPredefFunction, DefaultTableModel[] data) {
        this.data = data ;
        this.type = type;
        this.idPredefFunction = idPredefFunction;
        this.owner = owner;
        if(data != null)
            this.reliabilityFactor = new Double[data.length];
        maJFonction(intitule, type, idPredefFunction) ;
    }
    
    public void maJFonction(String intitule, char type, String idPredefFunction) {
        this.intitule=intitule ;
        this.type = type;
        this.idPredefFunction = idPredefFunction;
        
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
            expression = new Analyseur(owner, computeIntitule(intitule), this).analyser() ;
        } catch (ErreurDeSyntaxe e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("La fonction n'a pas pu etre analysee - erreur autre que ErreurDeSyntaxe.");
        }
        
        // recuperation des anciennes valeurs des parametre dans la newMapDesParametres
        for (String key:newMapDesParametres.keySet()) {
            if (mapDesParametres.get(key) != null)
                newMapDesParametres.get(key).setValeur(mapDesParametres.get(key).valeur()) ;
        }
        mapDesParametres.clear();
        mapDesParametres.putAll(newMapDesParametres) ;
        newMapDesParametres.clear() ;
        majRF() ;
    }

    private String computeIntitule(String s){
        for(int i=0; i<s.length(); i++){
            char current = s.charAt(i);
            if(current == '-' && i > 0 && i<s.length()-1){
                char c = s.charAt(i-1);
                if(c!='+' && c!='-' && c!='*' && c!='/' && c!='E' && c!='^'){
                    s = s.substring(0, i)+"+-"+s.substring(i+1);
                    i++;
                }
            }
        }
        //System.out.println("apres compute : "+s);
        return s;
    }

    /** calcule la distance de la courbe par rapport ie un tableau de valeurs passie en parametre */
    public void majRF() {
        
//        try{
//            double myd = Double.parseDouble("ttt");
//        }catch(NumberFormatException e){
//            System.out.println("***********majRF*****************");
//            e.printStackTrace();
//        }
        int nbPts = 0 ;
        
        Double x;
        Double y;
        Boolean ignore;
        if(data == null)
            return;
        // parcours et mesure de la distance ie tous les points diefinis dans le tableau de donniees
        // la tableModel du tableau qui contient les donnees :
        for(int d=0; d<data.length; d++){
            DefaultTableModel tableModel = data[d];

            reliabilityFactor[d] = 0.0 ;
            double oldR = 0.0;
            double r = 0.0;
            double pasX = 0.0;
            double deltaX = 0.0;
            double pasY = 0.0;
            double deltaY = 0.0;
            //double sommeYCarre = 0 ;
            if (expression != null) {
                int nb = tableModel.getRowCount();
                double xmin = Double.NaN;
                double xmax = Double.NaN;
                double ymin = Double.NaN;
                double ymax = Double.NaN;
                int id = 0;
                for(int i=0; i<nb; i++){
                    x=(Double)tableModel.getValueAt(i,0);
                    y=(Double)tableModel.getValueAt(i,1);
                    ignore=(Boolean)tableModel.getValueAt(i,2);
                    if (ignore==null) ignore=false;
                    if((x!=null) && (y!=null) && (!ignore)) {
                        xmin = x;
                        xmax = x;
                        ymin = y;
                        ymax = y;
                        id = i;
                        break;
                    }
                }
                for(int i=id; i<nb; i++){
                    x=(Double)tableModel.getValueAt(i,0);
                    y=(Double)tableModel.getValueAt(i,1);
                    ignore=(Boolean)tableModel.getValueAt(i,2);
                    if (ignore==null) ignore=false;
                    if((x!=null) && (y!=null) && (!ignore)) {
                        xmin = Math.min(x, xmin);
                        xmax = Math.max(x, xmax);
                        ymin = Math.min(y, ymin);
                        ymax = Math.max(y, ymax);
                    }
                }
                deltaX = Math.abs((xmax-xmin));
                pasX = deltaX / 1000;
                deltaY = Math.abs((ymax-ymin));
                pasY = deltaY / 1000;
                if(!OLD_DIST && type == DataConstants.FUNCTION_TYPE_Y_FCT_X && pasX == 0)
                    break;
                if(!OLD_DIST && type == DataConstants.FUNCTION_TYPE_X_FCT_Y && pasY == 0)
                    break;
                for (int i=0; i<nb; i++) {
                    // recuperation des valeurs de la ligne
                    x=(Double)tableModel.getValueAt(i,0);
                    y=(Double)tableModel.getValueAt(i,1);
                    ignore=(Boolean)tableModel.getValueAt(i,2);
                    if (ignore==null) ignore=false;
                    if((x!=null) && (y!=null) && (!ignore)) {
                        nbPts++;
                        // ajout de la distance au carre
                        if(type == DataConstants.FUNCTION_TYPE_Y_FCT_X){ // y = f(x)
                            oldR = oldR + Math.pow((y-expression.valeur(x)),2) ;
                            double di=Math.abs((y-expression.valeur(x)));
                            double minDi =di;
                            double xc = x+pasX;
                            double maxx = x+(10*deltaX);
                            if(!OLD_DIST){
                                while(xc < maxx ){
                                    di = Math.sqrt(Math.pow((x-xc), 2)+Math.pow((y-expression.valeur(xc)), 2));
                                    minDi = Math.min(di,minDi);
                                    if(di< (xc-x))
                                        break;
                                    xc += pasX;
                                }
                            }
                            xc = x-pasX;
                            maxx =x-(10*deltaX);
                            if(!OLD_DIST){
                                while(xc > maxx){
                                    di = Math.sqrt(Math.pow((x-xc), 2)+Math.pow((y-expression.valeur(xc)), 2));
                                    minDi = Math.min(di,minDi);
                                    if(di < x-xc)
                                        break;
                                    xc -= pasX;
                                }
                            }
                            r += Math.pow(minDi,2);
                            //sommeYCarre = sommeYCarre + Math.pow((y),2) ;
                        }else{
                            //x=f(y)
                            oldR = oldR + Math.pow((x-expression.valeur(y)),2) ;
                            //sommeYCarre = sommeYCarre + Math.pow((x),2) ;
                            double di=Math.abs((x-expression.valeur(y)));
                            double minDi =di;
                            double yc = y+pasY;
                            if(!OLD_DIST){
                                while(yc <(y+(10*deltaY) )){
                                    di = Math.sqrt(Math.pow((y-yc), 2)+Math.pow((x-expression.valeur(yc)), 2));
                                    minDi = Math.min(di,minDi);
                                    if(di< (yc-y))
                                        break;
                                    yc += pasY;
                                }
                            }
                            yc = y-pasY;
                            if(!OLD_DIST){
                                while(yc > (y-(10*deltaY))){
                                    di = Math.sqrt(Math.pow((y-yc), 2)+Math.pow((x-expression.valeur(yc)), 2));
                                    minDi = Math.min(di,minDi);
                                    if(di < y-yc)
                                        break;
                                    yc -= pasY;
                                }
                            }
                            r += Math.pow(minDi,2);
                        }
                    }
                }
            }
            // si l'expression n'est pas definie
            // si il n'y a pas de points definis dans la table,
            // si la fonction n'est pas definie sur certains points,
            // il ne faut pas afficher la distance
            if (expression == null || nbPts==0 || Double.isNaN(r)) reliabilityFactor[d]=null ;
            // sinon on calcule la MOYENNE des carres
            //marjolaine le 21/05/10: racine carree (1/n* sum(i=1=>n) di²
            //else reliabilityFactor[d] = reliabilityFactor[d] / sommeYCarre ;
            else {
                if(OLD_DIST){
                    reliabilityFactor[d] = Math.sqrt(oldR/nbPts);
                }else{
                    reliabilityFactor[d] = Math.sqrt(r/nbPts);
                }
                String s= "********** DISTANCES **********\n";
                //s += "reliabilityFactor old - sum(di²) / sum(yi²) avec di = |yi-f(xi)| : "+oldR / sommeYCarre+"\n";
                if(!Double.isNaN(oldR)){
                s += "reliabilityFactor - racineCarree(1/n * sum(di²)) avec di= |yi - f(xi)| : "+Math.sqrt(oldR/nbPts)+"\n";
                }
                //s += "pas : "+pasX+"\n";
                if(!Double.isNaN(r)){
                    s += "reliabilityFactor - racineCarree(1/n * sum(di²)) avec di= min(racineCarree((xi-x)²+(yi-f(x))²)) : "+Math.sqrt(r/nbPts)+"\n";
                }
                s += "*******************************\n";
                //System.out.println(s);
            }
        }
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
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

    public String getIdPredefFunction() {
        return idPredefFunction;
    }

    public void setIdPredefFunction(String idPredefFunction) {
        this.idPredefFunction = idPredefFunction;
    }
   
    public HashMap<String,Parametre> getMapParametre(){
        return mapDesParametres ;
    }

    public HashMap<String, Parametre> getNewMapDesParametres() {
        return newMapDesParametres;
    }
     
    /** fontion appeliee lors de la creation d'un nouveau parametre par l'analyseur
     * afin de mettre a jour la map des parametres de la fonction 
     */
    public void ajouterParametre (String nomParam, Parametre param){
        if(newMapDesParametres.get(nomParam) == null){
           newMapDesParametres.put(nomParam , param) ;
           param.setValeur(0.0) ;
        }
    }
    
    public void setValeurParametre (String nomParam, double valeur) {
       if(mapDesParametres.get(nomParam) != null)
        mapDesParametres.get(nomParam).setValeur(valeur) ;
    }
    
    public Double[] getRF(){
        return reliabilityFactor ;
    }  
}
