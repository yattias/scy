/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import roolo.elo.api.IELO;


/**
 *
 * @author sikkenj
 */
public interface MappingElo extends Cloneable
{
	public IELO getElo();

	public MappingElo clone();
	
	public void setName(String name);

	public String getName();

	public void setDescription(String description);

	public String getDescription();

	public void setMetadataDisplayMapping(MetadataDisplayMapping metadataDisplayMapping);

	public MetadataDisplayMapping getMetadataDisplayMapping();
}
