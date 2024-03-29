/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.imagegallery.action;

import java.io.File;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.ContentTypeUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.imagegallery.DuplicateImageNameException;
import com.liferay.portlet.imagegallery.ImageNameException;
import com.liferay.portlet.imagegallery.ImageSizeException;
import com.liferay.portlet.imagegallery.NoSuchFolderException;
import com.liferay.portlet.imagegallery.NoSuchImageException;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGImageServiceUtil;
import com.liferay.portlet.tags.TagsEntryException;
import com.liferay.util.MetadataActionUtil;

/**
 * Extended this class in updateImage method. After create/update new resource
 * also new metadata entry would be create. After delete also metadata will be
 * delete.
 * 
 * @author Daniel
 * 
 */
public class EditImageAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateImage(actionRequest);
			} else if (cmd.equals(Constants.DELETE)) {
				deleteImage(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		} catch (Exception e) {
			if (e instanceof NoSuchImageException || e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass().getName());

				setForward(actionRequest, "portlet.image_gallery.error");
			} else if (e instanceof DuplicateImageNameException || e instanceof ImageNameException || e instanceof ImageSizeException
					|| e instanceof NoSuchFolderException) {

				SessionErrors.add(actionRequest, e.getClass().getName());
			} else if (e instanceof TagsEntryException) {
				SessionErrors.add(actionRequest, e.getClass().getName(), e);
			} else {
				throw e;
			}
		}
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		try {
			ActionUtil.getImage(renderRequest);
		} catch (Exception e) {
			if (e instanceof NoSuchImageException || e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass().getName());

				return mapping.findForward("portlet.image_gallery.error");
			} else {
				throw e;
			}
		}

		String forward = "portlet.image_gallery.edit_image";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	protected void deleteImage(ActionRequest actionRequest) throws Exception {
		long imageId = ParamUtil.getLong(actionRequest, "imageId");

		IGImageServiceUtil.deleteImage(imageId);
		MetadataActionUtil.deleteMetadata(imageId);
	}

	protected String getContentType(UploadPortletRequest uploadRequest, File file) {

		String contentType = GetterUtil.getString(uploadRequest.getContentType("file"));

		if (contentType.equals("application/octet-stream")) {
			String ext = GetterUtil.getString(FileUtil.getExtension(file.getName())).toLowerCase();

			if (Validator.isNotNull(ext)) {
				contentType = ContentTypeUtil.getContentType(ext);
			}
		}

		return contentType;
	}

	protected void updateImage(ActionRequest actionRequest) throws Exception {
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);

		long imageId = ParamUtil.getLong(uploadRequest, "imageId");

		long folderId = ParamUtil.getLong(uploadRequest, "folderId");
		String name = ParamUtil.getString(uploadRequest, "name");
		String fileName = uploadRequest.getFileName("file");
		String description = ParamUtil.getString(uploadRequest, "description", fileName);

		File file = uploadRequest.getFile("file");
		String contentType = getContentType(uploadRequest, file);

		if (contentType.equals("application/octet-stream")) {
			String ext = GetterUtil.getString(FileUtil.getExtension(file.getName())).toLowerCase();

			if (Validator.isNotNull(ext)) {
				contentType = ContentTypeUtil.getContentType(ext);
			}
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(IGImage.class.getName(), actionRequest);

		if (imageId <= 0) {

			// Add image

			if (Validator.isNull(name)) {
				name = fileName;
			}

			IGImage image = IGImageServiceUtil.addImage(folderId, name, description, file, contentType, serviceContext);

			AssetPublisherUtil.addAndStoreSelection(actionRequest, IGImage.class.getName(), image.getImageId(), -1);
			MetadataActionUtil.addMetadata(image, actionRequest);
		} else {

			// Update image

			if (Validator.isNull(fileName)) {
				file = null;
			}

			IGImageServiceUtil.updateImage(imageId, folderId, name, description, file, contentType, serviceContext);
		}

		AssetPublisherUtil.addRecentFolderId(actionRequest, IGImage.class.getName(), folderId);
	}

}