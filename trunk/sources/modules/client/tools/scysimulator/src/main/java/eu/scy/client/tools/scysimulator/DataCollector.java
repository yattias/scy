package eu.scy.client.tools.scysimulator;

import info.collide.sqlspaces.commons.util.Base64;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import sqv.ISimQuestViewer;
import sqv.ModelVariable;
import sqv.data.IDataClient;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scysimulator.logger.ScySimLogger;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.notification.api.INotification;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javax.swing.JTable;

/**
 * This class collects datapoints from a SimQuest simulation and stores them as an ELO.
 * 
 * @author Lars Bollen
 * 
 */
public class DataCollector extends JPanel implements ActionListener, IDataClient {

    private ModelVariable rotationVariable = null;

    private final ResourceBundleWrapper bundle;

    public enum SCAFFOLD {

        VOTAT,
        INC_CHANGE,
        EXTREME_VALUES,
        CONFIRM_HYPO,
        IDENT_HYPO,
        SHOWBUTTON;
    }

    private JScrollPane pane;

    private JTable table;

    private ISimQuestViewer simquestViewer;

    private SCYDataAgent dataAgent;

    private List<ModelVariable> simulationVariables;

    private List<ModelVariable> selectedVariables;

    private JCheckBox checkbox;

    private DataSet dataset;

    private DatasetTableModel tableModel;

    private DatasetSandbox sandbox = null;

    private BalanceSlider balanceSlider = null;

    private ScySimLogger logger;

    private Logger debugLogger;

    private ToolBrokerAPI tbi;

    private JButton notifyButton;

    private String notificationMessage;

    protected boolean notify;

    private boolean notThreadRunning = false;

    private Vector<String> shownMessages;

    private String notificationSender;

    public DataCollector(ISimQuestViewer simquestViewer, ToolBrokerAPI tbi, String eloURI) {
        this.bundle = new ResourceBundleWrapper(this);
        // initialize the logger(s)
        debugLogger = Logger.getLogger(DataCollector.class.getName());
        shownMessages = new Vector<String>();
        this.tbi = tbi;
        if (tbi != null) {
            debugLogger.info("setting action logger to " + tbi.getActionLogger());
            logger = new ScySimLogger(simquestViewer.getDataServer(), tbi.getActionLogger(), eloURI);
            logger.setUsername(tbi.getLoginUserName());
        } else {
            debugLogger.info("setting action logger to DevNullActionLogger");
            logger = new ScySimLogger(simquestViewer.getDataServer(), new DevNullActionLogger(), eloURI);
        }
        // logger.sendListOfInputVariables();
        logger.logListOfVariables("input_variables", logger.getInputVariables());
        setSelectedVariables(new ArrayList<ModelVariable>());
        // initialize user interface
        initGUI();
        // setting some often-used variable
        this.simquestViewer = simquestViewer;
        simulationVariables = simquestViewer.getDataServer().getVariables("name is not relevant");
        // setSelectedVariables(simquestViewer.getDataServer().getVariables("name is not relevant"));

        // register agent
        dataAgent = new SCYDataAgent(this, simquestViewer.getDataServer());
        dataAgent.add(simquestViewer.getDataServer().getVariables("name is not relevant"));
        simquestViewer.getDataServer().register(dataAgent);

        // rotation awareness stuff
        for (ModelVariable var : simquestViewer.getDataServer().getVariables("name is not relevant")) {
            if (var.getName().equals("rotation")) {
                rotationVariable = var;
                debugLogger.info("rotation variable found.");
            }
        }
        if (simquestViewer.getApplication().getHeader().getDescription().equals("balance")) {
            balanceSlider = new BalanceSlider(simquestViewer.getDataServer());
            debugLogger.info("balance simulation found.");
        }
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(getBundleString("DATACOLLECTOR_TITLE")));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton button = new JButton(this.getBundleString("DATACOLLECTOR_SELECT_VARIABLES"));
        button.setActionCommand("configure");
        button.addActionListener(this);
        buttonPanel.add(button);

        button = new JButton(getBundleString("DATACOLLECTOR_ADD_DATAPOINT"));
        button.setActionCommand("adddata");
        button.addActionListener(this);
        buttonPanel.add(button);

        checkbox = new JCheckBox(getBundleString("DATACOLLECTOR_ADD_DATAPOINTS_CONT"));
        checkbox.setSelected(false);
        buttonPanel.add(checkbox);
        // URL imageUrl = this.getClass().getResource("pc.gif");
        // ImageIcon notificationImage = new ImageIcon(imageUrl);

