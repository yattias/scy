/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

/**
 *
 * @author Sven
 */
public enum OnlineState {
    
      ONLINE,AWAY,OFFLINE,PENDING,IS_ME;

   private final ResourceBundleWrapper resourceBundleWrapper = new ResourceBundleWrapper(this);

   @Override
    public String toString() {
        switch(this){
            case ONLINE: return resourceBundleWrapper.getString("onlineState.online");
            case AWAY: return resourceBundleWrapper.getString("onlineState.away");
            case OFFLINE: return resourceBundleWrapper.getString("onlineState.offline");
            case PENDING: return resourceBundleWrapper.getString("onlineState.pending");
            case IS_ME: return resourceBundleWrapper.getString("onlineState.thatsMe");
            default:return "";
        }
    }



}
