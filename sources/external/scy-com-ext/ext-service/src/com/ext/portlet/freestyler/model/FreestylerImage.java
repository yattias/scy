package com.ext.portlet.freestyler.model;

import com.ext.portlet.resourcehandling.interfaces.MultivalueResource;

/**
 * The images from freestyler entrie composition. Each Image has his own tags
 * asset.
 * 
 * @author Daniel
 * 
 */
public interface FreestylerImage extends FreestylerImageModel, MultivalueResource {
	public java.lang.String getUserUuid() throws com.liferay.portal.SystemException;

	public void setUserUuid(java.lang.String userUuid);

	public com.ext.portlet.freestyler.model.FreestylerFolder getFolder();

	public java.lang.String getNameWithExtension();

	public java.lang.String getImageType();

	public void setImageType(java.lang.String imageType);

	public int getImageSize();
}
