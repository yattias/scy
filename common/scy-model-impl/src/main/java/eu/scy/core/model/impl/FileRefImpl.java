package eu.scy.core.model.impl;

import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:05:52
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table (name = "fileref")
@DiscriminatorColumn(name = "fileType")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
public class FileRefImpl extends BaseObjectImpl implements FileRef {

    private FileData fileData;

    @Override
    @OneToOne(targetEntity = FileDataImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fileData_primKey")
    public FileData getFileData() {
        return fileData;
    }

    @Override
    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
}
