package eu.scy.tools.fitex.GUI;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphPanel.java
 *
 * Created on 27 nov. 2008, 20:41:06
 */



import eu.scy.tools.fitex.analyseFn.Function;
import eu.scy.tools.dataProcessTool.common.FunctionModel;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
public class FitexPanel extends javax.swing.JPanel {

     // definition d'une couleur vert fonce
    public static  final Color DARK_GREEN = new java.awt.Color(51, 153, 0) ;
    
    // PROPERTY
    private Locale locale;
    private ResourceBundle bundle;
    /* donnees */
    private DefaultTableModel[] datas = null;
    /* action fitexPanel */
    private ActionFitex actionFitex;

    /* zone dessinee */
    private DrawPanel zoneDeTrace = null;
    private ParamGraph paramGraph = null;
    // parametres de la zone graphique
    //private Graphics g ;
    private int width ;
    private int height ;
    // couleur de la courbe selectionnee (initialement bleue)
    private Color couleurSelect=Color.BLUE ;

    // stockage des fonctions
    private HashMap<Color,Function> mapDesFonctions = new HashMap<Color,Function>();
    // stockage des spinners
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
    private JPanel panelDist;

    private JPanel panelFctParam;
    private JPanel parametresFn;
    private JScrollPane scrollPaneParametresFn;
    private JSeparator sepDrawFct;



    
    /** Creates new form FitexPanel */
    public FitexPanel(Locale locale, DefaultTableModel[] datas, ArrayList<FunctionModel> listFunctionModel, ParamGraph pg) {
        super();
        this.locale = locale;
        this.datas = datas ;
        this.paramGraph = pg;
        tabPanelDist = new DistancePanel[datas.length];
        initGUI(listFunctionModel);
    }

    private void initGUI(ArrayList<FunctionModel> listFunctionModel){
        this.setLayout(new BorderLayout());
        // i18n
        //locale = Locale.getDefault();
        try{
            this.bundle = ResourceBundle.getBundle("FitexBundle", locale);
        }catch(MissingResourceException e){
          try{
              // par defaut on prend l'anglais
              locale = new Locale("en", "GB");
              bundle = ResourceBundle.getBundle("FitexBundle", locale);
          }catch (MissingResourceException e2){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR lors du chargement de fitex, la langue specifiee "+locale+" n'existe pas : "+e2);
            return;
            }
        }
        initComponents();
        getPanelFct();
        isPanelFunction = false;

        // functionSelector.setVisible(false);
        width = 400;
        height = 400;
        zoneDeTrace = new DrawPanel(this, datas, paramGraph, width, height) ;
        this.add(zoneDeTrace, BorderLayout.CENTER);
        //this.add(getPanelFctModel(), BorderLayout.NORTH);
        setInitialListFunction(listFunctionModel);

    }


