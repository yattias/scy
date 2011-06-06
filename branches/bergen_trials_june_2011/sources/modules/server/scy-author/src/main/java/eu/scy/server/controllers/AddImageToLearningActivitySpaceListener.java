package eu.scy.server.controllers;

import eu.scy.core.FileService;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.server.controllers.components.fileupload.FileUploadListener;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2010
 * Time: 12:38:48
 * To change this template use File | Settings | File Templates.
 */
public class AddImageToLearningActivitySpaceListener implements FileUploadListener {

    private FileService fileService;
    private LearningActivitySpace learningActivitySpace;

    @Override
    public void fileUploaded(File file) {
        learningActivitySpace.setImage(getFileService().saveFile(file));
        getFileService().save(learningActivitySpace);
    }

    @Override
    public void setModel(ScyBaseObject model) {
        this.learningActivitySpace = (LearningActivitySpace) model;

    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
