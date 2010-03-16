package com.ext.portlet.freestyler.model.impl;

import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Image;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.imagegallery.model.impl.IGImageImpl;

public class FreestylerImageImpl extends FreestylerImageModelImpl implements FreestylerImage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1570189671013315937L;

	public FreestylerImageImpl() {
	}

	public static String getNameWithExtension(String name, String type) {
		if (Validator.isNotNull(type)) {
			name += StringPool.PERIOD + type;
		}

		return name;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public FreestylerFolder getFolder() {
		FreestylerFolder folder = null;

		try {
			folder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(getFolderId());
		} catch (Exception e) {
			folder = new FreestylerFolderImpl();

			_log.error(e);
		}

		return folder;
	}

	public String getNameWithExtension() {
		String nameWithExtension = getName();

		if (Validator.isNull(nameWithExtension)) {
			nameWithExtension = String.valueOf(getImageId());
		}

		String type = getImageType();

		return getNameWithExtension(nameWithExtension, type);
	}

	public String getImageType() {
		if (_imageType == null) {
			try {
				Image largeImage = ImageLocalServiceUtil.getImage(getLargeImageId());

				_imageType = largeImage.getType();
			} catch (Exception e) {
				_imageType = StringPool.BLANK;

				_log.error(e);
			}
		}

		return _imageType;
	}

	public void setImageType(String imageType) {
		_imageType = imageType;
	}

	public int getImageSize() {
		if (_imageSize == null) {
			try {
				Image largeImage = ImageLocalServiceUtil.getImage(getLargeImageId());

				_imageSize = new Integer(largeImage.getSize());
			} catch (Exception e) {
				_imageSize = new Integer(0);

				_log.error(e);
			}
		}

		return _imageSize.intValue();
	}

	private static Log _log = LogFactoryUtil.getLog(IGImageImpl.class);

	private String _userUuid;
	private String _imageType;
	private Integer _imageSize;

}
