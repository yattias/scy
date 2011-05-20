package eu.scy.client.tools.fitex.GUI;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphPanel.java
 *
 * Created on 27 nov. 2008, 20:41:06
 */



import eu.scy.client.tools.fitex.analyseFn.Function;
import eu.scy.client.tools.dataProcessTool.common.FunctionModel;
import eu.scy.client.tools.dataProcessTool.common.FunctionParam;
import eu.scy.client.tools.dataProcessTool.common.ParamGraph;
import eu.scy.client.tools.dataProcessTool.common.PreDefinedFunction;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.client.tools.dataProcessTool.utilities.ActionCopexButton;
import eu.scy.client.tools.dataProcessTool.utilities.CopexButtonPanel;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marjolaine
 */
public class FitexPanel extends javax.swing.JPanel implements ActionCopexButton{

    private static final Color DEFAULT_TEXT_COLOR  = Color.LIGHT_GRAY;
    private FitexToolPanel owner;
    /* data */
    private DefaultTableModel[] datas = null;
    private char right;
    private Color[] plotsColor = null;
    /* action fitexPanel */
    private ActionFitex actionFitex;

    /* drawing zone */
    private DrawPanel zoneDeTrace = null;
    private ParamGraph paramGraph = null;
    // parameters of the drawing zone
    //private Graphics g ;
    private int width ;
    private int height ;
    // color of the selected function (blue)
    private Color couleurSelect=DataConstants.FUNCTION_COLOR_1 ;
    private char typeSelect = DataConstants.FUNCTION_TYPE_Y_FCT_X;
    private String idPredefFunction = null;

    // functions
    private HashMap<Color,Function> mapDesFonctions = new HashMap<Color,Function>();
    // spinners
    private HashMap<String, BoxSpinner> mapDesSpinners = new HashMap<String, BoxSpinner>();

    private boolean isPanelFunction;
    private JPanel panelFctModel;
    private JPanel panelFct;
    private JRadioButton rbBlue;
    private JRadioButton rbGreen;
    private JRadioButton rbBlack;
    private JLabel labelFct;
    private JTextField textFieldFct;
    private DistancePanel[] tabPanelDist;
    private JLabel labelDist;
    private JPanel panelDist;

    private JPanel panelFctParam;
    private JPanel parametresFn;
    private JScrollPane scrollPaneParametresFn;
    private JSeparator sepDrawFct;
    private CopexButtonPanel buttonPreDefinedFct = null;

    /* previous param , 1 level of zoom*/
    private ParamGraph previousParam = null;
    private ParamGraph nextParam = null;

    /* Images */
    private ImageIcon imgFct;
    private ImageIcon imgFctClic;
    private ImageIcon imgFctSurvol;
    private ImageIcon imgFctGris;

    
    /** Creates new form FitexPanel */
    public FitexPanel(FitexToolPanel owner, DefaultTableModel[] datas, Color[] plotsColor, ArrayList<FunctionModel> listFunctionModel, ParamGraph pg, char right) {
        super();
        this.owner = owner;
        this.datas = datas ;
        this.plotsColor = plotsColor;
        this.paramGraph = pg;
        tabPanelDist = new DistancePanel[datas.length];
        this.right = right;
        initGUI(listFunctionModel);
    }

    private void initGUI(ArrayList<FunctionModel> listFunctionModel){
        this.setLayout(new BorderLayout());
        imgFct = owner.getCopexImage("Bouton-AdT-28_graph_predefinedFunction.png");
        imgFctClic = owner.getCopexImage("Bouton-AdT-28_graph_predefinedFunction_cli.png");
        imgFctSurvol = owner.getCopexImage("Bouton-AdT-28_graph_predefinedFunction_sur.png");
        imgFctGris = owner.getCopexImage("Bouton-AdT-28_graph_predefinedFunction_gri.png");
        initComponents();
        getPanelFct();
        isPanelFunction = false;

        // functionSelector.setVisible(false);
        width = 400;
        height = 400;
        zoneDeTrace = new DrawPanel(this, datas, plotsColor, paramGraph, width, height, right) ;
        this.add(zoneDeTrace, BorderLayout.CENTER);
        //this.add(getPanelFctModel(), BorderLayout.NORTH);
        setInitialListFunction(listFunctionModel);

    }

    
    public DrawPanel getDrawPanel(){
        return this.zoneDeTrace ;
    }

