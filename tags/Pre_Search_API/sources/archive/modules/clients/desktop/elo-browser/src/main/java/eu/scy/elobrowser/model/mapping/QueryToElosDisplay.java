/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import java.util.List;
import roolo.api.search.IQuery;

/**
 *
 * @author sikken
 */
public interface QueryToElosDisplay
{

	public List<DisplayEloMapping> getDisplayEloMapping(MappingElo mappingElo, IQuery query);
}
