package eu.scy.agents.conceptmap.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import eu.scy.agents.conceptmap.Edge;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.conceptmap.Node;
import eu.scy.agents.conceptmap.proposer.CMProposerAgent;
import eu.scy.agents.conceptmap.proposer.CMProposerObserver;
import eu.scy.agents.impl.AgentProtocol;

public class CMProposerVisualizer extends JFrame implements CMProposerObserver {

    private TextHighlightingPanel textPanel;

    private ConceptMapView ontologyView;

    private ConceptMapView studentView;

    private JLabel statusLabel;

    public static void main(String[] args) throws Exception {
        CMProposerVisualizer viz = new CMProposerVisualizer();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "proposer id");
        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_PORT, 2525);
        map.put("observer", viz);
        CMProposerAgent agent = new CMProposerAgent(map);
        agent.start();
    }

    public CMProposerVisualizer() {
        super("CMProposerVisualizer");
        setLayout(new BorderLayout(5, 5));
        textPanel = new TextHighlightingPanel(10);
        ontologyView = new ConceptMapView();
        studentView = new ConceptMapView();
        statusLabel = new JLabel("No connection to agent", JLabel.CENTER);
        statusLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        textPanel.setBorder(BorderFactory.createTitledBorder("Text"));
        studentView.setBorder(BorderFactory.createTitledBorder("Student Concept Map"));
        ontologyView.setBorder(BorderFactory.createTitledBorder("Ontology-generated Concept Map"));

        JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ontologyView, studentView);
        centerPane.setDividerLocation(500);

        JPanel northPane = new JPanel(new BorderLayout(5,5));
        northPane.add(BorderLayout.CENTER, textPanel);
        northPane.add(BorderLayout.SOUTH, statusLabel);
        
        
        add(BorderLayout.CENTER, centerPane);
        add(BorderLayout.NORTH, northPane);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setCMText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                textPanel.setText(text);
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    public void foundTextKeyword(final String keyword) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                System.out.println("HIGHLIGHT: " + keyword);
                textPanel.highlight(keyword, Color.YELLOW);
            }
        });
//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException e) {}
    }

    public void foundOntoConcept(final String concept) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ontologyView.addConcept(concept);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
    }

    public void foundOntoRelation(final String from, final String to, final String label) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ontologyView.addLink(from, to, label);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
    }

    public void foundStudentsMap(final Graph g) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (Node n : g.getNodes()) {
                    studentView.addConcept(n.getLabel());
                }
                for (Edge e : g.getEdges()) {
                    studentView.addLink(e.getFromNode().getLabel(), e.getToNode().getLabel(), e.getLabel());
                }
                studentView.layoutGraph(false);
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    public void setStatusText(final String statusText) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                statusLabel.setText(statusText);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

    }

    public void clearState() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                textPanel.clearText();
                ontologyView.clearGraph();
                studentView.clearGraph();
            }
        });
    }

    public void markConceptAsMatching(String concept) {
        ontologyView.setNodeType(concept, "match");
    }

    public void markConceptAsProposal(String concept) {
        ontologyView.setNodeType(concept, "proposal");
    }

    public void foundTextCloudKeyword(String term) {
        foundOntoConcept(term);
    }

    public void markRelationAsProposal(String relation) {
        System.out.println(relation);
    }

}
