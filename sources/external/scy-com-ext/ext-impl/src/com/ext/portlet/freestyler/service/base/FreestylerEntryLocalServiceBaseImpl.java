package com.ext.portlet.freestyler.service.base;

import java.util.List;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalService;
import com.ext.portlet.freestyler.service.FreestylerEntryService;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalService;
import com.ext.portlet.freestyler.service.FreestylerFolderService;
import com.ext.portlet.freestyler.service.FreestylerImageLocalService;
import com.ext.portlet.freestyler.service.FreestylerImageService;
import com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence;
import com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence;
import com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence;
import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterService;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyService;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.GroupService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.GroupPersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalService;
import com.liferay.portlet.documentlibrary.service.DLFileEntryService;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalService;
import com.liferay.portlet.documentlibrary.service.DLFolderService;
import com.liferay.portlet.documentlibrary.service.persistence.DLFileEntryPersistence;
import com.liferay.portlet.documentlibrary.service.persistence.DLFolderFinder;
import com.liferay.portlet.documentlibrary.service.persistence.DLFolderPersistence;
import com.liferay.portlet.tags.service.TagsAssetLocalService;
import com.liferay.portlet.tags.service.TagsAssetService;
import com.liferay.portlet.tags.service.persistence.TagsAssetPersistence;


