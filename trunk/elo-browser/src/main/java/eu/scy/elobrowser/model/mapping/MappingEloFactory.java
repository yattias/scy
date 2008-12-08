/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import roolo.api.IELO;
import roolo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public interface MappingEloFactory
{

	public MappingElo createMappingElo();

	public MappingElo createMappingElo(IELO<IMetadataKey> elo);
}
