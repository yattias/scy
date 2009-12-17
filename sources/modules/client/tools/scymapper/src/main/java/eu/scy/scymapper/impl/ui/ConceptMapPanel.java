package eu.scy.scymapper.impl.ui;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 29.okt.2009
 * Time: 19:33:00
 * This panel contains two sub-components: the toolbar that allows manipulation of the concept map diagram
 * and the concept map diagram view itself.
 */
public class ConceptMapPanel extends JPanel {
    private JToolBar toolBar;
    private IConceptMap model;
    private ConceptDiagramView conceptDiagramView;

    public ConceptMapPanel(IConceptMap model) {
        this.model = model;
        initComponents();
        initToolBar();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        conceptDiagramView = new ConceptDiagramView(new DiagramController(model.getDiagram()), model.getDiagram(), model.getDiagramSelectionModel());
        add(new JScrollPane(conceptDiagramView));
    }

    public ConceptDiagramView getDiagramView() {
        return conceptDiagramView;
    }

    private void initToolBar() {
        toolBar = new JToolBar();

        JButton clearConceptMapBtn = new JButton("Clear");
        URL url = getClass().getResource("icons/clear.png");
        if (url != null) clearConceptMapBtn.setIcon(new ImageIcon(url));

        clearConceptMapBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "This will removeNode all concepts from the current concept map. Are you sure you'd like to do this?", "Are you sure?", JOptionPane.YES_NO_OPTION))
                    conceptDiagramView.getController().removeAll();
            }
        });
        toolBar.add(clearConceptMapBtn);

        JButton removeConceptBtn = new JButton("Remove", new ImageIcon(getClass().getResource("icons/delete.png")));
        removeConceptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (INodeModel selectedNode : model.getDiagramSelectionModel().getSelectedNodes()) {
                    conceptDiagramView.getController().removeNode(selectedNode);
                }
                for (ILinkModel selectedNode : model.getDiagramSelectionModel().getSelectedLinks()) {
                    conceptDiagramView.getController().removeLink(selectedNode);
                }
            }
        });
        toolBar.add(removeConceptBtn);
        add(toolBar, BorderLayout.NORTH);
    }
}
