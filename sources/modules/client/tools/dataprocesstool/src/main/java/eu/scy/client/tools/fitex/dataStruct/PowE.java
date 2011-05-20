/*
 * PowE.java
 * Created on 26 mai 2007, 14:23
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class PowE extends ExpressionBinaire {
    
    /** Creates a new instance of PowE */
    public PowE(Expression g, Expression d){
            super(g,d);
    }
        
    public double valeur(double x)
    {
        return argumentGauche.valeur(x) * Math.pow(10.0 , argumentDroite.valeur(x)) ;
    }
    
    public String toString()
    {
        return argumentGauche.toString() + "E" + argumentDroite.toString() ;
    }
}
