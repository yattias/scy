package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:29:25
 * To change this template use File | Settings | File Templates.
 */
public interface FileData extends BaseObject {



    public byte[] getBytes();

    public void setBytes(byte[] bytes);

    public String getContentType();

    public void setContentType(String contentType);

    String getOriginalName();

    void setOriginalName(String originalName);

    long getSize();

    void setSize(long size);
}
