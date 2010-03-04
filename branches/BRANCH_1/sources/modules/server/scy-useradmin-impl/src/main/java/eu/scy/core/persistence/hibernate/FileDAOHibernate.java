package eu.scy.core.persistence.hibernate;

import eu.scy.core.media.ImageConverter;
import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.impl.FileDataImpl;
import eu.scy.core.model.impl.FileRefImpl;
import eu.scy.core.model.impl.ImageRefImpl;
import eu.scy.core.persistence.FileDAO;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:32:07
 * To change this template use File | Settings | File Templates.
 */
public class FileDAOHibernate extends ScyBaseDAOHibernate implements FileDAO {

    private ImageConverter imageConverter;

    @Override
    public FileRef saveFile(File file) {
        FileData fileData = new FileDataImpl();
        fileData.setBytes(getBytes(file));
        fileData.setContentType(new MimetypesFileTypeMap().getContentType(file));
        fileData.setName(file.getName());
        save(fileData);

        FileRef fileRef = null;

        if(fileData.getContentType().contains("image")) {
            logger.info("Uploaded file is image - creating icon");
            try {
                fileRef = new ImageRefImpl();
                FileData icon = new FileDataImpl();
                File iconFile = getImageConverter().handleImageConversion(file);
                icon.setBytes(getBytes(iconFile));
                icon.setContentType("image/jpeg");
                icon.setName("ICON_" + file.getName());
                save(icon);
                ((ImageRef)fileRef).setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            fileRef = new FileRefImpl();
        }


        fileRef.setFileData(fileData);
        save(fileRef);
        return fileRef;
    }

    private byte [] getBytes(File file) {
        try {
            InputStream is = new FileInputStream(file);
            long length = file.length();
            byte[] bytes = new byte[(int)length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Problems reading file "+file.getName());
            }

            is.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("Could not get bytes from file");
    }

    public ImageConverter getImageConverter() {
        return imageConverter;
    }

    public void setImageConverter(ImageConverter imageConverter) {
        this.imageConverter = imageConverter;
    }
}
