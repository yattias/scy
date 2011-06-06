package eu.scy.client.tools.fitex.GUI;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DrawPanel.java
 *
 * Created on 13 janv. 2009, 13:25:34
 */



import eu.scy.client.tools.dataProcessTool.common.ParamGraph;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.client.tools.fitex.analyseFn.Function;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.HashMap;

import eu.scy.client.tools.fitex.dataStruct.Expression;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * draw panel
 * @author Marjolaine
 */
public class DrawPanel extends javax.swing.JPanel {
    
    /*font axes */
    private final static Font axisFont = new Font("dialog", Font.ITALIC, 10);
    private final static Font axisNameFont = new Font("dialog", Font.BOLD, 10);
    /* font coord */
    private final static Font coordFont = new Font("dialog", Font.BOLD, 11);
    private Color coordColor = Color.GRAY;
    /* graph panel */
    private FitexPanel fitexPanel;
    /* data */
    private DefaultTableModel[] datas = null;
    /* plots color*/
    private Color[] plotsColor = null;
    private char right;

    //zoom mode or move   mode
    private char graphMode;

    // parameters of graph
    private Double x_min  = -10.0 ;
    private Double x_max = 10.0;
    private Double delta_x =1.0 ;
    private Double y_min =-10.0;
    private Double y_max=10.0 ;
    private Double delta_y =1.0;
    // axes name
    private ArrayList<String> x_axisName;
    private ArrayList<Color> x_color;
    private ArrayList<String> y_axisName;
    private ArrayList<Color> y_color;
    private String x_axisUnit;
    private String y_axisUnit;
    // parameters of the zoom rectangle
    private int x_zoom1 ;
    private int y_zoom1 ;
    private int x_zoom2 ;
    private int y_zoom2;
    // graph zone parameters
    //private Graphics g ;
    private int width ;
    private int height ;
    // color of the selected curve (by default blue)
    private Color couleurSelect=DataConstants.FUNCTION_COLOR_1 ;
    /* show the zoom or not */
    private boolean isZoom;
    // move :
    private int x_move1;
    private int y_move1;
    private int x_move2;
    private int y_move2;


    // functions
    private HashMap<Color,Function> mapDesFonctions = new HashMap<Color,Function>();
    // spinners
    private HashMap<String, BoxSpinner> mapDesSpinners = new HashMap<String, BoxSpinner>();

    // show coord.
    private String coordX="";
    private String coordY="";
    private int posx;
    private int posy;

    private DecimalFormat decimalFormat;


    public DrawPanel(FitexPanel fitexPanel, DefaultTableModel[] datas, Color[] plotsColor, ParamGraph pg, int width, int height, char right) {
        super();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(fitexPanel.getLocale());
        this.decimalFormat = (DecimalFormat)numberFormat;
        this.fitexPanel = fitexPanel;
        this.datas = datas;
        this.right = right;
        this.plotsColor = plotsColor;
        setParamWithoutRepaint(pg);
        this.width = width;
        this.height = height;
        isZoom = false;
        this.graphMode = DataConstants.MODE_DEFAULT;
        initComponents();
        setPreferredSize(getSize());
    }


