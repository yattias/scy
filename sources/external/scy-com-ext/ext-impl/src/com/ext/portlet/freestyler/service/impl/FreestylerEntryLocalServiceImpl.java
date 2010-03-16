package com.ext.portlet.freestyler.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.base.FreestylerEntryLocalServiceBaseImpl;
import com.liferay.documentlibrary.FileSizeException;
import com.liferay.documentlibrary.util.Hook;
import com.liferay.documentlibrary.util.HookFactory;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryConstants;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.model.impl.DLFileEntryImpl;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.MetadataActionUtil;

public class FreestylerEntryLocalServiceImpl extends FreestylerEntryLocalServiceBaseImpl {

	public long getNewId() throws SystemException {
		long imageId = counterLocalService.increment();
		return imageId;
	}

	@Override
	public FreestylerEntry addFreestylerEntry(long userId, String name, String description, ServiceContext serviceContext, ThemeDisplay themeDisplay)
			throws PortalException, SystemException {
		User user = userPersistence.findByPrimaryKey(userId);

		Date now = new Date();
		long groupId = serviceContext.getScopeGroupId();

		long entryId = counterLocalService.increment();

		FreestylerEntry entry = freestylerEntryPersistence.create(entryId);
		entry.setCompanyId(user.getCompanyId());
		entry.setCreateDate(now);
		entry.setGroupId(groupId);
		entry.setModifiedDate(now);
		entry.setName(name);
		entry.setDescription(description);
		entry.setUserId(userId);

		freestylerEntryPersistence.update(entry, false);

		if (serviceContext.getAddCommunityPermissions() || serviceContext.getAddGuestPermissions()) {

			addEntryResources(entry, serviceContext.getAddCommunityPermissions(), serviceContext.getAddGuestPermissions());
		} else {
			addEntryResources(entry, serviceContext.getCommunityPermissions(), serviceContext.getGuestPermissions());
		}

		updateTagsAsset(userId, entry, serviceContext.getTagsEntries());

		return entry;
	}

