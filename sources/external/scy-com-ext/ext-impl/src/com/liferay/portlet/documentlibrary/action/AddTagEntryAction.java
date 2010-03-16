package com.liferay.portlet.documentlibrary.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.TagActionUtil;

/**
 * This class adds new tags to an exist tagsAsset. It is fire after pressing the
 * the taglib ui add own tags. Afer the user enter his tags and press save
 * button, this class persist the tags.
 * 
 * @author Daniel
 * 
 */

public class AddTagEntryAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		long entryId = Long.valueOf(req.getParameter("entryId"));
		String cmd = req.getParameter("cmd");
		String className = req.getParameter("className");
		String struts_action = req.getParameter("struts_action");
		String redirect = req.getParameter("redirect");

		TagsAsset tagsAssstentry = TagsAssetLocalServiceUtil.getAsset(className, entryId);

		if (cmd.equals(Constants.VIEW)) {
			req.setAttribute("entryId", entryId);
			req.setAttribute("assetEntry:view_addTag.jsp", tagsAssstentry);
			req.setAttribute("className", className);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("redirect", redirect);
			setForward(req, "portlet.document_library.view_addTag");
			return;
		}

		TagActionUtil.addNewTags(req, tagsAssstentry.getAssetId());

		res.sendRedirect(redirect);
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return mapping.findForward(getForward(renderRequest, "portlet.document_library.edit_file_entry"));
	}

}
