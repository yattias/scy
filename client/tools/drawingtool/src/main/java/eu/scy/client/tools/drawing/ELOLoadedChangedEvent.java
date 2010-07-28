package eu.scy.client.tools.drawing;

import java.util.EventObject;

import roolo.elo.api.IELO;


public class ELOLoadedChangedEvent extends EventObject
{
	private static final long serialVersionUID = -6015774432537126864L;

	private Object eloDrawingPanel;
	private IELO elo;

	public ELOLoadedChangedEvent(Object eloDrawingPanel, IELO elo)
	{
		super(eloDrawingPanel);
		this.eloDrawingPanel = eloDrawingPanel;
		this.elo = elo;
	}

	public Object getEloDrawingPanel()
	{
		return eloDrawingPanel;
	}

	public IELO getElo()
	{
		return elo;
	}

}
