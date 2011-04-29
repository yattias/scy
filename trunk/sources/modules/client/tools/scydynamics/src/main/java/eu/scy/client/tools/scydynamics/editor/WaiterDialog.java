package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class WaiterDialog extends JDialog implements ActionListener, MouseListener {
	private JFrame wind;
	private JPanel glass;
	private JProgressBar progressBar;
	private Cursor oldCursor;
	private Thread simulationThread;

	public WaiterDialog (JPanel parentPanel, Thread simulationThread, String title, String text) {
        super(SwingUtilities.windowForComponent(parentPanel), title);
        this.simulationThread = simulationThread;
        wind = (JFrame)SwingUtilities.windowForComponent(parentPanel);
        glass = new JPanel();
        glass.setOpaque(false);
        glass.addMouseListener(this);
        wind.setGlassPane(glass);
        wind.getGlassPane().setVisible(true);
        setModal(false);
        setAlwaysOnTop(true);
        
        oldCursor = wind.getCursor();
        wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new JLabel(text, SwingConstants.CENTER));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        contentPanel.add(progressBar, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(new JLabel("It takes too long?"));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);
        
        getContentPane().add(contentPanel);
        setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);    
        setLocation(parentPanel.getLocationOnScreen().x+parentPanel.getWidth()/2, parentPanel.getLocationOnScreen().y+parentPanel.getHeight()/2);
        pack();
        setVisible(true);
    }
	
	public void setProgress(int progress) {
		progressBar.setValue(progress);
	}
	
	public void stepProgress() {
		progressBar.setValue(progressBar.getValue()+1);
	}

	public void breakGlass() {
		wind.getGlassPane().setVisible(false);
		wind.setCursor(oldCursor);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {e.consume();}
	@Override
	public void mousePressed(MouseEvent e) {e.consume();}
	@Override
	public void mouseReleased(MouseEvent e) {e.consume();}
	@Override
	public void mouseEntered(MouseEvent e) {e.consume();}
	@Override
	public void mouseExited(MouseEvent e) {e.consume();}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			simulationThread.stop();
			this.breakGlass();
			this.dispose();
		}
	}

}
