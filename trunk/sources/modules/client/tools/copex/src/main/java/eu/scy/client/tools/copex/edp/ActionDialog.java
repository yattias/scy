/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.utilities.ActionComment;
import eu.scy.client.tools.copex.utilities.ActionMenuEvent;
import eu.scy.client.tools.copex.utilities.CommentsPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import eu.scy.client.tools.copex.utilities.MyMenuItem;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.jdom.Element;

/**
 * dialog to add/edit an action
 * @author Marjolaine
 */
public class ActionDialog extends JDialog implements ActionComment, ActionTaskRepeat, ActionMenuEvent{
    /* owner */
    private EdPPanel edP;
    /* visualization mode : add or modification  */
    private boolean modeAdd;
    /* user right */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* proc right */
    private char procRight = MyConstants.EXECUTE_RIGHT;
    /* action named list */
    private ArrayList<InitialNamedAction> listInitialNamedAction ;
    /* free action or not  */
    private boolean isFreeAction ;
    /* physical quantities list  */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity  ;

    /* selected action */
    private InitialNamedAction actionNamedSel = null;
    private char insertIn;

    /*action*/
    /* description action */
    private String description;
    /* comments */
    private String comments;
    /* possibly image */
    private ImageIcon taskImg;
    /* drawing*/
    private Element taskDraw;
    /* initial action named for the action */
    private InitialNamedAction actionNamed;
    /* action can be repeat? */
    private boolean isActionRepeat;
    /* repeat task */
    private TaskRepeat taskRepeat;
    /* init proc draw */
    private boolean isInitProcDraw;

    /* list of text -names for the combo box  */
    private ArrayList<String> listAction ;
    /* show name */
    private boolean viewName;
     /* array parameters of the action */
    private Object[] tabParam ;
    /* material prod list  */
    private ArrayList<Object> materialsProd;
    /* data prod list */
    private ArrayList<Object> datasProd;
    /* components list; to each index of the list we have:
     * _ param of type Quantity : a text Field, a comboBox with the objects list
     * _ param of type Material : a comboBox with the objects list
     * _ param of type Data : a text field (or a  combo)
     */
    private ArrayList listComponent;
    /* list of components material prod */
    private ArrayList listMaterialProd;
    /* list of componennts data prod. */
    private ArrayList listDataProd;
    /* text prod. */
    private String textProd = "";
    /* current comments */
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
    private JScrollPane scrollPaneDraw;
    private TaskRepeatPanel taskRepeatPanel;
    private Dimension minDim = null;
    private JPanel panelButtonsWhiteboard = null;
    private MyMenuItem menuItemCopy = null;
    private MyMenuItem menuItemPaste = null;
    private MyMenuItem menuitemCut = null;

    
    private static final Logger logger = Logger.getLogger(ActionDialog.class.getName());

