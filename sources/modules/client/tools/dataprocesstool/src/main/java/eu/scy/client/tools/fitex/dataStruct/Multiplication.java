/*
 * Multiplication.java
 * Created on 10 mai 2007, 15:59
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Multiplication extends ExpressionBinaire {
    
    /** Creates a new instance of Multiplication */
    public Multiplication(Expression g, Expression d) {
            super(g,d);
            //System.out.println("mult");
    }
    
    public double valeur(double x)
    {
        return (argumentGauche.valeur(x) * argumentDroite.valeur(x)) ;
    }
    
    public String toString()
    {
        return argumentGauche.toString() + "*" + argumentDroite.toString() ;
    }
}
