package eu.scy.scymapper;

import java.io.IOException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.SCYMapperPanelCollide;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Callback.Command;

public class SCYMapperStandaloneCollide extends SCYMapperStandalone {

	// TODO changed for testing purposes
//    private static final String SQLSPACES_HOST = "scy.collide.info";
//	private static final String SQLSPACES_HOST = "localhost";

    private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/scymapperCollideToolConfig.xml";

//    private static final int SQLSPACES_PORT = 2525;

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
        this.userid = new VMID().toString();
        super.init(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
        try {
            commandSpace = new TupleSpace(new User("SCYMapper"), SCYMapperStandaloneConfig.getInstance().getSQLSpacesHost(),  SCYMapperStandaloneConfig.getInstance().getSQLSpacesPort(), "command");
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
        scyMapperPanel = new SCYMapperPanelCollide(cmap, configuration, SCYMapperStandaloneConfig.getInstance().getSQLSpacesHost(), SCYMapperStandaloneConfig.getInstance().getSQLSpacesPort(), userid);
        currentConceptMap = cmap;
        return scyMapperPanel;
    }

}
