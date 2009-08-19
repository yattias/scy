/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.copex;

import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jdom.Element;
import roolo.elo.JDomStringConversion;


/**
 *
 * @author Marjolaine
 */
public class ScyCopexPanel extends JPanel {

    private EdPPanel copex;

    public ScyCopexPanel() {
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
