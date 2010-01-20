package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import org.jdom.JDOMException;

import sqv.ISimQuestViewer;
import sqv.ModelVariable;
import sqv.data.IDataClient;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.tools.scysimulator.logger.ScySimLogger;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;

/**
 * This class collects datapoints from a SimQuest simulation and stores them as an ELO.
 * 
 * @author Lars Bollen
 * 
 */
public class DataCollector extends JPanel implements ActionListener, IDataClient, WindowListener {

    private ISimQuestViewer simquestViewer;
    private JTextArea text = new JTextArea(5, 20);
    private SCYDataAgent dataAgent;
    private List<ModelVariable> simulationVariables;
    private List<ModelVariable> selectedVariables;
    private JCheckBox checkbox;
    private DataSet dataset;
    private JToggleButton sandboxbutton;
    private DatasetSandbox sandbox = null;
    private BalanceSlider balanceSlider;
    private ScySimLogger logger;
    private Logger debugLogger;
    private ToolBrokerAPI tbi;

    public DataCollector(ISimQuestViewer simquestViewer,ToolBrokerAPI tbi) {
        // initialize the logger(s)
        debugLogger = Logger.getLogger(DataCollector.class.getName());
        this.tbi = tbi;
        if (tbi != null) {
            debugLogger.info("setting action logger to "+tbi.getActionLogger());
            logger = new ScySimLogger(simquestViewer.getDataServer(), tbi.getActionLogger());
        } else {
            debugLogger.info("setting action logger to DevNullActionLogger");
            logger = new ScySimLogger(simquestViewer.getDataServer(), new DevNullActionLogger());
        }
        // initialize user interface
        initGUI();
        logger.toolStarted();

        // setting some often-used variable
        this.simquestViewer = simquestViewer;
        simulationVariables = simquestViewer.getDataServer().getVariables("name is not relevant");
        setSelectedVariables(simquestViewer.getDataServer().getVariables("name is not relevant"));
        setSelectedVariables(new ArrayList<ModelVariable>());
        
        // register agent
        dataAgent = new SCYDataAgent(this, simquestViewer.getDataServer());
        dataAgent.add(simquestViewer.getDataServer().getVariables("name is not relevant"));
        simquestViewer.getDataServer().register(dataAgent);

        balanceSlider = new BalanceSlider(simquestViewer.getDataServer());
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

        sandboxbutton = new JToggleButton("synchronise");
        sandboxbutton.setSelected(false);
        sandboxbutton.setActionCommand("sandbox");
        sandboxbutton.addActionListener(this);
        buttonPanel.add(sandboxbutton);

        checkbox = new JCheckBox("add datapoints continuosly");
        checkbox.setSelected(false);
        buttonPanel.add(checkbox);

        this.add(buttonPanel, BorderLayout.NORTH);

        JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setViewportView(text);
        this.add(pane, BorderLayout.CENTER);
    }

    public void setRotation(double angle) {
        balanceSlider.setRotationAngle(angle);
        // System.out.println("DataCollector.setRotation(): "+angle);
    }

    public void addCurrentDatapoint() {
    	if (selectedVariables.size() == 0) {
            JOptionPane.showMessageDialog(this, "You have not selected any relevant variables,\na new datapoint has not been added.", "Session created", JOptionPane.INFORMATION_MESSAGE);

    	}
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
        logger.logAddRow(newRow);
        if (sandboxbutton.isSelected()) {
            sandbox.sendDataSetRow(newRow);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("adddata")) {
            addCurrentDatapoint();
        } else if (evt.getActionCommand().equals("configure")) {
            VariableSelectionDialog dialog = new VariableSelectionDialog(simquestViewer.getMainFrame(), this);
            dialog.setVisible(true);
        } else if (evt.getActionCommand().equals("sandbox")) {
            if (sandboxbutton.isSelected()) {
                initSandbox();
            } else {
                sandbox.disconnect();
                // sandbox.clear();
                sandbox = null;
                text.append("sandbox and session disconnected.\n");
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
        try {
            SimConfig config = new SimConfig(xmlSimConfig);
            setSimConfig(config);
        } catch (JDOMException ex) {
            JOptionPane.showMessageDialog(this, "Could not parse the SimConfig; the current simulation will not be changed.", "Parsing problem", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setSimConfig(SimConfig config) {
        ModelVariable var;
        try {
            if (config.getSimulationName().equals(getSimQuestViewer().getApplication().getTopic(0).getName())) {
                for (Iterator<Entry<String, String>> it = config.getVariables().entrySet().iterator(); it.hasNext();) {
                    Entry<String, String> entry = it.next();
                    var = getVariableByName(entry.getKey());
                    if (var != null) {
                        var.setValue(entry.getValue());
                    }
                }
            } else {
                // the simulation names doesn't match
                JOptionPane.showMessageDialog(this, "The name of the current simulation and the config doesn't match - nothing loaded.", "Config file problem", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Could not update the Simulation with this Configuration. Do they really match?", "Update problem", JOptionPane.WARNING_MESSAGE);
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

    public void cleanDataSet() {
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
            sandbox.clear();
            sandbox.sendHeaderMessage();
        }
        if (text != null) {
            text.setText("");
        }
    }

    public void setSelectedVariables(List<ModelVariable> selection) {
        selectedVariables = selection;
        cleanDataSet();
        logger.logSelectedVariables(selectedVariables);
    }

    private ModelVariable getVariableByName(String name) {
        ModelVariable modelVar = null;
        List<ModelVariable> varList = getSimQuestViewer().getDataServer().getVariables("dummy");
        for (Iterator<ModelVariable> it = varList.iterator(); it.hasNext();) {
            ModelVariable var = it.next();
            if (var.getName().equals(name)) {
                modelVar = var;
            }
        }
        return modelVar;
    }

    private void initSandbox() {
        try { // init the sandbox
        	if (tbi==null || tbi.getDataSyncService()==null) {
        		throw new CollaborationServiceException("no datasyncservice available");
        	}
            sandbox = new DatasetSandbox(this, tbi);
            text.append("sandbox initialised.\n");
            ISyncSession session = sandbox.createSession();
            text.append("session created: " + session.getId() + "\n");
            JOptionPane.showMessageDialog(this, "A sychronised session has been created; use the identifier\n in the text box below to join this session", "Session created", JOptionPane.INFORMATION_MESSAGE);
        } catch (CollaborationServiceException ex) {
            text.append("could not initialise sandbox.\n");
            text.append(ex.getMessage() + "\n");
            sandboxbutton.setSelected(false);
            JOptionPane.showMessageDialog(this, "A synchronised session could not be created.\nIs a DataSyncService really available?", "Session not created", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        logger.focusGained();

    }

    @Override
    public void windowClosed(WindowEvent e) {
        logger.toolStopped();

    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.toolStopped();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        logger.focusLost();

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    // logger.focusGained();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    // TODO Auto-generated method stub

    }

    public ScySimLogger getLogger() {
        return logger;
    }

    @Override
    public void windowIconified(WindowEvent e) {
    // logger.focusLost();

    }

}
