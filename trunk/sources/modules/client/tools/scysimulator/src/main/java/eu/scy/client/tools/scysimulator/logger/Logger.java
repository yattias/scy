package eu.scy.client.tools.scysimulator.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import roolo.elo.api.IMetadataKey;
import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.Action;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbroker.ToolBrokerImpl;

public class Logger implements ActionListener, IDataClient {

    private static final String HOST = "localhost";

    private static final int PORT = 2525;

    private static final String COMMAND_SPACE = "command";

    private String username;

    private String toolname;

    private String missionname;

    private XMLOutputter outputter;

    private DataServer dataServer;

    private ArrayList<Double> oldInputValues;

    private ArrayList<Double> oldOutputValues;

    private ArrayList<ModelVariable> inputVariables;

    private ArrayList<ModelVariable> outputVariables;

    private IAction action;

    private IAction lastInputVariableValueChangedAction;

    private IAction lastOutputVariableValueChangedAction;

    private Timer inputVariableTimer;

    private Timer outputVariableTimer;

    private IAction firstInputVariableValueChangedAction;

    private IAction firstOutputVariableValueChangedAction;

    private IActionLogger actionLogger;

    public Logger(DataServer dataServer) {
        this.dataServer = dataServer;
        // TODO: these properties have to be fetched from the SCY environment
        username = System.getProperty("user.name");
        missionname = "mission 1";
        toolname = "scysimulator";
        outputter = new XMLOutputter(Format.getPrettyFormat());
        DataAgent dataAgent = new BasicDataAgent(this, dataServer);
        // find input variables
        inputVariables = getVariables(ModelVariable.VK_INPUT);
        outputVariables = getVariables(ModelVariable.VK_OUTPUT, "Mtot");
        // store the values to find the changed value later
        storeOldValues();
        // subscribe to variables
        dataAgent.add(inputVariables);
        dataAgent.add(outputVariables);
        // register

        dataAgent.register();
        inputVariableTimer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IAction action = createBasicAction("value_changed");
                action.addAttribute("name", lastInputVariableValueChangedAction.getAttribute("name"));
                action.addAttribute("oldValue", firstInputVariableValueChangedAction.getAttribute("oldValue"));
                action.addAttribute("newValue", lastInputVariableValueChangedAction.getAttribute("newValue"));
                write(action);
            }
        });
        inputVariableTimer.setRepeats(false);
        outputVariableTimer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IAction action = createBasicAction("value_changed");
                action.addAttribute("name", lastOutputVariableValueChangedAction.getAttribute("name"));
                action.addAttribute("oldValue", firstOutputVariableValueChangedAction.getAttribute("oldValue"));
                action.addAttribute("newValue", lastOutputVariableValueChangedAction.getAttribute("newValue"));
                write(action);
            }
        });
        outputVariableTimer.setRepeats(false);

        ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
        actionLogger = tbi.getActionLogger();

    }

    private ArrayList<ModelVariable> getVariables(int mv) {
        ArrayList<ModelVariable> variables = new ArrayList<ModelVariable>();
        for (ModelVariable variable : dataServer.getVariables("")) {
            if (variable.getKind() == mv) {
                System.out.println("Logger added variable: " + variable.getName() + " type: " + variable.getKind());
                variables.add(variable);
            }
        }
        return variables;
    }

    private ArrayList<ModelVariable> getVariables(int mv, String name) {
        ArrayList<ModelVariable> variables = new ArrayList<ModelVariable>();
        for (ModelVariable variable : dataServer.getVariables("")) {
            if (variable.getKind() == mv && variable.getName().equals(name)) {
                System.out.println("Logger added variable: " + variable.getName() + " type: " + variable.getKind());
                variables.add(variable);
            }
        }
        return variables;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ScyDynamics.Logger actionPerformed.");
    }

    @Override
    public void updateClient() {
        for (int i = 0; i < inputVariables.size(); i++) {
            if (inputVariables.get(i).getValue() != oldInputValues.get(i)) {
                logValueChanged(inputVariables.get(i).getName(), oldInputValues.get(i), inputVariables.get(i).getValue());
            }
        }
        for (int i = 0; i < outputVariables.size(); i++) {
            if (outputVariables.get(i).getValue() != oldOutputValues.get(i)) {
                logValueChanged(outputVariables.get(i).getName(), oldOutputValues.get(i), outputVariables.get(i).getValue());
            }
        }
        storeOldValues();
    }

    private void logValueChanged(String name, double oldValue, double newValue) {
        action = createBasicAction("value_changed");
        action.addAttribute("name", name);
        action.addAttribute("oldValue", oldValue + "");
        action.addAttribute("newValue", newValue + "");
        for (ModelVariable inVar : inputVariables) {
            if (inVar.getName().equals(name)) {
                if (!inputVariableTimer.isRunning()) {
                    firstInputVariableValueChangedAction = action;
                }
                lastInputVariableValueChangedAction = action;
                inputVariableTimer.restart();
                break;
            }

        }
        for (ModelVariable outVar : outputVariables) {
            if (outVar.getName().equals(name)) {
                if (!outputVariableTimer.isRunning()) {
                    firstOutputVariableValueChangedAction = action;
                }
                lastOutputVariableValueChangedAction = action;
                outputVariableTimer.restart();
                break;
            }
        }
    }

    private void storeOldValues() {
        oldInputValues = new ArrayList<Double>();
        oldOutputValues = new ArrayList<Double>();
        for (ModelVariable invar : inputVariables) {
            oldInputValues.add(invar.getValue());
        }
        for (ModelVariable outvar : outputVariables) {
            oldOutputValues.add(outvar.getValue());
        }
    }

    public IAction createBasicAction(String type) {
        IAction action = new Action(type, username);
        action.addContext("tool", toolname);
        action.addContext("mission", missionname);
        return action;
    }

    public void logAddRow(DataSetRow newRow) {
        action = createBasicAction("add_row");
        action.addAttribute("dataRow", "", newRow.toXML());
        write(action);
    }

    public void logSelectedVariables(List<ModelVariable> selectedVariables) {
        action = createBasicAction("variables_selected");
        String selection = new String();
        for (ModelVariable var : selectedVariables) {
            selection = selection + var.getName() + ", ";
        }
        if (selection.length() > 2) {
            selection = selection.substring(0, selection.length() - 2);
        }
        action.addAttribute("selected_variables", selection);
        write(action);
    }

    private void write(IAction action) {
        actionLogger.log(username, toolname, action);
        // outputter.output(action.getXML(), System.out);
    }

    public void toolStarted() {
        IAction loginAction = createBasicAction("tool started");
        write(loginAction);
    }
    public void toolStopped() {
        IAction loginAction = createBasicAction("tool stopped");
        write(loginAction);
    }
    
    public void focusGained() {
        IAction focusGained = createBasicAction("focus gained");
        write(focusGained);
    }
    
    public void focusLost() {
        IAction focusLost = createBasicAction("focus lost");
        write(focusLost);
    }
    

}
