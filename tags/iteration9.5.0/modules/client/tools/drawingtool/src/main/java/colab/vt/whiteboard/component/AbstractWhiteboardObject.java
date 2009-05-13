package colab.vt.whiteboard.component;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public abstract class AbstractWhiteboardObject implements WhiteboardObject
{
	private static final long serialVersionUID = 3203530313632175715L;

	@Override
	public String getToolTipText(MouseEvent event, Point mouseLocation)
	{
		return null;
	}

	@Override
	public boolean objectUnderMouse(int x, int y, double withPenSize)
	{
		Rectangle bounds = getBounds();
		double penSizeIncrease = withPenSize / 2;
		Rectangle2D boundsWithPenSize = new Rectangle2D.Double(bounds.getMinX() - penSizeIncrease,
					bounds.getMinY() - penSizeIncrease, bounds.getWidth() + 2 * penSizeIncrease, bounds
								.getHeight()
								+ 2 * penSizeIncrease);
		return boundsWithPenSize.contains(x, y);
	}

}
