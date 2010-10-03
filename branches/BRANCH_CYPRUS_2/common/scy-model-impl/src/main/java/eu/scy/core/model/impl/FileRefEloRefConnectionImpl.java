package eu.scy.core.model.impl;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.FileRefEloRefConnection;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.pedagogicalplan.BaseObject;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 12:17:12
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "eloref")
public class FileRefEloRefConnectionImpl extends FileRefConnectionImpl implements FileRefEloRefConnection {

    private ELORef eloRef;

    @Override
    @OneToOne(targetEntity = ELORefImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "eloref_primKey")
    public BaseObject getConnectedElement() {
        return this.eloRef;
    }

    @Override
    public void setConnectedElement(ELORef eloRef) {
        this.eloRef = eloRef;
    }
}
