/*
 * ExpressionBinaire.java
 * Created on 10 mai 2007, 15:03
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
abstract public class ExpressionBinaire implements Expression
{
    protected Expression argumentGauche, argumentDroite;
    
    /** Creates a new instance of ExpressionBinaire */
    public ExpressionBinaire(Expression g, Expression d)
    {
        argumentGauche = g;
        argumentDroite = d;
    }
    
}
