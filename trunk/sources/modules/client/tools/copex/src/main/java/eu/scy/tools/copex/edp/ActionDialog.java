/*
 * ActionDialog.java
 *
 * Created on 31 juillet 2008, 13:21
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

/**
 * fenêtre d'ajout ou de visualisation d'une action libre
 * @author  MBO
 */
public class ActionDialog extends JDialog implements ActionComment {

    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* mode de visualisation  : ajout / modification */
    private boolean modeAdd;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit du proc */
    private char procRight = MyConstants.EXECUTE_RIGHT;

    /* description action */
    private String description;
    /* commentaires */
    private String comments;
    /* dessin eventuel */
    private ImageIcon taskImg;

    /* action nommee de l'action */
    private InitialNamedAction actionNamed;
    /* liste des actions nommees */
    private ArrayList<InitialNamedAction> listInitialNamedAction ;
    /* presence ou non d'actions libres */
    private boolean isFreeAction ;
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

    /* panel des parametres */
    private JPanel panelSetting;
    /* liste des granseurs physiques */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity  ;
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
    /* longueur max des label */
    private int maxWidthLabel = 0;
    /* panelComments */
    private CommentsPanel panelComments;
    /* commentaire en cours */
    private String comment;
    /* draw Panel*/
    private MyWhiteBoardPanel drawPanel;


    // CONSTRUCTEURS
    /** Creates new form ActionDialog */
    public ActionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /* Constructeur de la fenetre d'ajout d'une action */
    public ActionDialog(EdPPanel edP, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity) {
        super();
        this.edP = edP;
        this.modeAdd = true;
        this.isFreeAction = isFreeAction;
        this.listInitialNamedAction = listInitialNamedActions ;
        this.listPhysicalQuantity = listPhysicalQuantity ;
        this.setLocationRelativeTo(edP);
        this.actionNamed = null;
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
    }
    
    /* constructeur de la fenetre d'edition de l'action */
    public ActionDialog(EdPPanel edP, boolean modeAdd, String description, String comments, ImageIcon taskImage, InitialNamedAction actionNamed, char right, char procRight, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity, ActionParam[] tabParam, ArrayList<Material> materialsProd, ArrayList<QData> datasProd ) {
        super();
        this.edP = edP;
        this.modeAdd = modeAdd;
        this.description = description;
        this.comments = comments;
        this.taskImg = taskImage ;
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
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
    }
    
    

    /* initialisation de la fenetre */
    private void init(){
        getContentPane().add(getPanelComments());
        //maj de la combo
        viewName = true;
        listAction = new ArrayList();
        int nb = 0;
        if (this.listInitialNamedAction != null)
            nb = this.listInitialNamedAction.size();
        if (isFreeAction){
            listAction.add(edP.getBundleString("LABEL_FREE_ACTION"));
            this.cbActionName.addItem(edP.getBundleString("LABEL_FREE_ACTION"));
        }else{
            listAction.add("");
            this.cbActionName.addItem("");
        }
        if (nb == 0){
            // on supprime la cb qui n'a pas lieu d'être
            viewName = false;
            remove(this.labelName);
            remove(this.cbActionName);
            this.labelName = null;
            this.cbActionName = null;
            setDescription();
        }else{
            for (int i=0; i<nb; i++){
                listAction.add(this.listInitialNamedAction.get(i).getLibelle());
                this.cbActionName.addItem(this.listInitialNamedAction.get(i).getLibelle());
            }
            this.cbActionName.setSelectedIndex(0);
        }
        if (!modeAdd){
            // mode edit
            this.setTitle(edP.getBundleString("TITLE_DIALOG_ACTION"));
            if(this.textAreaDescription != null)
                this.textAreaDescription.setText(this.description);
            this.panelComments.setComments(this.comments);
            this.comment = this.comments ;
            // image 
            if (this.taskImg != null ){
                this.labelImage.setIcon(this.taskImg);
            }
            // action nommee ?
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
            // gestion des droits 
            if (right == MyConstants.NONE_RIGHT)
               setDisabled();
            if (procRight == MyConstants.NONE_RIGHT)
                setAllDisabled();
        }
        if (labelDescription != null){
            textAreaDescription.setLineWrap(true);
            textAreaDescription.setWrapStyleWord(true);
        }
        resizeElements();
        actionComment();
    }

