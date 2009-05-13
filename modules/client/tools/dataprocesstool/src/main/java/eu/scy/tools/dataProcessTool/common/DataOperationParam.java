/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.util.ArrayList;

/**
 * operation parametree
 * @author Marjolaine
 */
public class DataOperationParam extends DataOperation implements Cloneable{
    // PROPERTY
    /* liste des parametres */
    ParamOperation[] allParam;

    // CONSTRUCTOR
    public DataOperationParam(long dbKey, String name, TypeOperation typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ParamOperation[] allParam) {
        super(dbKey, name, typeOperation, isOnCol, listNo);
        this.allParam = allParam;
    }

    // GETTER AND SETTER
    public ParamOperation[] getAllParam() {
        return allParam;
    }

    public void setAllParam(ParamOperation[] allParam) {
        this.allParam = allParam;
    }

    @Override
    public Object clone()  {
        DataOperationParam op = (DataOperationParam) super.clone() ;
        ParamOperation[] allParamC = new ParamOperation[this.allParam.length];
        for (int i=0; i<allParam.length; i++){
            allParamC[i] = (ParamOperation)this.allParam[i].clone();
        }
        op.setAllParam(allParamC);
        return op;
    }



}
