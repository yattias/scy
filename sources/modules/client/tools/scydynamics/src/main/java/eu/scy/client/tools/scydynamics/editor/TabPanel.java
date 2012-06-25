package eu.scy.client.tools.scydynamics.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TabPanel extends JPanel implements ActionListener, WindowListener {

	private JPanel panel;
	private String title;
	//private int index;
	//private Properties properties;
	private ModelEditor modelEditor;
	//private Icon icon;
	private JFrame frame;

	public TabPanel(String title, Icon icon, JPanel panel, ModelEditor modelEditor, Properties properties) {
		super();
		this.title = title;
		this.panel = panel;
		this.modelEditor = modelEditor;
		//this.properties = properties;
		//this.icon = icon;
		setOpaque(false);
		setLayout(new FlowLayout(0, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JLabel label = new JLabel(icon);
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
		add(label);
		label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
		add(label);
		if (!title.equalsIgnoreCase("model") && properties.getProperty("show.popouttabs", "false").equals("true")) {
			JButton butt = new TabButton();
			butt.addActionListener(this);
			butt.addMouseListener(buttonMouseListener);
			add(butt);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (title.equalsIgnoreCase("graph")) {
			modelEditor.tabbedPane.setSelectedIndex(modelEditor.TABINDEX_EDITOR);
			modelEditor.disableTab(modelEditor.TABINDEX_GRAPH);
	    	modelEditor.tabbedPane.setComponentAt(modelEditor.TABINDEX_GRAPH, new JLabel("dummy"));					
		} else if (title.equalsIgnoreCase("table")) {
			modelEditor.tabbedPane.setSelectedIndex(modelEditor.TABINDEX_EDITOR);
			modelEditor.disableTab(modelEditor.TABINDEX_TABLE);
			modelEditor.tabbedPane.setComponentAt(modelEditor.TABINDEX_TABLE, new JLabel("dummy"));
		} else {
			return;
		}
		
    	frame = new JFrame(title);
		frame.setLayout(new BorderLayout());		
    	frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setEnabled(true);
    	panel.setVisible(true);
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setSize(new Dimension(600, 400));
		frame.addWindowListener(this);
		((ChangeListener) panel).stateChanged(new ChangeEvent(this));
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
		if (title.equalsIgnoreCase("graph")) {
			modelEditor.tabbedPane.setComponentAt(modelEditor.TABINDEX_GRAPH, panel);
			modelEditor.enableTab(modelEditor.TABINDEX_GRAPH);
		} else if (title.equalsIgnoreCase("table")){
			modelEditor.tabbedPane.setComponentAt(modelEditor.TABINDEX_TABLE, panel);
			modelEditor.enableTab(modelEditor.TABINDEX_TABLE);
		}
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
