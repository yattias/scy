package eu.scy.scymapper.impl.ui.toolbar;

import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.impl.ui.Localization;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class BackgroundColorButton extends JButton implements ActionListener, IDiagramSelectionListener {

	private final ConceptMapToolBar toolbar;

	public BackgroundColorButton(ConceptMapToolBar toolbar, IDiagramSelectionModel diagramSelectionModel) {
		super(Localization.getString("Mainframe.Toolbar.Background.Label"));
		this.toolbar = toolbar;
		setIcon(new ImageIcon(getClass().getResource("color-bg.png")));
		setToolTipText(Localization.getString("Mainframe.Toolbar.Background.Tooltip"));
		setEnabled(false);
		this.addActionListener(this);
		diagramSelectionModel.addSelectionListener(this);
	}

	@Override
	public void selectionChanged(IDiagramSelectionModel selectionModel) {
		setEnabled(selectionModel.hasSelection());
		//setDisplayColor(getSelectionBg());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ColorDialog cdialog = new ColorDialog(JOptionPane.getFrameForComponent(toolbar), ((JButton)e.getSource()).getLocationOnScreen(), toolbar);
		cdialog.setVisible(true);
	}

}
