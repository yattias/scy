/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.contact;

/**
 *
 * @author weinbrenner
 */
public interface ChatInitiator {

    public void startChat(String contactName);

    public void startChat(String contactName, String initialMessage);

}
