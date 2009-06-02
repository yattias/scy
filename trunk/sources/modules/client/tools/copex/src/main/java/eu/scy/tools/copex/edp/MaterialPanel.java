/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;


import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.MaterialUseForProc;
import eu.scy.tools.copex.utilities.ActionCopexButton;
import eu.scy.tools.copex.utilities.CopexButtonPanel;
import eu.scy.tools.copex.utilities.CopexPanelHideShow;
import eu.scy.tools.copex.utilities.ListPanel;
import javax.swing.*;
import java.util.ArrayList;

/**
 * panel contenant le matériel diponible dans la mission en cours
 * @author MBO
 */
public class MaterialPanel extends CopexPanelHideShow implements ActionCopexButton {

    // ATTRIBUTS
    private JLayeredPane panelOwner;
    private CopexButtonPanel buttonSortType;
    private CopexButtonPanel buttonLearner;
    /* liste du materiel */
    private ListPanel myListMat = null;
    private ArrayList<Material> listMaterial;
    private ArrayList<MaterialUseForProc> listJustification;
    private char procRight;
    private JScrollPane scrollPane = null;
    private boolean showMaterial;
    /* detail d'un materiel */
    private DetailMaterialPanel detailMaterialPanel = null;
    /* mode d'affichage */
    private boolean showOnlyAdvise = false;
    private boolean sortByType = false;
    /* thread qui permet l'affichage du detail */
    DisplayMaterialThread threadDisplay;
    /* Images */
    private ImageIcon imgSortType;
    private ImageIcon imgSortTypeSelec ;
    private ImageIcon imgSortTypeClic;
    private ImageIcon imgSortTypeDisabled;
    private ImageIcon imgSortTypeSort;
    private ImageIcon imgLearner;
    private ImageIcon imgLearnerSelec ;
    private ImageIcon imgLearnerClic;
    private ImageIcon imgLearnerDisabled;
    private ImageIcon imgLearnerFilter;
    private boolean isLearnerVisible;
            
           
    // CONSTRUCTEURS
    public MaterialPanel(EdPPanel edP, JPanel owner, JLayeredPane panelOwner, ArrayList<Material> listMaterial, ArrayList<MaterialUseForProc> listJustification, char procRight) {
        super(edP, owner, edP.getBundleString("LABEL_MATERIAL"), false);
        this.listMaterial = listMaterial;
        this.listJustification = listJustification;
        this.showMaterial = false;
        this.panelOwner = panelOwner;
        this.procRight = procRight;
        initPanel();
    }
    
    private void initPanel(){
        imgSortType = edP.getCopexImage("Bouton-mat_tritype.png");
        imgSortTypeSelec = edP.getCopexImage("Bouton-mat_tritype_survol.png") ;
        imgSortTypeClic = edP.getCopexImage("Bouton-mat_tritype_clic.png") ;
        imgSortTypeDisabled = edP.getCopexImage("Bouton-mat_tritype.png") ;
        imgSortTypeSort = edP.getCopexImage("Bouton-mat_tritype_selec.png");
        imgLearner = edP.getCopexImage("Bouton-mat_triens.png");
        imgLearnerSelec = edP.getCopexImage("Bouton-mat_triens_survol.png");
        imgLearnerClic = edP.getCopexImage("Bouton-mat_triens_clic.png");
        imgLearnerDisabled = edP.getCopexImage("Bouton-mat_triens.png");
        imgLearnerFilter = edP.getCopexImage("Bouton-mat_triens_selec.png");
        setSize(owner.getWidth(), CopexPanelHideShow.HEIGHT_PANEL_HIDE);
        isLearnerVisible = isAllMaterialSameAdvised();
    }
   
    
    public CopexButtonPanel getButtonSortType(){
        if (buttonSortType == null){
            buttonSortType = new CopexButtonPanel(edP, 25,  
                    imgSortType.getImage(),
                    imgSortTypeSelec.getImage(),
                    imgSortTypeClic.getImage(),
                    imgSortTypeDisabled.getImage());
            buttonSortType.addActionCopexButton(this);
           buttonSortType.setBounds(this.getLabelEndX() + 20, 5, buttonSortType.getWidth(), buttonSortType.getHeight());
           buttonSortType.setToolTipText(edP.getBundleString("TOOLTIPTEXT_SORT_TYPE"));
        }
        return buttonSortType;
    }
    public CopexButtonPanel getButtonLearner(){
        if (buttonLearner == null){
           buttonLearner = new CopexButtonPanel(edP, 25,  
                    imgLearner.getImage(),
                    imgLearnerSelec.getImage(),
                    imgLearnerClic.getImage(),
                    imgLearnerDisabled.getImage());
           buttonLearner.addActionCopexButton(this);
           buttonLearner.setBounds(buttonSortType.getX()+ buttonSortType.getWidth(), 5, buttonLearner.getWidth(), buttonLearner.getHeight());
           buttonLearner.setToolTipText(edP.getBundleString("TOOLTIPTEX_FILTER_MATERIAL_ADVISE"));
          buttonLearner.setVisible(!isLearnerVisible);
        }
        
        return buttonLearner;
    }
    
