package eu.scy.scymapper.impl.ui.toolbar;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

public class ForegroundColorButton extends ColorChooserButton {

    private IConceptMap cmap;

    private IDiagramSelectionModel diagramSelectionModel;

    private ConceptDiagramView diagramView;

    public ForegroundColorButton(IConceptMap cmap, ConceptDiagramView diagramView) {
        super(Localization.getString("Mainframe.Toolbar.Foreground.Label"));
        this.cmap = cmap;
        this.diagramView = diagramView;
        diagramSelectionModel = cmap.getDiagramSelectionModel();
        setDisplayColor(getSelectionFg());
        setToolTipText(Localization.getString("Mainframe.Toolbar.Foreground.Tooltip"));
        setIcon(new ImageIcon(getClass().getResource("color-fg.png")));

        setEnabled(false);

        cmap.getDiagramSelectionModel().addSelectionListener(new IDiagramSelectionListener() {

            @Override
            public void selectionChanged(IDiagramSelectionModel selectionModel) {
                setEnabled(selectionModel.hasSelection());
                setDisplayColor(getSelectionFg());
            }
        });
    }

    Color getSelectionFg() {
        if (diagramSelectionModel.hasSelection()) {
            if (diagramSelectionModel.isMultipleSelection()) {
                Color prev = null;
                for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
                    if (prev == null)
                        prev = node.getStyle().getForeground();
                    else if (!node.getStyle().getForeground().equals(prev))
                        return null;
                }
                for (ILinkModel link : diagramSelectionModel.getSelectedLinks()) {
                    if (prev == null)
                        prev = link.getStyle().getForeground();
                    else if (!link.getStyle().getForeground().equals(prev))
                        return null;
                }
                return prev;

            } else {
                if (diagramSelectionModel.hasLinkSelection())
                    return diagramSelectionModel.getSelectedLink().getStyle().getForeground();
                else
                    return diagramSelectionModel.getSelectedNode().getStyle().getForeground();
            }
        }
        return null;
    }

    @Override
    void colorSelected(Color fg) {

        if (diagramSelectionModel.hasSelection()) {
            for (Component comp : diagramView.getComponents()) {
                if (comp instanceof NodeViewComponent) {
                    NodeViewComponent nw = ((NodeViewComponent) comp);
                    if (!nw.getModel().isSelected())
                        continue;
                    INodeStyle style = nw.getModel().getStyle();
                    style.setForeground(fg);
                    ((NodeViewComponent) comp).getController().setStyle(style);
                } else if (comp instanceof LinkViewComponent) {
                    LinkViewComponent lw = ((LinkViewComponent) comp);
                    if (!lw.getModel().isSelected())
                        continue;
                    ILinkStyle style = lw.getModel().getStyle();
                    style.setForeground(fg);
                    ((LinkViewComponent) comp).getController().setStyle(style);
                }
            }
            setDisplayColor(getSelectionFg());
        }
    }
}