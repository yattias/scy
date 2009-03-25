package eu.scy.elobrowser.tool.simquest;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdom.JDOMException;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import sqv.ISimQuestViewer;
import sqv.ModelVariable;
import sqv.data.IDataClient;
import javax.swing.JToggleButton;

/**
 * This class collects datapoints from a SimQuest simulation and stores them as
 * an ELO.
 *
 * @author Lars Bollen
 *
 */
public class DataCollector extends JPanel implements ActionListener,
        IDataClient {

    private static final long serialVersionUID = -2306183502112904729L;
    private ISimQuestViewer simquestViewer;
    private JTextArea text = new JTextArea(5, 20);
    private IMetadataTypeManager<IMetadataKey> metadataTypeManager;
    private SCYDataAgent dataAgent;
    private List<ModelVariable> simulationVariables;
    private List<ModelVariable> selectedVariables;
    private JCheckBox checkbox;
    private DataSet dataset;
    private Object eloTitle = "an unnamed SimQuest dataset";
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
    private JToggleButton sandboxbutton;
    private DatasetSandbox sandbox = null;

    public DataCollector(ISimQuestViewer simquestViewer) {
        // initialize user interface
        initGUI();

        // setting some often-used variable
        this.simquestViewer = simquestViewer;
        simulationVariables = simquestViewer.getDataServer().getVariables(
                "name is not relevant");
        setSelectedVariables(simquestViewer.getDataServer().getVariables(
                "name is not relevant"));

        // register agent
        dataAgent = new SCYDataAgent(this, simquestViewer.getDataServer());
        dataAgent.add(simquestViewer.getDataServer().getVariables(
                "name is not relevant"));
        simquestViewer.getDataServer().register(dataAgent);
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("SCY Dataset Collector"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton button = new JButton("select relevant variables");
        button.setActionCommand("configure");
        button.addActionListener(this);
        buttonPanel.add(button);

        button = new JButton("add current datapoint");
        button.setActionCommand("adddata");
        button.addActionListener(this);
        buttonPanel.add(button);

        sandboxbutton = new JToggleButton("sandbox");
        sandboxbutton.setSelected(false);
        sandboxbutton.setActionCommand("sandbox");
        sandboxbutton.addActionListener(this);
        buttonPanel.add(sandboxbutton);

        checkbox = new JCheckBox("add datapoints continuosly");
        checkbox.setSelected(false);
        buttonPanel.add(checkbox);

        this.add(buttonPanel, BorderLayout.NORTH);

        JScrollPane pane = new JScrollPane(text);
        this.add(pane, BorderLayout.CENTER);
    }

    public void addCurrentDatapoint() {
        ModelVariable var;
        List<String> values = new LinkedList<String>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars.hasNext();) {
            var = vars.next();
            values.add(var.getValueString());
            text.append(var.getExternalName() + ":" + var.getValueString() + " / ");
        }
        text.append("\n");
        DataSetRow newRow = new DataSetRow(values);
        dataset.addRow(newRow);
        if (sandboxbutton.isSelected()) {
            sandbox.sendDataSetRow(newRow);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("adddata")) {
            addCurrentDatapoint();
        } else if (evt.getActionCommand().equals("configure")) {
            VariableSelectionDialog dialog = new VariableSelectionDialog(
                    simquestViewer.getMainFrame(), this);
            dialog.setVisible(true);
        } else if (evt.getActionCommand().equals("sandbox")) {
            if (sandboxbutton.isSelected()) {
                initSandbox();
            } else {
                sandbox = null;
            }
        }
    }

    public void newELO() {
        dataset.removeAll();
        if (sandbox != null) {
            initSandbox();
        }
        text.setText("");
    }

    public DataSet getDataSet() {
        return dataset;
    }

    public SimConfig getSimConfig() {
        return new SimConfig(this);
    }

    public void setSimConfig(String xmlSimConfig) {
        SimConfig config;
        ModelVariable var;
        try {
            config = new SimConfig(xmlSimConfig);
            if (config.getSimulationName().equals(getSimQuestViewer().getApplication().getTopic(0).getName())) {
                for (Iterator<ModelVariable> variables = getSimulationVariables().iterator(); variables.hasNext();) {
                    var = variables.next();
                    // if the variable name cannot be found in the config, a nullpointerex. is thrown
                    var.setValue(config.getVariables().get(var.getName()));
                }
            } else {
                // the simulation names doesn't match
                JOptionPane.showMessageDialog(this, "The name of the current simulation and the config doesn't match - nothing loaded.",
                        "Config file problem",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (JDOMException ex) {
            JOptionPane.showMessageDialog(this, "Could not parse the SimConfig; the current simulation will not be changed.",
                    "Parsing problem",
                    JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Could not update the Simulation with this Configuration. Do they really match?",
                    "Update problem",
                    JOptionPane.WARNING_MESSAGE);
        }
        // trigger an update to all registered clients
        simquestViewer.getDataServer().updateClients();
    }

    @Override
    public void updateClient() {
        if (checkbox.isSelected()) {
            addCurrentDatapoint();
        }
    }

    public List<ModelVariable> getSimulationVariables() {
        return simulationVariables;
    }

    public List<ModelVariable> getSelectedVariables() {
        return selectedVariables;
    }

    public ISimQuestViewer getSimQuestViewer() {
        return simquestViewer;
    }

    public void setSelectedVariables(List<ModelVariable> selection) {
        selectedVariables = selection;
        ModelVariable var;
        List<DataSetColumn> datasetvariables = new LinkedList<DataSetColumn>();
        List<DataSetHeader> datasetheaders = new LinkedList<DataSetHeader>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars.hasNext();) {
            var = vars.next();
            datasetvariables.add(new DataSetColumn(var.getName(), var.getExternalName(), "double"));
        }
        datasetheaders.add(new DataSetHeader(datasetvariables, Locale.ENGLISH));
        dataset = new DataSet(datasetheaders);
        if (sandbox != null) {
            initSandbox();
        }

        if (text != null) {
            text.setText("");
        }
    }

    private void initSandbox() {
        try { // init the sandbox
            sandbox = new DatasetSandbox(this);
            text.append("sandbox initialised.\n");
        } catch (CollaborationServiceException ex) {
            text.append("could not initialise sandbox.\n");
            text.append(ex.getMessage() + "\n");
            sandboxbutton.setSelected(false);
        }
    }

}
