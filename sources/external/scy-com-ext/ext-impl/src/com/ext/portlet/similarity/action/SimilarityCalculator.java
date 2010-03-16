package com.ext.portlet.similarity.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.service.LinkEntryLocalServiceUtil;
import com.ext.portlet.resourcehandling.ResourceTypeList;
import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.DateComparator;
import com.liferay.util.DoubleComparator;
import com.liferay.util.IntegerComparator;

/**
 * Calculate all similarity entries from the start resource for the sim.
 * 
 * @author Daniel
 * 
 */
public class SimilarityCalculator {

	/**
	 * Get all allowed asset entries and linked asset entries from db. After
	 * that calulate the result for all siglesimilarityMeasure and
	 * multiSimilarityMeasure.
	 * 
	 * @param entryId
	 *            the id from start resource.
	 * @param classNameSimRessource
	 *            the class name from start resource.
	 * @param renderRequest
	 * @return list of sim result. At this time it is not sorted.
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<Sim> similarityCalculator(Long entryId, String classNameSimRessource, String[] checklist, String measureMethod) throws PortalException,
			SystemException {
		TagsAsset entry = TagsAssetLocalServiceUtil.getAsset(classNameSimRessource, Long.valueOf(entryId));
		List<Sim> simList = new ArrayList<Sim>();

		List<TagsAsset> allUserAllowedEntries = getAllAssetTagsWithUserPermission();
		List<TagsAsset> allAllowdSingleValueEntries = getAllowedSingleValueEntries(allUserAllowedEntries);
		List<LinkEntry> resourceLinkedEntires = LinkEntryLocalServiceUtil.getLinkEntriesByResourceId(entryId);
		List<TagsAsset> allSingleLinkedEntries = allSingleLinkedEntries(resourceLinkedEntires);

		List<TagsAsset> allAllowdMultiValueEntries = getAllowedMultiValueEntries(allUserAllowedEntries);
		List<TagsAsset> allMultiLinkedEntries = allMultiLinkedEntries(resourceLinkedEntires);

		Hashtable<Long, Double> simValueLinkList = new Hashtable<Long, Double>();
		Hashtable<Long, Date> simDateLinkList = new Hashtable<Long, Date>();
		Hashtable<Long, Integer> simViewLinkCountList = new Hashtable<Long, Integer>();

		// remove entries from all asset entries
		allAllowdSingleValueEntries.remove(entry);
		allAllowdMultiValueEntries.remove(entry);

		// remove linked entries from all asset entries and put entries to
		// sim lists
		for (TagsAsset linkedEntry : allSingleLinkedEntries) {
			allAllowdSingleValueEntries.remove(linkedEntry);
			simValueLinkList.put(linkedEntry.getAssetId(), 1.0);
			simDateLinkList.put(linkedEntry.getAssetId(), linkedEntry.getModifiedDate());
			simViewLinkCountList.put(linkedEntry.getAssetId(), linkedEntry.getViewCount());
		}

		for (TagsAsset linkedEntry : allMultiLinkedEntries) {
			allAllowdMultiValueEntries.remove(linkedEntry);
			simValueLinkList.put(linkedEntry.getAssetId(), 1.0);
			simDateLinkList.put(linkedEntry.getAssetId(), linkedEntry.getModifiedDate());
			simViewLinkCountList.put(linkedEntry.getAssetId(), linkedEntry.getViewCount());
		}

		Sim linkSimimirityMeasure = new Sim();
		linkSimimirityMeasure.setSimDateList(simDateLinkList);
		linkSimimirityMeasure.setSimValueList(simValueLinkList);
		linkSimimirityMeasure.setSimViewList(simViewLinkCountList);

		Sim sigleSimilarityMeasure = new SimilarityMeasureBuilder(checklist, measureMethod).getSingleSimilarityMeasure(entry, allAllowdSingleValueEntries);

		List<String> multiValueTitle = getAllMultiValueAssetTitle(allAllowdMultiValueEntries);

		for (String title : multiValueTitle) {
			List<TagsAsset> multiCompostion = new ArrayList<TagsAsset>();
			for (TagsAsset tagsAsset : allAllowdMultiValueEntries) {
				if (tagsAsset.getTitle().split(" MULTIVALUE")[0].equals(title)) {
					multiCompostion.add(tagsAsset);
				}
			}
			Sim multiSimilarityMeasure = new SimilarityMeasureBuilder(checklist, measureMethod).getMultiSimilarityMeasure(entry, multiCompostion);
			simList.add(multiSimilarityMeasure);
		}

		simList.add(linkSimimirityMeasure);
		simList.add(sigleSimilarityMeasure);

		return simList;
	}

	private List<String> getAllMultiValueAssetTitle(List<TagsAsset> allAllowdMultiValueEntries) {
		List<String> multiValueTitle = new ArrayList<String>();
		for (TagsAsset tagsAsset : allAllowdMultiValueEntries) {
			multiValueTitle.add(tagsAsset.getTitle().split(" MULTIVALUE")[0]);
		}
		multiValueTitle = removeDouble(multiValueTitle);
		return multiValueTitle;
	}

	/**
	 * Get all asset tags from portal where user has permission for.
	 * 
	 * @return list of tags asset
	 * @throws SystemException
	 * @throws PortalException
	 * @throws PrincipalException
	 */
	private List<TagsAsset> getAllAssetTagsWithUserPermission() throws SystemException, PortalException, PrincipalException {
		List<TagsAsset> allUserAllowedEntries = new ArrayList<TagsAsset>();
		List<TagsAsset> allAssetEntries = TagsAssetLocalServiceUtil.getTagsAssets(0, TagsAssetLocalServiceUtil.getTagsAssetsCount());

		for (TagsAsset tagsAsset : allAssetEntries) {
			String className = ClassNameLocalServiceUtil.getClassName(tagsAsset.getClassNameId()).getClassName();
			if (getPermissionChecker().hasPermission(tagsAsset.getGroupId(), className, tagsAsset.getClassPK(), ActionKeys.VIEW)) {
				allUserAllowedEntries.add(tagsAsset);
			}
		}
		return allUserAllowedEntries;
	}

