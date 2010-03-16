package com.ext.portlet.freestyler.service.base;

import com.ext.portlet.freestyler.model.FreestylerImage;
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
import com.liferay.portal.service.ImageLocalService;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.ImagePersistence;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import com.liferay.portlet.expando.service.ExpandoValueService;
import com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence;
import com.liferay.portlet.tags.service.TagsAssetLocalService;
import com.liferay.portlet.tags.service.TagsAssetService;
import com.liferay.portlet.tags.service.TagsEntryLocalService;
import com.liferay.portlet.tags.service.TagsEntryService;
import com.liferay.portlet.tags.service.persistence.TagsAssetPersistence;
import com.liferay.portlet.tags.service.persistence.TagsEntryPersistence;

import java.util.List;


public abstract class FreestylerImageLocalServiceBaseImpl
    implements FreestylerImageLocalService {
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
    @BeanReference(name = "com.liferay.portlet.tags.service.TagsAssetLocalService.impl")
    protected TagsAssetLocalService tagsAssetLocalService;
    @BeanReference(name = "com.liferay.portlet.tags.service.TagsAssetService.impl")
    protected TagsAssetService tagsAssetService;
    @BeanReference(name = "com.liferay.portlet.tags.service.persistence.TagsAssetPersistence.impl")
    protected TagsAssetPersistence tagsAssetPersistence;
    @BeanReference(name = "com.liferay.portlet.tags.service.TagsEntryLocalService.impl")
    protected TagsEntryLocalService tagsEntryLocalService;
    @BeanReference(name = "com.liferay.portlet.tags.service.TagsEntryService.impl")
    protected TagsEntryService tagsEntryService;
    @BeanReference(name = "com.liferay.portlet.tags.service.persistence.TagsEntryPersistence.impl")
    protected TagsEntryPersistence tagsEntryPersistence;

    public FreestylerImage addFreestylerImage(FreestylerImage freestylerImage)
        throws SystemException {
        freestylerImage.setNew(true);

        return freestylerImagePersistence.update(freestylerImage, false);
    }

    public FreestylerImage createFreestylerImage(long imageId) {
        return freestylerImagePersistence.create(imageId);
    }

    public void deleteFreestylerImage(long imageId)
        throws PortalException, SystemException {
        freestylerImagePersistence.remove(imageId);
    }

    public void deleteFreestylerImage(FreestylerImage freestylerImage)
        throws SystemException {
        freestylerImagePersistence.remove(freestylerImage);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return freestylerImagePersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return freestylerImagePersistence.findWithDynamicQuery(dynamicQuery,
            start, end);
    }

    public FreestylerImage getFreestylerImage(long imageId)
        throws PortalException, SystemException {
        return freestylerImagePersistence.findByPrimaryKey(imageId);
    }

    public List<FreestylerImage> getFreestylerImages(int start, int end)
        throws SystemException {
        return freestylerImagePersistence.findAll(start, end);
    }

    public int getFreestylerImagesCount() throws SystemException {
        return freestylerImagePersistence.countAll();
    }

    public FreestylerImage updateFreestylerImage(
        FreestylerImage freestylerImage) throws SystemException {
        freestylerImage.setNew(false);

        return freestylerImagePersistence.update(freestylerImage, true);
    }

    public FreestylerImage updateFreestylerImage(
        FreestylerImage freestylerImage, boolean merge)
        throws SystemException {
        freestylerImage.setNew(false);

        return freestylerImagePersistence.update(freestylerImage, merge);
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

    public TagsAssetLocalService getTagsAssetLocalService() {
        return tagsAssetLocalService;
    }

    public void setTagsAssetLocalService(
        TagsAssetLocalService tagsAssetLocalService) {
        this.tagsAssetLocalService = tagsAssetLocalService;
    }

    public TagsAssetService getTagsAssetService() {
        return tagsAssetService;
    }

    public void setTagsAssetService(TagsAssetService tagsAssetService) {
        this.tagsAssetService = tagsAssetService;
    }

    public TagsAssetPersistence getTagsAssetPersistence() {
        return tagsAssetPersistence;
    }

    public void setTagsAssetPersistence(
        TagsAssetPersistence tagsAssetPersistence) {
        this.tagsAssetPersistence = tagsAssetPersistence;
    }

    public TagsEntryLocalService getTagsEntryLocalService() {
        return tagsEntryLocalService;
    }

    public void setTagsEntryLocalService(
        TagsEntryLocalService tagsEntryLocalService) {
        this.tagsEntryLocalService = tagsEntryLocalService;
    }

    public TagsEntryService getTagsEntryService() {
        return tagsEntryService;
    }

    public void setTagsEntryService(TagsEntryService tagsEntryService) {
        this.tagsEntryService = tagsEntryService;
    }

    public TagsEntryPersistence getTagsEntryPersistence() {
        return tagsEntryPersistence;
    }

    public void setTagsEntryPersistence(
        TagsEntryPersistence tagsEntryPersistence) {
        this.tagsEntryPersistence = tagsEntryPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
