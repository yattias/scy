/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.resultbinder;

/**
 * actions for the resultBinder
 * @author Marjolaine
 */
public interface IActionResultBinder {
    /** logs a user action, modification of an element
     * @param attributeKey the key of the attribute
     * @param attributeValue the value of the element
     */
    public void logAction(String type, String attributeKey, String attributeValue);
}
