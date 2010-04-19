/*
 * CSVFileFilter.java
 * Created on 25 mai 2007, 15:26
 */

package eu.scy.tools.fitex.GUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Cedric
 */
public class CSVFileFilter extends FileFilter {
       
    public String getDescription(){
        return "Fichiers CSV" ;
    }
    
   private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("csv"))
                return true;
            else
                return false;
        }
        return false;
    }
}

