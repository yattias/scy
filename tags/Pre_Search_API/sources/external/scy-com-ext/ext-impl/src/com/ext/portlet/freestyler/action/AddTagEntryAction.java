package com.ext.portlet.freestyler.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.TagActionUtil;

/**
 * This class adds new tags to an exist tagsAsset. It is fire after pressing the the taglib ui add own tags. 
 * Afer the user enter his tags and press save button, this class persist the tags.
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

		TagsAsset entry = TagsAssetLocalServiceUtil.getAsset(className, entryId);

		FreestylerImage fi = FreestylerImageLocalServiceUtil.getFreestylerImage(entryId);

		if (cmd.equals(Constants.VIEW)) {
			// set Attributes for render page
			req.setAttribute("assetEntry:view_addTag.jsp", entry);
			req.setAttribute("className", className);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("isAddTag", true);
			req.setAttribute("redirect", redirect);
			setForward(req, "portlet.ext.freestyler.view_addTag");
			return;
		}

		TagActionUtil.addNewTags(req, entry.getAssetId());
		int actualImageEntry = 0;

		List<FreestylerImage> listImages = FreestylerEntryLocalServiceUtil.getFreestylerImages(fi.getFreestylerId());
		for (FreestylerImage freestylerImage : listImages) {
			if (freestylerImage.getImageId() < entryId) {
				actualImageEntry++;
			}
		}

		res.sendRedirect(redirect);
	}
}
