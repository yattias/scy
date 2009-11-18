/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import java.io.File;
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

    private String fileSize;
    private String fileFormat;

    public EloImporterModel(){
        this(null,"","","","");
    }

    public EloImporterModel(File file, String filename, String description, String title, String fileEncoded) {
        this.file = file;
        this.filename = filename;
        this.description = description;
        this.title = title;
        this.fileEncoded=fileEncoded;
        logger.info("created EloImporterModel");
//        String[] filenameSplitted = filename.split(".");
//        this.fileFormat = filenameSplitted[filenameSplitted.length-1];
    }

    public void updateModel(File file, String filename, String description, String title, String fileEncoded) {
        this.file = file;
        this.filename = filename;
        this.description = description;
        this.title = title;
        this.fileEncoded=fileEncoded;
//        String[] filenameSplitted = filename.split(".");
//        this.fileFormat = filenameSplitted[filenameSplitted.length-1];
        logger.info("updated EloImporterModel");
        logger.info("filename of imported ELO: "+this.filename);
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
