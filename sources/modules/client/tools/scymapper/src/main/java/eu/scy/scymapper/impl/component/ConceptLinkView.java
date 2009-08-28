package eu.scy.scymapper.impl.component;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModelObserver;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.impl.component.LinkView;
import eu.scy.scymapper.impl.model.NodeModel;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 11:24:47
 * To change this template use File | Settings | File Templates.
 */
public class ConceptLinkView extends LinkView implements KeyListener, INodeModelObserver {

    private JTextField labelEditor;

    public ConceptLinkView(ILinkController controller, IConceptLinkModel model) {
        super(controller, model);

        // I want to observe changes in my connected nodes
        if (model.getFromNode() != null) model.getFromNode().addObserver(this);
        if (model.getToNode() != null) model.getToNode().addObserver(this);

        labelEditor = new JTextField(this.model.getLabel());
        labelEditor.setHorizontalAlignment(JTextField.CENTER);
        labelEditor.addKeyListener(this);
        labelEditor.setEditable(false);
        labelEditor.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                labelEditor.setEditable(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                labelEditor.setEditable(false);
            }
        });
        add(labelEditor);

        updatePosition();
        layoutComponents();
    }

    private void layoutComponents() {
        FontMetrics f = labelEditor.getFontMetrics(labelEditor.getFont());
        int width = f.stringWidth(labelEditor.getText());
        width += labelEditor.getMargin().left + labelEditor.getMargin().right;
        width += labelEditor.getBorder().getBorderInsets(null).left + labelEditor.getBorder().getBorderInsets(null).right;

        if (width < 20) width = 20;

        // Add some space
        width += 10;
        if (width > getWidth()) width = getWidth();

        int height = f.getHeight();
        height += labelEditor.getMargin().top + labelEditor.getMargin().bottom;
        height += labelEditor.getBorder().getBorderInsets(null).top + labelEditor.getBorder().getBorderInsets(null).bottom;

        double x = (getWidth() / 2) - (width / 2);
        double y = (getHeight() / 2d) - (height / 2d);

        labelEditor.setBounds((int) x, (int) y, width, height);
    }

    @Override
    public void updated(ILinkModel m) {
        layoutComponents();
        super.updated(m);
    }

    @Override
    public void moved(INodeModel node) {
        updatePosition();
        layoutComponents();
    }

    @Override
    public void resized(INodeModel node) {
        updatePosition();
        layoutComponents();
    }

    // Do nothing when these events happens in one of my nodes
    @Override
    public void labelChanged(INodeModel node) {}
    @Override
    public void styleChanged(INodeModel node) {}
    @Override
    public void shapeChanged(INodeModel node) {}

    @Override
    public void nodeSelected(NodeModel conceptNode) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        controller.setLabel(labelEditor.getText());
    }
}
