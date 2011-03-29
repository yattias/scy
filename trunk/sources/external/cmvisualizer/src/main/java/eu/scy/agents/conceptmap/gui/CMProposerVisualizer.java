package eu.scy.agents.conceptmap.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import eu.scy.agents.conceptmap.Edge;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.conceptmap.Node;
import eu.scy.agents.conceptmap.StudyAdminAgent;
import eu.scy.agents.conceptmap.StudyAdminAgent.StudyAgent;
import eu.scy.agents.conceptmap.proposer.CMProposerAgent;
import eu.scy.agents.conceptmap.proposer.CMProposerObserver;
import eu.scy.agents.impl.AgentProtocol;

public class CMProposerVisualizer extends JFrame implements CMProposerObserver, ActionListener {

    private TextHighlightingPanel textPanel;

    private String modellerName;

    private String proposerName;

    private String enricherName;

    private ConceptMapView ontologyView;

    private ConceptMapView studentView;

    private JLabel statusLabel;

    private JButton continueButton;

    private JCheckBox autoplayCheckbox;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private JSplitPane centerPane;

    private JPanel panel;

    private JToolBar toolBar;

    private JButton agentsBT;

    private JPopupMenu agentsPopupMenu;

    private JCheckBoxMenuItem proposerCheckbox;

    private JCheckBoxMenuItem enricherCheckbox;

    private JCheckBoxMenuItem modellerCheckbox;

    private JPanel panel_1;

    private JLabel lblStatus;

    private StudyAdminAgent saa;

    public static void main(String[] args) throws Exception {
        CMProposerVisualizer viz = new CMProposerVisualizer();
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put(AgentProtocol.PARAM_AGENT_ID, "proposer id");
//        map.put(AgentProtocol.TS_HOST, "localhost");
//        map.put(AgentProtocol.TS_PORT, 2525);
//        map.put("observer", viz);
//        CMProposerAgent agent = new CMProposerAgent(map);
//        agent.start();
    }

    public CMProposerVisualizer() {
        super("CMProposerVisualizer");
        saa = new StudyAdminAgent();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        getContentPane().setLayout(new BorderLayout(5, 5));
        textPanel = new TextHighlightingPanel(10);
        ontologyView = new ConceptMapView();
        studentView = new ConceptMapView();

        JPanel statusPanel = new JPanel(new BorderLayout());

        textPanel.setBorder(BorderFactory.createTitledBorder("Text"));
        studentView.setBorder(BorderFactory.createTitledBorder("Student Concept Map"));
        ontologyView.setBorder(BorderFactory.createTitledBorder("Ontology-generated Concept Map"));

        centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ontologyView, studentView);
        centerPane.setDividerLocation(500);

        panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        toolBar = new JToolBar();
        toolBar.setForeground(Color.WHITE);
        toolBar.setFloatable(false);
        panel.add(toolBar);
        continueButton = new JButton("");
        continueButton.setIcon(new ImageIcon(CMProposerVisualizer.class.getResource("/eu/scy/agents/conceptmap/gui/play_16.png")));
        toolBar.add(continueButton);
        continueButton.addActionListener(this);
        continueButton.setEnabled(false);
        continueButton.setLayout(new BorderLayout());
        autoplayCheckbox = new JCheckBox("Autoplay", true);
        toolBar.add(autoplayCheckbox);

