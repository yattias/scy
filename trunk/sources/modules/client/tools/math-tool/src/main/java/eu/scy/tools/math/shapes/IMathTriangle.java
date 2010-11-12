package eu.scy.tools.math.shapes;

import java.awt.Point;

public interface IMathTriangle extends IMathShape {

	public Point getPointR();

	public void setPointR(Point pointR);

	public Point getPointQ();

	public void setPointQ(Point pointQ);

	public Point getPointP();

	public void setPointP(Point pointP);

	public void moveXY(int x, int y);


	public void moveCornerPoint(int position, Point point);

}
