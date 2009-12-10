/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import java.util.List;
import roolo.elo.api.IELO;

/**
 *
 * @author sikken
 */
public interface DisplayEloMapping
{

	public IELO getElo();

	public List<DisplayMapping> getDisplayMappings();

	public String getEloType();

   public boolean isDragable();
}
