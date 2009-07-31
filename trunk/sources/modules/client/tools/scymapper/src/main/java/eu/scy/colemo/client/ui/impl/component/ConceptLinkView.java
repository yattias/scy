package eu.scy.colemo.client.ui.impl.component;

import eu.scy.colemo.client.ui.api.links.ILinkController;
import eu.scy.colemo.client.ui.api.links.IConceptLink;
import eu.scy.colemo.client.ui.api.links.ILink;
import eu.scy.colemo.client.ui.api.nodes.INodeObserver;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.impl.component.LinkView;
import eu.scy.colemo.client.ui.impl.model.ConceptNode;

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
public class ConceptLinkView extends LinkView implements KeyListener, INodeObserver {

    private JTextField labelEditor;

    public ConceptLinkView(ILinkController controller, IConceptLink model) {
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
    public void updated(ILink m) {
        layoutComponents();
        super.updated(m);
    }

    @Override
    public void moved(IConceptNode node) {
        updatePosition();
        layoutComponents();
    }

    @Override
    public void resized(IConceptNode node) {
        updatePosition();
        layoutComponents();
    }

    // Do nothing when these events happens in one of my nodes
    @Override
    public void labelChanged(IConceptNode node) {}
    @Override
    public void styleChanged(IConceptNode conceptNode) {}
    @Override
    public void shapeChanged(IConceptNode conceptNode) {}

    @Override
    public void nodeSelected(ConceptNode conceptNode) {
        
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
