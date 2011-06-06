/*
 * Variable.java
 * Created on 10 mai 2007, 15:35
 */

package eu.scy.client.tools.fitex.dataStruct;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;

/**
 *
 * @author Cedric
 */
public class Variable implements Expression
{
    
    double variable ;
    private char type;
    
    /** Creates a new instance of Variable */
    public Variable(char type)
    {
        //System.out.println("var");
        this.type = type;
    }
    
    @Override
    public double valeur(double x)
    {
        return x ;
    }
    
    @Override
    public String toString()
    {
        if(type == DataConstants.FUNCTION_TYPE_X_FCT_Y)
            return "y";
        else
            return "x";
    }
}
