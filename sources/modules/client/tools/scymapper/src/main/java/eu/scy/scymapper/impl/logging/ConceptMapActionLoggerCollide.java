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
        try {
            File newFile = new File("." + File.separator + "scymapper_" + username + "_" + eloURI + ".xml.1");
            File oldFile = new File("." + File.separator + "scymapper_" + username + "_" + eloURI + ".xml");
            FileWriter fw = new FileWriter(newFile);
            XStream xstream = new XStream(new DomDriver());
            xstream.toXML(conceptMap, fw);
            fw.close();
            oldFile.delete();
            boolean success = newFile.renameTo(oldFile);
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