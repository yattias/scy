package eu.scy.tools.math.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jdesktop.swingx.JXButton;

public interface IMathShape {

	public void repaint();

	public void addX(int x);
	public void addY(int y);
	

	public void setId(String id);
	
	public String getId();
	
	public boolean isShowCornerPoints();

	public void setShowCornerPoints(boolean showCornerPoints);

	public Point[] getPoints();

	public void setPoints(Point[] points);

	public int isHitOnEndPoints(Point eventPoint);

	public boolean contains(Point point);

	public void paintComponent(Graphics g);

	public void setFillColor(Color fillColor);

	public Color getFillColor();

	public String getType();

	public void createCornerPoints();

	public void setCornerPointRectangles(Rectangle[] cornerPointRectangles);

	public Rectangle[] getCornerPointRectangles();

	
}