    /* renvoit true si tout le materiel est conseille par l'enseignant ou non conseille */
    private boolean isAllMaterialSameAdvised(){
        int nbMat = this.listMaterial.size();
        if (nbMat == 0)
            return true;
        Material firstM = listMaterial.get(0);
        boolean isAdvisedFM = firstM.isAdvisedLearner();
        for (int i=1; i<nbMat; i++){
            if (listMaterial.get(i).isAdvisedLearner() != isAdvisedFM){
                return false;
            }
        }
        return true;
    }
   
    @Override
    protected void setPanelDetailsHide() {
        super.setPanelDetailsHide();
        getPanelTitle().remove(getButtonSortType());
        getPanelTitle().remove(getButtonLearner());
        this.buttonLearner = null;
        this.buttonSortType = null;
        showMaterial = false;
        edP.resizeHideShowPanel();
       // revalidate();
        //repaint();
    }

    @Override
    protected void setPanelDetailsShown() {
        super.setPanelDetailsShown();
        getPanelTitle().add(getButtonSortType());
        getPanelTitle().add(getButtonLearner());
        getPanelDetails().add(getScrollPane());
        showMaterial = true;
        
        edP.resizeHideShowPanel();
       // revalidate();
        //repaint();
    }
    
    /* scroll pane */
    private JScrollPane getScrollPane(){
        if (scrollPane == null){
            scrollPane = new JScrollPane(getListMaterial());
            scrollPane.setBounds(0, 0, owner.getWidth(), getPanelDetails().getHeight());
        }
        return scrollPane;
    }
    /* liste du materiel */
    private ListPanel getListMaterial(){
        if (myListMat == null){
            myListMat = new ListPanel(edP, this, this.listMaterial, this.listJustification, procRight);
            //myListMat.setSize(owner.getWidth(), 150);
        }
        return myListMat;
    }
    
    
   
    
    /* retourne vrai s'il s'agit du bouton pour trier par type */
    public boolean isButtonSortByType(CopexButtonPanel button){
        return this.buttonSortType == button;
    }
    /* retourne vrai s'il s'agit du bouton pour afficher le materiel conseille */
    public boolean isButtonDisplayAdvise(CopexButtonPanel button){
        return this.buttonLearner == button;
    }
   
    
    /* tri par type */
    public void sortByType(){
        sortByType = !sortByType;
        this.myListMat.sortByType(showOnlyAdvise, sortByType);
        updateSortIcon();
        
    }
    private void updateSortIcon(){
        if (sortByType)
            getButtonSortType().setIcon(imgSortTypeSort.getImage());
        else
            getButtonSortType().setIcon(imgSortType.getImage());
    }
    private void updateLearnerIcon(){
        if (showOnlyAdvise)
            getButtonLearner().setIcon(imgLearnerFilter.getImage());
        else
            getButtonLearner().setIcon(imgLearner.getImage());
    }
    
