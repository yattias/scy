/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.controller;

import eu.scy.client.tools.dataProcessTool.common.TypeOperation;
import java.util.ArrayList;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;

/**
 * calculating in fitex
 * @author Marjolaine
 */
public class ScyMath {
    
    /*  calculating on a list*/
    public static  double calculate(TypeOperation typeOperation, ArrayList<Double> listValue){
        int  type  = typeOperation.getType();
        switch (type){
            case DataConstants.OP_SUM :return sum(listValue);
            case DataConstants.OP_AVERAGE:return average(listValue);
            case DataConstants.OP_MIN:return min(listValue);
            case DataConstants.OP_MAX:return max(listValue);
            default:return 0;
        }
    }

    
     
    /* returns the sum */
    public static double sum(ArrayList<Double> listValue){
        if (listValue == null)
            return 0;
        int nb = listValue.size();
        double sum = 0;
        for (int i=0; i<nb; i++){
            sum += listValue.get(i);
        }
        return sum;
    }
    
    /* returns the avg = somme / nb */
    public static double average(ArrayList<Double> listValue){
        double sum = sum(listValue);
        double nb = 0;
        if (listValue != null)
            nb = listValue.size();
        return nb == 0 ?  0 : ((double)(sum / nb));
    }
    
    /* return the min value in the list */
    public static double min(ArrayList<Double> listValue){
        if (listValue == null)
            return  0;
        int nb = listValue.size();
        if (nb == 0)
            return 0;
        double min = listValue.get(0);
        for (int i=1; i<nb; i++){
            if (listValue.get(i) < min)
                min = listValue.get(i);
        }
        return min;
    }
    
    /* returns the max value of the list */
    public static double max(ArrayList<Double> listValue){
        if (listValue == null)
            return  0;
        int nb = listValue.size();
        if (nb == 0)
            return 0;
        double max = listValue.get(0);
        for (int i=1; i<nb; i++){
            if (listValue.get(i) > max)
                max = listValue.get(i);
        }
        return max;
    }
     
}
