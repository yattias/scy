package com.ext.portlet.similarity.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.model.TagsEntry;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil;

/**
 * Calculate a list of sim. As measure for the result the cosinus measure is
 * used.
 * 
 * @author Daniel
 * 
 */
public class DefaultSigleMeasure implements SingleSimilarityMeasue {

	private String[] checklist;

	public DefaultSigleMeasure(String[] checklist) {
		this.checklist = checklist;
	}

	@Override
	/**
	 * Calculate for each entry a simValue with assetId and result, 
	 * simDate with assetId and modified date from this asset entry and 
	 * a simView with assetId and viewCount for this asset entry.
	 */
	public Sim calcSim(TagsAsset entry, List<TagsAsset> assetList) throws SystemException {
		Sim simResult = new Sim();
		Hashtable<Long, Double> simValueList = new Hashtable<Long, Double>();
		Hashtable<Long, Date> simDateList = new Hashtable<Long, Date>();
		Hashtable<Long, Integer> simViewList = new Hashtable<Long, Integer>();
		try {

			String[] allTags = TagsEntryLocalServiceUtil.getEntryNames();
			List<Group> allGroups = GroupLocalServiceUtil.getGroups(0, GroupLocalServiceUtil.getGroupsCount());

			Vector<Long> startResourceVector = getStartResourceVector(entry, allTags, allGroups, checklist);

			// start calculate sim value
			for (TagsAsset sim : assetList) {
				long nominator = 0;

				Vector<Long> simResourceVector = getSimResourceVector(allTags, allGroups, entry.getUserId(), sim, checklist);

				// fill nominator
				for (int i = 0; i < startResourceVector.size(); i++) {
					if (startResourceVector.get(i) != 0 && simResourceVector.get(i) != 0) {
						nominator += startResourceVector.get(i) * simResourceVector.get(i);
					}

				}
				
				// fill denominator
				long denominator_x = 0;
				long denominator_y = 0;
				
				for (int i = 0; i < startResourceVector.size(); i++) {
					if (startResourceVector.get(i) != 0) {
						denominator_x += Math.pow(startResourceVector.get(i), 2);
					}
				}

				for (int i = 0; i < simResourceVector.size(); i++) {
					if (simResourceVector.get(i) != 0) {
						denominator_y += Math.pow(simResourceVector.get(i), 2);
					}

				}

				// calc result
				double result;
				if (denominator_x == 0 || denominator_y == 0) {
					result = 0;
				} else {
					result = nominator / Math.sqrt(denominator_x * denominator_y);
				}

				result = roundResult(result);

				// add only result with value over 0. The result is normally
				// only null if tags, userId and groups all different from both
				// resource.
				if (result != 0) {
					simValueList.put(sim.getAssetId(), result);
					simDateList.put(sim.getAssetId(), sim.getModifiedDate());
					simViewList.put(sim.getAssetId(), sim.getViewCount());
				}
			}

			simResult.setSimValueList(simValueList);
			simResult.setSimDateList(simDateList);
			simResult.setSimViewList(simViewList);

			return simResult;
		} catch (Exception e) {
			System.out.println(e);
			simResult.setSimValueList(simValueList);
			simResult.setSimDateList(simDateList);
			simResult.setSimViewList(simViewList);
			return simResult;
		}
	}

	/**
	 * Round the result value.
	 * 
	 * @param result
	 *            the result from calculation.
	 * @return the result as rounded value
	 */
	private double roundResult(double result) {
		int position = 4;
		double factor = Math.pow(10, position);

		result = Math.round(result * factor) / factor;
		return result;
	}

