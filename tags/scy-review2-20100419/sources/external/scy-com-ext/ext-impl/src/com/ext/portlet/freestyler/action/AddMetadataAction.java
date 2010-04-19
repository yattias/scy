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
import com.ext.portlet.metadata.model.MetadataEntry;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.MetadataActionUtil;

/**
 * This class is fire if user pressed the taglib ui add own meta. It is to edit
 * existed metadata to an tagsAsset entry. After finished or cancel the page is
 * redirect to previews view.
 * 
 * @author Daniel
 * 
 */
public class AddMetadataAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		long entryId = Long.valueOf(req.getParameter("entryId"));
		String className = req.getParameter("className");
		String cmd = req.getParameter("cmd");
		String struts_action = req.getParameter("struts_action");
		String redirect = req.getParameter("redirect");

		FreestylerImage entry = FreestylerImageLocalServiceUtil.getFreestylerImage(entryId);
		TagsAsset tagsAssstEntry = TagsAssetLocalServiceUtil.getAsset(className, entryId);

		int actualImageEntry = 0;

		List<FreestylerImage> listImages = FreestylerEntryLocalServiceUtil.getFreestylerImages(entry.getFreestylerId());
		for (FreestylerImage freestylerImage : listImages) {
			if (freestylerImage.getImageId() < entryId) {
				actualImageEntry++;
			}
		}

		if (cmd == null) {
			cmd = "";
		}

		if (cmd.equals(Constants.VIEW)) {
			req.setAttribute("entryId", entryId);
			req.setAttribute("className", className);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("assetEntry:view_addMeta.jsp", tagsAssstEntry);
			req.setAttribute("isAddMeta", true);
			req.setAttribute("redirect", redirect);
			MetadataEntry metaEntry = MetadataActionUtil.getMetadata(className, entryId);

			if (metaEntry == null) {
				metaEntry = MetadataActionUtil.addMetadata(className, entryId, req);
			}
			req.setAttribute("meta", metaEntry);
			req.setAttribute("freestylerEntryId", entry.getFreestylerId());
			req.setAttribute("actualImageEntry", actualImageEntry);
			req.setAttribute("redirect", redirect);
			setForward(req, "portlet.ext.freestyler.view_addMetadata");
			return;
		}
		// add methods
		MetadataActionUtil.updateMetadata(req);

		res.sendRedirect(redirect);
	}
}
