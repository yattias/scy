/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 *
 * @author Marjolaine
 */
public class MyFileFilterXML extends FileFilter{
    @Override
    public boolean accept(File f) {
        String s = f.getPath();
        int idP = s.lastIndexOf(".");
        if (idP == -1)
            return false;
        String ext = s.substring(idP+1, s.length());
        return ext.equals("xml");
    }

    @Override
    public String getDescription() {
        return "*.xml";
    }
}