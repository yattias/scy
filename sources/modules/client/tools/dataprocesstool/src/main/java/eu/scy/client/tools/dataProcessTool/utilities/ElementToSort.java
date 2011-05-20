/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

/**
 * Elements to sort
 * @author Marjolaine Bodin
 */
public class ElementToSort {
    /* name of the column to sort */
    private String columnName = null;
		
    /* Sort Criteria
       - 0 : descending
       - 1 : ascending
    */
    private int order = -1 ;
		
    
    public ElementToSort(String columnName, int order){
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
