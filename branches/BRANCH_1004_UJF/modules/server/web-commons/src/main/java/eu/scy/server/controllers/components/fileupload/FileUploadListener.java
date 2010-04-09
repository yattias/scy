package eu.scy.server.controllers.components.fileupload;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 07:03:03
 * To change this template use File | Settings | File Templates.
 */
public interface FileUploadListener {

    public void fileUploaded(File file);
    
}
