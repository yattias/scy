package com.ext.portlet.freestyler.service;


/**
 * <a href="FreestylerEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.freestyler.service.FreestylerEntryLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.FreestylerEntryLocalService
 *
 */
public class FreestylerEntryLocalServiceUtil {
    private static FreestylerEntryLocalService _service;

    public static com.ext.portlet.freestyler.model.FreestylerEntry addFreestylerEntry(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException {
        return getService().addFreestylerEntry(freestylerEntry);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry createFreestylerEntry(
        long freestylerId) {
        return getService().createFreestylerEntry(freestylerId);
    }

    public static void deleteFreestylerEntry(long freestylerId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteFreestylerEntry(freestylerId);
    }

    public static void deleteFreestylerEntry(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException {
        getService().deleteFreestylerEntry(freestylerEntry);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry getFreestylerEntry(
        long freestylerId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getFreestylerEntry(freestylerId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> getFreestylerEntries(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getFreestylerEntries(start, end);
    }

    public static int getFreestylerEntriesCount()
        throws com.liferay.portal.SystemException {
        return getService().getFreestylerEntriesCount();
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException {
        return getService().updateFreestylerEntry(freestylerEntry);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getService().updateFreestylerEntry(freestylerEntry, merge);
    }
    
    public static com.ext.portlet.freestyler.model.FreestylerEntry updateFreestylerEntry(
    		long userId, long entryId, String name, String description,
    		com.liferay.portal.service.ServiceContext serviceContext)  throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
		return getService().updateFreestylerEntry(userId, entryId, name, description, serviceContext);
    }

    public static long getNewId() throws com.liferay.portal.SystemException {
        return getService().getNewId();
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry addFreestylerEntry(
        long userId, java.lang.String name, java.lang.String description,
        com.liferay.portal.service.ServiceContext serviceContext,
        com.liferay.portal.theme.ThemeDisplay themeDisplay)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService()
                   .addFreestylerEntry(userId, name, description,
            serviceContext, themeDisplay);
    }

    public static void addEntryResources(long entryId,
        boolean addCommunityPermissions, boolean addGuestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addEntryResources(entryId, addCommunityPermissions,
            addGuestPermissions);
    }

    public static void addEntryResources(
        com.ext.portlet.freestyler.model.FreestylerEntry entry,
        boolean addCommunityPermissions, boolean addGuestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addEntryResources(entry, addCommunityPermissions,
            addGuestPermissions);
    }

    public static void addEntryResources(long entryId,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addEntryResources(entryId, communityPermissions, guestPermissions);
    }

    public static void addEntryResources(
        com.ext.portlet.freestyler.model.FreestylerEntry entry,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addEntryResources(entry, communityPermissions, guestPermissions);
    }

    public static void updateTagsAsset(long userId,
        com.ext.portlet.freestyler.model.FreestylerEntry entry,
        java.lang.String[] tagsEntries)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().updateTagsAsset(userId, entry, tagsEntries);
    }

    public static com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(
        long userId, long folderId, java.lang.String name,
        java.lang.String title, java.lang.String description,
        java.lang.String extraSettings, java.io.File file,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService()
                   .addFileEntry(userId, folderId, name, title, description,
            extraSettings, file, serviceContext);
    }

    public static com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(
        java.lang.String uuid, long userId, long folderId,
        java.lang.String name, java.lang.String title,
        java.lang.String description, java.lang.String extraSettings,
        byte[] bytes, com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService()
                   .addFileEntry(uuid, userId, folderId, name, title,
            description, extraSettings, bytes, serviceContext);
    }

    public static com.liferay.portlet.documentlibrary.model.DLFileEntry addFileEntry(
        java.lang.String uuid, long userId, long folderId,
        java.lang.String name, java.lang.String title,
        java.lang.String description, java.lang.String extraSettings,
        java.io.InputStream is, long size,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService()
                   .addFileEntry(uuid, userId, folderId, name, title,
            description, extraSettings, is, size, serviceContext);
    }

    public static void updateTagsAsset(long userId,
        com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry,
        java.lang.String[] tagsCategories, java.lang.String[] tagsEntries)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .updateTagsAsset(userId, fileEntry, tagsCategories, tagsEntries);
    }

    public static void addFileEntryResources(
        com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry,
        boolean addCommunityPermissions, boolean addGuestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addFileEntryResources(fileEntry, addCommunityPermissions,
            addGuestPermissions);
    }

    public static void addFileEntryResources(long fileEntryId,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addFileEntryResources(fileEntryId, communityPermissions,
            guestPermissions);
    }

    public static void addFileEntryResources(
        com.liferay.portlet.documentlibrary.model.DLFileEntry fileEntry,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addFileEntryResources(fileEntry, communityPermissions,
            guestPermissions);
    }

    public static void addFile(long companyId, java.lang.String portletId,
        long groupId, long repositoryId, java.lang.String fileName,
        long fileEntryId, java.lang.String properties,
        java.util.Date modifiedDate, java.lang.String[] tagsCategories,
        java.lang.String[] tagsEntries, java.io.InputStream is)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService()
            .addFile(companyId, portletId, groupId, repositoryId, fileName,
            fileEntryId, properties, modifiedDate, tagsCategories, tagsEntries,
            is);
    }

    public static int getGroupEntriesCount(long groupId)
        throws com.liferay.portal.SystemException {
        return getService().getGroupEntriesCount(groupId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> getGroupEntries(
        long groupId) throws com.liferay.portal.SystemException {
        return getService().getGroupEntries(groupId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getFreestylerImages(pk);
    }

    public static FreestylerEntryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("FreestylerEntryLocalService is not set");
        }

        return _service;
    }

    public void setService(FreestylerEntryLocalService service) {
        _service = service;
    }
    
	public static void deleteEntry(long entryId)
	throws com.liferay.portal.PortalException,
		com.liferay.portal.SystemException {
	getService().deleteEntry(entryId);
}
}
