package com.liferay.portlet.imagegallery.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.resourcehandling.RequestUpdater;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * This class is for add new link from viewed resource to a choose intern
 * resource. It is fire after press the browse button at the add_link taglib. It
 * collect all needed data to show the user the intern resource. After chose
 * some explicit resource and press the add link button a new link whould be
 * create for viewed resource to intern and to the other way.
 * 
 * @author daniel
 * 
 */
public class LinkInternAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		Long entryId = Long.valueOf(req.getParameter("entryId"));
		String redirect = req.getParameter("redirect");
		String cmd = req.getParameter(Constants.CMD);
		String sType = req.getParameter("sType");
		String linkedResource = req.getParameter("radio");
		String classNameStartResource = req.getParameter("classPK");
		String previewEntryId = req.getParameter("previewEntryId");

		LinkToolUtil util = new LinkToolUtil();
		String checkedResource = util.checkCheckedResource(previewEntryId);

		RequestUpdater requestUpdater = RequestUpdater.getResourceAdapterBuilder();
		requestUpdater.updateResourceView(req);

		IGImage entry = IGImageLocalServiceUtil.getIGImage(entryId);

		if (sType != null && cmd.equals("addLink") && linkedResource != null && !linkedResource.equals(String.valueOf(entryId))) {
			classNameStartResource = util.getStartResourceClassName(sType, Long.valueOf(linkedResource));
			ServiceContext serviceContext = ServiceContextFactory.getInstance(classNameStartResource, req);
			System.out.println("Start add new link for resource with Id: " + linkedResource);
			util.addLink(String.valueOf(entryId), linkedResource, String.valueOf(ClassNameLocalServiceUtil.getClassNameId(classNameStartResource)),
					serviceContext);
			util.addLink(linkedResource, String.valueOf(entryId), String.valueOf(ClassNameLocalServiceUtil.getClassNameId(IGImage.class.getName())),
					serviceContext);
		} else {
			req.setAttribute("checkedResource", checkedResource);
			req.setAttribute(WebKeys.IMAGE_GALLERY_IMAGE, entry);
			req.setAttribute(WebKeys.IMAGE_GALLERY_FOLDER, entry.getFolder());
			setForward(req, "portlet.image_gallery.view_linkIntern");
			return;
		}
		// set Forward
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		redirect = themeDisplay.getURLHome();
		res.sendRedirect(redirect);

	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return mapping.findForward(getForward(renderRequest, "portlet.image_gallery.view_link"));
	}
}