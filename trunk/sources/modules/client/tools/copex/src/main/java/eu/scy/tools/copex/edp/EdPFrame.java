/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EdPFrame.java
 *
 * Created on 6 avr. 2009, 15:52:59
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * test frame edp
 * @author Marjolaine
 */
public class EdPFrame extends javax.swing.JFrame {
    private EdPPanel edP;
    private File lastUsedFile = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    private transient SAXBuilder builder = new SAXBuilder();

    /** Creates new form EdPFrame */
    public EdPFrame() {
        super();
        initComponents();
        initEdP();
    }

    public void load(){
        edP.loadData();
        setSize(695,495);
    }
    private void initEdP(){
        
        this.menuItemInitProc.setHorizontalTextPosition(SwingConstants.LEFT);
        this.menuItemLoadELO.setHorizontalTextPosition(SwingConstants.LEFT);
        this.menuItemSaveELO.setHorizontalTextPosition(SwingConstants.LEFT);
        this.menuItemQuit.setHorizontalTextPosition(SwingConstants.LEFT);
        edP = new EdPPanel();
        add(edP, BorderLayout.CENTER);
        setSize(EdPPanel.PANEL_WIDTH, EdPPanel.PANEL_HEIGHT);
    }

    private void quitEdP(){
        this.edP.stop();
        this.setVisible(false);
        this.dispose();
    }

    private void saveELO(){
        Element xproc = edP.getExperimentalProcedure() ;
        if(xproc == null){
            JOptionPane.showMessageDialog(this , "Error while saving the procedure : no procedure !  ", "ERROR",JOptionPane.ERROR_MESSAGE);
            return;
        }
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return "*.xml";
            }
        } ;
        JFileChooser aFileChooser = new JFileChooser();
        if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
        aFileChooser.setFileFilter(filter);
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
			FileWriter fileWriter = null;
//			try
//			{
//				fileWriter = new FileWriter(file);
//                xmlOutputter.output(xproc, fileWriter);
//			}
//			catch (IOException e)
//			{
//				edP.displayError(new CopexReturn("Erreur lors de la sauvegarde", false), "Erreur");
//			}

            builder = new SAXBuilder();
		try {
			Document doc = builder
					.build(new StringReader(CopexUtilities.xmlToString(xproc)));
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileWriter writer = new FileWriter(file);
			out.output(doc, writer);
			writer.flush();
			writer.close();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


			finally
			{
				if (fileWriter != null)
					try
					{
						fileWriter.close();
					}
					catch (IOException e)
					{
						edP.displayError(new CopexReturn("Erreur lors de la sauvegarde, fermeture du fichier", false), "Erreur");
					}
			}
        }
    }

    private void loadELO(){
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
			FileReader fileReader = null;
			try
			{
				fileReader = new FileReader(file);
				///Document doc = builder.build(fileReader, file.getAbsolutePath());
                Document doc = builder.build(file);
                edP.loadELO(doc.getRootElement());
			}
			catch (Exception e)
			{
				e.printStackTrace();
                edP.displayError(new CopexReturn("Erreur durant le chargement "+e, false), "Erreur");
			}
            

			finally
			{
				if (fileReader != null)
					try
					{
						fileReader.close();
					}
					catch (IOException e)
					{
						edP.displayError(new CopexReturn("Erreur durant le chargement, fermeture fichier "+e, false), "Erreur");
					}
			}
		}
    }

    
    private void newELO(){
        this.edP.newELO();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBarEdp = new javax.swing.JMenuBar();
        menuEdP = new javax.swing.JMenu();
        menuItemInitProc = new javax.swing.JMenuItem();
        menuItemLoadELO = new javax.swing.JMenuItem();
        menuItemSaveELO = new javax.swing.JMenuItem();
        menuItemQuit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COPEX- Experimental Design Tool");

        menuEdP.setText("Copex");

        menuItemInitProc.setText("New");
        menuItemInitProc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuItemInitProc.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        menuItemInitProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInitProcActionPerformed(evt);
            }
        });
        menuEdP.add(menuItemInitProc);

        menuItemLoadELO.setText("Load...");
        menuItemLoadELO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLoadELOActionPerformed(evt);
            }
        });
        menuEdP.add(menuItemLoadELO);

        menuItemSaveELO.setText("Save...");
        menuItemSaveELO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveELOActionPerformed(evt);
            }
        });
        menuEdP.add(menuItemSaveELO);

        menuItemQuit.setText("Quit");
        menuItemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemQuitActionPerformed(evt);
            }
        });
        menuEdP.add(menuItemQuit);

        menuBarEdp.add(menuEdP);

        setJMenuBar(menuBarEdp);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemQuitActionPerformed
        this.quitEdP();
    }//GEN-LAST:event_menuItemQuitActionPerformed

    private void menuItemSaveELOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveELOActionPerformed
        saveELO();
    }//GEN-LAST:event_menuItemSaveELOActionPerformed

    private void menuItemLoadELOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLoadELOActionPerformed
        loadELO();
    }//GEN-LAST:event_menuItemLoadELOActionPerformed

    private void menuItemInitProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInitProcActionPerformed
        newELO();
}//GEN-LAST:event_menuItemInitProcActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            //JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EdPFrame edPFrame =new EdPFrame();
                edPFrame.setVisible(true);
                edPFrame.load();
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar menuBarEdp;
    private javax.swing.JMenu menuEdP;
    private javax.swing.JMenuItem menuItemInitProc;
    private javax.swing.JMenuItem menuItemLoadELO;
    private javax.swing.JMenuItem menuItemQuit;
    private javax.swing.JMenuItem menuItemSaveELO;
    // End of variables declaration//GEN-END:variables

}
