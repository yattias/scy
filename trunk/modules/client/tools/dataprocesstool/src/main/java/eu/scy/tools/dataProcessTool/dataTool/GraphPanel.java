/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphPanel.java
 *
 * Created on 27 nov. 2008, 20:41:06
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.fitex.analyseFn.Function;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.FunctionModel;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marjolaine
 */
public class GraphPanel extends javax.swing.JPanel {

     // definition d'une couleur vert foncé
    public static  final Color DARK_GREEN = new java.awt.Color(51, 153, 0) ;
    
    // PROPERTY
    /* owner */
    private MainDataToolPanel owner;
    /* dataset */
    private long dbKeydDs;
     /* visulization */
    private long dbKeyVis;
    /* données */
    private Data[][] datas;

    /* zone dessinée */
    private DrawPanel zoneDeTrace;
    // param�tres de la zone de trac�
    private Double x_min ;
    private Double x_max ;
    private Double delta_x ;
    private Double y_min ;
    private Double y_max ;
    private Double delta_y ;
    private boolean autoScale;
    // parametres de la zone graphique
    //private Graphics g ;
    private int width ;
    private int height ;
    // couleur de la courbe selectionnee (initialement bleue)
    private Color couleurSelect=Color.BLUE ;

    // stockage des fonctions
    private HashMap<Color,Function> mapDesFonctions = new HashMap<Color,Function>();
    // stockage des spinners
    private HashMap<String, BoxSpinner> mapDesSpinners = new HashMap<String, BoxSpinner>();;

    
    /** Creates new form GraphPanel */
    public GraphPanel(MainDataToolPanel owner, long dbKeyDs,long dbKeyVis, Data[][] datas, ArrayList<FunctionModel> listFunctionModel, double x_min, double x_max, double delta_x, double y_min, double y_max, double delta_y, boolean autoScale) {
        super();
        this.dbKeydDs = dbKeyDs ;
        this.dbKeyVis = dbKeyVis ;
        this.owner  =owner;
        this.datas = datas ;
        this.x_min = x_min;
        this.x_max = x_max;
        this.delta_x = delta_x;
        this.y_min = y_min;
        this.y_max = y_max;
        this.delta_y = delta_y ;
        this.autoScale = autoScale;
        initComponents();
        bleu.setIcon(owner.getCopexImage("bleu_up.gif"));
        bleu.setSelectedIcon(owner.getCopexImage("bleu_dn.gif"));
        vert.setIcon(owner.getCopexImage("vert_up.gif"));
        vert.setSelectedIcon(owner.getCopexImage("vert_dn.gif"));
        noir.setIcon(owner.getCopexImage("noir_up.gif"));
        noir.setSelectedIcon(owner.getCopexImage("noir_dn.gif"));
        
        // functionSelector.setVisible(false);
        // recuperation de tous les parametres pour les traitements ulterieurs
        recupererParametresZdT();
        this.xmin.setText(x_min+"");
        this.xmax.setText(x_max+"");
        this.deltaX.setText(delta_x+"");
        this.ymin.setText(y_min+"");
        this.ymax.setText(y_max+"");
        this.deltaY.setText(delta_y+"");
        width = 400;
        height = 400;
        zoneDeTrace = new DrawPanel(owner, this, datas, x_min, x_max, delta_x, y_min, y_max, width, height) ;
        jPanel20.add(zoneDeTrace, null,0);
        setInitialListFunction(listFunctionModel);
    }

    /* mise à jour de fonctions initiales  */
    private void setInitialListFunction(ArrayList<FunctionModel> listFunctionModel){
        if (listFunctionModel == null)
            return;
        int nb = listFunctionModel.size() ;
        // si il n'existe pas, création de l'objet fonction
        if (this.datas.length == 0)
            return ;
        Object[][] tabData = new Object[this.datas.length][3];
        for (int i=0; i<this.datas.length; i++){
            for (int j=0; j<2; j++){
                tabData[i][j] = this.datas[i][j].getValue() ;
            }
            tabData[i][2] = this.datas[i][0].isIgnoredData() || this.datas[i][1].isIgnoredData() ;
        }
        // colName n'a pas d'importance ici
        String[] colNames = new String[3];
        DefaultTableModel tableModel = new DefaultTableModel(tabData, colNames);
        for (int i=0; i<nb; i++){
            FunctionModel fm = listFunctionModel.get(i);
            mapDesFonctions.put(fm.getColor(), new Function(owner.getLocale(), fm.getDescription(), tableModel));
            // affichage des paramètres de la fonction
            affichageParametres(fm.getColor()) ;
        }
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        maJFonction(Color.BLUE);
    }
    
