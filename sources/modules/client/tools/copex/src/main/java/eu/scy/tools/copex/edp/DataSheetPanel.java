/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.utilities.ActionComment;
import eu.scy.tools.copex.utilities.ActionCopexButton;
import eu.scy.tools.copex.utilities.CopexButtonPanel;
import eu.scy.tools.copex.utilities.CopexPanelHideShow;
import eu.scy.tools.copex.utilities.MyTable;
import eu.scy.tools.copex.utilities.MyTableModel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * represente le panel de la feuille de resultats
 * @author MBO
 */
public class DataSheetPanel extends CopexPanelHideShow implements ActionCopexButton{

    // ATTRIBUTS
    
    /* bouton tableau */
    private CopexButtonPanel buttonTab;
    /* tableau de donnees */
    private DataSheet dataSheet;
    /* scrollPane */
    private JScrollPane scrollPane;
    /* tableau des donnees */
    private MyTable tableDataSheet;
    /* modele */
    private MyTableModel tableModel;
    /* boolean qui indique si montre le tableau ou non */
    private boolean showTable;
    /* Images */
    private ImageIcon imgDataSheet;
    private ImageIcon imgDataSheetClic;
    private ImageIcon imgDataSheetSelect ;
    private ImageIcon imgDataSheetDisabled;
    private ImageIcon imgExport;
    private ImageIcon imgExportClic;
    private ImageIcon imgExportSelect ;
    private ImageIcon imgExportDisabled;
    /* droit de modif datasheet */
    private boolean canEdit = true;
    /* bouton export */
    private CopexButtonPanel buttonExport;
            

    // CONSTRUCTEURS
    public DataSheetPanel(EdPPanel edP, JPanel owner) {
        super(edP, owner, edP.getBundleString("LABEL_DATASHEET"), false);
        init();
    }
    
    // METHODES
    private void init(){
        imgDataSheet = edP.getCopexImage("Bouton-mat_tableau.png");
        imgDataSheetSelect = edP.getCopexImage("Bouton-mat_tableau_survol.png");
        imgDataSheetClic = edP.getCopexImage("Bouton-mat_tableau_clic.png");
        imgDataSheetDisabled = edP.getCopexImage("Bouton-mat_tableau.png");
        imgExport = edP.getCopexImage("Bouton-AdT-28_xls.png");
        imgExportSelect = edP.getCopexImage("Bouton-AdT-28_xls_survol.png");
        imgExportClic = edP.getCopexImage("Bouton-AdT-28_xls_clic.png");
        imgExportDisabled = edP.getCopexImage("Bouton-AdT-28_xls_grise.png");
        setSize(owner.getWidth(), CopexPanelHideShow.HEIGHT_PANEL_HIDE);
        setPreferredSize(getSize());
        showTable = false;
    }
    
    public CopexButtonPanel getButtonTab(){
        if (buttonTab == null){
            buttonTab = new CopexButtonPanel(edP, 22,  
                    imgDataSheet.getImage(),
                    imgDataSheetSelect.getImage(),
                    imgDataSheetClic.getImage(),
                    imgDataSheetDisabled.getImage());
            buttonTab.addActionCopexButton(this);
           buttonTab.setBounds(this.getLabelEndX() + 20, 2, buttonTab.getWidth(), buttonTab.getHeight());
           buttonTab.setToolTipText(edP.getBundleString("TOOLTIPTEXT_DATASHEET"));
           if (edP.noProc())
               buttonTab.setVisible(false);
        }
        return buttonTab;
    }

    public CopexButtonPanel getButtonExport(){
        if (buttonExport == null){
            buttonExport = new CopexButtonPanel(edP, 28,
                    imgExport.getImage(),
                    imgExportSelect.getImage(),
                    imgExportClic.getImage(),
                    imgExportDisabled.getImage());
            buttonExport.addActionCopexButton(this);
           int x = this.getLabelEndX() + 20;
           if (buttonTab != null)
               x = this.buttonTab.getX()+buttonTab.getWidth()+5;
            buttonExport.setBounds(x, 2, buttonExport.getWidth(), 25);
           buttonExport.setToolTipText(edP.getBundleString("TOOLTIPTEXT_EXPORT"));
           if (edP.noProc())
               buttonExport.setVisible(false);
        }
        return buttonExport;
    }

