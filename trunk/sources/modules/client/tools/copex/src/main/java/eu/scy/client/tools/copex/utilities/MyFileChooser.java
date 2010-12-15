/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.edp.EdPPanel;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * open a JFileChooser
 * sinon probleme de focus lors de l'ouverture pour la premiere fois
 * @author Marjolaine
 */
public class MyFileChooser extends JDialog{
    /*edP */
    private EdPPanel edP;
    /* path */
    private String path;
    /*fenetre de dialog */
    private JFileChooser fc;

    public MyFileChooser(EdPPanel edP, String path) {
        super();
        this.edP = edP;
        this.path = path;
        setSize(10, 10);
        setPreferredSize(getSize());
        setLocation(edP.getLocationDialog());
        setModal(true);
    }

    public int  showDialog(){
        // ouverture fenetre de selection fichier
        fc = new JFileChooser(path);
        fc.setFileFilter(new ObjFilter());
        int r= fc.showSaveDialog(this);
        this.setVisible(false);
        this.dispose();
        return r;
    }

    public File getSelectedFile(){
        return fc.getSelectedFile();
    }

}
