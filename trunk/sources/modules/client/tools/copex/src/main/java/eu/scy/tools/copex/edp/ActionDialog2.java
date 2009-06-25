/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.utilities.ActionComment;
import eu.scy.tools.copex.utilities.CommentsPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.*;
import org.jdom.Element;

/**
 * dialog to add/edit an action
 * @author Marjolaine
 */
public class ActionDialog2 extends JDialog implements ActionComment, ActionTaskRepeat{
    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* mode de visualisation  : ajout / modification */
    private boolean modeAdd;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit du proc */
    private char procRight = MyConstants.EXECUTE_RIGHT;
    /* liste des actions nommees */
    private ArrayList<InitialNamedAction> listInitialNamedAction ;
    /* presence ou non d'actions libres */
    private boolean isFreeAction ;
    /* liste des granseurs physiques */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity  ;


    /*action*/
    /* description action */
    private String description;
    /* commentaires */
    private String comments;
    /* image eventuel */
    private ImageIcon taskImg;
    /* dessin*/
    private Element taskDraw;
    /* action nommee de l'action */
    private InitialNamedAction actionNamed;

    /* liste des libelles des noms pour la comboBox */
    private ArrayList<String> listAction ;
    /* affichage du nom */
    private boolean viewName;
     /* tableau des parametres de l'action */
    private ActionParam[] tabParam ;
    /* liste des material prod */
    private ArrayList<Material> materialsProd;
    /* liste des data prod */
    private ArrayList<QData> datasProd;
    /* liste des composants : à chaque indice du parametre on fait correspondre :
     * _ param de type Quantity : un text Field, une comboBox avec sa liste d'objets
     * _ param de type Material : une comboBox avec sa liste d'objets
     * _ param de type Data : un text field (ou une combo)
     */
    private ArrayList listComponent;
    /* liste des composant material produite */
    private ArrayList listMaterialProd;
    /* liste des composant data produite */
    private ArrayList listDataProd;
    /* texte de qq produit */
    private String textProd = "";
    /* commentaire en cours */
    private String comment;

    /* component */
    private int maxWidthLabel = 0;
    private int posx = 20;
    private int posy = 10;
    private int areaW = 300;
    private int areaH = 140;
    private int drawH = 200;
    private JButton buttonCancel;
    private JButton buttonOk;
    private JLabel labelName;
    private JComboBox cbActionName;
    private JLabel labelDescription;
    private JScrollPane scrollPaneDescription;
    private JTextArea textAreaDescription;
    private JPanel panelSetting;
    private CommentsPanel panelComments;
    private JLabel labelImage;
    private MyWhiteBoardPanel drawPanel;
    private TaskRepeatPanel taskRepeatPanel;
    private Dimension minDim = null;
    


    /* Constructeur de la fenetre d'ajout d'une action */
    public ActionDialog2(EdPPanel edP, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity) {
        super();
        this.edP = edP;
        this.modeAdd = true;
        this.isFreeAction = isFreeAction;
        this.listInitialNamedAction = listInitialNamedActions ;
        this.listPhysicalQuantity = listPhysicalQuantity ;
        this.setLocationRelativeTo(edP);
        this.actionNamed = null;
        this.taskDraw = null;
        setModal(true);
        setLocation(edP.getLocationDialog());
        initGUI();
    }

    /* constructeur de la fenetre d'edition de l'action */
    public ActionDialog2(EdPPanel edP, boolean modeAdd, String description, String comments, ImageIcon taskImage, Element taskDraw, InitialNamedAction actionNamed, char right, char procRight, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity, ActionParam[] tabParam, ArrayList<Material> materialsProd, ArrayList<QData> datasProd ) {
        super();
        this.edP = edP;
        this.modeAdd = modeAdd;
        this.description = description;
        this.comments = comments;
        this.taskImg = taskImage ;
        this.taskDraw = taskDraw ;
        this.actionNamed = actionNamed ;
        this.right = right;
        this.procRight = procRight;
        this.isFreeAction = isFreeAction ;
        this.listInitialNamedAction = listInitialNamedActions ;
        this.listPhysicalQuantity = listPhysicalQuantity ;
        this.tabParam = tabParam ;
        this.materialsProd = materialsProd ;
        this.datasProd = datasProd ;
        this.comment = "";
        this.setLocationRelativeTo(edP);
        setModal(true);
        setLocation(edP.getLocationDialog());
        initGUI();
    }

