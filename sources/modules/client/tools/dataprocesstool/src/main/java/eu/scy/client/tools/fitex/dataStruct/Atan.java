/*
 * Atan.java
 * Created on 27 mai 2007, 13:12
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Atan extends ExpressionUnaire
{
    
    /** Creates a new instance of Atan */
    public Atan(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.atan(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "atan(" + argument.toString() + ")" ;
    }
}

