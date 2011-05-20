/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.dataProcessTool.utilities;

import eu.scy.common.datasync.ISyncObject;
import eu.scy.client.tools.dataProcessTool.logger.FitexProperty;
import java.util.List;

/**
 *
 * @author Marjolaine
 */
public interface ActionDataProcessTool {
    public void resizeDataToolPanel(int width, int height);
    public void logAction(String type, List<FitexProperty> attribute);
    public void addFitexSyncObject(ISyncObject syncObject);
    public void changeFitexSyncObject(ISyncObject syncObject);
    public void removeFitexSyncObject(ISyncObject syncObject);
}
