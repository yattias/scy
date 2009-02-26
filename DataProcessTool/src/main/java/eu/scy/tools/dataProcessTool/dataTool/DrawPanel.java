/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DrawPanel.java
 *
 * Created on 1 déc. 2008, 09:42:06
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.fitex.analyseFn.Function;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.HashMap;

import eu.scy.tools.fitex.dataStruct.Expression;

/**
 * panel dessin courbe
 * @author Marjolaine
 */
public class DrawPanel extends javax.swing.JPanel {

    // PROPERTY
    /* owner */
    private MainDataToolPanel owner;
    /* graph panel */
    private GraphPanel graphPanel;
    /* données */
    private Data[][] datas;

    // param�tres de la zone de trac�
    private Double x_min  = -10.0 ;
    private Double x_max = 10.0;
    private Double delta_x =1.0 ;
    private Double y_min =-10.0;
    private Double y_max=10.0 ;
    private Double delta_y =1.0;
    // param�tres du rectangle de zoom
    private int x_zoom1 ;
    private int y_zoom1 ;
    private int x_zoom2 ;
    private int y_zoom2;
    // parametres de la zone graphique
    //private Graphics g ;
    private int width ;
    private int height ;
    // couleur de la courbe selectionnee (initialement bleue)
    private Color couleurSelect=Color.BLUE ;
    /* indique si dessine le carré de zoom */
    private boolean isZoom;

    // stockage des fonctions
    private HashMap<Color,Function> mapDesFonctions = new HashMap<Color,Function>();
    // stockage des spinners
    private HashMap<String, BoxSpinner> mapDesSpinners = new HashMap<String, BoxSpinner>();

    private boolean updateParam = true;
    

    public DrawPanel(MainDataToolPanel owner, GraphPanel graphPanel, Data[][] datas, Double x_min, Double x_max, Double delta_x, Double y_min, Double y_max, int width, int height) {
        super();
        this.owner = owner;
        this.graphPanel = graphPanel;
        this.datas = datas;
        this.x_min = x_min;
        this.x_max = x_max;
        this.delta_x = delta_x;
        this.y_min = y_min;
        this.y_max = y_max;
        this.width = width;
        this.height = height;
        isZoom = false;
        initComponents();
    }


    /** Creates new form DrawPanel */
    public DrawPanel() {
        initComponents();
    }

    /* mise à jour des parametres */
    public void updateParam(){
        graphPanel.recupererParametresZdT();
    }
    /* */
    public void setParam(double x_min, double x_max, double delta_x, double y_min, double y_max, double delta_y){
        this.x_min = x_min;
        this.x_max = x_max;
        this.delta_x = delta_x;
        this.y_min = y_min;
        this.y_max = y_max;
        this.updateParam = true;
    }

    public void setUpdateParam(boolean updateParam) {
        this.updateParam = updateParam;
    }

   

    @Override
    public void paint(Graphics g){
       super.paint(g);
       effacerZone(g);
       tracerZone(g);
       if(isZoom){
        traceZoom(g);

       }else{
            // efface le rectangle de zoom
            g.setColor(Color.WHITE) ;
            g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
            tracerZone(g);
        }
        
    }

    /* trace zoom */
    private void traceZoom(Graphics g){
        g.setColor(Color.RED) ;
        g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
    }

    public void setData(Data[][] datas) {
        this.datas = datas;
    }

    public void setMapDesFonctions(HashMap<Color, Function> mapDesFonctions) {
        this.mapDesFonctions = mapDesFonctions;
    }

    public void updateSize(int width, int height){
        this.width = width;
        this.height = height;
        repaint();
    }
    
    /** m�thode pour retracer int�gralement la zone de trac�
     * penser � appeler la methode effacer() avant cette methode si besoin
     */
    public void tracerZone(Graphics g) {
        Double x;
        Double y;
        Boolean ignore;

        /*if (updateParam)
            updateParam();
        else
            updateParam = true;
         * */
        // trac� des axes et des graduations
        tracerAxes(g);

        // parcours et tracé pour toutes les courbes définies dans la HashMap
        for (Color coul:mapDesFonctions.keySet()) {
            if (mapDesFonctions.get(coul)!=null && mapDesFonctions.get(coul).getExpression()!=null) {
                tracerUneCourbe(g, coul, (mapDesFonctions.get(coul)).getExpression()) ;
            }
        }
        // parcours et trac� de tous les points d�finis dans le tableau de donn�es
        // la tableModel du tableau qui contient les donnees :
        //DefaultTableModel tableModel = data.getTableModel();
        int nbR = datas.length;
        for (int i=0; i<nbR; i++) {
            // r�cup�ration des valeurs de la ligne
            //x=(Double)tableModel.getValueAt(i,0);
            if (datas[i] != null && datas[i][0] != null &&  datas[i][1] != null){
                x=datas[i][0].getValue() ;
                //y=(Double)tableModel.getValueAt(i,1);
                y = datas[i][1].getValue() ;
                //ignore=(Boolean)tableModel.getValueAt(i,2);
                ignore = datas[i][0].isIgnoredData() || datas[i][1].isIgnoredData() ;
                if (ignore==null) ignore=false;

                if((x!=null) && (y!=null)) {
                    // trac� du point
                    if (ignore) tracerPoint(g, DataConstants.SCATTER_PLOT_COLOR, "cross", x, y) ; // point non pris en compte
                    else tracerPoint(g, DataConstants.SCATTER_PLOT_COLOR, "circle", x, y) ; // point pris en compte
                }
            }
        }

        // MaJ des param�tres de distance
        graphPanel.affichageK(Color.BLUE) ;
        graphPanel.affichageK(GraphPanel.DARK_GREEN) ;
        graphPanel.affichageK(Color.BLACK) ;
    }

