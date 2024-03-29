package eu.scy.client.tools.copex.utilities;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.logger.CopexProperty;
import java.util.List;
import eu.scy.common.datasync.ISyncObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * interface copexPanel
 * @author Marjolaine
 */
public interface ActionCopex {
    public void loadHelpProc(LearnerProcedure helpProc);

    public void logAction(String type, List<CopexProperty> attribute);
    public void addCopexSyncObject(ISyncObject syncObject);
    public void changeCopexSyncObject(ISyncObject syncObject);
    public void removeCopexSyncObject(ISyncObject syncObject);
}
