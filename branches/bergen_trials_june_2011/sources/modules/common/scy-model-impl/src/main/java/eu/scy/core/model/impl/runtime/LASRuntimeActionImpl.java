package eu.scy.core.model.impl.runtime;

import eu.scy.core.model.runtime.LASRuntimeAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2010
 * Time: 23:02:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("lasruntimeaction")
public class LASRuntimeActionImpl extends AbstractRuntimeActionImpl implements LASRuntimeAction{

    private String newLASId;
    private String oldLASId;

    @Override
    public String getNewLASId() {
        return newLASId;
    }

    @Override
    public void setNewLASId(String newLASId) {
        this.newLASId = newLASId;
    }

    @Override
    public String getOldLASId() {
        return oldLASId;
    }

    @Override
    public void setOldLASId(String oldLASId) {
        this.oldLASId = oldLASId;
    }
}
