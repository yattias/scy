package com.ext.portlet.freestyler.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;

/**
 * Collect all data for user chosen freestyler entry.
 * 
 * @author Daniel
 * 
 */
public class ViewFreestylerImageAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		setForward(req, "portlet.ext.freestyler.view_freestylerImage");
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		Long entryId;
		if (renderRequest.getParameter("freestylerEntryId") != null) {
			entryId = Long.valueOf(renderRequest.getParameter("freestylerEntryId"));
		} else {
			entryId = 0l;
		}

		Long actualImageId;
		if (renderRequest.getParameter("actualImageId") != null) {
			actualImageId = Long.valueOf(renderRequest.getParameter("actualImageId"));
		} else {
			actualImageId = 0l;
		}

		String redirect = renderRequest.getParameter("redirect");

		FreestylerEntry freestylerEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(entryId);
		FreestylerImage freestylerImage = FreestylerImageLocalServiceUtil.getFreestylerImage(actualImageId);

		Boolean windowStateMax;
		// variable for different views table or full content
		if (renderRequest.getWindowState().equals(WindowState.MAXIMIZED)) {
			windowStateMax = true;
		} else {
			windowStateMax = false;
		}

		renderRequest.setAttribute("windowStateMax", windowStateMax);
		renderRequest.setAttribute("freestyler_entry", freestylerEntry);
		renderRequest.setAttribute("actualImage", freestylerImage);
		renderRequest.setAttribute("redirect", redirect);

		return mapping.findForward("portlet.ext.freestyler.view_freestylerImage");

	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;
}
