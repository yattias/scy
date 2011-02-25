/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.resultbinder;

/**
 *
 * @author Marjolaine
 */
public interface IActionResultBinder {
    /** logs a user action*/
    public void logAction(String type, String attributeKey, String attributeValue);
}
