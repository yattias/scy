package eu.scy.client.tools.scysimulator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;
import sqv.ModelVariable;
import sqv.data.DataServer;

/**
 *
 * @author lars
 *
 * This class is used to show a (possible) effect of changing the rotation angle
 * of a ScyWindow. This quick and dirty implemention will only work for the
 * "balance" simulation.
 */
public class BalanceSlider {

    private DataServer dataServer;
    private List<ModelVariable> slideVars;
    private ModelVariable simulatorAction;
    Double step;

    BalanceSlider(DataServer dataServer) {
        this.dataServer = dataServer;
        step = 0.0;
        slideVars = new ArrayList();
        List<ModelVariable> variables = dataServer.getVariables("");
        for (ModelVariable variable : variables) {
            if (variable.getName().equals("l1") || variable.getName().equals("l2")) {
                slideVars.add(variable);
            }
        }
        new Timer(100, updateAction).start();
    }

    public void setRotationAngle(double angle) {
        step = Math.sin(Math.toRadians(angle)) / 5;
        if (Math.abs(step) < 0.005) {
            // if the inclination is very small, stop moving
            step = 0.0;
        }
    }

    public void slide(ModelVariable var) {
        double newval = var.getValue() + step;
        if (newval < -1.5) {
            newval = -1.5;
        } else if (newval > 1.5) {
            newval = 1.5;
        }
        var.setValue(newval);
    }

    Action updateAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            if (step != 0.0) {
                for (ModelVariable var : slideVars) {
                    slide(var);
                }
                dataServer.updateClients();
                if (step > 0) {
                    step = step + 0.02;
                } else {
                    step = step - 0.02;
                }
            }
        }
    };
}