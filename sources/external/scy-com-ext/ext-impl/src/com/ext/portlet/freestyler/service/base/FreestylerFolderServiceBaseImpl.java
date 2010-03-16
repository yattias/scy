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

import com.liferay.counter.service.CounterLocalService;
import com.liferay.counter.service.CounterService;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.ImageLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.service.LayoutService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.service.persistence.ImagePersistence;
import com.liferay.portal.service.persistence.LayoutPersistence;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import com.liferay.portlet.expando.service.ExpandoValueService;
import com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence;


public abstract class FreestylerFolderServiceBaseImpl extends PrincipalBean
    implements FreestylerFolderService {
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
    @BeanReference(name = "com.liferay.counter.service.CounterLocalService.impl")
    protected CounterLocalService counterLocalService;
    @BeanReference(name = "com.liferay.counter.service.CounterService.impl")
    protected CounterService counterService;
    @BeanReference(name = "com.liferay.portal.service.ImageLocalService.impl")
    protected ImageLocalService imageLocalService;
    @BeanReference(name = "com.liferay.portal.service.persistence.ImagePersistence.impl")
    protected ImagePersistence imagePersistence;
    @BeanReference(name = "com.liferay.portal.service.LayoutLocalService.impl")
    protected LayoutLocalService layoutLocalService;
    @BeanReference(name = "com.liferay.portal.service.LayoutService.impl")
    protected LayoutService layoutService;
    @BeanReference(name = "com.liferay.portal.service.persistence.LayoutPersistence.impl")
    protected LayoutPersistence layoutPersistence;
    @BeanReference(name = "com.liferay.portal.service.ResourceLocalService.impl")
    protected ResourceLocalService resourceLocalService;
    @BeanReference(name = "com.liferay.portal.service.ResourceService.impl")
    protected ResourceService resourceService;
    @BeanReference(name = "com.liferay.portal.service.persistence.ResourcePersistence.impl")
    protected ResourcePersistence resourcePersistence;
    @BeanReference(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;
    @BeanReference(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;
    @BeanReference(name = "com.liferay.portlet.expando.service.ExpandoValueLocalService.impl")
    protected ExpandoValueLocalService expandoValueLocalService;
    @BeanReference(name = "com.liferay.portlet.expando.service.ExpandoValueService.impl")
    protected ExpandoValueService expandoValueService;
    @BeanReference(name = "com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence.impl")
    protected ExpandoValuePersistence expandoValuePersistence;

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

    public CounterLocalService getCounterLocalService() {
        return counterLocalService;
    }

    public void setCounterLocalService(CounterLocalService counterLocalService) {
        this.counterLocalService = counterLocalService;
    }

    public CounterService getCounterService() {
        return counterService;
    }

    public void setCounterService(CounterService counterService) {
        this.counterService = counterService;
    }

    public ImageLocalService getImageLocalService() {
        return imageLocalService;
    }

    public void setImageLocalService(ImageLocalService imageLocalService) {
        this.imageLocalService = imageLocalService;
    }

    public ImagePersistence getImagePersistence() {
        return imagePersistence;
    }

    public void setImagePersistence(ImagePersistence imagePersistence) {
        this.imagePersistence = imagePersistence;
    }

    public LayoutLocalService getLayoutLocalService() {
        return layoutLocalService;
    }

    public void setLayoutLocalService(LayoutLocalService layoutLocalService) {
        this.layoutLocalService = layoutLocalService;
    }

    public LayoutService getLayoutService() {
        return layoutService;
    }

    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    public LayoutPersistence getLayoutPersistence() {
        return layoutPersistence;
    }

    public void setLayoutPersistence(LayoutPersistence layoutPersistence) {
        this.layoutPersistence = layoutPersistence;
    }

    public ResourceLocalService getResourceLocalService() {
        return resourceLocalService;
    }

    public void setResourceLocalService(
        ResourceLocalService resourceLocalService) {
        this.resourceLocalService = resourceLocalService;
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public ResourcePersistence getResourcePersistence() {
        return resourcePersistence;
    }

    public void setResourcePersistence(ResourcePersistence resourcePersistence) {
        this.resourcePersistence = resourcePersistence;
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

    public ExpandoValueLocalService getExpandoValueLocalService() {
        return expandoValueLocalService;
    }

    public void setExpandoValueLocalService(
        ExpandoValueLocalService expandoValueLocalService) {
        this.expandoValueLocalService = expandoValueLocalService;
    }

    public ExpandoValueService getExpandoValueService() {
        return expandoValueService;
    }

    public void setExpandoValueService(ExpandoValueService expandoValueService) {
        this.expandoValueService = expandoValueService;
    }

    public ExpandoValuePersistence getExpandoValuePersistence() {
        return expandoValuePersistence;
    }

    public void setExpandoValuePersistence(
        ExpandoValuePersistence expandoValuePersistence) {
        this.expandoValuePersistence = expandoValuePersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
