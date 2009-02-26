/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.utilities;

/**
 * Elements à trier
 * @author Marjolaine Bodin
 */
public class ElementToSort {
    //PROPERTY
    /* Le nom de la colonne à trier */
    private String columnName = null;
		
    /* Le critére de tri :
       - 0 : décroissant
       - 1 : croissant
    */
    private int order = -1 ;
		
    
    // CONSTRUCTOR
    public ElementToSort(String columnName,int order){
        this.columnName = columnName;
        this.order=order;
    }

    @Override
    public String toString(){
        return this.columnName + " ("+order+") ";
    }

    public String getColumnName() {
        return columnName;
    }

    public int getOrder() {
        return order;
    }
    
}
