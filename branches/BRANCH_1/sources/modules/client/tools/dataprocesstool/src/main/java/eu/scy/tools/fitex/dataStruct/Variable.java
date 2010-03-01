/*
 * Variable.java
 * Created on 10 mai 2007, 15:35
 */

package eu.scy.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Variable implements Expression
{
    
    double variable ;
    
    /** Creates a new instance of Variable */
    public Variable()
    {
        //System.out.println("var");
    }
    
    public double valeur(double x)
    {
        return x ;
    }
    
    public String toString()
    {
        return "x";
    }
}
