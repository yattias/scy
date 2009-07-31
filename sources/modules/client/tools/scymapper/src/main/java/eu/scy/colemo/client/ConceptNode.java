/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import eu.scy.colemo.client.figures.Arrow;
import eu.scy.colemo.server.uml.UmlClass;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Oystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ConceptNode extends JComponent implements FocusListener, MouseListener, MouseMotionListener {
	private static final Logger log = Logger.getLogger(ConceptNode.class.getName());

	public static final int CONNECTION_AREA_NONE = -1;
	public static final int CONNECTION_EDGE_EAST = 0;
	public static final int CONNECTION_EDGE_WEST = 1;
	public static final int CONNECTION_EDGE_NORTH = 2;
	public static final int CONNECTION_EDGE_SOUTH = 3;
	private int activeConnectionPoint = CONNECTION_AREA_NONE;
	private HashMap<Integer, Rectangle> connectionAreas;

	// directions - must be able to get the opposite direction by multiplying with -1
	public static final int NORTH = 2;
	public static final int WEST = 1;
	public static final int EAST = -1;
	public static final int SOUTH = -2;
	public static final int NORTHWEST = 3;
	public static final int NORTHEAST = 4;
	public static final int SOUTHWEST = -4;
	public static final int SOUTHEAST = -3;

	private JTextField nameField;

	private boolean isSelected = false;

	private HashSet<ConceptLink> inboundLinks = new HashSet<ConceptLink>();
	private HashSet<ConceptLink> outboundLinks = new HashSet<ConceptLink>();
	private UmlClass model;
	public final static Color defaultFillColor = Color.white;
	private Color fillColor;

	public ConceptNode(UmlClass umlClass) {

		model = umlClass;

		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);

		nameField = new JTextField(model.getName());
		NameFieldListener l = new NameFieldListener(this);
		nameField.addFocusListener(l);
		nameField.addKeyListener(l);
		nameField.addMouseListener(l);
		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.setVisible(false);
		add(nameField);

		setFocusable(true);
		addFocusListener(this);

		setFillColor(defaultFillColor);
		setConnectionAreas();
	}

	public void update() {
		nameField.setText(getModel().getName());
        repaint();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);

		for (ConceptLink link : outboundLinks) {
			link.updatePosition();
		}
		for (ConceptLink link : inboundLinks) {
			link.updatePosition();
		}
		setConnectionAreas();
	}

	protected void paintComponent(Graphics g) {
		Rectangle bounds = getBounds();
		int arc = 30;

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw shadow
		g2.setStroke(new BasicStroke(3f));
		g2.setColor(new Color(200, 200, 200, 200));
		g2.fillRoundRect(0, 0, bounds.width, bounds.height, arc, arc);

		// Draw the background
		Color c;
		if (isSelected()) c = new Color(255, 102, 0, 200);
		else c = getFillColor();
		g2.setColor(c);
		g2.fillRoundRect(0, 0, bounds.width - 2, bounds.height - 2, arc, arc);
		// Draw the current active connection area
		if (getActiveConnectionArea() != CONNECTION_AREA_NONE) {
			g2.setColor(new Color(200, 200, 200, 200));
			g2.fill(connectionAreas.get(getActiveConnectionArea()));
		}

		//if (getComponents())
		// Draw the label

		if (!nameField.isVisible()) {
			g2.setColor(new Color(0, 0, 0));
			FontMetrics metrics = g2.getFontMetrics();
			Rectangle rect = metrics.getStringBounds(nameField.getText(), g2).getBounds();
			int x = (bounds.width / 2 - rect.width / 2);
			if (x < 10) x = 10;
			g2.drawString(nameField.getText(), x, (bounds.height / 2 + rect.height / 2));
		}
		g2.dispose();
	}

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public Point getCenterPoint() {
		return (new Point(this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2)));
	}

	public HashSet<ConceptLink> getOutboundLinks() {
		return outboundLinks;
	}

	public void setOutboundLinks(HashSet<ConceptLink> outboundLinks) {
		this.outboundLinks = outboundLinks;
	}

	public HashSet<ConceptLink> getInboundLinks() {
		return inboundLinks;
	}

	public void setInboundLinks(HashSet<ConceptLink> inboundLinks) {
		this.inboundLinks = inboundLinks;
	}

	public void addInboundLink(ConceptLink link) {
		link.setToNode(this);
		inboundLinks.add(link);
	}

	public void addOutboundLink(ConceptLink link) {
		link.setFromNode(this);
		outboundLinks.add(link);
	}

	public void focusLost(FocusEvent e) {
		setSelected(false);
		if (e.getComponent().equals(nameField)) {
			log.info("Lost focus on field: " + nameField.getText());
		}

		log.info("FOCUS LOST!");
		repaint();
	}

	public int getActiveConnectionArea() {
		return activeConnectionPoint;
	}

	public void setActiveConnectionPoint(int activeConnectionPoint) {
		this.activeConnectionPoint = activeConnectionPoint;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		SelectionController.getDefaultInstance().setSelected(getModel());

	}

	public void mouseReleased(MouseEvent ae) {

	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		requestFocus();
		if (e.getClickCount() > 1) {
			nameField.setBounds(10, (getHeight() / 2) - 10, getWidth() - 20, 20);
			nameField.setVisible(true);
			nameField.requestFocus();
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {
		setActiveConnectionPoint(CONNECTION_AREA_NONE);
	}

	public Point getLinkConnectionPoint(int direction) {
		Point center = this.getCenterPoint();
		Rectangle bounds = getBounds();
		switch (direction) {
			case Arrow.WEST:
				return new Point(bounds.x - 2, center.y);
			case Arrow.EAST:
				return new Point(bounds.x + bounds.width + 2, center.y);
			case Arrow.NORTH:
				return new Point(center.x, bounds.y - 2);
			case Arrow.SOUTH:
				return new Point(center.x, bounds.y + bounds.height + 2);
			case Arrow.NORTHEAST:
				return new Point(bounds.x + bounds.width - 2, bounds.y);
			case Arrow.NORTHWEST:
				return new Point(bounds.x, bounds.y);
			case Arrow.SOUTHEAST:
				return new Point(bounds.x + bounds.width - 2, bounds.y + bounds.height - 2);
			case Arrow.SOUTHWEST:
				return new Point(bounds.x, bounds.y + bounds.height);

		}
		return null;
	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		if (inConnectionArea(e.getPoint())) {
			if (inConnectionPoint(p, CONNECTION_EDGE_SOUTH)) {
				setActiveConnectionPoint(CONNECTION_EDGE_SOUTH);
			} else if (inConnectionPoint(p, CONNECTION_EDGE_NORTH)) {
				setActiveConnectionPoint(CONNECTION_EDGE_NORTH);
			} else if (inConnectionPoint(p, CONNECTION_EDGE_WEST)) {
				setActiveConnectionPoint(CONNECTION_EDGE_WEST);
			} else if (inConnectionPoint(p, CONNECTION_EDGE_EAST)) {
				setActiveConnectionPoint(CONNECTION_EDGE_EAST);
			}
		} else setActiveConnectionPoint(CONNECTION_AREA_NONE);
	}

	private void setConnectionAreas() {
		if (connectionAreas == null) connectionAreas = new HashMap<Integer, Rectangle>();
		Rectangle bounds = getBounds();
		connectionAreas.put(CONNECTION_EDGE_NORTH, new Rectangle(15, 0, bounds.width - 30, 8));
		connectionAreas.put(CONNECTION_EDGE_EAST, new Rectangle(bounds.width - 8, 15, 8, bounds.height - 30));
		connectionAreas.put(CONNECTION_EDGE_SOUTH, new Rectangle(15, bounds.height - 8, bounds.width - 30, 8));
		connectionAreas.put(CONNECTION_EDGE_WEST, new Rectangle(0, 15, 8, bounds.height - 30));
	}

	public Boolean inConnectionArea(Point p) {
		return inConnectionPoint(p, CONNECTION_EDGE_SOUTH) ||
				inConnectionPoint(p, CONNECTION_EDGE_NORTH) ||
				inConnectionPoint(p, CONNECTION_EDGE_WEST) ||
				inConnectionPoint(p, CONNECTION_EDGE_EAST);
	}

	public Boolean inConnectionPoint(Point p, int connectPoint) {
		return connectionAreas.get(connectPoint).contains(p);
	}

	public int getConnectionEdge(Point p) {
		return connectionAreas.get(CONNECTION_EDGE_SOUTH).contains(p) ? CONNECTION_EDGE_SOUTH :
				connectionAreas.get(CONNECTION_EDGE_NORTH).contains(p) ? CONNECTION_EDGE_NORTH :
						connectionAreas.get(CONNECTION_EDGE_EAST).contains(p) ? CONNECTION_EDGE_EAST :
								connectionAreas.get(CONNECTION_EDGE_WEST).contains(p) ? CONNECTION_EDGE_WEST : -1;
	}

	public void focusGained(FocusEvent e) {
		setSelected(true);
		repaint();
	}

	public void setModel(UmlClass umlClass) {
		model = umlClass;
		update();
	}

	public UmlClass getModel() {
		return model;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	private static final class NameFieldListener implements FocusListener, KeyListener, MouseListener {
		private ConceptNode node;

		NameFieldListener(ConceptNode n) {
			node = n;
		}


		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			node.getModel().setName(textField.getText());
			ApplicationController.getDefaultInstance().getConnectionHandler().updateObject(node.getModel());
			textField.setVisible(false);
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (e.getKeyCode() == 10) {
				node.requestFocus();
				node.getModel().setName(textField.getText());
			}
		}

		public void keyReleased(KeyEvent e) {

		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		public void mouseReleased(MouseEvent e) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		public void mouseEntered(MouseEvent e) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		public void mouseExited(MouseEvent e) {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}
}