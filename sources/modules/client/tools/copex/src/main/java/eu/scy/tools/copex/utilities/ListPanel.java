/*
 * ListPanel.java
 *
 * Created on 8 août 2008, 16:12
 */

package eu.scy.tools.copex.utilities;


import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.MaterialUseForProc;
import eu.scy.tools.copex.common.TypeMaterial;
import eu.scy.tools.copex.edp.DetailMaterialPanel;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.MaterialPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;


/**
 * permet l'affichage du materiel comme une liste
 * ce n'est pas une liste car il n'y a pas de selection
 * la liste du materiel est par defaut trié par nom (tri dans la requete)
 * @author  MBO
 */
public class ListPanel extends JPanel{

    //CONSTANTE
    /* taille min */
    private final static int MIN_WIDTH = 140;
    // ATTRIBUTS
    /* edP */
    private EdPPanel edP;
    /* panel owner */
    private MaterialPanel owner;
    /* liste du materiel */
    private ArrayList<Material> listMaterial;
    /* liste des justifications */
    private ArrayList<MaterialUseForProc> listJustification;
    private int nbMat;
    /*icones */
    private Icon iconMaterial;
    private Icon iconAdvise;
    /* panel detail */
    private DetailMaterialPanel detailMaterialPanel;
    /* liste du materiel destine à être trié */
    private Vector materialToBeSorted;
    /* droit du proc*/
    private char procRight;
    /* liste des labesl affiches */
    private ArrayList<MaterialLabel> listLabels;

    // CONSTRUCTEURS 
    public ListPanel(EdPPanel edP, MaterialPanel owner, ArrayList<Material> listMaterial, ArrayList<MaterialUseForProc> listJustification, char procRight) {
        super();
        this.edP = edP;
        this.owner = owner;
        this.listMaterial = listMaterial;
        this.listJustification = listJustification;
        this.nbMat = listMaterial.size();
        this.procRight = procRight;
        this.iconAdvise = edP.getCopexImage("Puce-mat_enseignant.png");
        this.iconMaterial = edP.getCopexImage("Puce-mat_ronde.png");
        this.listLabels = new ArrayList();
        initComponents();
        init();
    }
    
    /** Creates new form ListPanel */
    public ListPanel() {
        initComponents();
    }

