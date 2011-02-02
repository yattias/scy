package com.ext.portlet.similarity.action;

import java.util.List;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.SystemException;
import com.liferay.portlet.tags.model.TagsAsset;

/**
 * Interface to calculate list of sim results from a list of assetTags.
 * 
 * @author Daniel
 * 
 */
public interface SingleSimilarityMeasue {
	Sim calcSim(TagsAsset simResource, List<TagsAsset> assetList) throws SystemException;
}
