/*
 * DataTable.java
 *
 * Created on 3 mai 2007, 13:39
 */

package eu.scy.tools.fitex.GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Cedric
 */
public class DataTable extends javax.swing.JFrame
{
    
    // la variable "modele de tableau" qui va sert a modifier la forme du tableau
    private DefaultTableModel tableModel ;
    // l'objet FunctionPlotter qui a appeli申 la DataTable
    private FitexPanel fp ;
    // la boite de dialogue pour ouvrir et sauvegarder des fichiers
    final JFileChooser fc = new JFileChooser();
    
    /**
     * Creates new form DataTable
     */
    public DataTable(FitexPanel fp)
    {
        this.fp = fp ;
        initComponents();
        // on recupere le modele de table cree par NetBeans et on le caste pour acceder a + de methodes
        tableModel = (DefaultTableModel) tableau.getModel();
        // on peut ainsi modifier le tableau a travers son modele :)
        tableModel.setRowCount(10);
        // modification generique pour le fileChooser fc
        // filechooser utile pour les csv et autres
        fc.addChoosableFileFilter(new CSVFileFilter()) ;
    }
    
    public DefaultTableModel getTableModel(){
        return tableModel ;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPaneData = new javax.swing.JScrollPane();
        tableau = new javax.swing.JTable();
        panelButtons = new javax.swing.JPanel();
        buttonTracer = new javax.swing.JButton();
        buttonClose = new javax.swing.JButton();
        menuDonnees1 = new javax.swing.JMenuBar();
        dataSetMenu = new javax.swing.JMenu();
        nbPoints = new javax.swing.JMenuItem();
        ouvrir = new javax.swing.JMenuItem();
        enregistrer = new javax.swing.JMenuItem();
        initialiser = new javax.swing.JMenuItem();

        setTitle("Jeu de donn辿es");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        scrollPaneData.setPreferredSize(new java.awt.Dimension(214, 404));

        tableau.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "java.lang.Object@1196c37", "java.lang.Object@1196c37", "java.lang.Object@1196c37"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableau.setOpaque(false);
        tableau.setPreferredSize(new java.awt.Dimension(100, 100000));
        scrollPaneData.setViewportView(tableau);
        tableau.getColumnModel().getColumn(0).setHeaderValue(fp.getBundleString("LABEL_COLUMN_X"));
        tableau.getColumnModel().getColumn(1).setHeaderValue(fp.getBundleString("LABEL_COLUMN_Y"));
        tableau.getColumnModel().getColumn(2).setHeaderValue(fp.getBundleString("LABEL_COLUMN_IGNORE"));

        getContentPane().add(scrollPaneData);

        buttonTracer.setText(fp.getBundleString("BUTTON_DRAW"));
        buttonTracer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTracerActionPerformed(evt);
            }
        });
        panelButtons.add(buttonTracer);

        buttonClose.setText(fp.getBundleString("BUTTON_CLOSE"));
        buttonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseActionPerformed(evt);
            }
        });
        panelButtons.add(buttonClose);

        getContentPane().add(panelButtons);

        dataSetMenu.setText(fp.getBundleString("MENU"));
        dataSetMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataSetMenuActionPerformed(evt);
            }
        });

        nbPoints.setText(fp.getBundleString("MENU_NB_POINTS"));
        nbPoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nbPointsActionPerformed(evt);
            }
        });
        dataSetMenu.add(nbPoints);

        ouvrir.setText(fp.getBundleString("MENU_OPEN"));
        ouvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirActionPerformed(evt);
            }
        });
        dataSetMenu.add(ouvrir);

        enregistrer.setText(fp.getBundleString("MENU_SAVE_AS"));
        enregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerActionPerformed(evt);
            }
        });
        dataSetMenu.add(enregistrer);

        initialiser.setText(fp.getBundleString("MENU_INITIALIZE"));
        initialiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialiserActionPerformed(evt);
            }
        });
        dataSetMenu.add(initialiser);

        menuDonnees1.add(dataSetMenu);

        setJMenuBar(menuDonnees1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerActionPerformed
        // affichage du fc
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // recupere le fichier
            File file = fc.getSelectedFile();
            String monFichier = file.getPath() ;
            if(!monFichier.endsWith(".csv") && !monFichier.endsWith(".CSV")) {
                monFichier=monFichier+".csv";
                file = new File(monFichier) ;
            }
            try {
                // Cri申er un objet java.io.FileWriter avec comme argument le mon du fichier dans lequel enregsitrer
                FileWriter fw = new FileWriter(file) ;
                // Mettre le flux en tampon (en cache)
                BufferedWriter out = new BufferedWriter(fw);
                
                // Balancer dans le flux le contenu de la zone de texte
                Double x ;
                Double y ;
                Boolean ignore ;
                for (int i=0; i<tableModel.getRowCount(); i++) {
                    // ri申cupi申ration des valeurs de la ligne
                    x=(Double)tableModel.getValueAt(i,0);
                    y=(Double)tableModel.getValueAt(i,1);
                    ignore=(Boolean)tableModel.getValueAt(i,2);
                    if (ignore==null) ignore=false ;
                    if((x!=null) && (y!=null)) {
                        if (ignore==true)
                            out.write(x+";"+y+";1\n");
                        else
                             out.write(x+";"+y+";0\n");
                    }
                }
                out.close(); 
            }
            catch(IOException e){
                e.printStackTrace() ;
                System.out.println("Le fichier ne peut pas i申tre enregistri申");
            }
        }
    }//GEN-LAST:event_enregistrerActionPerformed

    private void ouvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirActionPerformed
        // affichage du fc
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // recupere le fichier
            File file = fc.getSelectedFile();
            effacerTableau();
            try {
                // lit le fichier et rempli la table (en ajoutant des lignes)
                BufferedReader br = new BufferedReader(new FileReader(file));
                if (br != null){
                    try{
                        String tempLine=br.readLine();
                        while (tempLine != null) {
                            Vector vect=new Vector();
                            int i=0;
                            String token ;
                            int cursorBegin = 0;
                            int cursorEnd = tempLine.indexOf(';') ;
                            while (cursorBegin > -1) {
                                if (cursorEnd == -1) {
                                    token = tempLine.substring(cursorBegin) ;
                                    i++;
                                    if (i<3) vect.add(Double.parseDouble(token));
                                    else {
                                        if (Integer.parseInt(token)==0) vect.add(false);
                                        else vect.add(true);
                                    }
                                    cursorBegin = cursorEnd;
                                }
                                else {
                                    token = tempLine.substring(cursorBegin, cursorEnd) ;
                                    i++;
                                    vect.add(Double.parseDouble(token));
                                    cursorBegin = cursorEnd + 1;
                                }
                                cursorEnd = tempLine.indexOf(';', cursorBegin);
                            }
                            if (i<3) vect.add(false);
                            tableModel.addRow(vect);
                            
                            
                            tempLine = br.readLine();
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading CSV file: " + e.toString());
                    } finally {
                        try {
                            br.close();
                        } catch (IOException e) {
                            System.err.println("Error closing CSV file: "+ e.toString());
                        }
                    }
                }

            }
            catch(IOException e) {
                e.printStackTrace() ;
                System.out.println("Le fichier ne peut pas i申tre ouvert");
            }
        }
        fp.calculTousK();
        fp.repaint();
    }//GEN-LAST:event_ouvrirActionPerformed

    private void buttonTracerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTracerActionPerformed
        fp.calculTousK();
        fp.repaint();
}//GEN-LAST:event_buttonTracerActionPerformed

    private void nbPointsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nbPointsActionPerformed
    {//GEN-HEADEREND:event_nbPointsActionPerformed
        // modifie le nombre de points i申 tracer
        String s = JOptionPane.showInputDialog(this, fp.getBundleString("LABEL_NB_POINTS")+" ",tableau.getRowCount()) ;
        if(s != null){
            int nb = Integer.parseInt(s);
            tableModel.setRowCount(nb);
        }
    }//GEN-LAST:event_nbPointsActionPerformed

    private void initialiserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_initialiserActionPerformed
    {//GEN-HEADEREND:event_initialiserActionPerformed
        int nb = tableModel.getRowCount() ;
        effacerTableau();
        tableModel.setRowCount(nb);
    }//GEN-LAST:event_initialiserActionPerformed

    private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseActionPerformed
        this.setVisible(false);
}//GEN-LAST:event_buttonCloseActionPerformed

    private void dataSetMenuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dataSetMenuActionPerformed
    {//GEN-HEADEREND:event_dataSetMenuActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_dataSetMenuActionPerformed
    
    /** supprime toutes les lignes du tableau */
    private void effacerTableau(){
        int nb = tableModel.getRowCount() ;
        for (int i = nb-1 ; i>=0 ; i--){
            tableModel.removeRow(i);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new DataTable(null).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonTracer;
    private javax.swing.JMenu dataSetMenu;
    private javax.swing.JMenuItem enregistrer;
    private javax.swing.JMenuItem initialiser;
    private javax.swing.JMenuBar menuDonnees1;
    private javax.swing.JMenuItem nbPoints;
    private javax.swing.JMenuItem ouvrir;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JScrollPane scrollPaneData;
    private javax.swing.JTable tableau;
    // End of variables declaration//GEN-END:variables
    
}
