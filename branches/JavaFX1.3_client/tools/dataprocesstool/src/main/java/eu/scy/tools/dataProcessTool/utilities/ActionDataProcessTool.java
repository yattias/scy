/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.tools.dataProcessTool.utilities;

import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import java.util.List;

/**
 *
 * @author Marjolaine
 */
public interface ActionDataProcessTool {
    public void resizeDataToolPanel(int width, int height);
    public void logAction(String type, List<FitexProperty> attribute);
}
