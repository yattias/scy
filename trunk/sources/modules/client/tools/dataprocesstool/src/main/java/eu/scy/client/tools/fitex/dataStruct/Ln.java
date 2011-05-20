/*
 * Ln.java
 * Created on 10 mai 2007, 16:11
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Ln extends ExpressionUnaire
{
    
    /** Creates a new instance of ln  */
    public Ln(Expression a)
    {
        super(a);
        //System.out.println("ln");
    } 
    
    public double valeur(double x)
    {
        return Math.log(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "ln(" + argument.toString() + ")" ;
    }
}