	/**
	 * Get all vector data for sim resource.
	 * 
	 * @param allTags
	 *            all tags in portal
	 * @param allGroups
	 *            all groups in portal
	 * @param sim
	 *            the sim resource
	 * @return filled vector for sim resource
	 * @throws SystemException
	 * @throws PortalException
	 */
	private Vector<Long> getSimResourceVector(String[] allTags, List<Group> allGroups, long userId, TagsAsset sim, String[] checklist) throws SystemException,
			PortalException {
		List<String> simResource = getResourceTagList(sim);
		Long userSimId = sim.getUserId();
		User userSim = UserLocalServiceUtil.getUser(userSimId);
		long[] groupsSim = userSim.getGroupIds();

		Vector<Long> simResourceVector = new Vector<Long>();
		Vector<String> checkListVector = getCheckListVector(checklist);

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_TAGS)) {
			for (String tag : allTags) {
				Long hasTag = 0l;
				for (String simRessourceTag : simResource) {
					if (simRessourceTag.equals(tag)) {
						hasTag = 1l;
					}
				}
				simResourceVector.add(hasTag);
			}
		}

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_USER)) {
			if(userId == userSimId){
				simResourceVector.add(1l);				
			}else {
				simResourceVector.add(0l);
			}
		}

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_GROUP)) {
			for (Group portalGroup : allGroups) {
				Long hasGroup = 0l;
				for (Long simGroup : groupsSim) {
					if (simGroup == portalGroup.getGroupId()) {
						hasGroup = 1l;
					}
				}
				simResourceVector.add(hasGroup);
			}
		}
		return simResourceVector;
	}

	/**
	 * Get all vector data for start resource.
	 * 
	 * @param allTags
	 *            all tags in portal
	 * @param allGroups
	 *            all groups in portal
	 * @param sim
	 *            the start resource
	 * @return filled vector for start resource
	 * @throws SystemException
	 * @throws PortalException
	 */
	private Vector<Long> getStartResourceVector(TagsAsset entry, String[] allTags, List<Group> allGroups, String[] checklist) throws SystemException,
			PortalException {
		Vector<Long> startResourceVector = new Vector<Long>();
		List<String> tagsStartResource = getResourceTagList(entry);
		Long userStartId = entry.getUserId();
		User user = UserLocalServiceUtil.getUser(userStartId);
		long[] groupsStart = user.getGroupIds();

		Vector<String> checkListVector = getCheckListVector(checklist);

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_TAGS)) {
			for (String tag : allTags) {
				Long hasTag = 0l;
				for (String startRessourceTag : tagsStartResource) {
					if (startRessourceTag.equals(tag)) {
						hasTag = 1l;
					}
				}
				startResourceVector.add(hasTag);
			}
		}

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_USER)) {
			startResourceVector.add(1l);			
		}

		if (checkListVector.contains(SimilarityMeasureBuilder.CHECKLIST_GROUP)) {
			for (Group portalGroup : allGroups) {
				Long hasGroup = 0l;
				for (long userGroup : groupsStart) {
					if (userGroup == portalGroup.getGroupId()) {
						hasGroup = 1l;
					}
				}
				startResourceVector.add(hasGroup);
			}
		}
		return startResourceVector;
	}

	/**
	 * Add all user check sim measures to vector. If user checked nothing all
	 * sim terms will be added.
	 * 
	 * @param checklist
	 *            user checked sim term at view
	 * @return vector with sim terms
	 */
	private Vector<String> getCheckListVector(String[] checklist) {
		Vector<String> checkListVector = new Vector<String>();
		if (checklist != null && checklist.length > 0) {
			for (int i = 0; i < checklist.length; i++) {
				checkListVector.add(checklist[i]);
			}
		} else {
			checkListVector.add(SimilarityMeasureBuilder.CHECKLIST_TAGS);
			checkListVector.add(SimilarityMeasureBuilder.CHECKLIST_USER);
			checkListVector.add(SimilarityMeasureBuilder.CHECKLIST_GROUP);
		}
		return checkListVector;
	}

	private List<String> getResourceTagList(TagsAsset entry) throws SystemException {
		List<TagsEntry> tagsStartResource = entry.getEntries();
		List<String> tagNames = new ArrayList<String>();

		for (TagsEntry tagsEntry : tagsStartResource) {
			tagNames.add(tagsEntry.getName());
		}
		return tagNames;
	}

}
