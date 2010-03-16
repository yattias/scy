package com.ext.portlet.similarity.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.liferay.util.TagActionUtil;

/**
 * This class fire after user pressed the taglib ui asset_ownags_summary. It
 * delete one tag from user. The user can only delete his own tags.
 * 
 * @author Daniel
 * 
 */
public class DeleteTagEntryAction extends ViewSimilarityAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {
		long entryId = Long.valueOf(req.getParameter("entryId"));
		long tagId = Long.valueOf(req.getParameter("tagId"));
		String redirect = req.getParameter("redirect");

		TagActionUtil.deleteTagEntry(req, entryId, tagId);

		res.sendRedirect(redirect);
	}

}
