package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 11:24:47
 * To change this template use File | Settings | File Templates.
 */
public class ConceptLinkView extends LinkView implements KeyListener, INodeModelListener {

    private JTextField labelEditor;

    public ConceptLinkView(ILinkController controller, INodeLinkModel model) {
        super(controller, model);

        // I want to observe changes in my connected nodes
        if (model.getFromNode() != null) model.getFromNode().addListener(this);
        if (model.getToNode() != null) model.getToNode().addListener(this);

        labelEditor = new JTextField(model.getLabel());
        labelEditor.setHorizontalAlignment(JTextField.CENTER);
        labelEditor.addKeyListener(this);
        labelEditor.setEditable(false);

        add(labelEditor);
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
        labelEditor.setVisible(!getModel().isLabelHidden());
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
    public void shapeChanged(INodeModel node) {}

    @Override
    public void nodeSelected(INodeModel conceptNode) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        getController().setLabel(labelEditor.getText());
    }
}
