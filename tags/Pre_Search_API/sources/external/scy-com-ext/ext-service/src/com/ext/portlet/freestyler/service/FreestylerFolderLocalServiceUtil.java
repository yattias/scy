package com.ext.portlet.freestyler.service;

/**
 * <a href="FreestylerFolderLocalServiceUtil.java.html"><b><i>View
 * Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.freestyler.service.FreestylerFolderLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * 
 * @see com.ext.portlet.freestyler.service.FreestylerFolderLocalService
 * 
 */
public class FreestylerFolderLocalServiceUtil {
	private static FreestylerFolderLocalService _service;

	public static com.ext.portlet.freestyler.model.FreestylerFolder addFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
			throws com.liferay.portal.SystemException {
		return getService().addFreestylerFolder(freestylerFolder);
	}

	public static com.ext.portlet.freestyler.model.FreestylerFolder createFreestylerFolder(long folderId) {
		return getService().createFreestylerFolder(folderId);
	}

	public static void deleteFreestylerFolder(long folderId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().deleteFreestylerFolder(folderId);
	}

	public static void deleteFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder) throws com.liferay.portal.SystemException {
		getService().deleteFreestylerFolder(freestylerFolder);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	public static com.ext.portlet.freestyler.model.FreestylerFolder getFreestylerFolder(long folderId) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return getService().getFreestylerFolder(folderId);
	}

	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFreestylerFolders(int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().getFreestylerFolders(start, end);
	}

	public static int getFreestylerFoldersCount() throws com.liferay.portal.SystemException {
		return getService().getFreestylerFoldersCount();
	}

	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId) throws com.liferay.portal.SystemException {
		return getService().getFolders(groupId);
	}

	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId, long parentFolderId)
			throws com.liferay.portal.SystemException {
		return getService().getFolders(groupId, parentFolderId);
	}

	public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId, long parentFolderId, int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().getFolders(groupId, parentFolderId, start, end);
	}

	public static int getFoldersCount(long groupId, long parentFolderId) throws com.liferay.portal.SystemException {
		return getService().getFoldersCount(groupId, parentFolderId);
	}

	public static com.ext.portlet.freestyler.model.FreestylerFolder updateFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
			throws com.liferay.portal.SystemException {
		return getService().updateFreestylerFolder(freestylerFolder);
	}

	public static com.ext.portlet.freestyler.model.FreestylerFolder updateFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
			boolean merge) throws com.liferay.portal.SystemException {
		return getService().updateFreestylerFolder(freestylerFolder, merge);
	}

	public static FreestylerFolderLocalService getService() {
		if (_service == null) {
			throw new RuntimeException("FreestylerFolderLocalService is not set");
		}

		return _service;
	}

	public void setService(FreestylerFolderLocalService service) {
		_service = service;
	}
}
