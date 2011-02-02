package eu.scy.toolbroker;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class SessionSupervisor implements ActionListener {

    private final int INTERVALL = 30 * 1000;

    private boolean started = false;

    private Timer timer;

    private String activeTool;

    private String eloUri;

    private TupleSpace ts;

    private TupleID tid;

    public SessionSupervisor(TupleSpace ts) {
        this.ts = ts;
        timer = new Timer(INTERVALL, this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateTuple();

    }

    private void updateTuple() {
        if (tid == null) {
            // FIRST TIME
            try {
                Tuple tuple = new Tuple("session_alive", activeTool, eloUri);
                tuple.setExpiration(INTERVALL + 1000);
                tid = ts.write(tuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Tuple tuple = new Tuple("session_alive", activeTool, eloUri);
                tuple.setExpiration(INTERVALL + 1000);
                ts.update(tid, tuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    public void startUpdate(String activeTool, String eloURI) {
        this.activeTool = activeTool;
        this.eloUri = eloURI;
        timer.start();
        started = true;
    }

    public void stopUpdate() {
        if (started) {
            timer.stop();
        } else {
            // TODO replace with logging
            System.err.println("STOP command received but the timer is not active...");
        }
    }

    public String getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(String activeTool) {
        this.activeTool = activeTool;
        if (started) {
            updateTuple();
        }
    }

    public String getEloUri() {
        return eloUri;
    }

    public void setEloUri(String eloUri) {
        this.eloUri = eloUri;
        if (started) {
            updateTuple();
        }
    }
}
