/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import roolo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class BasicMapping implements Mapping
{

	private DisplayProperty displayPropperty;
	private IMetadataKey metadataKey;
	private boolean autoRanging;
	private double minimum;
	private double maximum;

	public BasicMapping(DisplayProperty displayPropperty, IMetadataKey metadataKey)
	{
		this.displayPropperty = displayPropperty;
		this.metadataKey = metadataKey;
		this.autoRanging = true;
		this.minimum = -1;
		this.maximum = -1;
	}

	public BasicMapping(DisplayProperty displayPropperty, IMetadataKey metadataKey, double minimum, double maximum)
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
	public double getMinimum()
	{
		return minimum;
	}

	@Override
	public double getMaximum()
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

	public void setMaximum(double maximum)
	{
		this.maximum = maximum;
	}

	public void setMetadataKey(IMetadataKey metadataKey)
	{
		this.metadataKey = metadataKey;
	}

	public void setMinimum(double minimum)
	{
		this.minimum = minimum;
	}
}
