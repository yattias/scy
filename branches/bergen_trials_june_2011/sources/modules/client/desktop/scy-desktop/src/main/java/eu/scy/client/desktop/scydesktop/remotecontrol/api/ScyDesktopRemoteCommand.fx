/*
 * RemoteAction.fx
 *
 * Created on 22.04.2010, 13:05:38
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.api;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.desktoputils.log4j.Logger;

/**
 * @author sven
 */
public mixin class ScyDesktopRemoteCommand extends IRemoteCommand {

    public def logger: Logger = Logger.getLogger(getClass());
    public-init var scyDesktop: ScyDesktop;
}
