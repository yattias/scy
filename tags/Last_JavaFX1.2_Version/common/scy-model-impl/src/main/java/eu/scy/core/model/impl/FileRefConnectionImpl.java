package eu.scy.core.model.impl;

import eu.scy.core.model.FileRef;
import eu.scy.core.model.FileRefConnection;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.pedagogicalplan.BaseObject;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 12:12:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "filerefconnection")
@DiscriminatorColumn(name = "connectiontype")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
public abstract class FileRefConnectionImpl extends BaseObjectImpl implements FileRefConnection{

    private FileRef fileRef;

    @Override
    @OneToOne(targetEntity = FileRefImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fileref_primKey")
    public FileRef getFileRef() {
        return fileRef;
    }

    @Override
    public void setFileRef(FileRef fileRef) {
        this.fileRef = fileRef;
    }

    @Transient
    public abstract BaseObject getConnectedElement();
}