	/**
	 * All allowed single value tags asset entries.
	 * 
	 * @param allAssetEntries
	 * @return a list of all allowed tag entires.
	 */
	private List<TagsAsset> getAllowedSingleValueEntries(List<TagsAsset> allAssetEntries) {
		Vector<Class<?>> allowedClassesList = ResourceTypeList.getAllSingleValueClasses();
		List<TagsAsset> allAllowdAssetEntries = new ArrayList<TagsAsset>();

		for (TagsAsset assetEntry : allAssetEntries) {
			for (Class<?> allowedClass : allowedClassesList) {
				if (assetEntry.getClassName().equals(allowedClass.getName())) {
					allAllowdAssetEntries.add(assetEntry);
				}

			}
		}

		return allAllowdAssetEntries;

	}

	/**
	 * All allowed multi value tags asset entries.
	 * 
	 * @param allAssetEntries
	 * @return a list of all allowed tag entires.
	 */
	private List<TagsAsset> getAllowedMultiValueEntries(List<TagsAsset> allAssetEntries) {
		Vector<Class<?>> allowedClassesList = ResourceTypeList.getAllMultiValueClasses();
		List<TagsAsset> allAllowdAssetEntries = new ArrayList<TagsAsset>();
		List<String> allowedClassNameList = new ArrayList<String>();

		for (Class<?> allowedClassName : allowedClassesList) {
			allowedClassNameList.add(allowedClassName.getName());
		}

		for (TagsAsset assetEntry : allAssetEntries) {
			if (allowedClassNameList.contains(assetEntry.getClassName())) {
				allAllowdAssetEntries.add(assetEntry);
			}

		}

		return allAllowdAssetEntries;

	}

