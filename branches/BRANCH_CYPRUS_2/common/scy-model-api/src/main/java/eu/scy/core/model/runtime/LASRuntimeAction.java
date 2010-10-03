package eu.scy.core.model.runtime;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2010
 * Time: 23:01:32
 * To change this template use File | Settings | File Templates.
 */
public interface LASRuntimeAction extends AbstractRuntimeAction{
    String getNewLASId();

    void setNewLASId(String newLASId);

    String getOldLASId();

    void setOldLASId(String oldLASId);
}
