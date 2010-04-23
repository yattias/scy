package eu.scy.client.common.datasync;

import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.common.datasync.ISyncObject;

/**
 *
 * @author lars
 */
public class DummySyncListener implements ISyncListener {

    @Override
    public void syncObjectAdded(ISyncObject syncObject) {}

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {}

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {}

}
