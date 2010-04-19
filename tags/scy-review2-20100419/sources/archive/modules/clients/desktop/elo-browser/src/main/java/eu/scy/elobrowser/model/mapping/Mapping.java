/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

import roolo.elo.api.IMetadataKey;


/**
 *
 * @author sikkenj
 */
public interface Mapping
{

	public DisplayProperty getDisplayPropperty();

	public IMetadataKey getMetadataKey();
	
	public boolean isAutoRanging();
	
	public float getMinimum();
	
	public float getMaximum();
}
