package eu.scy.tools.dataProcessTool.utilities;


import java.io.File;
import javax.swing.filechooser.FileFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class MyFileFilterCSV extends FileFilter {
    @Override
    public boolean accept(File f) {
        String s = f.getPath();
        int idP = s.lastIndexOf(".");
        if (idP == -1)
            return false;
        String ext = s.substring(idP+1, s.length());
        return ext.equals("csv");
    }

    @Override
    public String getDescription() {
        return "*.csv";
    }
}