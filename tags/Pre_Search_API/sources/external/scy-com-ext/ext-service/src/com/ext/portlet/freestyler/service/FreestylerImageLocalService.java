package com.ext.portlet.freestyler.service;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="FreestylerImageLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.ext.portlet.freestyler.service.impl.FreestylerImageLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil
 *
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface FreestylerImageLocalService {
    public com.ext.portlet.freestyler.model.FreestylerImage addFreestylerImage(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage createFreestylerImage(
        long imageId);

    public void deleteFreestylerImage(long imageId)
        throws com.liferay.portal.SystemException,
            com.liferay.portal.PortalException;

    public void deleteFreestylerImage(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.freestyler.model.FreestylerImage getFreestylerImage(
        long imageId)
        throws com.liferay.portal.SystemException,
            com.liferay.portal.PortalException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getFreestylerImagesCount()
        throws com.liferay.portal.SystemException;
    
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getImages(
		long folderId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws com.liferay.portal.SystemException;
    
	public com.ext.portlet.freestyler.model.FreestylerImage updateFreestylerImage(long userId, long entryId, String name, String description,
			com.liferay.portal.service.ServiceContext serviceContext) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        long userId, long folderId, java.lang.String name,
        java.lang.String description, java.io.File file,
        java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        long userId, long folderId, java.lang.String name,
        java.lang.String description, java.lang.String fileName, byte[] bytes,
        java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        long userId, long folderId, java.lang.String name,
        java.lang.String description, java.lang.String fileName,
        java.io.InputStream is, java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        java.lang.String uuid, long userId, long folderId,
        java.lang.String name, java.lang.String description, java.io.File file,
        java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        java.lang.String uuid, long userId, long folderId,
        java.lang.String name, java.lang.String description,
        java.lang.String fileName, byte[] bytes, java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage addImage(
        java.lang.String uuid, long userId, long folderId,
        java.lang.String name, java.lang.String description,
        java.lang.String fileName, java.io.InputStream is,
        java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext, FreestylerEntry entry)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void addImageResources(long imageId,
        boolean addCommunityPermissions, boolean addGuestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void addImageResources(
        com.ext.portlet.freestyler.model.FreestylerImage image,
        boolean addCommunityPermissions, boolean addGuestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void addImageResources(long imageId,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void addImageResources(
        com.ext.portlet.freestyler.model.FreestylerImage image,
        java.lang.String[] communityPermissions,
        java.lang.String[] guestPermissions)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void reIndex(long imageId) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void reIndex(com.ext.portlet.freestyler.model.FreestylerImage image)
        throws com.liferay.portal.SystemException;

    public void updateTagsAsset(long userId,
        com.ext.portlet.freestyler.model.FreestylerImage image,
        java.lang.String[] tagsCategories, java.lang.String[] tagsEntries)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;
}
