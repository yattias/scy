/*
 * Tan.java
 * Created on 27 mai 2007, 13:11
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Tan extends ExpressionUnaire
{
    
    /** Creates a new instance of Tan */
    public Tan(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.tan(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "tan(" + argument.toString() + ")" ;
    }
}

