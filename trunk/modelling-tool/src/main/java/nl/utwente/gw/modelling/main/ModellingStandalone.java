package nl.utwente.gw.modelling.main;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import nl.utwente.gw.modelling.editor.ModelEditor;

import colab.um.JColab;
import colab.um.tools.JTools;

public class ModellingStandalone {

	private JDesktopPane desktop;
	private JFrame frame;
	//private JInternalFrame iFrame;
	//private JPanel pTime;
	//private JLabel lblTime;
	private ModelEditor aEditor;

	public ModellingStandalone() {
		new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		initGUI();
		aEditor.setNewModel();
	}

	private void initGUI() {
		Box b = new Box(BoxLayout.Y_AXIS);
		b.add(new JLabel("one"));
		b.add(new JLabel("two"));
		//iFrame = new JInternalFrame("iframe", true, true, true, true);
		//iFrame.setContentPane(b);
		//iFrame.pack();
		//iFrame.setVisible(true);

		JInternalFrame iFrame2 = new JInternalFrame("iframe2", true, true, true, true);
		iFrame2.getContentPane().add(createComponents());
		iFrame2.pack();
		iFrame2.setVisible(true);

		desktop = new JDesktopPane();
		//desktop.add(iFrame);
		desktop.add(iFrame2);

		frame = new JFrame("DesktopPane");
		frame.getContentPane().add(desktop);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	//	private JPanel createEditorPanel() {		
	//		JdEditorStandalone editor = new JdEditorStandalone();
	//		return editor;
	//	}

	private JPanel createComponents() {
		// aEditor holds the workspace and the toolbar with stock / flow etc.
		//aEditor = new JdEditorStandalone();
		aEditor = new ModelEditor();
		//aEditor.setXmModel(null);
		//JdControlStandalone listener = new JdControlStandalone(aEditor);
		//ActionListener listener  = new ModellingAL(aEditor);

		// file & models panel
		//JToolBar fileToolbar = JTools.createToolbarH("vtEditorToolbar", listener);
		//		JToolBar fileToolbar = JTools.createToolbarH("vtEditorToolbar", (ActionListener) new JvtEditorAL());
		//		JPanel modelPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
		//		modelPanel.setBorder(new EmptyBorder(0, 2, 0, 2));
		//		JLabel lblModel = new javax.swing.JLabel("  " + JTools.getAppResourceString("VisualToolMoLabel") + " ");
		//		lblModel.setFont(new java.awt.Font("Courier New", 1, 11));
		//		JComboBox cbModel = new javax.swing.JComboBox();
		//		cbModel.setEditable(false);
		//		cbModel.setActionCommand("selectview");
		//		cbModel.addActionListener(listener);
		//		modelPanel.add(lblModel, java.awt.BorderLayout.WEST);
		//		modelPanel.add(cbModel, java.awt.BorderLayout.CENTER);
		//		JPanel vPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
		//		vPanel.setBorder(BorderFactory.createEtchedBorder());
		//		vPanel.add(fileToolbar, java.awt.BorderLayout.WEST);
		//		vPanel.add(modelPanel, java.awt.BorderLayout.CENTER);
		//		// time panel
		//		Font aFont = new java.awt.Font("Courier New", 1, 10);
		//		pTime = new javax.swing.JPanel(new java.awt.BorderLayout());
		//		pTime.setBorder(new EmptyBorder(2, 2, 2, 2));
		//		JPanel pLabels = new javax.swing.JPanel(new java.awt.BorderLayout());
		//		lblTime = new javax.swing.JLabel("t=0");
		//		lblTime.setFont(aFont);
		//		lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		//		lblTime.setForeground(Color.red);
		//		JLabel lblMin = new javax.swing.JLabel("0");
		//		lblMin.setFont(aFont);
		//		lblMin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		//		pLabels.add(lblTime, java.awt.BorderLayout.CENTER);
		//		pLabels.add(lblMin, java.awt.BorderLayout.WEST);
		//		pTime.add(pLabels, java.awt.BorderLayout.CENTER);
		//		JProgressBar pbTime = new javax.swing.JProgressBar(SwingConstants.HORIZONTAL);
		//		pbTime.setPreferredSize(new Dimension(1000,5));
		//		pbTime.setEnabled(false);
		//		pTime.add(pbTime, java.awt.BorderLayout.SOUTH);
		//		JColabTextField txtStop = new JColabTextField(JColabTextField.DOUBLE);
		//		txtStop.setValue(0.0);
		//		txtStop.setPreferredSize(new Dimension(40,15));
		//		txtStop.setMinimumSize(new Dimension(40,15));
		//		txtStop.setFont(aFont);
		//		txtStop.setActionCommand("txtStop");
		//		txtStop.addActionListener(listener);
		//		pTime.add(txtStop,BorderLayout.EAST);
		//		// simulation control toolbar
		//		JToolBar ctrlToolbar = JTools.createToolbarH("EditorCtrlToolbar", listener);
		//		ctrlToolbar.setBorder(new EmptyBorder(1, 1, 1, 1));
		//		// control panel
		//		JPanel pCtrl = new JPanel(new java.awt.BorderLayout());
		//		pCtrl.setBorder(BorderFactory.createEtchedBorder());
		//		pCtrl.add(ctrlToolbar, BorderLayout.WEST);
		//		pCtrl.add(pTime, BorderLayout.CENTER);
		//		// toolbars panel
		//		JPanel pToolbar = new JPanel(new java.awt.BorderLayout());
		//		pToolbar.setBorder(BorderFactory.createEtchedBorder());
		//		pToolbar.add(vPanel, BorderLayout.NORTH);
		//		pToolbar.add(pCtrl, BorderLayout.CENTER);
		// message panel
		//	      JPanel pStatus = new JPanel(new java.awt.BorderLayout());
		//	      pStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		//	      pStatus.setPreferredSize(new Dimension(40,18));
		//	      pStatus.setMinimumSize(new Dimension(40,18));
		//	      JLabel lblMsg = new javax.swing.JLabel();
		//	      lblMsg.setBorder(new EmptyBorder(0, 5, 0, 0));
		//	      lblMsg.setFont(new java.awt.Font("Dialog", 1, 10));
		//	      pStatus.add(lblMsg, BorderLayout.CENTER);
		// top panel
		JPanel top = new JPanel(new java.awt.BorderLayout());
		top.setBorder(new EmptyBorder(2,0,2,0));
		top.add(aEditor, BorderLayout.CENTER);
		//top.add(pToolbar, BorderLayout.NORTH);
		//	      top.add(pStatus, BorderLayout.SOUTH);
		return top;
	}

	public static void main(String[] args) {
		new ModellingStandalone();
	}

}
