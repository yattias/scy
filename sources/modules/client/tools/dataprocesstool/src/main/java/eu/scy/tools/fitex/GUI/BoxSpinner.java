/*
 * BoxSpinner.java
 *
 * Created on 3 juin 2007, 18:01
 */

package eu.scy.tools.fitex.GUI;

import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ImageIcon;

/**
 *
 * @author  Cedric
 */
public class BoxSpinner extends javax.swing.JPanel {
    
    FitexPanel owner ;
    private Double value ;
    private double step = 0.1 ;
    
    /** Creates new form BoxSpinner */
    public BoxSpinner(FitexPanel owner) {
        this.owner = owner ;
        initComponents();
    }

    public  ImageIcon getFitexImage(String img){
        return new ImageIcon(getClass().getResource( "/" +img));
    }

    public void setFocus(){
        this.champValeur.requestFocusInWindow();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        champValeur = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        next = new javax.swing.JButton();
        previous = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label.setText("jLabel1");
        label.setMinimumSize(new java.awt.Dimension(3, 14));
        add(label);

        jPanel2.setMaximumSize(new java.awt.Dimension(2, 2));
        jPanel2.setPreferredSize(new java.awt.Dimension(2, 2));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 2, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 2, Short.MAX_VALUE)
        );

        add(jPanel2);

        champValeur.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        champValeur.setText("jTextField1");
        champValeur.setMinimumSize(new java.awt.Dimension(6, 18));
        champValeur.setPreferredSize(new java.awt.Dimension(70, 25));
        champValeur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champValeurActionPerformed(evt);
            }
        });
        champValeur.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                champValeurFocusLost(evt);
            }
        });
        add(champValeur);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        next.setIcon(getFitexImage("up.gif"));
        next.setDoubleBuffered(true);
        next.setFocusPainted(false);
        next.setIconTextGap(10);
        next.setMargin(new java.awt.Insets(0, 0, 0, 0));
        next.setMaximumSize(new java.awt.Dimension(15, 9));
        next.setMinimumSize(new java.awt.Dimension(15, 9));
        next.setPreferredSize(new java.awt.Dimension(16, 9));
        next.setSelectedIcon(getFitexImage("up.gif"));
        next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nextMousePressed(evt);
            }
        });
        jPanel1.add(next);

        previous.setIcon(getFitexImage("dn.gif"));
        previous.setFocusPainted(false);
        previous.setMargin(new java.awt.Insets(0, 0, 0, 0));
        previous.setMaximumSize(new java.awt.Dimension(15, 9));
        previous.setMinimumSize(new java.awt.Dimension(15, 9));
        previous.setPreferredSize(new java.awt.Dimension(16, 9));
        previous.setSelectedIcon(getFitexImage("dn.gif"));
        previous.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                previousMousePressed(evt);
            }
        });
        jPanel1.add(previous);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void previousMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousMousePressed
        value = value - step ;
        majParametre() ;
    }//GEN-LAST:event_previousMousePressed

    private void nextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMousePressed
        value = value + step ;
        majParametre() ;
    }//GEN-LAST:event_nextMousePressed

    private void champValeurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champValeurActionPerformed
       updateValue();
    }//GEN-LAST:event_champValeurActionPerformed

    private void champValeurFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_champValeurFocusLost
        updateValue();
    }//GEN-LAST:event_champValeurFocusLost
    
    public void majParametre (){
        NumberFormat nfE = NumberFormat.getNumberInstance(owner.getLocale());
        DecimalFormat formatE = (DecimalFormat)nfE;
        formatE.applyPattern("0.######E0");
        NumberFormat nf = NumberFormat.getNumberInstance(owner.getLocale());
        DecimalFormat format = (DecimalFormat)nf;
        format.applyPattern("###.#####");
        if (value>-1E-16 && value<1E-16) champValeur.setText("0");
        else if ((value>-0.09999 && value<0.099999) || value>=1E6 || value<=-1E6)
            champValeur.setText(formatE.format(value));
        else{
            int id = Double.toString(step).indexOf(".");
            if(id != -1){
                int nb = Double.toString(step).length()-id-1;
                format.setMinimumFractionDigits(nb);
            }
            champValeur.setText(format.format(value));
        }
        owner.maJParametreDansFonction(label.getText(), value);
    }
    
    /** fonction appeliee uniquement lors de l'affichage ou du reaffichage du box
     */
    public void setValue(double val) {
        value= val ;
        majParametre() ;
    }
   
    public void setTextLabel(String nomParam){
        label.setText(nomParam) ;
    }
    
    /** Modifie la valeur du pas d'incrementation en fonction de la valeur du nombre du Spinner
     *  cette fonction est appeliee ie chaque fois (spinner.getModel ?) que l'utilisateur modifie ie la main
     *  la valeur du nombre dans l'editor du spinner (spinner.getEditor) 
     */
    public void majStep(String nombre){
        
        // dietermine l'ordre de la derniiere diecimale
        int ordre = 0 ;
        
        int placeDuE = nombre.indexOf('E') ;
        if (placeDuE == -1) placeDuE = nombre.indexOf('e') ;
        int placeDuPt = nombre.indexOf('.') ;
        
        // cas des nombres avec exposant
        if (placeDuE >= 0) {
            String exp = nombre.substring(placeDuE+1) ;
            ordre = Integer.parseInt(exp) ;
        }
        // cas des nbs a virgule
        if (placeDuPt >=0){
            if (placeDuE >= 0) ordre = ordre + placeDuPt - placeDuE + 1 ;
            else ordre = ordre - nombre.length()+ placeDuPt + 1 ;
        }
        step = Math.pow(10,ordre);
        //System.out.println(step);
    }
    
    public int getHauteur(){
        // impossible de trouver le moyen d'obtenir automatiquement la hauteur de ce JSpinner
        // donc je la fixe a la main => problemes possibles d'affichage suivant les config...
        int height = 23 ;
        //height=champValeur.getHeight() ;
        //System.out.println("height = "+ height);
        return height ;
    }

    private void updateValue(){
        String s = champValeur.getText();
        s = s.replace(",", ".");
        try {
            value = Double.parseDouble(s) ;
            owner.maJParametreDansFonction(label.getText(), value);
            majStep(s);
        } catch (NumberFormatException e) {
            System.out.println("Le nombre entre n'est pas reconnu. "+s);
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_PARAM_NUMBER"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField champValeur;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label;
    private javax.swing.JButton next;
    private javax.swing.JButton previous;
    // End of variables declaration//GEN-END:variables
    
}
