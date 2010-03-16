package com.ext.portlet.similarity.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.tags.model.TagsAsset;

/**
 * Calculate sim result with the highest simValue for a list of tagsAssets. It
 * user the defaultSigleMeasure for each listEntry and chose only the tagsAsset
 * entry with the highest result. The result is measure by the cosinus measure.
 * 
 * @author Daniel
 * 
 */
public class DefaultMultiMeasure implements MultiSimilarityMeasure {

	private String[] checklist;

	public DefaultMultiMeasure(String[] checklist) {
		this.checklist = checklist;
	}

	@SuppressWarnings("unchecked")
	public Sim calcMultiSim(TagsAsset simResource, List<TagsAsset> assetList) throws PortalException, SystemException {
		DefaultSigleMeasure defaultSigleMeasure = new DefaultSigleMeasure(checklist);
		Sim simResult = new Sim();

		Hashtable<Long, Double> simValueList = new Hashtable<Long, Double>();
		Hashtable<Long, Date> simDateList = new Hashtable<Long, Date>();
		Hashtable<Long, Integer> simViewList = new Hashtable<Long, Integer>();

		Sim multiMeasureResult = defaultSigleMeasure.calcSim(simResource, assetList);
		ArrayList multiValueList = new ArrayList(multiMeasureResult.getSimValueList().entrySet());

		Double maxValue = 0.0;
		Long maxKey = 0l;

		for (int resultIndex = 0; resultIndex < multiValueList.size(); resultIndex++) {
			Map.Entry e = (Map.Entry) multiValueList.get(resultIndex);
			Long key;
			Double value;
			key = (Long) e.getKey();
			value = ((Double) e.getValue());
			if (maxValue < value) {
				maxValue = value;
				maxKey = key;
			}
		}

		if (maxKey != 0) {
			simValueList.put(maxKey, maxValue);
			simDateList.put(maxKey, multiMeasureResult.getSimDateList().get(maxKey));
			simViewList.put(maxKey, multiMeasureResult.getSimViewList().get(maxKey));
		}

		simResult.setSimValueList(simValueList);
		simResult.setSimDateList(simDateList);
		simResult.setSimViewList(simViewList);

		return simResult;

	}
}
