package com.ext.portlet.similarity.action;

import java.util.List;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.tags.model.TagsAsset;

/**
 * Builder class for getSingleSimilarityMeasure and getMultiSimilarityMeasure.
 * The measure type can chose by measure type string which can be set at portlet
 * properties from similirity portlet. If new type should be integrate for calc
 * the result it only must defined here.
 * 
 * The added portlet prefs only reload from portlet-ext file after closing and reopening the porlet !!! 
 * 
 * @see portlet-ext.xml
 * @author Daniel
 * 
 */
public class SimilarityMeasureBuilder {

	public static String CHECKLIST_TAGS = "tags";
	public static String CHECKLIST_USER = "user";
	public static String CHECKLIST_GROUP = "group";
	
	public static String SIMMEASURE_DEFAULT = "sim.cosinus";
	

	private String[] checklist;
	private String measureMethod;

	public SimilarityMeasureBuilder(String[] checklist, String measureMethod) {
		this.checklist = checklist;
		this.measureMethod = measureMethod;
	}

	/**
	 * Calculate a sim result for each entry from assetList.
	 * 
	 * @param simResource
	 *            the start resource.
	 * @param assetList
	 *            list of asset tags.
	 * @param checklist
	 *            user checked terms for similarity
	 * @return sim list for each asset list entry.
	 * @throws SystemException
	 */
	public Sim getSingleSimilarityMeasure(TagsAsset simResource, List<TagsAsset> assetList) throws SystemException {
		Sim calcSim = new Sim();
		if (measureMethod.equals("cosinus")) {
			calcSim = new DefaultSigleMeasure(checklist).calcSim(simResource, assetList);
		} else if (measureMethod.equals("ownMeasure")) {
			// insert own measure method here and to portlet prefs
			calcSim = new DefaultSigleMeasure(checklist).calcSim(simResource, assetList);
		} else {
			calcSim = new DefaultSigleMeasure(checklist).calcSim(simResource, assetList);
		}

		return calcSim;
	}

	/**
	 * Calculate only one sim result for the whole assetList.
	 * 
	 * @param simResource
	 *            the start resource.
	 * @param assetList
	 *            list of asset tags.
	 * @return sim list with one entry.
	 * @throws SystemException
	 */
	public Sim getMultiSimilarityMeasure(TagsAsset simResource, List<TagsAsset> assetList) throws PortalException, SystemException {
		Sim calcSim;
		if (measureMethod == null) {
			calcSim = new DefaultMultiMeasure(checklist).calcMultiSim(simResource, assetList);
		} else {
			calcSim = new DefaultMultiMeasure(checklist).calcMultiSim(simResource, assetList);
		}
		return calcSim;
	}

}