    /*initialisation fenetre */
    private void initGUI(){
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADDA"));
        setSize(300,300);
        setPreferredSize(getSize());
        if (!modeAdd)
            this.setTitle(edP.getBundleString("TITLE_DIALOG_ACTION"));
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeDialog();
            }
        });
        // liste des actions
        viewName = true;
        listAction = new ArrayList();
        int nb = 0;
        if (this.listInitialNamedAction != null)
            nb = this.listInitialNamedAction.size();
        if(nb == 0){
            viewName = false;
            setDescription();
        }else{
            initActionName();
            if (isFreeAction){
                listAction.add(edP.getBundleString("LABEL_FREE_ACTION"));
                this.cbActionName.addItem(edP.getBundleString("LABEL_FREE_ACTION"));
            }else{
                listAction.add("");
                this.cbActionName.addItem("");
            }
            for (int i=0; i<nb; i++){
                listAction.add(this.listInitialNamedAction.get(i).getLibelle());
                this.cbActionName.addItem(this.listInitialNamedAction.get(i).getLibelle());
            }
            this.cbActionName.setSelectedIndex(0);
        }
        // comments
        getContentPane().add(getPanelComments());
        //image associée ?
        if(taskImg != null){
            getContentPane().add(getLabelImage());
        }
        // dessin ?
        // repetition ?
        // boutton
        if (procRight == MyConstants.EXECUTE_RIGHT){
            getContentPane().add(getButtonOk());
            getContentPane().add(getButtonCancel());
        }else{
            getContentPane().add(getButtonCancelOk());
        }
        //initialisation action
        // action nommee ?
        if(!modeAdd){
            if( this.actionNamed == null && isFreeAction && viewName)
                this.cbActionName.setSelectedIndex(0);
            else if (this.actionNamed != null){
                int id = getIdActionNamed(this.actionNamed);
                if (id != -1){
                    if (isFreeAction)
                        id++;
                    else
                        id++;
                    this.cbActionName.setSelectedIndex(id);
                }else{
                    edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_INIT_ACTION_FRAME"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                    this.dispose();
                    return;
                }
            }
            if (this.cbActionName != null)
             this.cbActionName.setEnabled(false);
        }
        // enabled ?
        if (right == MyConstants.NONE_RIGHT)
            setDisabled();
        if (procRight == MyConstants.NONE_RIGHT)
            setAllDisabled();
        resizeActionDialog();
    }


    /* nom des actions */
    private void initActionName(){
        getContentPane().add(getLabelName());
        getContentPane().add(getCbName());
    }

    /* description */
    private void setDescription(){
        getContentPane().add(getLabelDescription());
        getContentPane().add(getScrollPaneDescription());
    }

    /* label name*/
    private JLabel getLabelName(){
        if (labelName == null){
            labelName = new JLabel(edP.getBundleString("LABEL_NAME")+" :");
            labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelName.setName("labelName");
            int w = CopexUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont()));
            labelName.setBounds(posx, posy,w, 14 );
        }
        return labelName;
    }

    /* cb name*/
    private JComboBox getCbName(){
        if (cbActionName == null){
            cbActionName = new JComboBox();
            cbActionName.setName("cbActionName");
            cbActionName.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectAction();
                }
            });
            cbActionName.setBounds(getLabelName().getX()+labelName.getWidth()+10, posy, 210, 20);
        }
        return cbActionName ;
    }

    /* labelDescription */
    private JLabel getLabelDescription(){
        if (labelDescription == null){
            labelDescription = new JLabel(edP.getBundleString("LABEL_DESCRIPTION"));
            labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelDescription.setName("labelDescription");
            int w = CopexUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont()));
            int y = posy;
            if (viewName)
                y = labelName.getY()+labelName.getHeight()+30;
            labelDescription.setBounds(posx, y,w, 14 );
        }
        return labelDescription;
    }

    /* scroll pane description */
    private JScrollPane getScrollPaneDescription(){
        if (scrollPaneDescription == null){
            scrollPaneDescription = new JScrollPane();
            scrollPaneDescription.setName("scrollPaneDescription");
            scrollPaneDescription.setViewportView(getTextAreaDescription());
            scrollPaneDescription.setBounds(posx, labelDescription.getY()+labelDescription.getHeight()+7, areaW, areaH);
        }
        return scrollPaneDescription ;
    }

    /* area description */
    private JTextArea getTextAreaDescription(){
        if (textAreaDescription == null){
            textAreaDescription = new JTextArea();
            textAreaDescription.setName("textAreaDescription");
            textAreaDescription.setColumns(20);
            textAreaDescription.setRows(3);
            textAreaDescription.setMinimumSize(new Dimension(20, 20));
            textAreaDescription.setLineWrap(true);
            textAreaDescription.setWrapStyleWord(true);
            if(!modeAdd)
                this.textAreaDescription.setText(this.description);
        }
        return textAreaDescription;
    }

    /* mode descrtiption */
    private boolean isDescription(){
        return this.labelDescription != null;
    }
    /* mode setting */
    private boolean isSetting(){
        return this.panelSetting != null;
    }
    /* panel setting */
    private JPanel getPanelSetting(){
        if(panelSetting == null){
            panelSetting = new JPanel();
            FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
            panelSetting.setLayout(layout);
            panelSetting.setName("panelSetting");
            int y = posy;
            if (viewName)
                y = labelName.getY()+labelName.getHeight()+30;
            panelSetting.setBounds(posx-10, y, areaW, areaH);
        }
        return panelSetting;
    }

    /* suppression panel setting */
   private void removePanelSetting(){
       if (panelSetting != null){
           panelSetting.removeAll();
            getContentPane().remove(this.panelSetting);
       }
       panelSetting = null;
       listComponent = new ArrayList();
   }

   /* suppression panel description */
   private void removePanelDescription(){
       if(labelDescription != null){
           getContentPane().remove(this.labelDescription);
           getContentPane().remove(this.scrollPaneDescription);
       }
       labelDescription = null;
       scrollPaneDescription = null;
       textAreaDescription = null;
   }


    /* comments*/
    private CommentsPanel getPanelComments(){
        if (panelComments == null){
            JPanel p = new JPanel();
            p.setSize(320,150);
            p.setPreferredSize(getSize());
            p.setName("pComments");
            panelComments = new CommentsPanel(edP, p, edP.getBundleString("LABEL_ADD_COMMENT"), areaW);
            panelComments.addActionComment(this);
            panelComments.setName("panelComments");
            if(!modeAdd){
                panelComments.setComments(this.comments);
                this.comment = this.comments ;
            }
            int y = posy;
            if (isDescription()){
                y = scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+20;
            }else if (isSetting()){
                y = panelSetting.getY()+panelSetting.getHeight()+20;
            }
            panelComments.setBounds(posx,y,areaW,panelComments.getHeight());
        }
        return panelComments ;
    }


    /* label image */
    private JLabel getLabelImage(){
        if(labelImage == null){
            labelImage = new JLabel();
            labelImage.setName("labelImage");
            if (this.taskImg != null ){
                this.labelImage.setIcon(this.taskImg);
            }
            labelImage.setBounds(posx, panelComments.getY()+panelComments.getHeight()+20, this.taskImg.getIconWidth(), this.taskImg.getIconHeight());
        }
        return labelImage ;
    }
    
    private void removeLabelImage(){
        if(labelImage != null){
            getContentPane().remove(this.labelImage);
            labelImage = null;
        }
    }

    /* panel dessin */
    private MyWhiteBoardPanel getWhiteboardPanel(){
        if(drawPanel == null){
            drawPanel = new MyWhiteBoardPanel();
            drawPanel.setName("whiteboardPanel");
            if(!modeAdd && taskDraw !=null ){
                //drawPanel.getWhiteBoardPanel().setStatus(taskDraw);
            }
            int y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            drawPanel.setBounds(posx,y,areaW,drawH);
        }
        return drawPanel;
    }

    /* panel task repeat */
    private TaskRepeatPanel getTaskRepeatPanel(ArrayList<InitialActionParam> listAllParam){
        if(taskRepeatPanel == null){
            taskRepeatPanel = new TaskRepeatPanel(edP, listAllParam, true);
            taskRepeatPanel.addActionTaskRepeat(this);
            taskRepeatPanel.setName("taskRepeatPanel");
            int y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight()+20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(posx, y, areaW, taskRepeatPanel.getHeight());
        }
        return taskRepeatPanel ;
    }

    private void removeWhiteboardPanel(){
        if(drawPanel != null){
            getContentPane().remove(drawPanel);
            drawPanel = null;
        }
    }

    private void removePanelTaskRepeat(){
        if(taskRepeatPanel != null){
            getContentPane().remove(taskRepeatPanel);
            taskRepeatPanel = null;
        }
    }

    /* bouton OK*/
    private JButton getButtonOk(){
        if(buttonOk == null){
            buttonOk = new JButton();
            buttonOk.setText(edP.getBundleString("BUTTON_OK"));
            buttonOk.setName("buttonOk");
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    validDialog();
                }
            });
            int w = 60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())) ;
            int y = panelComments.getY()+panelComments.getHeight()+30;
            if(taskRepeatPanel != null){
               y = taskRepeatPanel.getY()+taskRepeatPanel.getHeight()+30;
            }else if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonOk.setBounds(this.getWidth() / 8, y, w, 23);
        }
        return buttonOk ;
    }

     /* bouton Cancel*/
    private JButton getButtonCancel(){
        if(buttonCancel == null){
            buttonCancel = new JButton();
            buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
            buttonCancel.setName("buttonCancel");
            buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                     closeDialog();
                }
            });
            int w = 60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())) ;
            int y = panelComments.getY()+panelComments.getHeight()+30;
            if(taskRepeatPanel != null){
               y = taskRepeatPanel.getY()+taskRepeatPanel.getHeight()+30; 
            }else if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonCancel.setBounds(this.getWidth() - ((this.getWidth() / 8)+w), y, w, 23);
        }
        return buttonCancel ;
    }

     /* bouton Cancel ok*/
    private JButton getButtonCancelOk(){
        if(buttonCancel == null){
            buttonCancel = new JButton();
            buttonCancel.setText(edP.getBundleString("BUTTON_OK"));
            buttonCancel.setName("buttonCancel");
            buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                     closeDialog();
                }
            });
            int w = 60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())) ;
            int y = panelComments.getY()+panelComments.getHeight()+30;
            if(taskRepeatPanel != null){
               y = taskRepeatPanel.getY()+taskRepeatPanel.getHeight()+30;
            }else if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonCancel.setBounds((this.getWidth()- this.buttonCancel.getWidth()) /2, y, w, 23);
        }
        return buttonCancel ;
    }

    /* fermeture fenetre */
    private void closeDialog(){
        this.dispose();
    }


    /* permet de rendre disabled tous les elements, ne laisse qu'un bouton pour fermer
     * MBO le 09/10/08 : seul le champ description est en lecture seule, on peut modifier commentaires
     */
    private void setDisabled(){
        this.textAreaDescription.setEnabled(false);
        if (viewName)
            this.cbActionName.setEnabled(false);
        if(drawPanel != null)
            drawPanel.setEnabled(false);
    }
    private void setAllDisabled(){
        setDisabled();
        this.panelComments.setEnabled(false);
    }


    @Override
    public void actionComment() {
        resizeActionDialog();
    }

   /* sauvegarde des commentaires */
    @Override
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* met à jour le texte des commenraires */
    @Override
    public void setComment(){
        this.panelComments.setComments(this.comment);
    }

    /* resize fenetre => repositione les elements et calcule la nouvelle taille  */
    private void resizeActionDialog(){
        // repositionne comments
        int y = posy;
        if (isDescription()){
            y = scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+20;
        }else if (isSetting()){
            y = panelSetting.getY()+panelSetting.getHeight()+20;
        }
        if(panelComments == null)
            return;
        panelComments.setBounds(panelComments.getX(),y,panelComments.getWidth(),panelComments.getHeight());
        // respositionne image
        if (labelImage != null){
            labelImage.setBounds(labelImage.getX(), panelComments.getY()+panelComments.getHeight()+20, labelImage.getWidth(), labelImage.getHeight());
        }
        // repositionne dessin
        if(drawPanel != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            drawPanel.setBounds(drawPanel.getX(),y,drawPanel.getWidth(),drawPanel.getHeight());
        }
        // repositionne task repeat
        if(taskRepeatPanel != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight() +20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(taskRepeatPanel.getX(),y,taskRepeatPanel.getWidth(),taskRepeatPanel.getHeight());
        }
        // calcule de la nouvelle width de la fenetre
        int newWidth = 2*posx+areaW;
        if(taskImg != null){
            newWidth = Math.max(newWidth, taskImg.getIconWidth()+2*posx);
        }
        // repositionne les boutons
        y = panelComments.getY()+panelComments.getHeight()+30;
        if(taskRepeatPanel != null){
            y = taskRepeatPanel.getY() + taskRepeatPanel.getHeight()+30;
        }else if(drawPanel != null){
            y = drawPanel.getY()+drawPanel.getHeight()+30;
        }else if (labelImage != null){
            y = labelImage.getY()+labelImage.getHeight()+30;
        }
        if (buttonOk != null){
            buttonOk.setBounds(newWidth / 8, y, buttonOk.getWidth(), buttonOk.getHeight());
        }
        if (procRight == MyConstants.EXECUTE_RIGHT && buttonCancel != null){
            buttonCancel.setBounds(newWidth - ((newWidth / 8)+buttonCancel.getWidth()), y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }else if(buttonCancel != null){
            buttonCancel.setBounds((newWidth- this.buttonCancel.getWidth()) /2, y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }
        // fenetre
        int newHeight = buttonCancel.getY()+buttonCancel.getHeight()+40;
        minDim = new Dimension(newWidth, newHeight);
        setSize(minDim);
        setPreferredSize(minDim);
        repaint();
    }

    /* retourne l'indice d'une action nommee dans la liste */
    private int getIdActionNamed(InitialNamedAction action ){
        int nb = 0;
        if (this.listInitialNamedAction != null)
            nb = this.listInitialNamedAction.size();
        for (int i=0; i<nb; i++){
            if (this.listInitialNamedAction.get(i).getDbKey() == action.getDbKey())
                return i;
        }
        return -1;
    }


    /* action sur le OK */
   private void validDialog(){
       this.panelComments.setPanelDetailsShown();
       // recupere les données :
       InitialNamedAction a = null;
       ActionParam[] tabP = null;
        if (viewName){
            boolean ex = false;
            int id = this.cbActionName.getSelectedIndex() ;
            if (!isFreeAction && id == 0){
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_SELECT_ACTION"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }else if (isFreeAction && id == 0){
                ex = true;
            }else{
                a = this.listInitialNamedAction.get(id-1)  ;
            }
            /** if (!(id == 0 && isFreeAction)){
                a = isFreeAction ? this.listInitialNamedAction.get(id-1) : this.listInitialNamedAction.get(id);
            }*/if (ex){

            }else
            if(a != null && a.isSetting()){
                tabP = new ActionParam[a.getVariable().getNbParam()];
                int nbP = tabP.length ;
                for (int i=0; i<nbP; i++){
                    InitialActionParam initActionParam = a.getVariable().getTabParam()[i] ;
                    if(initActionParam instanceof InitialParamQuantity){
                        System.out.println("param de type quantite");
                        // param de type quantite
                        ArrayList param = (ArrayList)listComponent.get(i);
                        // en 0 le textField
                        // en 1 : un vecteur avec en 0 la comboBox et en 1 la lsite correspondante
                        JTextField tf = (JTextField)param.get(0);
                        ArrayList paramCB = (ArrayList)param.get(1);
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        ArrayList<CopexUnit> listUnit  = (ArrayList<CopexUnit>)paramCB.get(1);
                        int idU = cb.getSelectedIndex() ;
                        CopexUnit unit = listUnit.get(idU);

                        String s = tf.getText() ;
                        if (s == null || s.length() == 0){
                            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_ACTION_PARAM_VALUE"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                            return;
                        }
                        double value = 0;
                        try{
                            value = Double.parseDouble(s);
                        }catch(NumberFormatException e){
                            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_ACTION_PARAM_VALUE"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                            return;
                        }
                        Parameter p = new Parameter(-1, ((InitialParamQuantity)initActionParam).getQuantityName(), "", value, "", unit);
                        ActionParamQuantity paramQ = new ActionParamQuantity(-1, initActionParam, p) ;
                        tabP[i] = paramQ;

                    }else if(initActionParam instanceof InitialParamMaterial){
                        System.out.println("param de type material ");
                        // param de type material
                        ArrayList paramCB = (ArrayList)listComponent.get(i);
                        // en 0 la combo et en 1 la liste correspondate
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        ArrayList<Material> listMat = (ArrayList<Material>)paramCB.get(1);
                        int idM = cb.getSelectedIndex() ;
                        Material material = listMat.get(idM);
                        ActionParamMaterial paramM = new ActionParamMaterial(-1, initActionParam, material, null);
                        tabP[i] = paramM ;
                    }else if (initActionParam instanceof InitialParamData){
                        System.out.println("param de type data ");
                        // param de type data
                        ArrayList paramCB = (ArrayList)listComponent.get(i);
                        // en 0 la combo et en 1 la liste correspondate
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        ArrayList<QData> listData = (ArrayList<QData>)paramCB.get(1);
                        //int idM = cb.getSelectedIndex() ;
                        //QData data = listData.get(idM);
                        QData data = null;
                        String d = (String)cb.getSelectedItem();
                        for (int j=0; j<listData.size(); j++){
                            if (listData.get(j).getName().equals(d)){
                                data = listData.get(j);
                                break;
                            }
                        }
                        if (data == null){
                            data = new QData(-1, d, "",0, "", listPhysicalQuantity.get(0).getListUnit().get(0));
                        }
                        ActionParamData paramD = new ActionParamData(-1, initActionParam, data);
                        tabP[i] = paramD ;

                    }
                }
                // on rattache eventuellement les parametres quantite  aux parametres material
                for (int i=0; i<tabP.length; i++){
                    if (tabP[i] instanceof ActionParamMaterial){
                        if ((tabP[i].getInitialParam() instanceof InitialParamMaterial && ((InitialParamMaterial)(tabP[i].getInitialParam())).getParamQuantity() !=null  )){
                            // recherche le param quantite
                            long dbKeyIQ = ((InitialParamMaterial)(tabP[i].getInitialParam())).getParamQuantity().getDbKey() ;
                            ActionParamQuantity paramQ = null;
                            for (int j=0; j<tabP.length; j++){
                                if (tabP[j].getInitialParam() instanceof InitialParamQuantity && ((InitialParamQuantity)tabP[j].getInitialParam()).getDbKey() ==  dbKeyIQ){
                                    paramQ = (ActionParamQuantity)tabP[j] ;
                                }
                            }
                        ((ActionParamMaterial)tabP[i]).setQuantity(paramQ);
                        }
                    }
                }
            }
        }

        String d = "";
        if (this.labelDescription != null){
            d = this.textAreaDescription.getText();
            if (d.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_DESCRIPTION"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
            if (d.length() == 0 && a == null){
            String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_DESCRIPTION"));
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
            }
        }
        String c = this.panelComments.getComments();
        if (c.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_COMMENTS);
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }

        CopexAction newAction = new CopexAction(d, c) ;
        if (a != null){
            System.out.println("creation d'une action nommée");
            newAction = new CopexActionNamed(d, c, a) ;
            newAction.getTaskRight().setDrawRight(a.isDraw() ? MyConstants.EXECUTE_RIGHT : MyConstants.NONE_RIGHT);
            if (tabP != null){
                if (a instanceof InitialActionChoice ){
                    System.out.println("creation d'une action choice");
                    newAction = new CopexActionChoice(d, c, a, tabP) ;
                }else if (a instanceof InitialActionManipulation){
                    System.out.println("creation d'une action manipulation");
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionManipulation(d, c, a, tabP, getListMaterialProd(a, v));
                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }else if (a instanceof InitialActionTreatment){
                    System.out.println("creation d'une action treatment");
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionTreatment(d, c, a, tabP, getListDataProd(a, v));
                    System.out.println("a : "+a.getDbKey());
                    System.out.println("tabP : "+tabP.length);

                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }else if (a instanceof InitialActionAcquisition){
                    System.out.println("creation d'une action acquisition");
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionAcquisition(d, c, a, tabP, getListDataProd(a, v));
                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }
            }
        }
        if(drawPanel != null){
            // sauvegarde du dessin
           // newAction.setDraw(drawPanel.getWhiteBoardPanel().getStatus());
        }
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (modeAdd){
            // Créé l'action
            CopexReturn cr = edP.addAction(newAction);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr,  edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }else{
            // mode modification
            CopexReturn cr = edP.updateAction(newAction);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.closeDialog();
   }

    /* retourne la liste des data produit */
   private ArrayList<QData> getListDataProd(InitialNamedAction action, ArrayList v){
       ArrayList<QData> ld = new ArrayList();
        if (!(action instanceof InitialActionTreatment || action instanceof InitialActionAcquisition)){
           System.out.println("ceci n'est pas une action treatment ou acquisition !!");
            v.add(true);
            return ld;
       }
       for (int i=0; i<this.listDataProd.size(); i++){
           String s = ((JTextField)listDataProd.get(i)).getText();
           if (s.length() == 0){
               String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return ld;
           }
           if (s.length() > MyConstants.MAX_LENGHT_QUANTITY_NAME){
                String msg = edP.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_QUANTITY_NAME);
                edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return ld;
            }
           CopexUnit unit = null;
           if (action instanceof InitialActionAcquisition)
               unit =((InitialActionAcquisition)action).getUnitDataProd()[0] ;
           else if (action instanceof InitialActionTreatment)
               unit =((InitialActionTreatment)action).getUnitDataProd()[0] ;
           QData data = new QData(-1, s, "", 0, "", unit);
           ld.add(data);
       }
       v.add(true);
       return ld ;
   }


   /* retourne la liste du materiel produit */
   private ArrayList<Material> getListMaterialProd(InitialNamedAction action, ArrayList v){
       ArrayList<Material> lm = new ArrayList();
       if (!(action instanceof InitialActionManipulation)){
           System.out.println("Attention ce n'est pas une action manipulation !! ");
           v.add(true);
           return lm;
       }
       for (int i=0; i<this.listMaterialProd.size(); i++){
           //String s = ((JTextField)listMaterialProd.get(i)).getText();
           String s = (String)((JComboBox)listMaterialProd.get(i)).getSelectedItem() ;
           if (s == null || s.length() == 0){
               String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return lm;
           }
           if (s.length() > MyConstants.MAX_LENGHT_MATERIAL_NAME){
                String msg = edP.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_NAME);
                edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return lm;
            }

           // liste des types
           ArrayList<TypeMaterial> listType = ((InitialActionManipulation)action).getTypeMaterialProd();

           // liste des parametres
           ArrayList<Parameter> listParameters = new ArrayList();

           Material material = getMaterialWithName(s, listType, listParameters);
           if (material == null){
               System.out.println("le materiel n'a pas ete trouve!!");
               material = new Material(s, "", listType, listParameters, false);
               material.setDbKey(-1);
           }
           lm.add(material);
       }
       v.add(true);
       return lm ;
   }

   /* retourne le material correspondant à ce nom, null sinon */
   private Material getMaterialWithName(String name, ArrayList<TypeMaterial>listType, ArrayList<Parameter> listParameters){
       ArrayList<Material> listMaterialProdBefore = edP.getMaterialProd(modeAdd);
       int nbM = listMaterialProdBefore.size();
       for (int i=0; i<nbM; i++){
           if (listMaterialProdBefore.get(i).getName().equals(name)){
               return listMaterialProdBefore.get(i);
           }
       }
       return null;
   }

   /* selection d'une action */
   private void selectAction(){
       removePanelDescription();
       removePanelSetting();
       removeWhiteboardPanel();
       removeLabelImage(); 
       int id = cbActionName.getSelectedIndex() ;
       //if (id == 0 && isFreeAction){
       if (id == 0){
           if(isFreeAction){
               setDescription();
           }
       }else{
           if (isFreeAction)
               id = id-1;
           else
               id = id -1 ;
           InitialNamedAction a = listInitialNamedAction.get(id);
           if (a.isSetting()){
               setSetting(a);
           }else{
               setDescription();
           }
           setPanelDraw(a.isDraw());
           ArrayList<InitialActionParam> listAllParams = new ArrayList();
           if(a.getVariable() != null){
                for (int i=0; i<a.getVariable().getTabParam().length; i++){
                    listAllParams.add(a.getVariable().getTabParam()[i]);
                }
           }
           setPanelTaskRepeat(a.isRepeat(), listAllParams);
       }
       resizeActionDialog();
   }


   /* parametres de l'action nommee */
   private void setSetting(InitialNamedAction action){
       getPanelSetting() ;
       // mise en place des parametres
       InitialActionVariable variable = action.getVariable() ;
       if( variable == null){
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_ADD_ACTION"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
           return;
       }
       int nbParam = variable.getNbParam() ;
       listComponent = new ArrayList();
       listMaterialProd = new ArrayList();
       listDataProd = new ArrayList();
       panelSetting = new JPanel();
       FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
       panelSetting.setLayout(layout);

       for (int i=0; i<nbParam; i++){
           String t = variable.getTextLibelle(i);
           if (t.length() > 0){
                JLabel label = new JLabel(t);
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                maxWidthLabel = Math.max(maxWidthLabel, label.getWidth());
                panelSetting.add(label);
           }
           InitialActionParam param = variable.getTabParam()[i];
           if (param instanceof InitialParamQuantity){
               // text field
               JTextField tf = new JTextField("");
               tf.setSize(60, 20);
               tf.setPreferredSize(new Dimension(60,20));
               tf.setToolTipText(edP.getBundleString("TOOLTIPTEXT_ACTION_PARAM_VALUE"));
               panelSetting.add(tf);
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamQuantity){
                   tf.setText(""+((ActionParamQuantity)tabParam[i]).getParameter().getValue());
               }
               // combo box unites
               JComboBox cb = new JComboBox();
               ArrayList<CopexUnit> listU = ((InitialParamQuantity)param).getListUnit() ;
               for (int j=0; j<listU.size(); j++){
                   cb.addItem(listU.get(j).getSymbol());
               }
               if (!modeAdd && tabParam != null  && tabParam[i] != null && tabParam[i] instanceof ActionParamQuantity){
                   cb.setSelectedItem(((ActionParamQuantity)tabParam[i]).getParameter().getUnit().getSymbol());
               }
               panelSetting.add(cb);
               ArrayList compParam = new ArrayList();
               compParam.add(tf);
               ArrayList compCB = new ArrayList();
               compCB.add(cb);
               compCB.add(listU);
               compParam.add(compCB);
               listComponent.add(compParam);
           }else if (param instanceof InitialParamMaterial){
               JComboBox cb = new JComboBox();
               ArrayList<Material> materials = edP.getListMaterial(((InitialParamMaterial)param).getTypeMaterial(), ((InitialParamMaterial)param).getTypeMaterial2(), ((InitialParamMaterial)param).isAndTypes(), modeAdd);

               for (int j=0; j<materials.size(); j++){
                   cb.addItem(materials.get(j).getName());
               }
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamMaterial){
                   cb.setSelectedItem(((ActionParamMaterial)tabParam[i]).getMaterial().getName());
               }
               ArrayList compCB = new ArrayList();
               compCB.add(cb);
               compCB.add(materials);
               listComponent.add(compCB);
               panelSetting.add(cb);
           }else if (param instanceof InitialParamData){
               JComboBox cb = new JComboBox();
               cb.setEditable(true);
               ArrayList<QData> datas = edP.getDataProd( modeAdd);
               for (int j=0; j<datas.size(); j++){
                   cb.addItem(datas.get(j).getName());
               }
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamData){
                   cb.setSelectedItem(((ActionParamData)tabParam[i]).getData().getName());
               }
               ArrayList compCB = new ArrayList();
               compCB.add(cb);
               compCB.add(datas);
               listComponent.add(compCB);
               panelSetting.add(cb);
           }
       }
       String t = variable.getTextLibelle(-1);
       if (t.length() > 0){
             JLabel label = new JLabel(t);
             label.setFont(new java.awt.Font("Tahoma", 1, 11));
             label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
             maxWidthLabel = Math.max(maxWidthLabel, label.getWidth());
             panelSetting.add(label);
       }
       // material / data produces
       System.out.println("action produces : "+action);
       if (action instanceof InitialActionManipulation){
           System.out.println("action manipulation");
           int nbMat = ((InitialActionManipulation)action).getNbMaterialProd() ;
           System.out.println("nb Material prod : "+nbMat);
           for (int i=0; i<nbMat; i++){
                textProd  = ((InitialActionManipulation)action).getTextProd();
                JLabel label = new JLabel(textProd+" :") ;
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                panelSetting.add(label);
                /*JTextField tf = new JTextField() ;
                tf.setSize(80, 20);
                tf.setPreferredSize(new Dimension(80,20));
                panelSetting.add(tf);
                listMaterialProd.add(tf);*/
               JComboBox cb = new JComboBox();
                cb.setEditable(true);
                ArrayList<Material> listMat = edP.getMaterialProd(modeAdd);
                for (int k=0; k<listMat.size(); k++){
                    cb.addItem(listMat.get(k).getName());
                }
                panelSetting.add(cb);
                listMaterialProd.add(cb);
                if (!modeAdd ){
                  //tf.setText(this.materialsProd.get(i).getName());
                    cb.setSelectedItem(this.materialsProd.get(i).getName());
                }
           }
       }else if (action instanceof InitialActionAcquisition){
           System.out.println("action acquisition");
           int nbData = ((InitialActionAcquisition)action).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                textProd  = ((InitialActionAcquisition)action).getTextProd();
                JLabel label = new JLabel(textProd+" :") ;
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                panelSetting.add(label);
                JTextField tf = new JTextField() ;
                tf.setSize(80, 20);
                tf.setPreferredSize(new Dimension(80,20));
                panelSetting.add(tf);
                listDataProd.add(tf);
                if (!modeAdd ){
                   tf.setText(this.datasProd.get(i).getName());
                }
           }
       }else if (action instanceof InitialActionTreatment){
           System.out.println("action treatment");
           int nbData = ((InitialActionTreatment)action).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                textProd  = ((InitialActionTreatment)action).getTextProd();
                JLabel label = new JLabel(textProd+" :") ;
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                panelSetting.add(label);
                JTextField tf = new JTextField() ;
                tf.setSize(80, 20);
                tf.setPreferredSize(new Dimension(80,20));
                panelSetting.add(tf);
                listDataProd.add(tf);
                if (!modeAdd ){
                   tf.setText(this.datasProd.get(i).getName());
                }
           }
       }
       if((maxWidthLabel+20) > this.getWidth()){
            this.panelSetting.setSize(maxWidthLabel+10, this.panelSetting.getHeight());
       }else{
           this.panelSetting.setSize(325,125);
       }
       this.panelSetting.revalidate();
       int y = posy;
       if (viewName)
            y = labelName.getY()+labelName.getHeight()+30;
       panelSetting.setBounds(posx-10, y, panelSetting.getWidth(), panelSetting.getHeight());
       getContentPane().add(panelSetting);
       resizeDialog();
   }


    /* mise en place du panel de dessin */
    private void setPanelDraw(boolean visible){
        removeWhiteboardPanel();
        if (visible){
            getContentPane().add(getWhiteboardPanel());
        }
    }

    /* mise en place du panel task repeat */
    private void setPanelTaskRepeat(boolean visible, ArrayList<InitialActionParam> listAllParams){
        removePanelTaskRepeat();
        if(visible){
            getContentPane().add(getTaskRepeatPanel(listAllParams));
        }
    }

    /* resize de la fenetre */
    private void resizeDialog(){
        if(minDim == null || panelComments == null)
            return;
        int newWidth = this.getWidth() ;
        int newHeight = this.getHeight() ;
        if(newWidth < minDim.getWidth()){
            setSize((int)minDim.getWidth(), newHeight);
            return;
        }else if (newHeight < minDim.getHeight()){
            setSize(newWidth, (int)minDim.getHeight());
            return;
        }
        int diffH = (int)(newHeight - minDim.getHeight());
        boolean isDraw = drawPanel != null;
        int newAreaW = (int)(newWidth - (2*posx+10));
        // si il y a une partie dessin, c'est elle qu'on priviligie pour le redimensionement
        if(isDescription()){
            int newAreaH = areaH;
            if(!isDraw){
                newAreaH += diffH;
            }
            this.scrollPaneDescription.setBounds(scrollPaneDescription.getX(), scrollPaneDescription.getY(), newAreaW, newAreaH);
            this.scrollPaneDescription.revalidate();
        }else if (isSetting()){
            this.panelSetting.setBounds(panelSetting.getX(), panelSetting.getY(), newAreaW, panelSetting.getHeight());
            this.panelSetting.revalidate();
        }
        int y = posy;
        if(viewName){
            y = labelName.getHeight()+labelName.getY()+20;
        }
        if(isDescription()){
            y = this.scrollPaneDescription.getY()+this.scrollPaneDescription.getHeight()+20;
        }else if (isSetting()){
            y = this.panelSetting.getY() + this.panelSetting.getHeight() + 20;
        }
        int h = panelComments.getHeight() ;
        //if(!isDraw && !isDescription())
        //    h = 120+diffH ;
        panelComments.setBounds(panelComments.getX(), y, newAreaW, h);
        panelComments.revalidate();
        if(labelImage != null){
            labelImage.setBounds(labelImage.getX(), panelComments.getY()+panelComments.getHeight()+20, labelImage.getWidth(), labelImage.getHeight());
        }
        if(isDraw){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            h = drawH+diffH ;
            drawPanel.setBounds(drawPanel.getX(), y, newAreaW, h);
            drawPanel.revalidate();
        }
        if(taskRepeatPanel != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(drawPanel != null){
                y = drawPanel.getY()+drawPanel.getHeight()+20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(taskRepeatPanel.getX(), y, newAreaW, taskRepeatPanel.getHeight());
            taskRepeatPanel.revalidate();
        }
        // repositionne les boutons
        y = panelComments.getY()+panelComments.getHeight()+30;
        if(taskRepeatPanel != null){
            y = taskRepeatPanel.getY()+taskRepeatPanel.getHeight()+30;
        }else if(drawPanel != null){
            y = drawPanel.getY()+drawPanel.getHeight()+30;
        }else if (labelImage != null){
            y = labelImage.getY()+labelImage.getHeight()+30;
        }
        if (buttonOk != null){
            buttonOk.setBounds(newWidth / 8, y, buttonOk.getWidth(), buttonOk.getHeight());
        }
        if (procRight == MyConstants.EXECUTE_RIGHT && buttonCancel != null){
            buttonCancel.setBounds(newWidth - ((newWidth / 8)+buttonCancel.getWidth()), y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }else if(buttonCancel != null){
            buttonCancel.setBounds((newWidth- this.buttonCancel.getWidth()) /2, y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }
        repaint();
    }

    @Override
    public void actionResize() {
        resizeActionDialog();
    }



}
