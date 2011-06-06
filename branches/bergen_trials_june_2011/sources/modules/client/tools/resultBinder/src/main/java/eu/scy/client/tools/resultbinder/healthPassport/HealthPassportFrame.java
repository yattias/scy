/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HealthPassportFrame.java
 *
 * Created on 23 févr. 2011, 14:52:50
 */

package eu.scy.client.tools.resultbinder.healthPassport;

import eu.scy.client.tools.resultbinder.IActionResultBinder;
import eu.scy.client.tools.resultbinder.ResultBinderPanel;
import eu.scy.client.tools.resultbinder.utils.MyFileFilterXML;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * JFrame to test the health passport
 * @author Marjolaine
 */
public class HealthPassportFrame extends javax.swing.JFrame implements IActionResultBinder, ComponentListener{

    /** main panel*/
    private ResultBinderPanel myPanel;

    /** last file used for saving or loading an xml resultCard*/
    private File lastUsedFile = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    private transient SAXBuilder builder = new SAXBuilder(false);

    private void initGUI(){
        setTitle("Health Passport ");
        setLayout(new BorderLayout());
        setResizable(true);
        this.addComponentListener(this);
        Locale locale = Locale.getDefault();
        //locale = new Locale("en");
        //locale = new Locale("fr");
        //locale = new Locale("de");
        Locale.setDefault(locale);
        myPanel = new ResultBinderPanel("userName");
        myPanel.addActionResultBinder(this);
        add(myPanel, BorderLayout.CENTER);
        myPanel.setPicture("http://scy.collide.info:8080/webapp/common/filestreamer.html?username=marjolaine");
    }

    /** Creates new form HealthPassportFrame */
    public HealthPassportFrame() {
        initComponents();
        initGUI();
    }

    /* choice of the file to save the resultCard elo*/
    private void doSave(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFile != null){
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFile);
        }else{
            File file = new File(aFileChooser.getCurrentDirectory(), "healthPassport.xml");
            aFileChooser.setSelectedFile(file);
        }
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!isXMLFile(file)){
                file = new File(file.getParent(), file.getName()+".xml");
            }
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Element healthPassport = myPanel.getResultCard() ;
            lastUsedFile = file;
            OutputStreamWriter fileWriter = null;
            try{
                fileWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                xmlOutputter.output(healthPassport, fileWriter);
            }catch (IOException e){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		JOptionPane.showMessageDialog(this ,"Error while saving "+e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE);
            }finally{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (fileWriter != null)
                    try{
			fileWriter.close();
                    }catch (IOException e){
			JOptionPane.showMessageDialog(this ,"Error while saving "+e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE);
                    }
            }
        }
    }

    /* returns true if the specified file os an xml file (.xml)*/
    private static boolean isXMLFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("xml");
    }

    /* returns the extension of a file (after the '.')*/
    private static  String getExtensionFile(File file){
        int id = file.getName().lastIndexOf(".");
        if(id == -1 || id==file.getName().length()-1)
            return "";
        return file.getName().substring(id+1);
    }

    /* load an xml resultCard ELO */
    private void doLoad(){
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFile != null){
            aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFile);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!isXMLFile(file)){
                JOptionPane.showMessageDialog(this ,"Error, the file must be an xml file" , "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            lastUsedFile = file;
            // open a file
            if(lastUsedFile == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                JOptionPane.showMessageDialog(this ,"Error while loading the file :: null" , "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
        InputStreamReader fileReader = null;
        try{
            lastUsedFile = file;
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            myPanel.loadELO(doc.getRootElement());
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this ,"Error while loading elo" , "Error",JOptionPane.ERROR_MESSAGE);
        }
        finally{
            if (fileReader != null)
            try{
                fileReader.close();
            }catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this ,"Error while loading elo "+e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE);
            }
       }
        }
    }

    /* new blank ELO */
    private void doNew(){
        myPanel.newElo();
    }

   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuNew = new javax.swing.JMenuItem();
        menuLoad = new javax.swing.JMenuItem();
        menuSave = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        menuNew.setText("new");
        menuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewActionPerformed(evt);
            }
        });
        jMenu1.add(menuNew);

        menuLoad.setText("load...");
        menuLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLoadActionPerformed(evt);
            }
        });
        jMenu1.add(menuLoad);

        menuSave.setText("Save...");
        menuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveActionPerformed(evt);
            }
        });
        jMenu1.add(menuSave);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveActionPerformed
        doSave();
    }//GEN-LAST:event_menuSaveActionPerformed

    private void menuLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLoadActionPerformed
        doLoad();
    }//GEN-LAST:event_menuLoadActionPerformed

    private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
        doNew();
    }//GEN-LAST:event_menuNewActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HealthPassportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem menuLoad;
    private javax.swing.JMenuItem menuNew;
    private javax.swing.JMenuItem menuSave;
    // End of variables declaration//GEN-END:variables

    @Override
    public void logAction(String type, String attributeKey, String attributeValue) {
        //System.out.println("action logged : "+type+" - "+attributeKey+" = "+attributeValue);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //System.out.println("size : "+getWidth()+" , "+getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
