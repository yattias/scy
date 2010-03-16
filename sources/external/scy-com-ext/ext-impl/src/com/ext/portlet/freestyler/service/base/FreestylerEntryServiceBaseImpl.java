package com.ext.portlet.freestyler.service.base;

import com.ext.portlet.freestyler.service.FreestylerEntryLocalService;
import com.ext.portlet.freestyler.service.FreestylerEntryService;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalService;
import com.ext.portlet.freestyler.service.FreestylerFolderService;
import com.ext.portlet.freestyler.service.FreestylerImageLocalService;
import com.ext.portlet.freestyler.service.FreestylerImageService;
import com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence;
import com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence;
import com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.GroupService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.service.persistence.GroupPersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;


public abstract class FreestylerEntryServiceBaseImpl extends PrincipalBean
    implements FreestylerEntryService {
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
