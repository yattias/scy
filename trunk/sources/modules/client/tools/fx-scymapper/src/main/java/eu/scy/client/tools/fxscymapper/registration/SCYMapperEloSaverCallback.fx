/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import org.apache.log4j.Logger;

/**
 * @author weinbrenner
 */
public class SCYMapperEloSaverCallback extends EloSaverCallBack {

    var logger = Logger.getLogger(SCYMapperEloSaverCallback.class.getName());
    public-init var scyWindow: ScyWindow;
    public-init var droppedObject: Object;
    public-init var functionToCallBefore: function(elo: IELO) ;

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        functionToCallBefore(elo);
        var c: ContactFrame = droppedObject as ContactFrame;
        logger.debug("acceptDrop user: {c.contact.name}");
        scyWindow.ownershipManager.addPendingOwner(c.contact.name);
        scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}", scyWindow.eloUri.toString(), scyWindow.mucId);
        logger.debug("scyDesktop: {scyWindow.windowManager.scyDesktop}");
    }

}
