package eu.scy.scymapper.impl.ui.toolbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

public class BackgroundColorButton extends JButton implements ActionListener, IDiagramSelectionListener {

    private ConceptDiagramView cdv;

    private IDiagramSelectionModel diagramSelectionModel;

    public BackgroundColorButton(ConceptDiagramView cdv, IDiagramSelectionModel diagramSelectionModel) {
//        super(Localization.getString("Mainframe.Toolbar.Background.Label"));
        super();
        this.cdv = cdv;
        this.diagramSelectionModel = diagramSelectionModel;
        setIcon(new ImageIcon(getClass().getResource("colorchoser.png")));
        setToolTipText(Localization.getString("Mainframe.Toolbar.Background.Tooltip"));
        setEnabled(false);
        this.addActionListener(this);
        diagramSelectionModel.addSelectionListener(this);
    }

    @Override
    public void selectionChanged(IDiagramSelectionModel selectionModel) {
        setEnabled(selectionModel.hasSelection());
        // setDisplayColor(getSelectionBg());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ColorDialog cdialog = new ColorDialog(JOptionPane.getFrameForComponent(cdv), ((JButton) e.getSource()).getLocationOnScreen(), diagramSelectionModel,cdv);
        cdialog.setVisible(true);
    }



}
