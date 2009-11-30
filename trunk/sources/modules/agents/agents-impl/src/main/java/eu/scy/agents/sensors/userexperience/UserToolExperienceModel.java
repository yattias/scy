package eu.scy.agents.sensors.userexperience;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.awt.image.BufferedImage;
import java.io.IOException;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class UserToolExperienceModel {

    private static final String USER_EXP_CHART = "user_exp_chart";

    private static final String USER_EXP = "user_exp";

    private String userName;

    private Map<String, Long> toolTimeMap;

    private String activeTool;

    private long startTime;

    private Lock lock = new ReentrantLock();

    private TupleSpace sensorSpace;

    private Map<String, TupleID> toolTIDMap;

    private TupleSpace toolAliveSpace;

    private static final Level DEBUGLEVEL = Level.FINE;

    private int startCount;

    private int stopCount;

    private static JFreeChart chart;

    private Map<String, TupleID> charts;

    private Map<String, XYSeries> series;

    private static XYSeriesCollection dataset;

    private long ustemStartTime;

    private static final Logger logger = Logger.getLogger(UserToolExperienceModel.class.getName());

    public UserToolExperienceModel(String userName, TupleSpace sensorSpace, TupleSpace toolAliveSpace, int startCount, int stopCount) {
       ustemStartTime = System.currentTimeMillis();
        initLogger();
        this.startCount = startCount;
        this.stopCount = stopCount;
        this.userName = userName;
        this.sensorSpace = sensorSpace;
        this.toolAliveSpace = toolAliveSpace;
        toolTimeMap = new HashMap<String, Long>();
        toolTIDMap = new HashMap<String, TupleID>();
        charts = new HashMap<String, TupleID>();
        series = new HashMap<String, XYSeries>();
    }

    public Set<String> getToolsList() {
        return toolTimeMap.keySet();
    }

    public long getExperience(String tool) {
        System.out.println("looking for tool "+tool);
        return toolTimeMap.get(tool);
    }

    public String getUserName() {
        return userName;
    }

    public void setActiveTool(String tool, long startTime, boolean started) throws TupleSpaceException, IOException {
        lock.lock();
        // requestAliveTuple();
        startCount = (started) ? startCount + 1 : startCount;
        activeTool = tool;
        this.startTime = startTime;
        //updateChart(tool);
        lock.unlock();
    }

    public void setToolInactive(String tool, long endTime, boolean stopped) throws IOException, TupleSpaceException {
        lock.lock();
        stopCount = (stopped) ? stopCount + 1 : stopCount;
        if (activeTool != null && activeTool.equals(tool)) {
            long timeToAdd = endTime - startTime;
            Long oldTime = toolTimeMap.get(tool);
            if (oldTime == null) {
                logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] Tool " + tool + " not yet in Map...will add it now");
                oldTime = 0l;
                Tuple t = new Tuple(USER_EXP, this.getUserName(), endTime, tool, oldTime, startCount, stopCount);
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
                    sensorSpace.update(tupleID, new Tuple(USER_EXP, this.getUserName(), endTime, tool, newTime, startCount, stopCount));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            } else {
                logger.log(Level.WARNING, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive]: TupleID not found!");
            }
            logger.log(Level.FINER, "[UserToolExperienceModel for User " + getUserName() + " and active Tool " + activeTool + "] [setToolInactive] User has now onlineTime for Tool " + activeTool + " of " + toolTimeMap.get(activeTool));
            updateChart(activeTool);
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
                    sensorSpace.update(tupleID, new Tuple(USER_EXP, this.getUserName(), updateTime, activeTool, newTime, startCount, stopCount));
                updateChart(activeTool);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
                   // updateChart(activeTool);
                    String user = (String) readTupleById.getField(1).getValue();
                    String tool = (String) readTupleById.getField(3).getValue();
                    updateChart(tool);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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

    private void requestAliveTuple() throws TupleSpaceException {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                logger.log(Level.FINE, "requestAlive-Thread started...user: " + getUserName() + " tool: " + activeTool);
                Tuple alive = null;
                boolean retrieveAliveOK = true;
                while (retrieveAliveOK) {
                    synchronized (this) {
                        try {
                            this.wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (activeTool != null) {
                        try {
                            alive = toolAliveSpace.read(new Tuple(activeTool, getUserName(), Field.createWildCardField()));
                        } catch (TupleSpaceException e) {
                            e.printStackTrace();
                            retrieveAliveOK = false;
                        }
                        if (alive == null) {
                            logger.log(Level.WARNING, "Alive tuple for tool " + activeTool + " from user " + getUserName() + " not found...assuming the tool crashed");
                            lock.lock();
                            activeTool = null;
                            lock.unlock();
                        }
                    }
                }
                logger.log(Level.FINE, "requestAliveTuple for user " + getUserName() + " and tool " + activeTool + "was stopped.");
            }
        });
        t.start();
    }

    public int getStarts() {
        return startCount;
    }

    public void setStarts(int startCount) {
        this.startCount = startCount;
    }

    public int getStops() {
        return stopCount;
    }

    public void setStops(int stopCount) {
        this.stopCount = stopCount;
    }

    public JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("User Experience", "User", "Experience", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        return chart;

    }

    public void createSeries(String tool) {
        String string = getUserName() + "/" + tool;
        XYSeries s = new XYSeries(string);
        dataset.addSeries(s);
        series.put(string, s);

    }

    public XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        return dataset;
    }

    public void updateChart(String tool) throws IOException, TupleSpaceException {
        if (tool==null){
            System.out.println();
        }
        if (chart == null) {
            dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            this.chart = chart;
        }
        if (!series.containsKey(getUserName() + "/" + tool)) {
            createSeries(tool);
        }
        XYSeries s = series.get(getUserName() + "/" + tool);
        s.add( getExperience(tool),System.currentTimeMillis()-ustemStartTime);
        System.out.println("added");
        getChart(sensorSpace, tool);

    }

    public void getChart(TupleSpace sensorSpace, String tool) throws IOException, TupleSpaceException {
        BufferedImage image = null;
        if (this.chart != null) {
            image = chart.createBufferedImage(800, 600);
            byte[] encodeAsPNG = ChartUtilities.encodeAsPNG(image);
            Tuple t = new Tuple(USER_EXP_CHART, encodeAsPNG);
            if (charts.get(tool) != null) {
                sensorSpace.update(charts.get(tool), t);
            } else {

                TupleID write = sensorSpace.write(t);
                charts.put(tool,write);
            }
        }
    }

}