	public void addEntryResources(long entryId, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {

		FreestylerEntry entry = freestylerEntryPersistence.findByPrimaryKey(entryId);

		addEntryResources(entry, addCommunityPermissions, addGuestPermissions);
	}

	public void addEntryResources(FreestylerEntry entry, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {

		resourceLocalService.addResources(entry.getCompanyId(), entry.getGroupId(), entry.getUserId(), FreestylerEntry.class.getName(),
				entry.getFreestylerId(), false, addCommunityPermissions, addGuestPermissions);
	}

	public void addEntryResources(long entryId, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		FreestylerEntry entry = freestylerEntryPersistence.findByPrimaryKey(entryId);

		addEntryResources(entry, communityPermissions, guestPermissions);
	}

	public void addEntryResources(FreestylerEntry entry, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		resourceLocalService.addModelResources(entry.getCompanyId(), entry.getGroupId(), entry.getUserId(), FreestylerEntry.class.getName(), entry
				.getFreestylerId(), communityPermissions, guestPermissions);
	}

	public void updateTagsAsset(long userId, FreestylerEntry entry, String[] tagsEntries) throws PortalException, SystemException {

		tagsAssetLocalService.updateAsset(userId, entry.getGroupId(), FreestylerEntry.class.getName(), entry.getFreestylerId(), null, tagsEntries, true, null,
				null, entry.getModifiedDate(), null, "freestyler/entry", entry.getName(), entry.getDescription(), null, null, 0, 0, null, false);

	}

	public DLFileEntry addFileEntry(long userId, long folderId, String name, String title, String description, String extraSettings, File file,
			ServiceContext serviceContext) throws PortalException, SystemException {

		if (!PropsValues.WEBDAV_LITMUS) {
			if (file == null) {
				throw new FileSizeException();
			}
		}

		InputStream is = null;

		try {
			is = new BufferedInputStream(new FileInputStream(file));

			return addFileEntry(null, userId, folderId, name, title, description, extraSettings, is, file.length(), serviceContext);
		} catch (FileNotFoundException fnfe) {
			throw new FileSizeException();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ioe) {
				_log.error(ioe);
			}
		}
	}

	public DLFileEntry addFileEntry(String uuid, long userId, long folderId, String name, String title, String description, String extraSettings, byte[] bytes,
			ServiceContext serviceContext) throws PortalException, SystemException {

		if (!PropsValues.WEBDAV_LITMUS) {
			if ((bytes == null) || (bytes.length == 0)) {
				throw new FileSizeException();
			}
		}

		InputStream is = new ByteArrayInputStream(bytes);

		return addFileEntry(uuid, userId, folderId, name, title, description, extraSettings, is, bytes.length, serviceContext);
	}

	public DLFileEntry addFileEntry(String uuid, long userId, long folderId, String name, String title, String description, String extraSettings,
			InputStream is, long size, ServiceContext serviceContext) throws PortalException, SystemException {

		// File entry

		User user = userPersistence.findByPrimaryKey(userId);
		folderId = getFolderId(user.getCompanyId(), folderId);
		DLFolder folder = dlFolderPersistence.findByPrimaryKey(folderId);
		Date now = new Date();

		name = getName(name);
		title = DLFileEntryImpl.stripExtension(name, title);
		long fileEntryId = counterLocalService.increment();

		DLFileEntry fileEntry = dlFileEntryPersistence.create(fileEntryId);

		fileEntry.setUuid(uuid);
		fileEntry.setGroupId(folder.getGroupId());
		fileEntry.setCompanyId(user.getCompanyId());
		fileEntry.setUserId(user.getUserId());
		fileEntry.setUserName(user.getFullName());
		fileEntry.setVersionUserId(user.getUserId());
		fileEntry.setVersionUserName(user.getFullName());
		fileEntry.setCreateDate(now);
		fileEntry.setModifiedDate(now);
		fileEntry.setFolderId(folderId);
		fileEntry.setName(name);
		fileEntry.setTitle(title);
		fileEntry.setDescription(description);
		fileEntry.setVersion(DLFileEntryConstants.DEFAULT_VERSION);
		fileEntry.setSize((int) size);
		fileEntry.setReadCount(DLFileEntryConstants.DEFAULT_READ_COUNT);
		fileEntry.setExtraSettings(extraSettings);

		dlFileEntryPersistence.update(fileEntry, false);

		// Resources

		if (serviceContext.getAddCommunityPermissions() || serviceContext.getAddGuestPermissions()) {

			addFileEntryResources(fileEntry, serviceContext.getAddCommunityPermissions(), serviceContext.getAddGuestPermissions());
		} else {
			addFileEntryResources(fileEntry, serviceContext.getCommunityPermissions(), serviceContext.getGuestPermissions());
		}

		// Expando

		ExpandoBridge expandoBridge = fileEntry.getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);

		// File

		addFile(user.getCompanyId(), PortletKeys.DOCUMENT_LIBRARY, fileEntry.getGroupId(), folderId, name, fileEntryId, fileEntry.getLuceneProperties(),
				fileEntry.getModifiedDate(), serviceContext.getTagsCategories(), serviceContext.getTagsEntries(), is);

		// Tags

		updateTagsAsset(userId, fileEntry, serviceContext.getTagsCategories(), serviceContext.getTagsEntries());

		// Folder

		folder.setLastPostDate(fileEntry.getModifiedDate());

		dlFolderPersistence.update(folder, false);

		return fileEntry;
	}

	public void updateTagsAsset(long userId, DLFileEntry fileEntry, String[] tagsCategories, String[] tagsEntries) throws PortalException, SystemException {

		String mimeType = "freestyler/xml";

		tagsAssetLocalService.updateAsset(userId, fileEntry.getGroupId(), DLFileEntry.class.getName(), fileEntry.getFileEntryId(), tagsCategories, tagsEntries,
				true, null, null, null, null, mimeType, fileEntry.getTitle(), fileEntry.getDescription(), null, null, 0, 0, null, false);
	}

	protected long getFolderId(long companyId, long folderId) throws SystemException {

		if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

			// Ensure folder exists and belongs to the proper company

			DLFolder folder = dlFolderPersistence.fetchByPrimaryKey(folderId);

			if ((folder == null) || (companyId != folder.getCompanyId())) {
				folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
			}
		}

