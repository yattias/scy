/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Sven
 */
public class EloImporterModel {

    private static final Logger logger = Logger.getLogger(EloImporterModel.class.getName());
    private File file;
    private String fileEncoded;
    private String filename;
    private String description;
    private String title;
    private long fileSize;
    private String fileFormat;
    private long fileLastModified;

    public EloImporterModel() {
        this(null, "", "", "");
    }

    public EloImporterModel(File file, String filename, String description, String title) {
        updateModel(file, filename, description, title);
        logger.info("created EloImporterModel");
    }

    public void updateModel(File file, String filename, String description, String title) {
        this.file = file;
        this.filename = filename;
        this.description = description;
        this.title = title;
        if (file != null) {
            try {
                this.fileEncoded = ImportUtils.encodeBase64(ImportUtils.getBytesFromFile(file));
                logger.info("Java  -base64-encoded: " + fileEncoded);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(EloImporterModel.class.getName()).log(Level.SEVERE, null, ex);
            }
           this.fileSize = file.length();
           fileLastModified = file.lastModified();
        }
        String[] filenameSplitted = filename.split(".");
        this.fileFormat = filenameSplitted[filenameSplitted.length-1];
        logger.info("updated EloImporterModel");
        logger.info("filename of imported ELO: " + this.filename);
    }

    public String getFileEncoded() {
        return fileEncoded;
    }

    public void setFileEncoded(String fileEncoded) {
        this.fileEncoded = fileEncoded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
