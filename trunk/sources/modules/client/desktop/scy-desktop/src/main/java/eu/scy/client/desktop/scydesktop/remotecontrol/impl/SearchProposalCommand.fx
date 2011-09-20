/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.notification.api.INotification;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.tools.search.HistoryEntry;

/**
 * @author weinbrenner
 */
public class SearchProposalCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "search_proposal"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************search_proposal*************************");
        def proposals: String[] = notification.getPropertyArray("proposals");
        def counts: String[] = notification.getPropertyArray("counts");

        var suggestions: HistoryEntry[] = [];

        for (i in [0..sizeof proposals - 1]) {
            insert HistoryEntry {
                nrOfResults: Integer.parseInt(counts[i]);
                query: proposals[i];
                selecterOptions: [];
            } into suggestions;

        }

        scyDesktop.eloManagement.eloSearchNode.setSuggestions(suggestions);
    }

}
