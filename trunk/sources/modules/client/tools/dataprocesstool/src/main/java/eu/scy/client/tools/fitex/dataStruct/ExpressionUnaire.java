/*
 * ExpressionUnaire.java
 * Created on 10 mai 2007, 15:06
 */

package eu.scy.client.tools.fitex.dataStruct;

/**
 *
 * @author Cedric
 */
abstract public class ExpressionUnaire implements Expression
{
    protected Expression argument = null;
    
    /** Creates a new instance of ExpressionUnaire */
    public ExpressionUnaire()
    {
    }
    
    /** Creates a new instance of ExpressionUnaire */
    public ExpressionUnaire(Expression a)
    {
        setArgument(a);
    }
            
    void setArgument(Expression a)
    {
        argument = a;
    }
    
}