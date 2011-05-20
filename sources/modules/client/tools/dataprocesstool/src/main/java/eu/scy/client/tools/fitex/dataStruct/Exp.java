/*
 * Exp.java
 * Created on 10 mai 2007, 16:11
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Exp extends ExpressionUnaire
{
    
    /** Creates a new instance of Exp */
    public Exp(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.exp(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "exp(" + argument.toString() + ")" ;
    }
}
