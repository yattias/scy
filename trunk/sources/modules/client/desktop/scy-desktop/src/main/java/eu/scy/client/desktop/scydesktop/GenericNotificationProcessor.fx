package eu.scy.client.desktop.scydesktop;

import eu.scy.notification.api.INotifiable;
import java.lang.UnsupportedOperationException;
import eu.scy.notification.api.INotification;
import org.apache.log4j.Logger;

/**
 * @author lars
 */

public class GenericNotificationProcessor  extends INotifiable {

    public-init var scyDesktop: ScyDesktop;
    def logger = Logger.getLogger(this.getClass());

    override public function processNotification (notification: INotification) : Boolean {
        logger.debug("processing notification {notification.getUniqueID()}");
        return true;
    }


}
