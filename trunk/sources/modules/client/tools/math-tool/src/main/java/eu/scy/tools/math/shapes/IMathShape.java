package eu.scy.tools.math.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public interface IMathShape {

	public void repaint();

	public void addX(int x);
	public void addY(int y);
	
	public String getSelectedItemName();

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
	
	public void setFormula(String formula);

	public String getFormula();
	
	public String getResult();
	
	public void setResult(String result);
	
	public void setHasDecorations(boolean hasDecorations);

	public void setSelectedItem(String item);
}
