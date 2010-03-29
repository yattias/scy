package eu.scy.server.controllers.components.fileupload;

import eu.scy.core.FileService;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 05:53:45
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadController extends SimpleFormController {

    private FileService fileService;

    private List<FileUploadListener> fileUploadListeners = new LinkedList<FileUploadListener>();

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();

        return dataMap;
    }


    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {
logger.info("*********************************************************************************************************************************************************");
        FileUploadBean bean = (FileUploadBean) command;

        logger.info("Uploading file!!");

        MultipartFile file = bean.getFile();
        File tmpFile = new File(file.getOriginalFilename());
        tmpFile.deleteOnExit();
        file.transferTo(tmpFile);

        String listener = bean.getListener();

        if (file.getContentType().contains("image")) {
            ImageRef fileRef = (ImageRef) getFileService().saveFile(tmpFile);
        } else {
            FileRef fileRef = getFileService().saveFile(tmpFile);
        }

        if (listener != null) {
            Class fileUploadListener = Class.forName(listener);
            FileUploadListener uploadListener = (FileUploadListener) fileUploadListener.newInstance();
            uploadListener.fileUploaded(tmpFile);
        }

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public List<FileUploadListener> getFileUploadListeners() {
        return fileUploadListeners;
    }

    public void setFileUploadListeners(List<FileUploadListener> fileUploadListeners) {
        this.fileUploadListeners = fileUploadListeners;
    }
}
