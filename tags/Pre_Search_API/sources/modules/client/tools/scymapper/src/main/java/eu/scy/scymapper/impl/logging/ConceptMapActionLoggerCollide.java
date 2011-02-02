package eu.scy.scymapper.impl.logging;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.SCYMapperPanelCollide;

public class ConceptMapActionLoggerCollide extends ConceptMapActionLogger {

	public static final String SYNONYM_ADDED = "synonym_added";

	public static final String LEXICON_SWITCHED = "lexicon_switched";

    private IConceptMap conceptMap;

    private SCYMapperPanelCollide panel;

    public ConceptMapActionLoggerCollide(IActionLogger actionLogger, IDiagramModel diagram, String username, IConceptMap conceptMap, SCYMapperPanelCollide panel) {
        super(actionLogger, diagram, username);
        this.conceptMap = conceptMap;
        this.panel = panel;
    }

    @Override
    public void log(IAction action) {
        super.log(action);
        try {

            GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage image = gfxConf.createCompatibleImage(panel.getWidth(), panel.getHeight(), Transparency.TRANSLUCENT);
            Graphics2D g2d = image.createGraphics();
            panel.paintAll(g2d);
            g2d.dispose();
            ImageIO.write(image, "png", new File("." + File.separator + "scymapper_screenshot_" + username + "_" + eloURI + ".png"));
            
            File newFile = new File("." + File.separator + "scymapper_" + username + "_" + eloURI + ".xml.1");
            File oldFile = new File("." + File.separator + "scymapper_" + username + "_" + eloURI + ".xml");
            FileWriter fw = new FileWriter(newFile);
            XStream xstream = new XStream(new DomDriver());
            xstream.toXML(conceptMap, fw);
            fw.close();
            oldFile.delete();
            newFile.renameTo(oldFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs when the user adds a synonym
     * 
     * @param node
     *            The node model
     */
    public void logSynonymAdded(INodeModel node, String synonym) {
        IAction a = createSCYMapperAction(SYNONYM_ADDED);
        a.addAttribute("id", node.getId());
        a.addAttribute("synonym", synonym);

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }
    
    public void logLexiconSwitched() {
        IAction a = createSCYMapperAction(LEXICON_SWITCHED);
        log(a);
    }
    

}