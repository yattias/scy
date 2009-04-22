/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayMapping;
import eu.scy.elobrowser.model.mapping.DisplayProperty;

/**
 *
 * @author sikken
 */
public class BasicDisplayMapping implements DisplayMapping
{

	private final DisplayProperty displayProperty;
	private Float value;

	public BasicDisplayMapping(DisplayProperty displayProperty, Float value)
	{
		this.displayProperty = displayProperty;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return displayProperty.toString() + ":" + value;
	}

	@Override
	public DisplayProperty getDisplayProperty()
	{
		return displayProperty;
	}

	@Override
	public Float getValue()
	{
		return value;
	}

	public void setValue(Float value)
	{
		this.value = value;
	}
}
