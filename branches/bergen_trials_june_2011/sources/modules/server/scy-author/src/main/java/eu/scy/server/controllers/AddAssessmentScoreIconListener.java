package eu.scy.server.controllers;

import eu.scy.core.FileService;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.server.controllers.components.fileupload.FileUploadListener;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jul.2010
 * Time: 06:24:35
 * To change this template use File | Settings | File Templates.
 */
public class AddAssessmentScoreIconListener implements FileUploadListener {

    private PedagogicalPlan pedagogicalPlan;
    private FileService fileService;

    @Override
    public void fileUploaded(File file) {
        pedagogicalPlan.setAssessmentScoreIcon((ImageRef) getFileService().saveFile(file));
        getFileService().save(pedagogicalPlan);
    }

    @Override
    public void setModel(ScyBaseObject model) {
        this.pedagogicalPlan = (PedagogicalPlan) model;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