    private JSeparator getSepDrawFct(){
        if(this.sepDrawFct == null){
            sepDrawFct = new JSeparator(JSeparator.HORIZONTAL);
        }
        return this.sepDrawFct;
    }

    
    private JPanel getPanelFctModel(){
        if (panelFctModel == null){
            panelFctModel = new JPanel();
            panelFctModel.setName("panelFctModel");
            panelFctModel.setLayout(new BorderLayout());
            panelFctModel.add(getPanelFct(), BorderLayout.NORTH);
            panelFctModel.add(getSepDrawFct(), BorderLayout.SOUTH);
        }
        return panelFctModel;
    }

    private JPanel getPanelFct(){
        if (panelFct == null){
            panelFct = new JPanel();
            panelFct.setName("panelFct");
            panelFct.setLayout(new BoxLayout(panelFct, javax.swing.BoxLayout.LINE_AXIS));
            panelFct.setBackground(new java.awt.Color(223, 223, 223));
            panelFct.add(getRbBlue());
            panelFct.add(getRbGreen());
            panelFct.add(getRbBlack());
            panelFct.add(Box.createHorizontalStrut(5));
            panelFct.add(getButtonPreDefinedFct());
            panelFct.add(Box.createHorizontalStrut(5));
            panelFct.add(getLabelFct());
            panelFct.add(getTextFieldFct());
        }
        return panelFct;
    }
    private JRadioButton getRbBlue(){
        if (rbBlue == null){
            rbBlue = new JRadioButton();
            rbBlue.setName("rbBlue");
            rbBlue.setSelected(true);
            rbBlue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rbBlue.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rbBlue.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    maJFonction(DataConstants.FUNCTION_COLOR_1);
                    rbBlue.setSelected(true);
                    if(rbGreen != null)
                            rbGreen.setSelected(false);
                    if(rbBlack != null)
                        rbBlack.setSelected(false);
                }
            });
            rbBlue.setIcon(getFitexImage("bleu_up.gif"));
            rbBlue.setSelectedIcon(getFitexImage("bleu_dn.gif"));
        }
        return rbBlue ;
    }

    private JRadioButton getRbGreen(){
        if (rbGreen == null){
            rbGreen = new JRadioButton();
            rbGreen.setName("rbGreen");
            rbGreen.setSelected(false);
            rbGreen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rbGreen.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rbGreen.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    maJFonction(DataConstants.FUNCTION_COLOR_2);
                    rbGreen.setSelected(true);
                    rbBlue.setSelected(false);
                    if(rbBlack != null)
                        rbBlack.setSelected(false);
                }
            });
            rbGreen.setIcon(getFitexImage("vert_up.gif"));
            rbGreen.setSelectedIcon(getFitexImage("vert_dn.gif"));
        }
        return rbGreen ;
    }
    private JRadioButton getRbBlack(){
        if (rbBlack == null){
            rbBlack = new JRadioButton();
            rbBlack.setName("rbBlack");
            rbBlack.setSelected(false);
            rbBlack.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rbBlack.setMargin(new java.awt.Insets(0, 0, 0, 0));
            rbBlack.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    maJFonction(DataConstants.FUNCTION_COLOR_3);
                    rbBlack.setSelected(true);
                    rbBlue.setSelected(false);
                    if(rbGreen != null)
                        rbGreen.setSelected(false);
                }
            });
            rbBlack.setIcon(getFitexImage("noir_up.gif"));
            rbBlack.setSelectedIcon(getFitexImage("noir_dn.gif"));
        }
        return rbBlack ;
    }

    private JLabel getLabelFct(){
        if(labelFct == null){
            labelFct = new JLabel();
            labelFct.setName("labelFct");
            labelFct.setText(getBundleString("LABEL_FUNCTION")+" = " );
        }
        return labelFct;
    }

    private JTextField getTextFieldFct(){
        if (textFieldFct == null){
            textFieldFct = new JTextField();
            textFieldFct.setName("textFieldFct");
            textFieldFct.setForeground(DataConstants.FUNCTION_COLOR_1);
            textFieldFct.setEnabled(right == DataConstants.EXECUTIVE_RIGHT);
            textFieldFct.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    drawFct();
                }
            });
            textFieldFct.setText(getBundleString("DEFAULT_FUNCTION_MODEL"));
            textFieldFct.setFont(new Font(Font.DIALOG, Font.ITALIC, 10));
            textFieldFct.setForeground(DEFAULT_TEXT_COLOR);
            textFieldFct.addFocusListener(new java.awt.event.FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if(textFieldFct.getText().equals(getBundleString("DEFAULT_FUNCTION_MODEL"))){
                        textFieldFct.setText("");
                        textFieldFct.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
                        textFieldFct.setForeground(couleurSelect);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    drawFct();
                    for (String param:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
                        mapDesSpinners.get(param).setFocus();
                    }
                    if(textFieldFct.getText().equals("")){
                        textFieldFct.setText(getBundleString("DEFAULT_FUNCTION_MODEL"));
                        textFieldFct.setFont(new Font(Font.DIALOG, Font.ITALIC, 10));
                        textFieldFct.setForeground(DEFAULT_TEXT_COLOR);
                    }
                }
            });
        }
        return textFieldFct;
    }

    private CopexButtonPanel getButtonPreDefinedFct(){
        if(buttonPreDefinedFct == null){
            buttonPreDefinedFct = new CopexButtonPanel(imgFct.getImage(),imgFctSurvol.getImage(), imgFctClic.getImage(), imgFctGris.getImage() );
            //buttonPreDefinedFct.setBounds((panelFct.getHeight()-buttonPreDefinedFct.getHeight())/2,5, buttonPreDefinedFct.getWidth(), buttonPreDefinedFct.getHeight());
            buttonPreDefinedFct.setPreferredSize(buttonPreDefinedFct.getSize());
            buttonPreDefinedFct.setMinimumSize(buttonPreDefinedFct.getSize());
            buttonPreDefinedFct.setMaximumSize(buttonPreDefinedFct.getSize());
            buttonPreDefinedFct.addActionCopexButton(this);
            buttonPreDefinedFct.setToolTipText(owner.getBundleString("TOOLTIPTEXT_PREDEFINED_FUNCTION"));
            buttonPreDefinedFct.setEnabled(right == DataConstants.EXECUTIVE_RIGHT);
        }
        return buttonPreDefinedFct;
    }
    
    private JLabel getLabelDist(){
        if(labelDist == null){
            labelDist = new JLabel();
            labelDist.setName("labelDist");
            labelDist.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            labelDist.setText(getBundleString("LABEL_DIST"));
        }
        return labelDist;
    }
    
    private void drawFct(){
        if(panelFctModel == null)
            return;
        if(panelFctParam != null){
            panelFctParam.removeAll();
            panelFctModel.remove(panelFctParam);
            panelFctParam = null;
            for(int d=0; d<tabPanelDist.length; d++){
                tabPanelDist[d] = null;
            }
            panelDist = null;
            parametresFn = null;
            scrollPaneParametresFn = null;
        }
        panelFctModel.add(getPanelFctParam(), BorderLayout.CENTER);
        panelFctModel.revalidate();
        panelFctModel.repaint();
        recupererFn();
        formComponentResized(null);
    }


    private JPanel getPanelDist(){
        if(panelDist == null){
            panelDist = new JPanel();
            panelDist.setName("panelDist");
            //panelDist.setLayout(new BoxLayout(panelDist, BoxLayout.Y_AXIS));
            panelDist.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelDist.add(getLabelDist());
            for(int d=0; d<tabPanelDist.length; d++){
               panelDist.add(getPanelDist(d));
           }
        }
        return panelDist;
    }
    private DistancePanel getPanelDist(int id){
        if (tabPanelDist[id] == null){
            tabPanelDist[id] = new DistancePanel(this, plotsColor[id]);
        }
        return tabPanelDist[id];
    }

    


   /**
    * Instanciation ActionFitex.
    * @param action ActionFitex
    */
    public void addActionFitex(ActionFitex action){
        this.actionFitex=action;
    }


    public  ImageIcon getFitexImage(String img){
        return new ImageIcon(getClass().getResource( "/Images/" +img));
    }

    /* show errors*/
    public boolean displayError(CopexReturn dr, String title) {
        if (dr.mustConfirm ()){
            int erreur = JOptionPane.showConfirmDialog(this ,dr.getText() , title,JOptionPane.OK_CANCEL_OPTION);
            if (erreur == JOptionPane.OK_OPTION)
                return true;
        }else if (dr.isError()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.ERROR_MESSAGE);

        }else if (dr.isWarning()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }


    /* return the message*/
    public String getBundleString(String key){
       return owner.getBundleString(key);

    }

   private boolean isRows(){
       for(int i=0; i<datas.length; i++){
           if(datas[i].getRowCount() > 0)
               return true;
       }
       return false;
   }
    /* update the initial function */
    private void setInitialListFunction(ArrayList<FunctionModel> listFunctionModel){
        if (listFunctionModel == null)
            return;
        int nb = listFunctionModel.size() ;
        // if not exist, creation
        if (!isRows())
            return ;
        isPanelFunction = true;
        if(panelFctModel == null)
            this.add(getPanelFctModel(), BorderLayout.NORTH);
        drawFct();
        for (int i=0; i<nb; i++){
            FunctionModel fm = listFunctionModel.get(i);
            Function f = new Function(owner, fm.getDescription(),fm.getType(), fm.getIdPredefFunction(),  datas);
            typeSelect = f.getType();
            idPredefFunction = f.getIdPredefFunction();
            int nbP = fm.getListParam().size();
            for (int k=0; k<nbP; k++){
                f.setValeurParametre(fm.getListParam().get(k).getParam(), fm.getListParam().get(k).getValue());
            }
            mapDesFonctions.put(fm.getColor(), f);
            // show parameters
            affichageParametres(fm.getColor()) ;
            f.majRF();
        }
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        maJFonction(DataConstants.FUNCTION_COLOR_1);
    }
    
    /** set parameters */
    public void setParameters(ParamGraph pg) {
        this.paramGraph = pg;
        if (zoneDeTrace != null)
            zoneDeTrace.setParam(pg) ;
   }

   

    public void setParameters(double x_min, double x_max, double y_min, double y_max){
        this.paramGraph.setX_min(x_min);
        this.paramGraph.setX_max(x_max);
        this.paramGraph.setY_min(y_min);
        this.paramGraph.setY_max(y_max);
        if (zoneDeTrace != null)
            zoneDeTrace.setParam(paramGraph) ;
        actionFitex.setParam(paramGraph.getPlots(),  x_min, x_max, paramGraph.getDeltaX(), y_min, y_max, paramGraph.getDeltaY(), paramGraph.isDeltaFixedAutoscale());
    }
    

    public char getGraphMode(){
        if (zoneDeTrace != null)
            return zoneDeTrace.getGraphMode();
        else
            return DataConstants.MODE_DEFAULT ;
    }

    /** sll K define */
    public void affichageK(Color coul) {
        Double k;
        NumberFormat nfE = NumberFormat.getNumberInstance(owner.getLocale());
        DecimalFormat formatE = (DecimalFormat)nfE;
        formatE.applyPattern("0.#####E0");
        NumberFormat nf = NumberFormat.getNumberInstance(owner.getLocale());
        DecimalFormat format = (DecimalFormat)nf;
        format.applyPattern("###.#####");
        //DecimalFormat formatE = new DecimalFormat("0.####E0");
        //DecimalFormat format = new DecimalFormat("####.#####");
        for (int d=0; d<tabPanelDist.length; d++){
            if(tabPanelDist[d] != null){
                if (mapDesFonctions.get(coul)==null || mapDesFonctions.get(coul).getRF()[d]==null)
                    tabPanelDist[d].setText(coul, DistancePanel.NO_DISTANCE);
                else {
                    k = (mapDesFonctions.get(coul)).getRF()[d] ;
                    k = chiffresSignificatifs(k,4) ;
                    if (k != 0 && (k>-0.1 && k<0.1) || k>=1000 || k<=-1000)
                        tabPanelDist[d].setText(coul, formatE.format(k));
                    else
                        tabPanelDist[d].setText(coul, format.format(k));
                }
            }
        }
    }

    /** returns a double with the significant number  */
    public double chiffresSignificatifs(double x, int nbChiffres) {
        if(x == 0)
            return 0;
        int rang = (int) Math.floor(1+Math.log10(Math.abs(x))) ;
        int power = nbChiffres - rang ;
        x =  (Math.round(x*Math.pow(10,power)) )/ (Math.pow(10,power)) ;
        return x ;
    }

    /** get function and insert in the hashmap*/
    public void recupererFn() {
        if (!isRows())
            return ;
        String text = textFieldFct.getText();
        if(text.equals(getBundleString("DEFAULT_FUNCTION_MODEL")))
            text = "";
        if (mapDesFonctions.get(couleurSelect) == null){
            mapDesFonctions.put(couleurSelect, new Function(owner, text, typeSelect, idPredefFunction, datas));
        }else{
            String oldT = mapDesFonctions.get(couleurSelect).getIntitule();
            if(oldT != null  && !oldT.equals(text) || typeSelect != mapDesFonctions.get(couleurSelect).getType() ||
                    ((idPredefFunction == null && mapDesFonctions.get(couleurSelect).getIdPredefFunction() != null) ||
                    (idPredefFunction != null && mapDesFonctions.get(couleurSelect).getIdPredefFunction() == null) ||
                    (idPredefFunction != null && mapDesFonctions.get(couleurSelect).getIdPredefFunction() != null && !idPredefFunction.equals(mapDesFonctions.get(couleurSelect).getIdPredefFunction()) )))
                mapDesFonctions.get(couleurSelect).maJFonction(text, typeSelect, idPredefFunction) ;
        }
        affichageParametres(couleurSelect) ;
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        ArrayList<FunctionParam> listParam = new ArrayList();
        for (String param:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
            double valParam = mapDesFonctions.get(couleurSelect).getMapParametre().get(param).valeur() ;
            FunctionParam p = new FunctionParam(-1, param, valParam);
            listParam.add(p);
        }
        if(actionFitex != null)
            actionFitex.setFunctionModel(text, typeSelect, couleurSelect, listParam, idPredefFunction);
    }

    /** update parameters fo the function */
    public void affichageParametres(Color coul){
        int heightPanel = 0;
        int widthPanel=5;
        //int widthPanel = parametresFn.getWidth();
        Dimension dim = new Dimension() ;

        parametresFn.removeAll();
        if (mapDesFonctions.get(coul)!=null) {

            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                mapDesSpinners.put(param , new BoxSpinner(this, right)) ;
                parametresFn.add(mapDesSpinners.get(param));
                mapDesSpinners.get(param).setTextLabel(param);
                double valParam = mapDesFonctions.get(coul).getMapParametre().get(param).valeur() ;
                mapDesSpinners.get(param).setValue(valParam) ;
                heightPanel = mapDesSpinners.get(param).getHauteur() ;
                heightPanel = 40;
            }
        }
        if(parametresFn.getPreferredSize().getWidth() > getWidth())
            heightPanel = 50;
        dim.setSize(getWidth(), heightPanel) ;
        scrollPaneParametresFn.setPreferredSize(dim);
        scrollPaneParametresFn.setMinimumSize(dim);
        this.repaint();
    }



    /** spinner change */
    public void maJParametreDansFonction(String param, double val) {
        mapDesFonctions.get(couleurSelect).setValeurParametre(param , val) ;
        zoneDeTrace.repaint();
        mapDesFonctions.get(couleurSelect).majRF();
        affichageK(couleurSelect);
        ArrayList<FunctionParam> listParam = new ArrayList();
        for (String p:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
            double valParam = mapDesFonctions.get(couleurSelect).getMapParametre().get(p).valeur() ;
            FunctionParam fp = new FunctionParam(-1, p, valParam);
            listParam.add(fp);
        }
        if(actionFitex != null)
            actionFitex.setFunctionModel(mapDesFonctions.get(couleurSelect).getIntitule(), typeSelect, couleurSelect,listParam ,mapDesFonctions.get(couleurSelect).getIdPredefFunction());
    }

    /** function update*/
    public void maJFonction(Color coul) {
        textFieldFct.setForeground(coul);
        if (mapDesFonctions.get(coul)!=null) {
            textFieldFct.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
            if(mapDesFonctions.get(coul).getType() == DataConstants.FUNCTION_TYPE_Y_FCT_X){
                labelFct.setText(getBundleString("LABEL_FUNCTION")+" = " );
                typeSelect = DataConstants.FUNCTION_TYPE_Y_FCT_X;
            }else{
                labelFct.setText(getBundleString("LABEL_FUNCTION_Y")+" = " );
                typeSelect = DataConstants.FUNCTION_TYPE_X_FCT_Y ;
            }
            idPredefFunction = mapDesFonctions.get(coul).getIdPredefFunction();
            textFieldFct.setText((mapDesFonctions.get(coul)).getIntitule());
        }
        else{
            textFieldFct.setText(getBundleString("DEFAULT_FUNCTION_MODEL"));
            textFieldFct.setFont(new Font(Font.DIALOG, Font.ITALIC, 10));
            textFieldFct.setForeground(DEFAULT_TEXT_COLOR);
            labelFct.setText(getBundleString("LABEL_FUNCTION")+" = " );
            typeSelect = DataConstants.FUNCTION_TYPE_Y_FCT_X;
            idPredefFunction = null;
        }
        textFieldFct.requestFocusInWindow();
        
        couleurSelect=coul ;
        drawFct();
        if(mapDesFonctions.get(coul) != null)
            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                mapDesSpinners.get(param).setFocus();
            }
        repaint() ;
    }

    public void calculTousK() {
        for (Color coul:mapDesFonctions.keySet()) {
            if (mapDesFonctions.get(coul)!=null) {
                // System.out.print("calculTousK ->");
                mapDesFonctions.get(coul).majRF();
            }
        }
    }


   public void setGraphMode(char graphMode){
       if (zoneDeTrace != null){
           zoneDeTrace.setGraphMode(graphMode);
       }
   }

   public char updateGraphMode(){
       if (zoneDeTrace != null){
           return zoneDeTrace.updateGraphMode();
       }
       return DataConstants.MODE_DEFAULT;
   }

   public void displayFunctionModel(){
       if (isPanelFunction){
           this.remove(panelFctModel);
           panelFctModel = null;
           panelFct = null;
           panelFctParam = null;
           for(int d=0; d<tabPanelDist.length; d++){
               tabPanelDist[d] = null;
           }
           panelDist = null;
           parametresFn = null;
           scrollPaneParametresFn = null;
       }else{
           this.add(getPanelFctModel(), BorderLayout.NORTH);
       }
       if(mapDesFonctions.size() > 0)
           drawFct();
       isPanelFunction = !isPanelFunction;
       formComponentResized(null);
       revalidate();
       repaint();
   }

   public boolean isDisplayFunctionModel(){
       return isPanelFunction;
   }

   private JPanel getPanelFctParam(){
       if(panelFctParam == null){
           panelFctParam = new JPanel();
           panelFctParam.setName("panelFctParam");
           panelFctParam.setLayout(new BorderLayout());
           panelFctParam.add(getScrollPaneParametresFn(), BorderLayout.NORTH);
           panelFctParam.add(getPanelDist(), BorderLayout.CENTER);
       }
       return panelFctParam;
   }
   private JScrollPane getScrollPaneParametresFn(){
       if(scrollPaneParametresFn == null){
           scrollPaneParametresFn = new JScrollPane();
           scrollPaneParametresFn.setName("scrollPaneParametresFn");
           scrollPaneParametresFn.setViewportView(getParametresFn());
           scrollPaneParametresFn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
       }
       return scrollPaneParametresFn;
   }
   private JPanel getParametresFn(){
       if(parametresFn == null){
           parametresFn = new JPanel();
           parametresFn.setName("parametresFn");
           parametresFn.setLayout(new FlowLayout(FlowLayout.LEFT));
           //parametresFn.setPreferredSize(new Dimension(0,0));
       }
       return parametresFn;
   }

   /* return true if a previous param */
   public boolean isPreviousParam(){
       return previousParam != null;
   }

   /* return true if a next param */
   public boolean isNextParam(){
       return nextParam != null;
   }

   public void setPreviousParam(){
       nextParam = null;
       if(paramGraph != null)
            previousParam= (ParamGraph)paramGraph.clone();
       if(this.actionFitex != null)
           actionFitex.setPreviousZoom();
   }

   
   public void previousParam(){
       if(previousParam != null){
           nextParam = (ParamGraph)paramGraph.clone();
           setParameters(previousParam.getX_min(), previousParam.getX_max(), previousParam.getY_min(), previousParam.getY_max());
           previousParam = null;
       }else if (nextParam != null){
           previousParam = (ParamGraph)paramGraph.clone();
           setParameters(nextParam.getX_min(), nextParam.getX_max(), nextParam.getY_min(), nextParam.getY_max());
           nextParam = null;
       }
       if(zoneDeTrace != null)
           zoneDeTrace.repaint();
   }


   @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        PreDefinedFunctionDialog fctDialog = new PreDefinedFunctionDialog(owner, this,owner.getListPreDefinedFunction(), idPredefFunction);
        fctDialog.setVisible(true);
    }

   public void setPredefinedFunction(PreDefinedFunction function){
       if(textFieldFct != null){
           textFieldFct.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
           textFieldFct.setForeground(couleurSelect);
           textFieldFct.setText(function.getExpression());
           if(function.getType() == DataConstants.FUNCTION_TYPE_Y_FCT_X){
                labelFct.setText(getBundleString("LABEL_FUNCTION")+" = " );
                typeSelect = DataConstants.FUNCTION_TYPE_Y_FCT_X;
            }else{
                labelFct.setText(getBundleString("LABEL_FUNCTION_Y")+" = " );
                typeSelect = DataConstants.FUNCTION_TYPE_X_FCT_Y ;
            }
           idPredefFunction = function.getIdFunction();
           textFieldFct.postActionEvent();
       }
   }

    public void updateSize(int width, int height){
        setSize(width, height);
        setPreferredSize(getSize());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        int h = 0;
        if(panelFctParam != null && scrollPaneParametresFn != null && scrollPaneParametresFn.getHorizontalScrollBar().isShowing()){
            panelFctParam.setSize(panelFctParam.getWidth(), panelFctParam.getHeight()+20);
        }
        if (panelFctModel != null){
            h = (int)panelFctModel.getPreferredSize().getHeight();
        }

        if(zoneDeTrace != null)
            this.zoneDeTrace.updateSize(this.getWidth(), this.getHeight()-h);
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables





    
}
