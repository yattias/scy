/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jdom.Element;

/**
 *
 * @author Marjolaine
 */
public class CopexPanel extends JPanel{
    private EdPPanel copex;

    public CopexPanel() {
        super();
        this.setLayout(new BorderLayout());
        copex = new EdPPanel();
        this.add(this.copex, BorderLayout.CENTER);
        copex.loadData();
    }

    /* load ELO into copex */
    public void loadELO(String xmlContent){
        this.copex.loadELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public void newElo(){
        this.copex.newELO();
    }

    public Element getExperimentalProcedure(){
        return this.copex.getExperimentalProcedure();
    }

}
