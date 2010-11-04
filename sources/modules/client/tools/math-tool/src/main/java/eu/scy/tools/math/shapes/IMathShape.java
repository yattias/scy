package eu.scy.tools.math.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;
import java.util.Vector;

public interface IMathShape {

	public void repaint();

	public void addX(int dx);
	public void addY(int y);
	

	public void setId(String id);
	
	public String getId();
	
	public void addMouseListeners(MouseAdapter mouseAdapter);

	public boolean isShowCornerPoints();

	public void setShowCornerPoints(boolean showCornerPoints);

	public Point[] getPoints();

	public void setPoints(Point[] points);

	public int isHitOnEndPoints(Point eventPoint);

	public boolean contains(Point point);

	public void setCornerPointRectangle(Rectangle cornerPointRectangle);

	public Rectangle getCornerPointRectangle();

	public void paintComponent(Graphics g);

}
