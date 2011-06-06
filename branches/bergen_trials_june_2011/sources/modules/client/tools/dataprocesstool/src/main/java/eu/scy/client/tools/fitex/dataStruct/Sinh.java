/*
 * Sinh.java
 * Created on 27 mai 2007, 17:48
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Sinh extends ExpressionUnaire
{
    
    /** Creates a new instance of Sinh */
    public Sinh(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return (Math.exp(argument.valeur(x))-Math.exp(-1*argument.valeur(x)))/2;
    }
    
    public String toString()
    {
        return "sinh(" + argument.toString() + ")" ;
    }
}
