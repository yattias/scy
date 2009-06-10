/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sikken
 */
public class DisplayMappingList
{
	private static final Logger logger = Logger.getLogger(DisplayMappingList.class);
	private final List<BasicDisplayMapping> displayMappings = new ArrayList<BasicDisplayMapping>();
	private float minimum = Float.MAX_VALUE;
	private float maximum = Float.MIN_VALUE;

	public void addBasicDisplayMapping(BasicDisplayMapping basicDisplayMapping)
	{
		if (basicDisplayMapping.getValue() != null)
		{
			displayMappings.add(basicDisplayMapping);
			if (basicDisplayMapping.getValue() < minimum)
			{
				minimum = basicDisplayMapping.getValue();
			}
			else if (basicDisplayMapping.getValue() > maximum)
			{
				maximum = basicDisplayMapping.getValue();
			}
		}
	}

	public void autoRange()
	{
		range(minimum, maximum);
	}

	public void range(float rangeMinimum, float rangeMaximum)
	{
//		logger.debug("range from " + rangeMinimum + " - " + maximum);
		for (BasicDisplayMapping basicDisplayMapping : displayMappings)
		{
			float rangedValue = (basicDisplayMapping.getValue() - minimum) / (maximum - minimum);
			rangedValue = Math.min(1.0f, Math.max(0.0f, rangedValue));
//			logger.debug("original value:" + basicDisplayMapping.getValue() + ", rangedValue:" + rangedValue);
			basicDisplayMapping.setValue(rangedValue);
		}
	}
}