    /* initialisation  */
    private void init(){
        int maxH = 0;
        setLayout(null);
        for (int i=0; i<nbMat; i++){
            Material m = listMaterial.get(i);
            int h = display(m,  i);
            maxH += h;
        }
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(MIN_WIDTH,0));
        setPreferredSize(new Dimension(owner.getWidth(), maxH));
        setSize(new Dimension(Math.max(MIN_WIDTH,owner.getWidth()), maxH));
    }
    
    /* affichage d'un materiel */
    private int display(Material m, int index){
        boolean isUsed = false;
        String justification = "";
        int nb = this.listJustification.size();
        for (int i=0; i<nb; i++){
            if (listJustification.get(i).getMaterial().getDbKey() == m.getDbKey()){
                justification = listJustification.get(i).getJustification() == null ? "" : listJustification.get(i).getJustification();
                isUsed = true;
            }
        }
        int pos = 0;
        int nbM = listLabels.size();
        for (int i=0; i<nbM; i++){
            pos += listLabels.get(i).getHeight() ;
        }
        pos += 3;
        MaterialLabel ml = new MaterialLabel(edP, this, m, justification,isUsed,  pos, procRight, owner.getWidth());
        add(ml);
        listLabels.add(ml);
        return ml.getHeight();
    }
    
    private void display(Vector listM){
        this.listLabels = new ArrayList();
        int nbM = listM.size();
        for (int i=0; i<nbM; i++){
            Material m = (Material)listM.elementAt(i);
            display(m,  i);
        }
    }
    private void display(ArrayList<Material> listM){
        this.listLabels = new ArrayList();
        int nbM = listM.size();
        for (int i=0; i<nbM; i++){
            Material m = (Material)listM.get(i);
            display(m, i);
        }
    }
    
    
    // METHODES 
    /* retourne l'icone material */
    public Icon getIconMaterial(){
        return this.iconMaterial;
    }
    /* retourne l'icone materiel conseille par l'enseignant */
    public Icon getIconAdvise(){
        return this.iconAdvise;
    }
    /* affiche le detail materiel  */
    public void showMaterial(Material m){
        owner.showDetailMaterial(m);
    }
    /* cache le detail material */
    public void hideMaterial(){
        owner.hideDetailMaterial();
    }
    
    /* tri par type */
    public void sortByType(boolean displayAdvise, boolean sortByType){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        removeAll();
        display(getMaterialSort(displayAdvise, sortByType));
        revalidate();
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    
    /* n'affiche que les materiels conseilles par l'enseignant */
    public void displayMaterialAdviseByTeacher(boolean display, boolean sortByType){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        removeAll();
        display(getMaterialSort(display, sortByType));
        revalidate();
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /* retourne la liste du materiel conseille par l'enseignant */
    private ArrayList<Material> getListAdvise(){
        ArrayList<Material> listMAdvise = new ArrayList();
        for (int i=0; i<nbMat; i++){
            Material m = listMaterial.get(i);
            if (m.isAdvisedLearner())
                listMAdvise.add(m);
        }
        return listMAdvise;
    }
   
    /* tri par type */
    private Vector sortListByType(ArrayList<Material> listM) {
         materialToBeSorted = getMaterialToBeSort(1, listM);
        Vector tabToSort = getElementToSort(1, listM);
        if (tabToSort == null)
            return null;
        executeQuickSort(0, tabToSort.size()-1, materialToBeSorted, tabToSort);
        return materialToBeSorted;
    }
    
    
    
    /* tri par nom */
    private Vector sortListByName(ArrayList<Material> listM) {
        materialToBeSorted = getMaterialToBeSort(0, listM);
        Vector tabToSort = getElementToSort(0, listM);
        if (tabToSort == null)
            return null;
        executeQuickSort(0, tabToSort.size()-1, materialToBeSorted, tabToSort);
        return materialToBeSorted;
    }
    
   
    /*retourne le materiel trie */
    private Vector getMaterialSort(boolean advise, boolean sortByType){
        ArrayList<Material> listM = listMaterial;
        if (advise)
            listM = getListAdvise();
        if (sortByType)
            return sortListByType(listM);
        else
            return sortListByName(listM);
    }
    
   
    
    /* retourne la liste du materiel à trier */
    private Vector getMaterialToBeSort(int mode, ArrayList listM){
        // pour l'instant retourne la liste complete 
        Vector listMat = new Vector();
        for (int k=0; k<listM.size(); k++){
            Material m = (Material)listM.get(k);
            ArrayList<TypeMaterial> listType = m.getListType();
            int nbT = listType.size();
            if (mode == 1 && nbT > 0){
                // on cree autant de materiel que de type
                for (int i=0; i<nbT; i++){
                    Material mi = (Material)m.clone();
                    ArrayList<TypeMaterial> l = new ArrayList();
                    l.add(listType.get(i));
                    mi.setListType(l);
                    listMat.add(mi);
                }
            }else
                listMat.add(listM.get(k));
        }
        return listMat;
    }
    
    
    
    /* retourne la liste des elements à trier */
    private Vector getElementToSort(int mode, ArrayList<Material> listM){
        Vector elementToSort = new Vector();
        for (int k=0; k<listM.size(); k++){
            Material m = listM.get(k);
            if (mode == 0)
                elementToSort.add(m.getName());
            else{
                ArrayList<TypeMaterial> listT = m.getListType();
                for (int i=0; i<listT.size(); i++){
                    String type = listT.get(i).getType();
                    elementToSort.add(type);
                }
            }
               
        }
        return elementToSort;
    }
    
    /* suppression du materiel utilise  */
    public void removeMaterialUse(Material m){
        int id = getIdMaterialUse(m.getDbKey());
        if (id != -1){
            listJustification.remove(id);
        }
        MaterialLabel ml = getMaterialLabel(m);
        if (ml != null)
            ml.removeMaterialUse();
    }
    
    /* modification du materiel utilise  */
    public void updateMaterialUse(MaterialUseForProc m){
        int id = getIdMaterialUse(m.getMaterial().getDbKey());
        if (id != -1){
            listJustification.set(id, m);
        }
        MaterialLabel ml = getMaterialLabel(m.getMaterial());
        if (ml != null)
            ml.updateMaterialUse(m.getJustification());
    }
    
    /* ajout du materiel utilise  */
    public void addMaterialUse(MaterialUseForProc m){
        listJustification.add(m);
        MaterialLabel ml = getMaterialLabel(m.getMaterial());
        if (ml != null)
            ml.addMaterialUse(m.getJustification());
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
    
    /* recherche du label correspondant */
    private MaterialLabel getMaterialLabel(Material m){
        int n = listLabels.size();
        for (int k=0; k<n; k++){
            if (listLabels.get(k).getMaterial().getDbKey() == m.getDbKey())
                return listLabels.get(k) ;
        }
        return null;
    }

    /* resize des composants de la liste */
    public void resizeList(int width){
        int nb = this.listLabels.size();
        int pos = 0;
        for (int i=0; i<nb; i++){
            MaterialLabel ml = this.listLabels.get(i);
            int nh = ml.resizeWidth(width);
            ml.setBounds(ml.getX(),pos , ml.getWidth(), ml.getHeight());
            pos += nh;
        }
        setSize(Math.max(width, MIN_WIDTH), pos);
        setPreferredSize(getSize());
    }
    
    /* execution du tri 
     * algorithme de tri "quickSort", il echange aussi l'ordre d'un vecteur
    * @param g int premier indice
    * @param d int deuxieme indice
    * @param exchange java.util.Vector vecteur a echanger
    * @param keys Vector vecteur a trier
     */
    private void executeQuickSort(int g, int d,Vector exchange, Vector keys) {
	int i,j, idT=-1;
 	String v = " ",t=" ";
 	while (g < d) {
	 	v = (String)keys.elementAt(d);
	  	i= g-1;
	  	j = d;
		for (;;) {
			while (((String)keys.elementAt(++i)).compareTo(v) < 0 )
				if (i == d)
					break;
			while (((String)keys.elementAt(--j)).compareTo(v) > 0 )
				if (j == g)
					break;
			if (i >= j)
				break;
		  idT=i;
		  /* On échange */
		  t = ((String)keys.elementAt(i));
		  keys.setElementAt(keys.elementAt(j),i);

		  Object w = exchange.elementAt(j);
		  exchange.setElementAt(exchange.elementAt(i),j);
		  exchange.setElementAt(w,i);
		  keys.setElementAt(t,j);		
	 	}
		
		/* On échange */
		t = ((String)keys.elementAt(i));
		keys.setElementAt(keys.elementAt(d),i);		
	 	Object w =  exchange.elementAt(d);
		exchange.setElementAt(exchange.elementAt(i),d);
		exchange.setElementAt(w,i);
		keys.setElementAt(t,d);			
  		 
 		executeQuickSort (g, i-1,exchange, keys);
		g = i+1;
	}
}
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(5, 1, 5, 0));
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
