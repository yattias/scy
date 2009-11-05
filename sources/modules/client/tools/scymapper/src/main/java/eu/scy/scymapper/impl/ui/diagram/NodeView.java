package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.INodeController;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.INodeModelListener;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private JTextField labelEditor;

    private static final INodeStyle DEFAULT_NODESTYLE = new DefaultNodeStyle();

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

        labelEditor = new JTextField(this.model.getLabel());
        labelEditor.setHorizontalAlignment(JTextField.CENTER);
        labelEditor.addKeyListener(this);
        labelEditor.setEditable(false);
        labelEditor.setOpaque(false);

        INodeStyle style = model.getStyle();

        if (style == null) style = DEFAULT_NODESTYLE;

		labelEditor.setBorder(BorderFactory.createEmptyBorder());

        add(labelEditor);
        labelEditor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                labelEditor.setEditable(true);
                labelEditor.setOpaque(true);
				labelEditor.setBorder(BorderFactory.createEtchedBorder());
            }

            @Override
            public void focusLost(FocusEvent e) {
                labelEditor.setEditable(false);
                labelEditor.setOpaque(false);
				labelEditor.setBorder(BorderFactory.createEmptyBorder());
            }
        });

		labelEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) NodeView.this.requestFocus();
			}
		});

//        resizeHandle = createResizeHandle();
//        add(resizeHandle);

        layoutComponents();
    }

//    private JComponent createResizeHandle() {
//        if (resizeHandle == null) {
//            try {
//                URL uri = getClass().getResource(RESIZEHANDLE_FILENAME);
//                if (uri == null) throw new FileNotFoundException("File " + RESIZEHANDLE_FILENAME + " not found");
//                BufferedImage i = ImageIO.read(uri);
//                resizeHandle = new JLabel(new ImageIcon(i));
//                resizeHandle.setSize(15, 15);
//            } catch (IOException e) {
//                JLabel button = new JLabel(">");
//                Font f = new Font("Serif", Font.PLAIN, 10);
//                button.setFont(f);
//                button.setSize(10, 10);
//                //button.setMargin(new Insets(1, 1, 1, 1));
//                resizeHandle = button;
//            }
//        }
//        return resizeHandle;
//    }


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
        labelEditor.setVisible(!getModel().isLabelHidden());

        double x = (getWidth() / 2) - (width / 2);
        double y = (getHeight() / 2d) - (height / 2d);

        labelEditor.setBounds((int) x, (int) y, width, height);

//        resizeHandle.setBounds(getWidth() - resizeHandle.getWidth(), getHeight() - resizeHandle.getHeight(), resizeHandle.getWidth(), resizeHandle.getHeight());

//        resizeHandle.setForeground(getForeground());
 //       resizeHandle.setBackground(getBackground());
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
                conceptNode.isSelected() ? BorderFactory.createLineBorder(model.getStyle().getSelectionColor(), 1) :
                        BorderFactory.createEmptyBorder()
        );
        if (conceptNode.isSelected()) requestFocus();
    }

    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        INodeStyle style = model.getStyle();
        if (style == null) style = DEFAULT_NODESTYLE;

        g2.setColor(style.getBackground());
        g2.setStroke(style.getStroke());

		Rectangle relativeBounds = new Rectangle(new Point(0,0), getSize());

        INodeShape shape = model.getShape();
        if (shape != null) {
            shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
            shape.paint(g2, relativeBounds);
        }

		// Update colors of label component TODO: Should have a style listener instead
		labelEditor.setForeground(getModel().getStyle().getForeground());
		labelEditor.setBackground(getModel().getStyle().getBackground());

        // Continue painting any other component
        super.paint(g);
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

	@Override
	public void styleChanged(INodeStyle s) {
		repaint();
	}
}
