/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.main;

import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Locale;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * copex applet for the WISE project
 * @author Marjolaine
 */
public class CopexApplet extends JApplet implements ActionCopex{
    private CopexPanel copexPanel;
    private JPanel myPanel;

    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initEdP();
                    loadEdP();
                }
            });
        } catch (Exception ex) {
        }
    }

    @Override
    public void stop() {
        this.copexPanel.stop();
        super.stop();
    }



    private void initEdP(){
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        this.add(myPanel, BorderLayout.CENTER);
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en");
        //locale = new Locale("fr");
        //locale = new Locale("de");
        //Locale.setDefault(locale);
        copexPanel = new CopexPanel(true, locale);
        copexPanel.addActionCopex(this);
        myPanel.add(copexPanel, BorderLayout.CENTER);
        setSize(CopexPanel.PANEL_WIDTH, CopexPanel.PANEL_HEIGHT);
    }

    private void loadEdP(){
//        copexPanel.loadData();
//        copexPanel.setQuestionDialog();
        setSize(550,350);
    }

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {

    }

    @Override
    public void logAction(String type, List<CopexProperty> attribute) {

    }

    /** returns the copex elo as xmlString */
    public String getCopexElement(){
        return CopexUtilities.xmlToString(this.copexPanel.getXProc());
    }

   /** set the copex elo as xml String */
    public void setCopexElement(String xproc){
        this.copexPanel.loadELO(CopexUtilities.stringToXml(xproc));
        myPanel.revalidate();
        repaint();
    }

}
