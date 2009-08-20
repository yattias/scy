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
import javax.swing.JTextField;
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
    private DefaultTableModel datas;
    /* action fitexPanel */
    private ActionFitex actionFitex;

    /* zone dessinee */
    private DrawPanel zoneDeTrace;
    private ParamGraph paramGraph;
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
    private JPanel panelDist;
    private JLabel labelDist;
    private JPanel panelDistBlue;
    private JPanel panelDistGreen;
    private JPanel panelDistBlack;
    private JLabel labelKBlue;
    private JLabel labelKGreen;
    private JLabel labelKBlack;




    public FitexPanel(){
        super();
        this.locale = Locale.FRANCE ;
        this.datas = new DefaultTableModel();
        this.paramGraph = new ParamGraph("", "", -10, 10, -10, 10, 1, 1, false);
        initGUI(null);
    }


    /** Creates new form FitexPanel */
    public FitexPanel(Locale locale, DefaultTableModel datas, ArrayList<FunctionModel> listFunctionModel, ParamGraph pg) {
        super();
        this.locale = locale;
        this.datas = datas ;
        this.paramGraph = pg;
        initGUI(listFunctionModel);
    }

    private void initGUI(ArrayList<FunctionModel> listFunctionModel){
        this.setLayout(new BorderLayout());
        // i18n
        locale = Locale.getDefault();
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
        isPanelFunction = true;

        // functionSelector.setVisible(false);
        width = 400;
        height = 400;
        zoneDeTrace = new DrawPanel(this, datas, paramGraph, width, height) ;
        this.add(zoneDeTrace, BorderLayout.CENTER);
        this.add(getPanelFctModel(), BorderLayout.PAGE_END);
        setInitialListFunction(listFunctionModel);
    }


    public DrawPanel getDrawPanel(){
        return this.zoneDeTrace ;
    }

    
    private JPanel getPanelFctModel(){
        if (panelFctModel == null){
            panelFctModel = new JPanel();
            panelFctModel.setName("panelFctModel");
            panelFctModel.setLayout(new BorderLayout());
            panelFctModel.add(getPanelFct(), BorderLayout.CENTER);
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
                }
            });
        }
        return textFieldFct;
    }

    private void drawFct(){
        panelFctModel.add(getPanelDist(), BorderLayout.PAGE_END);
        recupererFn();
        zoneDeTrace.setSize(zoneDeTrace.getWidth(), zoneDeTrace.getHeight() - panelDist.getHeight());
    }


    private JPanel getPanelDist(){
        if (panelDist == null){
            panelDist = new JPanel();
            panelDist.setName("panelDist");
            panelDist.setMaximumSize(new java.awt.Dimension(32767, 25));
            panelDist.setMinimumSize(new java.awt.Dimension(50, 25));
            panelDist.setPreferredSize(new java.awt.Dimension(60, 25));
            panelDist.setLayout(new FlowLayout());
            panelDist.add(getLabelDist());
            panelDist.add(getPanelDistBlue());
            panelDist.add(getPanelDistGreen());
            panelDist.add(getPanelDistBlack());
        }
        return panelDist;
    }

    private JLabel getLabelDist(){
        if(labelDist == null){
            labelDist = new JLabel();
            labelDist.setName("labelDist");
            labelDist.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            labelDist.setText(getBundleString("LABEL_DISTANCE")+" : ");
        }
        return labelDist;
    }

    private JPanel getPanelDistBlue(){
        if(panelDistBlue == null){
            panelDistBlue = new JPanel();
            panelDistBlue.setName("panelDistBlue");
            panelDistBlue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistBlue.setMinimumSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistBlueLayout = new javax.swing.GroupLayout(panelDistBlue);
            panelDistBlue.setLayout(panelDistBlueLayout);
            getLabelKBlue();
            panelDistBlueLayout.setHorizontalGroup(
                panelDistBlueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlueLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKBlue)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistBlueLayout.setVerticalGroup(
                panelDistBlueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlueLayout.createSequentialGroup()
                    .addComponent(labelKBlue)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistBlue;
    }

    private JLabel getLabelKBlue(){
        if (labelKBlue == null){
            labelKBlue = new JLabel();
            labelKBlue.setName("labelKBlue");
            labelKBlue.setForeground(java.awt.Color.BLUE);
            labelKBlue.setText("...");
        }
        return labelKBlue ;
    }

    private JPanel getPanelDistGreen(){
        if(panelDistGreen == null){
            panelDistGreen = new JPanel();
            panelDistGreen.setName("panelDistGreen");
            panelDistGreen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistGreen.setMinimumSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistGreenLayout = new javax.swing.GroupLayout(panelDistGreen);
            panelDistGreen.setLayout(panelDistGreenLayout);
            getLabelKGreen();
            panelDistGreenLayout.setHorizontalGroup(
                panelDistGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistGreenLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKGreen)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistGreenLayout.setVerticalGroup(
                panelDistGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistGreenLayout.createSequentialGroup()
                    .addComponent(labelKGreen)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistGreen;
    }

    private JLabel getLabelKGreen(){
        if (labelKGreen == null){
            labelKGreen = new JLabel();
            labelKGreen.setName("labelKGreen");
            labelKGreen.setForeground(new java.awt.Color(51, 153, 0));
            labelKGreen.setText("...");
        }
        return labelKGreen ;
    }

    private JPanel getPanelDistBlack(){
        if(panelDistBlack == null){
            panelDistBlack = new JPanel();
            panelDistBlack.setName("panelDistBlack");
            panelDistBlack.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistBlack.setMinimumSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistBlackLayout = new javax.swing.GroupLayout(panelDistBlack);
            panelDistBlack.setLayout(panelDistBlackLayout);
            getLabelKBlack();
            panelDistBlackLayout.setHorizontalGroup(
                panelDistBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlackLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKBlack)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistBlackLayout.setVerticalGroup(
                panelDistBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlackLayout.createSequentialGroup()
                    .addComponent(labelKBlack)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistBlack;
    }

    private JLabel getLabelKBlack(){
        if (labelKBlack == null){
            labelKBlack = new JLabel();
            labelKBlack.setName("labelKBlack");
            labelKBlack.setText("...");
        }
        return labelKBlack ;
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

   
    /* mise a  jour de fonctions initiales  */
    private void setInitialListFunction(ArrayList<FunctionModel> listFunctionModel){
        if (listFunctionModel == null)
            return;
        int nb = listFunctionModel.size() ;
        // si il n'existe pas, creation de l'objet fonction
        if (this.datas.getRowCount() == 0)
            return ;
        
        for (int i=0; i<nb; i++){
            FunctionModel fm = listFunctionModel.get(i);
            mapDesFonctions.put(fm.getColor(), new Function(locale, fm.getDescription(), datas));
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
        DecimalFormat formatE = new DecimalFormat("0.###E0");
        DecimalFormat format = new DecimalFormat("####.####");
        javax.swing.JLabel jLab ;

        if (coul == Color.BLUE) jLab = labelKBlue ;
        else if (coul == DARK_GREEN ) jLab = labelKGreen ;
        else jLab = labelKBlack ;

        if(jLab != null){
            if (mapDesFonctions.get(coul)==null || mapDesFonctions.get(coul).getRF()==null)
                jLab.setText("...");
            else {
                k = (mapDesFonctions.get(coul)).getRF() ;
                k = chiffresSignificatifs(k,3) ;
                if (k != 0 && (k>-0.1 && k<0.1) || k>=1000 || k<=-1000)
                    jLab.setText(formatE.format(k));
                else
                    jLab.setText(format.format(k));
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
        if (this.datas.getRowCount() == 0)
            return ;
        if (mapDesFonctions.get(couleurSelect) == null)
            mapDesFonctions.put(couleurSelect, new Function(locale, textFieldFct.getText(), datas));
        else
            mapDesFonctions.get(couleurSelect).maJFonction(textFieldFct.getText()) ;
        // affichage des parama¨tres de la fonction
        affichageParametres(couleurSelect) ;
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        // enregistrement memoire
        actionFitex.setFunctionModel(textFieldFct.getText(), couleurSelect);
    }

    /** MaJ de l'affichage des parama¨tres de la fonction */
    public void affichageParametres(Color coul){
        // calcul de la taille du panel contenant les spinners
        int heightPanel = 5;
        int widthPanel=5;
        //int widthPanel = parametresFn.getWidth();
        Dimension dim = new Dimension() ;

        // suppression des anciens parama¨tres affiches
        //parametresFn.removeAll();
        // et affichage des nouveaux
        if (mapDesFonctions.get(coul)!=null) {

            // parcours de tous les parama¨tres pour creer les differents BoxSpinners
            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                // creation d'un objet BoxSpinner
                mapDesSpinners.put(param , new BoxSpinner(this)) ;
                // on ajoute le box et on l'initialise
                //parametresFn.add(mapDesSpinners.get(param));
                mapDesSpinners.get(param).setTextLabel(param);
                double valParam = mapDesFonctions.get(coul).getMapParametre().get(param).valeur() ;
                mapDesSpinners.get(param).setValue(valParam) ;
                // on etire le panel qui accueille le box
                heightPanel = heightPanel + mapDesSpinners.get(param).getHauteur() ;
            }
        }
        dim.setSize(widthPanel, heightPanel) ;
        //parametresFn.setPreferredSize(dim);
        //parametresFn.setMinimumSize(dim);
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
    }

    /** methode  appelee lors d'un changement de couleur de fonction */
    public void maJFonction(Color coul) {
        // MaJ de la fonction dans son label
        textFieldFct.setForeground(coul);
        if (mapDesFonctions.get(coul)!=null) {
            textFieldFct.setText((mapDesFonctions.get(coul)).getIntitule());
        }
        else textFieldFct.setText("");
        textFieldFct.requestFocusInWindow();

        // MaJ de la variable globale couleurSelect
        couleurSelect=coul ;

        // faire apparaitre les parametres pour l'utilisateur
        repaint() ;
    }

     /** methode appelee par la table des donnees afin de mettre a  jour le K des fonctions
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
       }else{
           this.add(getPanelFctModel(), BorderLayout.PAGE_END);
       }
       isPanelFunction = !isPanelFunction;
       formComponentResized(null);
       revalidate();
       repaint();
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
        //System.out.println("fitex panel : "+this.getWidth()+", "+this.getHeight());

        int h = 0;
        if (panelFctModel != null)
            h = panelFct.getHeight();
        if(zoneDeTrace != null)
            this.zoneDeTrace.updateSize(this.getWidth(), this.getHeight()-h);
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables



   


    
}