    /* Constructor : add an action */
    public ActionDialog(EdPPanel edP, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity, boolean isActionRepeat, char insertIn, boolean isInitProcDraw) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.modeAdd = true;
        this.isFreeAction = isFreeAction;
        this.listInitialNamedAction = listInitialNamedActions ;
        this.listPhysicalQuantity = listPhysicalQuantity ;
        this.setLocationRelativeTo(edP);
        this.actionNamed = null;
        this.taskDraw = null;
        this.taskRepeat = null;
        this.isActionRepeat = isActionRepeat ;
        this.insertIn = insertIn;
        this.isInitProcDraw = isInitProcDraw;
        setModal(true);
        setLocation(edP.getLocationDialog());
        initGUI();
        setIconImage(edP.getIconDialog());
    }

    /* Constructor: edit an action */
    public ActionDialog(EdPPanel edP, boolean modeAdd, String description, String comments, ImageIcon taskImage, Element taskDraw, InitialNamedAction actionNamed, char right, char procRight, boolean isFreeAction, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<PhysicalQuantity> listPhysicalQuantity, Object[] tabParam, ArrayList<Object> materialsProd, ArrayList<Object> datasProd, boolean isActionRepeat, TaskRepeat tr, boolean isInitProcDraw ) {
        super(edP.getOwnerFrame());
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
        this.insertIn = MyConstants.INSERT_TASK_UNDEF;
        this.listInitialNamedAction = listInitialNamedActions ;
        this.listPhysicalQuantity = listPhysicalQuantity ;
        this.tabParam = tabParam ;
        this.materialsProd = materialsProd ;
        this.isInitProcDraw = isInitProcDraw;
        this.datasProd = datasProd ;
        this.comment = "";
        this.isActionRepeat = isActionRepeat ;
        this.taskRepeat = tr ;
        this.setLocationRelativeTo(edP);
        setModal(true);
        setLocation(edP.getLocationDialog());
        setIconImage(edP.getIconDialog());
        initGUI();
    }

    /* initialization  */
    private void initGUI(){
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADDA"));
        setSize(300,300);
        setPreferredSize(getSize());
        if (!modeAdd)
            this.setTitle(edP.getBundleString("TITLE_DIALOG_ACTION"));
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeDialog();
            }
        });
        // actions list
        viewName = true;
        listAction = new ArrayList();
        int nb = 0;
        if (this.listInitialNamedAction != null)
            nb = this.listInitialNamedAction.size();
        boolean setTaskRepeat = false;
        if(nb == 0){
            viewName = false;
            setDescription();
            setTaskRepeat = isActionRepeat ;
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
                listAction.add(this.listInitialNamedAction.get(i).getLibelle(edP.getLocale()));
                this.cbActionName.addItem(this.listInitialNamedAction.get(i).getLibelle(edP.getLocale()));
            }
            this.cbActionName.setSelectedIndex(0);
            int max = getMaxWidth(listAction, cbActionName.getFontMetrics(cbActionName.getFont()));
            cbActionName.setSize(max, cbActionName.getHeight());
        }
        // comments
        getContentPane().add(getPanelComments());
        //image ?
        if(taskImg != null){
            getContentPane().add(getLabelImage());
        }
        // drawing?
        if(nb == 0 && isInitProcDraw)
            setPanelDraw(true);
        // repeat
        if(setTaskRepeat)
            setPanelTaskRepeat(true, new ArrayList(), new ArrayList());
        // button
        if (procRight == MyConstants.EXECUTE_RIGHT){
            getContentPane().add(getButtonOk());
            getContentPane().add(getButtonCancel());
        }else{
            getContentPane().add(getButtonCancelOk());
        }
        //initialization action
        // action named?
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
        }else{
            if(cbActionName != null)
                cbActionName.setSelectedIndex(0);
        }
        // enabled ?
        if (right == MyConstants.NONE_RIGHT)
            setDisabled();
        if (procRight == MyConstants.NONE_RIGHT)
            setAllDisabled();
        resizeActionDialog();
    }

    private int getMaxWidth(ArrayList<String> list, FontMetrics fm){
         int max = 0;
         for(Iterator<String> s = list.iterator();s.hasNext();){
             max = Math.max(max,CopexUtilities.lenghtOfString(s.next(), fm));
         }
         return max+40;
     }

    /* name of actions */
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
            labelName = new JLabel(edP.getBundleString("LABEL_NAME"));
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
            textAreaDescription.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
                        validDialog();
                    }
                }
            });
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

    /* remove panel setting */
   private void removePanelSetting(){
       if (panelSetting != null){
           panelSetting.removeAll();
            getContentPane().remove(this.panelSetting);
       }
       panelSetting = null;
       listComponent = new ArrayList();
   }

   /* remove panel description */
   private void removePanelDescription(){
       if(labelDescription != null){
           getContentPane().remove(this.labelDescription);
           getContentPane().remove(this.scrollPaneDescription);
       }
       labelDescription = null;
       scrollPaneDescription = null;
       textAreaDescription = null;
   }

    /* remove panel comment */
   private void removePanelComment(){
       if(panelComments != null){
           getContentPane().remove(panelComments);
       }
       panelComments = null;
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

    /* panel drawing */
    private MyWhiteBoardPanel getWhiteboardPanel(){
        if(drawPanel == null){
            drawPanel = new MyWhiteBoardPanel();
            drawPanel.setName("whiteboardPanel");
            if(!modeAdd && taskDraw !=null ){
                drawPanel.getWhiteBoardPanel().setStatus(taskDraw);
                drawPanel.setSize(drawPanel.getWhiteBoardPanel().getWidth(), drawPanel.getWhiteBoardPanel().getHeight());
                drawPanel.setPreferredSize(new Dimension((int)drawPanel.getWhiteBoardPanel().getEnclosingScreenRectangle().getWidth(), (int)drawPanel.getWhiteBoardPanel().getEnclosingScreenRectangle().getHeight()+40));
            }
//            int y = panelComments.getHeight()+panelComments.getY()+20 ;
//            if(labelImage != null){
//                y = labelImage.getY()+labelImage.getHeight()+20;
//            }
//            drawPanel.setBounds(posx,y,areaW,drawH);
        }
        return drawPanel;
    }

    private JScrollPane getScrollPaneDraw(){
        if(scrollPaneDraw == null){
            scrollPaneDraw = new JScrollPane();
            scrollPaneDraw.setName("scrollPaneDraw");
            scrollPaneDraw.setViewportView(getWhiteboardPanel());
//            int y = panelComments.getHeight()+panelComments.getY()+20 ;
//            if(labelImage != null){
//                y = labelImage.getY()+labelImage.getHeight()+20;
//            }
            int y = panelButtonsWhiteboard.getY()+panelButtonsWhiteboard.getHeight()+5;
            scrollPaneDraw.setBounds(posx,y,areaW,drawH);
        }
        return scrollPaneDraw;
    }

    /* panel task repeat */
    private TaskRepeatPanel getTaskRepeatPanel(ArrayList<InitialActionParam> listAllParam, ArrayList<InitialOutput> listOutput){
        if(taskRepeatPanel == null){
            taskRepeatPanel = new TaskRepeatPanel(edP, listAllParam, null,listOutput,null, true, modeAdd);
            taskRepeatPanel.addActionTaskRepeat(this);
            taskRepeatPanel.setName("taskRepeatPanel");
            
            int y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(posx, y, areaW, taskRepeatPanel.getHeight());
            if(!modeAdd && this.taskRepeat != null){
                taskRepeatPanel.setSize(290, taskRepeatPanel.getHeight());
                taskRepeatPanel.setTaskRepeat(taskRepeat);
            }
        }
        return taskRepeatPanel ;
    }

    private void removeWhiteboardPanel(){
        if(drawPanel != null){
            getContentPane().remove(scrollPaneDraw);
            getContentPane().remove(panelButtonsWhiteboard);
            drawPanel = null;
            scrollPaneDraw = null;
            panelButtonsWhiteboard = null;
            menuItemCopy = null;
            menuItemPaste = null;
            menuitemCut = null;
        }
        setResizable(false);
    }

    private void removePanelTaskRepeat(){
        if(taskRepeatPanel != null){
            getContentPane().remove(taskRepeatPanel);
            taskRepeatPanel = null;
        }
    }

    /* buttton OK*/
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
            }else if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonOk.setBounds(this.getWidth() / 8, y, w, 23);
        }
        return buttonOk ;
    }

     /* button Cancel*/
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
            }else if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonCancel.setBounds(this.getWidth() - ((this.getWidth() / 8)+w), y, w, 23);
        }
        return buttonCancel ;
    }

     /* button Cancel ok*/
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
            }else if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+30;
            }else if (labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+30;
            }
            buttonCancel.setBounds((this.getWidth()- this.buttonCancel.getWidth()) /2, y, w, 23);
        }
        return buttonCancel ;
    }

    /* close dialog */
    private void closeDialog(){
        this.dispose();
    }


    /* allows to set disabled all elements , just a button to close
     * MBO 09/10/08 : only the description field is on read-only, we can add some comments 
     */
    private void setDisabled(){
        this.textAreaDescription.setEnabled(false);
        if (viewName)
            this.cbActionName.setEnabled(false);
        if(drawPanel != null)
            drawPanel.setEnabled(false);
        if(this.taskRepeatPanel != null)
            taskRepeatPanel.setDisabled();
    }
    private void setAllDisabled(){
        setDisabled();
        this.panelComments.setEnabled(false);
    }


    @Override
    public void actionComment() {
        resizeActionDialog();
    }

   /* save comments */
    @Override
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* update comments text  */
    @Override
    public void setComment(){
        this.panelComments.setComments(this.comment);
    }

    /* resize frame => position again all element and calculate new size */
    private void resizeActionDialog(){
        // comments
        int y = posy;
        if (isDescription()){
            y = scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+20;
        }else if (isSetting()){
            y = panelSetting.getY()+panelSetting.getHeight()+20;
        }
        if(panelComments != null)
            panelComments.setBounds(panelComments.getX(),y,panelComments.getWidth(),panelComments.getHeight());
        //  image
        if (labelImage != null){
            labelImage.setBounds(labelImage.getX(), panelComments.getY()+panelComments.getHeight()+20, labelImage.getWidth(), labelImage.getHeight());
        }
        // drawing
        if(scrollPaneDraw != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            panelButtonsWhiteboard.setBounds(panelButtonsWhiteboard.getX(),y,panelButtonsWhiteboard.getWidth(),panelButtonsWhiteboard.getHeight());
            //scrollPaneDraw.setBounds(scrollPaneDraw.getX(),y,scrollPaneDraw.getWidth(),scrollPaneDraw.getHeight());
            scrollPaneDraw.setBounds(scrollPaneDraw.getX(),5+panelButtonsWhiteboard.getY()+panelButtonsWhiteboard.getHeight(),scrollPaneDraw.getWidth(),scrollPaneDraw.getHeight());
        }
        // task repeat
        if(taskRepeatPanel != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight() +20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(taskRepeatPanel.getX(),y,taskRepeatPanel.getWidth(),taskRepeatPanel.getHeight());
        }
        // calculate new width of the window
        int newWidth = 2*posx+areaW;
        if(cbActionName != null){
            newWidth = Math.max(newWidth, cbActionName.getWidth()+ cbActionName.getX()+posx);
        }
        if (panelSetting != null)
                newWidth = Math.max(newWidth, panelSetting.getWidth()+2*posx);
        if(taskImg != null){
            newWidth = Math.max(newWidth, taskImg.getIconWidth()+2*posx);
        }
        // buttons
        if(cbActionName != null)
            y = cbActionName.getY()+cbActionName.getHeight()+20;
        if(panelComments != null)
            y = panelComments.getY()+panelComments.getHeight()+30;
        if(taskRepeatPanel != null){
            y = taskRepeatPanel.getY() + taskRepeatPanel.getHeight()+30;
        }else if(scrollPaneDraw != null){
            y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+30;
        }else if (labelImage != null){
            y = labelImage.getY()+labelImage.getHeight()+30;
        }
        if(panelSetting != null)
            panelSetting.setSize(newWidth -(2*posx), panelSetting.getHeight());
        if(panelComments != null)
            panelComments.setSize(newWidth -(2*posx), panelComments.getHeight());
        if (buttonOk != null){
            buttonOk.setBounds(newWidth / 8, y, buttonOk.getWidth(), buttonOk.getHeight());
        }
        if (procRight == MyConstants.EXECUTE_RIGHT && buttonCancel != null){
            buttonCancel.setBounds(newWidth - ((newWidth / 8)+buttonCancel.getWidth()), y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }else if(buttonCancel != null){
            buttonCancel.setBounds((newWidth- this.buttonCancel.getWidth()) /2, y, buttonCancel.getWidth(), buttonCancel.getHeight());
        }
        // frame
        int newHeight = getHeight();
        if (buttonCancel != null)
                newHeight = buttonCancel.getY()+buttonCancel.getHeight()+40;
        minDim = new Dimension(newWidth, newHeight);
        setSize(minDim);
        setPreferredSize(minDim);
        repaint();
    }

    /* returns the index of  a action named in the list  */
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


    /*ok action */
   private void validDialog(){
       if(panelComments != null)
        this.panelComments.setPanelDetailsShown();
       // gets data
       InitialNamedAction a = null;
       Object[] tabP = null;
       // gets repeat
       int nbTaskRepeat = 1;
        if(taskRepeatPanel != null){
            nbTaskRepeat = taskRepeatPanel.getNbRepeat();
        }
       if(nbTaskRepeat > 1){
           ArrayList v2 = new ArrayList();
           CopexReturn cr=  taskRepeatPanel.getTaskRepeat(v2);
           if(cr.isError()){
               edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
               return;
           }
           taskRepeat = (TaskRepeat)v2.get(0);

        }else
            taskRepeat = null;

        // rgets param
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
            }*/
            if (ex){

            }else
            if(a != null && a.isSetting()){
                tabP = new Object[a.getVariable().getNbParam()];
                int nbP = tabP.length ;
                for (int i=0; i<nbP; i++){
                    InitialActionParam initActionParam = a.getVariable().getTabParam()[i] ;
                    ArrayList<ActionParam> l = null;
                    if(taskRepeat != null)
                        l = taskRepeat.getListActionParam(initActionParam);
                    if(l == null || l.isEmpty()){
                    if(initActionParam instanceof InitialParamQuantity){
                        // quantity param
                        ArrayList param = (ArrayList)listComponent.get(i);
                        // in 0  textField
                        // in 1 : a vector with  in 0 the comboBox and in 1 the list (corresponding)
                        JTextField tf = (JTextField)param.get(0);
                        ArrayList paramCB = (ArrayList)param.get(1);
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        List<CopexUnit> listUnit  = (List<CopexUnit>)paramCB.get(1);
                        int idU = cb.getSelectedIndex() ;
                        CopexUnit unit = listUnit.get(idU);

                        String s = tf.getText() ;
                        if (s == null || s.length() == 0){
                            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_ACTION_PARAM_VALUE"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                            return;
                        }
                        double value = 0;
                        try{
                            value = Double.parseDouble(s.replace(',', '.'));
                        }catch(NumberFormatException e){
                            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_ACTION_PARAM_VALUE"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                            return;
                        }
                        Parameter p = new Parameter(-1, CopexUtilities.getLocalText(((InitialParamQuantity)initActionParam).getQuantityName(edP.getLocale()), edP.getLocale()), CopexUtilities.getLocalText("", edP.getLocale()), value, CopexUtilities.getLocalText("", edP.getLocale()), unit);
                        ActionParamQuantity paramQ = new ActionParamQuantity(-1, initActionParam, p) ;
                        tabP[i] = paramQ;

                    }else if(initActionParam instanceof InitialParamMaterial){
                        // param of type material
                        ArrayList paramCB = (ArrayList)listComponent.get(i);
                        // in  0 the combo and in 1 the list (corresponding)
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        ArrayList<Material> listMat = (ArrayList<Material>)paramCB.get(1);
                        int idM = cb.getSelectedIndex() ;
                        Material material = listMat.get(idM);
                        ActionParamMaterial paramM = new ActionParamMaterial(-1, initActionParam, material, null);
                        tabP[i] = paramM ;
                    }else if (initActionParam instanceof InitialParamData){
                        // param of type data
                        ArrayList paramCB = (ArrayList)listComponent.get(i);
                        // in 0 the combo and in 1 the list (corresponding)
                        JComboBox cb = (JComboBox)paramCB.get(0);
                        ArrayList<QData> listData = (ArrayList<QData>)paramCB.get(1);
                        //int idM = cb.getSelectedIndex() ;
                        //QData data = listData.get(idM);
                        QData data = null;
                        String d = (String)cb.getSelectedItem();
                        for (int j=0; j<listData.size(); j++){
                            if (listData.get(j).getName(edP.getLocale()).equals(d)){
                                data = listData.get(j);
                                break;
                            }
                        }
                        if (data == null){
                            data = new QData(-1,CopexUtilities.getLocalText(d, edP.getLocale()), CopexUtilities.getLocalText("", edP.getLocale()), 0, CopexUtilities.getLocalText("", edP.getLocale()), listPhysicalQuantity.get(0).getListUnit().get(0));
                        }
                        ActionParamData paramD = new ActionParamData(-1, initActionParam, data);
                        tabP[i] = paramD ;
                    }
                    }else{
                        tabP[i] = l;
                    }
                }
                // possibly, link the qtt param to material param
                for (int i=0; i<tabP.length; i++){
                    if (tabP[i] instanceof ActionParamMaterial){
                        if ((((ActionParamMaterial)tabP[i]).getInitialParam() instanceof InitialParamMaterial && ((InitialParamMaterial)(((ActionParamMaterial)tabP[i]).getInitialParam())).getParamQuantity() !=null  )){
                            // rsearch param qtt
                            long dbKeyIQ = ((InitialParamMaterial)(((ActionParamMaterial)tabP[i]).getInitialParam())).getParamQuantity().getDbKey() ;
                            ActionParamQuantity paramQ = null;
                            for (int j=0; j<tabP.length; j++){
                                if (tabP[j] instanceof ActionParam && ((ActionParam)tabP[j]).getInitialParam() instanceof InitialParamQuantity && ((InitialParamQuantity)((ActionParam)tabP[j]).getInitialParam()).getDbKey() ==  dbKeyIQ){
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
            if (controlLenght() && d.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION){
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
        if (controlLenght() && c.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_COMMENTS);
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }

        List<LocalText> ld = CopexUtilities.getLocalText(d, edP.getLocale());
        List<LocalText> lc = CopexUtilities.getLocalText(c, edP.getLocale());
        CopexAction newAction = new CopexAction(ld, lc) ;
        if (a != null){
            newAction = new CopexActionNamed(ld, lc, a) ;
            newAction.getTaskRight().setDrawRight(a.isDraw() ? MyConstants.EXECUTE_RIGHT : MyConstants.NONE_RIGHT);
            if (tabP != null){
                if (a instanceof InitialActionChoice ){
                    newAction = new CopexActionChoice(ld, lc, a, tabP) ;
                }else if (a instanceof InitialActionManipulation){
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionManipulation(ld, lc, a, tabP, getListMaterialProd(a, v));
                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }else if (a instanceof InitialActionTreatment){
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionTreatment(ld, lc, a, tabP, getListDataProd(a, v));

                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }else if (a instanceof InitialActionAcquisition){
                    ArrayList v = new ArrayList();
                    newAction = new CopexActionAcquisition(ld, lc, a, tabP, getListDataProd(a, v));
                    if (v.get(0) != null &&  !((Boolean)v.get(0)))
                        return;
                }
            }
        }
        if(drawPanel != null){
            // save drawing
            if(drawPanel.getWhiteBoardPanel().getStatus().getChild("whiteboardContainers") == null || (drawPanel.getWhiteBoardPanel().getStatus().getChild("whiteboardContainers") != null && drawPanel.getWhiteBoardPanel().getStatus().getChild("whiteboardContainers").getContentSize() > 0)){
                newAction.setDraw(drawPanel.getWhiteBoardPanel().getStatus());
            }else{
                newAction.setDraw(null);
            }
        }

        newAction.setTaskRepeat(taskRepeat);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (modeAdd){
            // Create action
            CopexReturn cr = edP.addAction(newAction, insertIn);
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
        if(drawPanel != null){
            edP.createDrawTaskPreview(newAction.getDbKey(), drawPanel.getWhiteBoardPanel());
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.closeDialog();
   }

    /* returns the list od data prod  */
   private ArrayList<Object> getListDataProd(InitialNamedAction action, ArrayList v){
       ArrayList<Object> ld = new ArrayList();
        if (!(action instanceof InitialActionTreatment || action instanceof InitialActionAcquisition)){
           logger.log(Level.INFO, ("ceci n'est pas une action treatment ou acquisition !!"));
            v.add(true);
            return ld;
       }

       if(taskRepeat != null && taskRepeat.getListParam().size() > 0 && taskRepeat.getNbRepeat() > 1){
           int nbP = taskRepeat.getListParam().size();
           for (int i=0; i<nbP; i++){
               TaskRepeatParam param = taskRepeat.getListParam().get(i);
               if(param instanceof TaskRepeatParamOutputAcquisition){
                   ArrayList<QData> l = new ArrayList();
                   int nbO = ((TaskRepeatParamOutputAcquisition)param).getListValue().size();
                   for (int j=0; j<nbO; j++){
                       l.add(((TaskRepeatParamOutputAcquisition)param).getListValue().get(j).getData());
                   }
                   ld.add(l);
               }else if (param instanceof TaskRepeatParamOutputTreatment){
                   ArrayList<QData> l = new ArrayList();
                   int nbO = ((TaskRepeatParamOutputTreatment)param).getListValue().size();
                   for (int j=0; j<nbO; j++){
                       l.add(((TaskRepeatParamOutputTreatment)param).getListValue().get(j).getData());
                   }
                   ld.add(l);
               }
           }
           if(ld.size() > 0){
                v.add(true);
                return ld;
           }
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
           if (controlLenght() && s.length() > MyConstants.MAX_LENGHT_QUANTITY_NAME){
                String msg = edP.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_QUANTITY_NAME);
                edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return ld;
            }
           CopexUnit unit = null;
           if (action instanceof InitialActionAcquisition)
               unit =((InitialActionAcquisition)action).getListOutput().get(i).getUnitDataProd()[0];
           else if (action instanceof InitialActionTreatment)
               unit =((InitialActionTreatment)action).getListOutput().get(i).getUnitDataProd()[0];
           QData data = new QData(-1, CopexUtilities.getLocalText(s, edP.getLocale()), CopexUtilities.getLocalText("", edP.getLocale()), 0, CopexUtilities.getLocalText("", edP.getLocale()), unit);
           ld.add(data);
       }
       v.add(true);
       return ld ;
   }


   /* returns the list of material prod.  */
   private ArrayList<Object> getListMaterialProd(InitialNamedAction action, ArrayList v){
       ArrayList<Object> lm = new ArrayList();
       if (!(action instanceof InitialActionManipulation)){
           logger.log(Level.INFO, ("Attention ce n'est pas une action manipulation !! "));
           v.add(true);
           return lm;
       }
       if(taskRepeat != null && taskRepeat.getListParam().size() > 0 && taskRepeat.getNbRepeat() > 1){
           int nbP = taskRepeat.getListParam().size();
           for (int i=0; i<nbP; i++){
               TaskRepeatParam param = taskRepeat.getListParam().get(i);
               if(param instanceof TaskRepeatParamOutputManipulation){
                   ArrayList<Material> l = new ArrayList();
                   int nbO = ((TaskRepeatParamOutputManipulation)param).getListValue().size();
                   for (int j=0; j<nbO; j++){
                       l.add(((TaskRepeatParamOutputManipulation)param).getListValue().get(j).getMaterial());
                   }
                   lm.add(l);
               }
           }
           if(lm.size() > 0){
                v.add(true);
                return lm;
           }
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
           if (controlLenght() && s.length() > MyConstants.MAX_LENGHT_MATERIAL_NAME){
                String msg = edP.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, textProd);
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_NAME);
                edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                v.add(false);
                return lm;
            }

           // types list
           List<TypeMaterial> listType = ((InitialActionManipulation)action).getListOutput().get(i).getTypeMaterialProd();
           // param list
           List<Parameter> listParameters = new ArrayList();

           Material material = getMaterialWithName(s, listType, listParameters);
           if (material == null){
               logger.log(Level.INFO, ("le materiel n'a pas ete trouve!!"));
               material = new Material(CopexUtilities.getLocalText(s, edP.getLocale()),CopexUtilities.getLocalText("", edP.getLocale()), null, listType, listParameters, new MaterialSourceAction(action.getDbKey()));
               material.setDbKey(-1);
           }
           lm.add(material);
       }
       v.add(true);
       return lm ;
   }

   /* returns the material correspondin to this name,null otherwise  */
   private Material getMaterialWithName(String name, List<TypeMaterial>listType, List<Parameter> listParameters){
       ArrayList<Material> listMaterialProdBefore = edP.getMaterialProd(modeAdd);
       int nbM = listMaterialProdBefore.size();
       for (int i=0; i<nbM; i++){
           if (listMaterialProdBefore.get(i).getName(edP.getLocale()).equals(name)){
               return listMaterialProdBefore.get(i);
           }
       }
       return null;
   }

   /*select an action */
   private void selectAction(){
       removePanelDescription();
       removePanelSetting();
       removePanelComment();
       removeWhiteboardPanel();
       removeLabelImage();
       removePanelTaskRepeat();
       int id = cbActionName.getSelectedIndex() ;
       //if (id == 0 && isFreeAction){
       if (id == 0){
           if(isFreeAction){
               getContentPane().add(getPanelComments());
               setDescription();
               if(isActionRepeat)
                   setPanelTaskRepeat(true, new ArrayList(), new ArrayList());
               if(isInitProcDraw)
                    setPanelDraw(true);
           }
       }else{
           if (isFreeAction)
               id = id-1;
           else
               id = id -1 ;
           InitialNamedAction a = listInitialNamedAction.get(id);
           if (a.isSetting()){
               getContentPane().add(getPanelComments());
               setSetting(a);
           }else{
               getContentPane().add(getPanelComments());
               setDescription();
           }
           setPanelDraw(a.isDraw());
           if(a.isDraw() && a.getDefaultDraw() != null && modeAdd ){
               drawPanel.getWhiteBoardPanel().setStatus(a.getDefaultDraw());
               drawPanel.setSize(drawPanel.getWhiteBoardPanel().getWidth(), drawPanel.getWhiteBoardPanel().getHeight());
               drawPanel.setPreferredSize(new Dimension((int)drawPanel.getWhiteBoardPanel().getEnclosingScreenRectangle().getWidth(), (int)drawPanel.getWhiteBoardPanel().getEnclosingScreenRectangle().getHeight()+40));
           }
           ArrayList<InitialActionParam> listAllParams = new ArrayList();
           if(a.getVariable() != null){
                listAllParams.addAll(Arrays.asList(a.getVariable().getTabParam()));
           }
           ArrayList<InitialOutput> listOutput = new ArrayList();
           if(a instanceof InitialActionManipulation){
               ArrayList<InitialManipulationOutput> l = ((InitialActionManipulation)a).getListOutput();
               int nb = l.size();
               for (int i=0; i<nb; i++){
                   listOutput.add(l.get(i));
               }
           }else if (a instanceof InitialActionAcquisition){
               ArrayList<InitialAcquisitionOutput> l = ((InitialActionAcquisition)a).getListOutput();
               int nb = l.size();
               for (int i=0; i<nb; i++){
                   listOutput.add(l.get(i));
               }
           }else if (a instanceof InitialActionTreatment){
               ArrayList<InitialTreatmentOutput> l = ((InitialActionTreatment)a).getListOutput();
               int nb = l.size();
               for (int i=0; i<nb; i++){
                   listOutput.add(l.get(i));
               }
           }
           setPanelTaskRepeat(a.isRepeat(), listAllParams, listOutput);
       }
       resizeActionDialog();
   }


   /* parameters of the action named  */
   private void setSetting(InitialNamedAction action){
       this.actionNamedSel = action;
       getPanelSetting() ;
       // set parameters
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
           String t = variable.getTextLibelle(edP.getLocale(), i);
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
               tf.setSize(60, 27);
               tf.setPreferredSize(new Dimension(60,27));
               tf.setToolTipText(edP.getBundleString("TOOLTIPTEXT_ACTION_PARAM_VALUE"));
               panelSetting.add(tf);
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamQuantity){
                   NumberFormat numberFormat = NumberFormat.getNumberInstance(getLocale());
                   numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
                   numberFormat.setGroupingUsed(false);
                   tf.setText(numberFormat.format(((ActionParamQuantity)tabParam[i]).getParameter().getValue()));
               }
               // combo box units
               JComboBox cb = new JComboBox();
               List<CopexUnit> listU = ((InitialParamQuantity)param).getListUnit() ;
               for (int j=0; j<listU.size(); j++){
                   cb.addItem(listU.get(j).getSymbol(edP.getLocale()));
               }
               if (!modeAdd && tabParam != null  && tabParam[i] != null && tabParam[i] instanceof ActionParamQuantity){
                   cb.setSelectedItem(((ActionParamQuantity)tabParam[i]).getParameter().getUnit().getSymbol(edP.getLocale()));
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
                   cb.addItem(materials.get(j).getName(edP.getLocale()));
               }
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamMaterial){
                   cb.setSelectedItem(((ActionParamMaterial)tabParam[i]).getMaterial().getName(edP.getLocale()));
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
                   cb.addItem(datas.get(j).getName(edP.getLocale()));
               }
               if (!modeAdd && tabParam != null && tabParam[i] != null && tabParam[i] instanceof ActionParamData){
                   cb.setSelectedItem(((ActionParamData)tabParam[i]).getData().getName(edP.getLocale()));
               }
               ArrayList compCB = new ArrayList();
               compCB.add(cb);
               compCB.add(datas);
               listComponent.add(compCB);
               panelSetting.add(cb);
           }
       }
       String t = variable.getTextLibelle(edP.getLocale(), -1);
       if (t.length() > 0){
             JLabel label = new JLabel(t);
             label.setFont(new java.awt.Font("Tahoma", 1, 11));
             label.setSize(5+CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
             maxWidthLabel = Math.max(maxWidthLabel, label.getWidth());
             panelSetting.add(label);
       }
       // material / data produces
       if (action instanceof InitialActionManipulation){
           int nbMat = ((InitialActionManipulation)action).getNbMaterialProd() ;
           for (int i=0; i<nbMat; i++){
                textProd  = ((InitialActionManipulation)action).getListOutput().get(i).getTextProd(edP.getLocale());
                JLabel label = new JLabel(textProd+":") ;
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
                    cb.addItem(listMat.get(k).getName(edP.getLocale()));
                }
                panelSetting.add(cb);
                listMaterialProd.add(cb);
                if (!modeAdd &&  this.materialsProd.get(i) instanceof Material){
                  //tf.setText(this.materialsProd.get(i).getName());
                    cb.setSelectedItem(((Material)(this.materialsProd.get(i))).getName(edP.getLocale()));
                }
           }
       }else if (action instanceof InitialActionAcquisition){
           int nbData = ((InitialActionAcquisition)action).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                textProd  = ((InitialActionAcquisition)action).getListOutput().get(i).getTextProd(edP.getLocale());
                JLabel label = new JLabel(textProd+" :") ;
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                panelSetting.add(label);
                JTextField tf = new JTextField() ;
                tf.setSize(80, 25);
                tf.setPreferredSize(new Dimension(80,25));
                panelSetting.add(tf);
                listDataProd.add(tf);
                if (!modeAdd && this.datasProd.get(i) instanceof QData){
                   tf.setText(((QData)this.datasProd.get(i)).getName(edP.getLocale()));
                }
           }
       }else if (action instanceof InitialActionTreatment){
           int nbData = ((InitialActionTreatment)action).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                textProd  = ((InitialActionTreatment)action).getListOutput().get(i).getTextProd(edP.getLocale());
                JLabel label = new JLabel(textProd+" :") ;
                label.setFont(new java.awt.Font("Tahoma", 1, 11));
                label.setSize(CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 20);
                panelSetting.add(label);
                JTextField tf = new JTextField() ;
                tf.setSize(80, 25);
                tf.setPreferredSize(new Dimension(80,25));
                panelSetting.add(tf);
                listDataProd.add(tf);
                if (!modeAdd && this.datasProd.get(i) instanceof QData){
                   tf.setText(((QData)this.datasProd.get(i)).getName(edP.getLocale()));
                }
           }
       }
       if((maxWidthLabel+20) > this.getWidth()){
            this.panelSetting.setSize(maxWidthLabel+40,190);
       }else{
           this.panelSetting.setSize(this.getWidth() -10,190);
       }
       //this.panelSetting.setSize(325,125);
       this.panelSetting.revalidate();
       int y = posy;
       if (viewName)
            y = labelName.getY()+labelName.getHeight()+30;
       panelSetting.setBounds(posx-10, y, panelSetting.getWidth(), panelSetting.getHeight());
       getContentPane().add(panelSetting);
       resizeDialog();
   }


    /*set the drawing panel */
    private void setPanelDraw(boolean visible){
        removeWhiteboardPanel();
        if (visible){
            //getContentPane().add(getWhiteboardPanel());
            getContentPane().add(getPanelButtonsWhiteboard());
            getContentPane().add(getScrollPaneDraw());
            setResizable(true);
        }
    }

    /*set the task repeat panel */
    private void setPanelTaskRepeat(boolean visible, ArrayList<InitialActionParam> listAllParams, ArrayList<InitialOutput> listOutput){
        if(this.panelComments == null)
            return;
        removePanelTaskRepeat();
        if(visible){
            getContentPane().add(getTaskRepeatPanel(listAllParams, listOutput));
        }
    }

    /* resize dialog */
    private void resizeDialog(){
        if(minDim == null || panelComments == null)
            return;
        int newWidth = this.getWidth() ;
        if(panelSetting != null)
            newWidth = panelSetting.getWidth()+20;
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
        // if the panel drawing exists, we give it the most place possible 
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
            panelButtonsWhiteboard.setBounds(panelButtonsWhiteboard.getX(), y, panelButtonsWhiteboard.getWidth(), panelButtonsWhiteboard.getHeight());
            scrollPaneDraw.setBounds(scrollPaneDraw.getX(),5+ panelButtonsWhiteboard.getY()+panelButtonsWhiteboard.getHeight(), newAreaW, h);
            scrollPaneDraw.revalidate();
        }
        if(taskRepeatPanel != null){
            y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(scrollPaneDraw != null){
                y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+20;
            }else if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(taskRepeatPanel.getX(), y, newAreaW, taskRepeatPanel.getHeight());
            taskRepeatPanel.revalidate();
        }
        // buttons
        y = panelComments.getY()+panelComments.getHeight()+30;
        if(taskRepeatPanel != null){
            y = taskRepeatPanel.getY()+taskRepeatPanel.getHeight()+30;
        }else if(scrollPaneDraw != null){
            y = scrollPaneDraw.getY()+scrollPaneDraw.getHeight()+30;
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

    @Override
    public void actionUpdateNbRepeat(int nbRepeat) {
        if(nbRepeat ==1)
            setSettingEnabled(true);
    }

    private void setSettingEnabled(boolean enabled){
        if (this.panelSetting != null){
            int nb = panelSetting.getComponentCount();
            for (int i=0; i<nb; i++){
                panelSetting.getComponent(i).setEnabled(enabled);
            }
        }
    }

    private void setElementEnabled(Object o, boolean enabled){
        if (o instanceof String){
            setSettingEnabled(true);
        }else if (o instanceof InitialParamData){
            int id = getIdParam((InitialParamData)o);
            if (id != -1){
                ArrayList paramCB = (ArrayList)listComponent.get(id);
                // in 0 the combo and in 1 the list (corresponding)
                JComboBox cb = (JComboBox)paramCB.get(0);
                cb.setEnabled(enabled);
            }
        }else if (o instanceof InitialParamMaterial){
            int id = getIdParam((InitialParamMaterial)o);
            if (id != -1){
                ArrayList paramCB = (ArrayList)listComponent.get(id);
                // in 0 the combo and in 1 the list (corresponding)
                JComboBox cb = (JComboBox)paramCB.get(0);
                cb.setEnabled(enabled);
            }
        }else if (o instanceof InitialParamQuantity){
            int id = getIdParam((InitialParamQuantity)o);
            if (id != -1){
                ArrayList param = (ArrayList)listComponent.get(id);
                // in 0 the textField
                // in 1 : a vector with in 0 the combobox  and in 1 the list (corresponding)
                JTextField tf = (JTextField)param.get(0);
                ArrayList paramCB = (ArrayList)param.get(1);
                JComboBox cb = (JComboBox)paramCB.get(0);
                tf.setEnabled(enabled);
                cb.setEnabled(enabled);
            }
        }else if (o instanceof InitialManipulationOutput){
            int id = getIdOutputMaterial((InitialManipulationOutput)o);
            if (id != -1){
                ((JComboBox)listMaterialProd.get(id)).setEnabled(enabled) ;
            }
        }else if (o instanceof InitialAcquisitionOutput){
            int id = getIdOutputData((InitialAcquisitionOutput)o);
            if (id != -1){
                ((JTextField)listDataProd.get(id)).setEnabled(enabled) ;
            }
        }else if (o instanceof InitialTreatmentOutput){
            int id = getIdOutputData((InitialTreatmentOutput)o);
            if (id != -1){
                ((JTextField)listDataProd.get(id)).setEnabled(enabled) ;
            }
        }
    }

    /* returns the index of the parameter  */
    private int getIdParam(InitialActionParam p){
       InitialActionVariable variable = actionNamedSel.getVariable() ;
       int nbParam = variable.getNbParam() ;
       for (int i=0; i<nbParam; i++){
           InitialActionParam param = variable.getTabParam()[i];
           if (param.equals(p))
               return i;
       }
       return -1;
    }

    /* return the index output material, -1 otherwise */
    private int getIdOutputMaterial(InitialManipulationOutput o){
         if (actionNamedSel instanceof InitialActionManipulation){
           int nbMat = ((InitialActionManipulation)actionNamedSel).getNbMaterialProd() ;
           for (int i=0; i<nbMat; i++){
                if(((InitialActionManipulation)actionNamedSel).getListOutput().get(i).equals(o)){
                    return i;
                }
           }
         }
         return -1;
    }

    /* returns index output data, -1 if not found */
    private int getIdOutputData(InitialOutput o){
         if (actionNamedSel instanceof InitialActionAcquisition){
           int nbData = ((InitialActionAcquisition)actionNamedSel).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                if(((InitialActionAcquisition)actionNamedSel).getListOutput().get(i).equals(o)){
                    return i;
                }
           }
         }else if (actionNamedSel instanceof InitialActionTreatment){
             int nbData = ((InitialActionTreatment)actionNamedSel).getNbDataProd() ;
           for (int i=0; i<nbData; i++){
                if(((InitialActionTreatment)actionNamedSel).getListOutput().get(i).equals(o)){
                    return i;
                }
           }
         }
         return -1;
    }

    @Override
    public void actionSetSelected(Object oldSel, Object newSel) {
        setElementEnabled(oldSel, true);
        setElementEnabled(newSel, false);
    }

    @Override
    public void enterKeyPressed() {
        validDialog();
    }

    private JPanel getPanelButtonsWhiteboard(){
        if(panelButtonsWhiteboard == null){
            panelButtonsWhiteboard = new JPanel();
            panelButtonsWhiteboard.setName("panelButtonsWhiteboard");
            panelButtonsWhiteboard.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelButtonsWhiteboard.add(getWhiteboardButtonCopy());
            panelButtonsWhiteboard.add(getWhiteboardButtonPaste());
            panelButtonsWhiteboard.add(getWhiteboardButtonCut());
            int y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            panelButtonsWhiteboard.setBounds(posx,y,areaW,30);
        }
        return panelButtonsWhiteboard;
    }

    private MyMenuItem getWhiteboardButtonCopy(){
        if(menuItemCopy == null){
            menuItemCopy = new MyMenuItem(edP.getBundleString("TOOLTIPTEXT_WHITEBOARD_COPY"),panelButtonsWhiteboard.getBackground(),edP.getCopexImage("Bouton-AdT-28_copier.png"), edP.getCopexImage("Bouton-AdT-28_copier_survol.png"), edP.getCopexImage("Bouton-AdT-28_copier_clic.png"), edP.getCopexImage("Bouton-AdT-28_copier_grise.png"));
            menuItemCopy.addActionMenuEvent(this);
        }
        return menuItemCopy;
    }
    private MyMenuItem getWhiteboardButtonPaste(){
        if(menuItemPaste == null){
            menuItemPaste = new MyMenuItem(edP.getBundleString("TOOLTIPTEXT_WHITEBOARD_PASTE"),panelButtonsWhiteboard.getBackground(),edP.getCopexImage("Bouton-AdT-28_coller.png"), edP.getCopexImage("Bouton-AdT-28_coller_survol.png"), edP.getCopexImage("Bouton-AdT-28_coller_clic.png"), edP.getCopexImage("Bouton-AdT-28_coller_grise.png"));
            menuItemPaste.addActionMenuEvent(this);
        }
        return menuItemPaste;
    }
    private MyMenuItem getWhiteboardButtonCut(){
        if(menuitemCut == null){
            menuitemCut = new MyMenuItem(edP.getBundleString("TOOLTIPTEXT_WHITEBOARD_CUT"),panelButtonsWhiteboard.getBackground(),edP.getCopexImage("Bouton-AdT-28_couper.png"), edP.getCopexImage("Bouton-AdT-28_couper_survol.png"), edP.getCopexImage("Bouton-AdT-28_couper_clic.png"), edP.getCopexImage("Bouton-AdT-28_couper_grise.png"));
            menuitemCut.addActionMenuEvent(this);
        }
        return menuitemCut;
    }

    @Override
    public void doMenuItemMousePressed(MyMenuItem menu) {
        if(menuItemCopy.equals(menu)){
            whiteboardCopy();
        }else if(menuItemPaste.equals(menu)){
            whiteboardPaste();
        }else if (menuitemCut.equals(menu)){
            whiteboardCut();
        }
    }

    /* cut*/
    private void whiteboardCut(){
        //drawPanel.getWhiteBoardPanel().whiteboardCut();
    }

    /*copy*/
    private void whiteboardCopy(){
        //drawPanel.getWhiteBoardPanel().whiteboardCopy();
    }

    /* paste */
    private void whiteboardPaste(){
        //drawPanel.getWhiteBoardPanel().whiteboardPaste();
    }

    private boolean controlLenght(){
        return edP.controlLenght();
    }
    
}
