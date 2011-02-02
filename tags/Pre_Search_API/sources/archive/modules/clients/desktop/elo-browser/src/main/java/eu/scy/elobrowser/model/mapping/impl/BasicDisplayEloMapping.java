/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayEloMapping;
import eu.scy.elobrowser.model.mapping.DisplayMapping;
import java.util.List;
import roolo.elo.api.IELO;

/**
 *
 * @author sikken
 */
public class BasicDisplayEloMapping implements DisplayEloMapping {

	private final IELO elo;
	private final List<DisplayMapping> displayMappings;
	private final String eloType;
   private final boolean dragable;

	public BasicDisplayEloMapping(IELO elo,List<DisplayMapping> displayMappings,String eloType,boolean dragable)
	{
		this.elo = elo;
		this.displayMappings = displayMappings;
		this.eloType = eloType;
		this.dragable = dragable;
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

	@Override
	public String getEloType()
	{
		return eloType;
	}

   @Override
   public boolean isDragable()
   {
      return dragable;
   }


}
