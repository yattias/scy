package com.liferay.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Transactional;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.tags.DuplicateEntryException;
import com.liferay.portlet.tags.NoSuchPropertyException;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.model.TagsEntry;
import com.liferay.portlet.tags.model.TagsProperty;
import com.liferay.portlet.tags.model.TagsVocabulary;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsPropertyLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsVocabularyLocalServiceUtil;
import com.liferay.portlet.tags.service.persistence.TagsAssetUtil;

/**
 * Util class for all tag operations on each entry. Add functionality too add
 * and remove own tags on viewed entry.
 * 
 * @author daniel
 * 
 */

public class TagActionUtil {

	/**
	 * Persist new tag entry.
	 * 
	 * @param req
	 *            ActionRequest from blogs AddTagEntryAction
	 * @return assetTag Entry
	 * @throws Exception
	 */
	public static TagsEntry addTag(ActionRequest req) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		String title = req.getParameter("title");
		User user = UserLocalServiceUtil.getUser(userId);
		Date now = new Date();
		long tagId = CounterLocalServiceUtil.increment(TagsEntry.class.getName());
		TagsEntry assetTag = TagsEntryLocalServiceUtil.createTagsEntry(tagId);
		assetTag.setCompanyId(themeDisplay.getCompanyId());
		assetTag.setCreateDate(now);
		assetTag.setGroupId(themeDisplay.getScopeGroupId());
		assetTag.setModifiedDate(now);
		assetTag.setName(title);
		assetTag.setUserId(userId);
		assetTag.setUserName(user.getFullName());

		Long vocaId = 0l;
		List<TagsVocabulary> tagsVocabulary = TagsVocabularyLocalServiceUtil.getTagsVocabularies(0, TagsVocabularyLocalServiceUtil.getTagsVocabulariesCount());
		if (tagsVocabulary != null && tagsVocabulary.size() > 0) {
			for (TagsVocabulary tagsVocabulary2 : tagsVocabulary) {
				if (tagsVocabulary2.getUserId() == user.getUserId()) {
					vocaId = tagsVocabulary2.getVocabularyId();
				}
			}
		}

