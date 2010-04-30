package eu.scy.core.model.impl.runtime;

import eu.scy.core.model.runtime.EloRuntimeAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 08:42:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("eloruntimeaction")
public class EloRuntimeActionImpl extends AbstractRuntimeActionImpl implements EloRuntimeAction {

    private String eloUri;

    @Override
    public String getEloUri() {
        return eloUri;
    }

    @Override
    public void setEloUri(String eloUri) {
        this.eloUri = eloUri;
    }
}
