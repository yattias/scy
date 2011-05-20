package eu.scy.external.tester.environmenttester;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eu.scy.external.tester.environmenttester.test.ITest;

@SuppressWarnings("serial")
public class View extends JFrame {
	private JButton startTestBT;
	private JButton viewLogBT;
	private JPanel center;
	private JLabel scyLogo;
	private Vector<JLabel> results;
	public View() {
		this.build();
		this.loadDefaultCenter();
	}
	
	private void build() {
		//builds the gui
		this.setLocationByPlatform(true);
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//		this.setResizable(false);
		startTestBT = new JButton("Start");
		viewLogBT = new JButton("View Log");
		viewLogBT.setEnabled(false);
		center = new JPanel();
		center.setBackground(null);
		center.setPreferredSize(new Dimension(300, 260));
		scyLogo = new JLabel();
		scyLogo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/scy.png"))));
		//add items
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.add(scyLogo);
		con.add(scyLogo, BorderLayout.NORTH);
		con.add(center, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		bottom.add(viewLogBT);
		bottom.add(startTestBT);
		bottom.setBackground(Color.WHITE);
		con.add(bottom, BorderLayout.SOUTH);
		con.setBackground(Color.WHITE);
		this.pack();
		this.setVisible(true);
	}
	
	private void loadDefaultCenter() {
		JLabel label = new JLabel("Click 'Start' to run the test!");
		label.setFont(new Font("Arial", Font.BOLD, 15));
		center.add(label);
	}
	
	public void addStartBTListener(ActionListener listener) {
		startTestBT.addActionListener(listener);
	}
	
	public void addLogActionListener(ActionListener l) {
		viewLogBT.addActionListener(l);
	}
	
	public void startTestMode(Vector<ITest> tests) {
		viewLogBT.setEnabled(false);
		center.removeAll();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		center.setLayout(gbl);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		results = new Vector<JLabel>();
		int i = 0;
		for(ITest test:tests) {
			JLabel name = new JLabel(test.getName());
			name.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.insets = new Insets(10, 15, 5 ,0);
			gbl.setConstraints(name, gbc);
			center.add(name);
			
			gbc.gridx = 1;
			gbc.gridy = i;
			gbc.insets = new Insets(0, 20, 0, 0);
			JLabel result = new JLabel();
			gbl.setConstraints(result, gbc);
			center.add(result);
			results.add(result);
			
			i++;
		}
		this.repaint();
		startTestBT.setEnabled(false);
		startTestBT.setText("Running Tests..");
	}

	public void changeImage(int i, String fileName) {
		results.get(i).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/"+fileName))));
	}
	
	public void prepareTestMode() {
		startTestBT.setText("Running Tests..");
		startTestBT.setEnabled(true);
	}
	
	public void disableTestBT() {
		startTestBT.setEnabled(false);
	}

	public void showOkPopup() {
		JOptionPane.showMessageDialog(this, "All tests are successfull!\nThis system is ready to run the SCY-Lab!", "SCY Environment Test", JOptionPane.INFORMATION_MESSAGE);
		startTestBT.setEnabled(true);
		startTestBT.setText("Restart");
		viewLogBT.setEnabled(true);
	}

	public void showErrorPopup(int errors, int warnings) {
	        StringBuilder sb = new StringBuilder("Warning: ");
	        int errorMessage = JOptionPane.WARNING_MESSAGE;
	        if (errors > 0) {
	            errorMessage = JOptionPane.ERROR_MESSAGE;
	            sb.append(errors + " error(s)");
	        }
	        if (errors > 0 && warnings > 0) {
	            sb.append(" and ");
	        }
	        if (warnings > 0) {
	            sb.append(warnings + " warning(s)"); 
	        }
	        sb.append(" occured during the test.\nPlease send the log to your SCY-Partner for further steps.");
		JOptionPane.showMessageDialog(this, sb.toString(), "SCY Environment Test", errorMessage);
		startTestBT.setEnabled(true);
		startTestBT.setText("Restart");
		viewLogBT.setEnabled(true);
	}


}
