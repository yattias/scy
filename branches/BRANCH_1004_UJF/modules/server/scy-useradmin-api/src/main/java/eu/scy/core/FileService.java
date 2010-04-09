package eu.scy.core;

import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:31:29
 * To change this template use File | Settings | File Templates.
 */
public interface FileService extends BaseService{

    public FileRef saveFile(File file);

    public void saveFile(FileData fileData);


}
