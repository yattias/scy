/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * represente un parametre lie a  un  materiel
 * @author MBO
 */
public class Parameter extends Quantity{

    // CONSTRUCTEUR
    public Parameter(long dbKey, String name, String type, double value, String uncertainty, CopexUnit unit) {
        super(dbKey, name, type, value, uncertainty, unit);
    }

}
