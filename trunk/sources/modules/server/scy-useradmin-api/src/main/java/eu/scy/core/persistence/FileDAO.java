package eu.scy.core.persistence;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;

import java.io.File;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:31:48
 * To change this template use File | Settings | File Templates.
 */
public interface FileDAO extends BaseDAO{
    FileRef saveFile(File file);

    void addFileToELORef(FileRef fileRef, ELORef eloRef);

    List getFilesForELORef(ELORef eloRef);

    FileRef getFileRef(String fileId);
}
