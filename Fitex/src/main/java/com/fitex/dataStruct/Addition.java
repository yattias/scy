/*
 * Addition.java
 * Created on 10 mai 2007, 16:00
 */

package com.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Addition extends ExpressionBinaire
{
    
    /** Creates a new instance of Addition */
    public Addition(Expression g, Expression d)
    {
            super(g,d);
            //System.out.println("add");
    }
    
    public double valeur(double x)
    {
        return (argumentGauche.valeur(x) + argumentDroite.valeur(x)) ;
    }
    
    public String toString()
    {
        return argumentGauche.toString() + "+" + argumentDroite.toString() ;
    }
}
