/*
 * Rac.java
 * Created on 27 mai 2007, 13:12
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Rac extends ExpressionUnaire
{
    
    /**
     * Creates a new instance of Rac
     */
    public Rac(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.sqrt(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "rac(" + argument.toString() + ")" ;
    }
}
