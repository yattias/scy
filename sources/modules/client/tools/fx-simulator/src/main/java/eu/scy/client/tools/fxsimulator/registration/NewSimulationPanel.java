package eu.scy.client.tools.fxsimulator.registration;

import eu.scy.client.tools.scysimulator.SimConfig.MODE;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NewSimulationPanel extends JPanel implements ActionListener {

    private final static Logger debugLogger = Logger.getLogger(NewSimulationPanel.class.getName());
    private String simulationURI = "http://www.scy-lab.eu/sqzx/balance.sqzx";
    private JTextField free;
    private static FlowLayout flow;
    protected JButton load;
    private final ActionListener externalListener;
    private MODE mode = MODE.collect_data;

    public NewSimulationPanel(ActionListener listener) {
	flow = new FlowLayout();
	flow.setAlignment(FlowLayout.LEFT);
        externalListener = listener;

	this.setLayout(new BorderLayout());
	this.add(createModesPanel(), BorderLayout.EAST);
	this.add(createSimulationsPanel(), BorderLayout.WEST);

        this.setVisible(true);
    }

    private JPanel createSimulationsPanel() {
	JPanel allSimulationsPanel = new JPanel();
	allSimulationsPanel.setBorder(BorderFactory.createTitledBorder("Simulation"));
	allSimulationsPanel.setLayout(new GridLayout(6,1));
	ButtonGroup radios = new ButtonGroup();

        JRadioButton simulationRadio;
        JPanel simulationsPanel = new JPanel();
	simulationRadio = new JRadioButton();
        simulationRadio.setName("http://www.scy-lab.eu/sqzx/balance.sqzx");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
	simulationRadio.setSelected(true);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("Balance / Seesaw simulation"));
        allSimulationsPanel.add(simulationsPanel);

        simulationRadio = new JRadioButton();
        simulationRadio.setName("http://www.scy-lab.eu/sqzx/co2_converter.sqzx");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("CO2-Converter"));
        allSimulationsPanel.add(simulationsPanel);

        simulationRadio = new JRadioButton();
        simulationRadio.setName("http://www.scy-lab.eu/sqzx/co2_house.sqzx");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("new CO2-House simulation (with variable descr.)"));
        allSimulationsPanel.add(simulationsPanel);

        simulationRadio = new JRadioButton();
        simulationRadio.setName("http://www.scy-lab.eu/sqzx/pizza.sqzx");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("pizza simulation"));
        allSimulationsPanel.add(simulationsPanel);

        simulationRadio = new JRadioButton();
        simulationRadio.setName("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("pendulum"));
        allSimulationsPanel.add(simulationsPanel);

        simulationRadio = new JRadioButton();
        simulationRadio.setName("free");
        simulationRadio.setActionCommand("setsimulation");
        simulationRadio.addActionListener(this);
        radios.add(simulationRadio);
        simulationsPanel = new JPanel();
        simulationsPanel.setLayout(flow);
        simulationsPanel.add(simulationRadio);
        simulationsPanel.add(new JLabel("or a new URI: "));
        free = new JTextField(40);
        simulationsPanel.add(free);
	allSimulationsPanel.add(simulationsPanel);

	return allSimulationsPanel;
    }

    private JPanel createModesPanel() {
	ButtonGroup modeGroup = new ButtonGroup();
	JPanel allModesPanel = new JPanel();
	allModesPanel.setBorder(BorderFactory.createTitledBorder("Mode"));
	allModesPanel.setLayout(new GridLayout(5,1));

	JPanel modePanel = new JPanel();
	
	modePanel.setLayout(flow);
	JRadioButton modeRadio = new JRadioButton();
	modeRadio.setName(MODE.explore_only.toString());
	modeRadio.setActionCommand("mode");
	modeRadio.addActionListener(this);
	modeGroup.add(modeRadio);
	modePanel.add(modeRadio);
	modePanel.add(new JLabel("<html>explore only<br>- cannot store datasets</html>"));
	allModesPanel.add(modePanel);
	
	modePanel = new JPanel();
	modePanel.setLayout(flow);
	modeRadio = new JRadioButton();
	modeRadio.setName(MODE.explore_simple_data.toString());
	modeRadio.setActionCommand("mode");
	modeRadio.addActionListener(this);
	modeGroup.add(modeRadio);
	modePanel.add(modeRadio);
	modePanel.add(new JLabel("<html>explore simple data<br>- can only store one-row datasets</html>"));
	allModesPanel.add(modePanel);
	
	modePanel = new JPanel();
	modePanel.setLayout(flow);
	modeRadio = new JRadioButton();
	modeRadio.setName(MODE.collect_simple_data.toString());
	modeRadio.setActionCommand("mode");
	modeRadio.addActionListener(this);
	modeGroup.add(modeRadio);
	modePanel.add(modeRadio);
	modePanel.add(new JLabel("<html>collect simple data<br>- cannot change relevant variables</html>"));
	allModesPanel.add(modePanel);
	
	modePanel = new JPanel();
	modePanel.setLayout(flow);
	modeRadio = new JRadioButton();
	modeRadio.setName(MODE.collect_data.toString());
	modeRadio.setActionCommand("mode");
	modeRadio.addActionListener(this);
	modeRadio.setSelected(true);
	modeGroup.add(modeRadio);
	modePanel.add(modeRadio);
	modePanel.add(new JLabel("<html>collect data<br>- all features available</html>"));
	allModesPanel.add(modePanel);

	load = new JButton("load simulation");
        load.setActionCommand("loadsimulation");
        load.addActionListener(externalListener);
	allModesPanel.add(load);

	return allModesPanel;
    }

    public String getSimulationURI() {
        return simulationURI;
    }

    public MODE getMode() {
	return mode;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	debugLogger.severe(ae.getActionCommand());
        if (ae.getActionCommand().equals("setsimulation")) {
            this.simulationURI = ((JRadioButton)ae.getSource()).getName();
            if (this.simulationURI.equals("free")) {
                this.simulationURI = free.getText();
            }
        } else if (ae.getActionCommand().equals("mode")) {
	    this.mode = MODE.valueOf(((JRadioButton)ae.getSource()).getName());
	}
    }

    public static void main(String[] args) {
	JFrame frame = new JFrame();
	frame.getContentPane().add(new NewSimulationPanel(null));
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setVisible(true);
    }

}
