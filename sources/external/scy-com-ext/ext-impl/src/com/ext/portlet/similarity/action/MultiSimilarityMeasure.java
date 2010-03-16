package com.ext.portlet.similarity.action;

import java.util.List;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.tags.model.TagsAsset;

/**
 * Interface to calculate one sim result from a list of assetTags.
 * 
 * @author Daniel
 * 
 */
public interface MultiSimilarityMeasure {

	public abstract Sim calcMultiSim(TagsAsset simResource, List<TagsAsset> assetList) throws PortalException, SystemException;

}