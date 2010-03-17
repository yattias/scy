package eu.scy.chartviewer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class NotificationSender implements ActionListener, Callback {

    private static final String DEFAULT_PROPOSING_USER = "adam@scy.collide.info/Smack";

    private static final String DEFAULT_TYPE = "collaboration_request";

    private static final String DEFAULT_SESSION = "TestSession";

    private static final String DEFAULT_MISSION = "TestMission";

    private static final String DEFAULT_TARGET_USER = "jan@scy.collide.info/Smack";

    private static final String DEFAULT_ELO_URL = "roolo://memory/126/0/Start_page.url";

    private static final String TS_HOST = "scy.collide.info";

    private static final int TS_PORT = 2525;

    private static final String COMMAND_SPACE = "command";

    private static final Tuple NOTIFICATION_TEMPLATE = new Tuple("notification", Field.createWildCardField());

    private static final String DEFAULT_TOOL_NAME = "scyhub";

    private static final String DEFAULT_SENDER_NAME = "Notification Test Tool (NTT)";

    private TupleSpace commandSpace;

    private DefaultTableModel tableModel;

    private JTextField toUserTF;

    private JTextField missionTF;

    private JTextField sessionTF;

    private JTextField typeTF;

    private JTextField proposingUserTF;

    private JTextField elouriTF;

    private JTable notificationTable;

    private Vector<String> rowData;

    private JTextField toolNameTF;

    private JTextField senderTF;

    public NotificationSender() {
        JFrame frame = initGui();
        try {
            commandSpace = new TupleSpace(new User("ChartViewer"), TS_HOST, TS_PORT, COMMAND_SPACE);
            commandSpace.eventRegister(Command.WRITE, NOTIFICATION_TEMPLATE, this, true);
        } catch (TupleSpaceException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage()+"\n"+e.getCause(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        rowData = new Vector<String>();

    }

    private JFrame initGui() {
        try {
            UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Notification Sender - just for debugging purposes");
        JButton sendButton = new JButton("Send test notification");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        JPanel inputPanel = new JPanel();

        String[] columnNames = { "Target User", "Proposed User", "UUID", "Mission", "Session", "Type", "ELO URI" };

        inputPanel.setBorder(BorderFactory.createTitledBorder("Please input parameters for notification"));
        inputPanel.setLayout(new GridLayout(0, 2));
        JLabel toUserLabel = new JLabel("To user (JID): ");
        toUserTF = new JTextField(DEFAULT_TARGET_USER);
        JLabel missionLabel = new JLabel("Mission: ");
        missionTF = new JTextField(DEFAULT_MISSION);
        JLabel sessionLabel = new JLabel("Session: ");
        sessionTF = new JTextField(DEFAULT_SESSION);
        JLabel typeLabel = new JLabel("Type: ");
        typeTF = new JTextField(DEFAULT_TYPE);
        JLabel proposingUserLabel = new JLabel("Proposing User: ");
        proposingUserTF = new JTextField(DEFAULT_PROPOSING_USER);
        JLabel elouriLabel = new JLabel("ELO URI: ");
        elouriTF = new JTextField(DEFAULT_ELO_URL);
        JLabel toolNameLabel = new JLabel("Target ToolName: ");
        toolNameTF = new JTextField(DEFAULT_TOOL_NAME);
        JLabel senderNameLabel = new JLabel("Sender Name: ");
        senderTF = new JTextField(DEFAULT_SENDER_NAME);
        inputPanel.add(toUserLabel);
        inputPanel.add(toUserTF);
        inputPanel.add(missionLabel);
        inputPanel.add(missionTF);
        inputPanel.add(sessionLabel);
        inputPanel.add(sessionTF);
        inputPanel.add(typeLabel);
        inputPanel.add(typeTF);
        inputPanel.add(proposingUserLabel);
        inputPanel.add(proposingUserTF);
        inputPanel.add(elouriLabel);
        inputPanel.add(elouriTF);
        inputPanel.add(toolNameLabel);
        inputPanel.add(toolNameTF);
        inputPanel.add(senderNameLabel);
        inputPanel.add(senderTF);
        mainPanel.add(inputPanel);
        sendButton.addActionListener(this);
        tableModel = new DefaultTableModel(columnNames, 0);
        notificationTable = new JTable(tableModel);
        notificationTable.setBackground(Color.GREEN);
        notificationTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notifications in SQLSpace: '" + COMMAND_SPACE + "' on server : '" + TS_HOST + ":" + TS_PORT+"'"));
        mainPanel.add(scrollPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(sendButton, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setSize(850,600);
        return frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // notification 583c10bfdbd326ba:-1fa94f2b:126e08b5632:-7d8b stefan@scy.collide.info scylab collaboration agent co2Mission mysession type=collaboration_request proposing_user=adam@scy.collide.info/Smack elo=roolo://memory/126/0/Start_page.url
        Tuple noti = new Tuple("notification", new VMID().toString(), toUserTF.getText().trim(),toolNameTF.getText().trim(), senderTF.getText().trim(), missionTF.getText().trim(), sessionTF.getText().trim(), "type=" + typeTF.getText().trim(), "proposing_user=" + proposingUserTF.getText().trim(), "elo=" + elouriTF.getText().trim());
        try {
            commandSpace.write(noti);
        } catch (TupleSpaceException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void call(Command arg0, int arg1, Tuple afterTuple, Tuple arg3) {
        rowData.add(afterTuple.getField(2).getValue().toString());
        rowData.add(afterTuple.getField(8).getValue().toString().substring(afterTuple.getField(8).getValue().toString().indexOf("=")+1));
        rowData.add(afterTuple.getField(1).getValue().toString());
        rowData.add(afterTuple.getField(5).getValue().toString());
        rowData.add(afterTuple.getField(6).getValue().toString());
        rowData.add(afterTuple.getField(7).getValue().toString().substring(afterTuple.getField(7).getValue().toString().indexOf("=")+1));
        rowData.add(afterTuple.getField(9).getValue().toString().substring(afterTuple.getField(9).getValue().toString().indexOf("=")+1));
        tableModel.addRow(rowData);
    }

    public static void main(String[] args) {
        new NotificationSender();
    }
}