		if (vocaId == 0l) {
			TagsVocabulary tVocabulary = TagsVocabularyLocalServiceUtil.createTagsVocabulary(user.getUserId());
			tVocabulary.setCompanyId(themeDisplay.getCompanyId());
			tVocabulary.setCreateDate(now);
			tVocabulary.setGroupId(themeDisplay.getScopeGroupId());
			tVocabulary.setName(String.valueOf(user.getUserId()));
			tVocabulary.setUserId(user.getUserId());
			tVocabulary.setModifiedDate(now);
			TagsVocabularyLocalServiceUtil.addTagsVocabulary(tVocabulary);
			vocaId = userId;
		}
		assetTag.setVocabularyId(vocaId);
		assetTag = TagsEntryLocalServiceUtil.addTagsEntry(assetTag);
		TagsEntryLocalServiceUtil.addEntryResources(assetTag, true, true);
		System.out.println("Add new Tag Entry " + assetTag.getName() + " with id: " + assetTag.getEntryId() + " from user with id: " + assetTag.getUserId());
		return assetTag;
	}

	/**
	 * Add tag relationship between tagsAsset and tagsEntry.
	 * 
	 * @param tagId
	 *            TagEntryId from new TagEntry
	 * @param entryId
	 *            EntryId from viewed blog
	 * @throws SystemException
	 * @throws PortalException
	 */
	@Transactional
	public static void addTagRelationship(ActionRequest req, long tagId, long entryId) throws SystemException, PortalException {
		String className = req.getParameter("className");
		TagsAsset assetEntry = TagsAssetLocalServiceUtil.getAsset(className, entryId);
		TagsAssetUtil.addTagsEntry(assetEntry.getAssetId(), tagId);
		System.out.println("Add TagsAssetEntry - TagEntry relationship with entry id " + assetEntry.getAssetId() + " and tagId " + tagId);
	}

	/**
	 * Persist new taglist.
	 * 
	 * @param req
	 * @param assetId
	 *            the id from tags asset entry where tags should add.
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addNewTags(ActionRequest req, long assetId) throws PortalException, SystemException {
		String className = req.getParameter("className");
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		String parentEntryName = "";
		String name;
		String vocabularyName = "";
		String[] properties = {};
		ServiceContext serviceContext = ServiceContextFactory.getInstance(className, req);

		String[] tagEntries = req.getParameterValues("tagsEntries");
		tagEntries = StringUtil.split(tagEntries[0], ",");

		List<TagsEntry> tagsList = new ArrayList<TagsEntry>();
		TagsEntry tagsEntry;

		for (int i = 0; i < tagEntries.length; i++) {

			name = tagEntries[i];

			// add new tagsEntry if no tagsEntry exist with value of name
			try {
				tagsEntry = TagsEntryLocalServiceUtil.addEntry(themeDisplay.getUserId(), parentEntryName, name, vocabularyName, properties, serviceContext);
				tagsList.add(tagsEntry);

			} catch (DuplicateEntryException dee) {
				System.out.println("tag allready exists in system, if no property is fround for this tag name and user a new property will create");
				tagsEntry = TagsEntryLocalServiceUtil.getEntry(themeDisplay.getScopeGroupId(), name);
				tagsList.add(tagsEntry);
			}

			// add property for tagsEntry and given user
			try {
				TagsPropertyLocalServiceUtil.getProperty(tagsEntry.getEntryId(), String.valueOf(assetId));
			} catch (NoSuchPropertyException e) {
				TagsPropertyLocalServiceUtil.addProperty(themeDisplay.getUserId(), tagsEntry.getEntryId(), String.valueOf(themeDisplay.getUserId())
						+ String.valueOf(assetId) + String.valueOf(tagsEntry.getEntryId()), String.valueOf(assetId));
			}

		}

		if (tagsList.size() > 0) {
			TagsAssetUtil.addTagsEntries(assetId, tagsList);
		}
	}

	/**
	 * Delete tag entry.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static TagsEntry deleteTag(ActionRequest req) throws Exception {
		long tagId = Long.valueOf(req.getParameter("tagId"));
		TagsEntryLocalServiceUtil.deleteEntry(tagId);
		System.out.println("Delete tag with id: " + tagId);
		return null;
	}

	/**
	 * Delete relationship between tag entry and tags asset entry.
	 * 
	 * @param tagId
	 * @param entryId
	 * @throws SystemException
	 */
	@Transactional
	public static void deleteTagRelationship(long tagId, long entryId) throws SystemException {
		TagsAssetUtil.removeTagsEntry(entryId, tagId);
		System.out.println("Delete TagsAssetEntry - TagEntry relationship with entry id " + entryId + " and tagId " + tagId);
	}

	/**
	 * Delete tag property with tagId as entryId and userId as key.
	 * 
	 * @param tagId
	 *            id from TagsEntry
	 * @param userId
	 *            id from given user
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void deleteTagsProperty(long tagId, long userId, long assetId) throws PortalException, SystemException {
		try {
			TagsProperty tagsProperty = TagsPropertyLocalServiceUtil.getProperty(tagId, String.valueOf(userId) + String.valueOf(assetId)
					+ String.valueOf(tagId));
			TagsPropertyLocalServiceUtil.deleteProperty(tagsProperty.getPropertyId());
		} catch (NoSuchPropertyException e) {
			System.out.println("TagsProperty not found with entryId: " + tagId + " and userID: " + assetId);
		}
	}

	/**
	 * Delete tag action. First delete property. If tagPropertyList for this
	 * entry is empty also delete tagsEntry and relationship to asset. If
	 * propertyList is not empty but no other property for this asset exists
	 * delete relationship between asset and tag.
	 * 
	 * @param req
	 *            actionRequest
	 * @param entryId
	 *            id from resource.
	 * @param tagId
	 *            id from clicked tagsEntry.
	 * @throws PortalException
	 * @throws SystemException
	 * @throws Exception
	 */
	public static void deleteTagEntry(ActionRequest req, long entryId, long tagId) throws PortalException, SystemException, Exception {
		String className = req.getParameter("className");
		TagsAsset asset = TagsAssetLocalServiceUtil.getAsset(className, entryId);
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		int relationshipCount = 0;

		if (TagActionUtil.isTagAlreadyDelete(req) == false) {

			deleteTagsProperty(tagId, userId, asset.getAssetId());

			List<TagsProperty> tagsPropertiesList = TagsPropertyLocalServiceUtil.getProperties(tagId);

			// delete hole tagsEntry if no other tagsProperties is set
			if (tagsPropertiesList.isEmpty()) {
				deleteTag(req);
				deleteTagRelationship(tagId, entryId);
				// delete asset/tag relationship if no property exists for this
				// relationship
			} else {
				for (TagsProperty tagsProperty : tagsPropertiesList) {
					if (tagsProperty.getValue().equals(String.valueOf(asset.getAssetId()))) {
						relationshipCount++;
					}
				}
				if (relationshipCount == 0) {
					deleteTagRelationship(tagId, asset.getAssetId());
				}

			}

		}
	}

	/**
	 * Check if user had already tag this entry with this tag name
	 * 
	 * @param req
	 *            ActionRequest
	 * @param tag
	 *            tag name as string
	 * @param entryId
	 *            Id from viewed resource
	 * @return true/false
	 * @throws SystemException
	 * @throws PortalException
	 */

	public static boolean isTagAlreadyAtDB(ActionRequest req, String tag, long entryId) throws SystemException, PortalException {
		String className = req.getParameter("className");
		List<TagsEntry> allTagsFromEntry = TagsEntryLocalServiceUtil.getEntries(className, entryId);
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		for (TagsEntry assetTag : allTagsFromEntry) {
			// check if one tag from actual entry tags equals with insert tag
			if (assetTag.getName().contentEquals(tag)) {
				if (assetTag.getUserId() == userId) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if user has update browser and delete the tag before.
	 * 
	 * @param req
	 * @return has user delete the tag with entered tagId.
	 * @throws SystemException
	 */
	public static boolean isTagAlreadyDelete(ActionRequest req) throws SystemException {
		long tagId = Long.valueOf(req.getParameter("tagId"));
		try {
			TagsEntryLocalServiceUtil.getEntry(tagId);
		} catch (PortalException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
}
