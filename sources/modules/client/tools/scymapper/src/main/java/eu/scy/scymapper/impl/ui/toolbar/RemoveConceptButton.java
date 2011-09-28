package eu.scy.scymapper.impl.ui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

public class RemoveConceptButton extends JButton implements ActionListener {

    private IConceptMap cmap;
    private ConceptDiagramView diagramView;

    public RemoveConceptButton(IConceptMap cmap, ConceptDiagramView diagramView) {
        super();
        this.cmap = cmap;
        this.diagramView = diagramView;
        setToolTipText(Localization.getString("Mainframe.Toolbar.Delete"));
        setIcon(new ImageIcon(getClass().getResource("delete.png")));
        addActionListener(this);

        cmap.getDiagramSelectionModel().addSelectionListener(new IDiagramSelectionListener() {

            @Override
            public void selectionChanged(IDiagramSelectionModel selectionModel) {
                boolean enabled = false;

                for (INodeModel node : selectionModel.getSelectedNodes()) {
                    if (node.getConstraints().getCanDelete()) {
                        enabled = true;
                        break;
                    }
                }
                if (selectionModel.hasLinkSelection()) {
                    enabled = true;
                }
                setEnabled(enabled);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        diagramView.confirmAndRemoveSelectedObjects();
    }
}