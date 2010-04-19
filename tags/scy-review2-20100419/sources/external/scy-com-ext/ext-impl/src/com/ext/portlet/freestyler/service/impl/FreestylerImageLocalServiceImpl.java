package com.ext.portlet.freestyler.service.impl;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.base.FreestylerImageLocalServiceBaseImpl;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.image.ImageProcessor;
import com.liferay.portal.kernel.image.ImageProcessorUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ByteArrayMaker;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.imagegallery.DuplicateImageNameException;
import com.liferay.portlet.imagegallery.ImageNameException;
import com.liferay.portlet.imagegallery.ImageSizeException;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.util.Indexer;
import com.liferay.portlet.tags.model.TagsEntryConstants;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

public class FreestylerImageLocalServiceImpl extends FreestylerImageLocalServiceBaseImpl {

	@Override
	public FreestylerImage addImage(long userId, long folderId, String name, String description, File file, String contentType, ServiceContext serviceContext,
			FreestylerEntry entry) throws PortalException, SystemException {

		return addImage(null, userId, folderId, name, description, file, contentType, serviceContext, entry);
	}

	public FreestylerImage addImage(long userId, long folderId, String name, String description, String fileName, byte[] bytes, String contentType,
			ServiceContext serviceContext, FreestylerEntry entry) throws PortalException, SystemException {

		return addImage(null, userId, folderId, name, description, fileName, bytes, contentType, serviceContext, entry);
	}

	public FreestylerImage addImage(long userId, long folderId, String name, String description, String fileName, InputStream is, String contentType,
			ServiceContext serviceContext, FreestylerEntry entry) throws PortalException, SystemException {

		return addImage(null, userId, folderId, name, description, fileName, is, contentType, serviceContext, entry);
	}

