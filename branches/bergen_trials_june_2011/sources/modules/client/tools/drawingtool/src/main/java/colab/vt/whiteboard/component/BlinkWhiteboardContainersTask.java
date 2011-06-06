package colab.vt.whiteboard.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

public class BlinkWhiteboardContainersTask extends TimerTask
{
	private static final long DEFAULT_DELAY = 500;
	private static final Color transparent = new Color(0, 0, 0, 0);
	private transient boolean stopped = false;
	private Timer blinkTimer = null;
//	private List<WhiteboardContainer> whiteboardContainers;
	boolean onStatus = false;
	boolean initialStatus = false;
	private List<WhiteboardContainerState> whiteboardContainerStates;

	private class WhiteboardContainerState
	{
		WhiteboardContainer whiteboardContainer;
		Color lineColor;
		Color fillColor;
		double penSize;
		boolean selected;

		WhiteboardContainerState(WhiteboardContainer whiteboardContainer)
		{
			this.whiteboardContainer = whiteboardContainer;
			lineColor = whiteboardContainer.getLineColor();
			fillColor = whiteboardContainer.getFillColor();
			penSize = whiteboardContainer.getPenSize();
			selected = whiteboardContainer.isSelected();
		}

		void setBlinkState()
		{
			whiteboardContainer.setLineColor(transparent);
			whiteboardContainer.setFillColor(transparent);
			whiteboardContainer.setSelected(false);
		}

		void resetState()
		{
			whiteboardContainer.setLineColor(lineColor);
			whiteboardContainer.setFillColor(fillColor);
			whiteboardContainer.setPenSize(penSize);
			whiteboardContainer.setSelected(selected);
		}
	}

	private class SwitchBlink implements Runnable
	{
		private boolean newState;

		public SwitchBlink(boolean newState)
		{
			super();
			this.newState = newState;
		}

		@Override
		public void run()
		{
			for (WhiteboardContainerState whiteboardContainerState : whiteboardContainerStates)
			{
				whiteboardContainerState.whiteboardContainer.repaint();
				if (newState)
					whiteboardContainerState.setBlinkState();
				else
					whiteboardContainerState.resetState();
				whiteboardContainerState.whiteboardContainer.repaint();
			}
		}
	}

	public BlinkWhiteboardContainersTask(List<WhiteboardContainer> whiteboardContainers)
	{
//		this.whiteboardContainers = whiteboardContainers;
		whiteboardContainerStates = new ArrayList<WhiteboardContainerState>();
		for (WhiteboardContainer whiteboardContainer : whiteboardContainers)
			whiteboardContainerStates.add(new WhiteboardContainerState(whiteboardContainer));
		initialStatus = whiteboardContainers.get(0).isSelected();
		onStatus = initialStatus;
		blinkTimer = new Timer();
		blinkTimer.schedule(this, 0, DEFAULT_DELAY);
	}

	@Override
	public void run()
	{
		if (!stopped)
		{
			onStatus = !onStatus;
			SwingUtilities.invokeLater(new SwitchBlink(onStatus));
			try
			{
				Thread.sleep(DEFAULT_DELAY);
			}
			catch (InterruptedException e)
			{
				// no need to do any thing;
			}
		}
	}

	public void stop()
	{
		stopped = true;
		cancel();
		for (WhiteboardContainerState whiteboardContainerState : whiteboardContainerStates)
		{
			whiteboardContainerState.whiteboardContainer.repaint();
			whiteboardContainerState.resetState();
			whiteboardContainerState.whiteboardContainer.repaint();
		}
	}
}
