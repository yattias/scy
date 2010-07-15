package eu.scy.core;

import eu.scy.core.BaseServiceImpl;
import eu.scy.core.FileService;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import eu.scy.core.persistence.FileDAO;
import eu.scy.core.persistence.SCYBaseDAO;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

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
        setScyBaseDAO((SCYBaseDAO) fileDAO);
        this.fileDAO = fileDAO;

    }

    @Override
    @Transactional
    public FileRef saveFile(File file) {
        return getFileDAO().saveFile(file);
    }

    @Override
    @Transactional
    public void saveFile(FileData fileData) {
        getFileDAO().save(fileData);
    }

    @Override
    @Transactional
    public void addFileToELORef(FileRef fileRef, ELORef eloRef) {
        getFileDAO().addFileToELORef(fileRef, eloRef);
    }

    @Override
    public List getFilesForELORef(ELORef eloRef) {
        return getFileDAO().getFilesForELORef(eloRef);

    }

    @Override
    public FileRef getFileRef(String fileId) {
        return getFileDAO().getFileRef(fileId);
    }
}
