package eu.scy.client.tools.drawing;

import java.util.EventObject;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;


public class ELOLoadedChangedEvent<K extends IMetadataKey> extends EventObject
{
	private static final long serialVersionUID = -6015774432537126864L;

	private Object eloDrawingPanel;
	private IELO<K> elo;

	public ELOLoadedChangedEvent(Object eloDrawingPanel, IELO<K> elo)
	{
		super(eloDrawingPanel);
		this.eloDrawingPanel = eloDrawingPanel;
		this.elo = elo;
	}

	public Object getEloDrawingPanel()
	{
		return eloDrawingPanel;
	}

	public IELO<K> getElo()
	{
		return elo;
	}

}
