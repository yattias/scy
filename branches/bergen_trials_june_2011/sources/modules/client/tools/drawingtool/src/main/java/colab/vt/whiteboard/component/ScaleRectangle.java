package colab.vt.whiteboard.component;

import java.awt.geom.Rectangle2D;

public class ScaleRectangle
{
	public enum Direction
	{
		northWest(315, true, true), north(0, false, true), northEast(45, true, true), east(90, true,
					false), southEast(135, true, true), south(180, false, true), southWest(225, true,
					true), west(270, true, false);

		private double angle;
		private boolean scaleOnX = false;
		private boolean scaleOnY = false;

		private Direction(double degrees, boolean scaleOnX, boolean scaleOnY)
		{
			angle = Math.toRadians(degrees);
			this.scaleOnX = scaleOnX;
			this.scaleOnY = scaleOnY;
		}

		public double getAngle()
		{
			return angle;
		}

		public boolean isScaleOnX()
		{
			return scaleOnX;
		}

		public boolean isScaleOnY()
		{
			return scaleOnY;
		}
	}

	private Direction direction = null;
	private Rectangle2D rectangle;

	public ScaleRectangle(Direction direction, Rectangle2D rectangle)
	{
		super();
		this.direction = direction;
		this.rectangle = rectangle;
	}

	public Direction getDirection()
	{
		return direction;
	}

	public double getAngle()
	{
		return direction.getAngle();
	}

	public boolean isScaleOnX()
	{
		return direction.isScaleOnX();
	}

	public boolean isScaleOnY()
	{
		return direction.isScaleOnY();
	}

	public Rectangle2D getRectangle()
	{
		return rectangle;
	}

	@Override
	public String toString()
	{
		return "direction=" + direction + ", rectangle=" + rectangle;
	}

}
