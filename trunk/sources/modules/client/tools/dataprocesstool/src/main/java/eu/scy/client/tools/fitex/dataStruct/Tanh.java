/*
 * Tanh.java
 * Created on 27 mai 2007, 17:51
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Tanh extends ExpressionUnaire
{
    
    /** Creates a new instance of Tanh */
    public Tanh(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return (Math.exp(2*argument.valeur(x))-1)/(Math.exp(2*argument.valeur(x))+1);
    }
    
    public String toString()
    {
        return "tanh(" + argument.toString() + ")" ;
    }
}
