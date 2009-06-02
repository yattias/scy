/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * quantity de type data
 * @author Marjolaine
 */
public class QData extends Quantity {

    // CONSTRUCTOR
    public QData(long dbKey, String name, String type, double value, String uncertainty, CopexUnit unit) {
        super(dbKey, name, type, value, uncertainty, unit);
    }

}
