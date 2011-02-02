package eu.scy.tools.math.shapes;

import java.awt.Rectangle;

public interface IMathRectangle extends IMathShape {

	public static String WIDTH = "w";
	public static String HEIGHT = "h";

	public Rectangle getRectangle();

	public void setRectangle(Rectangle rectangle);

	public double getHeight();
	
	public double getWidth();
	
	public double getScaledHeight();
	
	public double getScaledWidth();
}