    /** Creates new form DrawPanel */
    public DrawPanel() {
        initComponents();
    }

   
    /* */
    private void setParamWithoutRepaint(ParamGraph pg){
        this.x_axisUnit = "";
        if(!pg.getPlot1().getHeaderX().isUnitNull())
            this.x_axisUnit = "("+pg.getPlot1().getHeaderX().getUnit()+")";
        this.y_axisUnit = "";
        if(!pg.getPlot1().getHeaderY().isUnitNull())
            this.y_axisUnit = "("+pg.getPlot1().getHeaderY().getUnit()+")";
        this.x_axisName = new ArrayList();
        this.x_color = new ArrayList();
        this.y_axisName = new ArrayList();
        this.y_color = new ArrayList();
        for(int d=0; d<datas.length; d++){
            String s = pg.getPlots().get(d).getHeaderX().getValue();
            int id = x_axisName.indexOf(s);
            if(id == -1){
                x_axisName.add(s);
                x_color.add(plotsColor[d]);
            }else{
                x_color.set(id, Color.GRAY);
            }
            s = pg.getPlots().get(d).getHeaderY().getValue();
            id = y_axisName.indexOf(s);
            if(id == -1){
                y_axisName.add(s);
                y_color.add(plotsColor[d]);
            }else{
                y_color.set(id, Color.GRAY);
            }
        }
        this.x_min = pg.getX_min();
        this.x_max = pg.getX_max();
        this.delta_x = pg.getDeltaX();
        this.y_min = pg.getY_min();
        this.y_max = pg.getY_max();
        this.delta_y = pg.getDeltaY();
    }


    public void setParam(ParamGraph pg){
        setParamWithoutRepaint(pg);
        repaint();
    }
   

    


    @Override
    public void paint(Graphics g){
       super.paint(g);
       effacerZone(g);
       tracerZone(g);
       if(isZoom){
            tracerZoom(g);
       }else{
            // hide the zoom rectangle
            g.setColor(Color.WHITE) ;
            g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
            tracerZone(g);
        }
    }

