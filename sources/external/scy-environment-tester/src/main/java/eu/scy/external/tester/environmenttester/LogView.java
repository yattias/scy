package eu.scy.external.tester.environmenttester;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class LogView extends JDialog {
	String log;
	JButton closeBT = new JButton("Close");
	JButton saveBT = new JButton("Save to File");
	
	public LogView(String log, JFrame parent) {
		super(parent, true);
		this.log = log;
		build();
	}
	
	public void build() {
		this.setLocationByPlatform(true);
		this.setTitle("SCY Network Test - Log Viewer");
		Container con = this.getContentPane();
		con.setBackground(Color.WHITE);
		con.setLayout(new BorderLayout());
		TextArea logArea = new TextArea(log);
		con.add(logArea, BorderLayout.CENTER);
		Container bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		saveBT.addActionListener(new SaveBTListener());
		bottom.add(saveBT);
		closeBT.addActionListener(new CloseBTListener());
		bottom.add(closeBT);
		bottom.setBackground(Color.WHITE);
		con.add(bottom, BorderLayout.SOUTH);
		pack();
		this.setSize(getWidth(), 600);
		setVisible(true);
	}
	
	public void closeMe() {
		this.dispose();
	}
	
	private class CloseBTListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closeMe();
		}
	}
	
	private class SaveBTListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser("SCY_network_log.txt");
			fc.setFileFilter(new FileFilter () {

				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
				}

				@Override
				public String getDescription() {
					return "text file (*.txt)";
				}
			
			});
			int result = fc.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				String path = fc.getSelectedFile().getPath();
				if (!path.toLowerCase().endsWith(".txt")) {
					path = path + ".txt";
				}
				File file = new File(path);
				Writer fw = null;
				try {
					fw = new FileWriter(path);
					fw.write(log);
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: Could not save to "+path, "SCY Environment Test", JOptionPane.ERROR_MESSAGE);

				}
				finally {
					if(fw != null)  {
						try { fw.close(); } catch(Exception ex) {}
						JOptionPane.showMessageDialog(null, "Saved to "+path, "SCY Environment Test", JOptionPane.INFORMATION_MESSAGE);

					}
				}
			}
			
		}
		
	}

}