	public FreestylerImage addImage(String uuid, long userId, long folderId, String name, String description, File file, String contentType,
			ServiceContext serviceContext, FreestylerEntry entry) throws PortalException, SystemException {

		try {
			String fileName = file.getName();
			byte[] bytes = FileUtil.getBytes(file);

			return addImage(uuid, userId, folderId, name, description, fileName, bytes, contentType, serviceContext, entry);
		} catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public FreestylerImage addImage(String uuid, long userId, long folderId, String name, String description, String fileName, byte[] bytes,
			String contentType, ServiceContext serviceContext, FreestylerEntry entry) throws PortalException, SystemException {

		try {

			// Image

			String extension = FileUtil.getExtension(fileName);

			if (Validator.isNotNull(name) && StringUtil.endsWith(name, extension)) {

				name = FileUtil.stripExtension(name);
			}

			String nameWithExtension = name + StringPool.PERIOD + extension;

			validate(folderId, nameWithExtension, fileName, bytes);

			User user = userPersistence.findByPrimaryKey(userId);
			FreestylerFolder folder = freestylerFolderPersistence.findByPrimaryKey(folderId);
			RenderedImage renderedImage = ImageProcessorUtil.read(bytes).getRenderedImage();
			Date now = new Date();

			long imageId = counterLocalService.increment();

			if (Validator.isNull(name)) {
				name = String.valueOf(imageId);
			}

			FreestylerImage image = freestylerImagePersistence.create(imageId);

			image.setGroupId(folder.getGroupId());
			image.setCompanyId(user.getCompanyId());
			image.setUserId(user.getUserId());
			image.setCreateDate(now);
			image.setFreestylerId(entry.getFreestylerId());
			image.setModifiedDate(now);
			image.setFolderId(folderId);
			image.setName(name);
			image.setDescription(description);
			image.setSmallImageId(counterLocalService.increment());
			image.setLargeImageId(counterLocalService.increment());

			if (PropsValues.IG_IMAGE_CUSTOM_1_MAX_DIMENSION > 0) {
				image.setCustom1ImageId(counterLocalService.increment());
			}

			if (PropsValues.IG_IMAGE_CUSTOM_2_MAX_DIMENSION > 0) {
				image.setCustom2ImageId(counterLocalService.increment());
			}

			freestylerImagePersistence.update(image, false);

			// Images

			saveImages(image.getLargeImageId(), renderedImage, image.getSmallImageId(), image.getCustom1ImageId(), image.getCustom2ImageId(), bytes,
					contentType);

			// Resources

			if (serviceContext.getAddCommunityPermissions() || serviceContext.getAddGuestPermissions()) {

				addImageResources(image, serviceContext.getAddCommunityPermissions(), serviceContext.getAddGuestPermissions());
			} else {
				addImageResources(image, serviceContext.getCommunityPermissions(), serviceContext.getGuestPermissions());
			}
			
			
			resourceLocalService.addResources(
					image.getCompanyId(), image.getGroupId(), image.getUserId(),
					"com.ext.portlet.freestyler", image.getGroupId(),
					false, true, false);
			

			// Expando

			ExpandoBridge expandoBridge = image.getExpandoBridge();

			expandoBridge.setAttributes(serviceContext);

			// Tags

			updateTagsAsset(userId, image, serviceContext.getTagsCategories(), serviceContext.getTagsEntries());

			// Indexer

			reIndex(image);

			return image;
		} catch (IOException ioe) {
			throw new ImageSizeException(ioe);
		}
	}

	public FreestylerImage addImage(String uuid, long userId, long folderId, String name, String description, String fileName, InputStream is,
			String contentType, ServiceContext serviceContext, FreestylerEntry entry) throws PortalException, SystemException {

		try {
			byte[] bytes = FileUtil.getBytes(is);

			return addImage(uuid, userId, folderId, name, description, fileName, bytes, contentType, serviceContext, entry);
		} catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public void addImageResources(long imageId, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {

		FreestylerImage image = freestylerImagePersistence.findByPrimaryKey(imageId);

		addImageResources(image, addCommunityPermissions, addGuestPermissions);
	}

	public void addImageResources(FreestylerImage image, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {

		resourceLocalService.addResources(image.getCompanyId(), image.getGroupId(), image.getUserId(), FreestylerImage.class.getName(), image.getImageId(), false,
				addCommunityPermissions, addGuestPermissions);
	}

	public void addImageResources(long imageId, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		FreestylerImage image = freestylerImagePersistence.findByPrimaryKey(imageId);

		addImageResources(image, communityPermissions, guestPermissions);
	}

	public void addImageResources(FreestylerImage image, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {

		resourceLocalService.addModelResources(image.getCompanyId(), image.getGroupId(), image.getUserId(), FreestylerImage.class.getName(), image.getImageId(),
				communityPermissions, guestPermissions);
	}

	public void reIndex(long imageId) throws SystemException {
		if (SearchEngineUtil.isIndexReadOnly()) {
			return;
		}

		FreestylerImage image = freestylerImagePersistence.fetchByPrimaryKey(imageId);

		if (image == null) {
			return;
		}

		reIndex(image);
	}

	public void reIndex(FreestylerImage image) throws SystemException {
		long companyId = image.getCompanyId();
		long groupId = image.getGroupId();
		long folderId = image.getFolderId();
		long imageId = image.getImageId();
		String name = image.getName();
		String description = image.getDescription();
		Date modifiedDate = image.getModifiedDate();

		String[] tagsCategories = tagsEntryLocalService.getEntryNames(IGImage.class.getName(), imageId, TagsEntryConstants.FOLKSONOMY_CATEGORY);
		String[] tagsEntries = tagsEntryLocalService.getEntryNames(IGImage.class.getName(), imageId);

		ExpandoBridge expandoBridge = image.getExpandoBridge();

		try {
			Indexer.updateImage(companyId, groupId, folderId, imageId, name, description, modifiedDate, tagsCategories, tagsEntries, expandoBridge);
		} catch (SearchException se) {
			_log.error("Reindexing " + imageId, se);
		}
	}

	protected void saveImages(long largeImageId, RenderedImage renderedImage, long smallImageId, long custom1ImageId, long custom2ImageId, byte[] bytes,
			String contentType) throws PortalException, SystemException {

		try {

			// Image

			imageLocalService.updateImage(largeImageId, bytes);

			// Thumbnail and custom sizes

			saveScaledImage(renderedImage, smallImageId, contentType, PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION));

			if (custom1ImageId > 0) {
				saveScaledImage(renderedImage, custom1ImageId, contentType, PropsValues.IG_IMAGE_CUSTOM_1_MAX_DIMENSION);
			}

			if (custom2ImageId > 0) {
				saveScaledImage(renderedImage, custom2ImageId, contentType, PropsValues.IG_IMAGE_CUSTOM_2_MAX_DIMENSION);
			}
		} catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	protected void saveScaledImage(RenderedImage renderedImage, long imageId, String contentType, int dimension) throws IOException, PortalException,
			SystemException {

		RenderedImage thumbnail = ImageProcessorUtil.scale(renderedImage, dimension, dimension);

		ByteArrayMaker bam = new ByteArrayMaker();

		if (contentType.indexOf("bmp") != -1) {
			ImageEncoder encoder = ImageCodec.createImageEncoder("BMP", bam, null);

			encoder.encode(thumbnail);
		} else if (contentType.indexOf("gif") != -1) {
			ImageProcessorUtil.encodeGIF(thumbnail, bam);
		} else if (contentType.indexOf("jpg") != -1 || contentType.indexOf("jpeg") != -1) {

			ImageIO.write(thumbnail, "jpeg", bam);
		} else if (contentType.indexOf("png") != -1) {
			ImageIO.write(thumbnail, "png", bam);
		} else if (contentType.indexOf("tif") != -1) {
			ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", bam, null);

			encoder.encode(thumbnail);
		}

		imageLocalService.updateImage(imageId, bam.toByteArray());
	}

	@Override
	public FreestylerImage updateFreestylerImage(long userId, long entryId, String name, String description, ServiceContext serviceContext)
			throws SystemException, PortalException {
		FreestylerImage entry = freestylerImagePersistence.findByPrimaryKey(entryId);

		if (Validator.isNull(description)) {
			description = "";
		}

		entry.setModifiedDate(new Date());
		entry.setName(name);
		entry.setDescription(description);

		freestylerImagePersistence.update(entry, false);

		// Tags

		updateTagsAsset(userId, entry, serviceContext.getTagsEntries());

		// Indexer

		return entry;
	}

	public void updateTagsAsset(long userId, FreestylerImage image, String[] tagsCategories, String[] tagsEntries) throws PortalException, SystemException {

		Image largeImage = imageLocalService.getImage(image.getLargeImageId());
		
		FreestylerEntry entry = freestylerEntryLocalService.getFreestylerEntry(image.getFreestylerId());

		if (largeImage == null) {
			return;
		}

		tagsAssetLocalService.updateAsset(userId, image.getGroupId(), FreestylerImage.class.getName(), image.getImageId(), tagsCategories, tagsEntries, true,
				null, null, null, null, "freestyler/" + largeImage.getType(), entry.getName() + " MULTIVALUE "  +image.getName(), image.getDescription(), null, null, largeImage.getHeight(),
				largeImage.getWidth(), null, false);
	}

	public void updateTagsAsset(long userId, FreestylerImage image, String[] tagsEntries) throws PortalException, SystemException {

		Image largeImage = imageLocalService.getImage(image.getLargeImageId());

		if (largeImage == null) {
			return;
		}

		tagsAssetLocalService.updateAsset(userId, image.getGroupId(), FreestylerImage.class.getName(), image.getImageId(), null, tagsEntries, true, null, null,
				null, null, "freestyler/" + largeImage.getType(), image.getName(), image.getDescription(), null, null, largeImage.getHeight(), largeImage
						.getWidth(), null, false);
	}

	protected void validate(byte[] bytes) throws ImageSizeException, SystemException {

		if ((PrefsPropsUtil.getLong(PropsKeys.IG_IMAGE_MAX_SIZE) > 0)
				&& ((bytes == null) || (bytes.length > PrefsPropsUtil.getLong(PropsKeys.IG_IMAGE_MAX_SIZE)))) {

			throw new ImageSizeException();
		}
	}

	protected void validate(long folderId, String nameWithExtension) throws PortalException, SystemException {

		if ((nameWithExtension.indexOf("\\\\") != -1) || (nameWithExtension.indexOf("//") != -1) || (nameWithExtension.indexOf(":") != -1)
				|| (nameWithExtension.indexOf("*") != -1) || (nameWithExtension.indexOf("?") != -1) || (nameWithExtension.indexOf("\"") != -1)
				|| (nameWithExtension.indexOf("<") != -1) || (nameWithExtension.indexOf(">") != -1) || (nameWithExtension.indexOf("|") != -1)
				|| (nameWithExtension.indexOf("&") != -1) || (nameWithExtension.indexOf("[") != -1) || (nameWithExtension.indexOf("]") != -1)
				|| (nameWithExtension.indexOf("'") != -1)) {

			throw new ImageNameException();
		}

		boolean validImageExtension = false;

		String[] imageExtensions = PrefsPropsUtil.getStringArray(PropsKeys.IG_IMAGE_EXTENSIONS, StringPool.COMMA);

		for (int i = 0; i < imageExtensions.length; i++) {
			if (StringPool.STAR.equals(imageExtensions[i]) || StringUtil.endsWith(nameWithExtension, imageExtensions[i])) {

				validImageExtension = true;

				break;
			}
		}

		if (!validImageExtension) {
			throw new ImageNameException();
		}

		String name = FileUtil.stripExtension(nameWithExtension);
		String imageType = FileUtil.getExtension(nameWithExtension);

		List<FreestylerImage> images = freestylerImagePersistence.findByF_N(folderId, name);

		if (imageType.equals("jpeg")) {
			imageType = ImageProcessor.TYPE_JPEG;
		} else if (imageType.equals("tif")) {
			imageType = ImageProcessor.TYPE_TIFF;
		}

		for (FreestylerImage image : images) {
			if (imageType.equals(image.getImageType())) {
				throw new DuplicateImageNameException();
			}
		}
	}

	protected void validate(long folderId, String nameWithExtension, String fileName, byte[] bytes) throws PortalException, SystemException {

		if (Validator.isNotNull(fileName)) {
			String extension = FileUtil.getExtension(fileName);

			if (Validator.isNull(nameWithExtension)) {
				nameWithExtension = fileName;
			} else if (!StringUtil.endsWith(nameWithExtension, extension)) {
				throw new ImageNameException();
			}
		}

		validate(folderId, nameWithExtension);
		validate(bytes);
	}

	private static Log _log = LogFactoryUtil.getLog(FreestylerImageLocalServiceImpl.class);

	public List<FreestylerImage> getImages(long folderId) throws SystemException {
		return freestylerImagePersistence.findByFolderId(folderId);
	}

}
