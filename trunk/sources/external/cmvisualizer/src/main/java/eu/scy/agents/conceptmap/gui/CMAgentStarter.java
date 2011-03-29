package eu.scy.agents.conceptmap.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.StudyAdminAgent;
import eu.scy.agents.conceptmap.StudyAdminAgent.StudyAgent;
import eu.scy.agents.conceptmap.StudySetupStarter;
import eu.scy.agents.conceptmap.proposer.CMProposerAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMAgentStarter implements ActionListener {

    private JFrame frmAgentstarter;

    private JTextField hostTF;

    private CMProposerVisualizer visualizer;

    private JTextField portTF;

    private JTextField enricherNameTF;

    private JTextField proposerNameTF;

    private JTextField modellerNameTF;

    private JToggleButton enricherStartStop;

    private JToggleButton proposerStartStop;

    private JToggleButton modellerStartStop;

    private ImageIcon playIcon;

    private ImageIcon stopIcon;

    private StudyAdminAgent studyAdminAgent;

    private String enricherName;

    private String modellerName;

    private String proposerName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    CMAgentStarter window = new CMAgentStarter();
                    window.frmAgentstarter.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     *
     * @throws Throwable
     */
    public CMAgentStarter() throws Throwable {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     *
     * @throws Throwable
     */
    private void initialize() throws Throwable {
        frmAgentstarter = new JFrame();
        frmAgentstarter.setTitle("AgentStarter");
        frmAgentstarter.setBounds(100, 100, 632, 339);
        frmAgentstarter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playIcon = new ImageIcon(this.getClass().getResource("play.png"));
        stopIcon = new ImageIcon(this.getClass().getResource("stop.png"));

        JPanel agentsPanel = new JPanel();
        agentsPanel.setBorder(new TitledBorder(null, "Agents", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        frmAgentstarter.getContentPane().add(agentsPanel, BorderLayout.NORTH);
        GridBagLayout agentsPanelGBL = new GridBagLayout();
        agentsPanelGBL.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        agentsPanelGBL.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        agentsPanelGBL.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        agentsPanelGBL.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        agentsPanel.setLayout(agentsPanelGBL);

        JLabel enricherLabel = new JLabel("Enricher");
        enricherLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        GridBagConstraints enricherLabelConstraints = new GridBagConstraints();
        enricherLabelConstraints.insets = new Insets(0, 20, 5, 50);
        enricherLabelConstraints.gridx = 0;
        enricherLabelConstraints.gridy = 0;
        agentsPanel.add(enricherLabel, enricherLabelConstraints);

        enricherStartStop = new JToggleButton(stopIcon);
        enricherStartStop.addActionListener(this);
        GridBagConstraints enricherStartStopConstraints = new GridBagConstraints();
        enricherStartStopConstraints.insets = new Insets(0, 0, 5, 5);
        enricherStartStopConstraints.gridx = 2;
        enricherStartStopConstraints.gridy = 0;
        agentsPanel.add(enricherStartStop, enricherStartStopConstraints);

        JLabel enricherNameLabel = new JLabel("Name");
        enricherNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        GridBagConstraints enricherNameLabelConstraints = new GridBagConstraints();
        enricherNameLabelConstraints.anchor = GridBagConstraints.EAST;
        enricherNameLabelConstraints.insets = new Insets(0, 0, 5, 5);
        enricherNameLabelConstraints.gridx = 4;
        enricherNameLabelConstraints.gridy = 0;
        agentsPanel.add(enricherNameLabel, enricherNameLabelConstraints);

        enricherNameTF = new JTextField();
        enricherNameTF.setEditable(false);
        GridBagConstraints enricherNameTFConstraints = new GridBagConstraints();
        enricherNameTFConstraints.insets = new Insets(0, 0, 5, 0);
        enricherNameTFConstraints.fill = GridBagConstraints.HORIZONTAL;
        enricherNameTFConstraints.gridx = 5;
        enricherNameTFConstraints.gridy = 0;
        agentsPanel.add(enricherNameTF, enricherNameTFConstraints);
        enricherNameTF.setColumns(10);

        JLabel proposerLabel = new JLabel("Proposer");
        proposerLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        GridBagConstraints proposerLabelConstraints = new GridBagConstraints();
        proposerLabelConstraints.insets = new Insets(0, 20, 5, 50);
        proposerLabelConstraints.gridx = 0;
        proposerLabelConstraints.gridy = 2;
        agentsPanel.add(proposerLabel, proposerLabelConstraints);

        proposerStartStop = new JToggleButton(stopIcon);
        proposerStartStop.addActionListener(this);
        GridBagConstraints proposerStartStopConstraints = new GridBagConstraints();
        proposerStartStopConstraints.insets = new Insets(0, 0, 5, 5);
        proposerStartStopConstraints.gridx = 2;
        proposerStartStopConstraints.gridy = 2;
        agentsPanel.add(proposerStartStop, proposerStartStopConstraints);

        JLabel proposerNameLabel = new JLabel("Name");
        proposerNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        GridBagConstraints proposerNameLabelConstraints = new GridBagConstraints();
        proposerNameLabelConstraints.anchor = GridBagConstraints.EAST;
        proposerNameLabelConstraints.insets = new Insets(0, 0, 5, 5);
        proposerNameLabelConstraints.gridx = 4;
        proposerNameLabelConstraints.gridy = 2;
        agentsPanel.add(proposerNameLabel, proposerNameLabelConstraints);

        proposerNameTF = new JTextField();
        proposerNameTF.setEditable(false);
        GridBagConstraints proposerNameTFConstraints = new GridBagConstraints();
        proposerNameTFConstraints.insets = new Insets(0, 0, 5, 0);
        proposerNameTFConstraints.fill = GridBagConstraints.HORIZONTAL;
        proposerNameTFConstraints.gridx = 5;
        proposerNameTFConstraints.gridy = 2;
        agentsPanel.add(proposerNameTF, proposerNameTFConstraints);
        proposerNameTF.setColumns(10);

        JLabel modellerLabel = new JLabel("Modeller");
        modellerLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        GridBagConstraints modellerLabelConstraints = new GridBagConstraints();
        modellerLabelConstraints.insets = new Insets(0, 20, 5, 50);
        modellerLabelConstraints.gridx = 0;
        modellerLabelConstraints.gridy = 4;
        agentsPanel.add(modellerLabel, modellerLabelConstraints);

        modellerStartStop = new JToggleButton(stopIcon);
        modellerStartStop.addActionListener(this);
        GridBagConstraints modellerStartStopConstraints = new GridBagConstraints();
        modellerStartStopConstraints.insets = new Insets(0, 0, 5, 5);
        modellerStartStopConstraints.gridx = 2;
        modellerStartStopConstraints.gridy = 4;
        agentsPanel.add(modellerStartStop, modellerStartStopConstraints);

        JLabel modellerNameLabel = new JLabel("Name");
        modellerNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        GridBagConstraints modellerNameLabelConstraints = new GridBagConstraints();
        modellerNameLabelConstraints.anchor = GridBagConstraints.EAST;
        modellerNameLabelConstraints.insets = new Insets(0, 0, 5, 5);
        modellerNameLabelConstraints.gridx = 4;
        modellerNameLabelConstraints.gridy = 4;
        agentsPanel.add(modellerNameLabel, modellerNameLabelConstraints);

        modellerNameTF = new JTextField();
        modellerNameTF.setEditable(false);
        GridBagConstraints modellerNameTFConstraints = new GridBagConstraints();
        modellerNameTFConstraints.insets = new Insets(0, 0, 5, 0);
        modellerNameTFConstraints.fill = GridBagConstraints.HORIZONTAL;
        modellerNameTFConstraints.gridx = 5;
        modellerNameTFConstraints.gridy = 4;
        agentsPanel.add(modellerNameTF, modellerNameTFConstraints);
        modellerNameTF.setColumns(10);
        StudySetupStarter starter = new StudySetupStarter(new String[] {});

        studyAdminAgent = starter.getStudyAdminAgent();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enricherStartStop) {
            if (enricherStartStop.isSelected()) {
                startEnricher();
            } else {
                stopEnricher();

            }

        } else if (e.getSource() == proposerStartStop) {
            if (proposerStartStop.isSelected()) {
                startProposer();
            } else {
                stopProposer();
            }

        } else if (e.getSource() == modellerStartStop) {
            if (modellerStartStop.isSelected()) {
                startModeller();
            } else {
                stopModeller();
            }

        }

    }

    private void startModeller() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                modellerStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                modellerName = studyAdminAgent.startAgent(StudyAgent.MODELLER, null);
                modellerStartStop.setIcon(playIcon);
                return modellerName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    modellerNameTF.setText(agentName);
                    modellerStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
                modellerStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                studyAdminAgent.stopAgent(modellerName);
                return modellerName;

            }

            @Override
            public void done() {
                modellerName = "";
                modellerNameTF.setText(modellerName);
                modellerStartStop.setIcon(stopIcon);
                modellerStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

    private void startProposer() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                proposerStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                visualizer = new CMProposerVisualizer();
               setProgress(32);
               
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("observer", visualizer);
                proposerName = studyAdminAgent.startAgent(StudyAgent.PROPOSER, params);
                proposerStartStop.setIcon(playIcon);
                return proposerName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    proposerNameTF.setText(agentName);
                    proposerStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
                proposerStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                studyAdminAgent.stopAgent(proposerName);
                visualizer.dispose();
                visualizer = null;
                return proposerName;

            }

            @Override
            public void done() {
                proposerName = "";
                proposerNameTF.setText(proposerName);
                proposerStartStop.setIcon(stopIcon);
                proposerStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

    private void startEnricher() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() {
                enricherStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                enricherName = studyAdminAgent.startAgent(StudyAgent.ENRICHER, null);
                enricherStartStop.setIcon(playIcon);
                return enricherName;

            }

            @Override
            public void done() {
                try {
                    String agentName = get();
                    enricherNameTF.setText(agentName);
                    enricherStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
                enricherStartStop.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                studyAdminAgent.stopAgent(enricherName);
                return enricherName;

            }

            @Override
            public void done() {
                enricherName = "";
                enricherNameTF.setText(enricherName);
                enricherStartStop.setIcon(stopIcon);
                enricherStartStop.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        };
        worker.execute();

    }

}
