package eu.scy.scymapper.impl.logging;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;

public class ConceptMapActionLoggerCollide extends ConceptMapActionLogger {

    private IConceptMap conceptMap;

    public ConceptMapActionLoggerCollide(IActionLogger actionLogger, IDiagramModel diagram, String username, IConceptMap conceptMap) {
        super(actionLogger, diagram, username);
        this.conceptMap = conceptMap;
    }

    @Override
    public void log(IAction action) {
        super.log(action);
        String userdir = System.getProperty("user.home");
        try {
            File f = new File(userdir + File.separator + "scymapper_" + username + "_" + eloURI + ".xml.1");
            FileWriter fw = new FileWriter(f);
            XStream xstream = new XStream(new DomDriver());
            xstream.toXML(conceptMap, fw);
            fw.close();
            boolean success = f.renameTo(new File(userdir + File.separator + "scymapper_" + username + "_" + eloURI + ".xml"));
            if (success) {
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Beim automatischen Speichern ist ein Fehler aufgetreten. Bitte melden Sie sich bei einem der Betreuer.", "Fehler", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

}