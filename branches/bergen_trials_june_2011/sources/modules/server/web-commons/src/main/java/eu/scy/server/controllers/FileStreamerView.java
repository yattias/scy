package eu.scy.server.controllers;

import eu.scy.core.FileService;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import org.springframework.web.servlet.view.AbstractView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 14:25:17
 * To change this template use File | Settings | File Templates.
 */
public class FileStreamerView extends AbstractView {

    private FileService fileService;
    private BufferedImage thumbnail;

    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Boolean showIcon = false;
        ServletOutputStream out = null;
        if (getEloImage() != null) {
            logger.info("Streaming ELO IMAGE!");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(getEloImage(), "png", baos);
            byte[] bytes = baos.toByteArray();
            out = httpServletResponse.getOutputStream();
            httpServletResponse.setContentType("image/png");
            httpServletResponse.setContentLength(bytes.length);
            out.write(bytes);
            out.flush();
        } else {
            if (httpServletRequest.getParameter("showIcon") != null) showIcon = true;
            String fileId = httpServletRequest.getParameter("id");

            FileRef fileRef = null;
            try {
                fileRef = getFileService().getFileRef(fileId);
            } catch (Exception e) {
                logger.warn("ImageRef does not exist!");
                fileRef = null;
            }


            if (fileRef != null) {

                if (fileRef instanceof ImageRef) {
                    ImageRef imageRef = (ImageRef) fileRef;
                    byte[] bytes = null;
                    if (imageRef.getIcon() != null) {
                        if (showIcon) {
                            bytes = (imageRef).getIcon().getBytes();
                        } else {
                            bytes = imageRef.getFileData().getBytes();
                        }

                        out = httpServletResponse.getOutputStream();
                        httpServletResponse.setContentType(imageRef.getFileData().getContentType());
                        httpServletResponse.setContentLength(bytes.length);
                        out.write(bytes);
                        out.flush();
                    }


                } else {
                    logger.info("Streaming file ref!");
                    byte[] bytes = null;
                    bytes = fileRef.getFileData().getBytes();
                    out = httpServletResponse.getOutputStream();
                    httpServletResponse.setContentType(fileRef.getFileData().getContentType());
                    httpServletResponse.setContentLength(bytes.length);
                    out.write(bytes);
                    out.flush();
                }

            }

        }


    }


    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setEloImage(BufferedImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BufferedImage getEloImage() {
        return this.thumbnail;
    }
}
