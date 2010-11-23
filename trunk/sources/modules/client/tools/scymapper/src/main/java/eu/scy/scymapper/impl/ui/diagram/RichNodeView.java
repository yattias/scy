package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;
import eu.scy.scymapper.impl.ui.Localization;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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
public class RichNodeView extends NodeViewComponent implements INodeModelListener, INodeStyleListener {

	private final static Logger logger = Logger.getLogger(RichNodeView.class);

	private static final String RESIZEHANDLE_FILENAME = "resize.png";

	protected JTextArea labelTextarea;
	protected JComponent resizeHandle;

	protected JScrollPane labelScroller;
	private boolean isEditing;
	private Border selectionBorder;

	public RichNodeView(INodeController controller, INodeModel model) {

		super(controller, model);

		// Subscribe to events in the model
		getModel().addListener(this);

		// Subscribe to events in the models style
		getModel().getStyle().addStyleListener(this);

		setSize(model.getSize());
		setLocation(model.getLocation());

		setOpaque(false);
		setLayout(null);
		setFocusable(true);

		labelTextarea = new JTextArea(getModel().getLabel());
		//labelTextarea.setHorizontalAlignment(JTextField.CENTER);
		labelTextarea.setForeground(getModel().getStyle().getForeground());
		labelTextarea.setWrapStyleWord(true);
		labelTextarea.setLineWrap(true);
		labelTextarea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		labelScroller = new JScrollPane(labelTextarea);
		labelTextarea.setMargin(new Insets(0, 0, 0, 0));
		labelTextarea.setBorder(BorderFactory.createEmptyBorder());
		labelScroller.getViewport().setOpaque(false);
		labelScroller.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		setLabelEditable(false, true);

		add(labelScroller);

		MouseListener dblClickListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setLabelEditable(true);
				}
			}
		};
		addMouseListener(dblClickListener);

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
				if (!labelTextarea.getText().equals(getModel().getLabel())) {
					getController().setLabel(labelTextarea.getText());
				}
			}
		});

		labelTextarea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					requestFocus();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				layoutComponents();
			}
		});

		logger.debug("Can resize: " + model.getConstraints().getCanResize());
		if (model.getConstraints().getCanResize()) {
			resizeHandle = createResizeHandle();
			add(resizeHandle);

			MouseListener resizeHandleListener = new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					resizeHandle.setVisible(true);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					resizeHandle.setVisible(false);
				}
			};

			addMouseListener(resizeHandleListener);
			resizeHandle.addMouseListener(resizeHandleListener);

			resizeHandle.addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					Dimension size = getModel().getSize();
					size.height += e.getPoint().y;
					size.width += e.getPoint().x;
					getController().setSize(size);
				}
			});
		}

		//setToolTipText(model.getId());

		labelTextarea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// Create a border using the inverted background color
				int c = getModel().getStyle().getBackground().getRGB() ^ 0xFFFFFF;
				if (!isEditing) labelScroller.setBorder(BorderFactory.createLineBorder(new Color(c), 1));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isEditing) labelScroller.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		});
		labelTextarea.setToolTipText(Localization.getString("Mainframe.ConceptMap.NodeLabel.Tooptip"));

		layoutComponents();
	}

	void setLabelEditable(boolean editable, final boolean selected) {

		if (!getModel().getConstraints().getCanEditLabel()) editable = false;

		labelScroller.setOpaque(editable);
		labelScroller.getViewport().setOpaque(editable);
		labelScroller.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));

		labelTextarea.setFocusable(editable);
		labelTextarea.setEditable(editable);
		labelTextarea.setOpaque(editable);

		labelTextarea.setForeground(editable ? Color.BLACK : getModel().getStyle().getForeground());
		labelTextarea.setBackground(editable ? Color.WHITE : getModel().getStyle().getBackground());

		//labelTextarea.setCaretPosition(caretPos);
		if (editable) {
			SwingUtilities.invokeLater(
					new Thread() {
						@Override
						public void run() {
							labelTextarea.requestFocus();
							if (selected) labelTextarea.selectAll();
						}
					});
		}
		if (!editable) labelTextarea.select(0, 0);

		isEditing = editable;
	}

	void setLabelEditable(boolean editable) {
		setLabelEditable(editable, false);
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
				resizeHandle = button;
			}
		}
		resizeHandle.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		resizeHandle.setVisible(false);
		return resizeHandle;
	}


	void layoutComponents() {

		FontMetrics f = labelTextarea.getFontMetrics(labelTextarea.getFont());
		int width = f.stringWidth(labelTextarea.getText()) + 10;

		int maxHeight = getHeight() - 20; // 10 px spacing on each side
		int maxWidth = getWidth() - 20; // 10 px spacing on each side

		if (width < getModel().getStyle().getMinWidth()) width = getModel().getStyle().getMinWidth();
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

		labelScroller.setBounds((int) x, (int) y, width - 2, height - 2);
		labelScroller.revalidate();

		if (resizeHandle != null) {
			resizeHandle.setBounds(getWidth() - resizeHandle.getWidth() - 2, getHeight() - resizeHandle.getHeight() - 2, resizeHandle.getWidth(), resizeHandle.getHeight());

			resizeHandle.setForeground(getForeground());
			resizeHandle.setBackground(getBackground());
		}
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
		labelTextarea.setText(node.getLabel());
		layoutComponents();
	}

	@Override
	public void shapeChanged(INodeModel node) {
		repaint();
	}

	@Override
	public void styleChanged(INodeModel node) {
		labelTextarea.setForeground(node.getStyle().getForeground());
		labelTextarea.setBackground(node.getStyle().getBackground());
		labelScroller.setOpaque(false);
		labelScroller.getViewport().setOpaque(false);
		repaint();
	}

	@Override
	public void selectionChanged(INodeModel conceptNode) {
		if (selectionBorder == null) {
			selectionBorder = new SelectionBorder();
		}
		setBorder(conceptNode.isSelected() ? selectionBorder : BorderFactory.createEmptyBorder());
		if (conceptNode.isSelected()) requestFocus();
	}

	@Override
	public void deleted(INodeModel nodeModel) {
		this.setVisible(false);
	}

	@Override
	public String toString() {
		return "RichNodeView{" +
				"model=" + getModel() +
				'}';
	}

	@Override
	public void styleChanged(INodeStyle s) {
		labelTextarea.setForeground(s.getForeground());
		labelTextarea.setBackground(s.getBackground());
		labelScroller.setOpaque(false);
		labelScroller.getViewport().setOpaque(false);
		repaint();
	}
}

