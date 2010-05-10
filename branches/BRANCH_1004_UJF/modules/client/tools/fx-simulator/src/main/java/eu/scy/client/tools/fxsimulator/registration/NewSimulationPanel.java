package eu.scy.client.tools.fxsimulator.registration;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NewSimulationPanel extends JPanel implements ActionListener {

    private String simulationURI = null;
    private JTextField free;
    protected JButton load;

    public NewSimulationPanel(ActionListener listener) {
        this.setLayout(new GridLayout(7,1));
        JRadioButton radio;
        JPanel panel;
        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        ButtonGroup radios = new ButtonGroup();

        radio = new JRadioButton();
        radio.setName("http://www.scy-lab.eu/sqzx/balance.sqzx");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("Balance / Seesaw simulation"));
        this.add(panel);

        radio = new JRadioButton();
        //radio.setName("http://www.scy-lab.eu/sqzx/co2_converter_210110_mod1.sqzx");
        radio.setName("http://www.scy-lab.eu/sqzx/co2_converter.sqzx");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("CO2-Converter"));
        this.add(panel);

        radio = new JRadioButton();
        radio.setName("http://www.scy-lab.eu/sqzx/co2_house.sqzx");
        //radio.setName("http://www.scy-lab.eu/sqzx/house_05022010.sqzx");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("new CO2-House simulation (with variable descr.)"));
        this.add(panel);

        radio = new JRadioButton();
        radio.setName("http://www.scy-lab.eu/sqzx/HouseNew.sqzx");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("old CO2-House simulation (slow loading)"));
        this.add(panel);

        radio = new JRadioButton();
        radio.setName("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
        //radio.setName("http://www.scy-lab.eu/sqzx/house_05022010.sqzx");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("pendulum"));
        this.add(panel);

        radio = new JRadioButton();
        radio.setName("free");
        radio.setActionCommand("setsimulation");
        radio.addActionListener(this);
        radios.add(radio);
        panel = new JPanel();
        panel.setLayout(flow);
        panel.add(radio);
        panel.add(new JLabel("or a new URI: "));
        free = new JTextField(40);
        panel.add(free);
        this.add(panel);

        load = new JButton("load simulation");
        load.setActionCommand("loadsimulation");
        load.addActionListener(listener);
        this.add(load);

        this.setVisible(true);
    }

    public String getSimulationURI() {
        return simulationURI;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() instanceof JRadioButton) {
            this.simulationURI = ((JRadioButton)ae.getSource()).getName();
            if (this.simulationURI.equals("free")) {
                this.simulationURI = free.getText();
            }
        }
    }

}
