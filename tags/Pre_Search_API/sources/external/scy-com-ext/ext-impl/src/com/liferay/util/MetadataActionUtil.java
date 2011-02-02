package com.liferay.util;

import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.portlet.ActionRequest;

import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.service.MetadataEntryLocalServiceUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;

/**
 * Util class for all metadata action.
 * 
 * @author daniel
 * 
 */
public class MetadataActionUtil {

	/**
	 * Persist metadata to db for images. Get some content from tagsAsset entry.
	 * 
	 * @param image
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static MetadataEntry addMetadata(IGImage image, ActionRequest req) throws Exception {

		long metaId = CounterLocalServiceUtil.increment(MetadataEntry.class.getName());

		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);

		Date now = new Date();
		User user = UserLocalServiceUtil.getUser(image.getUserId());
		String userName = user.getFirstName() + " " + user.getLastName();

		MetadataEntry metadataEntry = MetadataEntryLocalServiceUtil.createMetadataEntry(metaId);
		MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();

		metadataEntry.setCreateDate(now);
		metadataEntry.setModifiedDate(now);
		metadataEntry.setGroupId(themeDisplay.getScopeGroupId());
		metadataEntry.setCompanyId(themeDisplay.getCompanyId());
		metadataEntry.setAssertEntryId(image.getImageId());

		metadataEntry.setDc_date(now.toString());
		metadataEntry.setDc_title(image.getName());
		metadataEntry.setDc_description(image.getDescription());
		metadataEntry.setDc_format(mimeType.getContentType("." + image.getImageType()));
		metadataEntry.setDc_type("");
		metadataEntry.setDc_publisher(userName);
		metadataEntry.setDc_creator(userName);
		metadataEntry.setDc_contributor(userName);
		metadataEntry.setDc_identifier(Long.toString(image.getImageId()));
		metadataEntry.setDc_language(user.getLanguageId());

		MetadataEntryLocalServiceUtil.updateMetadataEntry(metadataEntry);
		System.out.println("Add MetadataEntry for image with id: " + image.getImageId());
		return metadataEntry;

	}

	/**
	 * Persist metadata to db. Get some content from tagsAsset entry.
	 * 
	 * @param image
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static MetadataEntry addMetadata(String className, Long classPK, ActionRequest req) throws Exception {

		TagsAsset tagsAssstEntry = TagsAssetLocalServiceUtil.getAsset(className, classPK);
		long metaId = CounterLocalServiceUtil.increment(MetadataEntry.class.getName());

		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);

		TagsAsset asset = TagsAssetLocalServiceUtil.getAsset(className, classPK);
		Date now = new Date();

		User user = UserLocalServiceUtil.getUser(asset.getUserId());
		String userName = user.getFirstName() + " " + user.getLastName();

		MetadataEntry metadataEntry = MetadataEntryLocalServiceUtil.createMetadataEntry(metaId);

		metadataEntry.setCreateDate(now);
		metadataEntry.setModifiedDate(now);
		metadataEntry.setGroupId(themeDisplay.getScopeGroupId());
		metadataEntry.setCompanyId(themeDisplay.getCompanyId());
		metadataEntry.setAssertEntryId(tagsAssstEntry.getClassPK());
		metadataEntry.setDc_date(now.toString());
		metadataEntry.setDc_title(tagsAssstEntry.getTitle());
		metadataEntry.setDc_description(tagsAssstEntry.getDescription());
		metadataEntry.setDc_format(tagsAssstEntry.getMimeType());
		metadataEntry.setDc_type("");
		metadataEntry.setDc_publisher(userName);
		metadataEntry.setDc_creator(userName);
		metadataEntry.setDc_contributor(userName);
		metadataEntry.setDc_identifier(String.valueOf(tagsAssstEntry.getAssetId()));
		metadataEntry.setDc_language(user.getLanguageId());

		MetadataEntryLocalServiceUtil.updateMetadataEntry(metadataEntry);
		System.out.println("Add MetadataEntry for asset entry with id: " + tagsAssstEntry.getAssetId());
		return metadataEntry;
	}

	/**
	 * Delete metadata from tagsAsset with assetEntryId.
	 * 
	 * @param assetEntryId
	 *            the id from given resource.
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static void deleteMetadata(Long assetEntryId) throws SystemException, PortalException {
		List<MetadataEntry> metaList = MetadataEntryLocalServiceUtil.getMetadataEntries(0, MetadataEntryLocalServiceUtil.getMetadataEntriesCount());
		for (MetadataEntry metadataEntry : metaList) {
			if (assetEntryId.equals(metadataEntry.getAssertEntryId())) {
				MetadataEntryLocalServiceUtil.deleteMetadataEntry(metadataEntry.getEntryId());
				System.out.println("Remove MetadataEntry for file with id: " + assetEntryId);
				return;
			}
		}
	}

	/**
	 * Update metadata. Get parameter from user filled form.
	 * 
	 * @param req
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static void updateMetadata(ActionRequest req) throws SystemException, PortalException {
		long entryId = Long.valueOf(req.getParameter("entryId"));
		String className = req.getParameter("className");

		String contributor = req.getParameter("contributor");
		String coverage = req.getParameter("coverage");
		String creator = req.getParameter("creator");
		String description = req.getParameter("description");
		String format = req.getParameter("format");
		String identifier = req.getParameter("identifier");
		String language = req.getParameter("language");
		String publisher = req.getParameter("publisher");
		String relation = req.getParameter("relation");
		String rights = req.getParameter("rights");
		String source = req.getParameter("source");
		String title = req.getParameter("title");

		String[] subject = (String[]) req.getParameterValues("subject");
		String[] type = (String[]) req.getParameterValues("type");

		StringBuffer sbSubject = new StringBuffer();

		if (subject != null) {
			for (int i = 0; i < subject.length; i++) {
				sbSubject.append(subject[i]);
				if (i < (subject.length - 1)) {
					sbSubject.append(", ");
				}
			}
		}

		StringBuffer sbType = new StringBuffer();

		if (type != null) {
			for (int i = 0; i < type.length; i++) {
				sbType.append(type[i]);
				if (i < (type.length - 1)) {
					sbType.append(", ");
				}
			}
		}

		MetadataEntry metadataEntry = getMetadata(className, entryId);
		Date now = new Date();

		metadataEntry.setDc_contributor(contributor);
		metadataEntry.setDc_coverage(coverage);
		metadataEntry.setDc_creator(creator);
		metadataEntry.setDc_date(now.toString());
		metadataEntry.setDc_description(description);
		metadataEntry.setDc_format(format);
		metadataEntry.setDc_identifier(identifier);
		metadataEntry.setDc_language(language);
		metadataEntry.setDc_publisher(publisher);
		metadataEntry.setDc_relation(relation);
		metadataEntry.setDc_rights(rights);
		metadataEntry.setDc_source(source);
		metadataEntry.setDc_subject(sbSubject.toString());
		metadataEntry.setDc_title(title);
		metadataEntry.setDc_type(sbType.toString());

		MetadataEntryLocalServiceUtil.updateMetadataEntry(metadataEntry);
		System.out.println("update metadataEntry with id: " + metadataEntry.getEntryId());
	}

	/**
	 * Get metadata for given resource.
	 * 
	 * @param className
	 *            the class name from given resource.
	 * @param entryId
	 *            the id from given resource.
	 * @return metadata entry from regarded resource.
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static MetadataEntry getMetadata(String className, Long entryId) throws SystemException, PortalException {
		List<MetadataEntry> metaList = MetadataEntryLocalServiceUtil.getMetadataEntries(0, MetadataEntryLocalServiceUtil.getMetadataEntriesCount());
		for (MetadataEntry metadataEntry : metaList) {
			if (entryId.equals(metadataEntry.getAssertEntryId())) {
				System.out.println("get MetadateEntry for assertEntry with id: " + entryId);
				return metadataEntry;
			}

		}
		return null;
	}

}
