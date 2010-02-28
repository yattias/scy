package eu.scy.tools.copex.utilities;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.logger.CopexProperty;
import java.util.List;

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
}
