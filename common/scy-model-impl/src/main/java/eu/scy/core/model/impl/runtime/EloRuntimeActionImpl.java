package eu.scy.core.model.impl.runtime;

import eu.scy.core.model.runtime.EloRuntimeAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Transient
    public String getDate() {
        Date aDate = new Date(getTimeInMillis());
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat("dd/MM/yyyy hh:ss");
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

}