    @Override
    public Locale getLocale(){
        return this.locale;
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
                    maJFonction(Color.BLUE);
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
                    maJFonction(DARK_GREEN);
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
                    maJFonction(Color.BLACK);
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
            textFieldFct.setForeground(java.awt.Color.BLUE);
            textFieldFct.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    drawFct();
                }
            });
            textFieldFct.addFocusListener(new java.awt.event.FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }
                @Override
                public void focusLost(FocusEvent e) {
                    drawFct();
                    for (String param:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
                        mapDesSpinners.get(param).setFocus();
                    }
                }
            });
        }
        return textFieldFct;
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
            panelDist.setLayout(new BoxLayout(panelDist, BoxLayout.Y_AXIS));
            for(int d=0; d<tabPanelDist.length; d++){
               panelDist.add(getPanelDist(d));
           }
        }
        return panelDist;
    }
    private DistancePanel getPanelDist(int id){
        if (tabPanelDist[id] == null){
            tabPanelDist[id] = new DistancePanel(this, id);
        }
        return tabPanelDist[id];
    }

    


   /**
    * Instancie l'objet ActionFitex.
    * @param action ActionFitex
    */
    public void addActionFitex(ActionFitex action){
        this.actionFitex=action;
    }


    public  ImageIcon getFitexImage(String img){
        return new ImageIcon(getClass().getResource( "/" +img));
    }

    /* affichage des erreurs*/
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


    /* retourne un message selon cle*/
    public String getBundleString(String key){
       String s = "";
        try{
            s = this.bundle.getString(key);
        }catch(Exception e){
            try{
                String msg = this.bundle.getString("ERROR_KEY");
                msg = MyUtilities.replace(msg, 0, key);
                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
            }catch(Exception e2){
                displayError(new CopexReturn("No message found !"+key, false) ,"ERROR");
             }
        }
        return s;

    }

   private boolean isRows(){
       for(int i=0; i<datas.length; i++){
           if(datas[i].getRowCount() > 0)
               return true;
       }
       return false;
   }
    /* mise a jour de fonctions initiales  */
    private void setInitialListFunction(ArrayList<FunctionModel> listFunctionModel){
        if (listFunctionModel == null)
            return;
        int nb = listFunctionModel.size() ;
        // si il n'existe pas, creation de l'objet fonction
        if (!isRows())
            return ;
        isPanelFunction = true;
        if(panelFctModel == null)
            this.add(getPanelFctModel(), BorderLayout.NORTH);
        drawFct();
        for (int i=0; i<nb; i++){
            FunctionModel fm = listFunctionModel.get(i);
            Function f = new Function(locale, fm.getDescription(), datas);
            int nbP = fm.getListParam().size();
            System.out.println("*******");
            for (int k=0; k<nbP; k++){
                System.out.println("setInitial : "+fm.getListParam().get(k).toString());
                f.setValeurParametre(fm.getListParam().get(k).getParam(), fm.getListParam().get(k).getValue());
            }
            mapDesFonctions.put(fm.getColor(), f);
            // affichage des parametres de la fonction
            affichageParametres(fm.getColor()) ;
        }
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        maJFonction(Color.BLUE);
    }
    
    /** miethode pour riecupierer les paramietres des axes et de la ZdT */
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
        actionFitex.setParam(paramGraph.getPlots(), paramGraph.isAutoscale(), x_min, x_max, paramGraph.getDeltaX(), y_min, y_max, paramGraph.getDeltaY());
    }
    

    public char getGraphMode(){
        if (zoneDeTrace != null)
            return zoneDeTrace.getGraphMode();
        else
            return DataConstants.MODE_DEFAULT ;
    }

    /** affichage des tous les K definis */
    public void affichageK(Color coul) {
        Double k;
        NumberFormat nfE = NumberFormat.getNumberInstance(locale);
        DecimalFormat formatE = (DecimalFormat)nfE;
        formatE.applyPattern("0.#####E0");
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat format = (DecimalFormat)nf;
        format.applyPattern("###.#####");
        //DecimalFormat formatE = new DecimalFormat("0.####E0");
        //DecimalFormat format = new DecimalFormat("####.#####");
        for (int d=0; d<tabPanelDist.length; d++){
            if(tabPanelDist[d] != null){
                if (mapDesFonctions.get(coul)==null || mapDesFonctions.get(coul).getRF()[d]==null)
                    tabPanelDist[d].setText(coul, "...");
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

    /** retourne un double avec le nombre de chiffres significatifs souhaites */
    public double chiffresSignificatifs(double x, int nbChiffres) {
        if(x == 0)
            return 0;
        int rang = (int) Math.floor(1+Math.log10(Math.abs(x))) ;
        int power = nbChiffres - rang ;
        x =  (Math.round(x*Math.pow(10,power)) )/ (Math.pow(10,power)) ;
        return x ;
    }

    /** methode pour recuperer la fonction et l'inserer dans les hashMaps */
    public void recupererFn() {
        // si il n'existe pas, creation de l'objet fonction
        if (!isRows())
            return ;
        if (mapDesFonctions.get(couleurSelect) == null)
            mapDesFonctions.put(couleurSelect, new Function(locale, textFieldFct.getText(), datas));
        else
            mapDesFonctions.get(couleurSelect).maJFonction(textFieldFct.getText()) ;
        // affichage des parametres de la fonction
        affichageParametres(couleurSelect) ;
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        // enregistrement memoire
        ArrayList<FunctionParam> listParam = new ArrayList();
        // parcours de tous les parametres
        for (String param:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
            double valParam = mapDesFonctions.get(couleurSelect).getMapParametre().get(param).valeur() ;
            FunctionParam p = new FunctionParam(-1, param, valParam);
            listParam.add(p);
        }
        if(actionFitex != null)
            actionFitex.setFunctionModel(textFieldFct.getText(), couleurSelect, listParam);
    }

    /** MaJ de l'affichage des parametres de la fonction */
    public void affichageParametres(Color coul){
        // calcul de la taille du panel contenant les spinners
        int heightPanel = 0;
        int widthPanel=5;
        //int widthPanel = parametresFn.getWidth();
        Dimension dim = new Dimension() ;

        // suppression des anciens parametres affiches
        parametresFn.removeAll();
        // et affichage des nouveaux
        if (mapDesFonctions.get(coul)!=null) {

            // parcours de tous les parametres pour creer les differents BoxSpinners
            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                // creation d'un objet BoxSpinner
                mapDesSpinners.put(param , new BoxSpinner(this)) ;
                // on ajoute le box et on l'initialise
                parametresFn.add(mapDesSpinners.get(param));
                mapDesSpinners.get(param).setTextLabel(param);
                double valParam = mapDesFonctions.get(coul).getMapParametre().get(param).valeur() ;
                mapDesSpinners.get(param).setValue(valParam) ;
                // on etire le panel qui accueille le box
                heightPanel = mapDesSpinners.get(param).getHauteur() ;
                heightPanel = 35;
            }
        }
        if(parametresFn.getPreferredSize().getWidth() > getWidth())
            heightPanel = 50;
        dim.setSize(getWidth(), heightPanel) ;
        scrollPaneParametresFn.setPreferredSize(dim);
        scrollPaneParametresFn.setMinimumSize(dim);
        this.repaint();
    }



    /** A chaque fois qu'un spinner est modifie, il appelle cette methode */
    public void maJParametreDansFonction(String param, double val) {
         // mise a jour de la valeur du parametre
        mapDesFonctions.get(couleurSelect).setValeurParametre(param , val) ;
        //tracer la fonction
        zoneDeTrace.repaint();
        // calculer et afficher k
        mapDesFonctions.get(couleurSelect).majRF();
        affichageK(couleurSelect);
        // parcours de tous les parametres
        ArrayList<FunctionParam> listParam = new ArrayList();
        for (String p:mapDesFonctions.get(couleurSelect).getMapParametre().keySet()) {
            double valParam = mapDesFonctions.get(couleurSelect).getMapParametre().get(p).valeur() ;
            FunctionParam fp = new FunctionParam(-1, p, valParam);
            listParam.add(fp);
        }
        if(actionFitex != null)
            actionFitex.setFunctionModel(mapDesFonctions.get(couleurSelect).getIntitule(), couleurSelect,listParam );
    }

    /** methode  appelee lors d'un changement de couleur de fonction */
    public void maJFonction(Color coul) {
        // MaJ de la fonction dans son label
        textFieldFct.setForeground(coul);
        if (mapDesFonctions.get(coul)!=null) {
            textFieldFct.setText((mapDesFonctions.get(coul)).getIntitule());
        }
        else
            textFieldFct.setText("");
        textFieldFct.requestFocusInWindow();
        
        // MaJ de la variable globale couleurSelect
        couleurSelect=coul ;
        drawFct();
        if(mapDesFonctions.get(coul) != null)
            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                mapDesSpinners.get(param).setFocus();
            }
        // faire apparaitre les parametres pour l'utilisateur
        repaint() ;
    }

     /** methode appelee par la table des donnees afin de mettre a jour le K des fonctions
     * lors de la modif d'une donnee */
    public void calculTousK() {
        for (Color coul:mapDesFonctions.keySet()) {
            if (mapDesFonctions.get(coul)!=null) mapDesFonctions.get(coul).majRF();
        }
    }


   public void setGraphMode(char graphMode){
       if (zoneDeTrace != null){
           zoneDeTrace.setGraphMode(graphMode);
       }
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
        if (panelFctModel != null){
            h = (int)panelFctModel.getPreferredSize().getHeight();
        }
        if(zoneDeTrace != null)
            this.zoneDeTrace.updateSize(this.getWidth(), this.getHeight()-h);
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables



   


    
}
