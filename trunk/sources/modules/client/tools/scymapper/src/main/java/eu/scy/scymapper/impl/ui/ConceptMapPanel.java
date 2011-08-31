package eu.scy.scymapper.impl.ui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

/**
 * Created by IntelliJ IDEA. User: Bjoerge Date: 29.okt.2009 Time: 19:33:00 This panel contains two sub-components: the toolbar that allows manipulation of the concept map diagram and the concept map diagram view itself.
 */
public class ConceptMapPanel extends JPanel {

    private IConceptMap model;

    private ConceptDiagramView conceptDiagramView;

    private DiagramController controller;

    public ConceptMapPanel(IConceptMap model) {
        this.model = model;
       
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        controller = new DiagramController(model.getDiagram(), model.getDiagramSelectionModel());
        conceptDiagramView = new ConceptDiagramView(getController(), model.getDiagram(), model.getDiagramSelectionModel());
        add(new JScrollPane(conceptDiagramView));
    }

    public ConceptDiagramView getDiagramView() {
        return conceptDiagramView;
    }



    /**
     * @return the controller
     */
    public DiagramController getController() {
        return controller;
    }
    public IConceptMap getConceptMapModel(){
        return model;
    }
}