        notifyButton = new JButton("!");
        notifyButton.setFont(notifyButton.getFont().deriveFont(Font.BOLD));
        notifyButton.setVisible(false);
        notifyButton.setActionCommand("notification");
        notifyButton.addActionListener(this);
        Dimension ps = notifyButton.getPreferredSize();
        notifyButton.setPreferredSize(new Dimension((int) (ps.getWidth() * 1.2), (int) (ps.getHeight() * 1.2)));

        // ImageBackgroundPanel imp = new ImageBackgroundPanel(notificationImage);
        buttonPanel.add(notifyButton);
        this.add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DatasetTableModel(getSelectedVariables());
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setSize(550, 140);
        pane.setPreferredSize(new Dimension(550, 140));
        pane.setMaximumSize(new Dimension(550, 140));
        pane.setViewportView(table);
        this.add(pane, BorderLayout.SOUTH);
    }

    public void setRotation(double angle) {
        if (balanceSlider != null) {
            balanceSlider.setRotationAngle(angle);
        }
        if (rotationVariable != null) {
            rotationVariable.setValue(angle);
            simquestViewer.getDataServer().updateClients();
        }
    }

    public void setEloURI(String eloURI) {
        logger.setEloURI(eloURI);
    }

    public void addCurrentDatapoint() {
        if (selectedVariables.size() == 0) {
            JOptionPane.showMessageDialog(this, getBundleString("DATACOLLECTOR_SELECT_VARIABLES_WARNING"), getBundleString("DATACOLLECTOR_SELECT_VARIABLES_WARNING_TITLE"), JOptionPane.INFORMATION_MESSAGE);

        }
        ModelVariable var;
        List<String> values = new LinkedList<String>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars.hasNext();) {
            var = vars.next();
            values.add(var.getValueString());
        }
        DataSetRow newRow = new DataSetRow(values);
        dataset.addRow(newRow);
        tableModel.addRow(newRow);
        pane.setViewportView(table);
        logger.logAddRow(newRow);
        if (sandbox != null) {
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
        } else if (evt.getActionCommand().equals("notification")) {
            notify = false;
            int highlight = -1;
            if (notificationMessage.equals(SCAFFOLD.VOTAT.name())) {
                highlight = 0;
            } else if (notificationMessage.equals(SCAFFOLD.INC_CHANGE.name())) {
                highlight = 1;
            } else if (notificationMessage.equals(SCAFFOLD.EXTREME_VALUES.name())) {
                highlight = 2;
            } else if (notificationMessage.equals(SCAFFOLD.CONFIRM_HYPO.name())) {
                highlight = 3;
            } else if (notificationMessage.equals(SCAFFOLD.IDENT_HYPO.name())) {
                highlight = 4;

            } else if (notificationMessage.equals(SCAFFOLD.SHOWBUTTON.name())) {
                highlight = -1;
            } else {
                Map<String, String> message = new LinkedHashMap<String, String>();
                if (notificationSender != null) {

                    message.put("Notification from " + notificationSender, notificationMessage);
                } else {

                    message.put("Notification", notificationMessage);
                }
                highlight = 0;
                NotificationGUI notiGUI = new NotificationGUI(simquestViewer.getMainFrame(), "Notification", message, highlight);
                notiGUI.prompt();
                return;
            }

            NotificationGUI notiGUI = new NotificationGUI(simquestViewer.getMainFrame(), "Notification", getNotificationTexts(), highlight);
            notiGUI.prompt();
            if (!notiGUI.showMessageAgain()) {
                shownMessages.add(notificationMessage);
            } else {
                if (shownMessages.contains(notificationMessage)) {
                    shownMessages.remove(notificationMessage);
                }
            }
            // JOptionPane.showMessageDialog(this, notificationMessage);
            // NotificationDialog d = new NotificationDialog(notificationMessage, "Notification", simquestViewer.getMainFrame(), -1);
            // d.show();
        }
    }

    private Map<String, String> getNotificationTexts() {
        String votat = "If a variable is not relevant for the hypothesis under, \ntest then hold that variable constant, or vary one thing at a time (VOTAT), or If not varying a variable, then pick the same value as used in the previous experiment";
        String equalIncrement = "If choosing a third value for a variable, \nthen choose an equal increment as between first and second values. Or if manipulating a variable, then choose simple, canonical manipulations ";
        String extemevalues = "Try some extreme values to see if there are limits on the proposed relationship";
        String confirmHypothesis = "Generate several additional cases in an attempt \nto either confirm or disconfirm the hypothesized relation";
        String identifyHypothesis = "Generate a small amount of data and examine for a candidate rule or relation";
        Map<String, String> texts = new LinkedHashMap<String, String>();
        texts.put("Vary only one thing at a time.", votat);
        texts.put("Use equal increments when varying variables.", equalIncrement);
        texts.put("Try some extreme values.", extemevalues);
        texts.put("Try to confirm your hypothesis.", confirmHypothesis);
        texts.put("Try to indentify a hypothesis.", identifyHypothesis);
        return texts;

    }

    /*
     * public void newELO() { dataset.removeAll(); if (sandbox != null) { initSandbox(); } //text.setText(""); }
     */
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
        String type;
        List<DataSetColumn> datasetvariables = new LinkedList<DataSetColumn>();
        List<DataSetHeader> datasetheaders = new LinkedList<DataSetHeader>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars.hasNext();) {
            var = vars.next();
            switch (var.getType()) {
                case ModelVariable.VT_BOOLEAN:
                    type = "boolean";
                    break;
                case ModelVariable.VT_INTEGER:
                    type = "integer";
                    break;
                case ModelVariable.VT_STRING:
                    type = "string";
                    break;
                case ModelVariable.VT_REAL:
                    type = "double";
                    break;
                default:
                    type = "double";
            }
            datasetvariables.add(new DataSetColumn(var.getExternalName(), var.getDescription(), type));
        }
        datasetheaders.add(new DataSetHeader(datasetvariables, Locale.ENGLISH));
        dataset = new DataSet(datasetheaders);
        tableModel = new DatasetTableModel(selectedVariables);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        if (pane != null) {
            pane.setViewportView(table);
        }
        if (sandbox != null) {
            sandbox.clear();
            sandbox.sendHeaderMessage();
        }
    }

    public void setSelectedVariables(List<ModelVariable> selection) {
        selectedVariables = selection;
        cleanDataSet();
        logger.logListOfVariables("variables_selected", selectedVariables);
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

    public void join(String mucID) {
        sandbox = new DatasetSandbox(this, mucID, tbi);
    }

    public void leave() {
        sandbox.disconnect();
        sandbox = null;
    }

    public String getSessionID() {
        if (sandbox == null) {
            return null;
        } else {
            return sandbox.getSessionID();
        }
    }

    public ScySimLogger getLogger() {
        return logger;
    }

    private void setFontStyle(JLabel label, int size, int style) {
        label.setFont(new Font("Serif", style, size));
    }

    public void processNotification(INotification notification) {
        String message = notification.getFirstProperty("message");
        String type = notification.getFirstProperty("type");
        String popup = notification.getFirstProperty("popup");
        if (message != null) {
            if (popup != null && popup.equals("true")) {
                final JDialog jd = new JDialog();
                jd.setSize(850, 600);
                jd.setLocationRelativeTo(this);
                jd.setTitle("Notification from: " + notification.getSender());
                jd.setLayout(new BorderLayout());
                JPanel northPanel = new JPanel(new BorderLayout());

                JLabel notiLabel = new JLabel("Notification from: " + notification.getSender());
                setFontStyle(notiLabel, 24, Font.BOLD);
                notiLabel.setHorizontalAlignment(SwingConstants.CENTER);

                northPanel.add(notiLabel, BorderLayout.NORTH);
                JPanel centerNorth = new JPanel(new GridLayout(3, 1));
                JLabel sessionLabel = new JLabel("Session : " + notification.getSession());
                setFontStyle(sessionLabel, 15, Font.PLAIN);
                sessionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel missionLabel = new JLabel("Mission : " + notification.getMission());
                setFontStyle(missionLabel, 15, Font.PLAIN);
                missionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel timeLabel = new JLabel("Time : " + notification.getTimestamp());
                setFontStyle(timeLabel, 15, Font.PLAIN);
                timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                centerNorth.add(sessionLabel);
                centerNorth.add(missionLabel);
                centerNorth.add(timeLabel);
                northPanel.add(centerNorth, BorderLayout.CENTER);
                jd.add(northPanel, BorderLayout.NORTH);

                JPanel centerPanel;
                JTextArea jta = new JTextArea(notification.getFirstProperty("message"));
                JLabel textLabel = new JLabel();
                textLabel.setBorder(BorderFactory.createTitledBorder("Message"));
                // JTextPane jta = new JTextPane();
                // jta.setText(notification.getProperty("message"));
                jta.setEditable(false);
                // jta.setLogicalStyle(centerStyle);
                JLabel imageLabel = new JLabel();
                ImageIcon ii;
                String image = notification.getFirstProperty("image");
                byte[] decode = Base64.decode(image);
                if (image != null) {
                    centerPanel = new JPanel(new GridLayout(1, 2));
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decode));
                        ii = new ImageIcon(bufferedImage);
                        imageLabel.setIcon(ii);
                        imageLabel.setBorder(BorderFactory.createTitledBorder("Image"));
                        centerPanel.add(imageLabel);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    centerPanel = new JPanel(new GridLayout(1, 1));

                }
                textLabel.add(jta, BorderLayout.CENTER);
                centerPanel.add(jta);
                jd.add(centerPanel, BorderLayout.CENTER);
                JButton ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jd.dispose();
                    }
                });
                jd.add(ok, BorderLayout.SOUTH);
                jd.pack();
                jd.setVisible(true);

            } else {
                System.out.println("message without popup received...");
                notificationSender = notification.getSender();
                notificationMessage = message;
                if (SwingUtilities.isEventDispatchThread()) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            startNotifyThread();

                        }
                    }).start();
                } else {
                    startNotifyThread();
                }
            }
        } else if (type != null && notification.getFirstProperty("level") != null && !shownMessages.contains(notification.getFirstProperty("level"))) {
            if (type.equals("scaffold")) {
                if (notification.getFirstProperty("level").equals(SCAFFOLD.SHOWBUTTON.name())) {
                    notificationMessage = SCAFFOLD.SHOWBUTTON.name();
                    notifyButton.setVisible(true);
                } else {

                    notificationMessage = notification.getFirstProperty("level");
                    if (SwingUtilities.isEventDispatchThread()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                startNotifyThread();

                            }
                        }).start();
                    } else {
                        startNotifyThread();
                    }

                }
            }
        }
    }

    private void startNotifyThread() {

        if (!notThreadRunning && !notify) {
            notThreadRunning = true;
            notify = true;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    notifyButton.setVisible(true);
                }
            });

            boolean up = true;
            Color upColor = Color.RED;
            Color downColor = notifyButton.getBackground();
            Color currentColor = downColor;
            float upFontSize = 22;
            float downFontSize = notifyButton.getFont().getSize();
            float currentFontSize = downFontSize;
            double updateMillis = 2;
            double cycleLengthInMillis = 1000;
            int count = 0;
            double step = cycleLengthInMillis / updateMillis;
            while (notify) {
                if (count > step) {
                    up = !up;
                    count = 0;
                    if (up) {
                        currentColor = downColor;
                    } else {
                        currentColor = upColor;
                    }
                }
                int red = currentColor.getRed();
                int green = currentColor.getGreen();
                int blue = currentColor.getBlue();
                if (up) {
                    red += (upColor.getRed() - downColor.getRed()) / step;
                    green += (upColor.getGreen() - downColor.getGreen()) / step;
                    blue += (upColor.getBlue() - downColor.getBlue()) / step;
                    currentFontSize += (upFontSize - downFontSize) / step;
                } else {
                    red += (downColor.getRed() - upColor.getRed()) / step;
                    green += (downColor.getGreen() - upColor.getGreen()) / step;
                    blue += (downColor.getBlue() - upColor.getBlue()) / step;
                    currentFontSize += (downFontSize - upFontSize) / step;
                }
                red = Math.min(red, 230);
                green = Math.min(green, 230);
                blue = Math.min(blue, 230);
                red = Math.max(red, 30);
                green = Math.max(green, 30);
                blue = Math.max(blue, 30);
                currentFontSize = Math.min(upFontSize, currentFontSize);
                currentFontSize = Math.max(downFontSize, currentFontSize);
                currentColor = new Color(red, green, blue);
                setButtonStyle(currentColor, currentFontSize);
                count++;
                try {
                    Thread.sleep((long) updateMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setButtonStyle(downColor, downFontSize);
            notThreadRunning = false;
        }
    }

    private void setButtonStyle(final Color currentColor, final float currentFontSize) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                notifyButton.setForeground(currentColor);
                notifyButton.setFont(notifyButton.getFont().deriveFont(currentFontSize));
            }
        });
    }

    private String getBundleString(String key) {
        return this.bundle.getString(key);
    }
}
