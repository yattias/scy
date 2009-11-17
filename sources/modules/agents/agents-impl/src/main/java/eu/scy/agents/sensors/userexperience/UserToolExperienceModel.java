package eu.scy.agents.sensors.userexperience;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserToolExperienceModel {

    private String userName;

    private HashMap<String, Long> toolTimeMap;

    private String activeTool;

    private long startTime;

    private Lock lock = new ReentrantLock();

    private TupleSpace sensorSpace;

    private HashMap<String, TupleID> toolTIDMap;

    public UserToolExperienceModel(String userName, TupleSpace sensorSpace) {
        this.userName = userName;
        this.sensorSpace = sensorSpace;
        toolTimeMap = new HashMap<String, Long>();
        toolTIDMap = new HashMap<String, TupleID>();
    }

    public Set<String> getToolsList() {
        return toolTimeMap.keySet();
    }

    public long getExperience(String tool) {
        return toolTimeMap.get(tool);
    }

    public String getUserName() {
        return userName;
    }

    public void setActiveTool(String tool, long startTime) {
        lock.lock();
        activeTool = tool;
        this.startTime = startTime;
        lock.unlock();
    }

    public void setToolInactive(String tool, long endTime) {
        lock.lock();
        if (activeTool != null && activeTool.equals(tool)) {
            long timeToAdd = endTime - startTime;
            Long oldTime = toolTimeMap.get(tool);
            if (oldTime == null) {
                System.out.println("Tool " + tool + " not yet in Map...will add it now");
                oldTime = 0l;
                Tuple t = new Tuple("user_exp", this.getUserName(), endTime, tool, oldTime);
                TupleID id = null;
                try {
                    id = sensorSpace.write(t);
                    toolTIDMap.put(tool, id);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("[INACTIVE] Old time is "+oldTime);
            long newTime = oldTime + timeToAdd;
            toolTimeMap.put(tool, newTime);
            TupleID tupleID = toolTIDMap.get(tool);
            if (tupleID != null) {
                try {
                    sensorSpace.update(tupleID, new Tuple("user_exp", this.getUserName(), endTime, tool, newTime));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("[setToolInactive]: TupleID not found!");
            }
            System.out.println("");
            System.out.println("[INACTIVE]User " + this.getUserName() + " has now onlineTime for Tool " + activeTool + " of " + toolTimeMap.get(activeTool));
            System.out.println("");
            activeTool = null;

        } else {
            System.err.println("Fatal! The Tool " + tool + " that should be closed isn't open! The open Tool is " + activeTool);
        }
        lock.unlock();
        System.out.println("User " + getUserName() + " with tool " + tool + " has exp of " + toolTimeMap.get(tool));
    }

    public void updateActiveToolExperience(long intervall, long lastActionTime) {
        lock.lock();
        if (activeTool != null) {
            Long oldTime = toolTimeMap.get(activeTool);
            if (oldTime == null) {
                System.out.println("Tool " + activeTool + " not yet in Map...will add it now");
                oldTime = 0l;
            }
            long newTime = oldTime + intervall;
            toolTimeMap.put(activeTool, newTime);
            TupleID tupleID = toolTIDMap.get(activeTool);
            if (tupleID != null) {
                try {
                    sensorSpace.update(tupleID, new Tuple("user_exp", this.getUserName(), lastActionTime, activeTool, newTime));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("[updateActiveToolExperience]: TupleID not found!");
            }
            startTime += intervall;
            System.out.println("");
            System.out.println("[UPDATE]User " + this.getUserName() + " has now onlineTime for Tool " + activeTool + " of " + toolTimeMap.get(activeTool));
            System.out.println("");
        } else {
            System.out.println("no active tool found...for user " + this.getUserName());
            HashMap<String, TupleID> clone = (HashMap<String, TupleID>) toolTIDMap.clone();
            Collection<TupleID> values = clone.values();
            for (TupleID tupleID : values) {
                try {
                    Tuple readTupleById = sensorSpace.readTupleById(tupleID);
                    sensorSpace.update(tupleID, readTupleById);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }
        }
        lock.unlock();
    }

    public void setToolTime(String tool, long time) {
        toolTimeMap.put(tool, time);
    }

    public void setToolTID(String tool, TupleID tid) {
        toolTIDMap.put(tool, tid);
    }
}
