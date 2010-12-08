package eu.scy.scymapper.impl.logging;

import java.io.File;
import java.io.FileWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

public class ConceptMapActionLoggerCollide extends ConceptMapActionLogger {

    private static final String SYNONYM_ADDED = "synonym_added";

    private IConceptMap conceptMap;

    public ConceptMapActionLoggerCollide(IActionLogger actionLogger, IDiagramModel diagram, String username, IConceptMap conceptMap) {
        super(actionLogger, diagram, username);
        this.conceptMap = conceptMap;
    }

    @Override
    public void log(IAction action) {
        super.log(action);
        try {
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
     * Logs when the user added a synonym
     *
     * @param node The node model
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

}