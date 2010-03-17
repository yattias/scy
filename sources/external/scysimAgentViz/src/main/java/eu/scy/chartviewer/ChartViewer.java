package eu.scy.chartviewer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.util.Base64;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.dgc.VMID;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class ChartViewer implements Callback, ActionListener, MouseListener {

    private static final int UPDATE_DELAY = 6 * 1000;

    private static final String FRAME_TITLE = "GraphViewer Version 0.1";

    private static final String VALUE_AXIS_LABEL = "Experience/Votat/Canonical";

    private static final String CHART_TITLE = "Agent Output";

    private static final String TIME_AXIS_LABEL = "Time";

    private static final int TS_PORT = 2525;

    private static final String TS_HOST = "scy.collide.info";

    private static final String SENSOR_SPACE = "command";

    private static final Tuple USER_EXP_TEMPLATE = new Tuple("user_exp", Field.createWildCardField());

    private static final Tuple VOTAT_TEMPLATE = new Tuple("votat", Field.createWildCardField());

    private static final Tuple CANONICAL_TEMPLATE = new Tuple("inc_change", Field.createWildCardField());

    private JFrame jf;

    private TupleSpace sensorSpace;

    private TupleSpace commandSpace;

    private int votatSeq;

    private int userExpSeq;

    private int canonicalSeq;

    private TimeSeriesCollection dataset;

    private JFreeChart chart;

    private Map<String, TimeSeries> expSeries;

    private Map<String, TimeSeries> votatSeries;

    private Map<String, TimeSeries> canoSeries;

    private long startTime;

    private Tuple lastUserExp;

    private Tuple lastVotat;

    private Tuple lastCano;

    private Timer timer;

    private ReentrantLock lock;

    private static final double MAX_EXP_TIME = 1 * 60 * 60 * 1000;

    private static final String COMMAND_SPACE = "command";

    private ChartPanel panel;

    private Vector<String> activeUsers;

    public enum SeriesType {
        EXP_SERIES("User Experience"),
        VOTAT_SERIES("Votat"),
        CANO_SERIES("Canonical"),
        ALL("All");

        private String name;

        private SeriesType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public void rebuildFromSpace() throws TupleSpaceException, IOException {
        Tuple[] allCano = sensorSpace.readAll(CANONICAL_TEMPLATE);
        Tuple[] allExp = sensorSpace.readAll(USER_EXP_TEMPLATE);
        Tuple[] allVotat = sensorSpace.readAll(VOTAT_TEMPLATE);
        for (Tuple tuple : allCano) {
            updateChart((String) tuple.getField(2).getValue(), (String) tuple.getField(1).getValue(), SeriesType.CANO_SERIES, tuple, false, Long.parseLong(tuple.getField(5).getValue().toString()));
        }
        for (Tuple tuple : allVotat) {
            updateChart((String) tuple.getField(2).getValue(), (String) tuple.getField(1).getValue(), SeriesType.VOTAT_SERIES, tuple, false, Long.parseLong(tuple.getField(5).getValue().toString()));
        }
        for (Tuple tuple : allExp) {
            updateChart((String) tuple.getField(2).getValue(), (String) tuple.getField(1).getValue(), SeriesType.EXP_SERIES, tuple, false, Long.parseLong(tuple.getField(5).getValue().toString()));
        }

    }

    public ChartViewer() {
        try {
            // tbi = new ToolBrokerImpl("ChartViewer","chartviewer");
            lock = new ReentrantLock();
            activeUsers = new Vector<String>();
            jf = new JFrame(FRAME_TITLE);
            jf.setSize(900, 900);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.setVisible(true);
            dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            JMenuItem notItem = new JMenuItem("Notification");
            notItem.setActionCommand("notitem");
            notItem.addActionListener(this);
            panel.getPopupMenu().add(notItem);
            expSeries = new HashMap<String, TimeSeries>();
            votatSeries = new HashMap<String, TimeSeries>();
            canoSeries = new HashMap<String, TimeSeries>();
            this.chart = chart;
            startTime = 0;
            sensorSpace = new TupleSpace(new User("ChartViewer"), TS_HOST, TS_PORT, SENSOR_SPACE);
            commandSpace = new TupleSpace(new User("ChartViewer"), TS_HOST, TS_PORT, COMMAND_SPACE);
            rebuildFromSpace();
            userExpSeq = sensorSpace.eventRegister(Command.ALL, USER_EXP_TEMPLATE, this, true);
            votatSeq = sensorSpace.eventRegister(Command.ALL, VOTAT_TEMPLATE, this, true);
            canonicalSeq = sensorSpace.eventRegister(Command.ALL, CANONICAL_TEMPLATE, this, true);
            timer = new Timer(UPDATE_DELAY, this);
            timer.start();
            panel.repaint();
            panel.validate();

        } catch (TupleSpaceException e) {
            if (e.getMessage().contains("establish")) {
                JOptionPane.showMessageDialog(jf, e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChartViewer();

    }

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        try {
            if (seqnum == votatSeq) {
                updateChart((String) afterTuple.getField(2).getValue(), (String) afterTuple.getField(1).getValue(), SeriesType.VOTAT_SERIES, afterTuple, false, System.currentTimeMillis());
            } else if (seqnum == userExpSeq) {
                updateChart((String) afterTuple.getField(2).getValue(), (String) afterTuple.getField(1).getValue(), SeriesType.EXP_SERIES, afterTuple, false, System.currentTimeMillis());
                timer.restart();
            } else if (seqnum == canonicalSeq) {
                updateChart((String) afterTuple.getField(2).getValue(), (String) afterTuple.getField(1).getValue(), SeriesType.CANO_SERIES, afterTuple, false, System.currentTimeMillis());
            } else {
                System.err.println("Callback without registered seqnum arrived!");
            }
        } catch (TupleSpaceException tse) {
            tse.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(CHART_TITLE, TIME_AXIS_LABEL, VALUE_AXIS_LABEL, dataset, true, true, true);
        XYPlot plot = (XYPlot) chart.getPlot();

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setItemLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        renderer.setStroke(new BasicStroke(2));
        DateAxis daxis = (DateAxis) plot.getDomainAxis();
        DateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
        daxis.setDateFormatOverride(formatter);
        chart.getXYPlot().getDomainAxis().setAutoRange(true);
        chart.getXYPlot().getRangeAxis().setLowerBound(0);
        chart.getXYPlot().getRangeAxis().setUpperBound(110);
        panel = new ChartPanel(chart);
        jf.add(panel);
        return chart;
    }

    public void createSeries(String tool, String userName, SeriesType type) {
        String seriesName = getSeriesName(tool, userName, type);
        if (!activeUsers.contains(userName)) {
            activeUsers.add(userName);
        }
        TimeSeries s = new TimeSeries(seriesName, Millisecond.class);
        s.setDescription(type.toString());
        dataset.addSeries(s);
        switch (type) {
            case CANO_SERIES:
                canoSeries.put(seriesName, s);
                break;
            case VOTAT_SERIES:
                votatSeries.put(seriesName, s);
                break;
            case EXP_SERIES:
                expSeries.put(seriesName, s);
                break;
            default:
                break;
        }
    }

    public TimeSeriesCollection createDataset() {
        dataset = new TimeSeriesCollection();
        return dataset;
    }

    public void updateChart(String tool, String userName, SeriesType type, Tuple changeTuple, boolean update, long timeOccured) throws IOException, TupleSpaceException {
        lock.lock();
        if (tool == null) {
            System.err.println("Tool is null");
        }
        if (chart == null) {
            dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            this.chart = chart;
        }
        TimeSeries s;
        switch (type) {
            case EXP_SERIES:
                if (!expSeries.containsKey(getSeriesName(tool, userName, type))) {
                    createSeries(tool, userName, type);
                }
                s = expSeries.get(getSeriesName(tool, userName, type));

                double l = (Long) changeTuple.getField(6).getValue() / MAX_EXP_TIME * 100;
                l = Math.min(l, 100);
                long time;
                if (update) {

                    time = System.currentTimeMillis() - startTime;
                } else {

                    time = timeOccured - startTime;
                }
                lastUserExp = changeTuple;
                s.addOrUpdate(new Millisecond(new Date(time)), l);
                break;
            case VOTAT_SERIES:
                if (!votatSeries.containsKey(getSeriesName(tool, userName, type))) {
                    createSeries(tool, userName, type);
                }
                s = votatSeries.get(getSeriesName(tool, userName, type));
                double d = (Double) changeTuple.getField(6).getValue() * 100;
                long time2;
                if (update) {

                    time2 = System.currentTimeMillis() - startTime;
                } else {

                    time2 = changeTuple.getLastModificationTimestamp() - startTime;
                }
                lastVotat = changeTuple;
                s.add(new Millisecond(new Date(time2)), d);
                System.out.println(votatSeries);
                break;
            case CANO_SERIES:
                if (!canoSeries.containsKey(getSeriesName(tool, userName, type))) {
                    createSeries(tool, userName, type);
                }
                s = canoSeries.get(getSeriesName(tool, userName, type));
                double d2 = (Double) changeTuple.getField(6).getValue() * 100;
                long time3;
                if (update) {
                    time3 = System.currentTimeMillis() - startTime;
                } else {
                    time3 = changeTuple.getLastModificationTimestamp() - startTime;
                }
                lastCano = changeTuple;
                s.addOrUpdate(new Millisecond(new Date(time3)), d2);
                break;
            default:
                break;
        }
        lock.unlock();
    }

    public Date convertLongToDate(long time) {
        Date d = new Date(time);
        return d;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() != null && e.getActionCommand().equals("notitem")) {
            final JDialog jd = new JDialog(jf);
            jd.setSize(400, 250);
            jd.setModal(true);
            jd.setLayout(new BorderLayout());
            final JList list = new JList(activeUsers);
            // tbi.getAwarenessService().
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setBorder(BorderFactory.createTitledBorder("Active users"));
            list.setBackground(Color.GRAY.brighter().brighter());
            jd.add(list, BorderLayout.NORTH);
            final JTextArea jtf = new JTextArea("Please input notification");
            jtf.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseClicked(MouseEvent e) {
                    jtf.setSelectionStart(0);
                    jtf.setSelectionEnd(jtf.getText().length());
                }
            });
            jtf.setBorder(BorderFactory.createTitledBorder("Notification message"));
            jd.add(jtf, BorderLayout.CENTER);
            final JCheckBox cb = new JCheckBox("Attach image?");
            final JCheckBox popup = new JCheckBox("Popup?");
            JButton okB = new JButton("Send");
            okB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (list.getSelectedValue() == null) {
                        JOptionPane.showMessageDialog(jd, "You havn't selected any receiver.");
                    } else {
                        Tuple t;
                        if (cb.isSelected()) {
                            BufferedImage image = chart.createBufferedImage(450, 450);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            try {
                                ImageIO.write(image, "PNG", baos);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            byte[] bytesOut = baos.toByteArray();
                            String string = Base64.encodeToString(bytesOut, false);
                            t = new Tuple("notification", new VMID().toString(), (String) list.getSelectedValue(), "SCY Simulator", "ChartViewer", "mission1", "session1", "message=" + jtf.getText(), "popup=" + popup.isSelected(), "image=" + string);
                        } else {
                            t = new Tuple("notification", new VMID().toString(), (String) list.getSelectedValue(), "SCY Simulator", "ChartViewer", "mission1", "session1", "popup=" + popup.isSelected(), "message=" + jtf.getText());
                        }
                        try {
                            commandSpace.write(t);
                            JOptionPane.showMessageDialog(jd, "Message sent successfully");
                            jd.dispose();
                        } catch (TupleSpaceException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            JPanel southPanel = new JPanel(new GridLayout(2, 1));
            southPanel.add(cb);
            southPanel.add(popup);
            southPanel.add(okB);
            jd.add(southPanel, BorderLayout.SOUTH);
            jd.setAlwaysOnTop(true);
            jd.setName("Send Notification");
            jd.setLocationRelativeTo(jf);
            jd.setVisible(true);
        } else {
            try {
                if (lastCano != null) {
                    updateChart((String) lastCano.getField(2).getValue(), (String) lastCano.getField(1).getValue(), SeriesType.CANO_SERIES, lastCano, true, System.currentTimeMillis());
                }
                if (lastUserExp != null) {
                    updateChart((String) lastUserExp.getField(2).getValue(), (String) lastUserExp.getField(1).getValue(), SeriesType.EXP_SERIES, lastUserExp, true, System.currentTimeMillis());
                }
                if (lastVotat != null) {
                    updateChart((String) lastVotat.getField(2).getValue(), (String) lastVotat.getField(1).getValue(), SeriesType.VOTAT_SERIES, lastVotat, true, System.currentTimeMillis());
                }
            } catch (TupleSpaceException tse) {
                tse.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private String getSeriesName(String tool, String userName, SeriesType type) {
        userName = userName.split("@")[0];
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        String name = type.getName() + ": " + userName + "(Tool: " + tool + ")";
        return name;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
