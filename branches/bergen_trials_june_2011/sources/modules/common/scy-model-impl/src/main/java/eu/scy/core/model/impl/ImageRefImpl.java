package eu.scy.core.model.impl;

import eu.scy.core.model.FileData;
import eu.scy.core.model.ImageRef;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 10:00:07
 * To change this template use File | Settings | File Templates.
 */


@Entity
@DiscriminatorValue(value = "imageFile")
public class ImageRefImpl extends FileRefImpl implements ImageRef {

    private FileData icon;

    @Override
    @OneToOne(targetEntity = FileDataImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "iconFileData_primKey")
    public FileData getIcon() {
        return icon;
    }

    @Override
    public void setIcon(FileData icon) {
        this.icon = icon;
    }
}