 public void effacerZone(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height) ;
    }
    /** m�thode permettant de r�aliser le trac� d'un point (croix ou cercle) avec une certaine couleur*/
    public void tracerPoint(Graphics g, Color couleur, String type, double x, double y) {

        // les coordon�es des segments � tracer (coordonn�es de l'�cran)
        int xt = xToXEcran(x) ;
        int yt = yToYEcran(y) ;

        g.setColor(couleur);
        if (type.equals("circle")){
            g.drawOval(xt-2,yt-2,4,4);
        } else if (type.equals("cross") ){
            g.drawLine(xt-2,yt-2,xt+2,yt+2);
            g.drawLine(xt-2,yt+2,xt+2,yt-2);
        }
    }

    /** m�thode permettant de r�aliser le trac� d'un segment de droite avec une certaine couleur*/
    public void tracerSegment(Graphics g, Color couleur, double x1, double y1, double x2, double y2) {

        // les coordon�es des segments � tracer (coordonn�es de l'�cran)
        int xt1 = xToXEcran(x1) ;
        int xt2 = xToXEcran(x2) ;
        int yt1 = yToYEcran(y1) ;
        int yt2 = yToYEcran(y2) ;

        g.setColor(couleur);
        g.drawLine(xt1,yt1,xt2,yt2);
    }

     /** Trac� des graduations et des axes du graphique */
    public void tracerAxes(Graphics g) {

        int k0 ;
        int k1 ;

        // trac� des graduations
        // axe des X
        k0=(int)Math.ceil(x_min/delta_x) ;
        k1=(int)Math.floor(x_max/delta_x);
        for(int i=k0 ; i<=k1 ; i++) {
            tracerSegment(g, Color.LIGHT_GRAY, i*delta_x, y_min, i*delta_x, y_max);
        }

        // axe des Y
        k0=(int)Math.ceil(y_min/delta_y) ;
        k1=(int)Math.floor(y_max/delta_y);
        for(int i=k0 ; i<=k1 ; i++) {
            tracerSegment(g, Color.LIGHT_GRAY, x_min, i*delta_y, x_max, i*delta_y);
        }

        // trac� des axes
        // axe des X
        if (y_min*y_max<=0) { // la zone de trac� contient l'axe des x
            tracerSegment(g, Color.GRAY,x_min,0.0,x_max,0.0);
        }
        // axe des Y
        if (x_min*x_max<=0) { // la zone de trac� contient l'axe des y
            tracerSegment(g, Color.GRAY,0.0,y_min,0.0,y_max);
        }
    }

     /** méthode permettant de réaliser le tracé d'une courbe avec une certaine couleur*/
    public void tracerUneCourbe(Graphics g, Color couleur, Expression fonction) {
        // les coordonées des segments (coordonnées du repère)
        double x1;
        double x2;
        double y1;
        double y2;

        x1 = x_min ;
        y1 = fonction.valeur(x1) ;

        while (x1 < x_max) {
            // on calcule 2 fois plus de points que la largeur en pixels de la zone de tracé
            x2 = x1 + (x_max-x_min)/(width*2) ;
            y2 = fonction.valeur(x2) ;

            if ( !( (Double.isNaN(y1)) || (Double.isNaN(y2)) // évite les valeurs qui ne sont pas définies
            || (y1<y_min && y2>y_max)  || (y1>y_max && y2<y_min) // évite les raccord d'infinis
            || (Double.isInfinite(y1)) || (Double.isInfinite(y2)) // evite de faire des tarcés pour des valeurs infinies
            ))
                tracerSegment(g, couleur,x1,y1,x2,y2);

            x1 = x2 ;
            y1 = y2 ;
        }
    }
    

