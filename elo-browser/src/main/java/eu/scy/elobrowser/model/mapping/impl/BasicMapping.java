/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class BasicMapping implements Mapping
{

	private DisplayProperty displayPropperty;
	private IMetadataKey metadataKey;
	private boolean autoRanging;
	private float minimum;
	private float maximum;

	public BasicMapping(DisplayProperty displayPropperty, IMetadataKey metadataKey)
	{
		this.displayPropperty = displayPropperty;
		this.metadataKey = metadataKey;
		this.autoRanging = true;
		this.minimum = -1;
		this.maximum = -1;
	}

	public BasicMapping(DisplayProperty displayPropperty, IMetadataKey metadataKey, float minimum, float maximum)
	{
		this.displayPropperty = displayPropperty;
		this.metadataKey = metadataKey;
		this.autoRanging = false;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public String toString()
	{
		return "displayPropperty:" + displayPropperty + ", metadataKey:" + metadataKey.getId()
			+ ",autoRanging:" + autoRanging + ", minimum:" + minimum + ", maximum:" + maximum;
	}

	@Override
	public DisplayProperty getDisplayPropperty()
	{
		return displayPropperty;
	}

	@Override
	public IMetadataKey getMetadataKey()
	{
		return metadataKey;
	}

	@Override
	public boolean isAutoRanging()
	{
		return autoRanging;
	}

	@Override
	public float getMinimum()
	{
		return minimum;
	}

	@Override
	public float getMaximum()
	{
		return maximum;
	}

	public void setAutoRanging(boolean autoRanging)
	{
		this.autoRanging = autoRanging;
	}

	public void setDisplayPropperty(DisplayProperty displayPropperty)
	{
		this.displayPropperty = displayPropperty;
	}

	public void setMaximum(float maximum)
	{
		this.maximum = maximum;
	}

	public void setMetadataKey(IMetadataKey metadataKey)
	{
		this.metadataKey = metadataKey;
	}

	public void setMinimum(float minimum)
	{
		this.minimum = minimum;
	}
}
