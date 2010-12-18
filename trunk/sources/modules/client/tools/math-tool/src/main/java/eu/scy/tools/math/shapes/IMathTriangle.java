package eu.scy.tools.math.shapes;

import java.awt.Point;

public interface IMathTriangle extends IMathShape {

	public static String WIDTH = "w";
	public static String HEIGHT = "h";


	public Point getPointR();

	public void setPointR(Point pointR);

	public Point getPointQ();

	public void setPointQ(Point pointQ);

	public Point getPointP();

	public void setPointP(Point pointP);

	public void moveXY(int x, int y);

	public void moveCornerPoint(int position, Point point);
	
	public double getHeight();
	
	public double getWidth();
	
	public double getScaledHeight();
	
	public double getScaledWidth();

}
