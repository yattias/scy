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

package com.liferay.portlet.documentlibrary.action;

import java.io.File;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.documentlibrary.DuplicateFileException;
import com.liferay.documentlibrary.FileNameException;
import com.liferay.documentlibrary.FileSizeException;
import com.liferay.documentlibrary.SourceFileNameException;
import com.liferay.lock.DuplicateLockException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.form.FileEntryForm;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryServiceUtil;
import com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission;
import com.liferay.portlet.documentlibrary.service.permission.DLFolderPermission;
import com.liferay.portlet.tags.TagsEntryException;
import com.liferay.util.MetadataActionUtil;

/**
 * Extended this class in updateFileEntry method. After create/update new
 * resource also new metadata entry would be create. After delete also metadata
 * will be delete.
 * 
 * @author Daniel
 * 
 */
public class EditFileEntryAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		FileEntryForm fileEntryForm = (FileEntryForm) form;

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateFileEntry(fileEntryForm, actionRequest, actionResponse);
			} else if (cmd.equals(Constants.DELETE)) {
				deleteFileEntry(actionRequest);
			} else if (cmd.equals(Constants.LOCK)) {
				lockFileEntry(actionRequest);
			} else if (cmd.equals(Constants.UNLOCK)) {
				unlockFileEntry(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		} catch (Exception e) {
			if (e instanceof DuplicateLockException || e instanceof NoSuchFileEntryException || e instanceof PrincipalException) {

				if (e instanceof DuplicateLockException) {
					DuplicateLockException dle = (DuplicateLockException) e;

					SessionErrors.add(actionRequest, dle.getClass().getName(), dle.getLock());
				} else {
					SessionErrors.add(actionRequest, e.getClass().getName());
				}

				setForward(actionRequest, "portlet.document_library.error");
			} else if (e instanceof DuplicateFileException || e instanceof DuplicateFolderNameException || e instanceof FileNameException
					|| e instanceof FileSizeException || e instanceof NoSuchFolderException || e instanceof SourceFileNameException) {

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
			ActionUtil.getFileEntry(renderRequest);
		} catch (Exception e) {
			if (e instanceof NoSuchFileEntryException || e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass().getName());

				return mapping.findForward("portlet.document_library.error");
			} else {
				throw e;
			}
		}

		String forward = "portlet.document_library.edit_file_entry";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	protected void deleteFileEntry(ActionRequest actionRequest) throws Exception {

		long folderId = ParamUtil.getLong(actionRequest, "folderId");
		String name = ParamUtil.getString(actionRequest, "name");
		double version = ParamUtil.getDouble(actionRequest, "version");

		DLFileEntry entry = DLFileEntryLocalServiceUtil.getFileEntry(folderId, name);
		DLFileEntryServiceUtil.deleteFileEntry(folderId, name, version);

		// delete metadata
		MetadataActionUtil.deleteMetadata(entry.getFileEntryId());
	}

	protected void lockFileEntry(ActionRequest actionRequest) throws Exception {
		long folderId = ParamUtil.getLong(actionRequest, "folderId");
		String name = ParamUtil.getString(actionRequest, "name");

		DLFileEntryServiceUtil.lockFileEntry(folderId, name);
	}

	protected void unlockFileEntry(ActionRequest actionRequest) throws Exception {

		long folderId = ParamUtil.getLong(actionRequest, "folderId");
		String name = ParamUtil.getString(actionRequest, "name");

		DLFileEntryServiceUtil.unlockFileEntry(folderId, name);
	}

	protected void updateFileEntry(FileEntryForm fileEntryForm, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);

		String cmd = ParamUtil.getString(uploadRequest, Constants.CMD);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		long folderId = ParamUtil.getLong(uploadRequest, "folderId");
		long newFolderId = ParamUtil.getLong(uploadRequest, "newFolderId");
		String name = ParamUtil.getString(uploadRequest, "name");
		String sourceFileName = uploadRequest.getFileName("file");

		String title = ParamUtil.getString(uploadRequest, "title");
		String description = ParamUtil.getString(uploadRequest, "description");

		String extraSettings = PropertiesUtil.toString(fileEntryForm.getExtraSettingsProperties());

		File file = uploadRequest.getFile("file");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFileEntry.class.getName(), actionRequest);

		if (cmd.equals(Constants.ADD)) {

			// Add file entry

			DLFolderPermission.check(themeDisplay.getPermissionChecker(), folderId, ActionKeys.ADD_DOCUMENT);

			DLFileEntry entry = DLFileEntryLocalServiceUtil.addFileEntry(themeDisplay.getUserId(), folderId, sourceFileName, title, description, extraSettings,
					file, serviceContext);

			MetadataActionUtil.addMetadata(DLFileEntry.class.getName(), entry.getFileEntryId(), actionRequest);

			AssetPublisherUtil.addAndStoreSelection(actionRequest, DLFileEntry.class.getName(), entry.getFileEntryId(), -1);
		} else {

			// Update file entry

			DLFileEntryPermission.check(themeDisplay.getPermissionChecker(), folderId, name, ActionKeys.UPDATE);

			DLFileEntryLocalServiceUtil.updateFileEntry(themeDisplay.getUserId(), folderId, newFolderId, name, sourceFileName, title, description,
					extraSettings, file, serviceContext);
		}

		AssetPublisherUtil.addRecentFolderId(actionRequest, DLFileEntry.class.getName(), folderId);
	}

}