/*************************   M�thodes de traitements mathematiques **************************/

    public int xToXEcran(double x) {
        return (int)Math.round((x - x_min) * width / (x_max - x_min));
    }

    public int yToYEcran(double y) {
        return (int)Math.round((y_max - y) * height / (y_max - y_min));
    }

    public double xEcranToX(int x){
        return x_min + x/(double)width*(x_max-x_min);
    }

    public double yEcranToY(int y){
        return y_min + (height-y)/(double)height*(y_max-y_min);
    }

    /** retourne un double avec le nombre de chiffres significatifs souhait�s */
    public double chiffresSignificatifs(double x, int nbChiffres) {
        int rang = (int) Math.floor(1+Math.log10(Math.abs(x))) ;
        int power = nbChiffres - rang ;
        x =  (Math.round(x*Math.pow(10,power)) )/ (Math.pow(10,power)) ;
        return x ;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // met � jour l'affichage des coordonn�es
        formMouseMoved(evt);
        // efface le pr�c�dent rectangle
        isZoom = false;
        repaint();
        //g.setColor(Color.WHITE) ;
        //g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
        //tracerZone();
        // r�cup�re x2 et y2
        /*if(getMousePosition() == null)
            return;
        x_zoom2 = getMousePosition().x ;
        y_zoom2 = getMousePosition().y ;*/
         x_zoom2 = evt.getX() ;
        y_zoom2 = evt.getY() ;
        // affiche le rectangle correspondant � la zone de zoom
        isZoom = true;
        repaint();
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
       /* if (getMousePosition() == null)
            return;
        Double x = chiffresSignificatifs(xEcranToX(getMousePosition().x) , 3);
        Double y = chiffresSignificatifs(yEcranToY(getMousePosition().y) , 3);
        * */
        Double x = chiffresSignificatifs(xEcranToX(evt.getX()) , 3);
        Double y = chiffresSignificatifs(yEcranToY(evt.getY()) , 3);
        // format des coordonn�es
        DecimalFormat formatE = new DecimalFormat("0.#####E0");
        DecimalFormat format = new DecimalFormat("###.###");

        if ((x>-0.1 && x<0.1) || x>=10000 || x<=-10000)
            graphPanel.setCoordX(formatE.format(x));
        else
            graphPanel.setCoordX(format.format(x));
        if ((y>-0.1 && y<0.1) || y>=10000 || y<=-10000)
            graphPanel.setCoordY(formatE.format(y));
        else
            graphPanel.setCoordY(format.format(y));
    }//GEN-LAST:event_formMouseMoved

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount()==2){
            float facteurZoom = 0.5f ;
            graphPanel.setXMin(Double.toString(chiffresSignificatifs(x_min-facteurZoom*(x_max-x_min) , 3)));
            graphPanel.setXMax(Double.toString(chiffresSignificatifs(x_max+facteurZoom*(x_max-x_min) , 3)));
            graphPanel.setYMin(Double.toString(chiffresSignificatifs(y_min-facteurZoom*(y_max-y_min) , 3)));
            graphPanel.setYMax(Double.toString(chiffresSignificatifs(y_max+facteurZoom*(y_max-y_min) , 3)));
            this.updateParam() ;
            repaint();
        }
    }//GEN-LAST:event_formMouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
     
        graphPanel.setCoordX("...");
        graphPanel.setCoordY("...");
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_formMouseExited

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        
        // affiche en rouge les coordon�es
        graphPanel.setForegroundX(Color.RED) ;
        graphPanel.setForegroundY(Color.RED) ;
        // r�cup�re x1 et y1
        /*if (getMousePosition() == null)
            return;
        x_zoom1 = getMousePosition().x ;
        y_zoom1 = getMousePosition().y ;*/
        x_zoom1 = evt.getX();
        y_zoom1 = evt.getY() ;
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
       
        // r�affiche en noir les coordon�es
        graphPanel.setForegroundX(Color.BLACK) ;
        graphPanel.setForegroundY(Color.BLACK) ;
        // r�cup�re x2 et y2
        //if (getMousePosition() == null)
        //    return;

        //x_zoom2 = getMousePosition().x ;
       // y_zoom2 = getMousePosition().y ;
        x_zoom2 = evt.getX();
        y_zoom2 = evt.getY() ;
        // efface le rectangle de zoom
        isZoom = false;
        repaint();
        //g.setColor(Color.WHITE) ;
        //g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
        //tracerZone();
        // effectue le zoom si  x2!=x1 et y2!=y1
        if (x_zoom1 != x_zoom2 && y_zoom1 != y_zoom2) {
            // met � jour les coordonn�es dans les box
            graphPanel.setXMin(Double.toString(chiffresSignificatifs(xEcranToX(Math.min(x_zoom1,x_zoom2)) , 3)));
            graphPanel.setXMax(Double.toString(chiffresSignificatifs(xEcranToX(Math.max(x_zoom1,x_zoom2)) , 3)));
            graphPanel.setYMin(Double.toString(chiffresSignificatifs(yEcranToY(Math.max(y_zoom1,y_zoom2)) , 3)));
            graphPanel.setYMax(Double.toString(chiffresSignificatifs(yEcranToY(Math.min(y_zoom1,y_zoom2)) , 3)));
            this.updateParam() ;
            repaint();
        }
    }//GEN-LAST:event_formMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
