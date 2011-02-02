package com.ext.portlet.freestyler.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.ext.portlet.freestyler.service.persistence.FreestylerEntryUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.ContentTypeUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.DuplicateImageNameException;
import com.liferay.portlet.imagegallery.ImageNameException;
import com.liferay.portlet.imagegallery.ImageSizeException;
import com.liferay.portlet.imagegallery.NoSuchFolderException;
import com.liferay.portlet.imagegallery.NoSuchImageException;
import com.liferay.portlet.imagegallery.action.ActionUtil;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGImageServiceUtil;
import com.liferay.portlet.tags.TagsEntryException;
import com.liferay.util.MetadataActionUtil;

/**
 * This class is to add new freestyler documents to the portal. The freestyler
 * document must be zip with his images and xml file to add it to system. The
 * processAction is fire after pressing the add freestyler button.
 * 
 * @author Daniel
 * 
 */
public class AddFreestylerAction extends PortletAction {

	/**
	 * Add new freestyler document or delete som exist if user has the needed
	 * permission.
	 */
	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {
			if (PortletPermissionUtil.contains(getPermissionChecker(), themeDisplay.getPlid(), "freestyler", "ADD_FREESTYLER") && cmd.equals(Constants.ADD)
					|| cmd.equals(Constants.UPDATE)) {
				updateImage(actionRequest);
			} else if (PortletPermissionUtil.contains(getPermissionChecker(), themeDisplay.getPlid(), "freestyler", "ADD_FREESTYLER")
					&& cmd.equals(Constants.DELETE)) {
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

		if (renderRequest.getParameter("entryId") != null) {
			Long entryId = Long.valueOf(renderRequest.getParameter("entryId"));
			FreestylerEntry entry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(entryId);
			renderRequest.setAttribute("freestyler_entry", entry);
		}
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

		String forward = "portlet.ext.freestyler.add_free";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	/**
	 * Get content typer from file.
	 * 
	 * @param uploadRequest
	 * @param file
	 *            uploaded file
	 * @return
	 */
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

	/**
	 * Add new freestyler composition to portal. Extract the image and xml files
	 * from zip file and add to lists. Add new FreestylerFolder, the images as
	 * freestylerImage and TagsAsset entries, new FreestylerEntry, new folder
	 * for xml file and new document with xml file as content.
	 * 
	 * @param actionRequest
	 * @throws Exception
	 */
	protected void updateImage(ActionRequest actionRequest) throws Exception {
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);

		Date now = new Date();
		long imageId = ParamUtil.getLong(uploadRequest, "imageId");

		long folderId = ParamUtil.getLong(uploadRequest, "folderId");
		String name = ParamUtil.getString(uploadRequest, "name");
		String fileName = uploadRequest.getFileName("file");
		String description = ParamUtil.getString(uploadRequest, "description", fileName);

		File file = uploadRequest.getFile("file");
		String contentType = getContentType(uploadRequest, file);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Long userId = themeDisplay.getUserId();

		if (contentType.equals("application/zip")) {
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

			ZipFile zipFile = new ZipFile(file);

			Enumeration<?> entries = zipFile.entries();

			byte[] buffer = new byte[16384];
			int len;

			List<File> imageList = new ArrayList<File>();
			List<File> xmlList = new ArrayList<File>();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String entryFileName = entry.getName();

				if (!entry.isDirectory()) {

					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(
							FileUtil.getPath(file.getAbsolutePath()) + File.separator, entryFileName)));

					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					while ((len = bis.read(buffer)) > 0) {
						bos.write(buffer, 0, len);
					}

					bos.flush();
					bos.close();
					bis.close();

					File zipFileEntry = new File(FileUtil.getPath(file.getAbsolutePath()) + File.separator + entryFileName);
					System.out.println(zipFileEntry.length());
					String fileZipName = zipFileEntry.getName();
					String zipFileEntryExt = zipFileEntry.getName().substring(fileZipName.length() - 3, fileZipName.length());

					if (zipFileEntry.length() > 0 && zipFileEntry.isFile() && zipFileEntryExt.equals("jpg")) {
						imageList.add(zipFileEntry);
					} else if (zipFileEntry.length() > 0 && zipFileEntry.isFile() && zipFileEntryExt.equals("png")) {
						imageList.add(zipFileEntry);
					} else if (zipFileEntry.length() > 0 && zipFileEntry.isFile() && zipFileEntryExt.equals("xml")) {
						xmlList.add(zipFileEntry);
					}

				}
			}