    /** m�thode pour r�cup�rer les param�tres des axes et de la ZdT */
    public void recupererParametresZdT() {
        double oldXMax = x_max == null ? 0: x_max;
        double oldYMax = y_max == null ? 0 : y_max ;
        double oldXMin = x_min == null ? 0: x_min;
        double oldYMin = y_min == null ? 0 : y_min ;
        try{
            x_min = Double.parseDouble(xmin.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            this.setXMin(""+x_min);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }
        try{
            x_max = Double.parseDouble(xmax.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            this.setXMax(""+x_max);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }
        try{
            delta_x = Double.parseDouble(deltaX.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            deltaX.setText(""+delta_x);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }
        try{
            y_min = Double.parseDouble(ymin.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            this.setYMin(""+y_min);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }
        try{
            y_max = Double.parseDouble(ymax.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            this.setYMax(""+y_max);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }
        try{
            delta_y = Double.parseDouble(deltaY.getText()) ;
        }catch (NumberFormatException e) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            deltaY.setText(""+delta_y);
            if (zoneDeTrace != null)
                this.zoneDeTrace.setUpdateParam(false);
            repaint();
            return;
        }

        if (x_min>=x_max || y_min>=y_max) {
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                setXMax(""+oldXMax);
                setYMax(""+oldYMax);
                setXMin(""+oldXMin);
                setYMin(""+oldYMin);
                this.zoneDeTrace.setUpdateParam(false);
                repaint();
                return;
        }
        /*try {
            // les coordon�es
            x_min = Double.parseDouble(xmin.getText()) ;
            x_max = Double.parseDouble(xmax.getText()) ;
            delta_x = Double.parseDouble(deltaX.getText()) ;
            y_min = Double.parseDouble(ymin.getText()) ;
            y_max = Double.parseDouble(ymax.getText()) ;
            delta_y = Double.parseDouble(deltaY.getText()) ;

            if (x_min>=x_max || y_min>=y_max) {
                zoneDeTrace.effacerZone();
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (zoneDeTrace != null)
                zoneDeTrace.noRepaint(false);

        } catch (NumberFormatException e) {
             if (zoneDeTrace != null)
                 zoneDeTrace.noRepaint(true);
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }*/
        
        if (zoneDeTrace != null)
            zoneDeTrace.setParam(x_min, x_max, delta_x, y_min, y_max, delta_y) ;
        
    }

    /* met les coord X à jour */
    public void setCoordX(String s){
        coordX.setText(s);
        coordX.repaint();
    }
    /* met les coord Y à jour */
    public void setCoordY(String s){
        coordY.setText(s);
        coordY.repaint();
    }

    /* met les foreground X à jour */
    public void setForegroundX(Color c){
        coordX.setForeground(c);
    }
    /* met les foreground X à jour */
    public void setForegroundY(Color c){
        coordY.setForeground(c);
    }

    /* met à jour xmin*/
    public void setXMin(String s){
        xmin.setText(s);
        xmin.repaint();
    }
    /* met à jour xmax*/
    public void setXMax(String s){
        xmax.setText(s);
        xmax.repaint();
    }
    /* met à jour ymin*/
    public void setYMin(String s){
        ymin.setText(s);
        ymin.repaint();
    }
    /* met à jour ymax*/
    public void setYMax(String s){
        ymax.setText(s);
        ymax.repaint();
    }


    /** affichage des tous les K definis */
    public void affichageK(Color coul) {

        Double k;
        DecimalFormat formatE = new DecimalFormat("0.###E0");
        DecimalFormat format = new DecimalFormat("####.####");
        javax.swing.JLabel jLab ;

        if (coul == Color.BLUE) jLab = kBleu ;
        else if (coul == DARK_GREEN ) jLab = kVert ;
        else jLab = kNoir ;

        if (mapDesFonctions.get(coul)==null || mapDesFonctions.get(coul).getRF()==null)
            jLab.setText("...");
        else {
            k = (mapDesFonctions.get(coul)).getRF() ;
            k = chiffresSignificatifs(k,3) ;
            if ((k>-0.1 && k<0.1) || k>=1000 || k<=-1000) jLab.setText(formatE.format(k));
            else jLab.setText(format.format(k));
        }
    }

    /** retourne un double avec le nombre de chiffres significatifs souhaités */
    public double chiffresSignificatifs(double x, int nbChiffres) {
        int rang = (int) Math.floor(1+Math.log10(Math.abs(x))) ;
        int power = nbChiffres - rang ;
        x =  (Math.round(x*Math.pow(10,power)) )/ (Math.pow(10,power)) ;
        return x ;
    }

    /** méthode pour récupérer la fonction et l'insérer dans les hashMaps */
    public void recupererFn() {
        // si il n'existe pas, création de l'objet fonction
        if (this.datas.length == 0)
            return ;
        Object[][] tabData = new Object[this.datas.length][3];
        for (int i=0; i<this.datas.length; i++){
            for (int j=0; j<2; j++){
                tabData[i][j] = this.datas[i][j].getValue() ;
            }
            tabData[i][2] = this.datas[i][0].isIgnoredData() || this.datas[i][1].isIgnoredData() ;
        }
        // colName n'a pas d'importance ici
        String[] colNames = new String[3];
        DefaultTableModel tableModel = new DefaultTableModel(tabData, colNames);
        if (mapDesFonctions.get(couleurSelect) == null)
            mapDesFonctions.put(couleurSelect, new Function(owner.getLocale(), fonction.getText(), tableModel));
        else
            mapDesFonctions.get(couleurSelect).maJFonction(fonction.getText()) ;
        // affichage des paramètres de la fonction
        affichageParametres(couleurSelect) ;
        zoneDeTrace.setMapDesFonctions(mapDesFonctions);
        // enregistrement memoire
        owner.setFunctionModel(fonction.getText(), couleurSelect);
    }

    /** MaJ de l'affichage des paramètres de la fonction */
    public void affichageParametres(Color coul){
        // calcul de la taille du panel contenant les spinners
        int heightPanel = 5;
        int widthPanel = parametresFn.getWidth();
        Dimension dim = new Dimension() ;

        // suppression des anciens paramètres affichés
        parametresFn.removeAll();
        // et affichage des nouveaux
        if (mapDesFonctions.get(coul)!=null) {

            // parcours de tous les paramètres pour créer les différents BoxSpinners
            for (String param:mapDesFonctions.get(coul).getMapParametre().keySet()) {
                // création d'un objet BoxSpinner
                mapDesSpinners.put(param , new BoxSpinner(this)) ;
                // on ajoute le box et on l'initialise
                parametresFn.add(mapDesSpinners.get(param));
                mapDesSpinners.get(param).setTextLabel(param);
                double valParam = mapDesFonctions.get(coul).getMapParametre().get(param).valeur() ;
                mapDesSpinners.get(param).setValue(valParam) ;
                // on etire le panel qui accueille le box
                heightPanel = heightPanel + mapDesSpinners.get(param).getHauteur() ;
            }
        }
        dim.setSize(widthPanel, heightPanel) ;
        parametresFn.setPreferredSize(dim);
        parametresFn.setMinimumSize(dim);
        jPanel4.repaint();
    }

    /** A chaque fois qu'un spinner est modifié, il appelle cette methode */
    public void maJParametreDansFonction(String param, double val) {
        // mise a jour de la valeur du parametre
        mapDesFonctions.get(couleurSelect).setValeurParametre(param , val) ;
        //tracer la fonction
        zoneDeTrace.repaint();
        // calculer et afficher k
        mapDesFonctions.get(couleurSelect).majRF();
        affichageK(couleurSelect);
    }

    /** méthode  appelée lors d'un changement de couleur de fonction */
    public void maJFonction(Color coul) {
        // MaJ de la fonction dans son label
        fonction.setForeground(coul);
        if (mapDesFonctions.get(coul)!=null) {
            fonction.setText((mapDesFonctions.get(coul)).getIntitule());
        }
        else fonction.setText("");
        fonction.requestFocusInWindow();

        // MaJ de la variable globale couleurSelect
        couleurSelect=coul ;

        // faire apparaitre les parametres pour l'utilisateur
        repaint() ;
    }

    /* maj des parametres */
    public void setParameters(double x_min, double x_max, double deltaX, double y_min, double y_max, double deltaY){
        setXMin(""+x_min);
        setYMin(""+y_min);
        setYMax(""+x_max);
        setYMax(""+x_max);
        this.deltaX.setText(""+deltaX);
        this.deltaY.setText(""+deltaY);
        recupererParametresZdT();
    }
    /* maj autoscale*/
    private void setAutoScaling(){
        owner.setAutoScale(this.dbKeydDs, this.dbKeyVis, this.cbAutoScaling.isSelected());
    }

    /* mise à jour echelle automatique */
    public boolean isAutomaticScale(){
        return this.cbAutoScaling.isSelected() ;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel14 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        coordX = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        coordY = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        kBleu = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        kVert = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        kNoir = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        parametresFn = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        tracer = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        xmin = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        xmax = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        deltaX = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ymin = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ymax = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        deltaY = new javax.swing.JTextField();
        cbAutoScaling = new javax.swing.JCheckBox();
        buttonRefresh = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        bleu = new javax.swing.JRadioButton();
        vert = new javax.swing.JRadioButton();
        noir = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fonction = new javax.swing.JTextField();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel14.setMinimumSize(new java.awt.Dimension(210, 200));
        jPanel14.setLayout(new java.awt.BorderLayout(10, 10));

        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel2.setMinimumSize(new java.awt.Dimension(59, 25));
        jPanel2.setPreferredSize(new java.awt.Dimension(54, 25));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText(owner.getBundleString("LABEL_X")+" = ");
        jPanel2.add(jLabel8);

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setMinimumSize(new java.awt.Dimension(60, 20));
        jPanel15.setRequestFocusEnabled(false);

        coordX.setText("...");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coordX)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(coordX)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel15);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText(owner.getBundleString("LABEL_Y")+" = ");
        jPanel2.add(jLabel10);

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel16.setMinimumSize(new java.awt.Dimension(60, 20));

        coordY.setText("...");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coordY)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(coordY)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel16);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(owner.getBundleString("LABEL_DISTANCE")+" : ");
        jPanel2.add(jLabel11);

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setMinimumSize(new java.awt.Dimension(60, 20));

        kBleu.setForeground(java.awt.Color.blue);
        kBleu.setText("...");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kBleu)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(kBleu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel19);

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setMinimumSize(new java.awt.Dimension(60, 20));

