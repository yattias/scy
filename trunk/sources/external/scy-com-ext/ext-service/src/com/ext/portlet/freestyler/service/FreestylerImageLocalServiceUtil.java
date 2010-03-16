package com.ext.portlet.freestyler.service;

import com.ext.portlet.freestyler.model.FreestylerEntry;

/**
 * <a href="FreestylerImageLocalServiceUtil.java.html"><b><i>View
 * Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.freestyler.service.FreestylerImageLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * 
 * @see com.ext.portlet.freestyler.service.FreestylerImageLocalService
 * 
 */
public class FreestylerImageLocalServiceUtil {
	private static FreestylerImageLocalService _service;

	public static com.ext.portlet.freestyler.model.FreestylerImage addFreestylerImage(com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
			throws com.liferay.portal.SystemException {
		return getService().addFreestylerImage(freestylerImage);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage createFreestylerImage(long imageId) {
		return getService().createFreestylerImage(imageId);
	}

	public static void deleteFreestylerImage(long imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().deleteFreestylerImage(imageId);
	}

	public static void deleteFreestylerImage(com.ext.portlet.freestyler.model.FreestylerImage freestylerImage) throws com.liferay.portal.SystemException {
		getService().deleteFreestylerImage(freestylerImage);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage getFreestylerImage(long imageId) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return getService().getFreestylerImage(imageId);
	}

	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().getFreestylerImages(start, end);
	}

	public static int getFreestylerImagesCount() throws com.liferay.portal.SystemException {
		return getService().getFreestylerImagesCount();
	}
	
	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getImages(
			long folderId) throws com.liferay.portal.SystemException {
			return getService().getImages(folderId);
		}

	public static com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
			throws com.liferay.portal.SystemException {
		return getService().updateFreestylerImage(freestylerImage);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
			boolean merge) throws com.liferay.portal.SystemException {
		return getService().updateFreestylerImage(freestylerImage, merge);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(long userId, long entryId, String name, String description,
			com.liferay.portal.service.ServiceContext serviceContext) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
		return getService().updateFreestylerImage(userId, entryId, name, description, serviceContext);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(long userId, long folderId, java.lang.String name, java.lang.String description,
			java.io.File file, java.lang.String contentType, com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		return getService().addImage(userId, folderId, name, description, file, contentType, serviceContext, entry);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(long userId, long folderId, java.lang.String name, java.lang.String description,
			java.lang.String fileName, byte[] bytes, java.lang.String contentType, com.liferay.portal.service.ServiceContext serviceContext,
			FreestylerEntry entry) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		return getService().addImage(userId, folderId, name, description, fileName, bytes, contentType, serviceContext, entry);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(long userId, long folderId, java.lang.String name, java.lang.String description,
			java.lang.String fileName, java.io.InputStream is, java.lang.String contentType, com.liferay.portal.service.ServiceContext serviceContext,
			FreestylerEntry entry) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		return getService().addImage(userId, folderId, name, description, fileName, is, contentType, serviceContext, entry);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(java.lang.String uuid, long userId, long folderId, java.lang.String name,
			java.lang.String description, java.io.File file, java.lang.String contentType, com.liferay.portal.service.ServiceContext serviceContext,
			FreestylerEntry entry) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		return getService().addImage(uuid, userId, folderId, name, description, file, contentType, serviceContext, entry);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(java.lang.String uuid, long userId, long folderId, java.lang.String name,
			java.lang.String description, java.lang.String fileName, byte[] bytes, java.lang.String contentType,
			com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return getService().addImage(uuid, userId, folderId, name, description, fileName, bytes, contentType, serviceContext, entry);
	}

	public static com.ext.portlet.freestyler.model.FreestylerImage addImage(java.lang.String uuid, long userId, long folderId, java.lang.String name,
			java.lang.String description, java.lang.String fileName, java.io.InputStream is, java.lang.String contentType,
			com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return getService().addImage(uuid, userId, folderId, name, description, fileName, is, contentType, serviceContext, entry);
	}

	public static void addImageResources(long imageId, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		getService().addImageResources(imageId, addCommunityPermissions, addGuestPermissions);
	}

	public static void addImageResources(com.ext.portlet.freestyler.model.FreestylerImage image, boolean addCommunityPermissions, boolean addGuestPermissions)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().addImageResources(image, addCommunityPermissions, addGuestPermissions);
	}

	public static void addImageResources(long imageId, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().addImageResources(imageId, communityPermissions, guestPermissions);
	}

	public static void addImageResources(com.ext.portlet.freestyler.model.FreestylerImage image, java.lang.String[] communityPermissions,
			java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().addImageResources(image, communityPermissions, guestPermissions);
	}

	public static void reIndex(long imageId) throws com.liferay.portal.SystemException {
		getService().reIndex(imageId);
	}

	public static void reIndex(com.ext.portlet.freestyler.model.FreestylerImage image) throws com.liferay.portal.SystemException {
		getService().reIndex(image);
	}

	public static void updateTagsAsset(long userId, com.ext.portlet.freestyler.model.FreestylerImage image, java.lang.String[] tagsCategories,
			java.lang.String[] tagsEntries) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().updateTagsAsset(userId, image, tagsCategories, tagsEntries);
	}

	public static FreestylerImageLocalService getService() {
		if (_service == null) {
			throw new RuntimeException("FreestylerImageLocalService is not set");
		}

		return _service;
	}

	public void setService(FreestylerImageLocalService service) {
		_service = service;
	}
}