	/**
	 * Sort the value list by from highest to smallerst value.
	 * 
	 * @param simValueList
	 * @return sorted list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Hashtable<Long, Double>> getSortByValueList(Hashtable<Long, Double> simValueList) {
		ArrayList mySortetValueList = new ArrayList(simValueList.entrySet());
		Collections.sort(mySortetValueList, new DoubleComparator());
		return mySortetValueList;
	}

	/**
	 * Sort the date list from newerst date to olderst.
	 * 
	 * @param simDateList
	 * @return sorted list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Hashtable<Long, Date>> getSortByDateList(Hashtable<Long, Date> simDateList) {
		ArrayList mySortetValueList = new ArrayList(simDateList.entrySet());
		Collections.sort(mySortetValueList, new DateComparator());
		return mySortetValueList;
	}

	/**
	 * Sort the view count list from most views to least views.
	 * 
	 * @param simViewCountList
	 * @return sorted list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Hashtable<Long, Integer>> getSortByViewCountList(Hashtable<Long, Integer> simViewCountList) {
		ArrayList mySortetValueList = new ArrayList(simViewCountList.entrySet());
		Collections.sort(mySortetValueList, new IntegerComparator());
		return mySortetValueList;
	}

	/**
	 * All linked tags asset entries with allowed single value classes.
	 * 
	 * @param resourceLinkedEntires
	 * @return list of all linked tags assets.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> allSingleLinkedEntries(List<LinkEntry> resourceLinkedEntires) throws PortalException, SystemException {
		List<TagsAsset> allLinkedEntries = new ArrayList<TagsAsset>();

		List<String> multiValueClassNameIds = new ArrayList<String>();
		for (Class<?> multiValueClass : ResourceTypeList.getAllMultiValueClasses()) {
			multiValueClassNameIds.add(String.valueOf(ClassNameLocalServiceUtil.getClassNameId(multiValueClass.getName())));
		}

		for (LinkEntry linkEntry : resourceLinkedEntires) {
			String className = ClassNameLocalServiceUtil.getClassName(Long.valueOf(linkEntry.getLinkedResourceClassNameId())).getClassName();
			// check that link isnt multi value resource
			if (!multiValueClassNameIds.contains(linkEntry.getLinkedResourceClassNameId())
					&& getPermissionChecker().hasPermission(linkEntry.getGroupId(), className, Long.valueOf(linkEntry.getLinkedResourceId()), ActionKeys.VIEW)) {
				allLinkedEntries.add(TagsAssetLocalServiceUtil.getAsset(className, Long.valueOf(linkEntry.getLinkedResourceId())));

			}
		}
		return allLinkedEntries;
	}

	/**
	 * All linked tags asset entries with allowed multi value classes.
	 * className.
	 * 
	 * @param resourceLinkedEntires
	 * @return list of all freestyler linked entries.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> allMultiLinkedEntries(List<LinkEntry> resourceLinkedEntires) throws PortalException, SystemException {
		List<TagsAsset> allLinkedEntries = new ArrayList<TagsAsset>();

		List<String> multiValueClassNameIds = new ArrayList<String>();
		for (Class<?> multiValueClass : ResourceTypeList.getAllMultiValueClasses()) {
			multiValueClassNameIds.add(String.valueOf(ClassNameLocalServiceUtil.getClassNameId(multiValueClass.getName())));
		}

		for (LinkEntry linkEntry : resourceLinkedEntires) {
			String currentClassName = ClassNameLocalServiceUtil.getClassName(Long.valueOf(linkEntry.getLinkedResourceClassNameId())).getClassName();
			// check that link is multi value resource
			if (multiValueClassNameIds.contains(linkEntry.getLinkedResourceClassNameId())) {
				allLinkedEntries.add(TagsAssetLocalServiceUtil.getAsset(currentClassName, Long.valueOf(linkEntry.getLinkedResourceId())));

			}
		}
		return allLinkedEntries;
	}

	/**
	 * Remove double entry from a arrayList.
	 * 
	 * @param <T>
	 * @param list
	 *            arrayList.
	 * 
	 * @return new list with no doubled value entries.
	 */
	public static <T> ArrayList<T> removeDouble(Collection<T> list) {
		return new ArrayList<T>(new HashSet<T>(list));
	}

	/**
	 * Initializes new permission checker
	 * 
	 * @return new initialized permission checker.
	 * @throws PrincipalException
	 */
	public PermissionChecker getPermissionChecker() throws PrincipalException {
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			throw new PrincipalException("PermissionChecker not initialized");
		}

		return permissionChecker;
	}

}