			if (imageList.size() > 0 && xmlList.size() > 0) {

				// create folder for images
				folderId = createImageFolder(now, name, description, themeDisplay, userId);

				// parent folder for all freestyler original file and xml files
				DLFolder parantFolder = getParentFolder(now, themeDisplay, userId);

				// create folder for freestyler xml
				DLFolder xmlFolder = createFolderForXML(now, name, description, themeDisplay, userId, parantFolder);

				// add new freestyler enty
				FreestylerEntry entry = FreestylerEntryLocalServiceUtil.addFreestylerEntry(userId, name, description, serviceContext, themeDisplay);

				Long xmlFileId = saveXML(name, description, userId, serviceContext, xmlList, xmlFolder, actionRequest);

				entry.setXmlFileId(xmlFileId);
				FreestylerEntryLocalServiceUtil.updateFreestylerEntry(entry);

				// add images in zip file to db as tagsAssets and
				// freestylerImages
				saveImages(folderId, description, userId, serviceContext, imageList, entry, actionRequest);

			}

		} else {

			// Update files ...

			if (Validator.isNull(fileName)) {
				file = null;
			}

			// IGImageServiceUtil.updateImage(imageId, folderId, name,
			// description, file, contentType, serviceContext);
		}

	}

	/**
	 * Save imagesList as new FreestylerEntries and TagsAsset entries.
	 * 
	 * @param folderId
	 *            created FreestylerFolder id.
	 * @param description
	 *            user entered description.
	 * @param userId
	 *            id from user who upload file.
	 * @param serviceContext
	 * @param imageList
	 *            the images from zip file.
	 * @param entry
	 *            created FreestylerEntry.
	 * @throws Exception
	 */
	private void saveImages(long folderId, String description, Long userId, ServiceContext serviceContext, List<File> imageList, FreestylerEntry entry,
			ActionRequest req) throws Exception {
		for (File image : imageList) {
			java.net.FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String imageContentType = fileNameMap.getContentTypeFor(image.getName());

			FreestylerImage imageNew = FreestylerImageLocalServiceUtil.addImage(userId, folderId, image.getName(), description, image, imageContentType,
					serviceContext, entry);

			FreestylerEntryUtil.addFreestylerImage(entry.getFreestylerId(), imageNew);
			MetadataActionUtil.addMetadata(FreestylerImage.class.getName(), imageNew.getImageId(), req);
		}
	}

	/**
	 * Save the xml file from zip file as DlFileEntry and TagsAsset.
	 * 
	 * @param name
	 *            user entered name.
	 * @param description
	 *            user entered description.
	 * @param userId
	 *            id from user who upload file.
	 * @param serviceContext
	 * @param xmlList
	 *            the xml files from zip file.
	 * @param xmlFolder
	 *            the created DLFolder.
	 * @return id from DLFileEntry
	 * @throws Exception
	 */
	private Long saveXML(String name, String description, Long userId, ServiceContext serviceContext, List<File> xmlList, DLFolder xmlFolder, ActionRequest req)
			throws Exception {
		Long xmlFileId = 0l;
		for (File xml : xmlList) {
			String extraSettings = "";
			DLFileEntry dlFileEntry = FreestylerEntryLocalServiceUtil.addFileEntry(userId, xmlFolder.getFolderId(), xml.getName(), name, description,
					extraSettings, xml, serviceContext);
			xmlFileId = dlFileEntry.getFileEntryId();
			MetadataActionUtil.addMetadata(DLFileEntry.class.getName(), xmlFileId, req);
		}
		return xmlFileId;
	}

	/**
	 * Created folder for xml file from zip file upload.
	 * 
	 * @param now
	 *            current date time.
	 * @param name
	 *            user entered name for freestyler composition
	 * @param description
	 *            user entered description for freestyler composition
	 * @param themeDisplay
	 * @param userId
	 *            the id from user who upload the file.
	 * @param parentFolder
	 *            the parent folder for all freestyler xml files.
	 * @return created DLFolder for xml file.
	 * @throws SystemException
	 * @throws PortalException
	 */
	private DLFolder createFolderForXML(Date now, String name, String description, ThemeDisplay themeDisplay, Long userId, DLFolder parentFolder)
			throws SystemException, PortalException {
		long xmlFolderId = FreestylerEntryLocalServiceUtil.getNewId();
		DLFolder xmlFolder;

		try {
			xmlFolder = DLFolderLocalServiceUtil.createDLFolder(xmlFolderId);
			xmlFolder.setCreateDate(now);
			xmlFolder.setModifiedDate(now);
			xmlFolder.setCompanyId(themeDisplay.getCompanyId());
			xmlFolder.setGroupId(themeDisplay.getScopeGroupId());
			xmlFolder.setName(name);
			xmlFolder.setDescription(description);
			xmlFolder.setUserId(userId);
			xmlFolder.setParentFolderId(parentFolder.getFolderId());
			DLFolderLocalServiceUtil.addDLFolder(xmlFolder);

		} catch (Exception e) {
			xmlFolder = DLFolderLocalServiceUtil.getFolder(themeDisplay.getScopeGroupId(), parentFolder.getFolderId(), name);
		}

		return xmlFolder;
	}

	/**
	 * Create parent DLFolder if does not exist for freestyler xml files.
	 * 
	 * @param now
	 * @param themeDisplay
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	private DLFolder getParentFolder(Date now, ThemeDisplay themeDisplay, Long userId) throws SystemException {
		DLFolder parantFolder;
		try {
			parantFolder = DLFolderLocalServiceUtil.getFolder(themeDisplay.getScopeGroupId(), 0, "freestyler");
		} catch (Exception e) {
			long parentFolderId = FreestylerEntryLocalServiceUtil.getNewId();
			parantFolder = DLFolderLocalServiceUtil.createDLFolder(parentFolderId);
			parantFolder.setCreateDate(now);
			parantFolder.setModifiedDate(now);
			parantFolder.setCompanyId(themeDisplay.getCompanyId());
			parantFolder.setGroupId(themeDisplay.getScopeGroupId());
			parantFolder.setName("freestyler");
			parantFolder.setDescription("All Freestyler files");
			parantFolder.setUserId(userId);
			DLFolderLocalServiceUtil.addDLFolder(parantFolder);
		}
		return parantFolder;
	}

	/**
	 * Create new freestylerFolder for uploaded zip file.
	 * 
	 * @param now
	 *            current date.
	 * @param name
	 *            user entered name.
	 * @param description
	 *            user entered desciption.
	 * @param themeDisplay
	 * @param userId
	 *            the id from user who upload the freestyler zip composition.
	 * @return the id from created folder.
	 * @throws SystemException
	 */
	private long createImageFolder(Date now, String name, String description, ThemeDisplay themeDisplay, Long userId) throws SystemException {
		long folderId;
		folderId = FreestylerEntryLocalServiceUtil.getNewId();
		FreestylerFolder entryFolder = FreestylerFolderLocalServiceUtil.createFreestylerFolder(folderId);
		entryFolder.setCreateDate(now);
		entryFolder.setModifiedDate(now);
		entryFolder.setCompanyId(themeDisplay.getCompanyId());
		entryFolder.setGroupId(themeDisplay.getScopeGroupId());
		entryFolder.setName(name);
		entryFolder.setDescription(description);
		entryFolder.setUserId(userId);
		FreestylerFolderLocalServiceUtil.addFreestylerFolder(entryFolder);
		return folderId;
	}

	/**
	 * Delete image and metadata information.
	 * 
	 * @param actionRequest
	 * @throws Exception
	 */
	protected void deleteImage(ActionRequest actionRequest) throws Exception {
		long imageId = ParamUtil.getLong(actionRequest, "imageId");

		IGImageServiceUtil.deleteImage(imageId);
		MetadataActionUtil.deleteMetadata(imageId);
	}

	/*
	 * Initializes new permission checker
	 * 
	 * @return new initialized permission checker.
	 * 
	 * @throws PrincipalException
	 */
	public PermissionChecker getPermissionChecker() throws PrincipalException {
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			throw new PrincipalException("PermissionChecker not initialized");
		}

		return permissionChecker;
	}

}