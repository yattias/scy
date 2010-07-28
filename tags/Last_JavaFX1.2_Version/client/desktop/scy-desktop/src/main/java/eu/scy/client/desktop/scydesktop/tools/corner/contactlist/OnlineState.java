/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

/**
 *
 * @author Sven
 */
public enum OnlineState {
    
      ONLINE,AWAY,OFFLINE;

    @Override
    public String toString() {
        switch(this){
            case ONLINE: return "Online";
            case AWAY: return "Away";
            case OFFLINE: return "Offline";
            default:return "";
        }
    }



}
