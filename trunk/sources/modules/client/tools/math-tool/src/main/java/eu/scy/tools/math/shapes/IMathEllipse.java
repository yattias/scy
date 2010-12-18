package eu.scy.tools.math.shapes;

import java.awt.geom.Ellipse2D;

public interface IMathEllipse extends IMathShape {

	public static String RADIUS = "r";
	
	public Ellipse2D.Double getEllipse();
	
	public void setEllipse(Ellipse2D.Double ellipse);
	
	public void addWidth(int w);
	
	public void addHeight(int h);

	public double getRadius();
	
	public double getScaledRadius();

	public void setRadius(double radius);
}
