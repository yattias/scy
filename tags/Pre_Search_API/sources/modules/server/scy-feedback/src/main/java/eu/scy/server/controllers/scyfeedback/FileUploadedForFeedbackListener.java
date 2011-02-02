package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.server.controllers.components.fileupload.FileUploadListener;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 06:01:40
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadedForFeedbackListener implements FileUploadListener {

    private static final Logger logger = Logger.getLogger(FileUploadedForFeedbackListener.class.getName());

    private File file;
    private ScyBaseObject model;



    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void fileUploaded(File file) {
        logger.info("FILE UPLOADED: " + file.getName());
    }

    @Override
    public void setModel(ScyBaseObject model) {
        this.model = model;
        logger.info("MODEL SET: " + model);
    }

    public ScyBaseObject getModel() {
        return model;
    }
}
