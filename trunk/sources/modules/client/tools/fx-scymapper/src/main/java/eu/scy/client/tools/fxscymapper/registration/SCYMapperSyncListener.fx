/*
 * SCYMapperSyncListener.fx
 *
 * Created on 16.02.2010, 21:29:23
 */

package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.client.common.datasync.ISyncListener;
import java.lang.UnsupportedOperationException;
import eu.scy.common.datasync.ISyncObject;

/**
 * @author Sven
 */

public class SCYMapperSyncListener extends ISyncListener{
        
    override public function syncObjectChanged (syncObject : ISyncObject) : Void {
            println("syncObjectChanged");
        throw new UnsupportedOperationException('Not implemented yet');
    }

    override public function syncObjectRemoved (syncObject : ISyncObject) : Void {
            println("syncObjectRemoved");
        throw new UnsupportedOperationException('Not implemented yet');
    }

    override public function syncObjectAdded (arg0 : ISyncObject) : Void {
            println("syncObjectAdded");
        throw new UnsupportedOperationException('Not implemented yet');
    }


}
