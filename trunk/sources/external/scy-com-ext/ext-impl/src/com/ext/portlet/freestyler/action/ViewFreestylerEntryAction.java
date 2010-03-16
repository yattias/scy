package com.ext.portlet.freestyler.action;

import java.util.List;

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
import com.liferay.portal.struts.PortletAction;

/**
 * This class collect all needed data to show all freestyler entries at the
 * start page from the freestyler portlet.
 * 
 * @author Daniel
 * 
 */
public class ViewFreestylerEntryAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		setForward(req, "portlet.ext.freestyler.view_freestyler");
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		String freestylerEntryId = renderRequest.getParameter("freestylerEntryId");
		if (freestylerEntryId == null && renderRequest.getAttribute("freestylerEntryId") == null && renderRequest.getAttribute("isAddTag") != null) {
			return mapping.findForward("portlet.ext.freestyler.view_addTag");
		}

		if (freestylerEntryId == null && renderRequest.getAttribute("isAddMeta") != null) {
			return mapping.findForward("portlet.ext.freestyler.view_addMetadata");
		}

		Long entryId;
		if (renderRequest.getParameter("freestylerEntryId") != null) {
			entryId = Long.valueOf(renderRequest.getParameter("freestylerEntryId"));
		} else {
			entryId = (Long) renderRequest.getAttribute("freestylerEntryId");
		}
		int actualImageEntry;
		if (renderRequest.getParameter("actualImageEntry") != null) {
			actualImageEntry = Integer.valueOf(renderRequest.getParameter("actualImageEntry"));
		} else {
			actualImageEntry = 0;
		}

		if (renderRequest.getAttribute("actualImageEntry") != null) {
			actualImageEntry = (Integer) renderRequest.getAttribute("actualImageEntry");
		}

		String redirect = renderRequest.getParameter("redirect");
		String nav = renderRequest.getParameter("nav");

		if (nav == null) {
			nav = "";
		}

		FreestylerEntry freestylerEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(entryId);
		List<FreestylerImage> listFreeImages = FreestylerEntryLocalServiceUtil.getFreestylerImages(entryId);

		if (nav.equals("prev") && actualImageEntry > 0) {
			actualImageEntry--;
		} else if (nav.equals("next") && actualImageEntry < (listFreeImages.size() - 1)) {
			actualImageEntry++;
		}

		Boolean windowStateMax;
		// variable for different views table or full content
		if (renderRequest.getWindowState().equals(WindowState.MAXIMIZED)) {
			windowStateMax = true;
		} else {
			windowStateMax = false;
		}

		renderRequest.setAttribute("windowStateMax", windowStateMax);
		renderRequest.setAttribute("freestyler_entry", freestylerEntry);
		renderRequest.setAttribute("actualImageEntry", actualImageEntry);
		renderRequest.setAttribute("listFreeImages", listFreeImages);
		renderRequest.setAttribute("redirect", redirect);

		return mapping.findForward("portlet.ext.freestyler.view_entry");

	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;
}
