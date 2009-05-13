package colab.vt.whiteboard.utils;

import java.awt.Point;

public class ShiftLimitations
{
	private static final double shiftRotationStep = Math.PI / 4;

	private ShiftLimitations()
	{
	}

	public static double getShiftRotation(double angle)
	{
		return shiftRotationStep * Math.round((angle) / shiftRotationStep);
	}

	public static Point getShiftLocation(int xBegin, int yBegin, int x, int y)
	{
		int deltaX = x - xBegin;
		int deltaY = y - yBegin;
		if (Math.abs(deltaX) < Math.abs(deltaY) / 2)
		{
			deltaX = 0;
		}
		else if (Math.abs(deltaY) < Math.abs(deltaX) / 2)
		{
			deltaY = 0;
		}
		else
		{
			int delta = (Math.abs(deltaX) + Math.abs(deltaY)) / 2;
			if (deltaX < 0)
				deltaX = -delta;
			else
				deltaX = delta;
			if (deltaY < 0)
				deltaY = -delta;
			else
				deltaY = delta;
		}
		int useX = xBegin + deltaX;
		int useY = yBegin + deltaY;
		return new Point(useX, useY);
	}

	public static Point getShiftLocationCross(int xBegin, int yBegin, int x, int y)
	{
		int deltaX = x - xBegin;
		int deltaY = y - yBegin;
		int delta = (Math.abs(deltaX) + Math.abs(deltaY)) / 2;
		if (deltaX < 0)
			deltaX = -delta;
		else
			deltaX = delta;
		if (deltaY < 0)
			deltaY = -delta;
		else
			deltaY = delta;
		int useX = xBegin + deltaX;
		int useY = yBegin + deltaY;
		return new Point(useX, useY);
	}

}
