package eu.scy.core.model.impl;

import eu.scy.core.model.FileData;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:05:40
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "filedata")
public class FileDataImpl extends BaseObjectImpl implements FileData {

    private byte [] bytes;
    private String contentType;
    private String originalName;
    private long size;

    @Override
    @Lob
    @Column(name="file")
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getOriginalName() {
        return originalName;
    }

    @Override
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }
}
