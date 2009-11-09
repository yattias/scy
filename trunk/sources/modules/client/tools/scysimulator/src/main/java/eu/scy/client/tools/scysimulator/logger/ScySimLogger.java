package eu.scy.client.tools.scysimulator.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jdom.output.XMLOutputter;

import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.elo.contenttype.dataset.DataSetRow;

public class ScySimLogger implements ActionListener, IDataClient {

    private String username = "default_username";

    private String toolname;

    private String missionname;

    private String sessionname;

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

    private XMLOutputter xmlOutputter;

    public ScySimLogger(DataServer dataServer) {
        this.dataServer = dataServer;
        xmlOutputter = new XMLOutputter();
        // TODO: these properties have to be fetched from the SCY environment
        username = System.getProperty("user.name");
        missionname = "mission 1";
        toolname = "scysimulator";
        sessionname = "testsession";
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
        // TODO that's the way!
        // ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
        actionLogger = new SQLSpacesActionLogger("localhost", 2525, "actionSpace");
        //actionLogger = new SystemOutActionLogger();
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
        IAction action = new Action();
        action.setUser(username);
        action.setType(type);
        action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, missionname);
        action.addContext(ContextConstants.session, sessionname);
        return action;
    }

    public void logAddRow(DataSetRow newRow) {
        action = createBasicAction("add_row");
        action.addAttribute("dataRow", xmlOutputter.outputString(newRow.toXML()));
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

    // TODO Should be logged from SCY-LAB?
    public void toolStarted() {
        IAction loginAction = createBasicAction("tool started");
        write(loginAction);
    }

    // TODO Should be logged from SCY-LAB?
    public void toolStopped() {
        IAction loginAction = createBasicAction("tool stopped");
        write(loginAction);
    }

    public void focusGained() {
        IAction focusGained = createBasicAction("focus gained");
        write(focusGained);
    }

    // TODO Should be logged from SCY-LAB?
    public void focusLost() {
        IAction focusLost = createBasicAction("focus lost");
        write(focusLost);
    }

}
