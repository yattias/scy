package com.ext.portlet.freestyler.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;

/**
 * <a href="FreestylerEntryLocalService.java.html"><b><i>View Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.ext.portlet.freestyler.service.impl.FreestylerEntryLocalServiceImpl</code>
 * . Modify methods in that class and rerun ServiceBuilder to populate this
 * class and all other generated classes.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * 
 * @see com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil
 * 
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor = { PortalException.class, SystemException.class })
public interface FreestylerEntryLocalService {
	public com.ext.portlet.freestyler.model.FreestylerEntry addFreestylerEntry(com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
			throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerEntry createFreestylerEntry(long freestylerId);

	public void deleteFreestylerEntry(long freestylerId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

	public void deleteFreestylerEntry(com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.ext.portlet.freestyler.model.FreestylerEntry getFreestylerEntry(long freestylerId) throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> getFreestylerEntries(int start, int end) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getFreestylerEntriesCount() throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
			throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
			boolean merge) throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(long userId, long entryId, String name, String description,
			com.liferay.portal.service.ServiceContext serviceContext) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getNewId() throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerEntry addFreestylerEntry(long userId, java.lang.String name, java.lang.String description,
			com.liferay.portal.service.ServiceContext serviceContext, com.liferay.portal.theme.ThemeDisplay themeDisplay)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addEntryResources(long entryId, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException;

	public void addEntryResources(com.ext.portlet.freestyler.model.FreestylerEntry entry, boolean addCommunityPermissions, boolean addGuestPermissions)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addEntryResources(long entryId, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addEntryResources(com.ext.portlet.freestyler.model.FreestylerEntry entry, java.lang.String[] communityPermissions,
			java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void updateTagsAsset(long userId, com.ext.portlet.freestyler.model.FreestylerEntry entry, java.lang.String[] tagsEntries)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(long userId, long folderId, java.lang.String name, java.lang.String title,
			java.lang.String description, java.lang.String extraSettings, java.io.File file, com.liferay.portal.service.ServiceContext serviceContext)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(java.lang.String uuid, long userId, long folderId, java.lang.String name,
			java.lang.String title, java.lang.String description, java.lang.String extraSettings, byte[] bytes,
			com.liferay.portal.service.ServiceContext serviceContext) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(java.lang.String uuid, long userId, long folderId, java.lang.String name,
			java.lang.String title, java.lang.String description, java.lang.String extraSettings, java.io.InputStream is, long size,
			com.liferay.portal.service.ServiceContext serviceContext) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void updateTagsAsset(long userId, com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry, java.lang.String[] tagsCategories,
			java.lang.String[] tagsEntries) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addFileEntryResources(com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry, boolean addCommunityPermissions,
			boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addFileEntryResources(long fileEntryId, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addFileEntryResources(com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry, java.lang.String[] communityPermissions,
			java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void addFile(long companyId, java.lang.String portletId, long groupId, long repositoryId, java.lang.String fileName, long fileEntryId,
			java.lang.String properties, java.util.Date modifiedDate, java.lang.String[] tagsCategories, java.lang.String[] tagsEntries, java.io.InputStream is)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getGroupEntriesCount(long groupId) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> getGroupEntries(long groupId) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(long pk) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException;

	public void deleteEntry(long entryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

}