public abstract class FreestylerEntryLocalServiceBaseImpl
    implements FreestylerEntryLocalService {
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerFolderLocalService.impl")
    protected FreestylerFolderLocalService freestylerFolderLocalService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerFolderService.impl")
    protected FreestylerFolderService freestylerFolderService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence.impl")
    protected FreestylerFolderPersistence freestylerFolderPersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerImageLocalService.impl")
    protected FreestylerImageLocalService freestylerImageLocalService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerImageService.impl")
    protected FreestylerImageService freestylerImageService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence.impl")
    protected FreestylerImagePersistence freestylerImagePersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerEntryLocalService.impl")
    protected FreestylerEntryLocalService freestylerEntryLocalService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.FreestylerEntryService.impl")
    protected FreestylerEntryService freestylerEntryService;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence.impl")
    protected FreestylerEntryPersistence freestylerEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.GroupLocalService.impl")
    protected GroupLocalService groupLocalService;
    @BeanReference(name = "com.liferay.portal.service.GroupService.impl")
    protected GroupService groupService;
    @BeanReference(name = "com.liferay.portal.service.persistence.GroupPersistence.impl")
    protected GroupPersistence groupPersistence;
    @BeanReference(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;
    @BeanReference(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;
	@BeanReference(name = "com.liferay.portal.service.ResourceLocalService.impl")
	protected ResourceLocalService resourceLocalService;
	@BeanReference(name = "com.liferay.portal.service.ResourceService.impl")
	protected ResourceService resourceService;
	@BeanReference(name = "com.liferay.counter.service.CounterLocalService.impl")
	protected CounterLocalService counterLocalService;
	@BeanReference(name = "com.liferay.counter.service.CounterService.impl")
	protected CounterService counterService;
	@BeanReference(name = "com.liferay.portal.service.CompanyLocalService.impl")
	protected CompanyLocalService companyLocalService;
	@BeanReference(name = "com.liferay.portal.service.CompanyService.impl")
	protected CompanyService companyService;
	@BeanReference(name = "com.liferay.portlet.tags.service.TagsAssetLocalService.impl")
	protected TagsAssetLocalService tagsAssetLocalService;
	@BeanReference(name = "com.liferay.portlet.tags.service.TagsAssetService.impl")
	protected TagsAssetService tagsAssetService;
	@BeanReference(name = "com.liferay.portlet.tags.service.persistence.TagsAssetPersistence.impl")
	protected TagsAssetPersistence tagsAssetPersistence;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.DLFolderLocalService.impl")
	protected DLFolderLocalService dlFolderLocalService;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.DLFolderService.impl")
	protected DLFolderService dlFolderService;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.persistence.DLFolderPersistence.impl")
	protected DLFolderPersistence dlFolderPersistence;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.persistence.DLFolderFinder.impl")
	protected DLFolderFinder dlFolderFinder;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.DLFileEntryLocalService.impl")
	protected DLFileEntryLocalService dlFileEntryLocalService;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.DLFileEntryService.impl")
	protected DLFileEntryService dlFileEntryService;
	@BeanReference(name = "com.liferay.portlet.documentlibrary.service.persistence.DLFileEntryPersistence.impl")
	protected DLFileEntryPersistence dlFileEntryPersistence;

    public FreestylerEntry addFreestylerEntry(FreestylerEntry freestylerEntry)
        throws SystemException {
        freestylerEntry.setNew(true);

        return freestylerEntryPersistence.update(freestylerEntry, false);
    }

    public FreestylerEntry createFreestylerEntry(long freestylerId) {
        return freestylerEntryPersistence.create(freestylerId);
    }

    public void deleteFreestylerEntry(long freestylerId)
        throws PortalException, SystemException {
        freestylerEntryPersistence.remove(freestylerId);
    }

    public void deleteFreestylerEntry(FreestylerEntry freestylerEntry)
        throws SystemException {
        freestylerEntryPersistence.remove(freestylerEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return freestylerEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return freestylerEntryPersistence.findWithDynamicQuery(dynamicQuery,
            start, end);
    }

    public FreestylerEntry getFreestylerEntry(long freestylerId)
        throws PortalException, SystemException {
        return freestylerEntryPersistence.findByPrimaryKey(freestylerId);
    }

    public List<FreestylerEntry> getFreestylerEntries(int start, int end)
        throws SystemException {
        return freestylerEntryPersistence.findAll(start, end);
    }

    public int getFreestylerEntriesCount() throws SystemException {
        return freestylerEntryPersistence.countAll();
    }

    public FreestylerEntry updateFreestylerEntry(
        FreestylerEntry freestylerEntry) throws SystemException {
        freestylerEntry.setNew(false);

        return freestylerEntryPersistence.update(freestylerEntry, true);
    }

    public FreestylerEntry updateFreestylerEntry(
        FreestylerEntry freestylerEntry, boolean merge)
        throws SystemException {
        freestylerEntry.setNew(false);

        return freestylerEntryPersistence.update(freestylerEntry, merge);
    }

    public FreestylerFolderLocalService getFreestylerFolderLocalService() {
        return freestylerFolderLocalService;
    }

    public void setFreestylerFolderLocalService(
        FreestylerFolderLocalService freestylerFolderLocalService) {
        this.freestylerFolderLocalService = freestylerFolderLocalService;
    }

    public FreestylerFolderService getFreestylerFolderService() {
        return freestylerFolderService;
    }

    public void setFreestylerFolderService(
        FreestylerFolderService freestylerFolderService) {
        this.freestylerFolderService = freestylerFolderService;
    }

    public FreestylerFolderPersistence getFreestylerFolderPersistence() {
        return freestylerFolderPersistence;
    }

    public void setFreestylerFolderPersistence(
        FreestylerFolderPersistence freestylerFolderPersistence) {
        this.freestylerFolderPersistence = freestylerFolderPersistence;
    }

    public FreestylerImageLocalService getFreestylerImageLocalService() {
        return freestylerImageLocalService;
    }

    public void setFreestylerImageLocalService(
        FreestylerImageLocalService freestylerImageLocalService) {
        this.freestylerImageLocalService = freestylerImageLocalService;
    }

    public FreestylerImageService getFreestylerImageService() {
        return freestylerImageService;
    }

    public void setFreestylerImageService(
        FreestylerImageService freestylerImageService) {
        this.freestylerImageService = freestylerImageService;
    }

    public FreestylerImagePersistence getFreestylerImagePersistence() {
        return freestylerImagePersistence;
    }

    public void setFreestylerImagePersistence(
        FreestylerImagePersistence freestylerImagePersistence) {
        this.freestylerImagePersistence = freestylerImagePersistence;
    }

    public FreestylerEntryLocalService getFreestylerEntryLocalService() {
        return freestylerEntryLocalService;
    }

    public void setFreestylerEntryLocalService(
        FreestylerEntryLocalService freestylerEntryLocalService) {
        this.freestylerEntryLocalService = freestylerEntryLocalService;
    }

    public FreestylerEntryService getFreestylerEntryService() {
        return freestylerEntryService;
    }

    public void setFreestylerEntryService(
        FreestylerEntryService freestylerEntryService) {
        this.freestylerEntryService = freestylerEntryService;
    }

    public FreestylerEntryPersistence getFreestylerEntryPersistence() {
        return freestylerEntryPersistence;
    }

    public void setFreestylerEntryPersistence(
        FreestylerEntryPersistence freestylerEntryPersistence) {
        this.freestylerEntryPersistence = freestylerEntryPersistence;
    }

    public GroupLocalService getGroupLocalService() {
        return groupLocalService;
    }

    public void setGroupLocalService(GroupLocalService groupLocalService) {
        this.groupLocalService = groupLocalService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public GroupPersistence getGroupPersistence() {
        return groupPersistence;
    }

    public void setGroupPersistence(GroupPersistence groupPersistence) {
        this.groupPersistence = groupPersistence;
    }

    public UserLocalService getUserLocalService() {
        return userLocalService;
    }

    public void setUserLocalService(UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserPersistence getUserPersistence() {
        return userPersistence;
    }

    public void setUserPersistence(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
