package eu.scy.agents.sensors.userexperience;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UserToolExperienceModel {

    private static final String USER_EXP = "user_exp";

    private String userName;

    private Map<String, Long> toolTimeMap;

    private String activeTool;

    private long startTime;

    private Lock lock = new ReentrantLock();

    private TupleSpace sensorSpace;
    private Map<String, TupleID> toolTIDMap;

    private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(UserToolExperienceModel.class.getName());

    public UserToolExperienceModel(String userName, TupleSpace sensorSpace) {
        initLogger();
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
                logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] Tool " + tool + " not yet in Map...will add it now");
                oldTime = 0l;
                Tuple t = new Tuple(USER_EXP, this.getUserName(), endTime, tool, oldTime);
                TupleID id = null;
                try {
                    id = sensorSpace.write(t);
                    toolTIDMap.put(tool, id);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }

            }
            long newTime = oldTime + timeToAdd;
            logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] TimeToAdd: " + timeToAdd);
            toolTimeMap.put(tool, newTime);
            TupleID tupleID = toolTIDMap.get(tool);
            if (tupleID != null) {
                try {
                    sensorSpace.update(tupleID, new Tuple(USER_EXP, this.getUserName(), endTime, tool, newTime));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            } else {
                logger.log(Level.WARNING, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive]: TupleID not found!");
            }
            logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] User has now onlineTime for Tool " + activeTool + " of " + toolTimeMap.get(activeTool));
            activeTool = null;

        } else {
            logger.log(Level.SEVERE, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] Fatal! The Tool " + tool + " that should be closed isn't open! The open Tool is " + activeTool);
        }
        lock.unlock();
        logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] User with tool " + tool + " has experience (in MS) of " + toolTimeMap.get(tool));
    }

    public void updateActiveToolExperience(long intervall, long updateTime) {
        lock.lock();
        if (activeTool != null) {
            Long oldTime = toolTimeMap.get(activeTool);
            if (oldTime == null) {
                logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] Tool " + activeTool + " [updateActiveToolExperience] Not yet in Map...will add it now");
                oldTime = 0l;
            }
            long newTime = oldTime + (updateTime - startTime);
            toolTimeMap.put(activeTool, newTime);
            TupleID tupleID = toolTIDMap.get(activeTool);
            if (tupleID != null) {
                try {
                    sensorSpace.update(tupleID, new Tuple(USER_EXP, this.getUserName(), updateTime, activeTool, newTime));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            } else {
                logger.log(Level.WARNING, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] Tool " + activeTool + " [updateActiveToolExperience] TupleID not found!");
            }
            startTime = updateTime;
            logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] Tool " + activeTool + " [updateActiveToolExperience] User " + this.getUserName() + " has now onlineTime for Tool " + activeTool + " of " + toolTimeMap.get(activeTool));
        } else {
            logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] Tool " + activeTool + " [updateActiveToolExperience] User " + this.getUserName() + " no active tool found...for user " + this.getUserName());
            Collection<TupleID> values = toolTIDMap.values();
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

    private void initLogger() {
        ConsoleHandler cH = new ConsoleHandler();
        SimpleFormatter sF = new SimpleFormatter();
        cH.setFormatter(sF);
        cH.setLevel(DEBUGLEVEL);
        logger.setLevel(DEBUGLEVEL);
        logger.addHandler(cH);
    }
}