    /* affichage enseignant */
    public void displayAdvise(){
        showOnlyAdvise = !showOnlyAdvise;
        this.myListMat.displayMaterialAdviseByTeacher(showOnlyAdvise, sortByType);
        updateLearnerIcon();
    }
    /* montre la fenetre de detail materiel */
    public void showDetailMaterial(Material m){
     /*   if (threadDisplay != null && threadDisplay.isAlive())
            return;
        int posD = 5;
        int posH = 40;
       // pas de place pour affichage => bloque
       if (this.getWidth() < DetailMaterialPanel.DETAIL_MATERIAL_WIDTH + posD ||this.getHeight() < DetailMaterialPanel.DETAIL_MATERIAL_HEIGHT + posH)
           return;
      //if (detailMaterialPanel == null){
           detailMaterialPanel = new DetailMaterialPanel(m, this, edP);
           detailMaterialPanel.setBounds(posD, posH, detailMaterialPanel.getWidth(), detailMaterialPanel.getHeight());
           this.panelOwner.add(detailMaterialPanel, 1);
           threadDisplay = new DisplayMaterialThread(panelOwner, detailMaterialPanel);
           threadDisplay.start();
          
      // }
       revalidate();
       repaint();*/
    }
    
    /* ferme la fenetre de detail materiel */
    public void hideDetailMaterial(){
       /* if (threadDisplay != null)
            threadDisplay.interrupt();
        if (detailMaterialPanel != null)
            this.panelOwner.remove(detailMaterialPanel);
        this.panelOwner.moveToFront(this);
        detailMaterialPanel = null;
        getButtonLearner().repaint();
        revalidate();
        repaint();*/
    }

    @Override
    protected void resizePanelDetails(int newWidth, int newHeight) {
       super.resizePanelDetails(newWidth, newHeight);
       if (showMaterial && myListMat != null){
           //myListMat.setSize(owner.getWidth(), getPanelDetails().getHeight());
           myListMat.resizeList(newWidth-10);
           //scrollPane.setSize(newWidth, getPanelDetails().getHeight());
           if(scrollPane != null){
                scrollPane.setSize(newWidth, newHeight - CopexPanelHideShow.HEIGHT_PANEL_HIDE);
                scrollPane.setPreferredSize(scrollPane.getSize());
                scrollPane.revalidate() ;
           }
           revalidate();
           repaint();
       }
    }

    /* mise à jour de la liste du materiel */
    public void setListMaterial(char procRight, ArrayList<Material> listMaterial, ArrayList<MaterialUseForProc> listJustification){
        this.procRight = procRight;
        this.listMaterial = listMaterial;
        this.listJustification = listJustification;
        isLearnerVisible = isAllMaterialSameAdvised();
        if (showMaterial){
            myListMat.removeAll();
            this.scrollPane.remove(myListMat);
            myListMat = null;
            getPanelDetails().remove(this.scrollPane);
            scrollPane = null;
            this.buttonLearner.setVisible(!isLearnerVisible);
            getPanelDetails().add(getScrollPane());
        }else{
            myListMat = null;
            scrollPane = null;
            getPanelDetails().add(getScrollPane());
        }
        revalidate();
        repaint();
    }
    
    /* suppression du materiel utilise  */
    public void removeMaterialUse(Material m){
        int id = getIdMaterialUse(m.getDbKey());
        if (id != -1){
            listJustification.remove(id);
        }
        myListMat.removeMaterialUse(m);
    }
    
    /* ajout du materiel utilise  */
    public void addMaterialUse(MaterialUseForProc m){
       listJustification.add(m);
        myListMat.addMaterialUse(m);
    }
    
    /* modifcation  du materiel utilise  */
    public void updateMaterialUse(MaterialUseForProc m){
        int id = getIdMaterialUse(m.getMaterial().getDbKey());
        if (id != -1){
            listJustification.set(id, m);
        }
        myListMat.updateMaterialUse(m);
    }
    
    /* recherche indice material utilise */
    private int getIdMaterialUse(long dbKey){
        int n= listJustification.size();
        for (int i=0; i<n; i++){
            if (listJustification.get(i).getMaterial().getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /* met bouton fleche dispo ou non */
    @Override
    public void setButtonEnabled(boolean enabled){
        super.setButtonEnabled(enabled);
    }

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if (isButtonSortByType(button)){
            sortByType();
        }else if (isButtonDisplayAdvise(button)){
            displayAdvise();
        }
    }
    
    
}
