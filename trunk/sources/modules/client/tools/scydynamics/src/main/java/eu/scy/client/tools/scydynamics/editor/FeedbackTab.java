package eu.scy.client.tools.scydynamics.editor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.domain.FeedbackPanel;
import eu.scy.client.tools.scydynamics.ui.VerticalLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colab.um.draw.JdFigure;
import colab.um.draw.JdObject;

@SuppressWarnings("serial")
public class FeedbackTab extends JPanel implements ChangeListener, ActionListener {

    private JSplitPane splitPane;
	private ModelEditor editor;
	private FeedbackPanel feedbackPanel;
	private JPanel variablesPanel;
	private final static Logger debugLogger = Logger.getLogger(FeedbackTab.class.getName());

    
    public FeedbackTab(ModelEditor editor, ResourceBundleWrapper bundle) {
        super();
        this.editor = editor;
        initUI();    
    }

    private void initUI() {
    	this.setLayout(new BorderLayout());
    	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    	JPanel leftPanel = new JPanel();
    	leftPanel.setLayout(new VerticalLayout(5, VerticalLayout.BOTH));
    	FlowLayout leftFlow = new FlowLayout();
    	leftFlow.setAlignment(FlowLayout.LEFT);
    	//leftPanel.setLayout(new BorderLayout());
    	JButton runButton = new JButton(Util.getImageIcon("media-playback-start.png"));
		runButton.setSize(36, 36);
		runButton.setPreferredSize(new Dimension(36, 36));
		runButton.setActionCommand("feedback");
		runButton.setToolTipText("get feedback");
		runButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(leftFlow);
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Model run"));
		buttonPanel.add(runButton);
		leftPanel.add(new JLabel(""));
		leftPanel.add(buttonPanel);
		
		leftPanel.add(getVariablesPanel());
		splitPane.setLeftComponent(leftPanel);
		try {
			feedbackPanel = new FeedbackPanel(editor);
			splitPane.setRightComponent(feedbackPanel);
		} catch (Exception e) {
			splitPane.setRightComponent(new JPanel());
		}
		splitPane.setDividerLocation((int)variablesPanel.getPreferredSize().getWidth());
    	this.add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel getVariablesPanel() {
    	variablesPanel = new JPanel();
    	variablesPanel.setBorder(BorderFactory.createTitledBorder("Variables"));
    	this.updateVariables();    	
    	return variablesPanel;
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e) {
    	this.updateVariables();
        editor.getActionLogger().logActivateWindow("feedback", null, this);
    }

	private void updateVariables() {
		variablesPanel.removeAll();		
		try {
			variablesPanel.setLayout(new GridLayout(editor.getModel().getNodes().size(), 1));
			FlowLayout leftFlow = new FlowLayout();
			leftFlow.setAlignment(FlowLayout.LEFT);
			
			Enumeration<JdObject> objects = editor.getModel().getNodes().elements();
			JdObject object;
			JLabel colorLabel;
			JPanel vPanel;
			
			while (objects.hasMoreElements()) {
				object = objects.nextElement();			
				if (object.getType() == JdFigure.STOCK) {
					vPanel = new JPanel(leftFlow);			
					colorLabel = new JLabel("\u25A0");
					colorLabel.setForeground(object.getLabelColor());
					vPanel.add(colorLabel);
					vPanel.add(new JLabel(object.getLabel()));
					variablesPanel.add(vPanel);
					//variables.put(object.getLabel(), box);
				} else if (object.getType() == JdFigure.AUX) {
					vPanel = new JPanel(leftFlow);				
					colorLabel = new JLabel("\u25CF");
					colorLabel.setForeground(object.getLabelColor());
					vPanel.add(colorLabel);
					vPanel.add(new JLabel(object.getLabel()));
					variablesPanel.add(vPanel);
				}  else if (object.getType() == JdFigure.CONSTANT) {
					vPanel = new JPanel(leftFlow);				
					colorLabel = new JLabel("\u25C6");
					colorLabel.setForeground(object.getLabelColor());
					vPanel.add(colorLabel);
					vPanel.add(new JLabel(object.getLabel()));
					variablesPanel.add(vPanel);
				}
			}
		} catch (Exception e) {
			debugLogger.warning(e.getMessage());
		}
		if (variablesPanel.getComponentCount()==0) {
			variablesPanel.add(new JLabel("<html>There are no variables<br>in your model.</html>"));
		}
		splitPane.setDividerLocation((int)variablesPanel.getPreferredSize().getWidth());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("feedback")) {
			feedbackPanel.update();
			splitPane.setDividerLocation((int)variablesPanel.getPreferredSize().getWidth());
		}
	}
	
	

}