/*
 * Asin.java
 * Created on 27 mai 2007, 13:10
 */

package com.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
public class Asin extends ExpressionUnaire
{
    
    /** Creates a new instance of Asin */
    public Asin(Expression a)
    {
        super(a);
    }
    
    public double valeur(double x)
    {
        return Math.asin(argument.valeur(x)) ;
    }
    
    public String toString()
    {
        return "asin(" + argument.toString() + ")" ;
    }
}
