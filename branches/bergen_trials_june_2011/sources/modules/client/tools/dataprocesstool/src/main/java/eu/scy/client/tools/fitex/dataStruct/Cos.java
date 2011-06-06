/*
 * Cos.java
 * Created on 10 mai 2007, 16:10
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Cos extends ExpressionUnaire
{
    
    /** Creates a new instance of Cos */
    public Cos(Expression a)
    {
        super(a);
        //System.out.println("cos");
    }
    
    public double valeur(double x)
    {
        return Math.cos(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "cos(" + argument.toString() + ")" ;
    }
}