		return folderId;
	}

	public void addFileEntryResources(DLFileEntry fileEntry, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException,
			SystemException {

		resourceLocalService.addResources(fileEntry.getCompanyId(), fileEntry.getGroupId(), fileEntry.getUserId(), DLFileEntry.class.getName(), fileEntry
				.getFileEntryId(), false, addCommunityPermissions, addGuestPermissions);
	}

	public void addFileEntryResources(long fileEntryId, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		DLFileEntry fileEntry = dlFileEntryPersistence.findByPrimaryKey(fileEntryId);

		addFileEntryResources(fileEntry, communityPermissions, guestPermissions);
	}

	public void addFileEntryResources(DLFileEntry fileEntry, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		resourceLocalService.addModelResources(fileEntry.getCompanyId(), fileEntry.getGroupId(), fileEntry.getUserId(), DLFileEntry.class.getName(), fileEntry
				.getFileEntryId(), communityPermissions, guestPermissions);
	}

	protected String getName(String name) throws SystemException {
		String extension = StringPool.BLANK;

		int pos = name.lastIndexOf(StringPool.PERIOD);

		if (pos != -1) {
			extension = name.substring(pos + 1, name.length()).toLowerCase();
		}

		name = String.valueOf(counterLocalService.increment(DLFileEntry.class.getName()));

		if (Validator.isNotNull(extension)) {
			name = "DLFE-" + name + StringPool.PERIOD + extension;
		}

		return name;
	}

	public void addFile(long companyId, String portletId, long groupId, long repositoryId, String fileName, long fileEntryId, String properties,
			Date modifiedDate, String[] tagsCategories, String[] tagsEntries, InputStream is) throws PortalException, SystemException {

		Hook hook = HookFactory.getInstance();

		hook.addFile(companyId, portletId, groupId, repositoryId, fileName, fileEntryId, properties, modifiedDate, tagsCategories, tagsEntries, is);
	}

	private static Log _log = LogFactoryUtil.getLog(FreestylerEntry.class);

	@Override
	public int getGroupEntriesCount(long groupId) throws SystemException {
		return freestylerEntryPersistence.countByGroupId(groupId);
	}

	public List<FreestylerEntry> getGroupEntries(long groupId) throws SystemException {

		return freestylerEntryPersistence.findByGroupId(groupId);
	}

	@Override
	public List<FreestylerImage> getFreestylerImages(long pk) throws PortalException, SystemException {

		return freestylerEntryPersistence.getFreestylerImages(pk);
	}

	@Override
	public FreestylerEntry updateFreestylerEntry(long userId, long entryId, String name, String description, ServiceContext serviceContext)
			throws SystemException, PortalException {
		FreestylerEntry entry = freestylerEntryPersistence.findByPrimaryKey(entryId);

		if (Validator.isNull(description)) {
			description = "";
		}

		entry.setModifiedDate(new Date());
		entry.setName(name);
		entry.setDescription(description);

		freestylerEntryPersistence.update(entry, false);

		// Tags

		updateTagsAsset(userId, entry, serviceContext.getTagsEntries());

		// Indexer

		return entry;
	}

	public void deleteEntry(long entryId) throws PortalException, SystemException {

		FreestylerEntry entry = freestylerEntryPersistence.findByPrimaryKey(entryId);

		deleteEntry(entry);
	}

	public void deleteEntry(FreestylerEntry entry) throws PortalException, SystemException {

		// Tags

		TagsAsset tagsAsset = TagsAssetLocalServiceUtil.getAsset(FreestylerEntry.class.getName(), entry.getFreestylerId());
		tagsAssetPersistence.remove(tagsAsset);

		// Images

		List<FreestylerImage> listFreestylerImages = freestylerEntryPersistence.getFreestylerImages(entry.getFreestylerId());

		long folderId = listFreestylerImages.get(0).getFolderId();

		for (FreestylerImage freestylerImage : listFreestylerImages) {
			freestylerImagePersistence.remove(freestylerImage);
			TagsAsset imageTagsAsset = TagsAssetLocalServiceUtil.getAsset(FreestylerImage.class.getName(), freestylerImage.getImageId());
			tagsAssetPersistence.remove(imageTagsAsset);
			// delete metadata for images
			MetadataActionUtil.deleteMetadata(freestylerImage.getImageId());
		}
		freestylerEntryPersistence.removeFreestylerImages(entry.getFreestylerId(), listFreestylerImages);

		// Folder
		freestylerFolderPersistence.remove(folderId);

		// XML

		DLFileEntry dlFile = dlFileEntryPersistence.findByPrimaryKey(entry.getXmlFileId());

		// delete metadata for images
		MetadataActionUtil.deleteMetadata(dlFile.getFileEntryId());

		TagsAsset xmlTagsAsset = TagsAssetLocalServiceUtil.getAsset(DLFileEntry.class.getName(), entry.getXmlFileId());
		tagsAssetPersistence.remove(xmlTagsAsset);
		dlFileEntryPersistence.remove(dlFile.getFileEntryId());
		dlFolderPersistence.remove(dlFile.getFolderId());

		// Entry

		freestylerEntryPersistence.remove(entry);
	}

}
