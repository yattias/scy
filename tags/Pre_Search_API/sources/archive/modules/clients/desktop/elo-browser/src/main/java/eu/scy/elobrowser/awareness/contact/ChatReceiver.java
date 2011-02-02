/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.awareness.contact;

/**
 *
 * @author weinbrenner
 */
public interface ChatReceiver {

    public void receiveMessage(String sender, String text);

}
