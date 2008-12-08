/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayEloMapping;
import eu.scy.elobrowser.model.mapping.DisplayMapping;
import java.util.ArrayList;
import java.util.List;
import roolo.api.IELO;

/**
 *
 * @author sikken
 */
public class BasicDisplayEloMapping implements DisplayEloMapping {

	private final IELO elo;
	private final List<DisplayMapping> displayMappings;

	public BasicDisplayEloMapping(IELO elo,List<DisplayMapping> displayMappings)
	{
		this.elo = elo;
		this.displayMappings = displayMappings;
	}

	@Override
	public String toString()
	{
		return elo.getUri() + ", displayMappings:" + displayMappings;
	}

	@Override
	public IELO getElo()
	{
		return elo;
	}

	@Override
	public List<DisplayMapping> getDisplayMappings()
	{
		return displayMappings;
	}

}
