package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.shapes.links.QuadCurvedLine;
import eu.scy.scymapper.impl.ui.Localization;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import org.apache.log4j.Logger;

/**
 * @author bjoerge
 * @created 11.jun.2009 11:24:47
 */
public class ConceptLinkView extends LinkView implements INodeModelListener {

	private final static Logger logger = Logger.getLogger(ConceptLinkView.class);

	private JTextPane labelTextarea;
	private JScrollPane labelScroller;
	private boolean isEditing;

	JPanel debugPanel;
	private JSlider slider;
	private ISCYMapperToolConfiguration conf;
	private double labelPos = 0.5;

	public ConceptLinkView(ILinkController controller, INodeLinkModel model) {
		super(controller, model);

		conf = SCYMapperToolConfiguration.getInstance();

		// I want to observe changes in my connected nodes
		if (model.getFromNode() != null) model.getFromNode().addListener(this);
		if (model.getToNode() != null) model.getToNode().addListener(this);

		DefaultStyledDocument doc = new DefaultStyledDocument();
                labelTextarea = new JTextPane(doc);
		try {
		    SimpleAttributeSet set = new SimpleAttributeSet();
		    StyleConstants.setFontSize(set, 14);
	            StyleConstants.setFontFamily(set, "Times New Roman");
		    set.addAttribute(StyleConstants.ParagraphConstants.Alignment, StyleConstants.ParagraphConstants.ALIGN_CENTER);
                    doc.setParagraphAttributes(0, 1, set, true);
                    doc.insertString(0, getModel().getLabel(), set);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
//		labelTextarea.setHorizontalAlignment(JTextField.CENTER);
//		labelTextarea.setForeground(getModel().getStyle().getColor());
//		labelTextarea.setWrapStyleWord(true);
//		labelTextarea.setLineWrap(true);
		labelTextarea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		labelScroller = new JScrollPane(labelTextarea);
		labelScroller.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				((JComponent) e.getSource()).repaint();
			}
		});
		labelTextarea.setMargin(new Insets(0, 0, 0, 0));
		labelTextarea.setMinimumSize(new Dimension(150, 22));
		labelTextarea.setBorder(BorderFactory.createEmptyBorder());
		labelScroller.getViewport().setOpaque(false);
		labelScroller.setOpaque(false);

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
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
					requestFocus();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				layoutComponents();
			}
		});

		labelTextarea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isEditing) {
					labelScroller.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(200, 200, 200, 150)));
					labelTextarea.setBackground(Color.WHITE);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isEditing) {
					labelScroller.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
					labelTextarea.setBackground(new Color(255, 255, 255, 150));
					repaint();
				}
			}
		});
		labelTextarea.setToolTipText(Localization.getString("Mainframe.ConceptMap.LinkLabel.Tooltip"));

		if (conf.isDebug()) {
			addCurveSlider();
		}

		updatePosition();
		layoutComponents();
	}

	private void addCurveSlider() {
		ILinkShape linkShape = getModel().getShape();
		if (!(linkShape instanceof QuadCurvedLine))
			return;

		slider = new JSlider();
		slider.setExtent(1);
		slider.setPaintTicks(true);
		slider.setMaximum(100);
		slider.setMinimum(1);
		slider.setBounds(0, 0, 100, 25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				ILinkShape linkShape = getModel().getShape();
				if (linkShape instanceof QuadCurvedLine) {
					logger.debug("source.getValue() = " + source.getValue());
					double curving = ((double) source.getValue()) / 100d;
					((QuadCurvedLine) linkShape).setCurving(curving);
					logger.debug("curving = " + curving);
					repaint();
				}
			}
		});
		add(slider);
	}

	@Override
	public boolean contains(int x, int y) {
		for (Component component : getComponents()) {
			if (component.getBounds().contains(x, y)) return true;
		}
		return super.contains(x, y);
	}

	private void setLabelEditable(boolean editable, final boolean select) {

		labelScroller.getViewport().setOpaque(editable);
		labelScroller.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(2, 2, 2, 2));

		labelTextarea.setFocusable(editable);
		labelTextarea.setEditable(editable);

		if (!editable && labelTextarea.getText().equals("")) {
			labelTextarea.setOpaque(false);
		} else {
			labelTextarea.setOpaque(true);
			labelTextarea.setForeground(editable ? Color.BLACK : getModel().getStyle().getForeground());
			labelTextarea.setBackground(editable ? Color.WHITE : new Color(255, 255, 255, 150));
		}
		if (editable) {
			SwingUtilities.invokeLater(
					new Runnable() {
						@Override
						public void run() {
							labelTextarea.requestFocus();
							if (select) labelTextarea.selectAll();
						}
					});
		}
		isEditing = editable;
		repaint();
	}

	void setLabelEditable(boolean editable) {
		setLabelEditable(editable, false);
	}

	public Dimension calculateStringBounds(JTextComponent textComponent, int breakWidth) {
		FontMetrics fm = textComponent.getFontMetrics(textComponent.getFont());
		FontRenderContext frc = fm.getFontRenderContext();
		String text = textComponent.getText();
		if (text.equals("")) text = " ";
		String[] lines = StringUtils.splitPreserveAllTokens(text, System.getProperty("line.separator"));
		int numLines = 0;
		double maxWidth = 0.0;
		for (String line : lines) {
			// Ignore blank lines
			if (line.equals("")) line = " ";

			AttributedString str = new AttributedString(line);
			AttributedCharacterIterator lineIt = str.getIterator();
			int paragraphStart = lineIt.getBeginIndex();
			int paragraphEnd = lineIt.getEndIndex();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(lineIt, frc);
			lineMeasurer.setPosition(paragraphStart);
			// Get lines until the entire line has been iterated over.
			while (lineMeasurer.getPosition() < paragraphEnd) {

				TextLayout layout = lineMeasurer.nextLayout(breakWidth);

				double w = layout.getBounds().getWidth();
				if (w > maxWidth) maxWidth = w;
				numLines++;
			}
		}
		int lineHeight = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
		return new Dimension((int) Math.ceil(maxWidth), numLines * lineHeight);
	}

	private void layoutComponents() {

		// Lay out the text area component
		// The size of the text area is negotiated in the following order:
		//
		// 1. First, find out the ideal size of the text box based on its content
		// 2. if the ideal size is bigger than the available size, shrink the component to fit in the container
		// 3. Calculate the ideal location of the text box of this size (using the link shape)
		//		This location should be based on the deCasteljauPoint or half-way point of the link shape
		//		if x + ideal width exeeds available width, move the component the exeeded amount of pixels to the left
		//		if y + ideal height exeeds available height, move the component the exeeded amount of pixels to the top
		// 4. Set the location of the text input component accordingly

		labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		Rectangle innerBounds = new Rectangle(getInnerBounds());
		innerBounds.translate(-getX(), -getY());

		if (innerBounds.width < 150) {
			double diff = innerBounds.width - 150;
			innerBounds.x += diff / 2d;
			innerBounds.width = 150;
		}
		if (innerBounds.height < 50) {
			double diff = innerBounds.height - 50;
			innerBounds.y += diff / 2;
			innerBounds.height = 50;
		}

		if (conf.isDebug()) {
			if (debugPanel == null) {
				debugPanel = new JPanel();
				debugPanel.setOpaque(false);
				debugPanel.setBorder(BorderFactory.createLineBorder(Color.red, 1));
				add(debugPanel);
			}
			debugPanel.setBounds(innerBounds);
		}

		Dimension prefSize = calculateStringBounds(labelTextarea, innerBounds.width);

		Insets insets = labelScroller.getInsets();
		Insets borderInsets = labelScroller.getBorder().getBorderInsets(labelScroller);
		prefSize.width += insets.left + insets.right + borderInsets.left + borderInsets.right;
		prefSize.height += insets.top + insets.bottom + borderInsets.top + borderInsets.bottom;

		if (prefSize.width < 100) prefSize.width = 100;
		else if (prefSize.width > innerBounds.width) prefSize.width = innerBounds.width;

		if (prefSize.height < 22) prefSize.height = 22;

		Point relFrom = new Point(getModel().getFrom());
		relFrom.translate(-getX(), -getY());

		Point relTo = new Point(getModel().getTo());
		relTo.translate(-getX(), -getY());
//
//		// This is the point that should be the x,y of the text box
		Point2D casteljaPt = getModel().getShape().getDeCasteljauPoint(relFrom, relTo, labelPos);
		int x = (int) casteljaPt.getX();
		int y = (int) casteljaPt.getY();

		x -= prefSize.width / 2;
		y -= prefSize.height / 2;
//
		if (x < innerBounds.x) x = innerBounds.x;
		if (y < innerBounds.y) y = innerBounds.y;

		if (prefSize.height > innerBounds.height) {
			labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			prefSize.height = innerBounds.height;
		}

		while (x + prefSize.width > innerBounds.x + innerBounds.width) {
			x--;
		}
		while (y + prefSize.height > innerBounds.y + innerBounds.height) {
			y--;
		}
		labelScroller.setBounds(x, y, prefSize.width, prefSize.height);
		labelScroller.revalidate();
	}

	@Override
	public void labelChanged(ILinkModel link) {

		if (!link.getLabel().equals(labelTextarea.getText())) {
			labelTextarea.setText(link.getLabel());

			SimpleAttributeSet set = new SimpleAttributeSet();
			set.addAttribute(StyleConstants.ParagraphConstants.Alignment, new Integer(StyleConstants.ParagraphConstants.ALIGN_CENTER));

	                    try {
                            labelTextarea.getStyledDocument().remove(0, labelTextarea.getDocument().getLength());
                            labelTextarea.getStyledDocument().insertString(0, link.getLabel(), set);
                            labelTextarea.getStyledDocument().setParagraphAttributes(0, 1, set, true);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }

		}
		if (conf.isDebug()) {
			labelPos = Math.min(Integer.parseInt(labelTextarea.getText()), 100) / 100d;
		}
		labelTextarea.setForeground(link.getStyle().getForeground());

		super.labelChanged(link);
		layoutComponents();
		repaint();
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
	public void styleChanged(INodeModel node) {
	}

	@Override
	public void selectionChanged(INodeModel conceptNode) {
	}

	@Override
	public void deleted(INodeModel nodeModel) {

	}
}
