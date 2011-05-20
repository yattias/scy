/*
 * Power.java
 * Created on 26 mai 2007, 14:19
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Power extends ExpressionBinaire {
    
    /** Creates a new instance of Power */
    public Power(Expression g, Expression d){
            super(g,d);
    }
        
    public double valeur(double x)
    {
        return Math.pow(argumentGauche.valeur(x), argumentDroite.valeur(x)) ;
    }
    
    public String toString()
    {
        return argumentGauche.toString() + "^" + argumentDroite.toString() ;
    }
}
