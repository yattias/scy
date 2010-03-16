package com.ext.portlet.freestyler.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.imagegallery.DuplicateImageNameException;
import com.liferay.portlet.imagegallery.ImageNameException;
import com.liferay.portlet.imagegallery.ImageSizeException;
import com.liferay.portlet.imagegallery.NoSuchFolderException;
import com.liferay.portlet.imagegallery.NoSuchImageException;
import com.liferay.portlet.tags.TagsEntryException;
import com.liferay.util.LinkToolUtil;

/**
 * This class is to edit freestylerEntry. The name, descption and tags can be
 * change, or delete the complete composition with FreestylerEntry,
 * FreestylerImages, FreestylerFolder, xml DLFileEnty and xml DLFolder.
 * 
 * @author Daniel
 * 
 */
public class EditFreestylerEntryAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.UPDATE)) {
				updateFreestyler(actionRequest);
			} else if (cmd.equals(Constants.DELETE)) {
				deleteFreestyler(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		} catch (Exception e) {
			if (e instanceof NoSuchImageException || e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass().getName());

				setForward(actionRequest, "portlet.ext.freestyler.error");
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
		long entryId = Long.valueOf(renderRequest.getParameter("entryId"));
		FreestylerEntry entry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(entryId);
		renderRequest.setAttribute("freestyler_entry", entry);
		String forward = "portlet.ext.freestyler.edit_free";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	/**
	 * Delete complete freestyler composition which was created after added the
	 * freestyler zip file.
	 * 
	 * @param actionRequest
	 * @throws Exception
	 */
	protected void deleteFreestyler(ActionRequest actionRequest) throws Exception {
		long entryId = ParamUtil.getLong(actionRequest, "entryId");
		FreestylerEntryLocalServiceUtil.deleteEntry(entryId);

		LinkToolUtil.deleteEntry(entryId);

	}

	/**
	 * Update name, description and/or tags for given FreestylerEntry.
	 * 
	 * @param actionRequest
	 * @throws Exception
	 */
	protected void updateFreestyler(ActionRequest actionRequest) throws Exception {

		long entryId = ParamUtil.getLong(actionRequest, "entryId");
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(FreestylerEntry.class.getName(), actionRequest);
		User user = UserLocalServiceUtil.getUser(serviceContext.getUserId());
		Long userId = user.getUserId();

		FreestylerEntryLocalServiceUtil.updateFreestylerEntry(userId, entryId, name, description, serviceContext);

	}
}