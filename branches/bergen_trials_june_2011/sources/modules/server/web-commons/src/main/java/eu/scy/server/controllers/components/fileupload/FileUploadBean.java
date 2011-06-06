package eu.scy.server.controllers.components.fileupload;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 05:57:50
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadBean {

    private MultipartFile file;
    private String listener;
    private String model;

    public MultipartFile getFile() {
        // System.out.println("returning file: " + file);
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
