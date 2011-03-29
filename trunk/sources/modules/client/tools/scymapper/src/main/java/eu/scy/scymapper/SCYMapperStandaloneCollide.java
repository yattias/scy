package eu.scy.scymapper;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Callback.Command;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.SCYMapperPanelCollide;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;

public class SCYMapperStandaloneCollide extends SCYMapperStandalone {

    private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/scymapperCollideToolConfig.xml";

    private TupleSpace commandSpace;

    private String userid;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                initLAF();
                SCYMapperStandaloneCollide app = new SCYMapperStandaloneCollide();
                app.setTitle("SCYMapper Concept Mapping Tool");
                try {
                    app.setIconImage(ImageIO.read(getClass().getResource("scy-mapper.png")));
                } catch (IOException e) {}
                app.setVisible(true);
            }
        });
    }

    @Override
    void init() {
        userid = "";
        while (userid.trim().length() == 0) {
            userid = JOptionPane.showInputDialog(this, "Bitte geben Sie Ihren Namen ein:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (userid == null) {
                System.exit(0);
            }
            for (char c : userid.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    JOptionPane.showMessageDialog(this, "Ihr Name beinhaltet nicht g\u00FCltige Zeichen. Es sind nur Zahlen und Buchstaben erlaubt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    userid = "";
                    break;
                }
            }
        }

        super.init(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
        try {
            commandSpace = new TupleSpace(new User("SCYMapper("+userid+")"), SCYMapperStandaloneConfig.getInstance().getSQLSpacesHost(), SCYMapperStandaloneConfig.getInstance().getSQLSpacesPort(), "command");
            Callback cb = new Callback() {

                @Override
                public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
                    List<String> keywords = new ArrayList<String>();
                    List<String> categories = new ArrayList<String>();
                    String type = afterTuple.getField(7).getValue().toString().split("=")[1];
                    for (int i = 8; i < afterTuple.getNumberOfFields(); i++) {
                        String keyword = afterTuple.getField(i).getValue().toString().split("=")[1];
                        String category = afterTuple.getField(i).getValue().toString().split("=")[0];
                        keywords.add(keyword);
                        categories.add(category);
                    }
                    String[] keywordArray = (String[]) keywords.toArray(new String[keywords.size()]);
                    String[] categoryArray = (String[]) categories.toArray(new String[categories.size()]);
                    scyMapperPanel.suggestKeywords(keywordArray, categoryArray, type);
                }
            };
            commandSpace.eventRegister(Command.WRITE, new Tuple("notification", String.class, userid, "scymapper", String.class, String.class, String.class, Field.createWildCardField()), cb, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SCYMapperPanel createScyMapperPanel(IConceptMap cmap) {
        String eloUri = new VMID().toString();
        eloUri = eloUri.replaceAll(":", "");
        eloUri = eloUri.replaceAll("-", "");
        File workingDir = new File(".");
        final String namePrefix = "scymapper_" + userid + "_";
        String[] files = workingDir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.matches(namePrefix + ".*\\.xml");
            }
        });

        if (files != null && files.length > 0) {
            try {
                FileReader fr = new FileReader(workingDir.getAbsolutePath() + File.separator + files[0]);
                XStream xstream = new XStream(new DomDriver());
                cmap = (IConceptMap) xstream.fromXML(fr);
                fr.close();
                eloUri = files[0].substring(namePrefix.length(), files[0].length() - 4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        scyMapperPanel = new SCYMapperPanelCollide(cmap, configuration, SCYMapperStandaloneConfig.getInstance().getSQLSpacesHost(), SCYMapperStandaloneConfig.getInstance().getSQLSpacesPort(), userid);
        scyMapperPanel.setEloURI(eloUri);
        currentConceptMap = cmap;
        
        return scyMapperPanel;
    }

    @Override
    protected void initMenuBar() {
        // no menu for the study
    }

}
