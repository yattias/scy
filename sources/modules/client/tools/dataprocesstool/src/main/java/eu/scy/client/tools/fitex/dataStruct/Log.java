/*
 * Ln.java
 * Created on 10 mai 2007, 16:11
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Log extends ExpressionUnaire
{
    
    /** Creates a new instance of ln  */
    public Log(Expression a)
    {
        super(a);
        //System.out.println("ln");
    } 
    
    public double valeur(double x)
    {
        return Math.log10(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "log(" + argument.toString() + ")" ;
    }
}
