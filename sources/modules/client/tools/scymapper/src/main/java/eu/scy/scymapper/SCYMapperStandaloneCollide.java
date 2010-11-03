package eu.scy.scymapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.SCYMapperPanelCollide;

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
	private static final String SQLSPACES_HOST = "localhost";
	
    private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/scymapperCollideToolConfig.xml";

    private static final int SQLSPACES_PORT = 2525;

    private TupleSpace commandSpace;

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
        super.init(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
        try {
            commandSpace = new TupleSpace(new User("SCYMapper"), SQLSPACES_HOST, SQLSPACES_PORT, "command");
            Callback cb = new Callback() {

                @Override
                public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
                    List<String> keywords = new ArrayList<String>();
                    String type = null;
                    for (int i = 7; i < afterTuple.getNumberOfFields(); i++) {
                        if (type == null) {
                            type = afterTuple.getField(i).getValue().toString().split("=")[0].split("_")[0] + "s";
                        }
                        String keyword = afterTuple.getField(i).getValue().toString().split("=")[1];
                        keywords.add(keyword);
                    }
                    scyMapperPanel.suggestKeywords(keywords, type);
                }
            };
            // TODO: insert user here!
            commandSpace.eventRegister(Command.WRITE, new Tuple("notification", String.class, String.class, "scymapper", String.class, String.class, String.class, Field.createWildCardField()), cb, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SCYMapperPanel createScyMapperPanel(IConceptMap cmap) {
        scyMapperPanel = new SCYMapperPanelCollide(cmap, configuration, SQLSPACES_HOST, SQLSPACES_PORT);
        currentConceptMap = cmap;
        return scyMapperPanel;
    }

}
