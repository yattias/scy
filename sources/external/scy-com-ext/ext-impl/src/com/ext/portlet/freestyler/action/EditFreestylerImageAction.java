package com.ext.portlet.freestyler.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
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
 * This class is to edit chosen freestyler image. The entry can only be edited
 * and not delete cause is peace of a complete composition. And only the name,
 * desciption and tag can be individual.
 * 
 * @author Daniel
 * 
 */
public class EditFreestylerImageAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.UPDATE)) {
				updateFreestylerEntry(actionRequest);
			} else if (cmd.equals(Constants.DELETE)) {
				deleteFreestylerEntry(actionRequest);
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
		FreestylerImage entry = FreestylerImageLocalServiceUtil.getFreestylerImage(entryId);
		renderRequest.setAttribute("freestylerImage_entry", entry);
		String forward = "portlet.ext.freestyler.edit_entry";

		return mapping.findForward(getForward(renderRequest, forward));
	}

	protected void deleteFreestylerEntry(ActionRequest actionRequest) throws Exception {
		long entryId = ParamUtil.getLong(actionRequest, "entryId");
		FreestylerEntryLocalServiceUtil.deleteEntry(entryId);

		LinkToolUtil.deleteEntry(entryId);

	}

	protected void updateFreestylerEntry(ActionRequest actionRequest) throws Exception {

		long entryId = ParamUtil.getLong(actionRequest, "entryId");
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(FreestylerImage.class.getName(), actionRequest);
		User user = UserLocalServiceUtil.getUser(serviceContext.getUserId());
		Long userId = user.getUserId();

		FreestylerImageLocalServiceUtil.updateFreestylerImage(userId, entryId, name, description, serviceContext);

	}
}