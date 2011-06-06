/*
 * Constante.java
 * Created on 10 mai 2007, 15:32
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Constante implements Expression
{
    
    double constante;
    
    /** Creates a new instance of Constante */
    public Constante(double c)
    {
        constante = c ;
        //System.out.println("const");
    }
    
    public double valeur(double x)
    {
        return constante ;
    }
    
    public String toString()
    {
        return "(" + String.valueOf(constante) + ")";
    }
}
