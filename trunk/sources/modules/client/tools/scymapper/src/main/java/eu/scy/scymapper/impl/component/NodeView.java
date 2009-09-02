package eu.scy.scymapper.impl.component;

import eu.scy.scymapper.api.diagram.INodeController;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.INodeModelObserver;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.model.NodeModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:27:37
 */
public class NodeView extends JComponent implements INodeModelObserver, KeyListener {

    private static final String RESIZEHANDLE_FILENAME = "resize.png";

    private JComponent resizeHandle;
    private INodeController controller;
    private INodeModel model;
    private JTextField labelEditor;

    private static final INodeStyle DEFAULT_NODESTYLE = new DefaultNodeStyle();
    private Color highlight;

    public NodeView(INodeController controller, INodeModel model) {

        super();

        this.controller = controller;
        this.model = model;

        // Subscribe to events in the model
        this.model.addObserver(this);

        // Listen for user input
        MouseEventsListener mouseListener = new MouseEventsListener();
        addMouseMotionListener(mouseListener);
        addMouseListener(mouseListener);

        setSize(model.getSize());
        setLocation(model.getLocation());

        //TODO:

        //  addFocusListener(nfl);

        setOpaque(false);
        setBackground(new Color(0xaaaaaa));
        setLayout(null);
        setFocusable(true);

        labelEditor = new JTextField(this.model.getLabel());
        labelEditor.setHorizontalAlignment(JTextField.CENTER);
        labelEditor.addKeyListener(this);
        labelEditor.setEditable(false);
        labelEditor.setOpaque(true);

        INodeStyle style = model.getStyle();

        if (style == null) style = DEFAULT_NODESTYLE;

        labelEditor.setBackground(style.getBackground());
        labelEditor.setForeground(style.getForeground());

        labelEditor.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                labelEditor.setEditable(true);
                labelEditor.setOpaque(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                labelEditor.setEditable(false);
                labelEditor.setOpaque(false);
            }
        });
        add(labelEditor);

        resizeHandle = createResizeHandle();
        add(resizeHandle);

        layoutComponents();
    }

    private JComponent createResizeHandle() {
        if (resizeHandle == null) {
            try {
                URL uri = getClass().getResource(RESIZEHANDLE_FILENAME);
                if (uri == null) throw new FileNotFoundException("File " + RESIZEHANDLE_FILENAME + " not found");
                BufferedImage i = ImageIO.read(uri);
                resizeHandle = new JLabel(new ImageIcon(i));
                resizeHandle.setSize(15, 15);
            } catch (IOException e) {
                JLabel button = new JLabel(">");
                Font f = new Font("Serif", Font.PLAIN, 10);
                button.setFont(f);
                button.setSize(10, 10);
                //button.setMargin(new Insets(1, 1, 1, 1));
                resizeHandle = button;
            }
        }
        return resizeHandle;
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
        height += labelEditor.getMargin().bottom + labelEditor.getMargin().bottom;
        height += labelEditor.getBorder().getBorderInsets(null).top + labelEditor.getBorder().getBorderInsets(null).bottom;

        labelEditor.setSize(width, height);

        double x = (getWidth() / 2) - (width / 2);
        double y = (getHeight() / 2d) - (height / 2d);

        labelEditor.setBounds((int) x, (int) y, width, height);

        resizeHandle.setBounds(getWidth() - resizeHandle.getWidth(), getHeight() - resizeHandle.getHeight(), resizeHandle.getWidth(), resizeHandle.getHeight());

        resizeHandle.setForeground(getForeground());
        resizeHandle.setBackground(getBackground());
    }


    public INodeModel getModel() {
        return model;
    }

    public void setModel(INodeModel model) {
        this.model = model;
    }

    @Override
    public void moved(INodeModel model) {
        setLocation(model.getLocation());
    }

    @Override
    public void resized(INodeModel model) {
        setSize(model.getSize());
        layoutComponents();
    }

    @Override
    public void labelChanged(INodeModel node) {
        layoutComponents();
    }

    @Override
    public void styleChanged(INodeModel node) {
    }

    @Override
    public void shapeChanged(INodeModel node) {
        repaint();
    }

    @Override
    public void nodeSelected(NodeModel conceptNode) {
        // Do nothing
        // Todo: paint with selectedstyle?
    }

    public void setHighlight(Color c) {
        this.highlight = c;
        repaint();
    }

    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        INodeStyle style = model.getStyle();
        if (style == null) style = DEFAULT_NODESTYLE;

        if (highlight != null) g2.setColor(highlight);
        else g2.setColor(style.getBackground());

        g2.setStroke(style.getStroke());

        INodeShape shape = model.getShape();
        if (shape != null) {
            if (style.getFillStyle() == INodeStyle.FILLSTYLE_FILLED)
                shape.setMode(INodeShape.FILL);
            else
                shape.setMode(INodeShape.DRAW);
            shape.paint(g2, getBounds());
        }

        // Continue painting any other component
        super.paint(g2);
    }

    public Point getRelativeCenter() {
        return new Point(getSize().width / 2, getSize().height / 2);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        controller.setLabel(labelEditor.getText());
    }


    public Point getConnectionPoint(Point p) {
        return model.getConnectionPoint(p);
    }

    @Override
    public String toString() {
        return "NodeView{" +
                "model=" + model +
                '}';
    }

    private final class MouseEventsListener implements MouseMotionListener, MouseListener {
        private Point relativePos = null;
        private Point lastLocation = null;

        private final int MODE_NONE = -1;
        private final int MODE_DRAGGING = 0;
        private final int MODE_RESIZING = 1;

        private int mode = MODE_NONE;

        public void mouseDragged(MouseEvent e) {

            // The relative mouse position from the component x,y
            Point relPoint = e.getPoint();

            // Tha impl
            NodeView view = (NodeView) e.getSource();

            // Create the new location
            Point newLocation = view.getLocation();
            // Translate the newLocation with the relative point
            newLocation.translate(relPoint.x, relPoint.y);
            newLocation.translate(-relativePos.x, -relativePos.y);

            switch (mode) {
                case MODE_DRAGGING:
                    controller.setLocation(newLocation);
                    break;
                case MODE_RESIZING:
                    if (lastLocation != null) {

                        int dx = relPoint.x - lastLocation.x;
                        int dy = relPoint.y - lastLocation.y;
                        // Old size of the impl
                        Dimension newSize = new Dimension(getWidth() + dx, getHeight() + dy);
                        if (newSize.width < 40) newSize.width = 40;
                        if (newSize.height < 40) newSize.height = 40;

                        controller.setSize(newSize);

                        lastLocation = new Point(newSize.width, newSize.height);
                    } else
                        lastLocation = new Point(relPoint);
                    break;
            }
        }

        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            requestFocus();
            controller.setSelected(true);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            relativePos = e.getPoint();

            Component componentAt = getComponentAt(relativePos.x, relativePos.y);
            if (resizeHandle.equals(componentAt)) mode = MODE_RESIZING;
            else mode = MODE_DRAGGING;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lastLocation = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
