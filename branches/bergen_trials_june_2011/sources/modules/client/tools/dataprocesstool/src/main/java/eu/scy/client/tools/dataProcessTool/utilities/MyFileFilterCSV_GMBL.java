/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author Marjolaine
 */
public class MyFileFilterCSV_GMBL extends FileFilter  {
    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        return MyUtilities.isCSVFile(f) || MyUtilities.isGMBLFile(f);
//        String s = f.getPath();
//        int idP = s.lastIndexOf(".");
//        if (idP == -1)
//            return false;
//        String ext = s.substring(idP+1, s.length());
//        return ext.equals("csv") || ext.equals("gmbl");
    }

    @Override
    public String getDescription() {
        return "*.csv; *.gmbl; *.qmbl; *.cmbl";
    }
}