    /* paint zoom */
    private void tracerZoom(Graphics g){
        g.setColor(Color.RED) ;
        g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2));
    }

    public void setData(DefaultTableModel[] datas) {
        this.datas = datas;
    }

    public void setMapDesFonctions(HashMap<Color, Function> mapDesFonctions) {
        this.mapDesFonctions = mapDesFonctions;
    }

    public void updateSize(int width, int height){
        if(getGraphics() != null)
            effacerZone(getGraphics());
        this.width = width;
        this.height = height;
        repaint();
    }

    /** paint draw zone
     * shud call effacer() before, if needed
     */
    public void tracerZone(Graphics g) {
        Double x;
        Double y;
        Boolean ignore;

        // axes and graduations
        tracerAxes(g);

        // paint all functions
        for (Color coul:mapDesFonctions.keySet()) {
            if (mapDesFonctions.get(coul)!=null && mapDesFonctions.get(coul).getExpression()!=null) {
                tracerUneCourbe(g, coul, (mapDesFonctions.get(coul)).getType(), (mapDesFonctions.get(coul)).getExpression()) ;
            }
        }
        // paint data points
        //DefaultTableModel tableModel = data.getTableModel();
        for(int d=0; d<datas.length; d++){
            int nbR = datas[d].getRowCount();
            for (int i=0; i<nbR; i++) {
                // retrieve the values
                x=(Double)datas[d].getValueAt(i,0);
                y=(Double)datas[d].getValueAt(i,1);
                ignore=(Boolean)datas[d].getValueAt(i,2);
                Color plotColor  = plotsColor[d];
                if((x!=null) && (y!=null)) {
                    // tracie du point
                    if (ignore) tracerPoint(g, plotColor, "cross", x, y) ; // point non pris en compte
                        else tracerPoint(g, plotColor, "circle", x, y) ; // point pris en compte
                }
            }
        }

        // show coord
        tracerCoord(g);
        // update dist. parameter
        fitexPanel.affichageK(DataConstants.FUNCTION_COLOR_1) ;
        fitexPanel.affichageK(DataConstants.FUNCTION_COLOR_2) ;
        fitexPanel.affichageK(DataConstants.FUNCTION_COLOR_3) ;
    }

    public void effacerZone(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height) ;
    }

    /* show coord*/
    private void tracerCoord(Graphics g){
        int lx = MyUtilities.lenghtOfString(coordX, getFontMetrics(coordFont));
        int ly = MyUtilities.lenghtOfString(coordY, getFontMetrics(coordFont));
        int maxl = Math.max(lx, ly);
        g.setColor(coordColor);
        g.setFont(coordFont);
        int px = posx + 10;
        int py = posy ;
        // on the edges
        if(px+maxl > this.getWidth())
            px = posx-maxl-10;
        if (py - 10 <0)
            py = 10;
        else if (py +20 > this.getHeight())
            py = this.getHeight() - 20;
        g.drawString(coordX, px, py);
        g.drawString(coordY, px, (py + 15));
    }

    /** paint the point (cross or circle) with a specified color*/
    public void tracerPoint(Graphics g, Color couleur, String type, double x, double y) {

        // coord. on screen
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

    /** paint a line segment with a specified color*/
    public void tracerSegment(Graphics g, Color couleur, double x1, double y1, double x2, double y2) {

        // coord. on screen
        int xt1 = xToXEcran(x1) ;
        int xt2 = xToXEcran(x2) ;
        int yt1 = yToYEcran(y1) ;
        int yt2 = yToYEcran(y2) ;

        g.setColor(couleur);
        g.drawLine(xt1,yt1,xt2,yt2);
    }

     /** paint axes */
    public void tracerAxes(Graphics g) {
        //System.out.println("******tracerAxes : "+xToXEcran(x_min)+", "+xToXEcran(x_max)+" // "+yToYEcran(y_min)+", "+yToYEcran(y_max));
        int k0 ;
        int k1 ;

        // paint graduations
        // X axe
        if(delta_x != 0){
            k0=(int)Math.ceil(x_min/delta_x) ;
            k1=(int)Math.floor(x_max/delta_x);
            for(int i=k0 ; i<=k1 ; i++) {
                tracerSegment(g, Color.LIGHT_GRAY, i*delta_x, y_min, i*delta_x, y_max);
            }
        }

        // Y axe
        if(delta_y != 0){
            k0=(int)Math.ceil(y_min/delta_y) ;
            k1=(int)Math.floor(y_max/delta_y);
            for(int i=k0 ; i<=k1 ; i++) {
                tracerSegment(g, Color.LIGHT_GRAY, x_min, i*delta_y, x_max, i*delta_y);
            }
        }

        // axes
        // X axe
        int y= (yToYEcran(y_min)-yToYEcran(y_max))/2;
        boolean decal = false;
        boolean decalX = false;
        if (y_min*y_max<=0) { // the drawn zone contains the axe
            tracerSegment(g, Color.GRAY,x_min,0.0,x_max,0.0);
            y= yToYEcran(0)-10;
            if(y <yToYEcran(y_max)){
                y = yToYEcran(0)+10;
                decal = true;
                decalX = true;
            }
        }
        // xmin
        g.setFont(axisFont);
        g.drawString(""+decimalFormat.format(x_min), xToXEcran(x_min),y);
        // xmax
        String s = ""+decimalFormat.format(x_max) ;
        int l = MyUtilities.lenghtOfString(s, g.getFontMetrics());
        g.drawString(s, xToXEcran(x_max)-l-10, y);
        // unit axe x
        g.setFont(axisNameFont);
        l = MyUtilities.lenghtOfString(x_axisUnit, g.getFontMetrics());
        if (y_min*y_max<=0) {
            y = yToYEcran(0)+10;
            if(decal)
                y = yToYEcran(0)+20;
            if(y> yToYEcran(y_min))
                y = yToYEcran(0)-20;
        }else{
            y+=10;
        }
        g.drawString(x_axisUnit,xToXEcran(x_max)-l-5, y);
        //name axe x
        int nbDx = x_axisName.size();
        int maxL = 0;
        g.setFont(axisNameFont);
        for(int d=0; d<nbDx; d++){
            maxL = Math.max(maxL, MyUtilities.lenghtOfString(x_axisName.get(d), g.getFontMetrics()));
        }
        for(int d=0; d<nbDx;d++){
            g.setColor(x_color.get(d));
            if (y_min*y_max<=0) {
                y = yToYEcran(0)+20+10*d;
                if(decal)
                    y = yToYEcran(0)+30+10*d;
                if(y> yToYEcran(y_min))
                    y = yToYEcran(0)-30-10*d;
            }else{
                y += 10;
            }
            g.drawString(x_axisName.get(d),xToXEcran(x_max)-maxL-5, y);
        }
        // Y axe
        decal = false;
        int x= (xToXEcran(x_max)-xToXEcran(x_min))/2;
        if (x_min*x_max<=0) { // drawn zone contains Y axe
            tracerSegment(g, Color.GRAY,0.0,y_min,0.0,y_max);
            x = xToXEcran(0)+5;
            decal = false;
            if(x+l > xToXEcran(x_max)){
                x = xToXEcran(0)-l-5;
                decal = true;
            }
        }
        // ymin
        g.setColor(coordColor);
        g.setFont(axisFont);
        g.drawString(""+decimalFormat.format(y_min), x,yToYEcran(y_min));
        // ymax
        s = ""+decimalFormat.format(y_max) ;
        l = MyUtilities.lenghtOfString(s, g.getFontMetrics());
        if(x_min*x_max<=0){
            if(decalX && decal)
                x = xToXEcran(0)-l-40;
            y =yToYEcran(y_max)+10;
        }else{
            y = yToYEcran(y_max)+10;
        }
        g.drawString(s, x, y);
        // unit axe y
        g.setFont(axisNameFont);
        l = MyUtilities.lenghtOfString(y_axisUnit, g.getFontMetrics());
        if(x_min*x_max<=0){
            y = yToYEcran(y_max)+20;
            if(decal)
                y = yToYEcran(y_max)+30;
        }else{
            y = yToYEcran(y_max)+20;
        }
        g.drawString(y_axisUnit,x, y);
        //name axe y
        int nbDy = y_axisName.size();
        maxL = 0;
        g.setFont(axisNameFont);
        for(int d=0; d<nbDy; d++){
            maxL = Math.max(maxL, MyUtilities.lenghtOfString(y_axisName.get(d), g.getFontMetrics()));
        }
        for(int d=0; d<nbDy;d++){
            g.setColor(y_color.get(d));
            if(x_min*x_max<=0){
                y = yToYEcran(y_max)+30+10*d;
                if(decal)
                    y = yToYEcran(y_max)+40+10*d;
            }else{
                y = yToYEcran(y_max)+30+10*d;
            }
            g.drawString(y_axisName.get(d),x, y);
        }
    }

     /** paint function */
    public void tracerUneCourbe(Graphics g, Color couleur, char type, Expression fonction) {
        // coord.
        double x1;
        double x2;
        double y1;
        double y2;
        if(type == DataConstants.FUNCTION_TYPE_Y_FCT_X){
            x1 = x_min ;
            y1 = fonction.valeur(x1) ;
            while (x1 < x_max) {
                // calculate 2* points than the width in pixels in the drawing zone
                x2 = x1 + (x_max-x_min)/(width*2) ;
                y2 = fonction.valeur(x2) ;

                if ( !( (Double.isNaN(y1)) || (Double.isNaN(y2)) // avoid non-defined values
                || (y1<y_min && y2>y_max)  || (y1>y_max && y2<y_min) // avoid infinite pbl
                || (Double.isInfinite(y1)) || (Double.isInfinite(y2)) // avoid to paint for infinie values
                ))
                    tracerSegment(g, couleur,x1,y1,x2,y2);

                x1 = x2 ;
                y1 = y2 ;
            }
        }else{ // x=f(y)
            y1 = y_min ;
            x1 = fonction.valeur(y1) ;

            while (y1 < y_max) {
                // calculate 2* points than the width in pixels in the drawing zone
                y2 = y1 + (y_max-y_min)/(width*2) ;
                x2 = fonction.valeur(y2) ;

                if ( !( (Double.isNaN(x1)) || (Double.isNaN(x2)) // avoid non-defined values
                || (x1<x_min && x2>x_max)  || (x1>x_max && x2<x_min) // avoid infinite pbl
                || (Double.isInfinite(x1)) || (Double.isInfinite(x2)) // avoid to paint for infinie values
                ))
                    tracerSegment(g, couleur,x1,y1,x2,y2);

                y1 = y2 ;
                x1 = x2 ;
            }
        }
    }

    

/*************************   math. treatments **************************/

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

    /** returns a double with the significant number specified. */
    public double chiffresSignificatifs(double x, int nbChiffres) {
        int rang = (int) Math.floor(1+Math.log10(Math.abs(x))) ;
        int power = nbChiffres - rang ;
        x =  (Math.round(x*Math.pow(10,power)) )/ (Math.pow(10,power)) ;
        return x ;
    }


    public void setGraphMode(char graphMode){
        this.graphMode = graphMode;
        
    }

    public char updateGraphMode(){
        char g = DataConstants.MODE_ZOOM;
        if(graphMode == DataConstants.MODE_ZOOM)
            g = DataConstants.MODE_MOVE;
        setGraphMode(g);
        return g;
    }
    public char getGraphMode() {
        return graphMode;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        zoneDeTrace = new javax.swing.JPanel();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        zoneDeTrace.setBackground(new java.awt.Color(255, 255, 255));
        zoneDeTrace.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        zoneDeTrace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                zoneDeTraceMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseReleased(evt);
            }
        });
        zoneDeTrace.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                zoneDeTraceComponentResized(evt);
            }
        });
        zoneDeTrace.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                zoneDeTraceMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout zoneDeTraceLayout = new javax.swing.GroupLayout(zoneDeTrace);
        zoneDeTrace.setLayout(zoneDeTraceLayout);
        zoneDeTraceLayout.setHorizontalGroup(
            zoneDeTraceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );
        zoneDeTraceLayout.setVerticalGroup(
            zoneDeTraceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        add(zoneDeTrace, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void zoneDeTraceMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseDragged
        // update coord.
        zoneDeTraceMouseMoved(evt);
        if(right == DataConstants.NONE_RIGHT)
            return;
        if(evt == null)
            return;
        int evtX = evt.getX();
        int evtY = evt.getY();
        if(graphMode == DataConstants.MODE_ZOOM){
            // remove the last rectangle
            isZoom = false;
            repaint();
            //g.setColor(Color.WHITE) ;
            //g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
            //tracerZone();
            // riecupiere x2 et y2
            x_zoom2 = evtX ;
            y_zoom2 = evtY ;
            isZoom = true;
            repaint();
        }else if (graphMode == DataConstants.MODE_MOVE){
            //move mode
            x_move2 = evtX ;
            y_move2 = evtY ;
            if (x_move1 != x_move2 && y_move1 != y_move2) {
                // met a jour les coordonnees
                double x1 = x_min;
                double x2 = x_max;
                double y1 = y_min;
                double y2 = y_max ;
                double deltax = Math.abs(chiffresSignificatifs(xEcranToX(x_move2) - xEcranToX(x_move1) , 3));
                double deltay = Math.abs(chiffresSignificatifs(yEcranToY(y_move2) - yEcranToY(y_move1) , 3)) ;
                if (x_move2 > x_move1){
                    x1 = x_min - deltax;
                    x2 = x_max - deltax;
                }else if (x_move2 < x_move1){
                    x1 = x_min + deltax ;
                    x2 = x_max + deltax ;
                }
                if (y_move2 > y_move1){
                    y1 = y_min + deltay;
                    y2 = y_max + deltay;
                }else if (y_move2 < y_move1){
                    y1 = y_min - deltay;
                    y2 = y_max - deltay;
                }
                if(x1 < x2 && y1 < y2){
                    this.x_min = chiffresSignificatifs(x1, 3);
                    this.x_max = chiffresSignificatifs(x2, 3);
                    this.y_min = chiffresSignificatifs(y1, 3);
                    this.y_max = chiffresSignificatifs(y2, 3);
                    x_move1 =x_move2;
                    y_move1 = y_move2 ;
                    repaint();
                }
             }
        }
}//GEN-LAST:event_zoneDeTraceMouseDragged

    private void zoneDeTraceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseMoved
        if(right == DataConstants.NONE_RIGHT)
            return;
        if (evt == null){
            coordX = "";
            coordY = "";
            repaint();
             return;
         }
        int evtX = evt.getX();
        int evtY = evt.getY();
        Double x = chiffresSignificatifs(xEcranToX(evtX) , 3);
        Double y = chiffresSignificatifs(yEcranToY(evtY) , 3);
        NumberFormat nfE = NumberFormat.getNumberInstance(fitexPanel.getLocale());
        DecimalFormat formatE = (DecimalFormat)nfE;
        formatE.applyPattern("0.#####E0");
        NumberFormat nf = NumberFormat.getNumberInstance(fitexPanel.getLocale());
        DecimalFormat format = (DecimalFormat)nf;
        format.applyPattern("###.###");
        //DecimalFormat formatE = new DecimalFormat("0.#####E0");
        //DecimalFormat format = new DecimalFormat("###.###");

        if ((x>-0.1 && x<0.1) || x>=10000 || x<=-10000){
            coordX = formatE.format(x);
        }else
            coordX =format.format(x);
        if ((y>-0.1 && y<0.1) || y>=10000 || y<=-10000)
            coordY = formatE.format(y);
        else
            coordY =  format.format(y);
        posx = evtX ;
        posy = evtY;
        repaint();
//        Double x = chiffresSignificatifs(xEcranToX(evt.getX()) , 3);
//        Double y = chiffresSignificatifs(yEcranToY(evt.getY()) , 3);
        //System.out.println("coord : "+x+", "+y);
        // format des coordonniees
//        DecimalFormat formatE = new DecimalFormat("0.#####E0");
//        DecimalFormat format = new DecimalFormat("###.###");

//        if ((x>-0.1 && x<0.1) || x>=10000 || x<=-10000)
//            fitexPanel.setCoordX(formatE.format(x));
//        else
//            fitexPanel.setCoordX(format.format(x));
//        if ((y>-0.1 && y<0.1) || y>=10000 || y<=-10000)
//            fitexPanel.setCoordY(formatE.format(y));
//        else
//            fitexPanel.setCoordY(format.format(y));
}//GEN-LAST:event_zoneDeTraceMouseMoved

    private void zoneDeTraceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseClicked
        // le 12/03/10: un-zoom, quelque soit  le mode
        //if (evt.getClickCount()==2 && graphMode == DataConstants.MODE_ZOOM){
        if(right == DataConstants.NONE_RIGHT)
            return;
        if (evt.getClickCount()==2){
            float facteurZoom = 0.5f ;
            fitexPanel.setPreviousParam();
            fitexPanel.setParameters(chiffresSignificatifs(x_min-facteurZoom*(x_max-x_min) , 3),
                    chiffresSignificatifs(x_max+facteurZoom*(x_max-x_min) , 3) ,
                    chiffresSignificatifs(y_min-facteurZoom*(y_max-y_min) , 3) ,
                    chiffresSignificatifs(y_max+facteurZoom*(y_max-y_min) , 3) );
        }
}//GEN-LAST:event_zoneDeTraceMouseClicked

    private void zoneDeTraceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseEntered
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        if(graphMode == DataConstants.MODE_MOVE){
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }
}//GEN-LAST:event_zoneDeTraceMouseEntered

    private void zoneDeTraceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseExited
        coordX="";
        coordY="";
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_zoneDeTraceMouseExited

    private void zoneDeTraceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMousePressed
        if(right == DataConstants.NONE_RIGHT)
            return;
        if (evt == null)
            return;
        int evtX = evt.getX();
        int evtY = evt.getY();
        if(graphMode == DataConstants.MODE_ZOOM){
            // coord in red
            coordColor = Color.RED ;
            // gets x1 & y1
            x_zoom1 = evtX ;
            y_zoom1 = evtY ;
        }else if (graphMode == DataConstants.MODE_MOVE){
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            //move mode
            x_move1 = evtX ;
            y_move1 = evtY ;
        }
}//GEN-LAST:event_zoneDeTraceMousePressed

    private void zoneDeTraceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneDeTraceMouseReleased
        if(right == DataConstants.NONE_RIGHT)
            return;
        // coord. in black
        // gets x2 & y2
        if (evt == null)
            return;
        int evtX = evt.getX();
        int evtY = evt.getY();
        if (graphMode == DataConstants.MODE_ZOOM){
            x_zoom2 = evtX ;
            y_zoom2 = evtY ;
            // delete zoom rectangle
            isZoom = false;
            repaint();
            //g.setColor(Color.WHITE) ;
            //g.drawRect(Math.min(x_zoom1,x_zoom2) , Math.min(y_zoom1,y_zoom2) , Math.abs(x_zoom1-x_zoom2) , Math.abs(y_zoom1-y_zoom2)) ;
            //tracerZone();
            // effectue le zoom si  x2!=x1 et y2!=y1
            if (x_zoom1 != x_zoom2 && y_zoom1 != y_zoom2) {
                fitexPanel.setPreviousParam();
                // update coord.
                fitexPanel.setParameters(chiffresSignificatifs(xEcranToX(Math.min(x_zoom1,x_zoom2)) , 3),
                   chiffresSignificatifs(xEcranToX(Math.max(x_zoom1,x_zoom2)) , 3),
                   chiffresSignificatifs(yEcranToY(Math.max(y_zoom1,y_zoom2)) , 3),
                   chiffresSignificatifs(yEcranToY(Math.min(y_zoom1,y_zoom2)) , 3));
             }
        }else if (graphMode == DataConstants.MODE_MOVE){
            // move mode
            x_move2 = evtX ;
            y_move2 = evtY ;
            //if (x_move1 != x_move2 && y_move1 != y_move2) {
            if(true){
                // update coord.
                double x1 = x_min;
                double x2 = x_max;
                double y1 = y_min;
                double y2 = y_max ;
                double deltax = Math.abs(chiffresSignificatifs(xEcranToX(x_move2) - xEcranToX(x_move1) , 3));
                double deltay = Math.abs(chiffresSignificatifs(yEcranToY(y_move2) - yEcranToY(y_move1) , 3)) ;
                if (x_move2 > x_move1){
                    x1 = x_min + deltax;
                    x2 = x_max + deltax;
                }else if (x_move2 < x_move1){
                    x1 = x_min - deltax ;
                    x2 = x_max - deltax ;
                }
                if (y_move2 > y_move1){
                    y1 = y_min + deltay;
                    y2 = y_max + deltay;
                }else if (y_move2 < y_move1){
                    y1 = y_min - deltay;
                    y2 = y_max - deltay;
                }
                if(x1 < x2 && y1 < y2)
                    fitexPanel.setPreviousParam();
                    fitexPanel.setParameters(chiffresSignificatifs(x1,3),
                            chiffresSignificatifs(x2,3),
                            chiffresSignificatifs(y1,3),
                            chiffresSignificatifs(y2,3));
             }
             
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            if(graphMode == DataConstants.MODE_MOVE){
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        }
}//GEN-LAST:event_zoneDeTraceMouseReleased

    private void zoneDeTraceComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_zoneDeTraceComponentResized
//         setPreferredSize(getSize());
//         repaint();
    }//GEN-LAST:event_zoneDeTraceComponentResized

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        zoneDeTraceComponentResized(evt);
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel zoneDeTrace;
    // End of variables declaration//GEN-END:variables

}
