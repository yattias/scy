/*
 * Acos.java
 * Created on 27 mai 2007, 12:53
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Acos extends ExpressionUnaire
{
    
    /** Creates a new instance of Acos */
    public Acos(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.acos(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "acos(" + argument.toString() + ")" ;
    }
}
