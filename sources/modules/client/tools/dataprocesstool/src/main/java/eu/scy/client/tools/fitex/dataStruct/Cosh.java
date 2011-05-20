/*
 * Cosh.java
 * Created on 27 mai 2007, 17:50
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Cosh extends ExpressionUnaire
{
    
    /** Creates a new instance of Cosh */
    public Cosh(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return (Math.exp(argument.valeur(x))+Math.exp(-1*argument.valeur(x)))/2;
    }
    
    public String toString()
    {
        return "cosh(" + argument.toString() + ")" ;
    }
}