        kVert.setForeground(new java.awt.Color(51, 153, 0));
        kVert.setText("...");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kVert)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(kVert)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel21);

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel22.setMinimumSize(new java.awt.Dimension(60, 20));

        kNoir.setText("...");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kNoir)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(kNoir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel22);

        jPanel20.add(jPanel2);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 301));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        parametresFn.setMaximumSize(new java.awt.Dimension(1000, 1000));
        parametresFn.setMinimumSize(new java.awt.Dimension(57, 5));
        parametresFn.setPreferredSize(new java.awt.Dimension(120, 5));
        parametresFn.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanel4.add(parametresFn);

        jPanel17.setMaximumSize(new java.awt.Dimension(2147483647, 3544));
        jPanel17.setMinimumSize(new java.awt.Dimension(100, 291));
        jPanel17.setPreferredSize(new java.awt.Dimension(120, 443));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 8, 1));
        jPanel6.setMaximumSize(new java.awt.Dimension(59, 32));
        jPanel6.setOpaque(false);
        jPanel6.setRequestFocusEnabled(false);
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        tracer.setText(owner.getBundleString("BUTTON_DRAW"));
        tracer.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tracer.setMaximumSize(new java.awt.Dimension(77, 23));
        tracer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tracerActionPerformed(evt);
            }
        });
        jPanel6.add(tracer);

        jPanel17.add(jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(owner.getBundleString("LABEL_AXIS")));
        jPanel7.setMinimumSize(new java.awt.Dimension(120, 226));
        jPanel7.setPreferredSize(new java.awt.Dimension(120, 168));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 2, 4));
        jPanel8.setMinimumSize(new java.awt.Dimension(28, 20));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText(owner.getBundleString("LABEL_XMIN"));
        jLabel2.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel8.add(jLabel2);

        xmin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xmin.setText("-10");
        xmin.setMaximumSize(new java.awt.Dimension(256, 20));
        xmin.setMinimumSize(new java.awt.Dimension(59, 20));
        xmin.setPreferredSize(new java.awt.Dimension(59, 20));
        xmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xminActionPerformed(evt);
            }
        });
        xmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                xminKeyPressed(evt);
            }
        });
        jPanel8.add(xmin);

        jPanel7.add(jPanel8);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 2, 4));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText(owner.getBundleString("LABEL_XMAX"));
        jLabel3.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel9.add(jLabel3);

        xmax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xmax.setText("10");
        xmax.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        xmax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xmaxActionPerformed(evt);
            }
        });
        xmax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                xmaxKeyPressed(evt);
            }
        });
        jPanel9.add(xmax);

        jPanel7.add(jPanel9);

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 6, 4));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText(owner.getBundleString("LABEL_DELTAX"));
        jLabel4.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel10.add(jLabel4);

        deltaX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        deltaX.setText("1");
        deltaX.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        deltaX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deltaXActionPerformed(evt);
            }
        });
        deltaX.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deltaXKeyPressed(evt);
            }
        });
        jPanel10.add(deltaX);

        jPanel7.add(jPanel10);

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 2, 4));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText(owner.getBundleString("LABEL_YMIN"));
        jLabel5.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel11.add(jLabel5);

        ymin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ymin.setText("-10");
        ymin.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        ymin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yminActionPerformed(evt);
            }
        });
        ymin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yminKeyPressed(evt);
            }
        });
        jPanel11.add(ymin);

        jPanel7.add(jPanel11);

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 2, 4));
        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText(owner.getBundleString("LABEL_YMAX"));
        jLabel6.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel6.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel6.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel12.add(jLabel6);

        ymax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ymax.setText("10");
        ymax.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        ymax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ymaxActionPerformed(evt);
            }
        });
        ymax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ymaxKeyPressed(evt);
            }
        });
        jPanel12.add(ymax);

        jPanel7.add(jPanel12);

        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 2, 4));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText(owner.getBundleString("LABEL_DELTAY"));
        jLabel7.setMaximumSize(new java.awt.Dimension(32, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(32, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(32, 14));
        jPanel13.add(jLabel7);

        deltaY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        deltaY.setText("1");
        deltaY.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        deltaY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deltaYActionPerformed(evt);
            }
        });
        deltaY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deltaYKeyPressed(evt);
            }
        });
        jPanel13.add(deltaY);

        jPanel7.add(jPanel13);

        cbAutoScaling.setSelected(this.autoScale);
        cbAutoScaling.setText(owner.getBundleString("LABEL_AUTOSCALING"));
        cbAutoScaling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutoScalingActionPerformed(evt);
            }
        });
        jPanel7.add(cbAutoScaling);

        buttonRefresh.setText(owner.getBundleString("BUTTON_REFRESH"));
        buttonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshActionPerformed(evt);
            }
        });
        jPanel7.add(buttonRefresh);

        jPanel17.add(jPanel7);

        jPanel4.add(jPanel17);

        jPanel14.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel1.setBackground(new java.awt.Color(223, 223, 223));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        bleu.setSelected(true);
        bleu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bleu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bleu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bleuActionPerformed(evt);
            }
        });
        jPanel1.add(bleu);

        vert.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        vert.setMargin(new java.awt.Insets(0, 0, 0, 0));
        vert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vertActionPerformed(evt);
            }
        });
        jPanel1.add(vert);

        noir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        noir.setMargin(new java.awt.Insets(0, 0, 0, 0));
        noir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noirActionPerformed(evt);
            }
        });
        jPanel1.add(noir);

        jPanel3.setBackground(new java.awt.Color(223, 223, 223));
        jPanel3.setPreferredSize(new java.awt.Dimension(2, 1));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);

        jLabel1.setText(owner.getBundleString("LABEL_FUNCTION")+" = " );
        jPanel1.add(jLabel1);

        fonction.setForeground(java.awt.Color.blue);
        fonction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fonctionActionPerformed(evt);
            }
        });
        jPanel1.add(fonction);

        jPanel14.add(jPanel1, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tracerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tracerActionPerformed
        recupererFn();
        zoneDeTrace.repaint();
}//GEN-LAST:event_tracerActionPerformed

    private void xminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xminActionPerformed
        //repaint();
}//GEN-LAST:event_xminActionPerformed

    private void xmaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmaxActionPerformed
       //repaint();
}//GEN-LAST:event_xmaxActionPerformed

    private void deltaXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deltaXActionPerformed
        //repaint();
}//GEN-LAST:event_deltaXActionPerformed

    private void yminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yminActionPerformed
        //repaint();
}//GEN-LAST:event_yminActionPerformed

    private void ymaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ymaxActionPerformed
        //repaint();
}//GEN-LAST:event_ymaxActionPerformed

    private void deltaYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deltaYActionPerformed
        //repaint();
}//GEN-LAST:event_deltaYActionPerformed

    private void bleuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bleuActionPerformed
        maJFonction(Color.BLUE);
}//GEN-LAST:event_bleuActionPerformed

    private void vertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vertActionPerformed
        maJFonction(DARK_GREEN);
}//GEN-LAST:event_vertActionPerformed

    private void noirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noirActionPerformed
        maJFonction(Color.BLACK);
        // récupère l'étiquette du bouton
        // System.out.println(evt.getActionCommand());
}//GEN-LAST:event_noirActionPerformed

    private void fonctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fonctionActionPerformed
        tracer.doClick();
}//GEN-LAST:event_fonctionActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        this.zoneDeTrace.updateSize(jPanel20.getWidth(), jPanel20.getHeight()-jPanel2.getHeight());
    }//GEN-LAST:event_formComponentResized

    private void xminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xminKeyPressed
       
    }//GEN-LAST:event_xminKeyPressed

    private void xmaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xmaxKeyPressed
        
    }//GEN-LAST:event_xmaxKeyPressed

    private void deltaXKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deltaXKeyPressed
        
    }//GEN-LAST:event_deltaXKeyPressed

    private void yminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yminKeyPressed
        
    }//GEN-LAST:event_yminKeyPressed

    private void ymaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ymaxKeyPressed
        
    }//GEN-LAST:event_ymaxKeyPressed

    private void deltaYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deltaYKeyPressed
        
    }//GEN-LAST:event_deltaYKeyPressed

    private void buttonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshActionPerformed
        this.recupererParametresZdT();
        owner.setParamGraph(dbKeydDs, dbKeyVis, isAutomaticScale(), x_min, x_max, delta_x, y_min, y_max, delta_y);
        this.zoneDeTrace.repaint();
    }//GEN-LAST:event_buttonRefreshActionPerformed

    private void cbAutoScalingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAutoScalingActionPerformed
        setAutoScaling();
    }//GEN-LAST:event_cbAutoScalingActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bleu;
    private javax.swing.JButton buttonRefresh;
    private javax.swing.JCheckBox cbAutoScaling;
    private javax.swing.JLabel coordX;
    private javax.swing.JLabel coordY;
    private javax.swing.JTextField deltaX;
    private javax.swing.JTextField deltaY;
    private javax.swing.JTextField fonction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel kBleu;
    private javax.swing.JLabel kNoir;
    private javax.swing.JLabel kVert;
    private javax.swing.JRadioButton noir;
    private javax.swing.JPanel parametresFn;
    private javax.swing.JButton tracer;
    private javax.swing.JRadioButton vert;
    private javax.swing.JTextField xmax;
    private javax.swing.JTextField xmin;
    private javax.swing.JTextField ymax;
    private javax.swing.JTextField ymin;
    // End of variables declaration//GEN-END:variables



   


    
}
