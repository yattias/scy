package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 12:27:06
 * To change this template use File | Settings | File Templates.
 */
public interface FileRefConnection extends BaseObject {

    FileRef getFileRef();

    void setFileRef(FileRef fileRef);
}
