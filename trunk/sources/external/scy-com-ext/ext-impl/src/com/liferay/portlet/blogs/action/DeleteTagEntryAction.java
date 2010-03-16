package com.liferay.portlet.blogs.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.struts.PortletAction;
import com.liferay.util.TagActionUtil;

/**
 * This class fire after user pressed the taglib ui asset_ownags_summary. It
 * delete the TagsProerty for given user and tag. If it the only property for
 * this tag the TagsEntry and relationship to TagsAsset will also delete.
 * 
 * @author Daniel
 * 
 */
public class DeleteTagEntryAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {
		long entryId = Long.valueOf(req.getParameter("entryId"));
		long tagId = Long.valueOf(req.getParameter("tagId"));
		String redirect = req.getParameter("redirect");

		TagActionUtil.deleteTagEntry(req, entryId, tagId);

		res.sendRedirect(redirect);

	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return mapping.findForward(getForward(renderRequest, "portlet.blogs.view"));
	}

}
