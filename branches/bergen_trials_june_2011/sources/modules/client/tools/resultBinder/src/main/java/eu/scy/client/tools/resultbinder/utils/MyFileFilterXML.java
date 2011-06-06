/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.resultbinder.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * file filter for xml files
 * @author Marjolaine
 */

public class MyFileFilterXML extends FileFilter{
    /* accepts the xml files only*/
    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        String s = f.getPath();
        int idP = s.lastIndexOf(".");
        if (idP == -1)
            return false;
        String ext = s.substring(idP+1, s.length());
        return ext.equals("xml");
    }

    /* xml files*/
    @Override
    public String getDescription() {
        return "*.xml";
    }
}
