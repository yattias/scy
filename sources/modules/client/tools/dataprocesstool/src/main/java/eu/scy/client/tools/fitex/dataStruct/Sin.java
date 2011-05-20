/*
 * Sin.java
 * Created on 10 mai 2007, 16:01
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Sin extends ExpressionUnaire
{
    
    /** Creates a new instance of Sin */
    public Sin(Expression a)
    {
        super(a);
        //System.out.println("sin");
    }
    
    public double valeur(double x)
    {
        return Math.sin(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "sin(" + argument.toString() + ")" ;
    }
}