/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import java.util.List;

/**
 *
 * @author sikkenj
 */
public interface MetadataDisplayMapping
{
	public MappingTypes getMappingType();
	
	public List<Mapping> getMappings();
}
