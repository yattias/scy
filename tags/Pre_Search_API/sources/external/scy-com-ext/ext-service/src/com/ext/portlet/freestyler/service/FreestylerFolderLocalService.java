package com.ext.portlet.freestyler.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;

/**
 * <a href="FreestylerFolderLocalService.java.html"><b><i>View
 * Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.ext.portlet.freestyler.service.impl.FreestylerFolderLocalServiceImpl</code>
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
 * @see com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil
 * 
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor = { PortalException.class, SystemException.class })
public interface FreestylerFolderLocalService {
	public com.ext.portlet.freestyler.model.FreestylerFolder addFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
			throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerFolder createFreestylerFolder(long folderId);

	public void deleteFreestylerFolder(long folderId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

	public void deleteFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.ext.portlet.freestyler.model.FreestylerFolder getFreestylerFolder(long folderId) throws com.liferay.portal.SystemException,
			com.liferay.portal.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFreestylerFolders(int start, int end) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId, long parentFolderId)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> getFolders(long groupId, long parentFolderId, int start, int end)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getFoldersCount(long groupId, long parentFolderId) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getFreestylerFoldersCount() throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerFolder updateFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
			throws com.liferay.portal.SystemException;

	public com.ext.portlet.freestyler.model.FreestylerFolder updateFreestylerFolder(com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
			boolean merge) throws com.liferay.portal.SystemException;
}