    private CommentsPanel getPanelComments(){
        JPanel p = new JPanel();
        p.setSize(320,150);
        p.setPreferredSize(getSize());
        panelComments = new CommentsPanel(edP, p, edP.getBundleString("LABEL_ADD_COMMENT"), 300);
        panelComments.addActionComment(this);
        panelComments.setBounds(20,230,300,panelComments.getHeight());
        return panelComments ;
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
    
    /* permet de rendre disabled tous les elements, ne laisse qu'un bouton pour fermer 
     * MBO le 09/10/08 : seul le champ description est en lecture seule, on peut modifier commentaires
     */
    private void setDisabled(){
        this.textAreaDescription.setEnabled(false);
        if (viewName)
            this.cbActionName.setEnabled(false);
    }
    private void setAllDisabled(){
        this.textAreaDescription.setEnabled(false);
        this.panelComments.setEnabled(false);
        if (viewName)
            this.cbActionName.setEnabled(false);
        remove(this.buttonOk);
        this.buttonOk = null;
        this.buttonCancel.setText(edP.getBundleString("BUTTON_OK"));
        this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
        this.buttonCancel.setBounds((this.getWidth()- this.buttonCancel.getWidth()) /2 , this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
    }
    
    /*
     * permet de resizer les elements de la fenetre en fonction de la longueur des textes
     * variable selon la langue
     */ 
   private void resizeElements(){
       setSize(350,350);
       // label description
       if (labelDescription != null)
        this.labelDescription .setSize(CopexUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont())), this.labelDescription.getHeight());
       // label nom
       if (viewName)
            this.labelName.setSize(CopexUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont())), this.labelName.getHeight());
       // label image
       if (this.taskImg != null )
        this.labelImage.setSize(this.taskImg.getIconWidth(), this.taskImg.getIconHeight());
       if (procRight != MyConstants.NONE_RIGHT){
        // bouton Ok
        this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
        // bouton Annuler
        this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       }
       if(!viewName){
           int h = 40;
           if (labelDescription != null){
            this.labelDescription.setBounds(this.labelDescription.getX(), this.labelDescription.getY() - h , this.labelDescription.getWidth(), this.labelDescription.getHeight());
            this.jScrollPaneDescription.setBounds(this.jScrollPaneDescription.getX(), jScrollPaneDescription.getY() - h, this.jScrollPaneDescription.getWidth(), jScrollPaneDescription.getHeight());
           }
           if(this.buttonOk != null)
               this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() -h, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() -h, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(this.getWidth(), this.getHeight() -h);
           this.setPreferredSize(this.getSize());
       }
       if (this.taskImg != null){
           int height = this.taskImg.getIconHeight() + 20; 
           int maxWidth = Math.max(this.getWidth(), this.taskImg.getIconWidth()+this.labelImage.getX());
           if(this.buttonOk != null)
               this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() +height, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() + height, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(maxWidth, this.getHeight() +height);
           this.setPreferredSize(this.getSize());
       }
       if (drawPanel != null){
           int height = drawPanel.getHeight() + 20;
           if(this.buttonOk != null)
               this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() +height, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() + height, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(getWidth(), this.getHeight() +height);
           this.setPreferredSize(this.getSize());
       }
      
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
    this.dispose();
   }

   /* selection d'une action */
   private void selectAction(){
       int id = cbActionName.getSelectedIndex() ;
       //if (id == 0 && isFreeAction){
       if (id == 0){
           if(isFreeAction)
               setDescription();
           else
               setDescriptionNull();
       }else{
           if (isFreeAction)
               id = id-1;
           else
               id = id -1 ;
           InitialNamedAction a = listInitialNamedAction.get(id);
           System.out.println("********");
           System.out.println("id : "+id);
           for (int k=0; k<listInitialNamedAction.size(); k++){
               System.out.println("list : "+listInitialNamedAction.get(k));
           }
           System.out.println("********");
           if (a.isSetting())
               setSetting(a);
           else
               setDescription();
           setPanelDraw(a.isDraw());
       }
   }

   /* */
   private void setDescriptionNull(){
       // enleve le panel setting
       removePanelSetting();
       if (labelDescription != null){
           getContentPane().remove(this.labelDescription);
           getContentPane().remove(this.jScrollPaneDescription);
           this.labelDescription = null;
       }
   }
   /* description*/
   private void setDescription(){
       // enleve le panel setting
       removePanelSetting();
       if (this.labelDescription == null){
        this.labelDescription = new JLabel(edP.getBundleString("LABEL_DESCRIPTION"));
        labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelDescription.setName("labelDescription"); // NOI18N
        getContentPane().add(labelDescription);
        labelDescription.setBounds(20, 50, 75, 14);
        this.labelDescription.setSize(CopexUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont())), this.labelDescription.getHeight());

        this.jScrollPaneDescription = new JScrollPane();
        this.textAreaDescription = new JTextArea();
        textAreaDescription.setColumns(20);
        textAreaDescription.setRows(3);
        textAreaDescription.setMinimumSize(new java.awt.Dimension(20, 20));
        textAreaDescription.setName("textAreaDescription");
        jScrollPaneDescription.setViewportView(textAreaDescription);
        getContentPane().add(jScrollPaneDescription);
        jScrollPaneDescription.setBounds(20, 70, 300, 140);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        if (!modeAdd)
            this.textAreaDescription.setText(this.description);
       }
       repaint();

   }

   /* parametres de l'action nommee */
   private void setSetting(InitialNamedAction action){
       // suppression description
       if (labelDescription != null){
            getContentPane().remove(this.labelDescription);
            getContentPane().remove(this.jScrollPaneDescription);
            this.labelDescription = null;
            this.jScrollPaneDescription = null;
            this.textAreaDescription = null;
       }else{
           removePanelSetting();
       }
       // mise en place des parametres
       InitialActionVariable variable = action.getVariable() ;
       if( variable == null){
           System.out.println("variable null !! "+action.getDbKey());
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

       panelSetting.setBounds(10,37,325,125);
       getContentPane().add(this.panelSetting);
       //pack();
       this.panelSetting.revalidate();
       if((maxWidthLabel+20) > this.getWidth()){
            this.panelSetting.setSize(maxWidthLabel+10, this.panelSetting.getHeight());
            this.setSize(maxWidthLabel+20, this.getHeight());
            this.setPreferredSize(this.getSize());
       }else{
           this.panelSetting.setSize(325,125);
           this.setSize(350,350);
           this.setPreferredSize(this.getSize());
       }
       repaint();

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


   /* suppression panel setting */
   private void removePanelSetting(){
       if (panelSetting != null){
        getContentPane().remove(this.panelSetting);
        panelSetting = null;
       }
       listComponent = new ArrayList();
   }

   public void actionComment() {
            int height = panelComments.getHeight()+panelComments.getY()+20 ;
            if(drawPanel != null){
                height = drawPanel.getHeight()+drawPanel.getY()+20;
                drawPanel.setBounds(drawPanel.getX(),panelComments.getHeight()+panelComments.getY()+20,drawPanel.getWidth(),drawPanel.getHeight());
            }
           if (this.buttonOk != null)
                this.buttonOk.setBounds(this.buttonOk.getX(), height, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), height, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           int h = this.buttonCancel.getY() + this.buttonCancel.getHeight() +50;
           this.setSize(this.getWidth(), h);
           this.setPreferredSize(this.getSize());
           this.repaint();
    }

   /* sauvegarde des commentaires */
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* met à jour le texte des commenraires */
    public void setComment(){
        this.panelComments.setComments(this.comment);
    }

    /* mise en place du panel de dessin */
    private void setPanelDraw(boolean visible){
        if(drawPanel != null){
            getContentPane().remove(drawPanel);
            drawPanel = null;
        }
        if (visible){
            drawPanel = new MyWhiteBoardPanel();
            drawPanel.setBounds(20,panelComments.getHeight()+panelComments.getY()+20,300,200);
            getContentPane().add(drawPanel);
        }
        resizeElements();
    }

   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelDescription = new javax.swing.JLabel();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelImage = new javax.swing.JLabel();
        jScrollPaneDescription = new javax.swing.JScrollPane();
        textAreaDescription = new javax.swing.JTextArea();
        labelName = new javax.swing.JLabel();
        cbActionName = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADDA"));
        setMinimumSize(new java.awt.Dimension(350, 300));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);

        labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelDescription.setText(edP.getBundleString("LABEL_DESCRIPTION"));
        labelDescription.setName("labelDescription"); // NOI18N
        getContentPane().add(labelDescription);
        labelDescription.setBounds(20, 50, 75, 14);

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(40, 280, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(190, 280, 99, 23);

        labelImage.setName("labelImage"); // NOI18N
        getContentPane().add(labelImage);
        labelImage.setBounds(20, 220, 30, 14);

        jScrollPaneDescription.setName("jScrollPaneDescription"); // NOI18N

        textAreaDescription.setColumns(20);
        textAreaDescription.setRows(3);
        textAreaDescription.setMinimumSize(new java.awt.Dimension(20, 20));
        textAreaDescription.setName("textAreaDescription"); // NOI18N
        jScrollPaneDescription.setViewportView(textAreaDescription);

        getContentPane().add(jScrollPaneDescription);
        jScrollPaneDescription.setBounds(20, 70, 300, 140);

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelName.setText(edP.getBundleString("LABEL_NAME")+" :");
        labelName.setName("labelName"); // NOI18N
        getContentPane().add(labelName);
        labelName.setBounds(20, 10, 90, 14);

        cbActionName.setName("cbActionName"); // NOI18N
        cbActionName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbActionNameItemStateChanged(evt);
            }
        });
        cbActionName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActionNameActionPerformed(evt);
            }
        });
        getContentPane().add(cbActionName);
        cbActionName.setBounds(100, 10, 210, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
   validDialog();
}//GEN-LAST:event_buttonOkActionPerformed

private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
    
}//GEN-LAST:event_formKeyReleased

private void cbActionNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbActionNameItemStateChanged
   
}//GEN-LAST:event_cbActionNameItemStateChanged

private void cbActionNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActionNameActionPerformed
    selectAction();
}//GEN-LAST:event_cbActionNameActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ActionDialog dialog = new ActionDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JComboBox cbActionName;
    private javax.swing.JScrollPane jScrollPaneDescription;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelName;
    private javax.swing.JTextArea textAreaDescription;
    // End of variables declaration//GEN-END:variables

   

}
