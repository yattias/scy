/*
 * CopexApplet.java
 *
 * Created on 25 juillet 2008, 14:38
 */

package eu.scy.tools.copex.edp;


import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.utilities.ActionCopex;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;

/**
 * represente l'applet Editeur de Protocole COPEX
 * 
 * @author  MBO
 */
public class CopexApplet extends JApplet implements ActionCopex {
    /* edp panel*/
    private CopexPanel copex;
    
    /** Initializes the applet CopexApplet */
    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                    initEdP();
                    
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        copex.stop();
        super.stop();
    }

    
   
    
    
    
    
    /**
     * initialisation de l'applet
     */
    public void initEdP(){
        // recuperation des parametres de l'applet :
        String idUser = getParameter("USER");
        String m = getParameter("MISSION");
        String mo = getParameter("MODE");
        String userName = getParameter("USERNAME");
        String firstName = "";
        try{
            firstName = getParameter("USERFIRSTNAME");
        }catch(Throwable t){
        }
        long dbKeyMission = -1;
        int mode = -1;
        try{
            dbKeyMission = Long.valueOf(m);
            mode = Integer.valueOf(mo);
        }catch(Throwable t){
            System.out.println(t);
            this.stop();
            this.destroy();
        }
        copex = new CopexPanel(null, idUser, dbKeyMission, mode, userName, firstName);
        copex.addActionCopex(this);
        getContentPane().remove(labelWait);
        add(copex, BorderLayout.CENTER);
        setSize(CopexPanel.PANEL_WIDTH, CopexPanel.PANEL_HEIGHT);
    }
    
    
    /* retourne l'applet */
    public CopexApplet getCopexApplet(){
        return this;
    }
    
     /* impression au format html */
    public void printCopexHTML(String nameFile) {
        try{
            URL urlPrint ;
            urlPrint = new URL(getCodeBase(), getDirectoryDataPrint()+nameFile);

            getAppletContext().showDocument(urlPrint, "_blank");
            repaint();
        }catch(MalformedURLException e){
            copex.displayError(new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT")+" : "+e, false), copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }


     public String getDirectorySaveXML() {
       return "../editeurProtocole/proc/";
    }

    public String getDirectoryDataPrint() {
         return "../editeurProtocole/print/print/";
    }

    public String getDirectoryTrace() {
         return "../trace/";
    }

   
   
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelWait = new javax.swing.JLabel();

        setBackground(new java.awt.Color(236, 233, 216));

        labelWait.setText("Veuillez patientez pendant le chargement des donnees....");
        labelWait.setName("labelWait"); // NOI18N
        getContentPane().add(labelWait, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelWait;
    // End of variables declaration//GEN-END:variables

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {
        
    }

    
 
   
    
}


