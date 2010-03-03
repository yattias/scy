package eu.scy.core;

import eu.scy.core.BaseServiceImpl;
import eu.scy.core.FileService;
import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;
import eu.scy.core.persistence.FileDAO;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 06:32:46
 * To change this template use File | Settings | File Templates.
 */
public class FileServiceImpl extends BaseServiceImpl implements FileService {

    private FileDAO fileDAO;

    public FileDAO getFileDAO() {
        return fileDAO;
    }

    public void setFileDAO(FileDAO fileDAO) {
        this.fileDAO = fileDAO;

    }

    @Transactional
    public FileRef saveFile(File file) {
        return getFileDAO().saveFile(file);
    }

    @Transactional
    public void saveFile(FileData fileData) {
        getFileDAO().save(fileData);
    }
}
