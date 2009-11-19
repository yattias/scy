package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.INodeController;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.INodeModelListener;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;
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
public class NodeView extends JComponent implements INodeModelListener, KeyListener, INodeStyleListener {

    private static final String RESIZEHANDLE_FILENAME = "resize.png";

    private JComponent resizeHandle;
    private INodeController controller;
    private INodeModel model;
    private JTextArea labelTextarea;

    private static final INodeStyle DEFAULT_NODESTYLE = new DefaultNodeStyle();
    private JScrollPane labelScroller;
    private boolean isEditing;

    public NodeView(INodeController controller, INodeModel model) {

        super();

        this.controller = controller;
        this.model = model;

        // Subscribe to events in the model
        this.model.addListener(this);

        // Subscribe to events in the models style
        this.model.getStyle().addStyleListener(this);

        setSize(model.getSize());
        setLocation(model.getLocation());

        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        labelTextarea = new JTextArea(this.model.getLabel());
        //labelTextarea.setHorizontalAlignment(JTextField.CENTER);
        labelTextarea.addKeyListener(this);
        labelTextarea.setForeground(getModel().getStyle().getForeground());
        labelTextarea.setWrapStyleWord(true);
        labelTextarea.setLineWrap(true);
        labelTextarea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        labelScroller = new JScrollPane(labelTextarea);
        labelTextarea.setMargin(new Insets(0, 0, 0, 0));
        labelTextarea.setBorder(BorderFactory.createEmptyBorder());
        labelScroller.getViewport().setOpaque(false);

        setLabelEditable(false);

        add(labelScroller);

//        MouseListener dblClickListener = new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2)
//                    setLabelEditable(true);
//            }
//        };
//
//        addMouseListener(dblClickListener);
        labelTextarea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setLabelEditable(true);
            }
        });

        labelTextarea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setLabelEditable(false);
            }
        });

        labelTextarea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == 10) NodeView.this.requestFocus();
            }
        });

        MouseAdapter parentEventDispatcher = new ParentComponentEventDispatcher(this) {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isEditing) super.mouseDragged(e);
            }
        };
        labelTextarea.addMouseMotionListener(parentEventDispatcher);
        labelTextarea.addMouseListener(parentEventDispatcher);

        if (model.getConstraints().getCanResize()) {
            resizeHandle = createResizeHandle();
            add(resizeHandle);

            resizeHandle.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Dimension size = NodeView.this.model.getSize();
                    size.height += e.getPoint().y;
                    size.width += e.getPoint().x;
                    if (size.height < 70) size.height = 70;
                    if (size.width < 70) size.width = 70;
                    NodeView.this.controller.setSize(size);
                }
            });
        }
        layoutComponents();
    }

    private void setLabelEditable(boolean editable, int caretPos) {

        if (!model.getConstraints().getCanEditLabel()) editable = false;

        labelScroller.setOpaque(editable);
        labelScroller.getViewport().setOpaque(editable);
        labelScroller.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));

        labelTextarea.setFocusable(editable);
        labelTextarea.setEditable(editable);
        labelTextarea.setOpaque(editable);


        labelTextarea.setForeground(editable ? Color.BLACK : model.getStyle().getForeground());
        labelTextarea.setBackground(editable ? Color.WHITE : model.getStyle().getBackground());

        labelTextarea.setCaretPosition(caretPos);
        if (editable) labelTextarea.requestFocus();

        isEditing = editable;
    }

    void setLabelEditable(boolean editable) {
        setLabelEditable(editable, labelTextarea.getCaretPosition());
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

        FontMetrics f = labelTextarea.getFontMetrics(labelTextarea.getFont());
        int width = f.stringWidth(labelTextarea.getText()) + 10;

        int maxHeight = getHeight() - 20; // 10 px spacing on each side
        int maxWidth = getWidth() - 20; // 10 px spacing on each side

        if (width < 70) width = 70;
            // Add some space
        else width = (width + 10 > maxWidth) ? maxWidth : width + 10;

        int height = labelTextarea.getPreferredScrollableViewportSize().height + 10;


        if (height > maxHeight) {
            height = maxHeight;
            labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
            labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        //labelTextarea.setSize(width, height);
        labelTextarea.setVisible(!getModel().isLabelHidden());

        double x = ((maxWidth / 2) - (width / 2)) + 10;
        double y = ((maxHeight / 2d) - (height / 2d)) + 10;

        labelScroller.setBounds((int) x, (int) y, width, height);
        labelScroller.revalidate();

        if (resizeHandle != null) {
            resizeHandle.setBounds(getWidth() - resizeHandle.getWidth(), getHeight() - resizeHandle.getHeight(), resizeHandle.getWidth(), resizeHandle.getHeight());

            resizeHandle.setForeground(getForeground());
            resizeHandle.setBackground(getBackground());
        }
    }

    public INodeModel getModel() {
        return model;
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
    public void shapeChanged(INodeModel node) {
        repaint();
    }

    @Override
    public void selectionChanged(INodeModel conceptNode) {
        setBorder(
                conceptNode.isSelected() ? BorderFactory.createLineBorder(new Color(0xc0c0c0), 1) :
                        BorderFactory.createEmptyBorder()
        );
        if (conceptNode.isSelected()) requestFocus();
    }

    @Override
    public void deleted(NodeModel nodeModel) {
        this.setVisible(false);
    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        INodeStyle style = model.getStyle();
        if (style == null) style = DEFAULT_NODESTYLE;

        g2.setColor(style.getBackground());
        g2.setStroke(style.getStroke());

        Rectangle relativeBounds = new Rectangle(new Point(0, 0), getSize());

        INodeShape shape = model.getShape();
        if (shape != null) {
            shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
            shape.paint(g2, relativeBounds);
        }
        // Continue painting any other component
        super.paintComponent(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        controller.setLabel(labelTextarea.getText());
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

    @Override
    public void styleChanged(INodeStyle s) {
        repaint();
    }
}

class ParentComponentEventDispatcher extends MouseAdapter {
    private Component reciever;

    ParentComponentEventDispatcher(Component reciever) {
        this.reciever = reciever;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        redirectMouseEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        redirectMouseEvent(e);
    }

    void redirectMouseEvent(MouseEvent e) {
        //e.translatePoint(reciever.getX(), reciever.getY());
        reciever.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, reciever));
    }
}