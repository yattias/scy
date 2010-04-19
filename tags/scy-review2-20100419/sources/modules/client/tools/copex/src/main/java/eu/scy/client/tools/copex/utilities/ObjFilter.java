/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Marjolaine
 */
public class ObjFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
        String s = f.getPath();
        int idP = s.indexOf(".");
        if (idP == -1)
            return false;
        String ext = s.substring(idP+1, s.length());
        return ext.equals("xls");
    }

    @Override
    public String getDescription() {
        return "*.xls";
    }


}
