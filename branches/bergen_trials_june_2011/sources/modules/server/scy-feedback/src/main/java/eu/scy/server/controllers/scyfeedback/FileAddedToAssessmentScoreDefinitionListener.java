package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.FileService;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentScoreDefinitionImpl;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;
import eu.scy.server.controllers.components.fileupload.FileUploadListener;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jul.2010
 * Time: 23:06:10
 * To change this template use File | Settings | File Templates.
 */
public class FileAddedToAssessmentScoreDefinitionListener implements FileUploadListener {

    private static final Logger logger = Logger.getLogger(FileAddedToAssessmentScoreDefinitionListener.class.getName());

    private File file;
    private ScyBaseObject model;

    private FileService fileService;

    @Override
    public void fileUploaded(File file) {
        this.file = file;
        logger.info("FILE: " + file + " model: " + getModel());
        AssessmentScoreDefinition assessmentScoreDefinition = (AssessmentScoreDefinition) getModel();
        FileRef fileRef = getFileService().saveFile(file);
        assessmentScoreDefinition.setFileRef(fileRef);
        getFileService().save(assessmentScoreDefinition);

    }

    @Override
    public void setModel(ScyBaseObject model) {
        this.model = model;
    }

    public ScyBaseObject getModel() {
        return model;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
