package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * @author bjoerge
 * @created 11.jun.2009 11:24:47
 */
public class ConceptLinkView extends LinkView implements INodeModelListener {

	private JTextArea labelTextarea;
	private JScrollPane labelScroller;
	private boolean isEditing;

	public ConceptLinkView(ILinkController controller, INodeLinkModel model) {
		super(controller, model);

		// I want to observe changes in my connected nodes
		if (model.getFromNode() != null) model.getFromNode().addListener(this);
		if (model.getToNode() != null) model.getToNode().addListener(this);

		labelTextarea = new JTextArea(getModel().getLabel());
		//labelTextarea.setHorizontalAlignment(JTextField.CENTER);
		//labelTextarea.setForeground(getModel().getStyle().getColor());
		labelTextarea.setWrapStyleWord(true);
		labelTextarea.setLineWrap(true);
		labelTextarea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		labelScroller = new JScrollPane(labelTextarea);
		labelScroller.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				((JComponent) e.getSource()).repaint();
			}
		});
		labelTextarea.setMargin(new Insets(0, 0, 0, 0));
		labelTextarea.setBorder(BorderFactory.createEmptyBorder());
		labelScroller.getViewport().setOpaque(false);

		add(labelScroller);

		setLabelEditable(true, true);

		MouseListener dblClickListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					setLabelEditable(true);
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
				if (e.isControlDown() && e.getKeyCode() == 10) ConceptLinkView.this.requestFocus();
			}
		});

		updatePosition();
		layoutComponents();
	}

	private void setLabelEditable(boolean editable, final boolean select) {

		labelScroller.setOpaque(editable);
		labelScroller.getViewport().setOpaque(editable);
		labelScroller.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));

		labelTextarea.setFocusable(editable);
		labelTextarea.setEditable(editable);

		if (!editable && labelTextarea.getText().equals("")) {
			labelTextarea.setOpaque(false);
		} else {
			labelTextarea.setOpaque(true);
			labelTextarea.setForeground(editable ? Color.BLACK : getModel().getStyle().getColor());
			labelTextarea.setBackground(editable ? Color.WHITE : new Color(255, 255, 255, 150));
		}
		if (editable) {
			SwingUtilities.invokeLater(
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(100);
								labelTextarea.requestFocus();
								if (select) labelTextarea.selectAll();
							}
							catch (InterruptedException e) {

							}
						}
					});
		}

	}

	void setLabelEditable(boolean editable) {
		setLabelEditable(editable, false);
	}

	private void layoutComponents() {

		FontMetrics f = labelTextarea.getFontMetrics(labelTextarea.getFont());
		int width = f.stringWidth(labelTextarea.getText()) + 10;

		int maxHeight = getHeight() - 140; // 10 px spacing on each side
		int maxWidth = getWidth() - 140; // 10 px spacing on each side

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

		if (width < 100) width = 100;
		if (height <= 25) height = 25;

		labelTextarea.setSize(width, height);
		labelTextarea.setVisible(!getModel().isLabelHidden());

		double x = ((maxWidth / 2) - (width / 2)) + 70;
		double y = ((maxHeight / 2d) - (height / 2d)) + 70;

		labelScroller.setBounds((int) x, (int) y, width, height);
		labelScroller.revalidate();
	}

	@Override
	public void updated(ILinkModel m) {
		if (!m.getLabel().equals(labelTextarea.getText())) {
			labelTextarea.setText(m.getLabel());
		}
		super.updated(m);
		layoutComponents();
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
	public void labelChanged(INodeModel node) {
	}

	@Override
	public void shapeChanged(INodeModel node) {
	}

	@Override
	public void selectionChanged(INodeModel conceptNode) {
	}

	@Override
	public void deleted(INodeModel nodeModel) {

	}
}
