/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.MaterialStrategy;
import eu.scy.tools.copex.common.MaterialUsed;
import eu.scy.tools.copex.utilities.ActionMaterial;
import eu.scy.tools.copex.utilities.ActionMaterialDetail;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MaterialDetailPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * list of materials used int the procedure
 * depends on the strategy of the material
 * @author Marjolaine
 */
public class ListMaterialPanel extends JPanel implements ActionMaterialDetail{
    private EdPPanel edP;
    private ActionMaterial actionMaterial;
    private MaterialStrategy materialStrategy;
    private List<MaterialUsed> listMaterialUsed;
    private ArrayList<MaterialDetailPanel> listPanel;

    private int panelWidth;


    public ListMaterialPanel(EdPPanel edP, MaterialStrategy materialStrategy, List<MaterialUsed> listMaterialUsed, int panelWidth) {
        super();
        this.edP = edP;
        this.materialStrategy = materialStrategy;
        this.listMaterialUsed = listMaterialUsed;
        this.listPanel = new ArrayList();
        this.panelWidth = panelWidth;
        initGUI();
    }

    private void initGUI(){
        setLayout(null);
        int nb = listMaterialUsed.size();
        int height = 0;
        for (int i=0; i<nb; i++){
            int h = setMaterial(listMaterialUsed.get(i), i);
            height += h;
        }
        setSize(panelWidth, height);
        setPreferredSize(getSize());
    }

    /**
    * Instancie l'objet ActionMaterial.
    * @param action ActionMaterial
    */
    public void addActionMaterial(ActionMaterial action){
        this.actionMaterial=action;
    }

    private int setMaterial(MaterialUsed mUsed, int id){
        MaterialDetailPanel m = new MaterialDetailPanel(edP, this, mUsed, materialStrategy);
        int h = m.getHeight();
        int y = id*h;
        m.setBounds(0,y,MaterialDialog.panelWidth, h);
        m.addActionMaterialDetail(this);
        listPanel.add(m);
        this.add(m);
        return h;
    }

    @Override
    public void actionResize() {
        int nb = listPanel.size();
        int h = 0;
        for (int i=0; i<nb; i++){
            int y = h;
            listPanel.get(i).setBounds(listPanel.get(i).getX(), y, listPanel.get(i).getWidth(), listPanel.get(i).getHeight());
            h+= listPanel.get(i).getHeight();
        }
        setSize(MaterialDialog.panelWidth, h);
        setPreferredSize(getSize());
    }

    @Override
    public void actionRemoveMaterial(MaterialUsed mUsed) {
        int id = listMaterialUsed.indexOf(mUsed);
        if(id == -1){
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_REMOVE_MATERIAL"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.remove(listPanel.get(id));
        this.listMaterialUsed.remove(id);
        this.listPanel.remove(id);
        if(actionMaterial != null)
            actionMaterial.actionRemoveMaterial(mUsed);
        actionResize();
        revalidate();
        repaint();
    }

    public ArrayList<MaterialUsed> getListMaterialUsed(){
        ArrayList<MaterialUsed> list = new ArrayList();
        int nb = listPanel.size();
        for (int i=0; i<nb; i++){
            MaterialUsed mUsed = listPanel.get(i).getMaterialUsed();
            list.add(mUsed);
        }
        return list;
    }

    public void saveData(MaterialUsed mUsed){
        actionMaterial.saveData(mUsed);
    }
 
}
