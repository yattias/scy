package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sqv.data.DataServer;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;

@SuppressWarnings("serial")
public abstract class SimulationPanel extends JPanel implements ActionListener, Runnable {

	protected SimulationSettingsPanel simulationPanel;
	protected ModelEditor editor;
	protected JSplitPane splitPane;
	protected VariableSelectionPanel variablePanel;
	protected ResourceBundleWrapper bundle;
	protected SimquestModelQuantitative sqModel;
	protected DataServer dataServer;
	protected Thread simulationThread;	

	public SimulationPanel(ModelEditor editor, ResourceBundleWrapper bundle) {
		this.editor = editor;
		this.bundle = bundle;
	}

	protected void initComponents(boolean withTimeVariable) {
		this.setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(simulationPanel = new SimulationSettingsPanel(editor, this, withTimeVariable), BorderLayout.NORTH);
		westPanel.add(variablePanel = new VariableSelectionPanel(editor, bundle, withTimeVariable), BorderLayout.CENTER);
		JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setViewportView(westPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(scroller);
		splitPane.setDividerLocation(330);
		this.add(splitPane, BorderLayout.CENTER);
	}
	
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand().equals("stop")) {
 			stop();
 		} else if (e.getActionCommand().equals("run")) {
 			runSimulation();
 		} else if (e.getActionCommand().equals("export")) {
 			export();
 		}
    }
	
    public void stop() {
		simulationPanel.setRunning(false);
		simulationThread.stop();
    }
    
    public abstract void runSimulation();
    
	protected void export() {
        editor.checkModel();
        if (editor.getModelCheckMessages().size() > 0) {
            String messages = new String(bundle.getString("PANEL_CANNOTEXECUTE"));
            for (String msg : editor.getModelCheckMessages()) {
                messages = messages + msg + "\n";
            }
            JOptionPane.showMessageDialog(null, messages);
            return;
        }
        try {
            injectSimulationSettings();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, bundle.getString("PANEL_CANNOTPARSE"));
            return;
        }
        // create the SimQuest model from the CoLab model
        sqModel = new SimquestModelQuantitative(editor.getModel());
        new sqv.Model(sqModel, dataServer);
        FileDialog dialog = new FileDialog((Frame) editor.getRootPane().getParent(), bundle.getString("PANEL_SAVESQX"), FileDialog.SAVE);
        dialog.setFile("*.sqx");
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            Element application = new Element("application");
            Element topics = new Element("topics");
            Element topic = new Element("topic");
            topic.setAttribute("id", java.util.UUID.randomUUID().toString());
            topic.addContent(sqModel);
            topics.addContent(topic);
            application.addContent(topics);
            Document doc = new Document(application);
            try {
                XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                FileWriter writer = new FileWriter(dialog.getDirectory() + dialog.getFile());
                out.output(doc, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
	
	protected void injectSimulationSettings() throws NumberFormatException {
		if (editor.getMode()==Mode.QUALITATIVE_MODELLING) {
			editor.getModel().setStart(-0.9);
			editor.getModel().setStop(0.9);
			editor.getModel().setStep(0.1);
			editor.getModel().setMethod("RungeKuttaFehlberg");
		} else {
			editor.getModel().setStart(Double.parseDouble(simulationPanel.getStart()));
			editor.getModel().setStop(Double.parseDouble(simulationPanel.getStop()));
			editor.getModel().setStep(Double.parseDouble(simulationPanel.getStep()));
			editor.getModel().setMethod(simulationPanel.getMethod());
		}
	}

}