package eu.scy.client.tools.scydynamics.editor;

import colab.um.tools.JTools;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabPanel extends JPanel implements ActionListener, WindowListener {
	
	private JPanel panel;
	private String title;
	private int index;
	private Properties properties;
	private ModelEditor modelEditor;

	private class TabButton extends JButton {

		public TabButton() {
			super(new ImageIcon(JTools.getSysResourceImage("SortAsc")));
			setUI(new BasicButtonUI());
			int size = 17;
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			setPreferredSize(new Dimension(size, size));
			setToolTipText("pop out");
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(TabPanel.buttonMouseListener);
			setRolloverEnabled(true);
		}
	}

	public TabPanel(String title, JPanel panel, ModelEditor modelEditor,
			Properties properties) {
		this.title = title;
		this.panel = panel;
		this.modelEditor = modelEditor;
		this.properties = properties;
		setOpaque(false);
		setLayout(new FlowLayout(0, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		JLabel label;
		if (title.equals("graph"))
			label = new JLabel(new ImageIcon(
					JTools.getSysResourceImage("JvtGraph")));
		else
			label = new JLabel(new ImageIcon(
					JTools.getSysResourceImage("JvtTable")));
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
		add(label);
		label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 10));
		add(label);
		if (properties.getProperty("show.popouttabs", "false").equals("true")) {
			JButton butt = new TabButton();
			butt.addActionListener(this);
			add(butt);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		modelEditor.tabbedPane.remove(panel);
		modelEditor.tabbedPane.setSelectedIndex(0);
		JFrame frame = new JFrame(title);
		frame.getContentPane().add(panel);
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setSize(new Dimension(600, 400));
		frame.addWindowListener(this);
		((ChangeListener)panel).stateChanged(new ChangeEvent(this));
		frame.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent windowevent) {
	}

	@Override
	public void windowClosed(WindowEvent windowevent) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (title.equals("graph"))
			modelEditor.addGraph();
		else
			modelEditor.addTable();
	}

	@Override
	public void windowDeactivated(WindowEvent windowevent) {
	}

	@Override
	public void windowDeiconified(WindowEvent windowevent) {
	}

	@Override
	public void windowIconified(WindowEvent windowevent) {
	}

	@Override
	public void windowOpened(WindowEvent windowevent) {
	}

	private static final MouseListener buttonMouseListener = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}

	};

}