    // OVERRIDE METHODS
    @Override
    protected void setPanelDetailsHide() {
        super.setPanelDetailsHide();
        getPanelTitle().remove(getButtonTab());
        this.remove(getScrollPane());
        scrollPane = null;
        tableDataSheet = null;
        this.buttonTab = null;
        this.showTable = false;
        edP.resizeHideShowPanel();
        //revalidate();
        //repaint();
    }

    @Override
    protected void setPanelDetailsShown() {
        super.setPanelDetailsShown();
        setDataSheet(this.dataSheet);
        if (canEdit){
               getPanelTitle().add(getButtonTab());
        }
        getPanelTitle().add(getButtonExport());
        edP.resizeHideShowPanel();
        this.showTable = true;
        //repaint();
    }

    @Override
    protected void resizePanelDetails(int newWidth, int newHeight) {
        super.resizePanelDetails(newWidth, newHeight);
        if (showTable && this.dataSheet != null){
            scrollPane.setSize(newWidth, newHeight - CopexPanelHideShow.HEIGHT_PANEL_HIDE);
            scrollPane.setPreferredSize(new Dimension(newWidth, newHeight - CopexPanelHideShow.HEIGHT_PANEL_HIDE));
            //revalidate();
            repaint();
        }
    }
    
    
    // METHODES
    public boolean isButtonTab(CopexButtonPanel button){
        return button == this.buttonTab;
    }
    public boolean isButtonExport(CopexButtonPanel button){
        return button == this.buttonExport;
    }
    
    // affichage tableau 
    public void setDataSheet(DataSheet dataSheet){
        if(this.buttonTab != null)
            this.buttonTab.setVisible(true);
        this.dataSheet = dataSheet;
        if (scrollPane != null){
            scrollPane.remove(tableDataSheet);
            getPanelDetails().remove(scrollPane);
        }
        scrollPane = null;
        tableDataSheet = null;
        if (dataSheet != null){
            getPanelDetails().add(getScrollPane());
        }
        revalidate();
        repaint();
    }
    /* scroll pane */
    private JScrollPane getScrollPane(){
        if (scrollPane == null){
            scrollPane = new JScrollPane(getTableDataSheet());
            scrollPane.setBounds(0, 0, owner.getWidth(), getPanelDetails().getHeight());
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrollPane;
    }
    /* construction de la table */
    private MyTable getTableDataSheet(){
        if (tableDataSheet == null){
            int nbR = 0;
            int nbC = 0;
            String[][] data = new String[nbR][nbC];
            if (this.dataSheet != null){
                nbR = this.dataSheet.getNbRows();
                nbC = this.dataSheet.getNbColumns();
                data = this.dataSheet.getStringData();
            }
            tableModel = new MyTableModel(edP, this,nbR, nbC, data);
            tableDataSheet = new MyTable(tableModel);
            tableModel.setTable(tableDataSheet);
            tableDataSheet.setBackground(Color.WHITE);
            tableDataSheet.setSize(tableDataSheet.getWidth(), tableDataSheet.getHeight());
            //tableDataSheet.setToolTipText(edP.getBundleString("TOOLTIPTEXT_TABLE_DATASHEET"));
        }
        return this.tableDataSheet;
    }
    
    /* mise a jour dataSheet */
    public void updateDataSheet(DataSheet ds){
        this.dataSheet = ds;
        setDataSheet(dataSheet);
    }
    
    /* mise a jour des droits du proc */
    public void setRight(boolean right){
        if (this.buttonTab == null && right){
            getPanelTitle().add(getButtonTab());
            getPanelTitle().add(getButtonExport());
            canEdit = true;
        }else if (this.buttonTab != null && !right){
            getPanelTitle().remove(buttonTab);
            getPanelTitle().remove(buttonExport);
            this.buttonTab = null;
            this.buttonExport = null;
            canEdit = false;
        }
        repaint();
    }

    /* retourne la position de la scroll */
    public int getScrollPosition(){
        return this.scrollPane.getHorizontalScrollBar().getValue() ;
    }

    /* scroll Vertical position */
    public void scrollToColumn(int noCol){
        int pos=0;
        for (int i=0; i<noCol; i++){
            pos += tableDataSheet.getColumnWidth(i);
        }
        JScrollBar hsb = this.scrollPane.getHorizontalScrollBar();
        this.scrollPane.getHorizontalScrollBar().setValues(pos, this.getWidth(), 0, hsb.getMaximum());
    }

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if (isButtonTab(button)){
            edP.openDialogDataSheet();
        }else if(isButtonExport(button)){
            edP.exportDataSheet();
        }
    }


   
}
