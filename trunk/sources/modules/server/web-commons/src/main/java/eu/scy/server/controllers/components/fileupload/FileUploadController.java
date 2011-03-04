package eu.scy.server.controllers.components.fileupload;

import eu.scy.core.FileService;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
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
public class FileUploadController extends SimpleFormController implements ApplicationContextAware {

    private FileService fileService;

    private List<FileUploadListener> fileUploadListeners = new LinkedList<FileUploadListener>();

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();

        return dataMap;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        String listener = (String) request.getParameter("listener");
        modelAndView.addObject("listener", listener);

        return modelAndView;



    }

    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {
        logger.info("*********************************************************************************************************************************************************");
        FileUploadBean bean = (FileUploadBean) command;
        request.setCharacterEncoding("UTF-8");

        logger.info("Uploading file!!");
        logger.info("encoding: " + request.getCharacterEncoding());

        MultipartFile file = bean.getFile();
        File tmpFile = new File(Math.random() + file.getOriginalFilename());
        tmpFile.deleteOnExit();
        file.transferTo(tmpFile);

        String listener = bean.getListener();

        if (file.getContentType().contains("image")) {
            getFileService().saveFile(tmpFile);
        } else {
            getFileService().saveFile(tmpFile);
        }

        if (listener != null) {


            FileUploadListener fileUploaded = (FileUploadListener) getApplicationContext().getBean(bean.getListener());

            String modelId = parseModelId(bean.getModel());
            Class clazz = parseClass(bean.getModel());

            logger.info("MODEL ID: " + modelId + " CLASS:_ " + clazz);

            ScyBaseObject model = (ScyBaseObject) getFileService().get(modelId, clazz);
            fileUploaded.setModel(model);

            fileUploaded.fileUploaded(tmpFile);

        }

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    private Class parseClass(String model) {
        if(model != null) {
            String [] array = model.split("_");
            try {
                Class clazz = Class.forName(array[0]);
                return clazz;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return ScyBaseObject.class;
    }

    private String parseModelId(String model) {
        logger.info("******************************************** PARSING MODEL ID: " + model);
        if(model != null) {
            String [] array = model.split("_");
            String id = array[1];
            return id;
        }

        return "";
    }

    protected boolean isFormSubmission(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            logger.error(uee);
            throw new RuntimeException(uee);
        }
        logger.info("encoding: " + request.getCharacterEncoding());

        return super.isFormSubmission(request);
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
