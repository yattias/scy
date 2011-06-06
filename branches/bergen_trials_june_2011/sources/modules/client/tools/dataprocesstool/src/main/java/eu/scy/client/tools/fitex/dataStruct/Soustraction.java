/*
 * Soustraction.java
 * Created on 10 mai 2007, 15:58
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Soustraction extends ExpressionBinaire
{
    
    /** Creates a new instance of Soustraction */
    public Soustraction(Expression g, Expression d)
    {
            super(g,d);
            //System.out.println("soust");
    }
    
    public double valeur(double x)
    {
        return (argumentGauche.valeur(x) - argumentDroite.valeur(x)) ;
    }
    
    public String toString()
    {
        return argumentGauche.toString() + "-" + argumentDroite.toString() ;
    }
}
