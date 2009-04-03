package eu.scy.colemo.client;

import eu.scy.colemo.server.uml.AbstractDiagramData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.apr.2009
 * Time: 12:19:28
 * To change this template use File | Settings | File Templates.
 */
public class LabelArrow extends JComponent implements FocusListener {

	public static final int STYLE_SOLID    = 0;
	public static final int STYLE_DOTTED   = 1;
	public static final int STYLE_DASHED   = 2;
	private int lineStyle = STYLE_SOLID;

	private String default_label = "";
	private Point from = new Point(0, 0);
	private Point to = new Point(100, 100);
	private boolean bidirectional = false;
	private JTextField textField = new JTextField(default_label);
	private Color color;

    private Object model;

	public LabelArrow() {
		setOpaque(false);
		setBackground(Color.cyan);
		setLayout(null);
		setFocusable(true);
		addFocusListener(this);
		TextFieldListener textFieldListener = new TextFieldListener(this);
		textField.addFocusListener(textFieldListener);
		textField.addKeyListener(textFieldListener);
		textField.setSize(textField.getPreferredSize());
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setOpaque(false);
		textField.setBorder(null);
		add(textField);
	}

	public double getDistanceFromLine(Point pt) {

		double dx = pt.x - from.x;
		double dy = pt.y - from.y;

		if (dx == 0)
			return dx;

		double fromtoAngle = Math.toRadians(getAngle());
		double pointAngle = Math.atan(dy / dx);
		return Math.abs(Math.sqrt(dx * dx + dy * dy) * Math.sin(fromtoAngle - pointAngle));
	}

	public Shape getArrow() {
		GeneralPath path = new GeneralPath();

		float diag = ((float) Math.sqrt(Math.pow(getInnerHeight(), 2) + Math.pow(getInnerWidth(), 2)));

		float[] start = new float[]{diag / 2f, 0};
		float[] end = new float[]{diag / -2f, 0};

		path.moveTo(start[0], start[1]);
		path.lineTo(end[0], end[1]);

		// Draw the arrow heads
		int x, y;

		// degrees between head and shaft
		float deg = 45;

		int size = 10;

		// Create the first arrow head line.
		x = (int) end[0] + size;
		y = (int) (size * Math.sin(deg));
		path.lineTo(x, y);

		// Move to line-end
		path.moveTo(end[0], end[1]);

		// and create the second
		y = (int) (size * Math.sin(-deg));
		path.lineTo(x, y);

		if (bidirectional) {
			// Move to start
			path.moveTo(start[0], start[1]);
			x = (int) start[0] - size;
			y = (int) (size * Math.sin(deg));
			path.lineTo(x, y);
			path.moveTo(start[0], start[1]);
			y = (int) (size * Math.sin(-deg));
			path.lineTo(x, y);
		}
		AffineTransform at = AffineTransform.getTranslateInstance(getWidth() / 2, getHeight() / 2);
		at.rotate(Math.toRadians(getAngle()));
		return at.createTransformedShape(path);
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public void setFrom(Point p) {
		from = p;
		fixBounds();
	}

	public void setTo(Point p) {
		to = p;
		fixBounds();
	}

	public boolean isBidirectional() {
		return bidirectional;
	}

	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}

	private int getInnerWidth() {
		return Math.abs(from.x - to.x);
	}

	private int getInnerHeight() {
		return Math.abs(from.y - to.y);
	}

	private void fixBounds() {
		Dimension labelSize = textField.getSize();
		int w, x, h, y;
		if (to.x < from.x) {
			x = to.x;
			w = from.x - to.x;
		} else {
			x = from.x;
			w = to.x - from.x;
		}
		if (to.y < from.y) {
			y = to.y;
			h = from.y - to.y;
		} else {
			y = from.y;
			h = to.y - from.y;
		}
		setBounds(x - (labelSize.width / 2), y - 10, w + labelSize.width, h + 20);
		textField.setBounds(getWidth() / 2 - (labelSize.width / 2), getHeight() / 2 - (labelSize.height / 2), labelSize.width, labelSize.height);
	}

	public double getAngle() {
		double dx = from.getX() - to.getX();
		double dy = from.getY() - to.getY();
		double angle;

		if (dx == 0.0) {
			if (dy == 0.0) angle = 0.0;
			else if (dy > 0.0) angle = Math.PI / 2.0;
			else angle = (Math.PI * 3.0) / 2.0;
		} else if (dy == 0.0) {
			if (dx > 0.0) angle = 0.0;
			else angle = Math.PI;
		} else {
			if (dx < 0.0) angle = Math.atan(dy / dx) + Math.PI;
			else if (dy < 0.0) angle = Math.atan(dy / dx) + (2 * Math.PI);
			else angle = Math.atan(dy / dx);
		}
		return (angle * 180) / Math.PI;
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		switch (lineStyle) {
			case STYLE_SOLID:
				break;
			case STYLE_DOTTED:
				g2.setStroke(new BasicStroke(
						1f,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						1f,
						new float[]{2f},
						0f));
				break;
			case STYLE_DASHED:
				g2.setStroke(new BasicStroke(
						1f,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						1f,
						new float[]{6f},
						0f));
				break;
		}

		Shape arrow = getArrow();
		g2.draw(arrow);
		super.paint(g2);
	}

	public int findDirection(Point from, Point to) {
		double x = to.x - from.x;
		double y = to.y - from.y;

		if (Math.abs(x) > Math.abs(y)) {
			if (from.getX() < to.getX()) {
				if (Math.abs(from.getY() - to.getY()) < 100) {
					return ConceptNode.EAST;
				} else if (from.getY() > to.getY()) {
					return ConceptNode.NORTHEAST;
				} else
					return ConceptNode.SOUTHEAST;
			} else {
				if (Math.abs(from.getY() - to.getY()) < 100) {
					return ConceptNode.WEST;
				} else if (from.getY() > to.getY()) {
					return ConceptNode.NORTHWEST;
				} else
					return ConceptNode.SOUTHWEST;
			}
		} else {
			if (from.getY() > to.getY()) {
				return ConceptNode.NORTH;
			} else {
				return ConceptNode.SOUTH;
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void focusGained(FocusEvent e) {
		setColor(Color.blue);
		repaint();
	}

	public void focusLost(FocusEvent e) {
		setColor(Color.black);
		repaint();
	}

	public void setLineStyle(int i) {
		lineStyle = i;
	}

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    private static class TextFieldListener implements FocusListener, KeyListener {
		private LabelArrow arrow;

		TextFieldListener(LabelArrow arrow) {
			this.arrow = arrow;
		}

		public void focusGained(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (textField.getText().equals(arrow.default_label)) textField.setText("");
			textField.setOpaque(true);
			textField.setBorder(BorderFactory.createEtchedBorder());

			Dimension prefsize = textField.getPreferredSize();
			textField.setSize(prefsize.width + 20, prefsize.height);
			arrow.fixBounds();
		}

		public void focusLost(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (textField.getText().equals("")) textField.setText(arrow.default_label);
			textField.setOpaque(false);
			textField.setBorder(null);
		}

		public void keyTyped(KeyEvent e) {
			JTextField textField = (JTextField) e.getSource();
			Dimension prefsize = textField.getPreferredSize();
			textField.setSize(prefsize.width + 20, prefsize.height);
			arrow.fixBounds();
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) arrow.requestFocus();
		}

		public void keyReleased(KeyEvent e) {

		}
	}
}
