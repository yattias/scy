package eu.scy.server.controllers.components.fileupload;

import eu.scy.core.FileService;
import eu.scy.core.UserService;
import eu.scy.core.model.*;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.feb.2011
 * Time: 07:12:03
 * To change this template use File | Settings | File Templates.
 */
public class UserProfileUploadController extends SimpleFormController {

    private static Logger log = Logger.getLogger("ProfilePictureUploadController.class");

    private UserService userService;
    private FileService fileService;

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        log.info("*********************************************************************************************************************************************************");
        return super.showForm(request, response, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("*********************************************************************************************************************************************************");
        return super.handleRequestInternal(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }



    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        log.info("*********************************************************************************************************************************************************");
        Map<Object, Object> dataMap = new HashMap<Object, Object>();

        User user = getUserService().getUser(getCurrentUserName(request));
        UserDetails details = user.getUserDetails();

        dataMap.put("userDetails", details);
        dataMap.put("hei", "Hei");
        return dataMap;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("*********************** HANDLE REQUEST");
        ModelAndView modelAndView = super.handleRequest(request, response);
        modelAndView.addObject("username", getCurrentUserName(request));

        User user = getUserService().getUser(getCurrentUserName(request));
        UserDetails details = user.getUserDetails();

        modelAndView.addObject("userDetails", details);


        return modelAndView;
    }



    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {
        log.info("ON SUBMIT");
        FileUploadBean bean = (FileUploadBean) command;

        User user = getUserService().getUser(getCurrentUserName(request));
        UserDetails details = user.getUserDetails();


        MultipartFile file = bean.getFile();
        File tmpFile = new File(file.getOriginalFilename());
        tmpFile.deleteOnExit();
        file.transferTo(tmpFile);

        if(file.getContentType().contains("image")) {
            ImageRef fileRef = (ImageRef) getFileService().saveFile(tmpFile);
            if(details instanceof StudentUserDetails) {
                ((StudentUserDetails)details).setProfilePicture(fileRef);
            } else if(details instanceof TeacherUserDetails) {
                log.info("Saving profile picture for teacher!");
                ((TeacherUserDetails)details).setProfilePicture(fileRef);
            }

            log.info("saved image!");
        } else {
            FileRef fileRef = getFileService().saveFile(tmpFile);
            log.info("saved file!");
        }

        getUserService().save(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/editors/userProfileEditor.html");
        return modelAndView;
    }


    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        return user.getUsername();


    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }


}
