package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.server.controllers.components.fileupload.FileUploadListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 09:47:40
 * To change this template use File | Settings | File Templates.
 */
public class UploadELOBean {

    private MultipartFile file;
    private String username;
    private String action;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
