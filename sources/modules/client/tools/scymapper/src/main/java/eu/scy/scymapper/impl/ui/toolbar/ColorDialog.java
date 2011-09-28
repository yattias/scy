package eu.scy.scymapper.impl.ui.toolbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;

import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

@SuppressWarnings("serial")
public class ColorDialog extends JDialog implements java.awt.event.ActionListener {

    private static final Color[] colors = { Color.WHITE, new Color(250, 128, 114), new Color(255, 105, 180), new Color(255, 165, 0), new Color(255, 255, 0), new Color(238, 130, 238), new Color(124, 252, 0), new Color(154, 205, 50), new Color(102, 205, 170), new Color(100, 149, 237), new Color(255, 222, 173) };

    private static final int size = 20;

    private IDiagramSelectionModel diagramSelectionModel;

    private ConceptDiagramView cdv;

    public ColorDialog(Frame frame, Point position, IDiagramSelectionModel diagramSelectionModel,ConceptDiagramView cdv) {
        super(frame, true);
        this.diagramSelectionModel = diagramSelectionModel;
        this.cdv = cdv;
        this.setTitle(Localization.getString("Mainframe.Toolbar.Background.Tooltip"));
        this.getContentPane().setLayout(new FlowLayout());

        JButton colorButton;
        for (Color color : colors) {
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(color);
            g.fillRect(0, 0, size, size);
            colorButton = new JButton(new javax.swing.ImageIcon(image));
            colorButton.setPreferredSize(new Dimension(20, 20));
            colorButton.setBackground(color);
            colorButton.setActionCommand("color");
            colorButton.setSelected(false);
            colorButton.addActionListener(this);
            this.getContentPane().add(colorButton);
        }

        colorButton = new JButton(Localization.getString("Mainframe.Input.Close"));
        colorButton.setActionCommand("cancel");
        colorButton.addActionListener(this);
        this.getContentPane().add(colorButton);
        pack();
        this.setLocation(position.x, position.y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("color")) {

            setBackgroundColorOfSelection(((JButton)e.getSource()).getBackground());
            setVisible(false);
            setEnabled(false);
            dispose();
        } else if (e.getActionCommand().equals("cancel")) {
            setVisible(false);
            setEnabled(false);
            dispose();
        }
    }

    public void setBackgroundColorOfSelection(Color bg) {
        if (diagramSelectionModel.hasSelection()) {
            for (Component comp : cdv.getComponents()) {
                if (comp instanceof NodeViewComponent) {
                    NodeViewComponent nw = ((NodeViewComponent) comp);
                    if (!nw.getModel().isSelected())
                        continue;
                    INodeStyle style = nw.getModel().getStyle();
                    style.setBackground(bg);
                    ((NodeViewComponent) comp).getController().setStyle(style);
                } else if (comp instanceof LinkViewComponent) {
                    LinkViewComponent lw = ((LinkViewComponent) comp);
                    if (!lw.getModel().isSelected())
                        continue;
                    ILinkStyle style = lw.getModel().getStyle();
                    style.setBackground(bg);
                    ((LinkViewComponent) comp).getController().setStyle(style);

                }
            }
            // setDisplayColor(getSelectionBg());
        }
    }

}