        agentsBT = new JButton("Agents");
        toolBar.add(agentsBT);
        agentsBT.setIcon(new ImageIcon(CMProposerVisualizer.class.getResource("/eu/scy/agents/conceptmap/gui/agent_16.png")));
        agentsBT.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                agentsPopupMenu.show(agentsBT, e.getX(), e.getY());
            }

        });

        agentsPopupMenu = new JPopupMenu();
        addPopup(agentsBT, agentsPopupMenu);

        proposerCheckbox = new JCheckBoxMenuItem("Proposer");
        proposerCheckbox.addActionListener(this);
        agentsPopupMenu.add(proposerCheckbox);

        enricherCheckbox = new JCheckBoxMenuItem("Enricher");
        enricherCheckbox.addActionListener(this);
        agentsPopupMenu.add(enricherCheckbox);

        modellerCheckbox = new JCheckBoxMenuItem("Modeller");
        modellerCheckbox.addActionListener(this);
        agentsPopupMenu.add(modellerCheckbox);
        toolBar.addSeparator(new Dimension(20, 20));
        lblStatus = new JLabel("Status:");
        toolBar.add(lblStatus);
        statusLabel = new JLabel("No connection to agent", JLabel.CENTER);
        toolBar.add(statusLabel);
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        autoplayCheckbox.addActionListener(this);

        panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.SOUTH);

        JPanel northPane = new JPanel(new BorderLayout(5, 5));
        // JScrollPane scrollPane = new JScrollPane(textPanel);
        // scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        northPane.add(BorderLayout.CENTER, textPanel);
        northPane.add(BorderLayout.SOUTH, statusPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northPane, centerPane);
        splitPane.setDividerLocation(300);
        getContentPane().add(BorderLayout.CENTER, splitPane);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setCMText(final String text) {
        studentView.clearMap();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                textPanel.setText(text);
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}
    }

    public void foundTextKeyword(final String keyword) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                System.out.println("HIGHLIGHT: " + keyword);
                textPanel.highlight(keyword, Color.YELLOW);
            }
        });
        // try {
        // Thread.sleep(400);
        // } catch (InterruptedException e) {}
    }

    public void foundOntoConcept(final String concept) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ontologyView.addConcept(concept);
            }
        });
        try {
            Thread.sleep(75);
        } catch (InterruptedException e) {}
    }

    public void foundOntoRelation(final String from, final String to, final String label) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ontologyView.addLink(from, to, label);
            }
        });
        try {
            Thread.sleep(25);
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
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    public void setStatusText(final String statusText) {
        if (statusText.equals(statusLabel.getText())) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                statusLabel.setText(statusText);
            }
        });
        if (!autoplayCheckbox.isSelected()) {
            continueButton.setEnabled(true);
            try {
                lock.lock();
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void clearState() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                textPanel.clearText();
                Container ontologyViewParent = ontologyView.getParent();
                Container studentViewParent = studentView.getParent();
                ontologyViewParent.remove(ontologyView);
                studentViewParent.remove(studentView);
                ontologyView = new ConceptMapView();
                studentView = new ConceptMapView();
                studentView.setBorder(BorderFactory.createTitledBorder("Student Concept Map"));
                ontologyView.setBorder(BorderFactory.createTitledBorder("Ontology-generated Concept Map"));
                ontologyViewParent.add(ontologyView);
                studentViewParent.add(studentView);
                centerPane.setDividerLocation(500);
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

    private void startModeller() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                modellerName = saa.startAgent(StudyAgent.MODELLER, null);
                return modellerName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        };
        worker.execute();
    }

    private void stopModeller() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                saa.stopAgent(modellerName);
                return modellerName;

            }

            @Override
            public void done() {
                modellerName = "";
                getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

    private void startProposer() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("observer", CMProposerVisualizer.this);
                proposerName = saa.startAgent(StudyAgent.PROPOSER, params);
                return proposerName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

        };
        worker.execute();

    }

    private void stopProposer() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                saa.stopAgent(proposerName);
                return proposerName;

            }

            @Override
            public void done() {
                proposerName = "";
                getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

    private void startEnricher() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                enricherName = saa.startAgent(StudyAgent.ENRICHER, null);
                return enricherName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        };
        worker.execute();

    }

    private void stopEnricher() {

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                saa.stopAgent(enricherName);
                return enricherName;

            }

            @Override
            public void done() {
                enricherName = "";
                getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(continueButton)) {
            lock.lock();
            continueButton.setEnabled(false);
            condition.signal();
            lock.unlock();
        } else if (e.getSource().equals(autoplayCheckbox)) {
            if (autoplayCheckbox.isSelected()) {
                if (continueButton.isEnabled()) {
                    lock.lock();
                    condition.signal();
                    lock.unlock();
                }
                continueButton.setEnabled(false);
            }
        } else if (e.getSource() == enricherCheckbox) {
            if (enricherCheckbox.isSelected()) {
                startEnricher();
            } else {
                stopEnricher();

            }

        } else if (e.getSource() == proposerCheckbox) {
            if (proposerCheckbox.isSelected()) {
                startProposer();
            } else {
                stopProposer();
            }

        } else if (e.getSource() == modellerCheckbox) {
            if (modellerCheckbox.isSelected()) {
                startModeller();
            } else {
                stopModeller();
            }

        }
    }

    private static void addPopup(Component component, final JPopupMenu popup) {}
}
