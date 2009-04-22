/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;


/**
 *
 * @author sikkenj
 */
public interface MappingEloFactory
{

	public MappingElo createMappingElo();

	public MappingElo createMappingElo(IELO<IMetadataKey> elo);
}
